package ru.tinkoff.qa.neptune.kafka.properties;

import ru.tinkoff.qa.neptune.core.api.data.format.DataTransformer;
import ru.tinkoff.qa.neptune.core.api.properties.PropertyDescription;
import ru.tinkoff.qa.neptune.core.api.properties.PropertyName;
import ru.tinkoff.qa.neptune.core.api.properties.object.ObjectByClassPropertySupplier;

@PropertyDescription(description = "Defines a serialization and deserialization mechanism",
        section = "Kafka. Mappers")
@PropertyName("KAFKA_DEFAULT_DATA_TRANSFORMER")
public final class KafkaDefaultDataTransformer implements ObjectByClassPropertySupplier<DataTransformer> {
    public static final KafkaDefaultDataTransformer KAFKA_DEFAULT_DATA_TRANSFORMER = new KafkaDefaultDataTransformer();

    private KafkaDefaultDataTransformer() {
        super();
    }
}
