package ru.tinkoff.qa.neptune.data.base.api.queries;

import org.datanucleus.api.jdo.JDOPersistenceManager;
import ru.tinkoff.qa.neptune.core.api.event.firing.annotation.MakeFileCapturesOnFinishing;
import ru.tinkoff.qa.neptune.core.api.event.firing.annotation.MakeStringCapturesOnFinishing;
import ru.tinkoff.qa.neptune.core.api.steps.Criteria;
import ru.tinkoff.qa.neptune.core.api.steps.SequentialGetStepSupplier;
import ru.tinkoff.qa.neptune.core.api.steps.parameters.StepParameter;
import ru.tinkoff.qa.neptune.data.base.api.DataBaseStepContext;
import ru.tinkoff.qa.neptune.data.base.api.NothingIsSelectedException;
import ru.tinkoff.qa.neptune.data.base.api.PersistableObject;
import ru.tinkoff.qa.neptune.data.base.api.connection.data.DBConnectionSupplier;
import ru.tinkoff.qa.neptune.data.base.api.queries.ids.Ids;
import ru.tinkoff.qa.neptune.data.base.api.queries.jdoql.JDOQLQueryParameters;
import ru.tinkoff.qa.neptune.data.base.api.queries.jdoql.JDOQLResultQueryParams;
import ru.tinkoff.qa.neptune.data.base.api.result.TableResultList;

import javax.jdo.query.PersistableExpression;
import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

import static com.google.common.base.Preconditions.checkArgument;
import static java.lang.String.format;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static ru.tinkoff.qa.neptune.data.base.api.PersistableObject.getTable;
import static ru.tinkoff.qa.neptune.data.base.api.properties.WaitingForQueryResultDuration.SLEEPING_TIME;
import static ru.tinkoff.qa.neptune.data.base.api.properties.WaitingForQueryResultDuration.WAITING_FOR_SELECTION_RESULT_TIME;
import static ru.tinkoff.qa.neptune.data.base.api.queries.JDOPersistenceManagerByConnectionSupplierClass.getConnectionBySupplierClass;
import static ru.tinkoff.qa.neptune.data.base.api.queries.JDOPersistenceManagerByPersistableClass.getConnectionByClass;
import static ru.tinkoff.qa.neptune.data.base.api.queries.jdoql.JDOQLQuery.byJDOQLQuery;
import static ru.tinkoff.qa.neptune.data.base.api.queries.jdoql.JDOQLResultQuery.byJDOQLResultQuery;
import static ru.tinkoff.qa.neptune.data.base.api.queries.sql.SqlQuery.bySql;

/**
 * This class is designed to select multiple objects from a data store.
 *
 * @param <T> is a type of retrieved values
 * @param <R> is a type of a list of retrieved values
 */
@MakeFileCapturesOnFinishing
@MakeStringCapturesOnFinishing
@SequentialGetStepSupplier.DefaultParameterNames(
        timeOut = "Time to get selected objects",
        criteria = "Object criteria"
)
public class SelectList<T, R extends List<T>> extends SequentialGetStepSupplier
        .GetIterableChainedStepSupplier<DataBaseStepContext, R, JDOPersistenceManager, T, SelectList<T, R>> {

    private final KeepResultPersistent resultPersistent;

    @StepParameter("By query")
    Query<T, R> query;

    private SelectList(String description, KeepResultPersistent resultPersistent, Query<T, R> selectBy) {
        super(description, selectBy::execute);
        this.resultPersistent = resultPersistent;
        this.query = selectBy;
        timeOut(WAITING_FOR_SELECTION_RESULT_TIME.get());
        pollingInterval(SLEEPING_TIME.get());
    }

    /**
     * Retrieves a list of {@link PersistableObject} selected by query
     *
     * @param toSelect is a class of each {@link PersistableObject} from returned list
     * @param params   is an instance of {@link JDOQLQueryParameters} that describes how to select desired objects
     * @param <R>      is a type of each {@link PersistableObject} from returned list
     * @param <Q>      is a type of {@link PersistableExpression} that represents {@code T} in query
     * @return new {@link ru.tinkoff.qa.neptune.data.base.api.queries.SelectList}
     */
    public static <R extends PersistableObject, Q extends PersistableExpression<R>> SelectList<R, List<R>> listOf(Class<R> toSelect,
                                                                                                                  JDOQLQueryParameters<R, Q> params) {
        var resultPersistent = new KeepResultPersistent();
        return new SelectList<>(format("List of '%s' from data store", getTable(toSelect)),
                resultPersistent,
                byJDOQLQuery(toSelect, params, resultPersistent)).
                from(getConnectionByClass(toSelect));
    }

    /**
     * Retrieves a list of lists. Each item is a list of field values taken from {@link PersistableObject}s that selected by query.
     *
     * @param toSelectFrom is a class of each {@link PersistableObject} to take field values from
     * @param params       is an instance of {@link JDOQLResultQueryParams} that describes how to select desired objects
     * @param <R>          is a type of each {@link PersistableObject} to take field values from
     * @param <Q>          is a type of {@link PersistableExpression} that represents {@code T} in query
     * @return new {@link ru.tinkoff.qa.neptune.data.base.api.queries.SelectList}
     */
    public static <R extends PersistableObject, Q extends PersistableExpression<R>> SelectList<List<Object>, TableResultList> rows(Class<R> toSelectFrom,
                                                                                                                                   JDOQLResultQueryParams<R, Q> params) {
        return new SelectList<>(format("Rows of data from data store. Rows are formed by records of '%s'", getTable(toSelectFrom)),
                null,
                byJDOQLResultQuery(toSelectFrom, params))
                .from(getConnectionByClass(toSelectFrom));
    }

    /**
     * Retrieves the full list {@link PersistableObject}
     *
     * @param toSelect is a class of each {@link PersistableObject} from returned list
     * @param <R>      is a type of each {@link PersistableObject} from returned list
     * @return new {@link ru.tinkoff.qa.neptune.data.base.api.queries.SelectList}
     */
    public static <R extends PersistableObject> SelectList<R, List<R>> listOf(Class<R> toSelect) {
        return listOf(toSelect, (JDOQLQueryParameters<R, PersistableExpression<R>>) null);
    }

    /**
     * Retrieves a list of {@link PersistableObject} selected by known ids.
     *
     * @param toSelect is a class of each {@link PersistableObject} from returned list
     * @param ids      is a wrapper of known ids used to find desired objects
     * @param <R>      is a type of each {@link PersistableObject} from returned list
     * @return new {@link ru.tinkoff.qa.neptune.data.base.api.queries.SelectList}
     */
    public static <R extends PersistableObject> SelectList<R, List<R>> listOf(Class<R> toSelect,
                                                                              Ids ids) {
        var resultPersistent = new KeepResultPersistent();
        return new SelectList<>(format("List of '%s' from data store", getTable(toSelect)),
                resultPersistent,
                ids.build(toSelect, resultPersistent))
                .from(getConnectionByClass(toSelect));
    }

    /**
     * Retrieves a list of  {@link PersistableObject} selected by sql-query.
     *
     * @param toSelect   is a class of each {@link PersistableObject} from returned list
     * @param sql        is an sql query. Parameter mask ({@code ?}) is supported. It is important!:
     *                   <p>Sql query should be defined as below</p>
     *                   {@code 'Select * from Persons...'}
     * @param parameters is an array of query parameters. It is necessary to define for queries as below
     *                   <p>
     *                   {@code 'Select * from Persons where Some_Field=?'}
     *                   </p>
     * @param <R>        is a type of each {@link PersistableObject} from returned list
     * @return new {@link ru.tinkoff.qa.neptune.data.base.api.queries.SelectList}
     */
    public static <R extends PersistableObject> SelectList<R, List<R>> listOf(Class<R> toSelect,
                                                                              String sql,
                                                                              Object... parameters) {
        var resultPersistent = new KeepResultPersistent();
        return new SelectList<>(format("List of '%s' from data store", getTable(toSelect)),
                resultPersistent,
                bySql(toSelect, sql, resultPersistent, parameters))
                .from(getConnectionByClass(toSelect));
    }

    /**
     * Retrieves a list of  {@link PersistableObject} selected by sql-query.
     *
     * @param toSelect   is a class of each {@link PersistableObject} from returned list
     * @param sql        is an sql query. Parameter naming is supported. It is important!:
     *                   <p>Sql query should be defined as below</p>
     *                   {@code 'Select * from Persons...'}
     * @param parameters is a map of parameter names and values. It is necessary to define for queries as below
     *                   <p>
     *                   {@code 'Select * from Persons where Some_Field=:paramName'}
     *                   </p>
     * @param <R>        is a type of each {@link PersistableObject} from returned list
     * @return new {@link ru.tinkoff.qa.neptune.data.base.api.queries.SelectList}
     */
    public static <R extends PersistableObject> SelectList<R, List<R>> listOf(Class<R> toSelect,
                                                                              String sql,
                                                                              Map<String, ?> parameters) {
        var resultPersistent = new KeepResultPersistent();
        return new SelectList<>(format("List of '%s' from data store", getTable(toSelect)),
                resultPersistent,
                bySql(toSelect, sql, resultPersistent, parameters))
                .from(getConnectionByClass(toSelect));
    }

    /**
     * Retrieves a list of lists. Each item is a list of raw objects taken from records of a data store. These records are selected by sql query.
     *
     * @param sql        is an sql query. Parameter mask ({@code ?}) is supported.
     * @param connection is an  class of {@link DBConnectionSupplier} that actually describes how to connect and how to use
     *                   the data store
     * @param parameters is an array of query parameters. It is necessary to define for queries as below
     *                   <p>
     *                   {@code 'Select * from Persons where Some_Field=?'}
     *                   </p>
     * @param <R>        is a type of {@link DBConnectionSupplier}
     * @return new {@link ru.tinkoff.qa.neptune.data.base.api.queries.SelectList}
     */
    public static <R extends DBConnectionSupplier> SelectList<List<Object>, TableResultList> rows(String sql,
                                                                                                  Class<R> connection,
                                                                                                  Object... parameters) {
        return new SelectList<>("Rows of raw data from data store",
                null,
                bySql(sql, parameters))
                .from(getConnectionBySupplierClass(connection));
    }

    /**
     * Retrieves a list of lists. Each item is a list of raw objects taken from records of a data store. These records are selected by sql query.
     *
     * @param sql        is an sql query. Parameter naming is supported.
     * @param connection is an  class of {@link DBConnectionSupplier} that actually describes how to connect and how to use
     *                   the data store
     * @param parameters is a map of parameter names and values. It is necessary to define for queries as below
     *                   <p>
     *                   {@code 'Select * from Persons where Some_Field=:paramName'}
     *                   </p>
     * @param <R>        is a type of {@link DBConnectionSupplier}
     * @return new {@link ru.tinkoff.qa.neptune.data.base.api.queries.SelectList}
     */
    public static <R extends DBConnectionSupplier> SelectList<List<Object>, TableResultList> rows(String sql,
                                                                                                  Class<R> connection,
                                                                                                  Map<String, ?> parameters) {
        return new SelectList<>("Rows of raw data from data store",
                null,
                bySql(sql, parameters))
                .from(getConnectionBySupplierClass(connection));
    }

    @Override
    public SelectList<T, R> timeOut(Duration timeOut) {
        return super.timeOut(timeOut);
    }

    @Override
    public SelectList<T, R> pollingInterval(Duration pollingTime) {
        return super.pollingInterval(pollingTime);
    }

    @Override
    public SelectList<T, R> criteria(Criteria<? super T> criteria) {
        return super.criteria(criteria);
    }

    @Override
    public SelectList<T, R> criteria(String conditionDescription, Predicate<? super T> condition) {
        return super.criteria(conditionDescription, condition);
    }

    /**
     * To throw {@link NothingIsSelectedException} when the selecting retrieves empty list.
     *
     * @param errorText as a text of the thrown exception
     * @return self reference
     */
    public SelectList<T, R> throwWhenResultEmpty(String errorText) {
        checkArgument(isNotBlank(errorText), "Please define not blank exception text");
        return super.throwOnEmptyResult(() -> new NothingIsSelectedException(errorText));
    }

    KeepResultPersistent getResultPersistent() {
        return resultPersistent;
    }
}
