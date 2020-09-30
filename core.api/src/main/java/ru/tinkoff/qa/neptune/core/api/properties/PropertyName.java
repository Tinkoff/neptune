package ru.tinkoff.qa.neptune.core.api.properties;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Used by subclasses of {@link PropertySupplier} to define names of properties.
 * It is recommended to annotate types and fields of enums which implement {@link PropertySupplier}.
 */
@Retention(RUNTIME)
@Target({TYPE, FIELD})
public @interface PropertyName {
    /**
     * @return name of a property
     */
    String value();
}
