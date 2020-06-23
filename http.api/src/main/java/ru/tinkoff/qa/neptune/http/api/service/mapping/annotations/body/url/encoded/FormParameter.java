package ru.tinkoff.qa.neptune.http.api.service.mapping.annotations.body.url.encoded;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Marks a parameter of a {@link java.lang.reflect.Method} those variable value
 * is used as a parameter value of URL encoded form.
 */
@Retention(RUNTIME)
@Target(PARAMETER)
public @interface FormParameter {
    /**
     * @return name of a form parameter
     */
    String name();
}
