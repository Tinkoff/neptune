package ru.tinkoff.qa.neptune.retrofit2.service.setup;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * This annotation marks a non-static field whose type
 * describes http-service in Retrofit style
 */
@Retention(RUNTIME)
@Target(FIELD)
public @interface ApiService {

}
