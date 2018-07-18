package ru.tinkoff.qa.neptune.core.api.exception.management;

import java.util.List;

public interface StopsIgnoreThrowable<T extends StopsIgnoreThrowable<T>> {
    /**
     * Removes subclasses of {@link Throwable} that have been ignored  by execution of some method/methods and
     * makes it pay attention to them.
     *
     * @param toStopIgnore Removes subclasses of {@link Throwable} that have been ignored.
     * @return an object for the chaining.
     */
    T stopIgnore(List<Class<? extends Throwable>> toStopIgnore);
}
