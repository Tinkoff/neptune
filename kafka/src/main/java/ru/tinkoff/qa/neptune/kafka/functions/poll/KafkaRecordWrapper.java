package ru.tinkoff.qa.neptune.kafka.functions.poll;

import org.apache.kafka.clients.consumer.ConsumerRecord;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.apache.commons.lang3.builder.EqualsBuilder.reflectionEquals;
import static org.apache.commons.lang3.builder.HashCodeBuilder.reflectionHashCode;

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
        return obj instanceof KafkaRecordWrapper && reflectionEquals(
            ((KafkaRecordWrapper) obj).getConsumerRecord(),
            consumerRecord);
    }

    @Override
    public int hashCode() {
        return reflectionHashCode(consumerRecord);
    }
}
