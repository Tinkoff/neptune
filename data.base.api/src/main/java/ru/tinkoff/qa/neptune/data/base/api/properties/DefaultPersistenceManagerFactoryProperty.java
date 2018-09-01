package ru.tinkoff.qa.neptune.data.base.api.properties;

import org.datanucleus.api.jdo.JDOPersistenceManagerFactory;
import ru.tinkoff.qa.neptune.core.api.properties.PropertySupplier;
import ru.tinkoff.qa.neptune.data.base.api.persistence.data.PersistenceManagerFactorySupplier;
import ru.tinkoff.qa.neptune.data.base.api.persistence.data.PersistenceUnit;

import static java.lang.String.format;
import static ru.tinkoff.qa.neptune.data.base.api.persistence.data.PersistenceManagerFactoryStore.getPersistenceManagerFactory;

/**
 * This class is designed to read the property {@code "default.persistence.unit.name"} and to return
 * an instance of {@link JDOPersistenceManagerFactory}.
 */
public final class DefaultPersistenceManagerFactoryProperty implements PropertySupplier<JDOPersistenceManagerFactory> {

    private final static String DEFAULT_PERSISTENCE_UNIT_NAME = "default.persistence.unit.name";

    /**
     * This instance reads value of the property {@code 'default.persistence.unit.name'} and returns an instance of
     * {@link JDOPersistenceManagerFactory}. The value should correspond to persistence units defined by
     * {@link PersistenceManagerFactorySupplier}. It is recommended to annotate each subclass of
     * {@link PersistenceManagerFactorySupplier} by {@link PersistenceUnit} to find default persistence unit.
     */
    public final static DefaultPersistenceManagerFactoryProperty DEFAULT_JDO_PERSISTENCE_MANAGER_FACTORY_PROPERTY =
            new DefaultPersistenceManagerFactoryProperty();

    private DefaultPersistenceManagerFactoryProperty() {
        super();
    }

    @Override
    public String getPropertyName() {
        return DEFAULT_PERSISTENCE_UNIT_NAME;
    }

    @Override
    public JDOPersistenceManagerFactory get() {
        return returnOptionalFromEnvironment().map(s -> getPersistenceManagerFactory(s, true))
                .orElseThrow(() -> new IllegalArgumentException(format("The property %s is not defined",
                        DEFAULT_PERSISTENCE_UNIT_NAME)));
    }
}
