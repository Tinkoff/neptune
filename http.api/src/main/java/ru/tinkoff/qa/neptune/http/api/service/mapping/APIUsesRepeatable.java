package ru.tinkoff.qa.neptune.http.api.service.mapping;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Retention(RUNTIME)
@Target({TYPE, FIELD})
public @interface APIUsesRepeatable {
    APIUses[] value();
}
