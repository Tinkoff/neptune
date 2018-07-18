package ru.tinkoff.qa.neptune.core.api;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * This annotation is used to provide default parameters for constructor invocation of some subclass of {@link PerformActionStep}
 * or {@link GetStep}.
 */
@Retention(RUNTIME) @Target({TYPE})
public @interface CreateWith {
    /**
     * Defined subclass of {@link ParameterProvider}.
     *
     * @return subclass of {@link ParameterProvider}.
     */
    Class<? extends ParameterProvider> provider();
}
