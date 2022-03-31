package ru.tinkoff.qa.neptune.core.api.localization.steps;

import ru.tinkoff.qa.neptune.core.api.steps.SequentialGetStepSupplier;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.StepParameter;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.ThrowWhenNoData;

import java.util.function.Function;

@Description("Default description")
@SequentialGetStepSupplier.DefineGetImperativeParameterName("Fetch:")
@SequentialGetStepSupplier.DefineFromParameterName("Use")
@SequentialGetStepSupplier.DefineTimeOutParameterName("Wait:")
@SequentialGetStepSupplier.DefinePollingTimeParameterName("Sleep:")
@SequentialGetStepSupplier.DefineCriteriaParameterName("Condition:")
@SequentialGetStepSupplier.DefineResultDescriptionParameterName("Returned:")
@ThrowWhenNoData(startDescription = "No data fetched")
public class SomeSequentialStepSupplier<T, R, M> extends SequentialGetStepSupplier.GetArrayChainedStepSupplier<T, R, M, SomeSequentialStepSupplier<T, R, M>> {

    @StepParameter("A")
    Object a;

    @StepParameter("B")
    Object b;

    private AggregatedParams aggregatedParams;

    protected SomeSequentialStepSupplier(Function<M, R[]> originalFunction) {
        super(originalFunction);
    }

    @Description("Some custom step name")
    public static <T, R, M> SomeSequentialStepSupplier<T, R, M> getStep(Function<M, R[]> originalFunction) {
        return new SomeSequentialStepSupplier<>(originalFunction);
    }

}
