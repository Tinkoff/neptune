package ru.tinkoff.qa.neptune.data.base.api.persistence.data;

import org.datanucleus.api.jdo.JDOPersistenceManagerFactory;
import org.datanucleus.metadata.PersistenceUnitMetaData;
import org.datanucleus.metadata.TransactionType;
import org.reflections.Reflections;
import ru.tinkoff.qa.neptune.data.base.api.PersistableObject;

import javax.jdo.annotations.PersistenceCapable;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static java.util.Objects.nonNull;
import static org.datanucleus.metadata.TransactionType.RESOURCE_LOCAL;

/**
 * This class is designed to prepare metadata of a {@link JDOPersistenceManagerFactory} to be created and used
 * for further interaction with a data base.
 */
public abstract class PersistenceManagerFactorySupplier implements Supplier<JDOPersistenceManagerFactory> {

    private static TransactionType DEFAULT_TRANSACTION_TYPE = RESOURCE_LOCAL;
    private static final Reflections REFLECTIONS = new Reflections("");

    private final PersistenceUnitMetaData persistenceUnitMetaData;

    public PersistenceManagerFactorySupplier() {
        persistenceUnitMetaData = fillPersistenceUnit(new PersistenceUnitMetaData(this.getClass().getName(),
                DEFAULT_TRANSACTION_TYPE.name(),
                null));

        REFLECTIONS.getSubTypesOf(PersistableObject.class).stream()
                .filter(clazz -> nonNull(clazz.getAnnotation(PersistenceCapable.class)))
                .map(Class::getName).collect(Collectors.toList()).forEach(persistenceUnitMetaData::addClassName);
        persistenceUnitMetaData.setExcludeUnlistedClasses(true);

    }

    /**
     * Fills given persistence unit with properties/parameters and returns filled object.
     *
     * @param toBeFilled a persistence unit to be filled
     * @return filled instance of {@link PersistenceUnitMetaData}
     */
    protected abstract PersistenceUnitMetaData fillPersistenceUnit(PersistenceUnitMetaData toBeFilled);


    @Override
    public JDOPersistenceManagerFactory get() {
        var managerFactory = new JDOPersistenceManagerFactory(persistenceUnitMetaData, null);
        managerFactory.setName(persistenceUnitMetaData.getName());
        return managerFactory;
    }
}
