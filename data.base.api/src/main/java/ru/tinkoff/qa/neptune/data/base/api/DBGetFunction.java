package ru.tinkoff.qa.neptune.data.base.api;

import org.datanucleus.api.jdo.JDOPersistenceManager;
import ru.tinkoff.qa.neptune.core.api.steps.SequentialGetStepSupplier;
import ru.tinkoff.qa.neptune.data.base.api.connection.data.DBConnection;
import ru.tinkoff.qa.neptune.data.base.api.connection.data.DBConnectionSupplier;

import java.util.function.Function;

import static java.util.Optional.ofNullable;

@Deprecated
@SuppressWarnings("unchecked")
public class DBGetFunction<T, R extends DBGetFunction> implements Function<DataBaseStepContextDeprecated, T> {

    private Object connection;
    private final SequentialGetStepSupplier<JDOPersistenceManager, T, ?, ?, ?> innerSupplier;

    protected <S extends SequentialGetStepSupplier<JDOPersistenceManager, T, ?, ?, ?>> DBGetFunction(S innerSupplier){
        this.innerSupplier = innerSupplier;
    }

    /**
     * This method defines where (which data base) some result should be received.
     *
     * @param dbConnectionSupplier is a subclass of {@link DBConnectionSupplier} that is designed to build instances
     *                             of {@link DBConnection}.
     *                             <p>NOTE!</p>
     *                             It is expected that all instances of {@link DBConnectionSupplier} to find and use are loaded by SPI firstly.
     * @return self-reference
     * @see <a href="https://docs.oracle.com/javase/tutorial/sound/SPI-intro.html">
     * Introduction to the Service Provider Interfaces</a>     *
     */
    public R useConnection(Class<? extends DBConnectionSupplier> dbConnectionSupplier) {
        connection = dbConnectionSupplier;
        return (R) this;
    }

    /**
     * This method defines where (on which data base) some result should be received.
     *
     * @param connection is a wrapper of connection parameters to desired data base
     * @return self-reference
     */
    public R useConnection(DBConnection connection) {
        this.connection = connection;
        return (R) this;
    }

    @Override
    public T apply(DataBaseStepContextDeprecated dataBaseStepContext) {
        try {
            ofNullable(connection).ifPresent(o -> {
                if (DBConnection.class.equals(o.getClass())) {
                    dataBaseStepContext.switchTo((DBConnection) o);
                    return;
                }
                dataBaseStepContext.switchTo((Class<? extends DBConnectionSupplier>) o);
            });
            return innerSupplier.get().apply(dataBaseStepContext.getCurrentPersistenceManager());
        }
        finally {
            dataBaseStepContext.switchToDefault();
        }
    }

    public String toString() {
        return innerSupplier.toString();
    }
}

