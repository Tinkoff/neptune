package ru.tinkoff.qa.neptune.data.base.api.query;

import ru.tinkoff.qa.neptune.data.base.api.DataBaseStepContext;
import ru.tinkoff.qa.neptune.data.base.api.PersistableObject;

import javax.jdo.JDOQLTypedQuery;
import javax.jdo.Query;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

import static java.lang.String.format;
import static java.util.List.of;
import static java.util.Objects.nonNull;
import static java.util.Optional.ofNullable;
import static ru.tinkoff.qa.neptune.core.api.steps.conditions.ToGetSubIterable.getIterable;
import static ru.tinkoff.qa.neptune.data.base.api.ListOfStoredObjects.INFO_PERSISTABLE_INFO;

public abstract class SelectListByQuerySupplier<T, R>
        extends ByQuerySequentialGetStepSupplier<T, List<T>, R, SelectListByQuerySupplier<T, R>> {

    private SelectListByQuerySupplier(Function<DataBaseStepContext, R> query) {
        super(query);
    }

    /**
     * Creates a supplier of a function that performs selection from a data base and returns the result as a list.
     *
     * @param queryBuilder is a builder of a typed query.
     * @param <T>          is a type of an item from the result list.
     * @return created supplier of a function.
     */
    public static <T extends PersistableObject> SelectListByQuerySupplier<T, JDOQLTypedQuery<T>> listByQuery(QueryBuilderFunction<T> queryBuilder) {
        var className = queryBuilder.getTypeOfItemToSelect().getName();

        return new SelectListByQuerySupplier<>(queryBuilder) {
            @Override
            protected Function<JDOQLTypedQuery<T>, List<T>> getEndFunction() {
                return this.getResultFunction(format("List of stored database objects. Type %s", className),
                        jdoTypedQuery ->
                                new ListOfSelectObjects<>(jdoTypedQuery.executeList(), INFO_PERSISTABLE_INFO::apply)
                                .setQuery(jdoTypedQuery));
            }
        };
    }

    /**
     * Creates a supplier of a function that performs selection from a data base and returns the result as a list.
     *
     * @param queryBuilder is a builder of SELECT-SQL query.
     * @param <T>          is a type of an item from the result list.
     * @return created supplier of a function.
     */
    @SuppressWarnings("unchecked")
    public static <T> SelectListByQuerySupplier<T, Query<T>> listByQuery(SQLQueryBuilderFunction<T> queryBuilder) {
        var description = ofNullable(queryBuilder.getTypeOfRequiredValue())
                .map(aClass -> format("List of stored database objects. Type %s", aClass.getName()))
                .orElse("List of records from the database by SQL query");

        return new SelectListByQuerySupplier<>(queryBuilder) {
            @Override
            protected Function<Query<T>, List<T>> getEndFunction() {
                return this.getResultFunction(description, query -> {
                    if (nonNull(queryBuilder.getTypeOfRequiredValue())) {
                        return new ListOfSelectObjects<>(query.executeList(),
                                ts -> INFO_PERSISTABLE_INFO.apply((List<PersistableObject>) ts))
                                .setQuery(query);
                    }

                    var toBeReturned = new ArrayList<>();
                    var result = query.executeList();

                    result.forEach(o -> {
                        if (o.getClass().isArray()) {
                            toBeReturned.add(Arrays.asList(((Object[]) o)));
                        } else {
                            toBeReturned.add(of(o));
                        }
                    });
                    return new ListOfSelectObjects("List of records/values from the data base",
                            toBeReturned).setQuery(query);
                });
            }
        };
    }

    Function<R, List<T>> getResultFunction(String description, Function<R, List<T>> listFunction) {
        return ofNullable(condition).map(tPredicate ->
                ofNullable(nothingIsSelectedExceptionSupplier).map(nothingIsSelectedExceptionSupplier1 ->
                        getIterable(description, listFunction, tPredicate,
                                timeToGetResult, sleepTime, false, true, nothingIsSelectedExceptionSupplier1))
                        .orElseGet(() -> getIterable(description, listFunction, tPredicate,
                                timeToGetResult, sleepTime, false, true)))

                .orElseGet(() -> ofNullable(nothingIsSelectedExceptionSupplier)
                        .map(nothingIsSelectedExceptionSupplier1 -> getIterable(description,
                                listFunction, timeToGetResult, sleepTime, nothingIsSelectedExceptionSupplier1)
                        ).orElseGet(() ->
                                getIterable(description, listFunction, timeToGetResult, sleepTime)
                        ));
    }
}
