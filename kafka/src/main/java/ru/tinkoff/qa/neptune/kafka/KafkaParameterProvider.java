package ru.tinkoff.qa.neptune.kafka;

import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import ru.tinkoff.qa.neptune.core.api.steps.context.ParameterProvider;

import static ru.tinkoff.qa.neptune.kafka.properties.DefaultKafkaProperties.KAFKA_CONSUMER_PROPERTIES;
import static ru.tinkoff.qa.neptune.kafka.properties.DefaultKafkaProperties.KAFKA_PRODUCER_PROPERTIES;


public class KafkaParameterProvider implements ParameterProvider {
    @Override
    public Object[] provide() {
        return parameters();
    }

    public static Object[] parameters() {
        return new Object[]{
                new KafkaProducer<>(KAFKA_PRODUCER_PROPERTIES.get(), new StringSerializer(), new StringSerializer()),
                new KafkaConsumer<>(KAFKA_CONSUMER_PROPERTIES.get(), new StringDeserializer(), new StringDeserializer())
        };
    }
}
