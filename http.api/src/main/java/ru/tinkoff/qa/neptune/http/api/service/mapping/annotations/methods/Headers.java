package ru.tinkoff.qa.neptune.http.api.service.mapping.annotations.methods;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Retention(RUNTIME)
@Target(METHOD)
public @interface Headers {
    Header[] value();
}
