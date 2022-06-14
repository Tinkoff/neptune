package ru.tinkoff.qa.neptune.kafka.functions.poll;

import com.fasterxml.jackson.core.type.TypeReference;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.TopicPartition;
import ru.tinkoff.qa.neptune.core.api.data.format.DataTransformer;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.StepParameter;
import ru.tinkoff.qa.neptune.core.api.steps.parameters.StepParameterPojo;

import java.lang.reflect.Type;
import java.util.*;
import java.util.function.Function;

import static com.google.common.base.Preconditions.checkArgument;
import static java.time.Duration.ofNanos;
import static java.util.Arrays.asList;
import static java.util.Objects.isNull;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;
import static java.util.stream.StreamSupport.stream;
import static ru.tinkoff.qa.neptune.kafka.properties.KafkaDefaultTopicsForPollProperty.DEFAULT_TOPICS_FOR_POLL;

final class GetFromTopics<T> implements Function<KafkaConsumer<String, String>, List<T>>, StepParameterPojo {

    private final Map<Object, ConsumerRecord<String, String>> successMessages = new HashMap<>();

    @StepParameter(value = "topics", makeReadableBy = TopicValueGetter.class)
    private final String[] topics;

    @StepParameter(value = "Class to deserialize to", doNotReportNullValues = true)
    private final Class<T> cls;

    private final TypeReference<T> typeRef;

    @StepParameter(value = "Type to deserialize to", doNotReportNullValues = true)
    final Type type;

    private DataTransformer transformer;
    private List<KafkaRecordWrapper> readRecords = new ArrayList<>();

    GetFromTopics(Class<T> cls, TypeReference<T> typeRef, String... topics) {
        checkArgument(!(isNull(cls) && isNull(typeRef)), "Any class or type reference should be defined");
        this.topics = topics.length == 0 ? DEFAULT_TOPICS_FOR_POLL.get() : topics;
        this.cls = cls;
        this.typeRef = typeRef;
        this.type = ofNullable(typeRef).map(TypeReference::getType).orElse(null);

    }

    GetFromTopics(Class<T> cls, String... topics) {
        this(cls, null, topics);
    }

    GetFromTopics(TypeReference<T> typeRef, String... topics) {
        this(null, typeRef, topics);
    }

    static GetFromTopics<String> getStringResult(String... topics) {
        return new GetFromTopics<>(String.class, null, topics);
    }

    @Override
    public List<T> apply(KafkaConsumer<String, String> consumer) {
        consumer.subscribe(asList(topics));

        ConsumerRecords<String, String> consumerRecords = consumer.poll(ofNanos(1));
        Set<TopicPartition> partitions = consumerRecords.partitions();

        if (partitions.isEmpty()) {
            return new ArrayList<>();
        }

        readRecords.addAll(stream(consumerRecords.spliterator(), false)
            .map(KafkaRecordWrapper::new)
            .collect(toList()));

        readRecords = readRecords.stream().distinct().collect(toList());

        return readRecords
            .stream()
            .map(record -> {
                try {
                    T t;
                    var value = record.getConsumerRecord().value();
                    if (cls != null) {
                        t = transformer.deserialize(value, cls);
                    } else {
                        t = transformer.deserialize(value, typeRef);
                    }
                    successMessages.put(t, record.getConsumerRecord());
                        return t;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                return null;
            })
            .filter(Objects::nonNull)
            .collect(toList());
    }

    void setTransformer(DataTransformer transformer) {
        this.transformer = transformer;
    }

    List<String> getMessages() {
        return readRecords.stream().map(r -> r.getConsumerRecord().toString()).collect(toList());
    }

    Map<Object, ConsumerRecord<String, String>> getSuccessMessages() {
        return successMessages;
    }
}
