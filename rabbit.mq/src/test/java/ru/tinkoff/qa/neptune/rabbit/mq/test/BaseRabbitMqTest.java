package ru.tinkoff.qa.neptune.rabbit.mq.test;

import com.rabbitmq.client.Channel;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import ru.tinkoff.qa.neptune.rabbit.mq.RabbitMqParameterProvider;
import ru.tinkoff.qa.neptune.rabbit.mq.RabbitMqStepContext;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static ru.tinkoff.qa.neptune.rabbit.mq.properties.RabbitMqClusterProperty.RABBIT_MQ_CLUSTER_PROPERTY;
import static ru.tinkoff.qa.neptune.rabbit.mq.properties.RabbitMqDefaultDataTransformer.RABBIT_MQ_DEFAULT_DATA_TRANSFORMER;

public class BaseRabbitMqTest {
    @Mock
    protected Channel channel;
    protected RabbitMqStepContext rabbitMqStepContext;
    MockedStatic<RabbitMqParameterProvider> provider;

    @BeforeClass
    public void setUp() throws IOException, TimeoutException {
        RABBIT_MQ_CLUSTER_PROPERTY.accept("localhost:5150");
        RABBIT_MQ_DEFAULT_DATA_TRANSFORMER.accept(DefaultMapper.class);

        channel = mock(Channel.class);

        provider = mockStatic(RabbitMqParameterProvider.class);
        provider.when(RabbitMqParameterProvider::channel).thenReturn(new Object[]{channel});

        rabbitMqStepContext = new RabbitMqStepContext(channel);
    }

    @AfterClass
    public void remove() {
        provider.close();
    }
}
