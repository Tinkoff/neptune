package ru.tinkoff.qa.neptune.selenium.api.widget;

import ru.tinkoff.qa.neptune.core.api.steps.annotations.Metadata;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * This annotation is used to define a name of set/list
 * of objects of customized extension of the {@link Widget}.
 */
@Retention(RUNTIME)
@Target({TYPE})
@Metadata
public @interface NameMultiple {
    String value();
}
