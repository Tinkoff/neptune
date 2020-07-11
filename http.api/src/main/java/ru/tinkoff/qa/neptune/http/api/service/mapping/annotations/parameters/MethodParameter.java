package ru.tinkoff.qa.neptune.http.api.service.mapping.annotations.parameters;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Marks a type of an object that is designed to represent a parameter (header, query parameter or path variable)
 * of http request.
 */
@Retention(RUNTIME)
@Target(TYPE)
public @interface MethodParameter {
}
