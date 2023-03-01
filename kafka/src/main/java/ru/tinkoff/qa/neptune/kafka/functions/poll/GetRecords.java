package ru.tinkoff.qa.neptune.kafka.functions.poll;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.KafkaException;
import ru.tinkoff.qa.neptune.kafka.KafkaStepContext;

import java.util.List;
import java.util.function.Function;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

final class GetRecords<K, V> implements Function<KafkaStepContext, List<ConsumerRecord<K, V>>> {

    private PollRunnable<K, V> pollRunnable;

    GetRecords<K, V> setPollRunnable(PollRunnable<K, V> pollRunnable) {
        this.pollRunnable = pollRunnable;
        return this;
    }

    @Override
    public List<ConsumerRecord<K, V>> apply(KafkaStepContext ignored) {
        while (!pollRunnable.isPolling() && isNull(pollRunnable.getThrown())) {
        }

        var thrown = pollRunnable.getThrown();
        if (nonNull(pollRunnable.getThrown())) {
            throw new KafkaException(thrown);
        }

        return pollRunnable.getConsumedRecords();
    }
}
