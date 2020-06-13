package ru.tinkoff.qa.neptune.http.api.service.mapping.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Marks a method parameter those value plays
 * a role of http request body
 */
@Retention(RUNTIME)
@Target(PARAMETER)
public @interface Body {
}
