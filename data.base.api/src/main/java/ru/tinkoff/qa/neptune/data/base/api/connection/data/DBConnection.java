package ru.tinkoff.qa.neptune.data.base.api.connection.data;

import org.datanucleus.api.jdo.JDOPersistenceManagerFactory;
import org.datanucleus.metadata.PersistenceUnitMetaData;

import static com.google.common.base.Preconditions.checkArgument;
import static java.util.Objects.nonNull;

/**
 * This class is designed to wrap data of a DB connection and and create the opened connection.
 * Connection data is described by {@link PersistenceUnitMetaData}
 * The resulted connection is represented by {@link JDOPersistenceManagerFactory}
 */
public final class DBConnection {

    private final PersistenceUnitMetaData persistenceUnitMetaData;
    private JDOPersistenceManagerFactory factory;

    /**
     * Creates an instance of the {@link DBConnection}
     *
     * @param persistenceUnitMetaData is the description of a connection to the data base represented
     *                                by {@link PersistenceUnitMetaData}.
     * @return created {@link DBConnection}
     */
    public static DBConnection connectionData(PersistenceUnitMetaData persistenceUnitMetaData) {
        return new DBConnection(persistenceUnitMetaData);
    }

    private DBConnection(PersistenceUnitMetaData persistenceUnitMetaData) {
        checkArgument(nonNull(persistenceUnitMetaData), "Meta data of a connection should not be a null value");
        this.persistenceUnitMetaData = persistenceUnitMetaData;
    }

    public PersistenceUnitMetaData getData() {
        return persistenceUnitMetaData;
    }

    public JDOPersistenceManagerFactory getConnectionFactory() {
        return new InnerJDOPersistenceManagerFactory(this);
    }
}
