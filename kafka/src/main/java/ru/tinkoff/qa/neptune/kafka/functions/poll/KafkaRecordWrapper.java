package ru.tinkoff.qa.neptune.kafka.functions.poll;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.consumer.ConsumerRecord;

import java.util.Map;
import java.util.Objects;

import static com.google.common.base.Preconditions.checkNotNull;

@SuppressWarnings("unchecked")
final class KafkaRecordWrapper<K, V> {

    private final ConsumerRecord<K, V> consumerRecord;
    private final Map<String, ?> recordAsMap;

    KafkaRecordWrapper(ConsumerRecord<K, V> consumerRecord) {
        checkNotNull(consumerRecord);
        this.consumerRecord = consumerRecord;
        var mapper = new ObjectMapper();
        recordAsMap = mapper.convertValue(consumerRecord, Map.class);
    }

    ConsumerRecord<K, V> getConsumerRecord() {
        return consumerRecord;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof KafkaRecordWrapper)) {
            return false;
        }

        return Objects.equals(recordAsMap, ((KafkaRecordWrapper<?, ?>) obj).recordAsMap);
    }

    @Override
    public int hashCode() {
        return recordAsMap.hashCode();
    }

    public String toString() {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(recordAsMap);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException(e);
        }
    }
}
