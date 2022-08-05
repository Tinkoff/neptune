package ru.tinkoff.qa.neptune.core.api.localization.data.generation.steps;

import ru.tinkoff.qa.neptune.core.api.steps.SequentialGetStepSupplier;

import java.util.function.Function;

public class SomeSequentialStepSupplierWithoutAnnotations<T, R, M> extends SequentialGetStepSupplier.GetArrayChainedStepSupplier<T, R, M, SomeSequentialStepSupplierWithoutAnnotations<T, R, M>> {

    Object a;

    Object b;

    private AggregatedParams aggregatedParams;

    protected SomeSequentialStepSupplierWithoutAnnotations(Function<M, R[]> originalFunction) {
        super(originalFunction);
    }

    public static <T, R, M> SomeSequentialStepSupplierWithoutAnnotations<T, R, M> getStep(Function<M, R[]> originalFunction) {
        return new SomeSequentialStepSupplierWithoutAnnotations<>(originalFunction);
    }
}
