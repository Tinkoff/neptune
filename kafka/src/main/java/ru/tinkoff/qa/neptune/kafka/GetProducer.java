package ru.tinkoff.qa.neptune.kafka;

import org.apache.kafka.clients.producer.KafkaProducer;

import java.util.function.Function;

public final class GetProducer implements Function<KafkaStepContext, KafkaProducer<String, String>> {

    private GetProducer() {
        super();
    }

    public static GetProducer getProducer() {
        return new GetProducer();
    }

    @Override
    public KafkaProducer<String, String> apply(KafkaStepContext kafkaStepContext) {
        return kafkaStepContext.getProducer();
    }
}
