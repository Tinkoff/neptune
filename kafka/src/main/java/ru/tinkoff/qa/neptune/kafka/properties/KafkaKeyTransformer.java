package ru.tinkoff.qa.neptune.kafka.properties;

import ru.tinkoff.qa.neptune.core.api.data.format.DataTransformer;
import ru.tinkoff.qa.neptune.core.api.properties.PropertyDescription;
import ru.tinkoff.qa.neptune.core.api.properties.PropertyName;
import ru.tinkoff.qa.neptune.core.api.properties.object.ObjectByClassPropertySupplier;

@PropertyDescription(description = "Defines a serialization mechanism for key",
        section = "Kafka. Mappers")
@PropertyName("KAFKA_KEY_TRANSFORMER")
public final class KafkaKeyTransformer implements ObjectByClassPropertySupplier<DataTransformer> {
    public static final KafkaKeyTransformer KAFKA_KEY_TRANSFORMER = new KafkaKeyTransformer();

    private KafkaKeyTransformer() {
        super();
    }
}
