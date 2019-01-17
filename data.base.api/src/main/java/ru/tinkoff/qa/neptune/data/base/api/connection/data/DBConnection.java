package ru.tinkoff.qa.neptune.data.base.api.connection.data;

import org.datanucleus.api.jdo.JDOPersistenceManagerFactory;
import org.datanucleus.metadata.PersistenceUnitMetaData;

import static com.google.common.base.Preconditions.checkArgument;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static java.util.Optional.ofNullable;

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

    private static boolean areEqual(Object o1, Object o2) {
        var isNull1 = isNull(o1);
        var isNull2 = isNull(o1);

        if (isNull1 && isNull2) {
            return true;
        }

        if ((isNull1 && !isNull2) || (!isNull1 && isNull2)) {
            return false;
        }

        return o1.equals(o2);
    }

    public PersistenceUnitMetaData getData() {
        return persistenceUnitMetaData;
    }

    public JDOPersistenceManagerFactory getConnectionFactory() {
        factory = ofNullable(factory).orElseGet(() -> new InnerJDOPersistenceManagerFactory(this));
        return factory;
    }

    @Override
    public boolean equals(Object obj) {
        return ofNullable(obj)
                .map(o -> {
                    if (!DBConnection.class.equals(o.getClass())) {
                        return false;
                    }

                    var metaData = ((DBConnection) o).getData();
                    return (areEqual(persistenceUnitMetaData.getName(), metaData.getName())
                            && areEqual(persistenceUnitMetaData.getRootURI(), metaData.getRootURI())
                            && areEqual(persistenceUnitMetaData.getTransactionType(), metaData.getTransactionType())
                            && areEqual(persistenceUnitMetaData.getDescription(), metaData.getDescription())
                            && areEqual(persistenceUnitMetaData.getProvider(), metaData.getProvider())
                            && areEqual(persistenceUnitMetaData.getValidationMode(), metaData.getValidationMode())
                            && areEqual(persistenceUnitMetaData.getJtaDataSource(), metaData.getJtaDataSource())
                            && areEqual(persistenceUnitMetaData.getNonJtaDataSource(), metaData.getNonJtaDataSource())
                            && areEqual(persistenceUnitMetaData.getClassNames(), metaData.getClassNames())
                            && areEqual(persistenceUnitMetaData.getJarFiles(), metaData.getJarFiles())
                            && areEqual(persistenceUnitMetaData.getMappingFiles(), metaData.getMappingFiles())
                            && areEqual(persistenceUnitMetaData.getProperties(), metaData.getProperties())
                            && areEqual(persistenceUnitMetaData.getExcludeUnlistedClasses(), metaData.getExcludeUnlistedClasses())
                            && areEqual(persistenceUnitMetaData.getSharedCacheMode(), metaData.getSharedCacheMode()));
                })
                .orElse(false);
    }
}
