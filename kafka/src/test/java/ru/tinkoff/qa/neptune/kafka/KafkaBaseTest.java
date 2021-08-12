package ru.tinkoff.qa.neptune.kafka;

import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static ru.tinkoff.qa.neptune.kafka.properties.KafkaDefaultDataTransformer.KAFKA_DEFAULT_DATA_TRANSFORMER;

public class KafkaBaseTest {
    @Mock
    protected KafkaProducer kafkaProducer;
    @Mock
    protected KafkaConsumer kafkaConsumer;

    protected KafkaStepContext kafka;
    protected Callback callBack;
    MockedStatic<KafkaParameterProvider> provider;

    @BeforeClass
    public void setUp() {
        kafkaProducer = mock(KafkaProducer.class);
        kafkaConsumer = mock(KafkaConsumer.class);

        KAFKA_DEFAULT_DATA_TRANSFORMER.accept(DefaultMapper.class);

        provider = mockStatic(KafkaParameterProvider.class);
        provider.when(KafkaParameterProvider::parameters).thenReturn(new Object[]{kafkaProducer, kafkaConsumer});

        kafka = new KafkaStepContext(kafkaProducer, kafkaConsumer);

        callBack = (metadata, exception) -> {
        };
    }

    @AfterClass
    public void remove() {
        provider.close();
    }

}
