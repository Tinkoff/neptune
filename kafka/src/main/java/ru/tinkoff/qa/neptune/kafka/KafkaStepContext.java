package ru.tinkoff.qa.neptune.kafka;

import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import ru.tinkoff.qa.neptune.core.api.steps.context.Context;
import ru.tinkoff.qa.neptune.core.api.steps.context.CreateWith;
import ru.tinkoff.qa.neptune.kafka.functions.poll.KafkaPollArrayItemSupplier;
import ru.tinkoff.qa.neptune.kafka.functions.poll.KafkaPollArraySupplier;
import ru.tinkoff.qa.neptune.kafka.functions.poll.KafkaPollIterableItemSupplier;
import ru.tinkoff.qa.neptune.kafka.functions.poll.KafkaPollIterableSupplier;
import ru.tinkoff.qa.neptune.kafka.functions.send.KafkaSendRecordsActionSupplier;

import java.util.List;


@CreateWith(provider = KafkaParameterProvider.class)
@SuppressWarnings("unchecked")
public class KafkaStepContext extends Context<KafkaStepContext> {
    private static final KafkaStepContext context = getInstance(KafkaStepContext.class);
    private final KafkaProducer<Object, Object> producer;
    private final KafkaConsumer<Object, Object> consumer;

    public KafkaStepContext(KafkaProducer<Object, Object> producer, KafkaConsumer<Object, Object> consumer) {
        this.producer = producer;
        this.consumer = consumer;
    }

    public static KafkaStepContext kafka() {
        return context;
    }

    public <K, V> KafkaProducer<K, V> getProducer() {
        return (KafkaProducer<K, V>) producer;
    }

    public <K, V> KafkaConsumer<K, V> getConsumer() {
        return (KafkaConsumer<K, V>) consumer;
    }

    public KafkaStepContext send(KafkaSendRecordsActionSupplier<?, ?, ?> kafkaSendRecordsActionSupplier) {
        return perform(kafkaSendRecordsActionSupplier);
    }

    public <T> T poll(KafkaPollArrayItemSupplier<T> kafkaPollArrayItemSupplier) {
        return get(kafkaPollArrayItemSupplier);
    }

    public <T> T[] poll(KafkaPollArraySupplier<T> kafkaPollArraySupplier) {
        return get(kafkaPollArraySupplier);
    }

    public <T> T poll(KafkaPollIterableItemSupplier<T> kafkaPollIterableItemSupplier) {
        return get(kafkaPollIterableItemSupplier);
    }

    public <T> List<T> poll(KafkaPollIterableSupplier<T> kafkaPollIterableSupplier) {
        return get(kafkaPollIterableSupplier);
    }
}
