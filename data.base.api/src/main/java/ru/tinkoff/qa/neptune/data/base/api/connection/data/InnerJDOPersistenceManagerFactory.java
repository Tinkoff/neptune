package ru.tinkoff.qa.neptune.data.base.api.connection.data;

import org.datanucleus.api.jdo.JDOPersistenceManagerFactory;

import static java.util.Optional.ofNullable;

final class InnerJDOPersistenceManagerFactory extends JDOPersistenceManagerFactory {

    private final DBConnection connection;

    InnerJDOPersistenceManagerFactory(DBConnection connection) {
        super(connection.getData(), null);
        this.connection = connection;
    }

    @Override
    public boolean equals(Object obj) {
        return ofNullable(obj).map(o -> {
            if (!this.getClass().equals(o.getClass())) {
                return false;
            }
            return this.connection
                    .equals(((InnerJDOPersistenceManagerFactory) obj).connection);
        }).orElse(false);
    }
}
