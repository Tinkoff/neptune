package ru.tinkoff.qa.neptune.data.base.api.query;

import org.datanucleus.api.jdo.JDOPersistenceManagerFactory;
import ru.tinkoff.qa.neptune.core.api.SequentialGetStepSupplier;
import ru.tinkoff.qa.neptune.core.api.StoryWriter;
import ru.tinkoff.qa.neptune.data.base.api.DataBaseSteps;
import ru.tinkoff.qa.neptune.data.base.api.PersistableObject;
import ru.tinkoff.qa.neptune.data.base.api.persistence.data.PersistenceManagerFactorySupplier;

import javax.jdo.JDOQLTypedQuery;
import java.time.Duration;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

import static com.google.common.base.Preconditions.checkArgument;
import static java.lang.String.format;
import static java.util.Optional.ofNullable;
import static ru.tinkoff.qa.neptune.core.api.StoryWriter.toGet;
import static ru.tinkoff.qa.neptune.data.base.api.properties.DefaultWaitingForSelectionResultProperty.DEFAULT_WAITING_FOR_SELECTION_RESULT_PROPERTY;

@SuppressWarnings("unchecked")
public abstract class ByQuerySequentialGetStepSupplier<T extends PersistableObject, S, Q extends ByQuerySequentialGetStepSupplier<T, S, Q>>
        extends SequentialGetStepSupplier<DataBaseSteps, S, JDOQLTypedQuery<T>, Q> implements SelectSupplier<S> {

    private Object connectionDescription;
    private boolean toUseDefaultConnection;
    private final QueryBuilderFunction<T> queryBuilder;
    Predicate<T> condition;
    Supplier<NothingIsSelectedException> nothingIsSelectedExceptionSupplier;
    Duration timeToGetResult = DEFAULT_WAITING_FOR_SELECTION_RESULT_PROPERTY.get();

    ByQuerySequentialGetStepSupplier(QueryBuilderFunction<T> queryBuilder) {
        this.queryBuilder = queryBuilder;
    }

    /**
     * This method defines where (on which data base) query should be performed.
     * WARNING!!! After the query performing the instance of {@link DataBaseSteps} keeps the given connection
     * until another query is performed. This query should have another parameter set up by
     * {@link ByQuerySequentialGetStepSupplier#fromDbDescribedBy(JDOPersistenceManagerFactory)} or
     * {@link ByQuerySequentialGetStepSupplier#useDefaultConnection}. Also it is possible
     * to invoke {@link DataBaseSteps#switchTo(String)} or {@link DataBaseSteps#switchTo(JDOPersistenceManagerFactory)}
     * or {@link DataBaseSteps#switchToDefault()} for same purposes.
     *
     * @param persistenceUnitName is a name of dynamic persistence unit described by any subclass of
     *                            {@link PersistenceManagerFactorySupplier}
     * @return self-reference
     */
    public Q fromDbDescribedBy(String persistenceUnitName) {
        connectionDescription = persistenceUnitName;
        toUseDefaultConnection = false;
        return (Q) this;
    }

    /**
     * This method defines where (on which data base) query should be performed.
     * WARNING!!! After the query performing the instance of {@link DataBaseSteps} keeps the given connection
     * until another query is performed. This query should have another parameter set up by
     * {@link ByQuerySequentialGetStepSupplier#fromDbDescribedBy(String)} or
     * {@link ByQuerySequentialGetStepSupplier#useDefaultConnection}. Also it is possible
     * to invoke {@link DataBaseSteps#switchTo(String)} or {@link DataBaseSteps#switchTo(JDOPersistenceManagerFactory)}
     * or {@link DataBaseSteps#switchToDefault()} for same purposes.
     *
     * @param persistenceManagerFactory is not closed instance of {@link JDOPersistenceManagerFactory}
     * @return self-reference
     */
    public Q fromDbDescribedBy(JDOPersistenceManagerFactory persistenceManagerFactory) {
        connectionDescription = persistenceManagerFactory;
        toUseDefaultConnection = false;
        return (Q) this;
    }

    /**
     * This method says that query should be performed on some default connection described by any subclass of
     * {@link PersistenceManagerFactorySupplier} and its name defined by the property {@code `default.persistence.unit.name`}.
     * WARNING!!! After the query performing the instance of {@link DataBaseSteps} keeps default connection
     * until another query is performed. This query should have another parameter set up by
     * {@link ByQuerySequentialGetStepSupplier#fromDbDescribedBy(String)} or
     * {@link ByQuerySequentialGetStepSupplier#fromDbDescribedBy(JDOPersistenceManagerFactory)}. Also it is possible
     * to invoke {@link DataBaseSteps#switchTo(String)} or {@link DataBaseSteps#switchTo(JDOPersistenceManagerFactory)}
     * for same purposes.
     *
     * @return self-reference
     */
    public Q useDefaultConnection() {
        connectionDescription = null;
        toUseDefaultConnection = true;
        return (Q) this;
    }

    /**
     * Sometimes the performing of a query can take o lot of time. The better solution is to create lighter query
     * and filter result by some condition. It is necessary to describe given condition by {@link StoryWriter#condition(String, Predicate)}.
     *
     * @param condition is a predicate to filter the selection result.
     * @return self-reference
     */
    public Q withCondiotion(Predicate<T> condition) {
        checkArgument(condition != null, "Condition should be defined");
        this.condition = condition;
        return (Q) this;
    }

    /**
     * This method sets specific time to get valuable result.
     *
     * @param timeToGetValue time to get valuable result.
     * @return self-reference
     */
    public Q withTimeToGetValue(Duration timeToGetValue) {
        checkArgument(timeToGetValue != null, "Time to get value should be defined");
        this.timeToGetResult = timeToGetValue;
        return (Q) this;
    }

    /**
     * This methods says that an exception should be thrown when query returns an empty result.
     *
     * @param nothingIsSelectedExceptionSupplier is a suppler of exception to be thrown.
     * @return self-reference.
     */
    public Q toThowrowOnEmptyResult(Supplier<NothingIsSelectedException> nothingIsSelectedExceptionSupplier) {
        checkArgument(nothingIsSelectedExceptionSupplier != null, "Supplier of an exception should be defined");
        this.nothingIsSelectedExceptionSupplier = nothingIsSelectedExceptionSupplier;
        return (Q) this;
    }

    @Override
    public Function<DataBaseSteps, S> get() {
        if (toUseDefaultConnection) {
            super.from(queryBuilder);
            return super.get().compose(toGet("Use default connection", DataBaseSteps::switchToDefault));
        }

        ofNullable(connectionDescription).map(o -> {
            Class<?> objectClass = o.getClass();
            if (String.class.equals(objectClass)) {
                return super.from(queryBuilder.compose(toGet(format("Change connection by name %s", o),
                        dataBaseSteps -> dataBaseSteps.switchTo(String.valueOf(o)))));
            }

            if (JDOPersistenceManagerFactory.class.isAssignableFrom(objectClass)) {
                return super.from(queryBuilder.compose(toGet(format("Change connection by name %s", o),
                        dataBaseSteps -> dataBaseSteps.switchTo((JDOPersistenceManagerFactory) o))));
            }

            throw new IllegalArgumentException(format("Unknown description of a connection of type %s",
                    objectClass.getName()));
        }).orElseGet(() -> super.from(queryBuilder));

        return super.get();
    }
}
