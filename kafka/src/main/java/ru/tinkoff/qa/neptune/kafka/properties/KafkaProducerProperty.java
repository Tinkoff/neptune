package ru.tinkoff.qa.neptune.kafka.properties;

import ru.tinkoff.qa.neptune.core.api.properties.PropertyDescription;
import ru.tinkoff.qa.neptune.core.api.properties.PropertyName;
import ru.tinkoff.qa.neptune.core.api.properties.object.ObjectPropertySupplier;

import java.util.Properties;
import java.util.function.Supplier;

@PropertyDescription(description = "Defines properties for KafkaProducer",
        section = "Kafka")
@PropertyName("KAFKA_PRODUCER_PROPERTIES")
public final class KafkaProducerProperty implements ObjectPropertySupplier<Properties, Supplier<Properties>> {
    public static final KafkaProducerProperty KAFKA_PRODUCER_PROPERTIES = new KafkaProducerProperty();

    private KafkaProducerProperty() {
        super();
    }
}
