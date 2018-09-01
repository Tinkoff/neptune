package ru.tinkoff.qa.neptune.data.base.api.persistence.data;

import org.datanucleus.api.jdo.JDOPersistenceManagerFactory;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Preconditions.checkArgument;
import static java.lang.String.format;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static ru.tinkoff.qa.neptune.core.api.utils.SPIUtil.loadSPI;

public final class PersistenceManagerFactoryStore {

    private static final List<PersistenceManagerFactorySupplier> persistenceManagerFactorySuppliers =
            new ArrayList<>();

    private PersistenceManagerFactoryStore() {
        super();
    }

    /**
     * Initializes persistence manager suppliers by SPI engines and returns them as a list.
     *
     * @return list of {@link PersistenceManagerFactorySupplier} initialized by SPI engines.
     */
    public static List<PersistenceManagerFactorySupplier> getPersistenceManagerFactorySuppliers() {
        if (persistenceManagerFactorySuppliers.size() == 0) {
            persistenceManagerFactorySuppliers.addAll(loadSPI(PersistenceManagerFactorySupplier.class));
        }
        return persistenceManagerFactorySuppliers;
    }

    static boolean isPersistentManagerFactory(String name) {
        checkArgument(!isBlank(name), "Persistence unit name is expected to be not a blank string.");
        return getPersistenceManagerFactorySuppliers().stream()
                .anyMatch(persistenceManagerFactorySupplier ->
                        persistenceManagerFactorySupplier.name().equalsIgnoreCase(name));
    }

    /**
     * Finds some {@link PersistenceManagerFactorySupplier} by it's name and returns persistence manager.
     *
     * @param name is a name of {@link PersistenceManagerFactorySupplier} to get.
     * @param throwExceptionIfNotPresent is what to do when nothing is found by name. When {@code true} then {@link IllegalArgumentException}
     *                                   is thrown. {@code null} is returned otherwise.
     * @return an instance of {@link PersistenceManagerFactorySupplier} found by name.
     */
    public static JDOPersistenceManagerFactory getPersistenceManagerFactory(String name,
                                                                            boolean throwExceptionIfNotPresent) {
        checkArgument(!isBlank(name), "Persistence unit name is expected to be not a blank string.");
        return getPersistenceManagerFactorySuppliers().stream().filter(persistenceManagerFactorySupplier ->
                persistenceManagerFactorySupplier.name().equalsIgnoreCase(name))
                .findFirst()
                .map(PersistenceManagerFactorySupplier::get).orElseGet(() -> {
                    if (!throwExceptionIfNotPresent) {
                        return null;
                    }
                    throw new IllegalArgumentException(format("A supplier of persistence manager factories named %s has not been found",
                            name));
                });
    }
}
