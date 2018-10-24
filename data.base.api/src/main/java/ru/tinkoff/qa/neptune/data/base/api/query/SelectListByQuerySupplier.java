package ru.tinkoff.qa.neptune.data.base.api.query;

import ru.tinkoff.qa.neptune.data.base.api.DataBaseSteps;
import ru.tinkoff.qa.neptune.data.base.api.PersistableList;
import ru.tinkoff.qa.neptune.data.base.api.PersistableObject;

import javax.jdo.JDOQLTypedQuery;
import javax.jdo.Query;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

import static java.util.List.of;
import static java.util.Optional.ofNullable;
import static ru.tinkoff.qa.neptune.core.api.conditions.ToGetSubIterable.getIterable;

public abstract class SelectListByQuerySupplier<T, R>
        extends ByQuerySequentialGetStepSupplier<T, List<T>, R, SelectListByQuerySupplier<T, R>> {

    private static final String DESCRIPTION = "Selection result as a list";

    private SelectListByQuerySupplier(Function<DataBaseSteps, R> query) {
        super(query);
    }

    /**
     * Creates a supplier of a function that performs selection from a data base and returns the result as a list.
     *
     * @param queryBuilder is a builder of a typed query.
     * @param <T> is a type of an item from the result list.
     * @return created supplier of a function.
     */
    public static <T extends PersistableObject> SelectListByQuerySupplier<T, JDOQLTypedQuery<T>> listByQuery(QueryBuilderFunction<T> queryBuilder) {
        return new SelectListByQuerySupplier<>(queryBuilder) {
            @Override
            protected Function<JDOQLTypedQuery<T>, List<T>> getEndFunction() {
                return this.getResultFunction(jdoTypedQuery ->
                        new PersistableList<>(jdoTypedQuery.executeList()));
            }
        };
    }

    /**
     * Creates a supplier of a function that performs selection from a data base and returns the result as a list.
     *
     * @param queryBuilder is a builder of SELECT-SQL query.
     * @param <T> is a type of an item from the result list.
     * @return created supplier of a function.
     */
    @SuppressWarnings("unchecked")
    public static <T> SelectListByQuerySupplier<T, Query<T>> listByQuery(SQLQueryBuilderFunction<T> queryBuilder) {
        return new SelectListByQuerySupplier<>(queryBuilder) {
            @Override
            protected Function<Query<T>, List<T>> getEndFunction() {
                return this.getResultFunction(query -> {
                    if (queryBuilder.getTypeOfRequiredValue() != null) {
                        return new PersistableList<>(query.executeList());
                    }
                    var toBeReturned = new PersistableList<>();
                    var result = query.executeList();
                    result.forEach(o -> {
                        if (o.getClass().isArray()) {
                            toBeReturned.add(Arrays.asList(((Object[]) o)));
                        }
                        else {
                            toBeReturned.add(of(o));
                        }
                    });
                    return (List<T>) toBeReturned;
                });
            }
        };
    }

    Function<R, List<T>>  getResultFunction(Function<R, List<T>> listFunction) {
        return ofNullable(condition).map(tPredicate ->
                ofNullable(nothingIsSelectedExceptionSupplier).map(nothingIsSelectedExceptionSupplier1 ->
                        getIterable(DESCRIPTION, listFunction, tPredicate,
                                timeToGetResult, false, true, nothingIsSelectedExceptionSupplier1))
                        .orElseGet(() -> getIterable(DESCRIPTION, listFunction, tPredicate,
                                timeToGetResult, false, true)))

                .orElseGet(() -> ofNullable(nothingIsSelectedExceptionSupplier)
                        .map(nothingIsSelectedExceptionSupplier1 -> getIterable(DESCRIPTION,
                                listFunction, timeToGetResult, nothingIsSelectedExceptionSupplier1)
                        ).orElseGet(() ->
                                getIterable(DESCRIPTION, listFunction, timeToGetResult)
                        ));
    }
}
