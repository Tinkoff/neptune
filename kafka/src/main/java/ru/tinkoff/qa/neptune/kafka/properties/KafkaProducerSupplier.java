package ru.tinkoff.qa.neptune.kafka.properties;

import org.apache.kafka.clients.producer.KafkaProducer;
import ru.tinkoff.qa.neptune.core.api.properties.PropertyDescription;
import ru.tinkoff.qa.neptune.core.api.properties.PropertyName;
import ru.tinkoff.qa.neptune.core.api.properties.object.ObjectPropertySupplier;

import java.util.function.Supplier;

@PropertyDescription(description = "Defines KafkaProducer",
        section = "Kafka")
@PropertyName("KAFKA_PRODUCER")
public final class KafkaProducerSupplier implements ObjectPropertySupplier<KafkaProducer<?, ?>, Supplier<KafkaProducer<?, ?>>> {
    public static final KafkaProducerSupplier KAFKA_PRODUCER = new KafkaProducerSupplier();

    private KafkaProducerSupplier() {
        super();
    }
}
