package ru.tinkoff.qa.neptune.rabbit.mq;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import org.mockito.Mock;
import org.testng.annotations.BeforeClass;
import ru.tinkoff.qa.neptune.rabbit.mq.test.DefaultMapper;

import java.io.IOException;

import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;
import static ru.tinkoff.qa.neptune.rabbit.mq.properties.RabbitMqDefaultDataTransformer.RABBIT_MQ_DEFAULT_DATA_TRANSFORMER;

public class BaseRabbitMqPreparations {

    @Mock
    protected Connection connection;

    @Mock
    protected Channel channel;

    protected RabbitMqStepContext rabbitMqStepContext;

    @BeforeClass
    public void setUp() throws IOException {
        RABBIT_MQ_DEFAULT_DATA_TRANSFORMER.accept(DefaultMapper.class);

        openMocks(this);
        when(connection.createChannel()).thenReturn(channel);

        rabbitMqStepContext = new RabbitMqStepContext() {

            @Override
            Connection createConnection() {
                return connection;
            }
        };
    }
}
