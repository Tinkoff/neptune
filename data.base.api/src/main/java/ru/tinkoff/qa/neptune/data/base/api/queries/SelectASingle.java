package ru.tinkoff.qa.neptune.data.base.api.queries;

import org.datanucleus.api.jdo.JDOPersistenceManager;
import ru.tinkoff.qa.neptune.core.api.event.firing.annotation.MakeFileCapturesOnFinishing;
import ru.tinkoff.qa.neptune.core.api.event.firing.annotation.MakeStringCapturesOnFinishing;
import ru.tinkoff.qa.neptune.core.api.steps.ConditionConcatenation;
import ru.tinkoff.qa.neptune.core.api.steps.SequentialGetStepSupplier;
import ru.tinkoff.qa.neptune.data.base.api.DataBaseStepContext;
import ru.tinkoff.qa.neptune.data.base.api.NothingIsSelectedException;
import ru.tinkoff.qa.neptune.data.base.api.PersistableObject;
import ru.tinkoff.qa.neptune.data.base.api.connection.data.DBConnectionSupplier;
import ru.tinkoff.qa.neptune.data.base.api.queries.jdoql.JDOQLQueryParameters;
import ru.tinkoff.qa.neptune.data.base.api.queries.jdoql.JDOQLResultQueryParams;
import ru.tinkoff.qa.neptune.data.base.api.queries.jdoql.ReadableJDOQuery;

import javax.jdo.query.PersistableExpression;
import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

import static com.google.common.base.Preconditions.checkArgument;
import static java.lang.String.format;
import static java.util.Optional.ofNullable;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static ru.tinkoff.qa.neptune.core.api.steps.StoryWriter.toGet;
import static ru.tinkoff.qa.neptune.data.base.api.properties.WaitingForQueryResultDuration.SLEEPING_TIME;
import static ru.tinkoff.qa.neptune.data.base.api.properties.WaitingForQueryResultDuration.WAITING_FOR_SELECTION_RESULT_TIME;
import static ru.tinkoff.qa.neptune.data.base.api.queries.JDOPersistenceManagerByConnectionSupplierClass.getConnectionBySupplierClass;
import static ru.tinkoff.qa.neptune.data.base.api.queries.JDOPersistenceManagerByPersistableClass.getConnectionByClass;
import static ru.tinkoff.qa.neptune.data.base.api.queries.ids.IdQuery.byIds;
import static ru.tinkoff.qa.neptune.data.base.api.queries.jdoql.JDOQLQuery.byJDOQLQuery;
import static ru.tinkoff.qa.neptune.data.base.api.queries.jdoql.JDOQLResultQuery.byJDOQLResultQuery;
import static ru.tinkoff.qa.neptune.data.base.api.queries.sql.SqlQuery.bySql;

/**
 * This class is designed to select a single object from a data store.
 *
 * @param <T> is a type of retrieved value
 * @param <M> is a type of an object to get result from
 */
@MakeFileCapturesOnFinishing
@MakeStringCapturesOnFinishing
public class SelectASingle<T, M> extends SequentialGetStepSupplier
        .GetObjectFromIterableChainedStepSupplier<DataBaseStepContext, T, M, SelectASingle<T, M>> {

    private SelectASingle(String description, Function<M, List<T>> originalFunction) {
        super(description, originalFunction);
        timeOut(WAITING_FOR_SELECTION_RESULT_TIME.get());
        pollingInterval(SLEEPING_TIME.get());
    }

    /**
     * Retrieves a single {@link PersistableObject} selected by query
     *
     * @param toSelect is a class of resulted {@link PersistableObject} to be returned
     * @param params   is an instance of {@link JDOQLQueryParameters} that describes how to select desired object
     * @param <R>      is a type of resulted {@link PersistableObject} to be returned
     * @param <Q>      is a type of {@link PersistableExpression} that represents {@code T} in query
     * @return new {@link SelectASingle}
     */
    public static <R extends PersistableObject, Q extends PersistableExpression<R>> SelectASingle<R, ReadableJDOQuery<R>> oneOf(Class<R> toSelect,
                                                                                                                                JDOQLQueryParameters<R, Q> params) {
        return new SelectASingle<R, ReadableJDOQuery<R>>(format("One of %s by JDO typed query", toSelect.getName()),
                byJDOQLQuery()) {
            protected Function<ReadableJDOQuery<R>, R> getEndFunction() {
                //such implementation is for advanced reporting
                return rReadableJDOQuery -> toGet(format("Result using JDO query %s",
                        rReadableJDOQuery.getInternalQuery()),
                        super.getEndFunction()).apply(rReadableJDOQuery);
            }
        }
                .from(getConnectionByClass(toSelect).andThen(manager ->
                        ofNullable(params)
                                .map(parameters -> parameters.buildQuery(new ReadableJDOQuery<>(manager, toSelect)))
                                .orElseGet(() -> new ReadableJDOQuery<>(manager, toSelect))));
    }

    /**
     * Retrieves a list of field values taken from a single one {@link PersistableObject} that selected by query.
     *
     * @param toSelectFrom is a class of resulted {@link PersistableObject} to take field values from
     * @param params       is an instance of {@link JDOQLResultQueryParams} that describes how to select desired object
     * @param <R>          is a type of resulted {@link PersistableObject} to take field values from
     * @param <Q>          is a type of {@link PersistableExpression} that represents {@code T} in query
     * @return new {@link SelectASingle}
     */
    public static <R extends PersistableObject, Q extends PersistableExpression<R>> SelectASingle<List<Object>, ReadableJDOQuery<R>> row(Class<R> toSelectFrom,
                                                                                                                                         JDOQLResultQueryParams<R, Q> params) {
        return new SelectASingle<List<Object>, ReadableJDOQuery<R>>(format("One row taken from %s by JDO query", toSelectFrom.getName()),
                byJDOQLResultQuery()) {
            protected Function<ReadableJDOQuery<R>, List<Object>> getEndFunction() {
                //such implementation is for advanced reporting
                return rReadableJDOQuery -> toGet(format("Result using JDO query %s",
                        rReadableJDOQuery.getInternalQuery()),
                        super.getEndFunction()).apply(rReadableJDOQuery);
            }
        }
                .from(getConnectionByClass(toSelectFrom).andThen(manager ->
                        ofNullable(params)
                                .map(parameters -> parameters.buildQuery(new ReadableJDOQuery<>(manager, toSelectFrom)))
                                .orElseGet(() -> new ReadableJDOQuery<>(manager, toSelectFrom))));
    }

    /**
     * Retrieves the first selected {@link PersistableObject}
     *
     * @param toSelect is a class of resulted {@link PersistableObject} to be returned
     * @param <R>      is a type of resulted {@link PersistableObject} to be returned
     * @return new {@link SelectASingle}
     */
    public static <R extends PersistableObject> SelectASingle<R, ReadableJDOQuery<R>> oneOf(Class<R> toSelect) {
        return oneOf(toSelect, null);
    }

    /**
     * Retrieves a single {@link PersistableObject} selected by known id.
     *
     * @param toSelect is a class of resulted {@link PersistableObject} to be returned
     * @param id       known id of the desired object
     * @param <R>      is a type of resulted {@link PersistableObject} to be returned
     * @return new {@link SelectASingle}
     */
    public static <R extends PersistableObject> SelectASingle<R, JDOPersistenceManager> oneOf(Class<R> toSelect,
                                                                                              Object id) {
        return new SelectASingle<>(format("One of %s by id %s",
                toSelect.getName(), id),
                byIds(toSelect, id))
                .from(getConnectionByClass(toSelect));
    }

    /**
     * Retrieves a single {@link PersistableObject} selected by sql-query.
     *
     * @param toSelect   is a class of resulted {@link PersistableObject} to be returned
     *
     * @param sql        is an sql query. Parameter mask ({@code ?}) is supported. It is important!:
     *                   <p>Sql query should be defined as below</p>
     *                   {@code 'Select * from Persons...'}
     *
     * @param parameters is an array of query parameters. It is necessary to define for queries as below
     *                   <p>
     *                   {@code 'Select * from Persons where Some_Field=?'}
     *                   </p>
     * @param <R>      is a type of resulted {@link PersistableObject} to be returned
     * @return new {@link SelectASingle}
     */
    public static <R extends PersistableObject> SelectASingle<R, JDOPersistenceManager> oneOf(Class<R> toSelect,
                                                                                              String sql,
                                                                                              Object... parameters) {
        return new SelectASingle<>(format("One of %s by query '%s'. " +
                        "Parameters: %s",
                toSelect.getName(),
                sql,
                Arrays.toString(parameters)),
                bySql(toSelect, sql, parameters))
                .from(getConnectionByClass(toSelect));
    }

    /**
     * Retrieves a list of raw objects taken from record of a data store. This record is selected by sql query.
     *
     * @param sql is an sql query. Parameter mask ({@code ?}) is supported.
     * @param connection is an  class of {@link DBConnectionSupplier} that actually describes how to connect and how to use
     *                   the data store
     * @param parameters is an array of query parameters. It is necessary to define for queries as below
     *                   <p>
     *                   {@code 'Select * from Persons where Some_Field=?'}
     *                   </p>
     * @param <R> is a type of {@link DBConnectionSupplier}
     * @return new {@link SelectASingle}
     */
    public static <R extends DBConnectionSupplier> SelectASingle<List<Object>, JDOPersistenceManager> row(String sql,
                                                                                                          Class<R> connection,
                                                                                                          Object... parameters) {
        return new SelectASingle<>(format("One row by query %s. " +
                        "The connection is described by %s. " +
                        "Parameters: %s",
                sql,
                connection.getName(),
                Arrays.toString(parameters)),
                bySql(sql, parameters))
                .from(getConnectionBySupplierClass(connection));
    }

    @Override
    public SelectASingle<T, M> timeOut(Duration timeOut) {
        return super.timeOut(timeOut);
    }

    @Override
    public SelectASingle<T, M> pollingInterval(Duration pollingTime) {
        return super.pollingInterval(pollingTime);
    }

    @Override
    public SelectASingle<T, M> criteria(ConditionConcatenation concat, Predicate<? super T> condition) {
        return super.criteria(concat, condition);
    }

    @Override
    public SelectASingle<T, M> criteria(ConditionConcatenation concat, String conditionDescription, Predicate<? super T> condition) {
        return super.criteria(concat, conditionDescription, condition);
    }

    @Override
    public SelectASingle<T, M> criteria(Predicate<? super T> condition) {
        return super.criteria(condition);
    }

    @Override
    public SelectASingle<T, M> criteria(String conditionDescription, Predicate<? super T> condition) {
        return super.criteria(conditionDescription, condition);
    }

    /**
     * To throw {@link NothingIsSelectedException} when the selecting retrieves no value.
     *
     * @param errorText as a text of the thrown exception
     * @return self reference
     */
    public SelectASingle<T, M> throwWhenResultEmpty(String errorText) {
        checkArgument(isNotBlank(errorText), "Please define not blank exception text");
        return super.throwOnEmptyResult(() -> new NothingIsSelectedException(errorText));
    }
}
