package ru.tinkoff.qa.neptune.kafka;

import ru.tinkoff.qa.neptune.core.api.steps.context.ParameterProvider;

import static ru.tinkoff.qa.neptune.kafka.properties.KafkaConsumerProperty.KAFKA_CONSUMER;
import static ru.tinkoff.qa.neptune.kafka.properties.KafkaProducerProperty.KAFKA_PRODUCER;

public class KafkaParameterProvider implements ParameterProvider {
    @Override
    public Object[] provide() {
        return parameters();
    }

    public static Object[] parameters() {
        return new Object[]{KAFKA_PRODUCER.get(), KAFKA_CONSUMER.get()};
    }
}
