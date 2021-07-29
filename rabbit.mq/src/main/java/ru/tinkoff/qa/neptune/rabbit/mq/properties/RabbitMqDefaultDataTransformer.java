package ru.tinkoff.qa.neptune.rabbit.mq.properties;

import ru.tinkoff.qa.neptune.core.api.data.format.DataTransformer;
import ru.tinkoff.qa.neptune.core.api.properties.PropertyDescription;
import ru.tinkoff.qa.neptune.core.api.properties.PropertyName;
import ru.tinkoff.qa.neptune.core.api.properties.object.ObjectByClassPropertySupplier;

@PropertyDescription(description = "Defines a serialization and deserialization mechanism",
        section = "RabbitMQ. Mappers")
@PropertyName("RABBIT_MQ_DEFAULT_DATA_TRANSFORMER")
public final class RabbitMqDefaultDataTransformer implements ObjectByClassPropertySupplier<DataTransformer> {

    public static final RabbitMqDefaultDataTransformer RABBIT_MQ_DEFAULT_DATA_TRANSFORMER = new RabbitMqDefaultDataTransformer();

    private RabbitMqDefaultDataTransformer() {
        super();
    }
}