package ru.tinkoff.qa.neptune.data.base.api;

import ru.tinkoff.qa.neptune.core.api.steps.SequentialGetStepSupplier;
import ru.tinkoff.qa.neptune.core.api.event.firing.annotation.MakeFileCapturesOnFinishing;
import ru.tinkoff.qa.neptune.core.api.event.firing.annotation.MakeStringCapturesOnFinishing;
import ru.tinkoff.qa.neptune.data.base.api.connection.data.DBConnection;
import ru.tinkoff.qa.neptune.data.base.api.connection.data.DBConnectionSupplier;

import java.util.function.Function;

import static java.util.Optional.ofNullable;

@SuppressWarnings("unchecked")
@MakeStringCapturesOnFinishing
@MakeFileCapturesOnFinishing
public abstract class DBSequentialGetStepSupplier<T, Q, R extends DBSequentialGetStepSupplier<T, Q, R>>
        extends SequentialGetStepSupplier<DataBaseStepContext, T, Q, R> {

    private Object connection;

    protected static <T extends PersistableObject> T setQuery(T persistable, Object query) {
        return (T) persistable.setQuery(query);
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
    public Function<DataBaseStepContext, T> get() {
        var result = super.get();
        return new Function<>() {
            @Override
            public T apply(DataBaseStepContext dataBaseStepContext) {
                try {
                    return result.apply(ofNullable(connection).map(o -> {
                        if (DBConnection.class.equals(o.getClass())) {
                            return dataBaseStepContext.switchTo((DBConnection) o);
                        }

                        return dataBaseStepContext.switchTo((Class<? extends DBConnectionSupplier>) o);
                    }).orElse(dataBaseStepContext));
                } finally {
                    dataBaseStepContext.switchToDefault();
                }
            }

            public String toString() {
                return result.toString();
            }
        };
    }
}

