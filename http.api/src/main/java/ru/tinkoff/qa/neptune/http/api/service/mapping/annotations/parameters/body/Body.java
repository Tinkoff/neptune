package ru.tinkoff.qa.neptune.http.api.service.mapping.annotations.parameters.body;

import ru.tinkoff.qa.neptune.http.api.service.mapping.annotations.parameters.Required;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Marks a parameter of a {@link java.lang.reflect.Method} those variable value
 * is used as a body of http method.
 */
@Retention(RUNTIME)
@Target(PARAMETER)
public @interface Body {

    /**
     * @return is parameter required or not. Default value is {@code true}.
     * It allows or doesn't allow {@code null} values of a parameter on a method
     * invocation.
     */
    @Required
    boolean isRequired() default true;
}
