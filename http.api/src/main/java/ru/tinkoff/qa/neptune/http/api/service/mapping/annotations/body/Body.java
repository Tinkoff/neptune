package ru.tinkoff.qa.neptune.http.api.service.mapping.annotations.body;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import static ru.tinkoff.qa.neptune.http.api.service.mapping.annotations.body.BodyDataFormat.NONE;

/**
 * Marks a parameter of a {@link java.lang.reflect.Method} those variable value
 * is used as a body of http method.
 */
@Retention(RUNTIME)
@Target(PARAMETER)
public @interface Body {

    /**
     * @return format of http request body.
     */
    BodyDataFormat format() default NONE;
}
