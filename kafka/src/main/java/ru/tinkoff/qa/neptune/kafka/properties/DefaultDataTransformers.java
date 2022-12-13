package ru.tinkoff.qa.neptune.kafka.properties;

import ru.tinkoff.qa.neptune.core.api.data.format.DataTransformer;
import ru.tinkoff.qa.neptune.core.api.properties.PropertyDescription;
import ru.tinkoff.qa.neptune.core.api.properties.PropertyName;
import ru.tinkoff.qa.neptune.core.api.properties.object.ObjectByClassPropertySupplier;

@Deprecated(forRemoval = true)
public enum DefaultDataTransformers implements ObjectByClassPropertySupplier<DataTransformer> {

    @PropertyDescription(description = "Defines a default serialization and deserialization of values of messages",
        section = "Kafka. Mappers")
    @PropertyName("KAFKA_DEFAULT_DATA_TRANSFORMER")
    KAFKA_DEFAULT_DATA_TRANSFORMER,

    @PropertyDescription(description = "Defines a serialization mechanism for key",
        section = "Kafka. Mappers")
    @PropertyName("KAFKA_KEY_TRANSFORMER")
    KAFKA_KEY_TRANSFORMER;

}
