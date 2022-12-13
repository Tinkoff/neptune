package ru.tinkoff.qa.neptune.kafka.functions.poll;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.Deserializer;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.StepParameter;
import ru.tinkoff.qa.neptune.core.api.steps.parameters.StepParameterPojo;
import ru.tinkoff.qa.neptune.kafka.KafkaStepContext;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static java.time.Duration.ofNanos;
import static java.util.Arrays.asList;
import static java.util.Objects.isNull;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;
import static java.util.stream.StreamSupport.stream;
import static ru.tinkoff.qa.neptune.kafka.properties.KafkaDefaultTopicsForPollProperty.DEFAULT_TOPICS_FOR_POLL;

@SuppressWarnings("unchecked")
class GetRecords<K, V> implements Function<KafkaStepContext, List<ConsumerRecord<K, V>>>, StepParameterPojo {

    @StepParameter(value = "topics", makeReadableBy = TopicValueGetter.class)
    private String[] topics;
    private List<KafkaRecordWrapper<K, V>> readRecords = new ArrayList<>();

    private Deserializer<K> keyDeserializer;

    private Deserializer<V> valueDeserializer;

    private KafkaConsumer<K, V> kafkaConsumer;

    @Override
    public List<ConsumerRecord<K, V>> apply(KafkaStepContext context) {
        kafkaConsumer = ofNullable(kafkaConsumer).orElseGet(() -> context.createConsumer(
            keyDeserializer,
            valueDeserializer)
        );

        var topicsToSubscribe = (isNull(topics) || topics.length == 0) ? DEFAULT_TOPICS_FOR_POLL.get() : topics;
        kafkaConsumer.subscribe(asList(topicsToSubscribe));

        var consumerRecords = kafkaConsumer.poll(ofNanos(1));
        var partitions = consumerRecords.partitions();

        if (partitions.isEmpty()) {
            return new ArrayList<>();
        }

        readRecords.addAll(stream(consumerRecords.spliterator(), false)
            .map(KafkaRecordWrapper::new)
            .collect(toList()));

        readRecords = readRecords.stream().distinct().collect(toList());

        return readRecords.stream()
            .map(KafkaRecordWrapper::getConsumerRecord)
            .collect(toList());
    }

    <K2> GetRecords<K2, V> setKeyDeserializer(Deserializer<K2> keyDeserializer) {
        checkNotNull(keyDeserializer);
        var thisRef = (GetRecords<K2, V>) this;
        thisRef.keyDeserializer = keyDeserializer;
        return thisRef;
    }

    <V2> GetRecords<K, V2> setValueDeserializer(Deserializer<V2> valueDeserializer) {
        checkNotNull(valueDeserializer);
        var thisRef = (GetRecords<K, V2>) this;
        thisRef.valueDeserializer = valueDeserializer;
        return thisRef;
    }

    void topics(String... topics) {
        checkNotNull(topics);
        checkArgument(topics.length > 0, "At least one topic should be defined");
        this.topics = topics;
    }

    void closeConsumer() {
        ofNullable(kafkaConsumer).ifPresent(KafkaConsumer::close);
    }

    List<String> getMessages() {
        return readRecords.stream().map(KafkaRecordWrapper::toString).collect(toList());
    }

}
