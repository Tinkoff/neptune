package ru.tinkoff.qa.neptune.rabbit.mq.test.declare;

import org.testng.annotations.Test;
import ru.tinkoff.qa.neptune.rabbit.mq.test.BaseRabbitMqTest;

import java.util.Map;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static ru.tinkoff.qa.neptune.rabbit.mq.function.declare.queue.ParametersForDeclareQueue.queueParams;

public class QueueDeclareTest extends BaseRabbitMqTest {

    @Test(description = "Check declare a server-named exclusive, autodelete, non-durable queue.")
    public void declareTest1() throws Exception {
        rabbitMqStepContext.queueDeclare();
        verify(channel, times(1)).queueDeclare();
    }

    @Test(description = "Check passive declare queue.")
    public void declareTest2() throws Exception {
        rabbitMqStepContext.queueDeclare("passive");
        verify(channel, times(1)).queueDeclarePassive("passive");
    }

    @Test(description = "Check declare queue with params")
    public void declareTest3() throws Exception {
        rabbitMqStepContext.queueDeclare("queue", queueParams()
                .durable()
                .exclusive()
                .autoDelete());

        verify(channel, times(1))
                .queueDeclare("queue",
                        true,
                        true,
                        true, null);
    }

    @Test(description = "Check declare queue with params and additional arguments")
    public void declareTest4() throws Exception {
        rabbitMqStepContext.queueDeclare("queue",
                queueParams().argument("name", "value"));

        verify(channel, times(1))
                .queueDeclare("queue",
                        false,
                        false,
                        false,
                        Map.of("name", "value"));
    }
}
