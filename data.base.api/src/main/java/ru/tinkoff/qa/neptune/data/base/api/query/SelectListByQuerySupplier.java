package ru.tinkoff.qa.neptune.data.base.api.query;

import ru.tinkoff.qa.neptune.data.base.api.PersistableObject;

import javax.jdo.JDOQLTypedQuery;
import java.util.List;
import java.util.function.Function;

import static java.util.Optional.ofNullable;
import static ru.tinkoff.qa.neptune.core.api.conditions.ToGetSubIterable.getIterable;

public final class SelectListByQuerySupplier<T extends PersistableObject>
        extends ByQuerySequentialGetStepSupplier<T, List<T>, SelectListByQuerySupplier<T>> {

    private SelectListByQuerySupplier(QueryBuilderFunction<T> queryBuilder) {
        super(queryBuilder);
    }

    /**
     * Creates a supplier of a function that performs selection from a data base and returns the result as a list.
     *
     * @param queryBuilder is a builder of a query.
     * @param <T> is a type of am item from the result list.
     * @return created supplier of a function.
     */
    public static <T extends PersistableObject> SelectListByQuerySupplier<T> list(QueryBuilderFunction<T> queryBuilder) {
        return new SelectListByQuerySupplier<>(queryBuilder);
    }

    @Override
    protected Function<JDOQLTypedQuery<T>, List<T>> getEndFunction() {
        Function<JDOQLTypedQuery<T>, List<T>> listFunction = JDOQLTypedQuery::executeList;
        return ofNullable(condition).map(tPredicate ->
                ofNullable(nothingIsSelectedExceptionSupplier).map(nothingIsSelectedExceptionSupplier1 ->
                        getIterable("Get selection result as a list", listFunction, tPredicate,
                                timeToGetResult, false, true, nothingIsSelectedExceptionSupplier1))
                        .orElseGet(() -> getIterable("Get selection result as a list", listFunction, tPredicate,
                                timeToGetResult, false, true)))

                .orElseGet(() -> ofNullable(nothingIsSelectedExceptionSupplier)
                        .map(nothingIsSelectedExceptionSupplier1 -> getIterable("Get selection result as a list",
                                listFunction, timeToGetResult,  nothingIsSelectedExceptionSupplier1)
                        ).orElseGet(() ->
                                getIterable("Get selection result as a list", listFunction, timeToGetResult)
                        ));
    }
}
