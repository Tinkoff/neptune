package ru.tinkoff.qa.neptune.core.api.localization.data.generation.steps;

import ru.tinkoff.qa.neptune.core.api.steps.SequentialActionSupplier;

public class SomeSequentialActionWithoutAnnotations<T, R> extends SequentialActionSupplier<T, R, SomeSequentialActionWithoutAnnotations<T, R>> {

    Object a;

    Object b;

    private AggregatedParams aggregatedParams;

    public static <T, R> SomeSequentialActionWithoutAnnotations<T, R> actionStep() {
        return new SomeSequentialActionWithoutAnnotations<>();
    }

    @Override
    protected void howToPerform(R value) {

    }
}
