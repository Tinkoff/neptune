package ru.tinkoff.qa.neptune.http.api.service.mapping.annotations.parameters;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Marks methods of annotations. These methods should give information
 * about necessity of a {@link java.lang.reflect.Parameter}
 */
@Retention(RUNTIME)
@Target(METHOD)
public @interface Required {
}
