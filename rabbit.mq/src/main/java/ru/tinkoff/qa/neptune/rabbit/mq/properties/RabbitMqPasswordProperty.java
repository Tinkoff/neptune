package ru.tinkoff.qa.neptune.rabbit.mq.properties;

import ru.tinkoff.qa.neptune.core.api.properties.PropertyDescription;
import ru.tinkoff.qa.neptune.core.api.properties.PropertyName;
import ru.tinkoff.qa.neptune.core.api.properties.string.StringValuePropertySupplier;

@PropertyDescription(description = "Defines password for the connection",
        section = "RabbitMQ")
@PropertyName("RABBIT_MQ_PASSWORD")
public class RabbitMqPasswordProperty implements StringValuePropertySupplier {
    public static final RabbitMqPasswordProperty RABBIT_MQ_PASSWORD_PROPERTY = new RabbitMqPasswordProperty();

}
