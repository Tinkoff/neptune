package ru.tinkoff.qa.neptune.core.api.localization.steps;

import ru.tinkoff.qa.neptune.core.api.steps.SequentialActionSupplier;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.StepParameter;

@SequentialActionSupplier.DefinePerformImperativeParameterName("Do:")
@SequentialActionSupplier.DefinePerformOnParameterName("On:")
public class SomeSequentialAction<T, R> extends SequentialActionSupplier<T, R, SomeSequentialAction<T, R>> {

    @StepParameter("A")
    Object a;

    @StepParameter("B")
    Object b;

    private AggregatedParams aggregatedParams;

    @Description("Some custom description")
    public static <T, R> SomeSequentialAction<T, R> actionStep() {
        return new SomeSequentialAction<>();
    }

    @Override
    protected void howToPerform(R value) {

    }
}
