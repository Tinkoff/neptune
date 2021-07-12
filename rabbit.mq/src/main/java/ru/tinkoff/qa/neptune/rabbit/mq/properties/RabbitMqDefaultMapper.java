package ru.tinkoff.qa.neptune.rabbit.mq.properties;

import com.fasterxml.jackson.databind.ObjectMapper;
import ru.tinkoff.qa.neptune.core.api.properties.PropertyDescription;
import ru.tinkoff.qa.neptune.core.api.properties.PropertyName;
import ru.tinkoff.qa.neptune.core.api.properties.object.ObjectPropertySupplier;

import java.util.function.Supplier;

@PropertyDescription(description = {"Defines full name of a class which implements Supplier<ObjectMapper> and whose objects",
        "supply mappers for serialization/deserialization of objects."},
        section = "RabbitMQ. Mappers")
@PropertyName("RABBIT_MQ_DEFAULT_READ_MAPPER")
public final class RabbitMqDefaultMapper implements ObjectPropertySupplier<ObjectMapper, Supplier<ObjectMapper>> {

    public static final RabbitMqDefaultMapper RABBIT_MQ_DEFAULT_MAPPER = new RabbitMqDefaultMapper();

    private RabbitMqDefaultMapper() {
        super();
    }
}