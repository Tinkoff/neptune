package ru.tinkoff.qa.neptune.core.api.steps;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

public final class StoryWriter {

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
    public static <T> StepAction<T> action(String description, Consumer<T> consumer) {
        return new StepAction<>(description, consumer);
    }

    /**
     * This method creates a function with some string description. This function is
     * supposed to get some value.
     *
     * @param description string narration of the getting value
     * @param function which gets the needed value
     * @param <T> type of the input value
     * @param <R> type of the value to get
     * @return a new function with the given string description. Description is returned
     * by the {@link #toString()} method.
     */
    public static <T, R> StepFunction<T, R> toGet(String description, Function<T, R> function) {
        return new StepFunction<>(description, function);
    }

    /**
     * This method creates a predicate with some string description. This predicate is
     * supposed to be used as some conditions or filter.
     *
     * @param description string narration of the conditions
     * @param predicate which checks some input value
     * @param <T> type of the input value
     * @return a new predicate with the given string description. Description is returned
     * by the {@link #toString()} method.
     */
    public static <T> Predicate<T> condition(String description, Predicate<T> predicate) {
        return new Condition.DescribedCondition<>(description, predicate);
    }
}
