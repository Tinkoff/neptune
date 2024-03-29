package ru.tinkoff.qa.neptune.kafka;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.apache.kafka.common.header.internals.RecordHeaders;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.mockito.Mock;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;

import java.util.Date;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;

import static java.util.List.of;
import static org.apache.kafka.common.record.TimestampType.LOG_APPEND_TIME;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.clearInvocations;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;
import static ru.tinkoff.qa.neptune.kafka.properties.KafkaCallbackProperty.KAFKA_CALLBACK;
import static ru.tinkoff.qa.neptune.kafka.properties.KafkaDefaultTopicForSendProperty.DEFAULT_TOPIC_FOR_SEND;
import static ru.tinkoff.qa.neptune.kafka.properties.KafkaDefaultTopicsForPollProperty.DEFAULT_TOPICS_FOR_POLL;

@SuppressWarnings("unchecked")
public class KafkaBasePreparations {

    @Mock
    protected KafkaConsumer<DraftDto, DraftDto> consumerWithDeserializedKeyAndValue;

    @Mock
    protected KafkaConsumer<DraftDto, String> consumerWithDeserializedKey;

    @Mock
    protected KafkaConsumer<String, DraftDto> consumerWithDeserializedValue;

    @Mock
    protected KafkaConsumer<String, String> consumerRaw;


    @Mock
    protected KafkaProducer<DraftDto, DraftDto> producerWithDeserializedKeyAndValue;

    @Mock
    protected KafkaProducer<DraftDto, String> producerWithDeserializedKey;

    @Mock
    protected KafkaProducer<String, DraftDto> producerWithDeserializedValue;

    @Mock
    protected KafkaProducer<String, String> producerRaw;

    protected KafkaStepContext kafka;

    protected Properties consumerProps;
    protected Properties producerProperties;

    private static final TopicPartition TOPIC_PARTITION = new TopicPartition("testTopic", 1);

    protected final static ConsumerRecords<String, String> RAW_CONSUMER_RECORDS = new ConsumerRecords<>(Map.of(TOPIC_PARTITION,
        of(new ConsumerRecord<>("testTopic", 1,
                1L,
                new Date().getTime(),
                LOG_APPEND_TIME,
                0,
                0,
                "Some String Key 1",
                "Some String Value 1",
                new RecordHeaders()
                    .add(new RecordHeader("header1", "value1".getBytes()))
                    .add(new RecordHeader("header1", "value2".getBytes()))
                    .add(new RecordHeader("header2", "value1".getBytes())),
                Optional.of(5)),
            new ConsumerRecord<>("testTopic", 1,
                1L,
                new Date().getTime(),
                LOG_APPEND_TIME,
                0,
                0,
                "Some String Key 2",
                "Some String Value 2",
                new RecordHeaders()
                    .add(new RecordHeader("header1", "value1".getBytes()))
                    .add(new RecordHeader("header1", "value2".getBytes()))
                    .add(new RecordHeader("header2", "value1".getBytes())),
                Optional.of(5)),
            new ConsumerRecord<>("testTopic", 1,
                1L,
                new Date().getTime(),
                LOG_APPEND_TIME,
                0,
                0,
                null,
                null,
                new RecordHeaders()
                    .add(new RecordHeader("header1", "value1".getBytes()))
                    .add(new RecordHeader("header1", "value2".getBytes()))
                    .add(new RecordHeader("header2", "value1".getBytes())),
                Optional.of(5)))));

    @BeforeClass
    public void setUp() {
        openMocks(this);
        kafka = new KafkaStepContext() {

            @Override
            <K, V> KafkaConsumer<K, V> createConsumer(Deserializer<K> kDeserializer,
                                                      Deserializer<V> vDeserializer,
                                                      Properties properties) {

                consumerProps = properties;

                if (kDeserializer instanceof SomeDeserializer && vDeserializer instanceof SomeDeserializer) {
                    return (KafkaConsumer<K, V>) consumerWithDeserializedKeyAndValue;
                }

                if (kDeserializer instanceof StringDeserializer && vDeserializer instanceof StringDeserializer) {
                    return (KafkaConsumer<K, V>) consumerRaw;
                }

                if (kDeserializer instanceof SomeDeserializer) {
                    return (KafkaConsumer<K, V>) consumerWithDeserializedKey;
                }

                return (KafkaConsumer<K, V>) consumerWithDeserializedValue;
            }

            @Override
            <K, V> KafkaProducer<K, V> createProducer(Serializer<K> keySerializer,
                                                      Serializer<V> valueSerializer,
                                                      Properties properties) {
                producerProperties = properties;
                if (keySerializer instanceof SomeSerializer && valueSerializer instanceof SomeSerializer) {
                    return (KafkaProducer<K, V>) producerWithDeserializedKeyAndValue;
                }

                if (keySerializer instanceof StringSerializer && valueSerializer instanceof StringSerializer) {
                    return (KafkaProducer<K, V>) producerRaw;
                }

                if (keySerializer instanceof SomeSerializer) {
                    return (KafkaProducer<K, V>) producerWithDeserializedKey;
                }

                return (KafkaProducer<K, V>) producerWithDeserializedValue;
            }
        };

        when(consumerWithDeserializedKeyAndValue.poll(any()))
            .thenReturn(new ConsumerRecords<>(Map.of(TOPIC_PARTITION,
                of(new ConsumerRecord<>("testTopic", 1,
                        1L,
                        new Date().getTime(),
                        LOG_APPEND_TIME,
                        0,
                        0,
                        new DraftDto().setName("Some Key"),
                        new DraftDto().setName("Some Value"),
                        new RecordHeaders()
                            .add(new RecordHeader("header1", "value1".getBytes()))
                            .add(new RecordHeader("header1", "value2".getBytes()))
                            .add(new RecordHeader("header2", "value1".getBytes())),
                        Optional.of(5)),
                    new ConsumerRecord<>("testTopic", 1,
                        1L,
                        new Date().getTime(),
                        LOG_APPEND_TIME,
                        0,
                        0,
                        new DraftDto().setName("Some Key2"),
                        new DraftDto().setName("Some Value2"),
                        new RecordHeaders()
                            .add(new RecordHeader("header1", "value1".getBytes()))
                            .add(new RecordHeader("header1", "value2".getBytes()))
                            .add(new RecordHeader("header2", "value1".getBytes())),
                        Optional.of(5)),
                    new ConsumerRecord<>("testTopic", 1,
                        1L,
                        new Date().getTime(),
                        LOG_APPEND_TIME,
                        0,
                        0,
                        null,
                        null,
                        new RecordHeaders()
                            .add(new RecordHeader("header1", "value1".getBytes()))
                            .add(new RecordHeader("header1", "value2".getBytes()))
                            .add(new RecordHeader("header2", "value1".getBytes())),
                        Optional.of(5))))));

        when(consumerWithDeserializedKey.poll(any()))
            .thenReturn(new ConsumerRecords<>(Map.of(TOPIC_PARTITION,
                of(new ConsumerRecord<>("testTopic", 1,
                        1L,
                        new Date().getTime(),
                        LOG_APPEND_TIME,
                        0,
                        0,
                        new DraftDto().setName("Some Key"),
                        "Some String Value 1",
                        new RecordHeaders()
                            .add(new RecordHeader("header1", "value1".getBytes()))
                            .add(new RecordHeader("header1", "value2".getBytes()))
                            .add(new RecordHeader("header2", "value1".getBytes())),
                        Optional.of(5)),
                    new ConsumerRecord<>("testTopic", 1,
                        1L,
                        new Date().getTime(),
                        LOG_APPEND_TIME,
                        0,
                        0,
                        new DraftDto().setName("Some Key2"),
                        "Some String Value 2",
                        new RecordHeaders()
                            .add(new RecordHeader("header1", "value1".getBytes()))
                            .add(new RecordHeader("header1", "value2".getBytes()))
                            .add(new RecordHeader("header2", "value1".getBytes())),
                        Optional.of(5)),
                    new ConsumerRecord<>("testTopic", 1,
                        1L,
                        new Date().getTime(),
                        LOG_APPEND_TIME,
                        0,
                        0,
                        null,
                        null,
                        new RecordHeaders()
                            .add(new RecordHeader("header1", "value1".getBytes()))
                            .add(new RecordHeader("header1", "value2".getBytes()))
                            .add(new RecordHeader("header2", "value1".getBytes())),
                        Optional.of(5))))));

        when(consumerWithDeserializedValue.poll(any()))
            .thenReturn(new ConsumerRecords<>(Map.of(TOPIC_PARTITION,
                of(new ConsumerRecord<>("testTopic", 1,
                        1L,
                        new Date().getTime(),
                        LOG_APPEND_TIME,
                        0,
                        0,
                        "Some String Key 1",
                        new DraftDto().setName("Some Value"),
                        new RecordHeaders()
                            .add(new RecordHeader("header1", "value1".getBytes()))
                            .add(new RecordHeader("header1", "value2".getBytes()))
                            .add(new RecordHeader("header2", "value1".getBytes())),
                        Optional.of(5)),
                    new ConsumerRecord<>("testTopic", 1,
                        1L,
                        new Date().getTime(),
                        LOG_APPEND_TIME,
                        0,
                        0,
                        "Some String Key 2",
                        new DraftDto().setName("Some Value2"),
                        new RecordHeaders()
                            .add(new RecordHeader("header1", "value1".getBytes()))
                            .add(new RecordHeader("header1", "value2".getBytes()))
                            .add(new RecordHeader("header2", "value1".getBytes())),
                        Optional.of(5)),
                    new ConsumerRecord<>("testTopic", 1,
                        1L,
                        new Date().getTime(),
                        LOG_APPEND_TIME,
                        0,
                        0,
                        null,
                        null,
                        new RecordHeaders()
                            .add(new RecordHeader("header1", "value1".getBytes()))
                            .add(new RecordHeader("header1", "value2".getBytes()))
                            .add(new RecordHeader("header2", "value1".getBytes())),
                        Optional.of(5))))));

        when(consumerRaw.poll(any()))
            .thenReturn(RAW_CONSUMER_RECORDS);
    }

    @BeforeMethod
    public void prepareDataTransformer() {
        DEFAULT_TOPICS_FOR_POLL.accept(null);
        DEFAULT_TOPIC_FOR_SEND.accept(null);
        KAFKA_CALLBACK.accept(null);

        clearInvocations(consumerWithDeserializedKeyAndValue,
            consumerWithDeserializedKey,
            consumerWithDeserializedValue,
            consumerRaw,
            producerWithDeserializedKey,
            producerWithDeserializedKey,
            producerWithDeserializedValue,
            producerRaw);
    }
}
