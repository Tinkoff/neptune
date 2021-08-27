package ru.tinkoff.qa.neptune.kafka.properties;

import ru.tinkoff.qa.neptune.core.api.properties.PropertyDescription;
import ru.tinkoff.qa.neptune.core.api.properties.PropertyName;
import ru.tinkoff.qa.neptune.core.api.properties.object.ObjectPropertySupplier;

import java.util.Properties;
import java.util.function.Supplier;

public enum DefaultKafkaProperties implements ObjectPropertySupplier<Properties, Supplier<Properties>> {

    @PropertyDescription(description = "Defines properties for KafkaProducer",
            section = "Kafka")
    @PropertyName("KAFKA_PRODUCER_PROPERTIES")
    KAFKA_PRODUCER_PROPERTIES,

    @PropertyDescription(description = "Defines properties for KafkaConsumer",
            section = "Kafka")
    @PropertyName("KAFKA_CONSUMER_PROPERTIES")
    KAFKA_CONSUMER_PROPERTIES;

}
