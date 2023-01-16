package ru.tinkoff.qa.neptune.kafka.functions.send;

import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.Header;
import org.apache.kafka.common.header.Headers;
import org.apache.kafka.common.header.internals.RecordHeaders;
import org.apache.kafka.common.serialization.Serializer;
import org.apache.kafka.common.serialization.StringSerializer;
import ru.tinkoff.qa.neptune.core.api.data.format.DataTransformer;
import ru.tinkoff.qa.neptune.core.api.steps.SequentialActionSupplier;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.MaxDepthOfReporting;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.StepParameter;
import ru.tinkoff.qa.neptune.kafka.KafkaStepContext;
import ru.tinkoff.qa.neptune.kafka.captors.ProducerRecordKeyCaptor;
import ru.tinkoff.qa.neptune.kafka.captors.ProducerRecordValueCaptor;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;
import static java.util.Objects.nonNull;
import static java.util.Optional.ofNullable;
import static ru.tinkoff.qa.neptune.core.api.event.firing.StaticEventFiring.catchValue;
import static ru.tinkoff.qa.neptune.core.api.event.firing.annotations.CaptorUtil.getCaptors;
import static ru.tinkoff.qa.neptune.kafka.properties.DefaultDataTransformers.KAFKA_DEFAULT_DATA_TRANSFORMER;
import static ru.tinkoff.qa.neptune.kafka.properties.DefaultDataTransformers.KAFKA_KEY_TRANSFORMER;
import static ru.tinkoff.qa.neptune.kafka.properties.KafkaCallbackProperty.KAFKA_CALLBACK;
import static ru.tinkoff.qa.neptune.kafka.properties.KafkaDefaultTopicForSendProperty.DEFAULT_TOPIC_FOR_SEND;

@SequentialActionSupplier.DefinePerformImperativeParameterName("Send to Kafka:")
@MaxDepthOfReporting(0)
@Description("message.")
@SuppressWarnings("unchecked")
public class KafkaSendRecordsActionSupplier<K, V, T extends KafkaSendRecordsActionSupplier<K, V, T>> extends SequentialActionSupplier<KafkaStepContext, KafkaProducer<K, V>, KafkaSendRecordsActionSupplier<K, V, T>> {

    private final Serializer<V> valueSerializer;
    //TODO make private and final after refactor
    V value;
    private K key;
    private Serializer<K> keySerializer;

    @StepParameter("topic")
    private String topic = DEFAULT_TOPIC_FOR_SEND.get();

    @StepParameter("partition")
    private Integer partition;

    @StepParameter("timestamp")
    private Long timestamp;

    @StepParameter("headers")
    private final Headers headers = new RecordHeaders();

    private Callback callback  = KAFKA_CALLBACK.get();

    private KafkaSendRecordsActionSupplier(Serializer<K> keySerializer, Serializer<V> valueSerializer, V value) {
        super();
        checkNotNull(valueSerializer);
        checkNotNull(keySerializer);
        this.valueSerializer = valueSerializer;
        this.value = value;
        this.keySerializer = keySerializer;
        performOn(kafkaStepContext -> kafkaStepContext.createProducer(this.keySerializer, this.valueSerializer));
    }

    /**
     * Sends an object to topic. This object is serialized to string message.
     *
     * @param toSend is an object to be serialized and send to the topic
     * @return an instance of {@link KafkaSendRecordsActionSupplier.Mapped}
     * @deprecated use {@link #producerRecord(Serializer, Object)}
     */
    @Deprecated(forRemoval = true)
    public static Mapped kafkaSerializedMessage(Object toSend) {
        return new Mapped(toSend);
    }

    /**
     * Sends a message to topic.
     *
     * @param message is a message to send
     * @return an instance of {@link KafkaSendRecordsActionSupplier}
     * @deprecated use {@link #producerRecord(String)}
     */
    @Deprecated(forRemoval = true)
    public static KafkaSendRecordsActionSupplier<String, String, ?> kafkaTextMessage(String message) {
        return producerRecord(message);
    }

    /**
     * Sends a message to topic.
     *
     * @param valueSerializer is a serializer value
     * @param value           is message value
     * @param <V>             is a type of message value
     * @return an instance of {@link KafkaSendRecordsActionSupplier}
     */
    public static <V> KafkaSendRecordsActionSupplier<String, V, ?> producerRecord(Serializer<V> valueSerializer, V value) {
        return new KafkaSendRecordsActionSupplier<>(new StringSerializer(), valueSerializer, value);
    }

    /**
     * Sends a message to topic as text.
     *
     * @param value is message value
     * @return an instance of {@link KafkaSendRecordsActionSupplier}
     */
    public static KafkaSendRecordsActionSupplier<String, String, ?> producerRecord(String value) {
        return producerRecord(new StringSerializer(), value);
    }

    public T topic(String topic) {
        this.topic = topic;
        return (T) this;
    }

    public T partition(Integer partition) {
        this.partition = partition;
        return (T) this;
    }

    public T timestamp(Long timestamp) {
        this.timestamp = timestamp;
        return (T) this;
    }

    public <K2> KafkaSendRecordsActionSupplier<K2, V, ?> setKey(K2 key, Serializer<K2> keySerializer) {
        checkNotNull(keySerializer);
        KafkaSendRecordsActionSupplier<K2, V, ?> selfReference = (KafkaSendRecordsActionSupplier<K2, V, ?>) this;
        selfReference.keySerializer = keySerializer;
        selfReference.key = key;
        return selfReference;
    }

    public KafkaSendRecordsActionSupplier<K, V, ?> setKey(K key) {
        this.key = key;
        return this;
    }

    /**
     * @deprecated use {@link #setKey(Object, Serializer)} or {@link #setKey(Object)}
     */
    @Deprecated(forRemoval = true)
    public KafkaSendRecordsActionSupplier<String, V, ?> key(String key) {
        return setKey(key, new StringSerializer());
    }

    /**
     * @deprecated use {@link #setKey(Object, Serializer)} or {@link #setKey(Object)}
     */
    @Deprecated(forRemoval = true)
    public KafkaSendRecordsActionSupplier<String, V, ?> key(Object key, DataTransformer dataTransformer) {
        return key(dataTransformer.serialize(key));
    }

    /**
     * @deprecated use {@link #setKey(Object, Serializer)} or {@link #setKey(Object)}
     */
    @Deprecated(forRemoval = true)
    public KafkaSendRecordsActionSupplier<String, V, ?> key(Object key) {
        var transformer = KAFKA_KEY_TRANSFORMER.get();
        checkState(nonNull(transformer), "Key transformer is not defined. Please invoke "
            + "the '#key(Object key, DataTransformer dataTransformer)' method or define '"
            + KAFKA_KEY_TRANSFORMER.getName()
            + "' property/env variable");
        return key(key, transformer);
    }

    public T header(Header header) {
        this.headers.add(header);
        return (T) this;
    }

    public T header(String key, byte[] bytes) {
        this.headers.add(key, bytes);
        return (T) this;
    }

    public T header(String key, String value) {
        return header(key, value.getBytes());
    }

    public T callback(Callback callback) {
        this.callback = callback;
        return (T) this;
    }

    @Override
    @SuppressWarnings("unchecked")
    protected void onStart(KafkaStepContext kafkaStepContext) {
        catchValue(key, getCaptors(new Class[]{ProducerRecordKeyCaptor.class}));
        catchValue(value, getCaptors(new Class[]{ProducerRecordValueCaptor.class}));
    }

    @Override
    protected void howToPerform(KafkaProducer<K, V> producer) {
        try (producer) {
            var producerRecord = new ProducerRecord<>(
                topic,
                partition,
                timestamp,
                key,
                value,
                headers);

            if (callback == null) {
                producer.send(producerRecord);
            } else {
                producer.send(producerRecord, callback);
            }
        }
    }

    public static final class Mapped extends KafkaSendRecordsActionSupplier<String, String, Mapped> {
        private final Object toSend;
        private DataTransformer transformer;

        private Mapped(Object toSend) {
            super(new StringSerializer(), new StringSerializer(), null);
            checkNotNull(toSend);
            this.toSend = toSend;
        }

        public Mapped dataTransformer(DataTransformer dataTransformer) {
            this.transformer = dataTransformer;
            return this;
        }

        @Override
        protected void onStart(KafkaStepContext kafkaStepContext) {
            var transformer = ofNullable(this.transformer)
                    .orElseGet(KAFKA_DEFAULT_DATA_TRANSFORMER);
            checkState(nonNull(transformer), "Data transformer is not defined. Please invoke "
                    + "the '#withDataTransformer(DataTransformer)' method or define '"
                    + KAFKA_DEFAULT_DATA_TRANSFORMER.getName()
                    + "' property/env variable");

            value = transformer.serialize(toSend);
            super.onStart(kafkaStepContext);
        }
    }
}
