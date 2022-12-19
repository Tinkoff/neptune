package ru.tinkoff.qa.neptune.kafka;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serializer;
import ru.tinkoff.qa.neptune.core.api.steps.context.Context;
import ru.tinkoff.qa.neptune.kafka.functions.poll.*;
import ru.tinkoff.qa.neptune.kafka.functions.send.KafkaSendRecordsActionSupplier;

import java.util.List;
import java.util.Properties;

import static com.google.common.base.Preconditions.checkNotNull;
import static java.util.Optional.ofNullable;
import static ru.tinkoff.qa.neptune.core.api.steps.context.ContextFactory.getCreatedContextOrCreate;
import static ru.tinkoff.qa.neptune.kafka.properties.DefaultKafkaProperties.KAFKA_CONSUMER_PROPERTIES;
import static ru.tinkoff.qa.neptune.kafka.properties.DefaultKafkaProperties.KAFKA_PRODUCER_PROPERTIES;

public class KafkaStepContext extends Context<KafkaStepContext> {

    public static KafkaStepContext kafka() {
        return getCreatedContextOrCreate(KafkaStepContext.class);
    }


    public <K, V> KafkaConsumer<K, V> createConsumer(Deserializer<K> keyDeserializer,
                                                     Deserializer<V> valueDeserializer) {
        checkNotNull(keyDeserializer);
        checkNotNull(valueDeserializer);
        return new KafkaConsumer<>(ofNullable(KAFKA_CONSUMER_PROPERTIES.get()).orElseGet(Properties::new),
            new InnerDeserializer<>(keyDeserializer),
            new InnerDeserializer<>(valueDeserializer));
    }

    public <K, V> KafkaProducer<K, V> createProducer(Serializer<K> keySerializer,
                                                     Serializer<V> valueSerializer) {
        return new KafkaProducer<>(ofNullable(KAFKA_PRODUCER_PROPERTIES.get()).orElseGet(Properties::new),
            keySerializer,
            valueSerializer);
    }

    /**
     * Sends something to a topic
     *
     * @param kafkaSendRecordsActionSupplier describes the object to be sent
     * @return self-reference
     */
    @SuppressWarnings("unused")
    public KafkaStepContext send(KafkaSendRecordsActionSupplier<?, ?, ?> kafkaSendRecordsActionSupplier) {
        return perform(kafkaSendRecordsActionSupplier);
    }

    /**
     * Polls topics and returns an array of values
     *
     * @param kafkaPollArraySupplier describes an array to get
     * @param <T>                    is a type of array item
     * @return resulted array
     */
    public <T> T[] poll(KafkaPollArraySupplier<?, ?, T, ?> kafkaPollArraySupplier) {
        return get(kafkaPollArraySupplier);
    }

    /**
     * Polls topics and returns some value
     *
     * @param kafkaPollIterableItemSupplier describes origin iterable value to get
     * @param <T>                           is a type of resulted value
     * @return resulted value
     */
    public <T> T poll(KafkaPollIterableItemSupplier<?, ?, T, ?> kafkaPollIterableItemSupplier) {
        return get(kafkaPollIterableItemSupplier);
    }

    /**
     * Polls topics and returns a list of values
     *
     * @param kafkaPollIterableSupplier describes iterable value to get
     * @param <T>                       is a type of list item
     * @return resulted list
     */
    public <T> List<T> poll(KafkaPollIterableSupplier<?, ?, T, ?> kafkaPollIterableSupplier) {
        return get(kafkaPollIterableSupplier);
    }

    /**
     * Polls topics and returns a list of ConsumerRecord
     *
     * @param recordSupplier describes iterable value to get
     * @return List<ConsumerRecord < String, String>>
     */
    public <K, V> List<ConsumerRecord<K, V>> poll(GetRecordSupplier<K, V> recordSupplier) {
        return get(recordSupplier);
    }

    public <T> List<T> poll(KafkaPollListFromRecordSupplier<?, ?, T, ?> recordSupplier) {
        return get(recordSupplier);
    }

    public <T> T poll(KafkaPollItemFromRecordSupplier<?, ?, T, ?> recordSupplier) {
        return get(recordSupplier);
    }
}