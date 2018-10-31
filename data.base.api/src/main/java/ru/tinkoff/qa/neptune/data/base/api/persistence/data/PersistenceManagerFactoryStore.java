package ru.tinkoff.qa.neptune.data.base.api.persistence.data;

import org.datanucleus.api.jdo.JDOPersistenceManagerFactory;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Preconditions.checkArgument;
import static java.lang.String.format;
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

    /**
     * Finds some {@link PersistenceManagerFactorySupplier} and returns the persistence manager factory.
     *
     * @param supplierClass is a subclass of {@link PersistenceManagerFactorySupplier} to get the instance
     *                      of {@link JDOPersistenceManagerFactory}.
     * @param throwExceptionIfNotPresent is what to do when nothing is found. When {@code true}
     *                                   then {@link IllegalArgumentException} is thrown. {@code null}
     *                                   is returned otherwise.
     * @return an instance of {@link JDOPersistenceManagerFactory}.
     */
    public static JDOPersistenceManagerFactory getPersistenceManagerFactory(Class<? extends PersistenceManagerFactorySupplier> supplierClass,
                                                                            boolean throwExceptionIfNotPresent) {
        checkArgument(supplierClass != null, "Class of persistence manager factory supplier " +
                "is expected to be not a null value");
        return getPersistenceManagerFactorySuppliers().stream().filter(persistenceManagerFactorySupplier ->
                persistenceManagerFactorySupplier.getClass().equals(supplierClass))
                .findFirst()
                .map(PersistenceManagerFactorySupplier::get).orElseGet(() -> {
                    if (!throwExceptionIfNotPresent) {
                        return null;
                    }
                    throw new IllegalArgumentException(format("A supplier of persistence manager factories has not been found. Expected class: %s",
                            supplierClass.getName()));
                });
    }
}
