package ru.tinkoff.qa.neptune.data.base.api.properties;

import org.datanucleus.api.jdo.JDOPersistenceManagerFactory;
import ru.tinkoff.qa.neptune.core.api.properties.object.ObjectPropertySupplier;
import ru.tinkoff.qa.neptune.data.base.api.persistence.data.PersistenceManagerFactorySupplier;

/**
 * This class is designed to read the property {@code "default.persistence.unit.name"} and to return
 * an instance of {@link PersistenceManagerFactorySupplier}.
 */
public final class DefaultPersistenceManagerFactoryProperty implements ObjectPropertySupplier<PersistenceManagerFactorySupplier> {

    private final static String PERSISTENCE_UNIT_NAME = "default.persistence.unit.name";

    /**
     * This instance reads value of the property {@code 'persistence.unit.name'} and returns an instance of
     * {@link JDOPersistenceManagerFactory}. The value should correspond to persistence units defined by
     * {@link PersistenceManagerFactorySupplier}. This value should be defined as fully qualified class name.
     */
    public final static DefaultPersistenceManagerFactoryProperty DEFAULT_JDO_PERSISTENCE_MANAGER_FACTORY_PROPERTY =
            new DefaultPersistenceManagerFactoryProperty();

    private DefaultPersistenceManagerFactoryProperty() {
        super();
    }

    @Override
    public String getPropertyName() {
        return PERSISTENCE_UNIT_NAME;
    }
}
