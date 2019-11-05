package ru.tinkoff.qa.neptune.data.base.api;

import io.github.classgraph.ClassGraph;
import io.github.classgraph.ScanResult;
import ru.tinkoff.qa.neptune.data.base.api.connection.data.DBConnection;
import ru.tinkoff.qa.neptune.data.base.api.connection.data.DBConnectionSupplier;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.String.format;
import static java.lang.annotation.ElementType.PACKAGE;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import static java.util.Optional.ofNullable;
import static ru.tinkoff.qa.neptune.data.base.api.connection.data.DBConnectionStore.getKnownConnection;

/**
 * This annotation marks a class that extends {@link PersistableObject} to define proper database connection.
 * Also this annotation marks whole package to define proper database connection for all {@link PersistableObject} extenders
 * from the marked package and its subpackages.
 */
@Retention(RUNTIME) @Target({TYPE, PACKAGE})
public @interface ConnectionToUse {
    /**
     * @return a class that describes a DB connection.
     * @see  DBConnectionSupplier
     */
    Class<? extends DBConnectionSupplier> connectionSupplier();
}
