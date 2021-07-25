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
    AMQImpl.Queue.BindOk bindOk3;

    @BeforeClass(dependsOnMethods = "setUp")
    public void configureMock() throws IOException {
        when(channel.queueBind("queue", "exchange", "routingKey"))
                .thenReturn(bindOk1 = new AMQImpl.Queue.BindOk());

        when(channel.queueBind("queue", "exchange", "routingKey", arguments().getHashMap()))
                .thenReturn(bindOk2 = new AMQImpl.Queue.BindOk());

        when(channel.queueBind("queue", "exchange", "routingKey",
                arguments()
                        .setArgument("testKey", "testValue").
                        getHashMap()))
                .thenReturn(bindOk3 = new AMQImpl.Queue.BindOk());
    }

    @Test(description = "Check bind a queue with exchange without additional arguments")
    public void bindTest1() {
        assertEquals(bindOk1, rabbitMqStepContext.queueBind("queue", "exchange", "routingKey"));
    }

    @Test(description = "Check bind a queue to exchange with empty additional arguments")
    public void bindTest2() {
        assertEquals(bindOk2, rabbitMqStepContext.queueBind("queue", "exchange", "routingKey", arguments()));
    }

    @Test(description = "Check bind a queue to exchange with additional arguments")
    public void bindTest3() {
        assertEquals(bindOk3, rabbitMqStepContext.queueBind("queue", "exchange", "routingKey", arguments().setArgument("testKey", "testValue")));
    }
}
