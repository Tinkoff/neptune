package ru.tinkoff.qa.neptune.rabbit.mq.function.delete.queue;


import ru.tinkoff.qa.neptune.core.api.steps.annotations.StepParameter;
import ru.tinkoff.qa.neptune.core.api.steps.parameters.StepParameterPojo;

public final class ParametersForDelete implements StepParameterPojo {
    @StepParameter("ifUnused")
    private boolean ifUnused;
    @StepParameter("ifEmpty")
    private boolean ifEmpty;

    public static ParametersForDelete deleteParams(){
        return new ParametersForDelete();
    }

    public ParametersForDelete unused() {
        this.ifUnused = true;
        return this;
    }

    public ParametersForDelete empty() {
        this.ifEmpty = true;
        return this;
    }

     boolean isIfUnused() {
        return ifUnused;
    }

     boolean isIfEmpty() {
        return ifEmpty;
    }
}
