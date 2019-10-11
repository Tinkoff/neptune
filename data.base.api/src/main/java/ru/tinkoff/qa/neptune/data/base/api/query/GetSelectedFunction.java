package ru.tinkoff.qa.neptune.data.base.api.query;

import org.datanucleus.api.jdo.JDOPersistenceManager;
import ru.tinkoff.qa.neptune.core.api.steps.SequentialGetStepSupplier;
import ru.tinkoff.qa.neptune.data.base.api.DBGetFunction;

import java.util.List;

/**
 * Creates function that performs selection from a data store and returns selected values
 */
@Deprecated
public class GetSelectedFunction<T> extends DBGetFunction<T, GetSelectedFunction<T>> {


    private  <S extends SequentialGetStepSupplier<JDOPersistenceManager, T, ?, ?, ?>> GetSelectedFunction(S innerSupplier) {
        super(innerSupplier);
    }

    /**
     * Creates function that performs selection from a data store and returns selected values as a list
     *
     * @param innerSupplier is a selection parameter
     * @param <T> is a type of an item from the resulted list
     * @return an instance of {@link GetSelectedFunction}
     */
    public static <T> GetSelectedFunction<List<T>> selected(SelectListGetSupplier<T, ?> innerSupplier) {
        return new GetSelectedFunction<>(innerSupplier);
    }

    /**
     * Creates function that performs selection from a data store and returns a single selected value
     *
     * @param innerSupplier is a selection parameter
     * @param <T> is a type of the resulted item
     * @return an instance of {@link GetSelectedFunction}
     */
    public static <T> GetSelectedFunction<T> selected(SelectOneGetSupplier<T, ?> innerSupplier) {
        return new GetSelectedFunction<>(innerSupplier);
    }
}
