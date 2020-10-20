package ru.tinkoff.qa.neptune.core.api.properties;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Used by subclasses of {@link PropertySupplier} to mark them excluded from the forming of {@link GeneralPropertyInitializer#PROPERTIES}
 * and {@link GeneralPropertyInitializer#GLOBAL_PROPERTIES}. These properties are supposed to be instantiated another way.
 * It is recommended to annotate types and fields of enums which implement {@link PropertySupplier}.
 */
@Retention(RUNTIME)
@Target({TYPE, FIELD})
public @interface ExcludeFromExport {
}
