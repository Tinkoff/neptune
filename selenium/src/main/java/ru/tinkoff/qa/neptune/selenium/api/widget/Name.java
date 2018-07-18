package ru.tinkoff.qa.neptune.selenium.api.widget;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * This annotation is used to define a name
 * of a customized extension of the {@link Widget}.
 */
@Retention(RUNTIME) @Target({TYPE})
public @interface Name {
    String value();
}
