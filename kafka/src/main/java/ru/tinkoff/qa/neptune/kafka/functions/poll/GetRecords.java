package ru.tinkoff.qa.neptune.kafka.functions.poll;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.common.TopicPartition;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.StepParameter;
import ru.tinkoff.qa.neptune.core.api.steps.parameters.StepParameterPojo;
import ru.tinkoff.qa.neptune.kafka.KafkaStepContext;

import java.util.*;
import java.util.function.Function;

import static java.time.Duration.ofNanos;
import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;
import static java.util.stream.StreamSupport.stream;

@SuppressWarnings("unchecked")
public class GetRecords implements Function<KafkaStepContext, List<ConsumerRecord<String, String>>>, StepParameterPojo, CollectsMessages {
    @StepParameter(value = "topics", makeReadableBy = TopicValueGetter.class)
    private final String[] topics;

    private List<KafkaRecordWrapper> readRecords = new ArrayList<>();

    public GetRecords(String[] topics) {
        this.topics = topics;
    }

    @Override
    public List<ConsumerRecord<String, String>> apply(KafkaStepContext context) {
        var kafkaConsumer = context.getConsumer();
        kafkaConsumer.subscribe(asList(topics));

        ConsumerRecords<String, String> consumerRecords = kafkaConsumer.poll(ofNanos(1));
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
    public <V> CombineProp<V> andThen(Function<? super List<ConsumerRecord<String, String>>, ? extends V> after) {
        Objects.requireNonNull(after);
        return new CombineProp<>(this, (Function<List<ConsumerRecord<String, String>>, V>) after);
    }

    @SuppressWarnings("unchecked")
    //todo переименовать
    static class CombineProp<T> implements Function<KafkaStepContext, T>, StepParameterPojo, CollectsMessages {

        private final GetRecords before;
        private final Function<List<ConsumerRecord<String, String>>, T> after;

        protected CombineProp(GetRecords f,
                              Function<List<ConsumerRecord<String, String>>, T> f2) {
            this.before = f;
            this.after = f2;
        }

        public T apply(KafkaStepContext k) {
            return (T) before.andThen(after).apply(k);
        }

        @Override
        public Map<String, String> getParameters() {
            if (before != null) {
                if (after instanceof StepParameterPojo) {
                    var parameters = before.getParameters();
                    parameters.putAll(((StepParameterPojo) after).getParameters());
                    return parameters;
                }
            }
            return StepParameterPojo.super.getParameters();
        }

        @Override
        public List<String> getMessages() {
            return before.getMessages();
        }
    }

    @Override
    public List<String> getMessages() {
        return readRecords.stream().map(r -> r.getConsumerRecord().toString()).collect(toList());
    }
}
