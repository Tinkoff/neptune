package ru.tinkoff.qa.neptune.rabbit.mq.function.declare.exchange;

import ru.tinkoff.qa.neptune.core.api.steps.annotations.StepParameter;
import ru.tinkoff.qa.neptune.core.api.steps.parameters.StepParameterPojo;
import ru.tinkoff.qa.neptune.rabbit.mq.AdditionalArguments;
import ru.tinkoff.qa.neptune.rabbit.mq.AdditionalArgumentsGetParameterValue;

public class ParametersForDeclareExchange implements StepParameterPojo {
    @StepParameter("durable")
    private boolean durable;
    @StepParameter("autoDelete")
    private boolean autoDelete;
    @StepParameter("internal")
    private boolean internal;
    @StepParameter(value = "additionalArguments", doNotReportNullValues = true, makeReadableBy = AdditionalArgumentsGetParameterValue.class)
    private AdditionalArguments additionalArguments;

    public boolean isDurable() {
        return durable;
    }

    public ParametersForDeclareExchange durable() {
        this.durable = true;
        return this;
    }

    public boolean isAutoDelete() {
        return autoDelete;
    }

    public ParametersForDeclareExchange autoDelete() {
        this.autoDelete = true;
        return this;
    }

    public boolean isInternal() {
        return internal;
    }

    public ParametersForDeclareExchange internal() {
        this.internal = true;
        return this;
    }

    public AdditionalArguments getAdditionalArguments() {
        return additionalArguments;
    }

    public ParametersForDeclareExchange additionalArguments(AdditionalArguments additionalArguments) {
        this.additionalArguments = additionalArguments;
        return this;
    }
}
