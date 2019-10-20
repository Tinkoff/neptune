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
import ru.tinkoff.qa.neptune.data.base.api.queries.jdoql.JDOQLQuery;
import ru.tinkoff.qa.neptune.data.base.api.queries.jdoql.JDOQLQueryParameters;
import ru.tinkoff.qa.neptune.data.base.api.queries.jdoql.ReadableJDOQuery;

import javax.jdo.query.PersistableExpression;
import java.time.Duration;
import java.util.function.Function;
import java.util.function.Predicate;

import static com.google.common.base.Preconditions.checkArgument;
import static java.lang.String.format;
import static java.util.Optional.ofNullable;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static ru.tinkoff.qa.neptune.core.api.steps.StoryWriter.toGet;
import static ru.tinkoff.qa.neptune.data.base.api.queries.JDOPersistenceManagerByConnectionSupplierClass.getConnectionBySupplierClass;
import static ru.tinkoff.qa.neptune.data.base.api.queries.JDOPersistenceManagerByPersistableClass.getConnectionByClass;
import static ru.tinkoff.qa.neptune.data.base.api.queries.ids.IdQuery.byIds;
import static ru.tinkoff.qa.neptune.data.base.api.queries.jdoql.JDOQLQuery.byJDOQLQuery;
import static ru.tinkoff.qa.neptune.data.base.api.queries.sql.SqlQuery.bySql;

@MakeFileCapturesOnFinishing
@MakeStringCapturesOnFinishing
public class SelectASingle<T, M> extends SequentialGetStepSupplier
        .GetObjectFromIterableChainedStepSupplier<DataBaseStepContext, T, M, SelectASingle<T, M>> {

    private <S extends Iterable<T>> SelectASingle(String description,
                                                  Function<M, S> originalFunction) {
        super(description, originalFunction);
    }

    public static <R extends PersistableObject, Q extends PersistableExpression<R>> SelectASingle<R, ReadableJDOQuery<R>> oneOf(Class<R> toSelect,
                                                                                                                                JDOQLQueryParameters<R, Q> params) {
        return new SelectASingle<>(format("One of %s by JDO typed query", toSelect.getName()),
                JDOQLQuery.<R>byJDOQLQuery()) {
            protected Function<ReadableJDOQuery<R>, R> getEndFunction() {
                //such implementation is for advanced reporting
                return rReadableJDOQuery -> toGet(format("Result using native query %s",
                        rReadableJDOQuery.getInternalQuery().getNativeQuery()),
                        super.getEndFunction()).apply(rReadableJDOQuery);
            }
        }
                .from(getConnectionByClass(toSelect).andThen(manager ->
                        ofNullable(params)
                                .map(parameters -> parameters.buildQuery(new ReadableJDOQuery<>(manager, toSelect)))
                                .orElseGet(() -> new ReadableJDOQuery<>(manager, toSelect))));
    }

    public static <R extends PersistableObject> SelectASingle<R, ReadableJDOQuery<R>> oneOf(Class<R> toSelect) {
        return oneOf(toSelect, (JDOQLQueryParameters<R, PersistableExpression<R>>) null);
    }

    public static <R extends PersistableObject> SelectASingle<R, JDOPersistenceManager> oneOf(Class<R> toSelect,
                                                                                              Object id) {
        return new SelectASingle<>(format("One of %s by id %s",
                toSelect.getName(), id),
                byIds(toSelect, id))
                .from(getConnectionByClass(toSelect));
    }

    public static <R extends PersistableObject> SelectASingle<R, JDOPersistenceManager> oneOf(Class<R> toSelect, String sql) {
        return new SelectASingle<>(format("One of %s by query '%s'",
                toSelect.getName(),
                sql),
                bySql(toSelect, sql))
                .from(getConnectionByClass(toSelect));
    }

    public static <R extends DBConnectionSupplier> SelectASingle<Object, JDOPersistenceManager> oneOf(String sql, Class<R> connection) {
        return new SelectASingle<>(format("One row by query %s. The connection is described by %s", sql, connection.getName()),
                bySql(sql))
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

    public SelectASingle<T, M> throwWhenResultEmpty(String errorText) {
        checkArgument(isNotBlank(errorText), "Please define not blank exception text");
        return super.throwOnEmptyResult(() -> new NothingIsSelectedException(errorText));
    }
}
