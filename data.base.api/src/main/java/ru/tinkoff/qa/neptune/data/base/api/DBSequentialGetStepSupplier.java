package ru.tinkoff.qa.neptune.data.base.api;

import org.datanucleus.api.jdo.JDOPersistenceManagerFactory;
import ru.tinkoff.qa.neptune.core.api.SequentialGetStepSupplier;
import ru.tinkoff.qa.neptune.data.base.api.persistence.data.PersistenceManagerFactorySupplier;

import java.util.function.Function;

import static java.lang.String.format;
import static java.util.Optional.ofNullable;
import static ru.tinkoff.qa.neptune.data.base.api.ChangePersistenceManagerByClassFunction.changeConnectionByClass;
import static ru.tinkoff.qa.neptune.data.base.api.ChangePersistenceManagerByPersistenceManagerFactory.changeConnectionByPersistenceManagerFactory;
import static ru.tinkoff.qa.neptune.data.base.api.ChangePersistenceManagerToDefault.changeConnectionToDefault;

@SuppressWarnings("unchecked")
public abstract class DBSequentialGetStepSupplier<T, Q, R extends DBSequentialGetStepSupplier<T, Q, R>>
        extends SequentialGetStepSupplier<DataBaseStepContext, T, Q, R> {

    private Object connectionDescription;
    private boolean toUseDefaultConnection = false;

    /**
     * This method defines where (on which data base) query should be performed.
     * WARNING!!! After the query performing the instance of {@link DataBaseStepContext} keeps the given connection
     * until another query is performed. This query should have another parameter set up by
     * {@link #usePersistenceUnit(JDOPersistenceManagerFactory)} or
     * {@link #useDefaultPersistenceUnit}. Also it is possible
     * to invoke {@link DataBaseStepContext#switchTo(Class)} or {@link DataBaseStepContext#switchTo(JDOPersistenceManagerFactory)}
     * or {@link DataBaseStepContext#switchToDefault()} for same purposes.
     *
     * @param factorySupplier is a name of dynamic persistence unit described by any subclass
     *                        of {@link PersistenceManagerFactorySupplier}
     * @return self-reference
     */
    public R usePersistenceUnit(Class<? extends PersistenceManagerFactorySupplier> factorySupplier) {
        connectionDescription = factorySupplier;
        toUseDefaultConnection = false;
        return (R) this;
    }

    /**
     * This method defines where (on which data base) query should be performed.
     * WARNING!!! After the query performing the instance of {@link DataBaseStepContext} keeps the given connection
     * until another query is performed. This query should have another parameter set up by
     * {@link #usePersistenceUnit(Class)} or
     * {@link #useDefaultPersistenceUnit}. Also it is possible
     * to invoke {@link DataBaseStepContext#switchTo(Class)} or {@link DataBaseStepContext#switchTo(JDOPersistenceManagerFactory)}
     * or {@link DataBaseStepContext#switchToDefault()} for same purposes.
     *
     * @param persistenceManagerFactory is not closed instance of {@link JDOPersistenceManagerFactory}
     * @return self-reference
     */
    public R usePersistenceUnit(JDOPersistenceManagerFactory persistenceManagerFactory) {
        connectionDescription = persistenceManagerFactory;
        toUseDefaultConnection = false;
        return (R) this;
    }

    /**
     * This method says that query should be performed on some default connection described by any subclass of
     * {@link PersistenceManagerFactorySupplier} and its name defined by the property {@code `default.persistence.unit.name`}.
     * WARNING!!! After the query performing the instance of {@link DataBaseStepContext} keeps default connection
     * until another query is performed. This query should have another parameter set up by
     * {@link #usePersistenceUnit(Class)} or
     * {@link #usePersistenceUnit(JDOPersistenceManagerFactory)}. Also it is possible
     * to invoke {@link DataBaseStepContext#switchTo(Class)} or {@link DataBaseStepContext#switchTo(JDOPersistenceManagerFactory)}
     * for same purposes.
     *
     * @return self-reference
     */
    public R useDefaultPersistenceUnit() {
        connectionDescription = null;
        toUseDefaultConnection = true;
        return (R) this;
    }

    private Function<DataBaseStepContext, T> changeConnectionAndGet(Function<DataBaseStepContext, DataBaseStepContext> changeConnection) {
        return ofNullable(super.get()).map(f -> f.compose(changeConnection))
                .orElseThrow(() -> new IllegalStateException("Supplied function has null value"));
    }

    @Override
    public Function<DataBaseStepContext, T> get() {
        if (toUseDefaultConnection) {
            return changeConnectionAndGet(changeConnectionToDefault());
        }

        return ofNullable(connectionDescription).map(o -> {
            var objectClass = o.getClass();
            if (Class.class.isAssignableFrom(objectClass)) {
                return changeConnectionAndGet(changeConnectionByClass((Class<? extends PersistenceManagerFactorySupplier>) o));
            }

            if (JDOPersistenceManagerFactory.class.isAssignableFrom(objectClass)) {
                return changeConnectionAndGet(changeConnectionByPersistenceManagerFactory((JDOPersistenceManagerFactory) o));
            }

            throw new IllegalStateException(format("It is unknown what to do with a connection described by %s", objectClass.getName()));
        }).orElseGet(() -> changeConnectionAndGet(dataBaseSteps -> dataBaseSteps));
    }
}

