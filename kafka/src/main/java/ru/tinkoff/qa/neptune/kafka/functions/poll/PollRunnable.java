package ru.tinkoff.qa.neptune.kafka.functions.poll;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.Deserializer;
import ru.tinkoff.qa.neptune.kafka.KafkaStepContext;
import ru.tinkoff.qa.neptune.kafka.jackson.desrializer.KafkaJacksonModule;

import java.util.*;

import static com.google.common.base.Preconditions.checkNotNull;
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
import static ru.tinkoff.qa.neptune.kafka.properties.KafkaDefaultTopicsForPollProperty.DEFAULT_TOPICS_FOR_POLL;

final class PollRunnable<K, V> implements Runnable {

    private String[] topics;

    private boolean excludeNullValues;

    private boolean excludeNullKeys;

    private KafkaConsumer<K, V> kafkaConsumer;

    private Boolean isPolling = false;

    private boolean toPoll = true;
    private Throwable thrown;

    private List<KafkaRecordWrapper<K, V>> readRecords = new ArrayList<>();

    @Override
    public void run() {
        var topicsToSubscribe = (isNull(topics) || topics.length == 0) ? DEFAULT_TOPICS_FOR_POLL.get() : topics;
        kafkaConsumer.subscribe(asList(topicsToSubscribe));

        while (toPoll) {
            synchronized (this) {
                try {
                    var consumerRecords = kafkaConsumer.poll(ofNanos(1));

                    readRecords.addAll(stream(consumerRecords.spliterator(), false)
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
                        .collect(toList()));

                    readRecords = readRecords.stream().distinct().collect(toList());
                    isPolling = true;
                } catch (Throwable t) {
                    thrown = t;
                    throw t;
                }
            }
        }
    }

    synchronized void stopPolling() {
        toPoll = false;
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

        kafkaConsumer = ofNullable(kafkaConsumer).orElseGet(() -> context.createConsumer(
            keyDeserializer,
            valueDeserializer,
            additionalProperties)
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

    List<ConsumerRecord<K, V>> getConsumedRecords() {
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
