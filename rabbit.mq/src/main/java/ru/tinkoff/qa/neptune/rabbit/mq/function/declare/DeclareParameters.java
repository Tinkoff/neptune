package ru.tinkoff.qa.neptune.rabbit.mq.function.declare;

import com.rabbitmq.client.Channel;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.StepParameter;
import ru.tinkoff.qa.neptune.core.api.steps.parameters.StepParameterPojo;
import ru.tinkoff.qa.neptune.rabbit.mq.AdditionalArguments;

import static java.util.Optional.ofNullable;
import static ru.tinkoff.qa.neptune.core.api.localization.StepLocalization.translate;

@SuppressWarnings("unchecked")
public abstract class DeclareParameters<T extends DeclareParameters<T>> implements StepParameterPojo {

    AdditionalArguments arguments;
    @StepParameter(value = "durable", doNotReportNullValues = true)
    private boolean durable;

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

    boolean isDurable() {
        return durable;
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

    public abstract void declare(Channel channel);

    @Override
    public final String toString() {
        return translate(this);
    }
}
