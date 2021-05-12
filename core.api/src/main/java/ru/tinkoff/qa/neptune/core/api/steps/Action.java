package ru.tinkoff.qa.neptune.core.api.steps;

/**
 * This is the functional interface which is designed to perform steps that
 * do something but do not return any value.
 *
 * @param <T>
 */
@FunctionalInterface
public interface Action<T> {

    void performAction(T t);
}
