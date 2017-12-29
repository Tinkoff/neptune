package com.github.toy.constructor.core.api;

import com.google.common.collect.Iterables;
import org.apache.commons.lang3.ArrayUtils;

import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

import static com.github.toy.constructor.core.api.StoryWriter.toGet;
import static java.lang.String.format;
import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;
import static java.util.stream.StreamSupport.stream;

/**
 * This is the util which helps to crate function with given condition.
 */
public final class ToGetConditionalHelper {

    private ToGetConditionalHelper() {
        super();
    }

    private static boolean returnFalseOrThrowException(Throwable t, boolean ignoreExceptionOnConditionCheck) {
        String message = format("%s was caught. Meassage: %s", t.getClass(), t.getMessage());
        if (!ignoreExceptionOnConditionCheck) {
            throw new CheckConditionException(message, t);
        }

        System.out.println(message);
        t.printStackTrace();
        return false;
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
     * @param <R> is a type of the target value
     * @param <V> is a type of {@link Iterable} of {@code R}
     * @return a function. The result function returns a single first found value from {@link Iterable}.
     * The result function will return some value if something is found. {@code null} will be returned otherwise.
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
                    return returnFalseOrThrowException(t1, ignoreExceptionOnConditionCheck);
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
     * @param <R> is a type of the target value
     * @return a function. The result function returns a single first found value from array.
     * The result function will return some value if something is found. {@code null} will be returned otherwise.
     */
    public static <T, R> Function<T, R> getFromArray(String description,
                                                     Function<T, R[]> function,
                                                     Predicate<R> condition,
                                                     boolean checkConditionInParallel,
                                                     boolean ignoreExceptionOnConditionCheck) {
        return getFromIterable(description, t -> asList(function.apply(t)),
                condition, checkConditionInParallel, ignoreExceptionOnConditionCheck);

    }

    /**
     * This method returns a function which returns a single value.
     * The original function should return a value to check.
     *
     * @param description of a value which should be returned.
     * @param function which should return a value to check.
     * @param condition which is used to check the target value.
     * @param ignoreExceptionOnConditionCheck is used to define what should be done when check is failed
     *                                        and some exception is thrown. Exception will be thrown when
     *                                        {@code true}.
     * @param <T> is a type of input value
     * @param <R> is a type of the target value
     * @return a function. The result function returns a single checked value.
     * The result function will return some value if the check is passed successfully.
     * {@code null} will be returned otherwise.
     */
    public static <T, R> Function<T, R> getSingleOnCondition(String description,
                                                             Function<T, R> function,
                                                             Predicate<R> condition,
                                                             boolean ignoreExceptionOnConditionCheck) {
        return toGet(format("%s on condition %s", description, condition), t -> {
            R r = function.apply(t);
            try {
                if (condition.test(r)) {
                    return r;
                }
            }
            catch (Throwable t1) {
                returnFalseOrThrowException(t1, ignoreExceptionOnConditionCheck);
            }
            return null;
        });
    }

    /**
     * This method returns a function. The result function returns found values from {@link Iterable}.
     * The original function should return iterable to match.
     *
     * @param description of values that should be returned
     * @param function which should return {@link Iterable}
     * @param condition which is used to find target values
     * @param checkConditionInParallel is how iterable should be matched. If {@code true} when each value will be
     *                                 checked in parallel.
     * @param ignoreExceptionOnConditionCheck is used to define what should be done when check is failed
     *                                        and some exception is thrown. Exception will be thrown when
     *                                        {@code true}.
     * @param <T> is a type of input value
     * @param <R> is a type of target values
     * @param <V> is a type of {@link Iterable} of {@code R}
     * @return a function. The result function returns found values from {@link Iterable}.
     * The result function will return values if something is found. Empty {@link Iterable} will be returned otherwise.
     */
    public static <T, R, V extends Iterable<R>> Function<T, V> getSubIterable(String description,
                                                                              Function<T, V> function,
                                                                              Predicate<R> condition,
                                                                              boolean checkConditionInParallel,
                                                                              boolean ignoreExceptionOnConditionCheck) {
        return toGet(format("%s on condition %s", description, condition), t -> {
            V v = function.apply(t);

            List<R> result = stream(v.spliterator(), checkConditionInParallel).filter(r -> {
                try {
                    return !condition.test(r);
                } catch (Throwable t1) {
                    return !returnFalseOrThrowException(t1, ignoreExceptionOnConditionCheck);
                }
            }).collect(toList());

            Iterables.removeAll(v, result);
            return v;
        });
    }

    /**
     * This method returns a function. The result function returns sub-array of found values from array.
     * The original function should return array to match.
     *
     * @param description of a value which should be returned
     * @param function which should return an array.
     * @param condition which is used to find target values.
     * @param checkConditionInParallel is how array should be matched. If {@code true} when each value will be
     *                                 checked in parallel.
     * @param ignoreExceptionOnConditionCheck is used to define what should be done when check is failed
     *                                        and some exception is thrown. Exception will be thrown when
     *                                        {@code true}.
     * @param <T> is a type of input value
     * @param <R> is a type of target values
     * @return a function. The result function returns sub-array of found values from array.
     * The result function will return values if something is found. Empty array will be returned otherwise.
     */
    public static <T, R> Function<T, R[]> getSubArray(String description,
                                                     Function<T, R[]> function,
                                                     Predicate<R> condition,
                                                     boolean checkConditionInParallel,
                                                     boolean ignoreExceptionOnConditionCheck) {
        return toGet(format("%s on condition %s", description, condition), t -> {
            R[] got = function.apply(t);

            List<R> subResult = stream(asList(got).spliterator(), checkConditionInParallel).filter(r -> {
                try {
                    return !condition.test(r);
                } catch (Throwable t1) {
                    return !returnFalseOrThrowException(t1, ignoreExceptionOnConditionCheck);
                }
            }).collect(toList());

            R[] result = got;
            for (R r: subResult) {
                result = ArrayUtils.removeElement(result, r);
            }
            return result;
        });
    }


    private static class CheckConditionException extends RuntimeException {
        CheckConditionException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}
