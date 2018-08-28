package ru.tinkoff.qa.neptune.data.base.api.query;

import ru.tinkoff.qa.neptune.data.base.api.PersistableObject;

import javax.jdo.JDOQLTypedQuery;
import java.util.List;
import java.util.function.Function;

import static java.util.Optional.ofNullable;
import static ru.tinkoff.qa.neptune.core.api.conditions.ToGetObjectFromIterable.getFromIterable;

public final class SelectSingleObjectByQuerySupplier<T extends PersistableObject>
        extends ByQuerySequentialGetStepSupplier<T, T, SelectSingleObjectByQuerySupplier<T>>  {


    private SelectSingleObjectByQuerySupplier(QueryBuilderFunction<T> queryBuilder) {
        super(queryBuilder);
    }

    /**
     * Creates a supplier of a function that performs selection from a data base and returns a single element.
     *
     * @param queryBuilder is a builder of a query.
     * @param <T> is a type of result element.
     * @return created supplier of a function.
     */
    public static <T extends PersistableObject>  SelectSingleObjectByQuerySupplier<T> aSingle(QueryBuilderFunction<T> queryBuilder) {
        return new SelectSingleObjectByQuerySupplier<>(queryBuilder);
    }

    @Override
    protected Function<JDOQLTypedQuery<T>, T> getEndFunction() {
        Function<JDOQLTypedQuery<T>, List<T>> listFunction = JDOQLTypedQuery::executeList;
        return ofNullable(condition).map(tPredicate ->
                ofNullable(nothingIsSelectedExceptionSupplier).map(nothingIsSelectedExceptionSupplier1 ->
                        getFromIterable("Get selection result as a single item", listFunction, tPredicate,
                                timeToGetResult, false, true, nothingIsSelectedExceptionSupplier1))
                        .orElseGet(() -> getFromIterable("Get selection result as a single item", listFunction, tPredicate,
                                timeToGetResult, false, true)))

                .orElseGet(() -> ofNullable(nothingIsSelectedExceptionSupplier)
                        .map(nothingIsSelectedExceptionSupplier1 -> getFromIterable("Get selection result as a single item",
                                listFunction, timeToGetResult,  nothingIsSelectedExceptionSupplier1)
                        ).orElseGet(() ->
                                getFromIterable("Get selection result as a single item", listFunction, timeToGetResult)
                        ));
    }
}
