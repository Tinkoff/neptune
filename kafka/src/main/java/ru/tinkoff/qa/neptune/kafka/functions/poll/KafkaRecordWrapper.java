package ru.tinkoff.qa.neptune.kafka.functions.poll;

import com.google.gson.Gson;
import org.apache.kafka.clients.consumer.ConsumerRecord;

import java.util.Objects;

import static com.google.common.base.Preconditions.checkNotNull;

final class KafkaRecordWrapper<K, V> {

    private final ConsumerRecord<K, V> consumerRecord;
    private final String recordAsString;

    KafkaRecordWrapper(ConsumerRecord<K, V> consumerRecord) {
        checkNotNull(consumerRecord);
        this.consumerRecord = consumerRecord;
        recordAsString = new Gson().toJson(consumerRecord);
    }

    ConsumerRecord<K, V> getConsumerRecord() {
        return consumerRecord;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof KafkaRecordWrapper)) {
            return false;
        }

        return Objects.equals(recordAsString, ((KafkaRecordWrapper<?, ?>) obj).recordAsString);
    }

    @Override
    public int hashCode() {
        return recordAsString.hashCode();
    }

    public String toString() {
        return "ConsumerRecord(" + recordAsString + ")";
    }
}
