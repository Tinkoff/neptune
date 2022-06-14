package ru.tinkoff.qa.neptune.kafka.functions.poll;

import org.apache.kafka.clients.consumer.ConsumerRecord;

import java.util.Objects;

import static com.google.common.base.Preconditions.checkNotNull;

final class KafkaRecordWrapper {

    private final ConsumerRecord<String, String> consumerRecord;

    KafkaRecordWrapper(ConsumerRecord<String, String> consumerRecord) {
        checkNotNull(consumerRecord);
        this.consumerRecord = consumerRecord;
    }

    ConsumerRecord<String, String> getConsumerRecord() {
        return consumerRecord;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof KafkaRecordWrapper && Objects.equals(
            ((KafkaRecordWrapper) obj).getConsumerRecord().toString(),
            consumerRecord.toString());
    }
}
