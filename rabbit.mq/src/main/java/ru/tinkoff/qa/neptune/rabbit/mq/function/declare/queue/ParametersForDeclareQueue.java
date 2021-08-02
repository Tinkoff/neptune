package ru.tinkoff.qa.neptune.rabbit.mq.function.declare.queue;

import ru.tinkoff.qa.neptune.core.api.steps.annotations.StepParameter;
import ru.tinkoff.qa.neptune.core.api.steps.parameters.StepParameterPojo;
import ru.tinkoff.qa.neptune.rabbit.mq.AdditionalArguments;

import java.util.HashMap;

import static java.util.Optional.ofNullable;

public class ParametersForDeclareQueue implements StepParameterPojo {
    @StepParameter("durable")
    private boolean durable;
    @StepParameter("exclusive")
    private boolean exclusive;
    @StepParameter("autoDelete")
    private boolean autoDelete;
    private AdditionalArguments additionalArguments;

    boolean isDurable() {
        return durable;
    }

    boolean isExclusive() {
        return exclusive;
    }

    boolean isAutoDelete() {
        return autoDelete;
    }

    public HashMap<String, Object> getAdditionalArguments() {
        return additionalArguments == null ? null : additionalArguments.getHashMap();
    }

    public static ParametersForDeclareQueue queueParams() {
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

    public ParametersForDeclareQueue argument(String name, Object value) {
        additionalArguments = ofNullable(additionalArguments).orElseGet(AdditionalArguments::arguments);
        additionalArguments.setArgument(name, value);
        return this;
    }
}
