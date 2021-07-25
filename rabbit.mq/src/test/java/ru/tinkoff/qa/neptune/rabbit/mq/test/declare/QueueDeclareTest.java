package ru.tinkoff.qa.neptune.rabbit.mq.test.declare;

import com.rabbitmq.client.impl.AMQImpl;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import ru.tinkoff.qa.neptune.rabbit.mq.test.BaseRabbitMqTest;

import java.io.IOException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;
import static ru.tinkoff.qa.neptune.rabbit.mq.function.declare.queue.ParametersForDeclareQueue.queueParams;

public class QueueDeclareTest extends BaseRabbitMqTest {
    AMQImpl.Queue.DeclareOk declareOk;

    @BeforeClass(dependsOnMethods = "setUp")
    public void configureMock() throws IOException {
        when(channel.queueDeclare())
                .thenReturn(new AMQImpl.Queue.DeclareOk("test1", 1, 1));
        when(channel.queueDeclarePassive("passive"))
                .thenReturn(new AMQImpl.Queue.DeclareOk("test2", 1, 1));
        when(channel.queueDeclare("queue", true, true, false, null))
                .thenReturn(new AMQImpl.Queue.DeclareOk("test3", 1, 1));
        when(channel.queueDeclare("queue", true, true, false, queueParams().argument("name", "value").getAdditionalArguments()))
                .thenReturn(declareOk = new AMQImpl.Queue.DeclareOk("test3", 1, 1));
    }

    @Test(description = "Check declare a server-named exclusive, autodelete, non-durable queue.")
    public void declareTest1() {
        assertThat(rabbitMqStepContext.queueDeclare(),
                is(new AMQImpl.Queue.DeclareOk("test1", 1, 1)));
    }

    @Test(description = "Check passive declare queue.")
    public void declareTest2() {
        assertThat(rabbitMqStepContext.queueDeclare("passive"),
                is(new AMQImpl.Queue.DeclareOk("test2", 1, 1)));
    }

    @Test(description = "Check declare queue with params")
    public void declareTest3() {
        assertThat(rabbitMqStepContext.queueDeclare("queue", queueParams().durable().exclusive()),
                is(new AMQImpl.Queue.DeclareOk("test3", 1, 1)));
    }

    @Test(description = "Check declare queue with params and additional arguments")
    public void declareTest4() {
        assertEquals(declareOk, rabbitMqStepContext.queueDeclare("queue", queueParams().durable().exclusive().argument("name", "value")));
    }
}
