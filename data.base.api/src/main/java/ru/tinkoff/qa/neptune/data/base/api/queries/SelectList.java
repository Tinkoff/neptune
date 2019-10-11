package ru.tinkoff.qa.neptune.data.base.api.queries;

import org.datanucleus.api.jdo.JDOPersistenceManager;
import ru.tinkoff.qa.neptune.core.api.steps.ConditionConcatenation;
import ru.tinkoff.qa.neptune.core.api.steps.SequentialGetStepSupplier;
import ru.tinkoff.qa.neptune.data.base.api.DataBaseStepContext;
import ru.tinkoff.qa.neptune.data.base.api.NothingIsSelectedException;
import ru.tinkoff.qa.neptune.data.base.api.PersistableObject;
import ru.tinkoff.qa.neptune.data.base.api.queries.jdoql.JDOQLQueryParameters;

import javax.jdo.query.PersistableExpression;
import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

import static com.google.common.base.Preconditions.checkArgument;
import static java.lang.String.format;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static ru.tinkoff.qa.neptune.data.base.api.queries.JDOPersistenceManagerByPersistableClass.getConnectionByClass;
import static ru.tinkoff.qa.neptune.data.base.api.queries.ids.IdQuery.byIds;
import static ru.tinkoff.qa.neptune.data.base.api.queries.jdoql.JDOQLQuery.byJDOQLQuery;

public final class SelectList<T> extends SequentialGetStepSupplier
        .GetIterableChainedStepSupplier<DataBaseStepContext, List<T>, JDOPersistenceManager, T, SelectList<T>> {

    private SelectList(String description,
                         Function<DataBaseStepContext, JDOPersistenceManager> from,
                         Function<JDOPersistenceManager, List<T>> originalFunction) {
        super(description, originalFunction);
        from(from);
    }

    public static <R extends PersistableObject, Q extends PersistableExpression<R>> SelectList<R> listOf(Class<R> toSelect,
                                                                                                         JDOQLQueryParameters<R, Q> params) {
        return new SelectList<>(format("List of %s by query %s",
                toSelect.getName(),
                params.toString()),
                getConnectionByClass(toSelect),
                byJDOQLQuery(toSelect).setParameters(params));
    }

    public static <R extends PersistableObject, Q extends PersistableExpression<R>> SelectList<R> listOf(Class<R> toSelect) {
        return new SelectList<>(format("List of %s", toSelect.getName()),
                getConnectionByClass(toSelect),
                byJDOQLQuery(toSelect));
    }

    public static <R extends PersistableObject, Q extends PersistableExpression<R>> SelectList<R> listOf(Class<R> toSelect,
                                                                                                         Object... ids) {
        return new SelectList<>(format("List of %s by ids [%s]",
                toSelect.getName(),
                Arrays.toString(ids)),
                getConnectionByClass(toSelect),
                byIds(toSelect, ids));
    }

    @Override
    public SelectList<T>  timeOut(Duration timeOut) {
        return super.timeOut(timeOut);
    }

    @Override
    public SelectList<T>  pollingInterval(Duration pollingTime) {
        return super.pollingInterval(pollingTime);
    }

    @Override
    public SelectList<T>  criteria(ConditionConcatenation concat, Predicate<? super T> condition) {
        return super.criteria(concat, condition);
    }

    @Override
    public SelectList<T>  criteria(ConditionConcatenation concat, String conditionDescription, Predicate<? super T> condition) {
        return super.criteria(concat, conditionDescription, condition);
    }

    @Override
    public SelectList<T>  criteria(Predicate<? super T> condition) {
        return super.criteria(condition);
    }

    @Override
    public SelectList<T>  criteria(String conditionDescription, Predicate<? super T> condition) {
        return super.criteria(conditionDescription, condition);
    }

    public SelectList<T>  throwWhenResultEmpty(String errorText) {
        checkArgument(isNotBlank(errorText), "Please define not blank exception text");
        return super.throwOnEmptyResult(() -> new NothingIsSelectedException(errorText));
    }
}
