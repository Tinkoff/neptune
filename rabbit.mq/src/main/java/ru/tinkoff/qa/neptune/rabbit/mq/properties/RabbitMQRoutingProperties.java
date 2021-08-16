package ru.tinkoff.qa.neptune.rabbit.mq.properties;

import ru.tinkoff.qa.neptune.core.api.properties.PropertyDescription;
import ru.tinkoff.qa.neptune.core.api.properties.PropertyName;
import ru.tinkoff.qa.neptune.core.api.properties.string.StringValuePropertySupplier;

import static com.google.common.base.Preconditions.checkState;
import static java.lang.String.format;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

public enum RabbitMQRoutingProperties implements StringValuePropertySupplier {
    @PropertyDescription(description = "Defines default RabbitMQ exchange",
            section = "RabbitMQ. Routing")
    @PropertyName("RABBIT_MQ_DEFAULT_EXCHANGE_NAME")
    DEFAULT_EXCHANGE_NAME,

    @PropertyDescription(description = "Defines default RabbitMQ queue",
            section = "RabbitMQ. Routing")
    @PropertyName("RABBIT_MQ_DEFAULT_QUEUE_NAME")
    DEFAULT_QUEUE_NAME,

    @PropertyDescription(description = "Defines default RabbitMQ routing key",
            section = "RabbitMQ. Routing")
    @PropertyName("RABBIT_MQ_DEFAULT_ROUTING_KEY_NAME")
    DEFAULT_ROUTING_KEY_NAME;

    @Override
    public String get() {
        var result = StringValuePropertySupplier.super.get();
        checkState(isNotBlank(result), format("Property/environment variable '%s' is not defined", getName()));
        return result;
    }
}
