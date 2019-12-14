package ru.tinkoff.qa.neptune.data.base.api.connection.data;

import org.datanucleus.api.jdo.JDOPersistenceManager;
import org.datanucleus.api.jdo.JDOPersistenceManagerFactory;
import org.datanucleus.metadata.PersistenceUnitMetaData;

import static com.google.common.base.Preconditions.checkArgument;
import static java.lang.Runtime.getRuntime;
import static java.util.Objects.nonNull;
import static java.util.Optional.ofNullable;

/**
 * This class is designed to wrap data of a DB connection and and create the opened connection.
 * Connection data is described by {@link PersistenceUnitMetaData}
 * The resulted connection is represented by {@link JDOPersistenceManagerFactory}
 */
public final class DBConnection {

    private final PersistenceUnitMetaData persistenceUnitMetaData;
    private InnerJDOPersistenceManagerFactory persistenceManagerFactory;


    DBConnection(PersistenceUnitMetaData persistenceUnitMetaData) {
        checkArgument(nonNull(persistenceUnitMetaData), "Meta data of a connection should not be a null value");
        this.persistenceUnitMetaData = persistenceUnitMetaData;
    }

    public PersistenceUnitMetaData getData() {
        return persistenceUnitMetaData;
    }

    private synchronized JDOPersistenceManagerFactory getPersistenceManagerFactory() {
        return ofNullable(persistenceManagerFactory).orElseGet(() -> {
            persistenceManagerFactory = new InnerJDOPersistenceManagerFactory(this);
            getRuntime().addShutdownHook(new Thread(() -> persistenceManagerFactory.close()));
            return persistenceManagerFactory;
        });
    }

    public synchronized JDOPersistenceManager getPersistenceManager() {
        return (JDOPersistenceManager) getPersistenceManagerFactory().getPersistenceManager();
    }
}
