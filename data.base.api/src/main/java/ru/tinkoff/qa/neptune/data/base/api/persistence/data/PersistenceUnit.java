package ru.tinkoff.qa.neptune.data.base.api.persistence.data;

import org.datanucleus.metadata.TransactionType;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import static org.datanucleus.metadata.TransactionType.RESOURCE_LOCAL;

/**
 * This annotation is designed to mark some subclass of {@link PersistenceUnitMetaDataSupplier}.
 */
@Retention(RUNTIME) @Target({TYPE})
public @interface PersistenceUnit {

    /**
     * Name of a target persistence-unit.
     *
     * @return name is a unit name.
     */
    String name();

    /**
     * Defines transaction type for the persistence-unit.
     *
     * @return Transaction type for the unit.
     */
    TransactionType transactionType() default RESOURCE_LOCAL;

    /**
     * Root URI of the unit.
     *
     * @return Root of the persistence-unit.
     */
    String uri() default "";
}
