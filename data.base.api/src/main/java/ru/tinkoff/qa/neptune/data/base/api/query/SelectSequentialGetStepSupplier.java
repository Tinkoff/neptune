package ru.tinkoff.qa.neptune.data.base.api.query;

import org.datanucleus.api.jdo.JDOPersistenceManagerFactory;
import ru.tinkoff.qa.neptune.core.api.SequentialGetStepSupplier;
import ru.tinkoff.qa.neptune.data.base.api.DataBaseSteps;
import ru.tinkoff.qa.neptune.data.base.api.persistence.data.PersistenceManagerFactorySupplier;

import java.time.Duration;
import java.util.function.Supplier;

import static com.google.common.base.Preconditions.checkArgument;
import static ru.tinkoff.qa.neptune.data.base.api.properties.DefaultWaitingForSelectionResultProperty.DEFAULT_WAITING_FOR_SELECTION_RESULT_PROPERTY;

@SuppressWarnings("unchecked")
public abstract class SelectSequentialGetStepSupplier<T, Q, R extends SelectSequentialGetStepSupplier<T, Q, R>>
        extends SequentialGetStepSupplier<DataBaseSteps, T, Q, R> {

    Object connectionDescription;
    boolean toUseDefaultConnection = true;
    Supplier<NothingIsSelectedException> nothingIsSelectedExceptionSupplier;
    Duration timeToGetResult = DEFAULT_WAITING_FOR_SELECTION_RESULT_PROPERTY.get();

    /**
     * This method defines where (on which data base) query should be performed.
     * WARNING!!! After the query performing the instance of {@link DataBaseSteps} keeps the given connection
     * until another query is performed. This query should have another parameter set up by
     * {@link #fromDbDescribedBy(JDOPersistenceManagerFactory)} or
     * {@link #useDefaultConnection}. Also it is possible
     * to invoke {@link DataBaseSteps#switchTo(String)} or {@link DataBaseSteps#switchTo(JDOPersistenceManagerFactory)}
     * or {@link DataBaseSteps#switchToDefault()} for same purposes.
     *
     * @param persistenceUnitName is a name of dynamic persistence unit described by any subclass of
     *                            {@link PersistenceManagerFactorySupplier}
     * @return self-reference
     */
    public R fromDbDescribedBy(String persistenceUnitName) {
        connectionDescription = persistenceUnitName;
        toUseDefaultConnection = false;
        return (R) this;
    }

    /**
     * This method defines where (on which data base) query should be performed.
     * WARNING!!! After the query performing the instance of {@link DataBaseSteps} keeps the given connection
     * until another query is performed. This query should have another parameter set up by
     * {@link #fromDbDescribedBy(String)} or
     * {@link #useDefaultConnection}. Also it is possible
     * to invoke {@link DataBaseSteps#switchTo(String)} or {@link DataBaseSteps#switchTo(JDOPersistenceManagerFactory)}
     * or {@link DataBaseSteps#switchToDefault()} for same purposes.
     *
     * @param persistenceManagerFactory is not closed instance of {@link JDOPersistenceManagerFactory}
     * @return self-reference
     */
    public R fromDbDescribedBy(JDOPersistenceManagerFactory persistenceManagerFactory) {
        connectionDescription = persistenceManagerFactory;
        toUseDefaultConnection = false;
        return (R) this;
    }

    /**
     * This method says that query should be performed on some default connection described by any subclass of
     * {@link PersistenceManagerFactorySupplier} and its name defined by the property {@code `default.persistence.unit.name`}.
     * WARNING!!! After the query performing the instance of {@link DataBaseSteps} keeps default connection
     * until another query is performed. This query should have another parameter set up by
     * {@link #fromDbDescribedBy(String)} or
     * {@link #fromDbDescribedBy(JDOPersistenceManagerFactory)}. Also it is possible
     * to invoke {@link DataBaseSteps#switchTo(String)} or {@link DataBaseSteps#switchTo(JDOPersistenceManagerFactory)}
     * for same purposes.
     *
     * @return self-reference
     */
    public R useDefaultConnection() {
        connectionDescription = null;
        toUseDefaultConnection = true;
        return (R) this;
    }

    /**
     * This method sets specific time to get valuable result.
     *
     * @param timeToGetValue time to get valuable result.
     * @return self-reference
     */
    public R withTimeToGetValue(Duration timeToGetValue) {
        checkArgument(timeToGetValue != null, "Time to get value should be defined");
        this.timeToGetResult = timeToGetValue;
        return (R) this;
    }

    /**
     * This methods says that an exception should be thrown when query returns an empty result.
     *
     * @param nothingIsSelectedExceptionSupplier is a suppler of exception to be thrown.
     * @return self-reference.
     */
    public R toThowrowOnEmptyResult(Supplier<NothingIsSelectedException> nothingIsSelectedExceptionSupplier) {
        checkArgument(nothingIsSelectedExceptionSupplier != null, "Supplier of an exception should be defined");
        this.nothingIsSelectedExceptionSupplier = nothingIsSelectedExceptionSupplier;
        return (R) this;
    }
}
