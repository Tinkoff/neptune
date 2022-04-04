package ru.tinkoff.qa.neptune.core.api.localization.data.generation.steps;

import java.util.function.Function;

public class SomeChildSequentialStepSupplierWithoutAnnotations<T, R, M> extends SomeSequentialStepSupplier<T, R, M> {

    protected SomeChildSequentialStepSupplierWithoutAnnotations(Function<M, R[]> originalFunction) {
        super(originalFunction);
    }
}
