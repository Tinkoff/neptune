package ru.tinkoff.qa.neptune.http.api.service.mapping.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Defines a name of http query parameter
 */
@Retention(RUNTIME)
@Target(PARAMETER)
public @interface QueryParameter {

    /**
     * @return a name of http query parameter
     */
    String value();
}
