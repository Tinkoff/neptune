package ru.tinkoff.qa.neptune.rabbit.mq.properties;

import ru.tinkoff.qa.neptune.core.api.properties.PropertyDescription;
import ru.tinkoff.qa.neptune.core.api.properties.PropertyName;
import ru.tinkoff.qa.neptune.core.api.properties.object.ObjectByClassPropertySupplier;

@PropertyDescription(description = "Defines a serialization and deserialization mechanism.",
        section = "RabbitMQ. Mappers")
@PropertyName("RABBIT_MQ_DEFAULT_MAPPER")
public final class RabbitMqDefaultMapper implements ObjectByClassPropertySupplier<RabbitMqMapper> {

    public static final RabbitMqDefaultMapper RABBIT_MQ_DEFAULT_MAPPER = new RabbitMqDefaultMapper();

    private RabbitMqDefaultMapper() {
        super();
    }
}