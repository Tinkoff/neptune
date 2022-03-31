package ru.tinkoff.qa.neptune.core.api.localization.steps;

import ru.tinkoff.qa.neptune.core.api.steps.annotations.StepParameter;
import ru.tinkoff.qa.neptune.core.api.steps.parameters.StepParameterPojo;

public class AggregatedParams implements StepParameterPojo {

    @StepParameter("A")
    Object a;

    @StepParameter("B")
    Object b;
}
