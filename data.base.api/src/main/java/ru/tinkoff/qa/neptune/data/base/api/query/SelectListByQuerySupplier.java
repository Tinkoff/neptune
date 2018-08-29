package ru.tinkoff.qa.neptune.data.base.api.query;

import ru.tinkoff.qa.neptune.data.base.api.PersistableObject;

import javax.jdo.JDOQLTypedQuery;
import java.util.List;
import java.util.function.Function;

import static java.util.Optional.ofNullable;
import static ru.tinkoff.qa.neptune.core.api.conditions.ToGetSubIterable.getIterable;

public final class SelectListByQuerySupplier<T extends PersistableObject>
        extends ByQuerySequentialGetStepSupplier<T, List<T>, SelectListByQuerySupplier<T>> {

    private static final String DESCRIPTION = "Get selection result as a list";

    private SelectListByQuerySupplier(QueryBuilderFunction<T> queryBuilder) {
        super(queryBuilder);
    }

    /**
     * Creates a supplier of a function that performs selection from a data base and returns the result as a list.
     *
     * @param queryBuilder is a builder of a query.
     * @param <T> is a type of an item from the result list.
     * @return created supplier of a function.
     */
    public static <T extends PersistableObject> SelectListByQuerySupplier<T> listByQuery(QueryBuilderFunction<T> queryBuilder) {
        return new SelectListByQuerySupplier<>(queryBuilder);
    }

    @Override
    protected Function<JDOQLTypedQuery<T>, List<T>> getEndFunction() {
        Function<JDOQLTypedQuery<T>, List<T>> listFunction = jdoTypedQuery ->
                new PersistableList<>(jdoTypedQuery.executeList());

        return ofNullable(condition).map(tPredicate ->
                ofNullable(nothingIsSelectedExceptionSupplier).map(nothingIsSelectedExceptionSupplier1 ->
                        getIterable(DESCRIPTION, listFunction, tPredicate,
                                timeToGetResult, false, true, nothingIsSelectedExceptionSupplier1))
                        .orElseGet(() -> getIterable(DESCRIPTION, listFunction, tPredicate,
                                timeToGetResult, false, true)))

                .orElseGet(() -> ofNullable(nothingIsSelectedExceptionSupplier)
                        .map(nothingIsSelectedExceptionSupplier1 -> getIterable(DESCRIPTION,
                                listFunction, timeToGetResult,  nothingIsSelectedExceptionSupplier1)
                        ).orElseGet(() ->
                                getIterable(DESCRIPTION, listFunction, timeToGetResult)
                        ));
    }
}
