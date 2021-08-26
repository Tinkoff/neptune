package ru.tinkoff.qa.neptune.kafka.properties;

import org.apache.kafka.clients.producer.KafkaProducer;
import ru.tinkoff.qa.neptune.core.api.properties.PropertyDescription;
import ru.tinkoff.qa.neptune.core.api.properties.PropertyName;
import ru.tinkoff.qa.neptune.core.api.properties.object.ObjectPropertySupplier;

import java.util.function.Supplier;

@PropertyDescription(description = "Defines KafkaProducer",
        section = "Kafka")
@PropertyName("KAFKA_PRODUCER")
public final class KafkaProducerProperty implements ObjectPropertySupplier<KafkaProducer<String, String>, Supplier<KafkaProducer<String, String>>> {
    public static final KafkaProducerProperty KAFKA_PRODUCER = new KafkaProducerProperty();

    private KafkaProducerProperty() {
        super();
    }
}
