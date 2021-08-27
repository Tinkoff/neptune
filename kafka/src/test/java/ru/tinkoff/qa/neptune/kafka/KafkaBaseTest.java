package ru.tinkoff.qa.neptune.kafka;

import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static ru.tinkoff.qa.neptune.kafka.properties.DefaultDataTransformers.KAFKA_DEFAULT_DATA_TRANSFORMER;
import static ru.tinkoff.qa.neptune.kafka.properties.DefaultDataTransformers.KAFKA_KEY_TRANSFORMER;
import static ru.tinkoff.qa.neptune.kafka.properties.KafkaDefaultTopicsForPollProperty.DEFAULT_TOPICS_FOR_POLL;

public class KafkaBaseTest {
    @Mock
    protected KafkaProducer kafkaProducer;
    @Mock
    protected KafkaConsumer kafkaConsumer;

    protected KafkaStepContext kafka;
    MockedStatic<KafkaParameterProvider> provider;

    @BeforeClass
    public void setUp() {
        kafkaProducer = mock(KafkaProducer.class);
        kafkaConsumer = mock(KafkaConsumer.class);

        provider = mockStatic(KafkaParameterProvider.class);
        provider.when(KafkaParameterProvider::parameters).thenReturn(new Object[]{kafkaProducer, kafkaConsumer});

        kafka = new KafkaStepContext(kafkaProducer, kafkaConsumer);
    }

    @BeforeMethod
    public void prepareDataTransformer() {
        DEFAULT_TOPICS_FOR_POLL.accept(null);
        KAFKA_DEFAULT_DATA_TRANSFORMER.accept(DefaultMapper.class);
        KAFKA_KEY_TRANSFORMER.accept(DefaultMapper.class);
    }

    @AfterClass
    public void remove() {
        provider.close();
    }

}
