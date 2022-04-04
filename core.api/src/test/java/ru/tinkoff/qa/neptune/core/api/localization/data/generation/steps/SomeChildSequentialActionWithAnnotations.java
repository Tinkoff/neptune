package ru.tinkoff.qa.neptune.core.api.localization.data.generation.steps;

import ru.tinkoff.qa.neptune.core.api.steps.SequentialActionSupplier;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.StepParameter;

@SequentialActionSupplier.DefinePerformImperativeParameterName("Child Do:")
@SequentialActionSupplier.DefinePerformOnParameterName("Child On:")
public class SomeChildSequentialActionWithAnnotations<T, R> extends SomeSequentialAction<T, R> {

    @StepParameter("C")
    Object c;

    @Description("Some another custom description")
    public static <T, R> SomeSequentialAction<T, R> actionStep() {
        return new SomeChildSequentialActionWithAnnotations<>();
    }
}
