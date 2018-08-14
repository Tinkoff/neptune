package ru.tinkoff.qa.neptune.data.base.api.persistence.data;

import java.util.List;

import static com.google.common.base.Preconditions.checkArgument;
import static java.lang.String.format;
import static java.util.Optional.ofNullable;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static ru.tinkoff.qa.neptune.core.api.utils.SPIUtil.loadSPI;

public final class PersistenceUnitMetaDataStore {

    private static List<PersistenceUnitMetaDataSupplier> persistenceUnitMetaDataSuppliers = null;

    private PersistenceUnitMetaDataStore() {
        super();
    }

    /**
     * Initializes persistence unit meta data suppliers by SPI engines and returns them as a list.
     *
     * @return list of {@link PersistenceUnitMetaDataSupplier} initialized by SPI engines.
     */
    public static List<PersistenceUnitMetaDataSupplier> getPersistenceUnitMetaDataSuppliers() {
        persistenceUnitMetaDataSuppliers = ofNullable(persistenceUnitMetaDataSuppliers).orElseGet(() ->
                loadSPI(PersistenceUnitMetaDataSupplier.class));
        return persistenceUnitMetaDataSuppliers;
    }

    static boolean isPersistentUnitMetaDataPresent(String name) {
        checkArgument(!isBlank(name), "Persistence unit name is expected to be not a blank string.");
        return getPersistenceUnitMetaDataSuppliers().stream().anyMatch(persistenceUnitMetaDataSupplier ->
                persistenceUnitMetaDataSupplier.name().equalsIgnoreCase(name));
    }

    /**
     * Finds some {@link PersistenceUnitMetaDataSupplier} by it's name.
     *
     * @param name is a name of {@link PersistenceUnitMetaDataSupplier} to get.
     * @param throwExceptionIfNotPresent is what to do is nothing is found by name. When {@code true} then {@link IllegalArgumentException}
     *                                   is thrown. {@code null} is returned otherwise.
     * @return an instance of {@link PersistenceUnitMetaDataSupplier} found by name.
     */
    public PersistenceUnitMetaDataSupplier getPersistenceUnitMetaDataSupplier(String name,
                                                                              boolean throwExceptionIfNotPresent) {
        checkArgument(!isBlank(name), "Persistence unit name is expected to be not a blank string.");
        return getPersistenceUnitMetaDataSuppliers().stream().filter(persistenceUnitMetaDataSupplier ->
                persistenceUnitMetaDataSupplier.name().equalsIgnoreCase(name))
                .findFirst().orElseGet(() -> {
                    if (!throwExceptionIfNotPresent) {
                        return null;
                    }
                    throw new IllegalArgumentException(format("Persistence unit meta-data supplier named %s has not been found",
                            name));
                });
    }
}
