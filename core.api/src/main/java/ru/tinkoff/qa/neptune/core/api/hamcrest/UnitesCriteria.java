package ru.tinkoff.qa.neptune.core.api.hamcrest;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Marks classes that extend {@link NeptuneFeatureMatcher}. These classes are supposed to
 * unite matchers into one criteria. It is necessary to form descriptions correctly.
 */
@Retention(RUNTIME)
@Target(TYPE)
public @interface UnitesCriteria {
}
