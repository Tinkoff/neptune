package ru.tinkoff.qa.neptune.kafka.functions.poll;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.TopicPartition;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.StepParameter;
import ru.tinkoff.qa.neptune.core.api.steps.parameters.StepParameterPojo;
import ru.tinkoff.qa.neptune.kafka.KafkaStepContext;

import java.util.*;
import java.util.function.Function;

import static java.time.Duration.ofMillis;
import static java.util.Arrays.asList;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;
import static java.util.stream.StreamSupport.stream;
import static ru.tinkoff.qa.neptune.kafka.properties.KafkaDefaultTopicsForPollProperty.DEFAULT_TOPICS_FOR_POLL;

@SuppressWarnings("unchecked")
class GetRecords implements Function<KafkaStepContext, List<ConsumerRecord<String, String>>>, StepParameterPojo {

    @StepParameter(value = "topics", makeReadableBy = TopicValueGetter.class)
    private final String[] topics;
    private List<KafkaRecordWrapper> readRecords = new ArrayList<>();

    public GetRecords(String[] topics) {
        this.topics = topics.length == 0 ? DEFAULT_TOPICS_FOR_POLL.get() : topics;
    }

    private KafkaConsumer<String, String> kafkaConsumer;

    @Override
    public List<ConsumerRecord<String, String>> apply(KafkaStepContext context) {
        kafkaConsumer = ofNullable(kafkaConsumer).orElseGet(context::createConsumer);

        kafkaConsumer.subscribe(asList(topics));
        ConsumerRecords<String, String> consumerRecords = kafkaConsumer.poll(ofMillis(100));
        Set<TopicPartition> partitions = consumerRecords.partitions();

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

    @Override
    public <V> MergeProperty<V> andThen(Function<? super List<ConsumerRecord<String, String>>, ? extends V> after) {
        Objects.requireNonNull(after);
        return new MergeProperty<>(this, (Function<List<ConsumerRecord<String, String>>, V>) after);
    }

    static class MergeProperty<T> implements Function<KafkaStepContext, T>, StepParameterPojo {

        private final GetRecords before;
        private final Function<List<ConsumerRecord<String, String>>, T> after;

        protected MergeProperty(GetRecords f,
                                Function<List<ConsumerRecord<String, String>>, T> f2) {
            this.before = f;
            this.after = f2;
        }

        public T apply(KafkaStepContext k) {
            return after.apply(before.apply(k));
        }

        @Override
        public Map<String, String> getParameters() {
            if (before != null && after instanceof StepParameterPojo) {
                var parameters = before.getParameters();
                parameters.putAll(((StepParameterPojo) after).getParameters());
                return parameters;
            }
            return StepParameterPojo.super.getParameters();
        }

        public GetRecords getBefore() {
            return before;
        }

        public Function<List<ConsumerRecord<String, String>>, T> getAfter() {
            return after;
        }
    }

    public List<String> getMessages() {
        return readRecords.stream().map(r -> r.getConsumerRecord().toString()).collect(toList());
    }
}
