package ru.tinkoff.qa.neptune.kafka.functions.send;

import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.Header;
import org.apache.kafka.common.header.Headers;
import org.apache.kafka.common.header.internals.RecordHeaders;
import org.apache.kafka.common.serialization.Serializer;
import org.apache.kafka.common.serialization.StringSerializer;
import ru.tinkoff.qa.neptune.core.api.steps.SequentialActionSupplier;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.MaxDepthOfReporting;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.StepParameter;
import ru.tinkoff.qa.neptune.kafka.KafkaStepContext;
import ru.tinkoff.qa.neptune.kafka.captors.ProducerRecordKeyCaptor;
import ru.tinkoff.qa.neptune.kafka.captors.ProducerRecordValueCaptor;

import java.util.LinkedHashMap;
import java.util.Map;

import static com.google.common.base.Preconditions.*;
import static com.google.common.base.Preconditions.checkArgument;
import static java.util.Objects.nonNull;
import static java.util.Optional.ofNullable;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static ru.tinkoff.qa.neptune.core.api.event.firing.StaticEventFiring.catchValue;
import static ru.tinkoff.qa.neptune.core.api.event.firing.annotations.CaptorUtil.getCaptors;
import static ru.tinkoff.qa.neptune.kafka.properties.KafkaCallbackProperty.KAFKA_CALLBACK;
import static ru.tinkoff.qa.neptune.kafka.properties.KafkaDefaultTopicForSendProperty.DEFAULT_TOPIC_FOR_SEND;

@SequentialActionSupplier.DefinePerformImperativeParameterName("Send to Kafka:")
@MaxDepthOfReporting(0)
@Description("message.")
@SuppressWarnings("unchecked")
public class KafkaSendRecordsActionSupplier<K, V> extends SequentialActionSupplier<KafkaStepContext, KafkaProducer<K, V>, KafkaSendRecordsActionSupplier<K, V>> {

    private final Serializer<V> valueSerializer;
    private final V value;
    private K key;
    private Serializer<K> keySerializer;

    @StepParameter("topic")
    private String topic = DEFAULT_TOPIC_FOR_SEND.get();

    @StepParameter(value = "partition", doNotReportNullValues = true)
    private Integer partition;

    @StepParameter(value = "timestamp", doNotReportNullValues = true)
    private Long timestamp;

    @StepParameter(value = "headers", doNotReportNullValues = true)
    private Headers headers;

    private Map<String, String> additionalProperties;

    private Callback callback  = KAFKA_CALLBACK.get();

    private KafkaSendRecordsActionSupplier(Serializer<K> keySerializer, Serializer<V> valueSerializer, V value) {
        super();
        checkNotNull(valueSerializer);
        checkNotNull(keySerializer);
        this.valueSerializer = valueSerializer;
        this.value = value;
        this.keySerializer = keySerializer;
        performOn(kafkaStepContext -> kafkaStepContext.createProducer(this.keySerializer, this.valueSerializer, additionalProperties));
    }

    /**
     * Sends a message to topic.
     *
     * @param valueSerializer is a serializer value
     * @param value           is message value
     * @param <V>             is a type of message value
     * @return an instance of {@link KafkaSendRecordsActionSupplier}
     */
    public static <V> KafkaSendRecordsActionSupplier<String, V> producerRecord(Serializer<V> valueSerializer, V value) {
        return new KafkaSendRecordsActionSupplier<>(new StringSerializer(), valueSerializer, value);
    }

    /**
     * Sends a message to topic as text.
     *
     * @param value is message value
     * @return an instance of {@link KafkaSendRecordsActionSupplier}
     */
    public static KafkaSendRecordsActionSupplier<String, String> producerRecord(String value) {
        return producerRecord(new StringSerializer(), value);
    }

    public KafkaSendRecordsActionSupplier<K, V> topic(String topic) {
        checkNotNull(topic);
        this.topic = topic;
        return this;
    }

    public KafkaSendRecordsActionSupplier<K, V> partition(Integer partition) {
        this.partition = partition;
        return this;
    }

    public KafkaSendRecordsActionSupplier<K, V> timestamp(Long timestamp) {
        this.timestamp = timestamp;
        return this;
    }

    public <K2> KafkaSendRecordsActionSupplier<K2, V> setKey(K2 key, Serializer<K2> keySerializer) {
        checkNotNull(keySerializer);
        KafkaSendRecordsActionSupplier<K2, V> selfReference = (KafkaSendRecordsActionSupplier<K2, V>) this;
        selfReference.keySerializer = keySerializer;
        selfReference.key = key;
        return selfReference;
    }

    public KafkaSendRecordsActionSupplier<K, V> setKey(K key) {
        this.key = key;
        return this;
    }

    public KafkaSendRecordsActionSupplier<K, V> header(Header header) {
        this.headers = ofNullable(headers).orElseGet(RecordHeaders::new);
        this.headers.add(header);
        return this;
    }

    public KafkaSendRecordsActionSupplier<K, V> header(String key, byte[] bytes) {
        this.headers = ofNullable(headers).orElseGet(RecordHeaders::new);
        this.headers.add(key, bytes);
        return this;
    }

    public KafkaSendRecordsActionSupplier<K, V> header(String key, String value) {
        return header(key, value.getBytes());
    }

    public KafkaSendRecordsActionSupplier<K, V> callback(Callback callback) {
        this.callback = callback;
        return this;
    }

    @Override
    @SuppressWarnings("unchecked")
    protected void onStart(KafkaStepContext kafkaStepContext) {
        catchValue(key, getCaptors(new Class[]{ProducerRecordKeyCaptor.class}));
        catchValue(value, getCaptors(new Class[]{ProducerRecordValueCaptor.class}));
    }

    @Override
    protected void howToPerform(KafkaProducer<K, V> producer) {
        checkState(nonNull(topic), "Topic is not defined. Please define the "
            + DEFAULT_TOPIC_FOR_SEND.getName()
            + " or use the #topic(String) method");
        try (producer) {
            var producerRecord = new ProducerRecord<>(
                topic,
                partition,
                timestamp,
                key,
                value,
                ofNullable(headers).orElseGet(RecordHeaders::new));

            if (callback == null) {
                producer.send(producerRecord);
            } else {
                producer.send(producerRecord, callback);
            }
        }
    }

    @Override
    public Map<String, String> getParameters() {
        var result =  super.getParameters();
        ofNullable(additionalProperties).ifPresent(result::putAll);
        return result;
    }

    /**
     * Defines producer property value
     *
     * @see <a href="https://kafka.apache.org/documentation/#producerconfigs">Producer Configs</a>
     *
     * @param property a property name
     * @param value a property value
     * @return self-reference
     */
    public KafkaSendRecordsActionSupplier<K, V> setProperty(String property, String value) {
        checkArgument(isNotBlank(property), "Property name should not be empty or null");
        checkArgument(isNotBlank(value), "Property value should not be empty or null");
        additionalProperties = ofNullable(additionalProperties).orElseGet(LinkedHashMap::new);
        additionalProperties.put(property, value);
        return this;
    }
}
