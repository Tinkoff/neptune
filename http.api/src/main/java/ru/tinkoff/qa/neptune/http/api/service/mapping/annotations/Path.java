package ru.tinkoff.qa.neptune.http.api.service.mapping.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Defines variable part of a path of a request URI
 */
@Retention(RUNTIME)
@Target(PARAMETER)
public @interface Path {

    /**
     * @return variable part of a path of a request URI
     */
    String value();
}
