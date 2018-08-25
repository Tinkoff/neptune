package ru.tinkoff.qa.neptune.data.base.api.query;

import ru.tinkoff.qa.neptune.data.base.api.DataBaseSteps;

import java.util.function.Function;
import java.util.function.Supplier;

/**
 * This interface marks classes that designed to perform selections from a data base.
 *
 * @param <T> of objects to be returned by selection.
 */
public interface SelectSupplier<T> extends Supplier<Function<DataBaseSteps, T>>  {
}
