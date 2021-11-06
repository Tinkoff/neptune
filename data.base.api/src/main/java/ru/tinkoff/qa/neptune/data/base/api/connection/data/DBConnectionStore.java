package ru.tinkoff.qa.neptune.data.base.api.connection.data;

import java.util.ArrayList;
import java.util.List;
import java.util.ServiceLoader;

import static com.google.common.base.Preconditions.checkArgument;
import static java.lang.String.format;
import static java.util.Objects.nonNull;
import static java.util.stream.Collectors.toList;

@Deprecated(forRemoval = true)
public final class DBConnectionStore {

    private static final List<DBConnectionSupplier> CONNECTION_DATA_SUPPLIERS =
            new ArrayList<>();

    private DBConnectionStore() {
        super();
    }

    /**
     * Initializes DD connection suppliers by SPI engines and returns them as a list.
     *
     * @return list of {@link DBConnectionSupplier} initialized by SPI engines.
     */
    private static synchronized List<DBConnectionSupplier> getConnectionDataSuppliers() {
        if (CONNECTION_DATA_SUPPLIERS.size() == 0) {
            CONNECTION_DATA_SUPPLIERS.addAll(ServiceLoader.load(DBConnectionSupplier.class)
                    .stream()
                    .map(ServiceLoader.Provider::get).collect(toList()));
        }
        return CONNECTION_DATA_SUPPLIERS;
    }

    /**
     * Finds some {@link DBConnectionSupplier} and returns the {@link DBConnection}.
     * <p>NOTE!</p>
     * It is expected that all instances of {@link DBConnectionSupplier} to find and use are loaded by SPI firstly.
     * @see <a href="https://docs.oracle.com/javase/tutorial/sound/SPI-intro.html">
     *     Introduction to the Service Provider Interfaces</a>
     *
     * @param supplierClass is a subclass of {@link DBConnectionSupplier} to get the instance
     *                      of {@link DBConnection}.
     * @param throwExceptionIfNotPresent is what to do when nothing is found. When {@code true}
     *                                   then {@link IllegalArgumentException} is thrown. {@code null}
     *                                   is returned otherwise.
     * @return an instance of {@link DBConnection}.
     */
    public static DBConnection getKnownConnection(Class<? extends DBConnectionSupplier> supplierClass,
                                                  boolean throwExceptionIfNotPresent) {
        checkArgument(nonNull(supplierClass), "Class of DB connection supplier " +
                "is expected to be not a null value");
        return getConnectionDataSuppliers().stream().filter(dbConnectionSupplier ->
                        dbConnectionSupplier.getClass().equals(supplierClass))
                .findFirst()
                .map(DBConnectionSupplier::get)
                .orElseGet(() -> {
                    if (!throwExceptionIfNotPresent) {
                        return null;
                    }
                    throw new IllegalArgumentException(format("A supplier of DB connection is not found. Expected class: %s",
                            supplierClass.getName()));
                });
    }
}
