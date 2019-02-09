package ru.tinkoff.qa.neptune.core.api.exception.management;

import java.util.Collection;

public interface IgnoresThrowable<T extends IgnoresThrowable<T>> {

    /**
     * Adds subclasses of {@link Throwable} that should be ignored by execution of some method/methods.
     *
     * @param toBeIgnored subclasses of {@link Throwable} that should be ignored.
     * @return an object for the chaining.
     */
    T addIgnored(Collection<Class<? extends Throwable>> toBeIgnored);

    /**
     * Adds subclass of {@link Throwable} that should be ignored by execution of some method/methods.
     *
     * @param toBeIgnored subclass of {@link Throwable} that should be ignored.
     * @return an object for the chaining.
     */
    T addIgnored(Class<? extends Throwable> toBeIgnored);
}
