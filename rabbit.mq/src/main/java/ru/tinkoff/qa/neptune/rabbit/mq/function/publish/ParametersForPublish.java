package ru.tinkoff.qa.neptune.rabbit.mq.function.publish;

import ru.tinkoff.qa.neptune.core.api.steps.annotations.StepParameter;
import ru.tinkoff.qa.neptune.core.api.steps.parameters.StepParameterPojo;

public class ParametersForPublish implements StepParameterPojo {
    @StepParameter("mandatory")
    private boolean mandatory;
    @StepParameter("immediate")
    private boolean immediate;

    public static ParametersForPublish parameters() {
        return new ParametersForPublish();
    }

    boolean isMandatory() {
        return mandatory;
    }

    public ParametersForPublish mandatory() {
        this.mandatory = true;
        return this;
    }

    boolean isImmediate() {
        return immediate;
    }

    public ParametersForPublish immediate() {
        this.immediate = true;
        return this;
    }
}
