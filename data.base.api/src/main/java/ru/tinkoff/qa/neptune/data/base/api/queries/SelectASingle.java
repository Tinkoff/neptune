package ru.tinkoff.qa.neptune.data.base.api.queries;

import org.datanucleus.api.jdo.JDOPersistenceManager;
import ru.tinkoff.qa.neptune.core.api.event.firing.annotations.CaptureOnSuccess;
import ru.tinkoff.qa.neptune.core.api.event.firing.annotations.MaxDepthOfReporting;
import ru.tinkoff.qa.neptune.core.api.steps.Criteria;
import ru.tinkoff.qa.neptune.core.api.steps.Description;
import ru.tinkoff.qa.neptune.core.api.steps.DescriptionFragment;
import ru.tinkoff.qa.neptune.core.api.steps.SequentialGetStepSupplier;
import ru.tinkoff.qa.neptune.core.api.steps.parameters.StepParameter;
import ru.tinkoff.qa.neptune.data.base.api.DataBaseStepContext;
import ru.tinkoff.qa.neptune.data.base.api.NothingIsSelectedException;
import ru.tinkoff.qa.neptune.data.base.api.PersistableObject;
import ru.tinkoff.qa.neptune.data.base.api.captors.DBCaptor;
import ru.tinkoff.qa.neptune.data.base.api.connection.data.DBConnectionSupplier;
import ru.tinkoff.qa.neptune.data.base.api.queries.ids.Id;
import ru.tinkoff.qa.neptune.data.base.api.queries.jdoql.JDOQLQueryParameters;
import ru.tinkoff.qa.neptune.data.base.api.queries.jdoql.JDOQLResultQueryParams;

import javax.jdo.query.PersistableExpression;
import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

import static com.google.common.base.Preconditions.checkArgument;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static ru.tinkoff.qa.neptune.data.base.api.properties.WaitingForQueryResultDuration.SLEEPING_TIME;
import static ru.tinkoff.qa.neptune.data.base.api.properties.WaitingForQueryResultDuration.WAITING_FOR_SELECTION_RESULT_TIME;
import static ru.tinkoff.qa.neptune.data.base.api.queries.JDOPersistenceManagerByConnectionSupplierClass.getConnectionBySupplierClass;
import static ru.tinkoff.qa.neptune.data.base.api.queries.JDOPersistenceManagerByPersistableClass.getConnectionByClass;
import static ru.tinkoff.qa.neptune.data.base.api.queries.jdoql.JDOQLQuery.byJDOQLQuery;
import static ru.tinkoff.qa.neptune.data.base.api.queries.jdoql.JDOQLResultQuery.byJDOQLResultQuery;
import static ru.tinkoff.qa.neptune.data.base.api.queries.sql.SqlQuery.bySql;


/**
 * This class is designed to select a single object from a data store.
 *
 * @param <T> is a type of retrieved value
 */
@CaptureOnSuccess(by = DBCaptor.class)
@SequentialGetStepSupplier.DefineGetImperativeParameterName("Select from data base:")
@SequentialGetStepSupplier.DefineTimeOutParameterName("Time to select object")
@SequentialGetStepSupplier.DefineCriteriaParameterName("Record criteria")
@MaxDepthOfReporting(0)
public class SelectASingle<T> extends SequentialGetStepSupplier
        .GetObjectFromIterableChainedStepSupplier<DataBaseStepContext, T, JDOPersistenceManager, SelectASingle<T>> {

    private final KeepResultPersistent resultPersistent;

    @StepParameter("By query")
    Query<T, ? extends List<T>> query;

    private SelectASingle(KeepResultPersistent resultPersistent, Query<T, ? extends List<T>> selectBy) {
        super(selectBy::execute);
        this.resultPersistent = resultPersistent;
        this.query = selectBy;
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
     * @return new {@link ru.tinkoff.qa.neptune.data.base.api.queries.SelectASingle}
     */
    @Description("one record of '{of}'")
    public static <R extends PersistableObject, Q extends PersistableExpression<R>> SelectASingle<R> oneOf(
            @DescriptionFragment(
                    value = "of",
                    makeReadableBy = TableNameGetter.class) Class<R> toSelect,
            JDOQLQueryParameters<R, Q> params) {
        var resultPersistent = new KeepResultPersistent();
        return new SelectASingle<>(resultPersistent,
                byJDOQLQuery(toSelect, params, resultPersistent))
                .from(getConnectionByClass(toSelect));
    }

    /**
     * Retrieves a list of field values taken from a single one {@link PersistableObject} that selected by query.
     *
     * @param toSelectFrom is a class of resulted {@link PersistableObject} to take field values from
     * @param params       is an instance of {@link JDOQLResultQueryParams} that describes how to select desired object
     * @param <R>          is a type of resulted {@link PersistableObject} to take field values from
     * @param <Q>          is a type of {@link PersistableExpression} that represents {@code T} in query
     * @return new {@link ru.tinkoff.qa.neptune.data.base.api.queries.SelectASingle}
     */
    @Description("the row of raw data from '{of}'")
    public static <R extends PersistableObject, Q extends PersistableExpression<R>> SelectASingle<List<Object>> row(
            @DescriptionFragment(
                    value = "of",
                    makeReadableBy = TableNameGetter.class) Class<R> toSelectFrom,
            JDOQLResultQueryParams<R, Q> params) {
        return new SelectASingle<>(null,
                byJDOQLResultQuery(toSelectFrom, params))
                .from(getConnectionByClass(toSelectFrom));
    }

    /**
     * Retrieves a single {@link PersistableObject} selected by known id.
     *
     * @param toSelect is a class of resulted {@link PersistableObject} to be returned
     * @param id       is a wrapper of known id used to find the desired object
     * @param <R>      is a type of resulted {@link PersistableObject} to be returned
     * @return new {@link ru.tinkoff.qa.neptune.data.base.api.queries.SelectASingle}
     */
    @Description("one record of '{of}' by id {id}")
    public static <R extends PersistableObject> SelectASingle<R> oneOf(
            @DescriptionFragment(
                    value = "of",
                    makeReadableBy = TableNameGetter.class) Class<R> toSelect,
            @DescriptionFragment(
                    value = "id") Id id) {
        var resultPersistent = new KeepResultPersistent();
        return new SelectASingle<>(resultPersistent,
                id.build(toSelect, resultPersistent))
                .from(getConnectionByClass(toSelect));
    }

    /**
     * Retrieves the first selected {@link PersistableObject}
     *
     * @param toSelect is a class of resulted {@link PersistableObject} to be returned
     * @param <R>      is a type of resulted {@link PersistableObject} to be returned
     * @return new {@link ru.tinkoff.qa.neptune.data.base.api.queries.SelectASingle}
     */
    public static <R extends PersistableObject> SelectASingle<R> oneOf(Class<R> toSelect) {
        return oneOf(toSelect, (JDOQLQueryParameters<R, ?>) null);
    }

    /**
     * Retrieves a single {@link PersistableObject} selected by sql-query.
     *
     * @param toSelect   is a class of resulted {@link PersistableObject} to be returned
     * @param sql        is an sql query. Parameter mask ({@code ?}) is supported. It is important!:
     *                   <p>Sql query should be defined as below</p>
     *                   {@code 'Select * from Persons...'}
     * @param parameters is an array of query parameters. It is necessary to define for queries as below
     *                   <p>
     *                   {@code 'Select * from Persons where Some_Field=?'}
     *                   </p>
     * @param <R>        is a type of resulted {@link PersistableObject} to be returned
     * @return new {@link ru.tinkoff.qa.neptune.data.base.api.queries.SelectASingle}
     */
    @Description("one record of '{of}'")
    public static <R extends PersistableObject> SelectASingle<R> oneOf(
            @DescriptionFragment(
                    value = "of",
                    makeReadableBy = TableNameGetter.class) Class<R> toSelect,
            String sql,
            Object... parameters) {
        var resultPersistent = new KeepResultPersistent();
        return new SelectASingle<>(resultPersistent,
                bySql(toSelect, sql, resultPersistent, parameters))
                .from(getConnectionByClass(toSelect));
    }

    /**
     * Retrieves a single {@link PersistableObject} selected by sql-query.
     *
     * @param toSelect   is a class of resulted {@link PersistableObject} to be returned
     * @param sql        is an sql query. Parameter naming is supported. It is important!:
     *                   <p>Sql query should be defined as below</p>
     *                   {@code 'Select * from Persons...'}
     * @param parameters is a map of parameter names and values. It is necessary to define for queries as below
     *                   <p>
     *                   {@code 'Select * from Persons where Some_Field=:paramName'}
     *                   </p>
     * @param <R>        is a type of resulted {@link PersistableObject} to be returned
     * @return new {@link ru.tinkoff.qa.neptune.data.base.api.queries.SelectASingle}
     */
    @Description("one record of '{of}'")
    public static <R extends PersistableObject> SelectASingle<R> oneOf(
            @DescriptionFragment(
                    value = "of",
                    makeReadableBy = TableNameGetter.class) Class<R> toSelect,
            String sql,
            Map<String, ?> parameters) {
        var resultPersistent = new KeepResultPersistent();
        return new SelectASingle<>(resultPersistent,
                bySql(toSelect, sql, resultPersistent, parameters))
                .from(getConnectionByClass(toSelect));
    }

    /**
     * Retrieves a list of raw objects taken from record of a data store. This record is selected by sql query.
     *
     * @param sql        is an sql query. Parameter mask ({@code ?}) is supported.
     * @param connection is an  class of {@link DBConnectionSupplier} that actually describes how to connect and how to use
     *                   the data store
     * @param parameters is an array of query parameters. It is necessary to define for queries as below
     *                   <p>
     *                   {@code 'Select * from Persons where Some_Field=?'}
     *                   </p>
     * @param <R>        is a type of {@link DBConnectionSupplier}
     * @return new {@link ru.tinkoff.qa.neptune.data.base.api.queries.SelectASingle}
     */
    @Description("the row of raw data from '{of}'")
    public static <R extends DBConnectionSupplier> SelectASingle<List<Object>> row(
            String sql,
            @DescriptionFragment(
                    value = "of",
                    makeReadableBy = TableNameGetter.class) Class<R> connection,
            Object... parameters) {
        return new SelectASingle<>(null,
                bySql(sql, parameters))
                .from(getConnectionBySupplierClass(connection));
    }

    /**
     * Retrieves a list of raw objects taken from record of a data store. This record is selected by sql query.
     *
     * @param sql        is an sql query. Parameter naming is supported.
     * @param connection is an  class of {@link DBConnectionSupplier} that actually describes how to connect and how to use
     *                   the data store
     * @param parameters is a map of parameter names and values. It is necessary to define for queries as below
     *                   <p>
     *                   {@code 'Select * from Persons where Some_Field=:paramName'}
     *                   </p>
     * @param <R>        is a type of {@link DBConnectionSupplier}
     * @return new {@link ru.tinkoff.qa.neptune.data.base.api.queries.SelectASingle}
     */
    @Description("the row of raw data from '{of}'")
    public static <R extends DBConnectionSupplier> SelectASingle<List<Object>> row(
            String sql,
            @DescriptionFragment(
                    value = "of",
                    makeReadableBy = TableNameGetter.class) Class<R> connection,
            Map<String, ?> parameters) {
        return new SelectASingle<>(null,
                bySql(sql, parameters))
                .from(getConnectionBySupplierClass(connection));
    }

    @Override
    public SelectASingle<T> timeOut(Duration timeOut) {
        return super.timeOut(timeOut);
    }

    @Override
    public SelectASingle<T> pollingInterval(Duration pollingTime) {
        return super.pollingInterval(pollingTime);
    }

    @Override
    public SelectASingle<T> criteria(Criteria<? super T> criteria) {
        return super.criteria(criteria);
    }

    @Override
    public SelectASingle<T> criteria(String conditionDescription, Predicate<? super T> condition) {
        return super.criteria(conditionDescription, condition);
    }

    /**
     * To throw {@link NothingIsSelectedException} when the selecting retrieves no value.
     *
     * @param errorText as a text of the thrown exception
     * @return self reference
     */
    public SelectASingle<T> throwWhenResultEmpty(String errorText) {
        checkArgument(isNotBlank(errorText), "Please define not blank exception text");
        return super.throwOnEmptyResult(() -> new NothingIsSelectedException(errorText));
    }

    KeepResultPersistent getResultPersistent() {
        return resultPersistent;
    }
}
