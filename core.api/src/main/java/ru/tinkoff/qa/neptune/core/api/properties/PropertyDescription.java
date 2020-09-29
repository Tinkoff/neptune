package ru.tinkoff.qa.neptune.core.api.properties;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import static org.apache.commons.lang3.StringUtils.EMPTY;

/**
 * Used by subclasses of {@link PropertySupplier} to define descriptions of properties.
 * It is recommended to annotate types and fields of enums which implement {@link PropertySupplier}.
 */
@Retention(RUNTIME)
@Target({TYPE, FIELD})
public @interface PropertyDescription {
    /**
     * @return description of a property. May be defined by multiple string
     */
    String[] description();

    String section() default EMPTY;
}
