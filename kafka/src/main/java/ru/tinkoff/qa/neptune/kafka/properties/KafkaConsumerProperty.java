package ru.tinkoff.qa.neptune.kafka.properties;

import org.apache.kafka.clients.consumer.KafkaConsumer;
import ru.tinkoff.qa.neptune.core.api.properties.PropertyDescription;
import ru.tinkoff.qa.neptune.core.api.properties.PropertyName;
import ru.tinkoff.qa.neptune.core.api.properties.object.ObjectPropertySupplier;

import java.util.function.Supplier;

@PropertyDescription(description = "Defines KafkaConsumer",
        section = "Kafka")
@PropertyName("KAFKA_CONSUMER")
public final class KafkaConsumerProperty implements ObjectPropertySupplier<KafkaConsumer<String, String>, Supplier<KafkaConsumer<String, String>>> {
    public static final KafkaConsumerProperty KAFKA_CONSUMER = new KafkaConsumerProperty();

    private KafkaConsumerProperty() {
        super();
    }
}
