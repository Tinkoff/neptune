package ru.tinkoff.qa.neptune.http.api.service.mapping.annotations.methods;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Marks a {@link java.lang.reflect.Method} and defines a path to requested endpoint.
 */
@Retention(RUNTIME)
@Target(METHOD)
public @interface URIPath {

    /**
     * @return a path to requested endpoint.
     */
    String value();
}
