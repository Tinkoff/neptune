package ru.tinkoff.qa.neptune.rabbit.mq.function.binding;

import com.rabbitmq.client.Channel;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.DescriptionFragment;
import ru.tinkoff.qa.neptune.core.api.steps.parameters.StepParameterPojo;
import ru.tinkoff.qa.neptune.rabbit.mq.AdditionalArguments;
import ru.tinkoff.qa.neptune.rabbit.mq.properties.RabbitMQRoutingProperties;

import static com.google.common.base.Preconditions.checkArgument;
import static java.util.Optional.ofNullable;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static ru.tinkoff.qa.neptune.core.api.localization.StepLocalization.translate;
import static ru.tinkoff.qa.neptune.rabbit.mq.properties.RabbitMQRoutingProperties.DEFAULT_ROUTING_KEY_NAME;

/**
 * Defines parameters of the binding.
 *
 * @param <T> is a type of subclass
 */
@SuppressWarnings("unchecked")
public abstract class BindUnbindParameters<T extends BindUnbindParameters<T>> implements StepParameterPojo {

    static final String ROUTING_KEY = "routingKey";

    @DescriptionFragment(ROUTING_KEY)
    String routingKey = "";

    AdditionalArguments arguments;

    /**
     * Defines a new value of a routing key
     *
     * @param key a new value of a routing key
     * @return self-reference
     */
    public T withRoutingKey(String key) {
        checkArgument(isNotBlank(key), "A passed value of routing key should not be blank");
        this.routingKey = key;
        return (T) this;
    }

    /**
     * Defines new value of a routing key using the {@link RabbitMQRoutingProperties#DEFAULT_ROUTING_KEY_NAME}
     * property.
     *
     * @return self-reference
     */
    public T withDefaultRoutingKey() {
        return withRoutingKey(DEFAULT_ROUTING_KEY_NAME.get());
    }

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

    abstract void bind(Channel value);

    abstract void unbind(Channel value);

    @Override
    public final String toString() {
        return translate(this);
    }
}
