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

import javax.jdo.query.PersistableExpression;
import java.time.Duration;
import java.util.function.Function;
import java.util.function.Predicate;

import static com.google.common.base.Preconditions.checkArgument;
import static java.lang.String.format;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static ru.tinkoff.qa.neptune.data.base.api.queries.JDOPersistenceManagerByConnectionSupplierClass.getConnectionBySupplierClass;
import static ru.tinkoff.qa.neptune.data.base.api.queries.JDOPersistenceManagerByPersistableClass.getConnectionByClass;
import static ru.tinkoff.qa.neptune.data.base.api.queries.ids.IdQuery.byIds;
import static ru.tinkoff.qa.neptune.data.base.api.queries.jdoql.JDOQLQuery.byJDOQLQuery;
import static ru.tinkoff.qa.neptune.data.base.api.queries.sql.SqlQuery.bySql;

@MakeFileCapturesOnFinishing
@MakeStringCapturesOnFinishing
public final class SelectASingle<T> extends SequentialGetStepSupplier
        .GetObjectFromIterableChainedStepSupplier<DataBaseStepContext, T, JDOPersistenceManager, SelectASingle<T>> {


    private  <S extends Iterable<T>> SelectASingle(String description,
                                                   Function<JDOPersistenceManager, S> originalFunction) {
        super(description, originalFunction);
    }

    public static <R extends PersistableObject, Q extends PersistableExpression<R>> SelectASingle<R> oneOf(Class<R> toSelect,
                                                                                                           JDOQLQueryParameters<R, Q> params) {
        return new SelectASingle<>(format("One of %s by query %s",
                toSelect.getName(),
                params.toString()),
                byJDOQLQuery(toSelect).setParameters(params))
                .from(getConnectionByClass(toSelect));
    }

    public static <R extends PersistableObject, Q extends PersistableExpression<R>> SelectASingle<R> oneOf(Class<R> toSelect) {
        return new SelectASingle<>(format("One of %s", toSelect.getName()),
                byJDOQLQuery(toSelect))
                .from(getConnectionByClass(toSelect));
    }

    public static <R extends PersistableObject, Q extends PersistableExpression<R>> SelectASingle<R> oneOf(Class<R> toSelect,
                                                                                                           Object id) {
        return new SelectASingle<>(format("One of %s by id %s",
                toSelect.getName(), id),
                byIds(toSelect, id))
                .from(getConnectionByClass(toSelect));
    }

    public static <R extends PersistableObject> SelectASingle<R> oneOf(Class<R> toSelect, String sql) {
        return new SelectASingle<>(format("One of %s by query '%s'",
                toSelect.getName(),
                sql),
                bySql(toSelect, sql))
                .from(getConnectionByClass(toSelect));
    }

    public static <R extends DBConnectionSupplier> SelectASingle<Object> oneOf(String sql, Class<R> connection) {
        return new SelectASingle<>(format("One row by query %s. The connection is described by %s", sql, connection.getName()),
                bySql(sql))
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
    public SelectASingle<T> criteria(ConditionConcatenation concat, Predicate<? super T> condition) {
        return super.criteria(concat, condition);
    }

    @Override
    public SelectASingle<T> criteria(ConditionConcatenation concat, String conditionDescription, Predicate<? super T> condition) {
        return super.criteria(concat, conditionDescription, condition);
    }

    @Override
    public SelectASingle<T> criteria(Predicate<? super T> condition) {
        return super.criteria(condition);
    }

    @Override
    public SelectASingle<T> criteria(String conditionDescription, Predicate<? super T> condition) {
        return super.criteria(conditionDescription, condition);
    }

    public SelectASingle<T> throwWhenResultEmpty(String errorText) {
        checkArgument(isNotBlank(errorText), "Please define not blank exception text");
        return super.throwOnEmptyResult(() -> new NothingIsSelectedException(errorText));
    }
}
