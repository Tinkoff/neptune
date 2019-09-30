package ru.tinkoff.qa.neptune.data.base.api.connection.data;

import org.datanucleus.api.jdo.JDOPersistenceManagerFactory;

public final class InnerJDOPersistenceManagerFactory extends JDOPersistenceManagerFactory {

    private final DBConnection connection;

    InnerJDOPersistenceManagerFactory(DBConnection connection) {
        super(connection.getData(), null);
        this.connection = connection;
    }

    public DBConnection getConnection() {
        return connection;
    }
}
