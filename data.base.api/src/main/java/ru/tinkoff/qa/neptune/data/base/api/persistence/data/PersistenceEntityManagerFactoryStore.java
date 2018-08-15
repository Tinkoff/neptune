package ru.tinkoff.qa.neptune.data.base.api.persistence.data;

import org.datanucleus.api.jpa.JPAEntityManagerFactory;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Preconditions.checkArgument;
import static java.lang.String.format;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static ru.tinkoff.qa.neptune.core.api.utils.SPIUtil.loadSPI;

public final class PersistenceEntityManagerFactoryStore {

    private static final List<PersistenceEntityManagerFactorySupplier> persistenceEntityManagerFactorySuppliers =
            new ArrayList<>();

    private PersistenceEntityManagerFactoryStore() {
        super();
    }

    /**
     * Initializes persistence entity manager suppliers by SPI engines and returns them as a list.
     *
     * @return list of {@link PersistenceEntityManagerFactorySupplier} initialized by SPI engines.
     */
    public static List<PersistenceEntityManagerFactorySupplier> getPersistenceEntityManagerFactorySuppliers() {
        if (persistenceEntityManagerFactorySuppliers.size() == 0) {
            persistenceEntityManagerFactorySuppliers.addAll(loadSPI(PersistenceEntityManagerFactorySupplier.class));
        }
        return persistenceEntityManagerFactorySuppliers;
    }

    static boolean isPersistentEntityManagerFactory(String name) {
        checkArgument(!isBlank(name), "Persistence unit name is expected to be not a blank string.");
        return getPersistenceEntityManagerFactorySuppliers().stream()
                .anyMatch(persistenceEntityManagerFactorySupplier ->
                        persistenceEntityManagerFactorySupplier.name().equalsIgnoreCase(name));
    }

    /**
     * Finds some {@link PersistenceEntityManagerFactorySupplier} by it's name and returns entity manager.
     *
     * @param name is a name of {@link PersistenceEntityManagerFactorySupplier} to get.
     * @param throwExceptionIfNotPresent is what to do is nothing is found by name. When {@code true} then {@link IllegalArgumentException}
     *                                   is thrown. {@code null} is returned otherwise.
     * @return an instance of {@link PersistenceEntityManagerFactorySupplier} found by name.
     */
    public static JPAEntityManagerFactory getEntityManagerFactory(String name,
                                                                  boolean throwExceptionIfNotPresent) {
        checkArgument(!isBlank(name), "Persistence unit name is expected to be not a blank string.");
        return getPersistenceEntityManagerFactorySuppliers().stream().filter(persistenceEntityManagerFactorySupplier ->
                persistenceEntityManagerFactorySupplier.name().equalsIgnoreCase(name))
                .findFirst()
                .map(PersistenceEntityManagerFactorySupplier::get).orElseGet(() -> {
                    if (!throwExceptionIfNotPresent) {
                        return null;
                    }
                    throw new IllegalArgumentException(format("A supplier of entity manager factories named %s has not been found",
                            name));
                });
    }
}
