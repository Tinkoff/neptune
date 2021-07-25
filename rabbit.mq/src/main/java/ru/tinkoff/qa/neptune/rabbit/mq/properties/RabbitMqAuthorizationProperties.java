package ru.tinkoff.qa.neptune.rabbit.mq.properties;

import ru.tinkoff.qa.neptune.core.api.properties.PropertyDescription;
import ru.tinkoff.qa.neptune.core.api.properties.PropertyName;
import ru.tinkoff.qa.neptune.core.api.properties.string.StringValuePropertySupplier;

public enum RabbitMqAuthorizationProperties  implements StringValuePropertySupplier {
    @PropertyDescription(description = "Defines password for the connection",
            section = "RabbitMQ")
    @PropertyName("RABBIT_MQ_PASSWORD")
    RABBIT_MQ_PASSWORD,

    @PropertyDescription(description = "Defines username for the connection",
            section = "RabbitMQ")
    @PropertyName("RABBIT_MQ_USERNAME")
    RABBIT_MQ_USERNAME
}
