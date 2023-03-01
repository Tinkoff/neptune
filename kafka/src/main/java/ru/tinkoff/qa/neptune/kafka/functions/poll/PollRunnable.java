package ru.tinkoff.qa.neptune.kafka.functions.poll;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.Deserializer;
import ru.tinkoff.qa.neptune.kafka.KafkaStepContext;
import ru.tinkoff.qa.neptune.kafka.jackson.desrializer.KafkaJacksonModule;

import java.util.*;

import static com.google.common.base.Preconditions.checkNotNull;
import static java.lang.Thread.sleep;
import static java.time.Duration.ofNanos;
import static java.util.Arrays.asList;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static java.util.Optional.ofNullable;
import static java.util.UUID.randomUUID;
import static java.util.stream.Collectors.toList;
import static java.util.stream.StreamSupport.stream;
import static org.apache.kafka.clients.consumer.ConsumerConfig.AUTO_OFFSET_RESET_CONFIG;
import static org.apache.kafka.clients.consumer.ConsumerConfig.GROUP_ID_CONFIG;

final class PollRunnable<K, V> implements Runnable {

    private String[] topics;

    private boolean excludeNullValues;

    private boolean excludeNullKeys;

    private KafkaConsumer<K, V> kafkaConsumer;

    private Boolean isPolling = false;

    private boolean toPoll = true;
    private Throwable thrown;

    private final LinkedHashSet<KafkaRecordWrapper<K, V>> readRecords = new LinkedHashSet<>();

    @Override
    public void run() {
        readRecords.clear();
        kafkaConsumer.subscribe(isNull(topics) ? null : asList(topics));

        while (toPoll) {
            poll();
            try {
                sleep(50);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private synchronized void poll() {
        try {
            var consumerRecords = kafkaConsumer.poll(ofNanos(1));

            stream(consumerRecords.spliterator(), false)
                .filter(cr -> {
                    boolean result = true;
                    if (excludeNullKeys) {
                        result = nonNull(cr.key());
                    }

                    if (!result) {
                        return false;
                    }

                    if (excludeNullValues) {
                        result = nonNull(cr.value());
                    }

                    return result;
                })
                .map(KafkaRecordWrapper::new)
                .forEach(readRecords::add);

            isPolling = true;
        } catch (Throwable t) {
            thrown = t;
            throw t;
        }
    }

    synchronized void stopPolling() {
        toPoll = false;
        isPolling = false;
        thrown = null;
        ofNullable(kafkaConsumer).ifPresent(KafkaConsumer::close);
    }

    PollRunnable<K, V> setExcludeNullValues(boolean excludeNullValues) {
        this.excludeNullValues = excludeNullValues;
        return this;
    }

    PollRunnable<K, V> setExcludeNullKeys(boolean excludeNullKeys) {
        this.excludeNullKeys = excludeNullKeys;
        return this;
    }

    PollRunnable<K, V> setKafkaConsumer(KafkaStepContext context,
                                        Map<String, String> additionalProperties,
                                        Deserializer<K> keyDeserializer,
                                        Deserializer<V> valueDeserializer,
                                        boolean latest) {
        var additional = new LinkedHashMap<>(additionalProperties);
        if (!additional.containsKey(GROUP_ID_CONFIG)) {
            additional.put(GROUP_ID_CONFIG, randomUUID().toString());
        }

        if (latest) {
            additional.put(AUTO_OFFSET_RESET_CONFIG, "latest");
        } else {
            additional.put(AUTO_OFFSET_RESET_CONFIG, "earliest");
        }

        kafkaConsumer = context.createConsumer(
            keyDeserializer,
            valueDeserializer,
            additional
        );

        return this;
    }

    Throwable getThrown() {
        return thrown;
    }

    PollRunnable<K, V> setTopics(String[] topics) {
        this.topics = topics;
        return this;
    }

    synchronized boolean isPolling() {
        return isPolling;
    }

    synchronized List<ConsumerRecord<K, V>> getConsumedRecords() {
        return readRecords.stream().map(KafkaRecordWrapper::getConsumerRecord).collect(toList());
    }


    private static final class KafkaRecordWrapper<K, V> {

        private static final ObjectMapper MAPPER = new ObjectMapper().registerModule(new KafkaJacksonModule());

        private final ConsumerRecord<K, V> consumerRecord;
        private final String recordAsString;

        KafkaRecordWrapper(ConsumerRecord<K, V> consumerRecord) {
            checkNotNull(consumerRecord);
            this.consumerRecord = consumerRecord;
            try {
                recordAsString = MAPPER.writerWithDefaultPrettyPrinter().writeValueAsString(consumerRecord);
            } catch (Exception e) {
                throw new IllegalStateException(e);
            }
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
    }
}
