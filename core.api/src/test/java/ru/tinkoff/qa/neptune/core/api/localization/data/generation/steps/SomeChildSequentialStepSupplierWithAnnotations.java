package ru.tinkoff.qa.neptune.core.api.localization.data.generation.steps;

import ru.tinkoff.qa.neptune.core.api.steps.SequentialGetStepSupplier;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.StepParameter;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.ThrowWhenNoData;

import java.util.function.Function;

@Description("Default child description")
@SequentialGetStepSupplier.DefineGetImperativeParameterName("child Fetch:")
@SequentialGetStepSupplier.DefineFromParameterName("child Use")
@SequentialGetStepSupplier.DefineTimeOutParameterName("child Wait:")
@SequentialGetStepSupplier.DefinePollingTimeParameterName("child Sleep:")
@SequentialGetStepSupplier.DefineCriteriaParameterName("child Condition:")
@SequentialGetStepSupplier.DefineResultDescriptionParameterName("child Returned:")
@ThrowWhenNoData(startDescription = "No data fetched")
public class SomeChildSequentialStepSupplierWithAnnotations<T, R, M> extends SomeSequentialStepSupplier<T, R, M> {

    @StepParameter("C")
    Object c;

    protected SomeChildSequentialStepSupplierWithAnnotations(Function<M, R[]> originalFunction) {
        super(originalFunction);
    }

    @Description("Some another custom step name")
    public static <T, R, M> SomeSequentialStepSupplier<T, R, M> getAnotherStep(Function<M, R[]> originalFunction) {
        return new SomeChildSequentialStepSupplierWithAnnotations<>(originalFunction);
    }
}
