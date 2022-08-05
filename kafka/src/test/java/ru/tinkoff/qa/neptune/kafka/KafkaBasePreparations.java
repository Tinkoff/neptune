package ru.tinkoff.qa.neptune.kafka;

import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.mockito.Mock;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;

import static org.mockito.MockitoAnnotations.openMocks;
import static ru.tinkoff.qa.neptune.kafka.properties.DefaultDataTransformers.KAFKA_DEFAULT_DATA_TRANSFORMER;
import static ru.tinkoff.qa.neptune.kafka.properties.DefaultDataTransformers.KAFKA_KEY_TRANSFORMER;
import static ru.tinkoff.qa.neptune.kafka.properties.KafkaDefaultTopicsForPollProperty.DEFAULT_TOPICS_FOR_POLL;

@SuppressWarnings({"rawtypes", "unchecked"})
public class KafkaBasePreparations {
    @Mock
    protected KafkaProducer kafkaProducer;
    @Mock
    protected KafkaConsumer kafkaConsumer;

    protected KafkaStepContext kafka;

    @BeforeClass
    public void setUp() {
        openMocks(this);
        kafka = new KafkaStepContext() {

            @Override
            KafkaConsumer<String, String> createConsumer() {
                return kafkaConsumer;
            }

            @Override
            public KafkaProducer<String, String> createProducer() {
                return kafkaProducer;
            }
        };
    }

    @BeforeMethod
    public void prepareDataTransformer() {
        DEFAULT_TOPICS_FOR_POLL.accept(null);
        KAFKA_DEFAULT_DATA_TRANSFORMER.accept(DefaultMapper.class);
        KAFKA_KEY_TRANSFORMER.accept(DefaultMapper.class);
    }
}
