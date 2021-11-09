package ru.tinkoff.qa.neptune.data.base.api;

import ru.tinkoff.qa.neptune.data.base.api.connection.data.DBConnectionSupplier;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.PACKAGE;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * This annotation marks a class that extends {@link PersistableObject} to define proper database connection.
 * Also this annotation marks whole package to define proper database connection for all {@link PersistableObject} extenders
 * from the marked package and its subpackages.
 */
@Deprecated(forRemoval = true)
@Retention(RUNTIME) @Target({TYPE, PACKAGE})
public @interface ConnectionToUse {
    /**
     * @return a class that describes a DB connection.
     * @see  DBConnectionSupplier
     */
    Class<? extends DBConnectionSupplier> connectionSupplier();
}
