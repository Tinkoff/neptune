package ru.tinkoff.qa.neptune.kafka.functions.poll;

import org.apache.kafka.clients.consumer.ConsumerRecord;

import java.util.function.Function;

final class KafkaSafeFunction<K, V, R> implements Function<ConsumerRecord<K, V>, R> {

    private final Function<ConsumerRecord<K, V>, R> f;

    KafkaSafeFunction(Function<ConsumerRecord<K, V>, R> f) {
        this.f = f;
    }

    @Override
    public R apply(ConsumerRecord<K, V> kvConsumerRecord) {
        try {
            return f.apply(kvConsumerRecord);
        } catch (Exception e) {
            return null;
        }
    }
}
