package ru.tinkoff.qa.neptune.kafka.functions.poll;

import com.fasterxml.jackson.core.type.TypeReference;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.TopicPartition;
import ru.tinkoff.qa.neptune.core.api.data.format.DataTransformer;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.StepParameter;
import ru.tinkoff.qa.neptune.core.api.steps.parameters.StepParameterPojo;
import ru.tinkoff.qa.neptune.kafka.KafkaStepContext;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.Function;

import static com.google.common.base.Preconditions.checkArgument;
import static java.time.Duration.ofNanos;
import static java.util.Objects.isNull;
import static java.util.stream.Collectors.toList;

final class GetFromTopics<T> implements Function<KafkaStepContext, List<T>>, StepParameterPojo {
    @StepParameter("topics")
    private final List<String> topics;

    @StepParameter(value = "Class to deserialize to", doNotReportNullValues = true)
    private final Class<T> cls;

    @StepParameter(value = "Type to deserialize to", doNotReportNullValues = true)
    private final TypeReference<T> typeRef;
    private DataTransformer transformer;


    GetFromTopics(List<String> topics, Class<T> cls, TypeReference<T> typeRef) {
        checkArgument(!topics.isEmpty(), "Topics should be defined");
        checkArgument(!(isNull(cls) && isNull(typeRef)), "Any class or type reference should be defined");
        this.topics = topics;
        this.cls = cls;
        this.typeRef = typeRef;
    }

    GetFromTopics(List<String> topics, Class<T> cls) {
        this(topics, cls, null);
    }

    GetFromTopics(List<String> topics, TypeReference<T> typeRef) {
        this(topics, null, typeRef);
    }

    @Override
    public List<T> apply(KafkaStepContext kafkaStepContext) {
        KafkaConsumer<String, String> consumer = kafkaStepContext.getConsumer();
        consumer.subscribe(topics);

        ConsumerRecords<String, String> consumerRecords = consumer.poll(ofNanos(1));
        Set<TopicPartition> partitions = consumerRecords.partitions();

        if (partitions.size() == 0) {
            return new ArrayList<>();
        }

        if (cls != null) {
            return consumerRecords.records(((TopicPartition) partitions.toArray()[0]))
                    .stream()
                    .map(consumerRecord -> transformer.deserialize(consumerRecord.value(), cls))
                    .collect(toList());
        }
        return consumerRecords.records(((TopicPartition) partitions.toArray()[0]))
                .stream()
                .map(consumerRecord -> transformer.deserialize(consumerRecord.value(), typeRef))
                .collect(toList());
    }

    void setTransformer(DataTransformer transformer) {
        this.transformer = transformer;
    }
}
