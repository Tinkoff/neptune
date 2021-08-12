package ru.tinkoff.qa.neptune.kafka.properties;

import org.apache.kafka.clients.consumer.KafkaConsumer;
import ru.tinkoff.qa.neptune.core.api.properties.PropertyDescription;
import ru.tinkoff.qa.neptune.core.api.properties.PropertyName;
import ru.tinkoff.qa.neptune.core.api.properties.object.ObjectPropertySupplier;

import java.util.function.Supplier;

@PropertyDescription(description = "Defines KafkaConsumer",
        section = "Kafka")
@PropertyName("KAFKA_CONSUMER")
public final class KafkaConsumerSupplier implements ObjectPropertySupplier<KafkaConsumer<Object, Object>, Supplier<KafkaConsumer<Object, Object>>> {
    public static final KafkaConsumerSupplier KAFKA_CONSUMER = new KafkaConsumerSupplier();

    private KafkaConsumerSupplier() {
        super();
    }


}
