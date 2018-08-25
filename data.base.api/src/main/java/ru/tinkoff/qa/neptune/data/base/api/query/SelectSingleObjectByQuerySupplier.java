package ru.tinkoff.qa.neptune.data.base.api.query;

import ru.tinkoff.qa.neptune.data.base.api.PersistableObject;

import javax.jdo.JDOQLTypedQuery;
import java.util.function.Function;

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
        return new SelectSingleObjectByQuerySupplier<T>(queryBuilder);
    }

    @Override
    protected Function<JDOQLTypedQuery<T>, T> getEndFunction() {
        return null;
    }
}
