package ru.tinkoff.qa.neptune.rabbit.mq.test.declare;

import com.rabbitmq.client.impl.AMQImpl;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import ru.tinkoff.qa.neptune.rabbit.mq.test.BaseRabbitMqTest;

import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import static ru.tinkoff.qa.neptune.rabbit.mq.function.declare.DeclareQueueParameters.newQueue;
import static ru.tinkoff.qa.neptune.rabbit.mq.function.declare.ServerNamedQueueSequentialGetSupplier.newQueueServerNamed;
import static ru.tinkoff.qa.neptune.rabbit.mq.properties.RabbitMQRoutingProperties.DEFAULT_QUEUE_NAME;

public class QueueDeclareTest extends BaseRabbitMqTest {

    @BeforeClass
    public void beforeClass() throws Exception {
        when(channel.queueDeclare())
                .thenReturn(new AMQImpl.Queue.DeclareOk("Server_Named_queue", 0, 0));
    }

    @AfterMethod
    @BeforeMethod
    public void clear() {
        DEFAULT_QUEUE_NAME.accept(null);
    }

    @Test(description = "Check declare a server-named exclusive, autodelete, non-durable queue.")
    public void declareTest1() {
        var queue = rabbitMqStepContext.declare(newQueueServerNamed());
        assertThat(queue, is("Server_Named_queue"));
    }

    @Test(description = "Check passive declare queue.")
    public void declareTest2() throws Exception {
        rabbitMqStepContext.declare(newQueue("passive")
                .argument("key", "value")
                .passive());

        verify(channel, times(1)).queueDeclarePassive("passive");
    }

    @Test(description = "Check declare queue with no parameters")
    public void declareTest3() throws Exception {
        rabbitMqStepContext.declare(newQueue("queue"));

        verify(channel, times(1))
                .queueDeclare("queue",
                        false,
                        false,
                        false, null);
    }

    @Test(description = "Check declare queue with full set of params")
    public void declareTest4() throws Exception {
        rabbitMqStepContext.declare(newQueue("queue")
                .argument("name", "value")
                .exclusive()
                .autoDelete()
                .durable());

        verify(channel, times(1))
                .queueDeclare("queue",
                        true,
                        true,
                        true,
                        Map.of("name", "value"));
    }

    @Test(description = "Check declare queue with default name")
    public void declareTest5() throws Exception {
        DEFAULT_QUEUE_NAME.accept("default_queue_name");
        rabbitMqStepContext.declare(newQueue());

        verify(channel, times(1))
                .queueDeclare("default_queue_name",
                        false,
                        false,
                        false, null);
    }
}
