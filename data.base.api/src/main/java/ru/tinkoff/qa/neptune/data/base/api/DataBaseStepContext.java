package ru.tinkoff.qa.neptune.data.base.api;

import org.datanucleus.api.jdo.JDOPersistenceManager;
import ru.tinkoff.qa.neptune.core.api.steps.context.CreateWith;
import ru.tinkoff.qa.neptune.core.api.steps.context.GetStepContext;
import ru.tinkoff.qa.neptune.core.api.steps.context.ActionStepContext;
import ru.tinkoff.qa.neptune.core.api.cleaning.Refreshable;
import ru.tinkoff.qa.neptune.core.api.cleaning.StoppableOnJVMShutdown;
import ru.tinkoff.qa.neptune.data.base.api.connection.data.DBConnection;
import ru.tinkoff.qa.neptune.data.base.api.connection.data.DBConnectionSupplier;

import java.util.HashSet;
import java.util.Set;

import static com.google.common.base.Preconditions.checkArgument;
import static java.lang.String.format;
import static java.util.Objects.nonNull;
import static java.util.Optional.ofNullable;
import static ru.tinkoff.qa.neptune.data.base.api.connection.data.DBConnectionStore.getKnownConnection;

@CreateWith(provider = DataBaseParameterProvider.class)
public class DataBaseStepContext implements GetStepContext<DataBaseStepContext>, ActionStepContext<DataBaseStepContext>, StoppableOnJVMShutdown,
        Refreshable {

    private final DBConnection defaultConnection;
    private final Set<JDOPersistenceManager> jdoPersistenceManagerSet = new HashSet<>();
    private JDOPersistenceManager currentManager;

    public DataBaseStepContext(DBConnection defaultConnection) {
        checkArgument(nonNull(defaultConnection), "Value of the default DB connection should differ from null");
        this.defaultConnection = defaultConnection;
    }

    /**
     * This method performs the switching to desired database by created {@link DBConnection}.
     *
     * @param dbConnection is data of a connection to the desired DB.
     * @return self-reference
     */
    DataBaseStepContext switchTo(DBConnection dbConnection) {
        checkArgument(nonNull(dbConnection), "DB connection should not be null-value");
        var factory = dbConnection.getConnectionFactory();
        checkArgument(!factory.isClosed(), "Persistence manager factory should not be closed");
        var manager = jdoPersistenceManagerSet
                .stream()
                .filter(jdoPersistenceManager -> !jdoPersistenceManager.isClosed()
                        && jdoPersistenceManager.getPersistenceManagerFactory()
                        .equals(factory))
                .findFirst()
                .orElse(null);

        currentManager = ofNullable(manager).orElseGet(() -> {
            var newManager = (JDOPersistenceManager) factory.getPersistenceManager();
            jdoPersistenceManagerSet.add(newManager);
            return newManager;
        });

        return this;
    }

    /**
     * This method performs the switching to desired database by class of DB connection supplier.
     * <p>NOTE!</p>
     * It is expected that all instances of {@link DBConnectionSupplier} to find and use are loaded by SPI firstly.
     * @see <a href="https://docs.oracle.com/javase/tutorial/sound/SPI-intro.html">
     *     Introduction to the Service Provider Interfaces</a>
     *
     * @param dbConnectionSupplierClass is a class of DB connection supplier.
     * @return self-reference
     */
    DataBaseStepContext switchTo(Class<? extends DBConnectionSupplier> dbConnectionSupplierClass) {
        checkArgument(nonNull(dbConnectionSupplierClass),
                format("Subclass of %s should be defined", DBConnectionSupplier.class.getName()));
        return switchTo(getKnownConnection(dbConnectionSupplierClass, true));
    }

    void switchToDefault() {
        switchTo(defaultConnection);
    }

    public JDOPersistenceManager getCurrentPersistenceManager() {
        return ofNullable(currentManager)
                .orElseGet(() -> {
                    switchToDefault();
                    return currentManager;
                });
    }

    @Override
    public Thread getHookOnJvmStop() {
        return new Thread(() -> jdoPersistenceManagerSet.forEach(jdoPersistenceManager -> {
            jdoPersistenceManager.getPersistenceManagerFactory().close();
            jdoPersistenceManager.close();
        }));
    }

    @Override
    public void refresh() {
        switchToDefault();
    }
}
