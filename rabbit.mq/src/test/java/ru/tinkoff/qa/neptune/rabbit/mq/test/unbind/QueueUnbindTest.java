package ru.tinkoff.qa.neptune.rabbit.mq.test.unbind;

import com.rabbitmq.client.impl.AMQImpl;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import ru.tinkoff.qa.neptune.rabbit.mq.test.BaseRabbitMqTest;

import java.io.IOException;

import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;
import static ru.tinkoff.qa.neptune.rabbit.mq.AdditionalArguments.arguments;

public class QueueUnbindTest extends BaseRabbitMqTest {
    AMQImpl.Queue.UnbindOk unbindOk1;
    AMQImpl.Queue.UnbindOk unbindOk2;
    AMQImpl.Queue.UnbindOk unbindOk3;

    @BeforeClass(dependsOnMethods = "setUp")
    public void configureMock() throws IOException {
        when(channel.queueUnbind("queue", "exchange", "routingKey"))
                .thenReturn(unbindOk1 = new AMQImpl.Queue.UnbindOk());

        when(channel.queueUnbind("queue", "exchange", "routingKey", arguments().getHashMap()))
                .thenReturn(unbindOk2 = new AMQImpl.Queue.UnbindOk());

        when(channel.queueUnbind("queue", "exchange", "routingKey", arguments().setArgument("key", "value").getHashMap()))
                .thenReturn(unbindOk3 = new AMQImpl.Queue.UnbindOk());
    }

    @Test(description = "Check unbind queue from exchange")
    public void bindTest1() {
        assertEquals(unbindOk1, rabbitMqStepContext.queueUnbind("queue", "exchange", "routingKey"));
    }

    @Test(description = "Check unbind queue from exchange with empty arguments")
    public void bindTest2() {
        assertEquals(unbindOk2, rabbitMqStepContext.queueUnbind("queue", "exchange", "routingKey", arguments()));
    }

    @Test(description = "Check unbind queue from exchange with arguments")
    public void bindTest3() {
        assertEquals(unbindOk3, rabbitMqStepContext.queueUnbind("queue", "exchange", "routingKey", arguments().setArgument("key", "value")));
    }
}
