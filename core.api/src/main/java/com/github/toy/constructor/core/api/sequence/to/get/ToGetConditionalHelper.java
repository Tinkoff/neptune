package com.github.toy.constructor.core.api.sequence.to.get;

import java.util.Collection;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.StreamSupport;

import static com.github.toy.constructor.core.api.story.StoryWriter.toGet;
import static java.lang.String.format;
import static java.util.Arrays.asList;
import static java.util.stream.StreamSupport.stream;

/**
 * This is the util which helps to crate function with given condition.
 */
public final class ToGetConditionalHelper {

    private ToGetConditionalHelper() {
        super();
    }

    /**
     * This method returns a function. The result function returns a single first found value from {@link Iterable}.
     * The original function should return iterable to match.
     *
     * @param description of a value which should be returned
     * @param function which should return {@link Iterable}
     * @param condition which is used to find some target value
     * @param checkConditionInParallel is how iterable should be matched. If {@code true} when each value will be
     *                                 checked in parallel.
     * @param ignoreExceptionOnConditionCheck is used to define what should be done when check is failed
     *                                        and some exception is thrown. Exception will be thrown when
     *                                        {@code true}.
     * @param <T> is a type of input value
     * @param <R> is a type of target value
     * @param <V> is a type of {@link Iterable} of {@code R}
     * @return a function. The result function returns a single first found value from {@link Iterable}.
     */
    public static <T, R, V extends Iterable<R>> Function<T, R> getFromIterable(String description,
                                                                             Function<T, V> function,
                                                                             Predicate<R> condition,
                                                                             boolean checkConditionInParallel,
                                                                             boolean ignoreExceptionOnConditionCheck) {
        return toGet(format("%s on condition %s", description, condition), t -> {
            V v = function.apply(t);
            return stream(v.spliterator(), checkConditionInParallel).filter(r -> {
                try {
                    return condition.test(r);
                }
                catch (Throwable t1) {
                    String message = format("%s was caught. Meassage: %s", t1.getClass(), t1.getMessage());
                    if (!ignoreExceptionOnConditionCheck) {
                        throw new CheckConditionException(message, t1);
                    }

                    System.out.println(message);
                    t1.printStackTrace();
                    return false;
                }
            }).findFirst().orElse(null);
        });
    }

    /**
     * This method returns a function. The result function returns a single first found value from array.
     * The original function should return array to match.
     *
     * @param description of a value which should be returned.
     * @param function which should return an array.
     * @param condition which is used to find some target value.
     * @param checkConditionInParallel is how array should be matched. If {@code true} when each value will be
     *                                 checked in parallel.
     * @param ignoreExceptionOnConditionCheck is used to define what should be done when check is failed
     *                                        and some exception is thrown. Exception will be thrown when
     *                                        {@code true}.
     * @param <T> is a type of input value
     * @param <R> is a type of target value
     * @return a function. The result function returns a single first found value from array.
     */
    public static <T, R> Function<T, R> getFromArray(String description,
                                                     Function<T, R[]> function,
                                                     Predicate<R> condition,
                                                     boolean checkConditionInParallel,
                                                     boolean ignoreExceptionOnConditionCheck) {
        return getFromIterable(description, (Function<T, Iterable<R>>) t -> asList(function.apply(t)),
                condition, checkConditionInParallel, ignoreExceptionOnConditionCheck);

    }

    private static class CheckConditionException extends RuntimeException {
        CheckConditionException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}
