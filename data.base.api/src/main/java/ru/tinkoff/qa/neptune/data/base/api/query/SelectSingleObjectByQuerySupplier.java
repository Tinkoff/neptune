package ru.tinkoff.qa.neptune.data.base.api.query;

import ru.tinkoff.qa.neptune.data.base.api.DataBaseSteps;
import ru.tinkoff.qa.neptune.data.base.api.PersistableList;
import ru.tinkoff.qa.neptune.data.base.api.PersistableObject;

import javax.jdo.JDOQLTypedQuery;
import javax.jdo.Query;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

import static java.util.Optional.ofNullable;
import static ru.tinkoff.qa.neptune.core.api.conditions.ToGetObjectFromIterable.getFromIterable;

public abstract class SelectSingleObjectByQuerySupplier<T, R>
        extends ByQuerySequentialGetStepSupplier<T, T, R, SelectSingleObjectByQuerySupplier<T, R>>  {

    private static final String DESCRIPTION = "Selection result as a single item";


    private SelectSingleObjectByQuerySupplier(Function<DataBaseSteps, R> query) {
        super(query);
    }

    /**
     * Creates a supplier of a function that performs selection from a data base and returns a single element.
     *
     * @param queryBuilder is a builder of a query.
     * @param <T> is a type of result element.
     * @return created supplier of a function.
     */
    public static <T extends PersistableObject> SelectSingleObjectByQuerySupplier<T, JDOQLTypedQuery<T>> aSingleByQuery(QueryBuilderFunction<T> queryBuilder) {
        return new SelectSingleObjectByQuerySupplier<>(queryBuilder) {
            @Override
            protected Function<JDOQLTypedQuery<T>, T> getEndFunction() {
                return getResultFunction(jdoTypedQuery -> new PersistableList<>(jdoTypedQuery.executeList()));
            }
        };
    }

    /**
     * Creates a supplier of a function that performs selection from a data base and returns a single element.
     *
     * @param queryBuilder is a builder of SELECT-SQL query.
     * @param <T> is a type of result element.
     * @return created supplier of a function.
     */
    @SuppressWarnings("unchecked")
    public static <T> SelectSingleObjectByQuerySupplier<T, Query<T>> aSingleByQuery(SQLQueryBuilderFunction<T> queryBuilder) {
        return new SelectSingleObjectByQuerySupplier<>(queryBuilder) {
            @Override
            protected Function<Query<T>, T> getEndFunction() {
                return getResultFunction(query -> {
                    if (queryBuilder.getTypeOfRequiredValue() != null) {
                        return new PersistableList<>(query.executeList());
                    }
                    PersistableList<List<Object>> toBeReturned = new PersistableList<>();
                    List<?> result = query.executeList();
                    result.forEach(o -> toBeReturned.add(Arrays.asList(((Object[]) o))));
                    return (List<T>) toBeReturned;
                });
            }
        };
    }

    Function<R, T>  getResultFunction(Function<R, List<T>> function) {
        return ofNullable(condition).map(tPredicate ->
                ofNullable(nothingIsSelectedExceptionSupplier).map(nothingIsSelectedExceptionSupplier1 ->
                        getFromIterable(DESCRIPTION, function, tPredicate,
                                timeToGetResult, false, true, nothingIsSelectedExceptionSupplier1))
                        .orElseGet(() -> getFromIterable(DESCRIPTION, function, tPredicate,
                                timeToGetResult, false, true)))

                .orElseGet(() -> ofNullable(nothingIsSelectedExceptionSupplier)
                        .map(nothingIsSelectedExceptionSupplier1 -> getFromIterable(DESCRIPTION,
                                function, timeToGetResult,  nothingIsSelectedExceptionSupplier1)
                        ).orElseGet(() ->
                                getFromIterable(DESCRIPTION, function, timeToGetResult)
                        ));
    }
}
