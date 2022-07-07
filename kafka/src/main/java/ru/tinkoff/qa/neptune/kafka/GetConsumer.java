package ru.tinkoff.qa.neptune.kafka;

import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.util.function.Function;

public final class GetConsumer implements Function<KafkaStepContext, KafkaConsumer<String, String>> {

    private GetConsumer() {
        super();
    }

    public static GetConsumer getConsumer() {
        return new GetConsumer();
    }

    @Override
    public KafkaConsumer<String, String> apply(KafkaStepContext kafkaStepContext) {
        return kafkaStepContext.getConsumer();
    }
}
