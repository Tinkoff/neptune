package ru.tinkoff.qa.neptune.rabbit.mq.test.bind;

import com.rabbitmq.client.impl.AMQImpl;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import ru.tinkoff.qa.neptune.rabbit.mq.test.BaseRabbitMqTest;

import java.io.IOException;

import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;
import static ru.tinkoff.qa.neptune.rabbit.mq.AdditionalArguments.arguments;

public class QueueBindTest extends BaseRabbitMqTest {
    AMQImpl.Queue.BindOk bindOk1;
    AMQImpl.Queue.BindOk bindOk2;

    @BeforeClass(dependsOnMethods = "setUp")
    public void configureMock() throws IOException {
        when(channel.queueBind("queue", "exchange", "routingKey"))
                .thenReturn(bindOk1 = new AMQImpl.Queue.BindOk());

        when(channel.queueBind("queue", "exchange", "routingKey", arguments().getHashMap()))
                .thenReturn(bindOk2 = new AMQImpl.Queue.BindOk());
    }

    @Test
    public void bindTest1() {
        assertEquals(bindOk1, rabbitMqStepContext.queueBind("queue", "exchange", "routingKey"));
    }

    @Test
    public void bindTest2() {
        assertEquals(bindOk2, rabbitMqStepContext.queueBind("queue", "exchange", "routingKey", arguments()));
    }
}
