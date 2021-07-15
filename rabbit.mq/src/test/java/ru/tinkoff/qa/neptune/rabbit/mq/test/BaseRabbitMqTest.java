package ru.tinkoff.qa.neptune.rabbit.mq.test;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.mockito.Mock;
import org.testng.annotations.BeforeClass;
import ru.tinkoff.qa.neptune.rabbit.mq.RabbitMqStepContext;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;
import static ru.tinkoff.qa.neptune.rabbit.mq.properties.RabbitMqClusterProperty.RABBIT_MQ_CLUSTER_PROPERTY;
import static ru.tinkoff.qa.neptune.rabbit.mq.properties.RabbitMqDefaultMapper.RABBIT_MQ_DEFAULT_MAPPER;

public class BaseRabbitMqTest {
    @Mock
    protected Channel channel;
    @Mock
    protected Connection connection;
    @Mock
    protected ConnectionFactory factory;

    protected RabbitMqStepContext rabbitMqStepContext;

    @BeforeClass
    public void setUp() throws IOException, TimeoutException {
        RABBIT_MQ_CLUSTER_PROPERTY.accept("localhost:5150");
        RABBIT_MQ_DEFAULT_MAPPER.accept(DefaultMapper.class);

        openMocks(this);
        when(factory.newConnection(RABBIT_MQ_CLUSTER_PROPERTY.get())).thenReturn(connection);
        when(connection.createChannel()).thenReturn(channel);

        rabbitMqStepContext = new RabbitMqStepContext(channel);
    }
}
