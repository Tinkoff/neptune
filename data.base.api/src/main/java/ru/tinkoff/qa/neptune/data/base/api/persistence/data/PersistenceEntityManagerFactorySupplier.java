package ru.tinkoff.qa.neptune.data.base.api.persistence.data;

import org.datanucleus.api.jpa.JPAEntityManagerFactory;
import org.datanucleus.metadata.PersistenceUnitMetaData;
import org.datanucleus.metadata.TransactionType;
import org.reflections.Reflections;
import ru.tinkoff.qa.neptune.data.base.api.PersistableObject;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static java.lang.String.format;
import static java.util.Optional.ofNullable;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.datanucleus.metadata.TransactionType.RESOURCE_LOCAL;
import static ru.tinkoff.qa.neptune.data.base.api.persistence.data.PersistenceEntityManagerFactoryStore.isPersistentEntityManagerFactory;

public abstract class PersistenceEntityManagerFactorySupplier implements Supplier<JPAEntityManagerFactory> {

    private static String DEFAULT_PERSISTENCE_NAME = "dynamic-unit";
    private static TransactionType DEFAULT_TRANSACTION_TYPE = RESOURCE_LOCAL;
    private static final Reflections REFLECTIONS = new Reflections("");

    private final PersistenceUnitMetaData persistenceUnitMetaData;

    public PersistenceEntityManagerFactorySupplier() {
        PersistenceUnit unit = this.getClass().getAnnotation(PersistenceUnit.class);
        Class<? extends PersistenceEntityManagerFactorySupplier> supplierClass = this.getClass();
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
                .map(Class::getName).collect(Collectors.toList()).forEach(persistenceUnitMetaData::addClassName);
        persistenceUnitMetaData.setExcludeUnlistedClasses();

    }

    private static String getDynamicUnitName() {
        String unitName = DEFAULT_PERSISTENCE_NAME;
        int index = 0;
        while (isPersistentEntityManagerFactory(unitName)) {
            index ++;
            unitName = DEFAULT_PERSISTENCE_NAME + index;
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
    public JPAEntityManagerFactory get() {
        return new JPAEntityManagerFactory(persistenceUnitMetaData, null);
    }
}
