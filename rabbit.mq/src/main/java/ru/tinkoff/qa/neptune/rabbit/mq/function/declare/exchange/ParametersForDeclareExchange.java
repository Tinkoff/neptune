package ru.tinkoff.qa.neptune.rabbit.mq.function.declare.exchange;

import ru.tinkoff.qa.neptune.core.api.steps.annotations.StepParameter;
import ru.tinkoff.qa.neptune.core.api.steps.parameters.StepParameterPojo;
import ru.tinkoff.qa.neptune.rabbit.mq.AdditionalArguments;

import java.util.HashMap;

import static java.util.Optional.ofNullable;

public class ParametersForDeclareExchange implements StepParameterPojo {
    @StepParameter("durable")
    private boolean durable;
    @StepParameter("autoDelete")
    private boolean autoDelete;
    @StepParameter("internal")
    private boolean internal;
    private AdditionalArguments additionalArguments;

    public static ParametersForDeclareExchange exchangeParams() {
        return new ParametersForDeclareExchange();
    }

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

    public HashMap<String, Object> getAdditionalArguments() {
        return additionalArguments == null ? null : additionalArguments.getHashMap();
    }

    public ParametersForDeclareExchange argument(String name, Object value) {
        additionalArguments = ofNullable(additionalArguments).orElseGet(AdditionalArguments::arguments);
        additionalArguments.setArgument(name, value);
        return this;
    }
}
