package ru.tinkoff.qa.neptune.core.api.exception.management;

import java.util.List;

public interface IgnoresThrowable<T extends IgnoresThrowable<T>> {

    /**
     * Adds subclasses of {@link Throwable} that should be ignored by execution of some method/methods.
     *
     * @param toBeIgnored subclasses of {@link Throwable} that should be ignored.
     * @return an object for the chaining.
     */
    T addIgnored(List<Class<? extends Throwable>> toBeIgnored);
}
