package ru.tinkoff.qa.neptune.kafka.properties;

import ru.tinkoff.qa.neptune.core.api.properties.PropertyDescription;
import ru.tinkoff.qa.neptune.core.api.properties.PropertyName;
import ru.tinkoff.qa.neptune.core.api.properties.object.ObjectPropertySupplier;

import java.util.Properties;
import java.util.function.Supplier;

@PropertyDescription(description = "Defines properties for KafkaConsumer",
        section = "Kafka")
@PropertyName("KAFKA_CONSUMER_PROPERTIES")
public final class KafkaConsumerProperty implements ObjectPropertySupplier<Properties, Supplier<Properties>> {
    public static final KafkaConsumerProperty KAFKA_CONSUMER_PROPERTY = new KafkaConsumerProperty();

    private KafkaConsumerProperty() {
        super();
    }
}
