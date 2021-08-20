package ru.tinkoff.qa.neptune.kafka.functions.send;

import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.Header;
import ru.tinkoff.qa.neptune.core.api.data.format.DataTransformer;
import ru.tinkoff.qa.neptune.core.api.event.firing.annotations.MaxDepthOfReporting;
import ru.tinkoff.qa.neptune.core.api.steps.SequentialActionSupplier;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.StepParameter;
import ru.tinkoff.qa.neptune.kafka.KafkaStepContext;
import ru.tinkoff.qa.neptune.kafka.captors.MessageCaptor;

import static com.google.common.base.Preconditions.*;
import static java.util.Objects.nonNull;
import static java.util.Optional.ofNullable;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static ru.tinkoff.qa.neptune.core.api.event.firing.StaticEventFiring.catchValue;
import static ru.tinkoff.qa.neptune.core.api.event.firing.annotations.CaptorUtil.createCaptors;
import static ru.tinkoff.qa.neptune.kafka.properties.KafkaDefaultDataTransformer.KAFKA_DEFAULT_DATA_TRANSFORMER;
import static ru.tinkoff.qa.neptune.kafka.properties.KafkaDefaultTopicForSendSupplier.DEFAULT_TOPIC_FOR_SEND;

@SequentialActionSupplier.DefinePerformImperativeParameterName("Send:")
@MaxDepthOfReporting(0)
@Description("message.")
@SuppressWarnings("unchecked")
public abstract class KafkaSendRecordsActionSupplier<K, V, T extends KafkaSendRecordsActionSupplier<K, V, T>> extends SequentialActionSupplier<KafkaStepContext, KafkaStepContext, T> {
    @StepParameter("topic")
    private String topic = DEFAULT_TOPIC_FOR_SEND.get();
    String value;
    @StepParameter("partition")
    private Integer partition;
    @StepParameter("timestamp")
    private Long timestamp;
    @StepParameter("key")
    private Object key;
    @StepParameter("headers")
    private Iterable<Header> headers;
    private Callback callback;

    public KafkaSendRecordsActionSupplier() {
        super();
        performOn(kafkaStepContext -> kafkaStepContext);
    }

    public static KafkaSendRecordsActionSupplier.Mapped serializedMessage(Object toSend) {
        return new Mapped(toSend);
    }

    public static KafkaSendRecordsActionSupplier.StringMessage textMessage(String message) {
        return new StringMessage(message);
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

    public T key(Object key) {
        this.key = key;
        return (T) this;
    }

    public T headers(Iterable<Header> headers) {
        this.headers = headers;
        return (T) this;
    }

    public T callback(Callback callback) {
        this.callback = callback;
        return (T) this;
    }

    @Override
    protected void howToPerform(KafkaStepContext kafkaStepContext) {
        KafkaProducer<K, V> producer = kafkaStepContext.getProducer();

        ProducerRecord<Object, String> records;

        records = new ProducerRecord<>(
                topic,
                partition,
                timestamp,
                key,
                value,
                headers);

        if (callback == null) {
            producer.send((ProducerRecord<K, V>) records);
        } else {
            producer.send((ProducerRecord<K, V>) records, callback);
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    protected void onStart(KafkaStepContext kafkaStepContext) {
        catchValue(value, createCaptors(new Class[]{MessageCaptor.class}));
    }

    public static final class Mapped extends KafkaSendRecordsActionSupplier<Object, Object, Mapped> {
        private final Object toSend;
        private DataTransformer transformer;


        public Mapped(Object toSend) {
            super();
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

    public static final class StringMessage extends KafkaSendRecordsActionSupplier<Object, Object, StringMessage> {

        private StringMessage(String message) {
            super();
            checkArgument(isNotBlank(message), "Message to publish should not be blank");
            value = message;
        }
    }
}
