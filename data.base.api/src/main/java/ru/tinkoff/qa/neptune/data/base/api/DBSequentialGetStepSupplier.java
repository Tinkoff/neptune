package ru.tinkoff.qa.neptune.data.base.api;

import org.datanucleus.api.jdo.JDOPersistenceManagerFactory;
import ru.tinkoff.qa.neptune.core.api.SequentialGetStepSupplier;
import ru.tinkoff.qa.neptune.data.base.api.persistence.data.PersistenceManagerFactorySupplier;

import java.util.function.Function;

import static java.util.Optional.ofNullable;
import static ru.tinkoff.qa.neptune.data.base.api.ChangePersistenceManagerByNameFunction.changeConnectionByName;
import static ru.tinkoff.qa.neptune.data.base.api.ChangePersistenceManagerByPersistenceManagerFactory.changeConnectionByPersistenceManagerFactory;
import static ru.tinkoff.qa.neptune.data.base.api.ChangePersistenceManagerToDefault.changeConnectionToDefault;

@SuppressWarnings("unchecked")
public abstract class DBSequentialGetStepSupplier<T, Q, R extends DBSequentialGetStepSupplier<T, Q, R>>
        extends SequentialGetStepSupplier<DataBaseSteps, T, Q, R> {

    protected Object connectionDescription;
    protected boolean toUseDefaultConnection = true;

    /**
     * This method defines where (on which data base) query should be performed.
     * WARNING!!! After the query performing the instance of {@link DataBaseSteps} keeps the given connection
     * until another query is performed. This query should have another parameter set up by
     * {@link #usePersistenceUnit(JDOPersistenceManagerFactory)} or
     * {@link #useDefaultPersistenceUnit}. Also it is possible
     * to invoke {@link DataBaseSteps#switchTo(String)} or {@link DataBaseSteps#switchTo(JDOPersistenceManagerFactory)}
     * or {@link DataBaseSteps#switchToDefault()} for same purposes.
     *
     * @param persistenceUnitName is a name of dynamic persistence unit described by any subclass of
     *                            {@link PersistenceManagerFactorySupplier}
     * @return self-reference
     */
    public R usePersistenceUnit(String persistenceUnitName) {
        connectionDescription = persistenceUnitName;
        toUseDefaultConnection = false;
        return (R) this;
    }

    /**
     * This method defines where (on which data base) query should be performed.
     * WARNING!!! After the query performing the instance of {@link DataBaseSteps} keeps the given connection
     * until another query is performed. This query should have another parameter set up by
     * {@link #usePersistenceUnit(String)} or
     * {@link #useDefaultPersistenceUnit}. Also it is possible
     * to invoke {@link DataBaseSteps#switchTo(String)} or {@link DataBaseSteps#switchTo(JDOPersistenceManagerFactory)}
     * or {@link DataBaseSteps#switchToDefault()} for same purposes.
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
     * WARNING!!! After the query performing the instance of {@link DataBaseSteps} keeps default connection
     * until another query is performed. This query should have another parameter set up by
     * {@link #usePersistenceUnit(String)} or
     * {@link #usePersistenceUnit(JDOPersistenceManagerFactory)}. Also it is possible
     * to invoke {@link DataBaseSteps#switchTo(String)} or {@link DataBaseSteps#switchTo(JDOPersistenceManagerFactory)}
     * for same purposes.
     *
     * @return self-reference
     */
    public R useDefaultPersistenceUnit() {
        connectionDescription = null;
        toUseDefaultConnection = true;
        return (R) this;
    }

    private Function<DataBaseSteps, T> changeConnectionAndGet(Function<DataBaseSteps, DataBaseSteps> changeConnection) {
        return ofNullable(super.get()).map(f -> f.compose(changeConnection))
                .orElseThrow(() -> new IllegalStateException("Supplied function has null value"));
    }

    @Override
    public Function<DataBaseSteps, T> get() {
        if (toUseDefaultConnection) {
            return changeConnectionAndGet(changeConnectionToDefault());
        }

        Class<?> objectClass = connectionDescription.getClass();
        if (String.class.equals(objectClass)) {
            return changeConnectionAndGet(changeConnectionByName(String.valueOf(connectionDescription)));
        }

        if (JDOPersistenceManagerFactory.class.isAssignableFrom(objectClass)) {
            return changeConnectionAndGet(changeConnectionByPersistenceManagerFactory((JDOPersistenceManagerFactory) connectionDescription));
        }

        throw new IllegalStateException("It is unknown what to do with a connection");
    }
}

