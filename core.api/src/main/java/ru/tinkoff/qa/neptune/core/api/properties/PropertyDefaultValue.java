package ru.tinkoff.qa.neptune.core.api.properties;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Used by subclasses of {@link PropertySupplier} to define default values of properties.
 * It is recommended to annotate types and fields of enums which implement {@link PropertySupplier}.
 * Be careful. Default value should be parsed according to type of supplied value.
 */
@Retention(RUNTIME)
@Target({TYPE, FIELD})
public @interface PropertyDefaultValue {
    /**
     * @return default string value of a property
     */
    String value();
}
