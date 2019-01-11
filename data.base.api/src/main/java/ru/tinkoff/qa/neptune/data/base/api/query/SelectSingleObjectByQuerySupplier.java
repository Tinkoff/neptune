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
import static java.util.stream.Collectors.toList;
import static ru.tinkoff.qa.neptune.core.api.conditions.ToGetObjectFromIterable.getFromIterable;

public abstract class SelectSingleObjectByQuerySupplier<T, R>
        extends ByQuerySequentialGetStepSupplier<T, T, R, SelectSingleObjectByQuerySupplier<T, R>>  {

    private SelectSingleObjectByQuerySupplier(Function<DataBaseStepContext, R> query) {
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
        var className = queryBuilder.getTypeOfItemToSelect().getName();

        return new SelectSingleObjectByQuerySupplier<>(queryBuilder) {
            @Override
            protected Function<JDOQLTypedQuery<T>, T> getEndFunction() {
                return getResultFunction(format("A single stored database object. Type %s", className),
                        jdoTypedQuery -> new ArrayList<>(jdoTypedQuery.executeList())
                                .stream().map(t -> setQuery(t, jdoTypedQuery))
                                .collect(toList()));
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
        var description = ofNullable(queryBuilder.getTypeOfRequiredValue())
                .map(aClass -> format("A single stored database object. Type %s", aClass.getName()))
                .orElse("A single record from the database by SQL query");

        return new SelectSingleObjectByQuerySupplier<>(queryBuilder) {
            @Override
            protected Function<Query<T>, T> getEndFunction() {
                return getResultFunction(description, query -> {
                    if (nonNull(queryBuilder.getTypeOfRequiredValue())) {
                        return new ArrayList<>(query.executeList())
                                .stream()
                                .map(t -> (T) setQuery((PersistableObject) t, query))
                                .collect(toList());
                    }
                    var toBeReturned = new ArrayList<>();
                    var result = query.executeList();

                    result.forEach(o -> {
                        var loggable = new LoggableElementList<>() {
                            @Override
                            public String toString() {
                                return "1 record/value from the data base";
                            }
                        }.setQuery(query);

                        if (o.getClass().isArray()) {
                            loggable.addAll(Arrays.asList(((Object[]) o)));
                        }
                        else {
                            loggable.addAll(of(o));
                        }
                        toBeReturned.add(loggable);
                    });
                    return (List<T>) toBeReturned;
                });
            }
        };
    }

    Function<R, T> getResultFunction(String description, Function<R, List<T>> function) {
        return ofNullable(condition).map(tPredicate ->
                ofNullable(nothingIsSelectedExceptionSupplier).map(nothingIsSelectedExceptionSupplier1 ->
                        getFromIterable(description, function, tPredicate,
                                timeToGetResult, false, true, nothingIsSelectedExceptionSupplier1))
                        .orElseGet(() -> getFromIterable(description, function, tPredicate,
                                timeToGetResult, false, true)))

                .orElseGet(() -> ofNullable(nothingIsSelectedExceptionSupplier)
                        .map(nothingIsSelectedExceptionSupplier1 -> getFromIterable(description,
                                function, timeToGetResult,  nothingIsSelectedExceptionSupplier1)
                        ).orElseGet(() ->
                                getFromIterable(description, function, timeToGetResult)
                        ));
    }
}
