package ru.tinkoff.qa.neptune.kafka.functions.poll;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.Deserializer;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.StepParameter;
import ru.tinkoff.qa.neptune.core.api.steps.parameters.StepParameterPojo;
import ru.tinkoff.qa.neptune.kafka.KafkaStepContext;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.Function;

import static com.google.common.base.Preconditions.checkNotNull;
import static java.time.Duration.ofNanos;
import static java.util.Arrays.asList;
import static java.util.Objects.nonNull;
import static java.util.stream.Collectors.toList;
import static java.util.stream.StreamSupport.stream;
import static ru.tinkoff.qa.neptune.kafka.properties.KafkaDefaultTopicsForPollProperty.DEFAULT_TOPICS_FOR_POLL;

@SuppressWarnings("unchecked")
class GetRecords<K, V> implements Function<KafkaStepContext, List<ConsumerRecord<K, V>>>, StepParameterPojo {

    @StepParameter(value = "topics", makeReadableBy = TopicValueGetter.class)
    private final String[] topics;
    private List<KafkaRecordWrapper<K, V>> readRecords = new ArrayList<>();

    private Deserializer<K> keyDeserializer;

    private Deserializer<V> valueDeserializer;

    public GetRecords(String[] topics) {
        this.topics = topics.length == 0 ? DEFAULT_TOPICS_FOR_POLL.get() : topics;
    }

    @Override
    public List<ConsumerRecord<K, V>> apply(KafkaStepContext context) {
        var kafkaConsumer = context.createConsumer(
            new InnerDeserializer<>(keyDeserializer),
            new InnerDeserializer<>(valueDeserializer)
        );

        try (kafkaConsumer) {
            kafkaConsumer.subscribe(asList(topics));

            var consumerRecords = kafkaConsumer.poll(ofNanos(1));
            Set<TopicPartition> partitions = consumerRecords.partitions();

            if (partitions.isEmpty()) {
                return new ArrayList<>();
            }

            readRecords.addAll(stream(consumerRecords.spliterator(), false)
                .filter(r -> nonNull(r.key()) && nonNull(r.value()))
                .map(KafkaRecordWrapper::new)
                .collect(toList()));

            readRecords = readRecords.stream().distinct().collect(toList());

            return readRecords.stream()
                .map(KafkaRecordWrapper::getConsumerRecord)
                .collect(toList());
        }
    }

    public <K2> GetRecords<K2, V> setKeyDeserializer(Deserializer<K2> keyDeserializer) {
        checkNotNull(keyDeserializer);
        var thisRef = (GetRecords<K2, V>) this;
        thisRef.keyDeserializer = keyDeserializer;
        return thisRef;
    }

    public <V2> GetRecords<K, V2> setValueDeserializer(Deserializer<V2> valueDeserializer) {
        checkNotNull(valueDeserializer);
        var thisRef = (GetRecords<K, V2>) this;
        thisRef.valueDeserializer = valueDeserializer;
        return thisRef;
    }

    public List<String> getMessages() {
        return readRecords.stream().map(KafkaRecordWrapper::toString).collect(toList());
    }

    private static final class InnerDeserializer<T> implements Deserializer<T> {

        private final Deserializer<T> wrapped;

        private InnerDeserializer(Deserializer<T> wrapped) {
            this.wrapped = wrapped;
        }

        @Override
        public T deserialize(String topic, byte[] data) {
            try {
                return wrapped.deserialize(topic, data);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
    }
}
