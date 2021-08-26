package ru.tinkoff.qa.neptune.kafka;

import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import ru.tinkoff.qa.neptune.core.api.steps.context.Context;
import ru.tinkoff.qa.neptune.core.api.steps.context.CreateWith;
import ru.tinkoff.qa.neptune.kafka.functions.poll.KafkaPollArraySupplier;
import ru.tinkoff.qa.neptune.kafka.functions.poll.KafkaPollIterableItemSupplier;
import ru.tinkoff.qa.neptune.kafka.functions.poll.KafkaPollIterableSupplier;
import ru.tinkoff.qa.neptune.kafka.functions.send.KafkaSendRecordsActionSupplier;

import java.util.List;


@CreateWith(provider = KafkaParameterProvider.class)
@SuppressWarnings("unchecked")
public class KafkaStepContext extends Context<KafkaStepContext> {
    private static final KafkaStepContext context = getInstance(KafkaStepContext.class);
    private final KafkaProducer<String, String> producer;
    private final KafkaConsumer<String, String> consumer;

    public KafkaStepContext(KafkaProducer<String, String> producer, KafkaConsumer<String, String> consumer) {
        this.producer = producer;
        this.consumer = consumer;
    }

    public static KafkaStepContext kafka() {
        return context;
    }

    public KafkaProducer<String, String> getProducer() {
        return producer;
    }

    public <K, V> KafkaConsumer<K, V> getConsumer() {
        return (KafkaConsumer<K, V>) consumer;
    }

    /**
     * Sends something to a topic
     *
     * @param kafkaSendRecordsActionSupplier describes the object to be sent
     * @return self-reference
     */
    public KafkaStepContext send(KafkaSendRecordsActionSupplier<?, ?, ?> kafkaSendRecordsActionSupplier) {
        return perform(kafkaSendRecordsActionSupplier);
    }

    /**
     * Polls topics and returns an array of values
     * @param kafkaPollArraySupplier describes an array to get
     * @param <T> is a type of array item
     * @return resulted array
     */
    public <T> T[] poll(KafkaPollArraySupplier<T, ?> kafkaPollArraySupplier) {
        return get(kafkaPollArraySupplier);
    }

    /**
     * Polls topics and returns some value
     *
     * @param kafkaPollIterableItemSupplier describes origin iterable value to get
     * @param <T> is a type of resulted value
     * @return resulted value
     */
    public <T> T poll(KafkaPollIterableItemSupplier<T, ?> kafkaPollIterableItemSupplier) {
        return get(kafkaPollIterableItemSupplier);
    }

    /**
     * Polls topics and returns a list of values
     *
     * @param kafkaPollIterableSupplier describes iterable value to get
     * @param <T> is a type of list item
     * @return resulted list
     */
    public <T> List<T> poll(KafkaPollIterableSupplier<T, ?> kafkaPollIterableSupplier) {
        return get(kafkaPollIterableSupplier);
    }
}
