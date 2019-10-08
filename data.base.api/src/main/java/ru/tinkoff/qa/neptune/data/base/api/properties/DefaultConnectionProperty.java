package ru.tinkoff.qa.neptune.data.base.api.properties;

import ru.tinkoff.qa.neptune.core.api.properties.object.ObjectPropertySupplier;
import ru.tinkoff.qa.neptune.data.base.api.connection.data.DBConnectionSupplier;

/**
 * This class is designed to read the property {@code "default.db.connection"} and to return
 * an instance of {@link DBConnectionSupplier}.
 */
@Deprecated
public final class DefaultConnectionProperty implements ObjectPropertySupplier<DBConnectionSupplier> {

    private final static String DB_CONNECTION_PROPERTY_NAME = "default.db.connection";

    /**
     * This instance reads value of the property {@code 'default.db.connection'} and returns an instance of
     * {@link DBConnectionSupplier}. The value of the property should be defined as fully qualified name of the
     * {@link DBConnectionSupplier} subclass.
     */
    public final static DefaultConnectionProperty DEFAULT_JDO_PERSISTENCE_MANAGER_FACTORY_PROPERTY =
            new DefaultConnectionProperty();

    private DefaultConnectionProperty() {
        super();
    }

    @Override
    public String getPropertyName() {
        return DB_CONNECTION_PROPERTY_NAME;
    }
}
