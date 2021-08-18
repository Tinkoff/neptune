package ru.tinkoff.qa.neptune.rabbit.mq.function.declare;

import com.rabbitmq.client.Channel;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.StepParameter;
import ru.tinkoff.qa.neptune.core.api.steps.parameters.StepParameterPojo;
import ru.tinkoff.qa.neptune.rabbit.mq.AdditionalArguments;

import java.util.LinkedHashMap;
import java.util.Map;

import static java.lang.String.valueOf;
import static java.util.Optional.ofNullable;
import static ru.tinkoff.qa.neptune.core.api.localization.StepLocalization.translate;

@SuppressWarnings("unchecked")
public abstract class DeclareParameters<T extends DeclareParameters<T>> implements StepParameterPojo {

    @StepParameter(value = "durable", doNotReportNullValues = true)
    private boolean durable;

    AdditionalArguments arguments;
    @StepParameter("autoDelete")
    private boolean autoDelete;
    @StepParameter(value = "passive", doNotReportNullValues = true)
    private Boolean passive;

    /**
     * Defines a new argument and its value
     *
     * @param argumentName  is a name of an argument
     * @param argumentValue is a value of an argument
     * @return self-reference
     */
    public T argument(String argumentName, String argumentValue) {
        arguments = ofNullable(arguments).orElseGet(AdditionalArguments::arguments);
        arguments.setArgument(argumentName, argumentValue);
        return (T) this;
    }

    /**
     * Defines declaration of a durable queue/exchange
     *
     * @return self-reference
     */
    public T durable() {
        this.durable = true;
        return (T) this;
    }

    /**
     * Makes new queue/exchange autoDelete
     *
     * @return self-reference
     */
    public T autoDelete() {
        this.autoDelete = true;
        return (T) this;
    }

    /**
     * Forces a new exchange/queue to be declared passively.
     * It makes ignore other defined parameters on declaration.
     *
     * @return self-reference
     */
    public T passive() {
        this.passive = true;
        return (T) this;
    }

    abstract void declare(Channel channel);

    @Override
    public Map<String, String> getParameters() {
        var result = new LinkedHashMap<String, String>();
        if (passive != null) {
            try {
                result.put(translate(DeclareParameters.class.getDeclaredField("passive")), valueOf(true));
            } catch (NoSuchFieldException e) {
                throw new RuntimeException(e);
            }
        } else {
            result.putAll(StepParameterPojo.super.getParameters());
        }
        return result;
    }

    @Override
    public final String toString() {
        return translate(this);
    }

    boolean isAutoDelete() {
        return autoDelete;
    }

    boolean isDurable() {
        return durable;
    }

    boolean isPassive() {
        return passive != null;
    }
}
