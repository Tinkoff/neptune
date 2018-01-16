package com.github.toy.constructor.core.api;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static java.lang.String.format;
import static java.util.Optional.ofNullable;
import static org.apache.commons.lang3.StringUtils.isBlank;

public final class StoryWriter {

    private static <T, V, R> Function<T, R> getSequentialDescribedFunction(Function<? super T, ? extends V> before,
                                                                           Function<? super V, ? extends R> after,
                                                                           String sequenceDelimiter) {
        checkNotNull(before);
        checkNotNull(after);
        checkArgument(DescribedFunction.class.isAssignableFrom(before.getClass()),
                "It seems given before-function doesn't describe any value to get. Use method " +
                        "StoryWriter.toGet to describe the value to get previously.");
        checkArgument(DescribedFunction.class.isAssignableFrom(after.getClass()),
                "It seems given after-function doesn't describe any value to get. Use method " +
                        "StoryWriter.toGet to describe the value to get previously.");

        return new DescribedFunction<>() {
            @Override
            public R apply(T t) {
                V result = before.apply(t);
                return ofNullable(result).map(after).orElse(null);
            }

            @Override
            public String toString() {
                return format("%s %s (%s)", after.toString(), sequenceDelimiter, before.toString());
            }

            public  <Q> Function<Q, R> compose(Function<? super Q, ? extends T> before) {
                return getSequentialDescribedFunction(before, this, sequenceDelimiter);
            }

            public  <U> Function<T, U> andThen(Function<? super R, ? extends U> after) {
                return getSequentialDescribedFunction(this, after, sequenceDelimiter);
            }
        };
    }

    private static <T> Consumer<T> getSequentialDescribedConsumer(Consumer<? super T> before,
                                                           Consumer<? super T> after,
                                                           String sequenceDelimiter) {
        checkNotNull(before);
        checkNotNull(after);
        checkArgument(DescribedConsumer.class.isAssignableFrom(before.getClass()),
                "It seems given consumer doesn't describe any before-action. Use method " +
                        "StoryWriter.action to describe the after-action.");
        checkArgument(DescribedConsumer.class.isAssignableFrom(after.getClass()),
                "It seems given consumer doesn't describe any after-action. Use method " +
                        "StoryWriter.action to describe the after-action.");

        return new DescribedConsumer<>() {
            @Override
            public void accept(T t) {
                before.accept(t); after.accept(t);
            }

            public String toString() {
                return format("%s %s %s", before, sequenceDelimiter, after);
            }

            public Consumer<T> andThen(Consumer<? super T> afterAction)  {
                return getSequentialDescribedConsumer(this, afterAction, sequenceDelimiter);
            }
        };
    }

    /**
     * This method creates a consumer with some string description. This consumer is
     * supposed to perform some action.
     *
     * @param description string narration of the action
     * @param sequenceDelimiter which should be used as description connector on {@link Consumer#andThen(Consumer)}
     * @param consumer which performs the action
     * @param <T> type of accepted value
     * @return a new consumer with the given string description. Description is returned
     * by the {@link #toString()} method.
     */
    public static <T> Consumer<T> action(String description, String sequenceDelimiter, Consumer<T> consumer) {
        checkArgument(!isBlank(description), "Description should not be empty");
        return new DescribedConsumer<>() {
            @Override
            public void accept(T t) {
                consumer.accept(t);
            }

            @Override
            public String toString() {
                return description;
            }

            public Consumer<T> andThen(Consumer<? super T> afterAction)  {
                return getSequentialDescribedConsumer(this, afterAction, sequenceDelimiter);
            }
        };
    }

    /**
     * This method creates a consumer with some string description. This consumer is
     * supposed to perform some action.
     *
     * @param description string narration of the action
     * @param consumer which performs the action
     * @param <T> type of accepted value
     * @return a new consumer with the given string description. Description is returned
     * by the {@link #toString()} method.
     */
    public static <T> Consumer<T> action(String description, Consumer<T> consumer) {
        return action(description, " \n and then -> \n", consumer);
    }

    /**
     * This method creates a function with some string description. This function is
     * supposed to get some value.
     *
     * @param description string narration of the getting value
     * @param sequenceBefore which should be used as description connector on {@link Function#compose(Function)}
     * @param sequenceAfter which should be used as description connector on {@link Function#andThen(Function)}
     * @param function which gets the needed value
     * @param <T> type of the input value
     * @return a new function with the given string description. Description is returned
     * by the {@link #toString()} method.
     */
    public static <T, R> Function<T, R> toGet(String description, String sequenceBefore,
                                              String sequenceAfter, Function<T, R> function) {
        checkArgument(!isBlank(description), "Description should not be empty");
        return new DescribedFunction<>() {
            @Override
            public R apply(T t) {
                return function.apply(t);
            }

            @Override
            public String toString() {
                return description;
            }

            public  <V> Function<V, R> compose(Function<? super V, ? extends T> before) {
                return getSequentialDescribedFunction(before, this, sequenceAfter);
            }

            public  <V> Function<T, V> andThen(Function<? super R, ? extends V> after) {
                return getSequentialDescribedFunction(this, after, sequenceBefore);
            }
        };
    }

    /**
     * This method creates a function with some string description. This function is
     * supposed to get some value.
     *
     * @param description string narration of the getting value
     * @param function which gets the needed value
     * @param <T> type of the input value
     * @return a new function with the given string description. Description is returned
     * by the {@link #toString()} method.
     */
    public static <T, R> Function<T, R> toGet(String description, Function<T, R> function) {
        return toGet(description, "from","from", function);
    }

    /**
     * This method creates a predicate with some string description. This predicate is
     * supposed to be used as some condition or filter.
     *
     * @param description string narration of the condition
     * @param predicate which checks some input value
     * @param <T> type of the input value
     * @return a new predicate with the given string description. Description is returned
     * by the {@link #toString()} method.
     */
    public static <T> Predicate<T> condition(String description, Predicate<T> predicate) {
        checkArgument(!isBlank(description), "Description should not be empty");
        return new DescribedPredicate<>() {
            @Override
            public boolean test(T t) {
                return predicate.test(t);
            }

            @Override
            public String toString() {
                return description;
            }
        };
    }
}
