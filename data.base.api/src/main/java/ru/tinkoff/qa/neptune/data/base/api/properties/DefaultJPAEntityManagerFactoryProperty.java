package ru.tinkoff.qa.neptune.data.base.api.properties;

import org.datanucleus.api.jpa.JPAEntityManagerFactory;
import ru.tinkoff.qa.neptune.core.api.properties.PropertySupplier;
import ru.tinkoff.qa.neptune.data.base.api.persistence.data.PersistenceEntityManagerFactorySupplier;
import ru.tinkoff.qa.neptune.data.base.api.persistence.data.PersistenceUnit;

import static java.lang.String.format;
import static ru.tinkoff.qa.neptune.data.base.api.persistence.data.PersistenceEntityManagerFactoryStore.getEntityManagerFactory;

/**
 * This class is designed to read the property {@code "default.persistence.unit.name"} and to return
 * an instance of {@link JPAEntityManagerFactory}.
 */
public final class DefaultJPAEntityManagerFactoryProperty implements PropertySupplier<JPAEntityManagerFactory> {

    private final static String DEFAULT_PERSISTENCE_UNIT_NAME = "default.persistence.unit.name";

    /**
     * This instance reads value of the property {@code 'default.persistence.unit.name'} and returns an instance of
     * {@link JPAEntityManagerFactory}. The value should correspond to persistence units defined by
     * {@link PersistenceEntityManagerFactorySupplier}. It is recommended to annotate each subclass of
     * {@link PersistenceEntityManagerFactorySupplier} by {@link PersistenceUnit} to find default persistence unit.
     */
    public final static DefaultJPAEntityManagerFactoryProperty DEFAULT_JPA_ENTITY_MANAGER_FACTORY_PROPERTY =
            new DefaultJPAEntityManagerFactoryProperty();

    private DefaultJPAEntityManagerFactoryProperty() {
        super();
    }

    @Override
    public String getPropertyName() {
        return DEFAULT_PERSISTENCE_UNIT_NAME;
    }

    @Override
    public JPAEntityManagerFactory get() {
        return returnOptionalFromEnvironment().map(s -> getEntityManagerFactory(s, true))
                .orElseThrow(() -> new IllegalArgumentException(format("The property %s is not defined",
                        DEFAULT_PERSISTENCE_UNIT_NAME)));
    }
}
