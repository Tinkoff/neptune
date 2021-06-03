package ru.tinkoff.qa.neptune.rabbit.mq.function.declare.queue;

import ru.tinkoff.qa.neptune.core.api.steps.annotations.StepParameter;
import ru.tinkoff.qa.neptune.core.api.steps.parameters.StepParameterPojo;
import ru.tinkoff.qa.neptune.rabbit.mq.AdditionalArguments;
import ru.tinkoff.qa.neptune.rabbit.mq.AdditionalArgumentsGetParameterValue;

public class ParametersForDeclareQueue implements StepParameterPojo {
    @StepParameter("durable")
    private boolean durable;
    @StepParameter("exclusive")
    private boolean exclusive;
    @StepParameter("autoDelete")
    private boolean autoDelete;
    @StepParameter(value = "additionalArguments", doNotReportNullValues = true, makeReadableBy = AdditionalArgumentsGetParameterValue.class)
    private AdditionalArguments additionalArguments;

    public boolean isDurable() {
        return durable;
    }

    public boolean isExclusive() {
        return exclusive;
    }

    public boolean isAutoDelete() {
        return autoDelete;
    }

    public AdditionalArguments getAdditionalArguments() {
        return additionalArguments;
    }

    public static ParametersForDeclareQueue queueParams(){
        return new ParametersForDeclareQueue();
    }

    public ParametersForDeclareQueue durable() {
        this.durable = true;
        return this;
    }

    public ParametersForDeclareQueue exclusive() {
        this.exclusive = true;
        return this;
    }

    public ParametersForDeclareQueue autoDelete() {
        this.autoDelete = true;
        return this;
    }

    public ParametersForDeclareQueue additionalArguments(AdditionalArguments additionalArguments) {
        this.additionalArguments = additionalArguments;
        return this;
    }
}
