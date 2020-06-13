package ru.tinkoff.qa.neptune.http.api.service.mapping.annotations;

import ru.tinkoff.qa.neptune.http.api.service.mapping.WebServiceAPI;

import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Marks methods of {@link WebServiceAPI}. It defines http headers
 * of resulted http request.
 */
@Retention(RUNTIME)
@Repeatable(Headers.class)
@Target(METHOD)
public @interface Header {

    /**
     * @return name of a header
     */
    String name();

    /**
     * @return values of a header
     */
    String[] value();
}
