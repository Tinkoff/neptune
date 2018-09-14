package ru.tinkoff.qa.neptune.data.base.api.persistence.data;

import org.datanucleus.api.jdo.JDOPersistenceManagerFactory;
import org.datanucleus.metadata.PersistenceUnitMetaData;
import org.datanucleus.metadata.TransactionType;
import org.reflections.Reflections;
import ru.tinkoff.qa.neptune.data.base.api.PersistableObject;

import javax.jdo.annotations.PersistenceCapable;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static java.lang.String.format;
import static java.util.Optional.ofNullable;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.datanucleus.metadata.TransactionType.RESOURCE_LOCAL;
import static ru.tinkoff.qa.neptune.data.base.api.persistence.data.PersistenceManagerFactoryStore.isPersistentManagerFactory;

public abstract class PersistenceManagerFactorySupplier implements Supplier<JDOPersistenceManagerFactory> {

    private static TransactionType DEFAULT_TRANSACTION_TYPE = RESOURCE_LOCAL;
    private static final Reflections REFLECTIONS = new Reflections("");

    private final PersistenceUnitMetaData persistenceUnitMetaData;

    public PersistenceManagerFactorySupplier() {
        PersistenceUnit unit = this.getClass().getAnnotation(PersistenceUnit.class);
        Class<? extends PersistenceManagerFactorySupplier> supplierClass = this.getClass();
        persistenceUnitMetaData = fillPersistenceUnit(ofNullable(unit)
                .map(persistenceUnit -> {
                    if (isBlank(persistenceUnit.uri())) {
                        return new PersistenceUnitMetaData(persistenceUnit.name(),
                                persistenceUnit.transactionType().name(), null);
                    }

                    try {
                        URI uri = new URI(persistenceUnit.uri());
                        return new PersistenceUnitMetaData(persistenceUnit.name(),
                                persistenceUnit.transactionType().name(), uri);
                    } catch (URISyntaxException e) {
                        throw new IllegalArgumentException(format("Class %s was annotated by %s with invalid uri parameter %s",
                                supplierClass.getName(), PersistenceUnit.class.getName(), persistenceUnit.uri()));
                    }
                }).orElseGet(() -> new PersistenceUnitMetaData(getDynamicUnitName(), DEFAULT_TRANSACTION_TYPE.name(),
                null)));

        REFLECTIONS.getSubTypesOf(PersistableObject.class).stream()
                .filter(clazz -> clazz.getAnnotation(PersistenceCapable.class) != null)
                .map(Class::getName).collect(Collectors.toList()).forEach(persistenceUnitMetaData::addClassName);
        persistenceUnitMetaData.setExcludeUnlistedClasses(true);

    }

    private static String getDynamicUnitName() {
        String defaultPersistenceName = "dynamic-unit";
        String unitName = defaultPersistenceName;
        int index = 0;
        while (isPersistentManagerFactory(unitName)) {
            index ++;
            unitName = defaultPersistenceName + index;
        }
        return unitName;
    }

    /**
     * Fills given persistence unit with properties/parameters and returns filled object.
     *
     * @param toBeFilled a persistence unit to be filled
     * @return filled instance of {@link PersistenceUnitMetaData}
     */
    protected abstract PersistenceUnitMetaData fillPersistenceUnit(PersistenceUnitMetaData toBeFilled);

    /**
     * Returns name of the persistence unit.
     *
     * @return name of the unit.
     */
    public String name() {
        return persistenceUnitMetaData.getName();
    }

    @Override
    public JDOPersistenceManagerFactory get() {
        JDOPersistenceManagerFactory managerFactory =
                new JDOPersistenceManagerFactory(persistenceUnitMetaData, null);
        managerFactory.setName(persistenceUnitMetaData.getName());
        return managerFactory;
    }
}
