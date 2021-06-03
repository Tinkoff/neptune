package ru.tinkoff.qa.neptune.rabbit.mq.properties;

import ru.tinkoff.qa.neptune.core.api.properties.PropertyDescription;
import ru.tinkoff.qa.neptune.core.api.properties.PropertyName;
import ru.tinkoff.qa.neptune.core.api.properties.string.StringValuePropertySupplier;

@PropertyDescription(description = "Defines username for the connection",
        section = "RabbitMQ")
@PropertyName("RABBIT_MQ_USERNAME")
public class RabbitMqUserNameProperty implements StringValuePropertySupplier {
    public static final RabbitMqUserNameProperty RABBIT_MQ_USERNAME_PROPERTY = new RabbitMqUserNameProperty();

}
