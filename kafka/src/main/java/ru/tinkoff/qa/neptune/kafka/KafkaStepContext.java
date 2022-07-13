package ru.tinkoff.qa.neptune.kafka;

import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import ru.tinkoff.qa.neptune.core.api.steps.SequentialGetStepSupplier;
import ru.tinkoff.qa.neptune.core.api.steps.context.Context;
import ru.tinkoff.qa.neptune.kafka.functions.poll.KafkaPollArraySupplier;
import ru.tinkoff.qa.neptune.kafka.functions.poll.KafkaPollIterableItemSupplier;
import ru.tinkoff.qa.neptune.kafka.functions.poll.KafkaPollIterableSupplier;
import ru.tinkoff.qa.neptune.kafka.functions.send.KafkaSendRecordsActionSupplier;

import java.util.List;

import static ru.tinkoff.qa.neptune.core.api.steps.context.ContextFactory.getCreatedContextOrCreate;
import static ru.tinkoff.qa.neptune.kafka.properties.DefaultKafkaProperties.KAFKA_CONSUMER_PROPERTIES;
import static ru.tinkoff.qa.neptune.kafka.properties.DefaultKafkaProperties.KAFKA_PRODUCER_PROPERTIES;


@SuppressWarnings("unchecked")
public class KafkaStepContext extends Context<KafkaStepContext> {
    private KafkaProducer<String, String> producer;
    private KafkaConsumer<String, String> consumer;

    public static KafkaStepContext kafka() {
        return getCreatedContextOrCreate(KafkaStepContext.class);
    }

    KafkaProducer<String, String> getProducer() {
        return producer;
    }

    public KafkaConsumer<String, String> getConsumer() {
        return consumer;
    }

    KafkaConsumer<String, String> createConsumer() {
        return new KafkaConsumer<>(KAFKA_CONSUMER_PROPERTIES.get(), new StringDeserializer(), new StringDeserializer());
    }

    KafkaProducer<String, String> createProducer() {
        return new KafkaProducer<>(KAFKA_PRODUCER_PROPERTIES.get(), new StringSerializer(), new StringSerializer());
    }

    /**
     * Sends something to a topic
     *
     * @param kafkaSendRecordsActionSupplier describes the object to be sent
     * @return self-reference
     */
    @SuppressWarnings("unused")
    public KafkaStepContext send(KafkaSendRecordsActionSupplier<?, ?, ?> kafkaSendRecordsActionSupplier) {
        producer = createProducer();
        try (var producerVar = producer) {
            return perform(kafkaSendRecordsActionSupplier);
        } finally {
            producer = null;
        }
    }

    @SuppressWarnings("unused")
    private <R> R pollPrivate(SequentialGetStepSupplier<KafkaStepContext, R, ?, ?, ?> pollStep) {
        consumer = createConsumer();
        try (var consumerVar = consumer) {
            return get(pollStep);
        } finally {
            consumer = null;
        }
    }

    /**
     * Polls topics and returns an array of values
     *
     * @param kafkaPollArraySupplier describes an array to get
     * @param <T>                    is a type of array item
     * @return resulted array
     */
    public <T> T[] poll(KafkaPollArraySupplier<T, ?> kafkaPollArraySupplier) {
        return pollPrivate(kafkaPollArraySupplier);
    }

    /**
     * Polls topics and returns some value
     *
     * @param kafkaPollIterableItemSupplier describes origin iterable value to get
     * @param <T> is a type of resulted value
     * @return resulted value
     */
    public <T> T poll(KafkaPollIterableItemSupplier<T, ?> kafkaPollIterableItemSupplier) {
        return pollPrivate(kafkaPollIterableItemSupplier);
    }

    /**
     * Polls topics and returns a list of values
     *
     * @param kafkaPollIterableSupplier describes iterable value to get
     * @param <T> is a type of list item
     * @return resulted list
     */
    public <T> List<T> poll(KafkaPollIterableSupplier<T, ?> kafkaPollIterableSupplier) {
        return pollPrivate(kafkaPollIterableSupplier);
    }
}
