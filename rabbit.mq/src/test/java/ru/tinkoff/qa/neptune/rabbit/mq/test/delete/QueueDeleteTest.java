package ru.tinkoff.qa.neptune.rabbit.mq.test.delete;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import ru.tinkoff.qa.neptune.rabbit.mq.test.BaseRabbitMqTest;

import java.io.IOException;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static ru.tinkoff.qa.neptune.rabbit.mq.function.delete.QueueDeleteParameters.queue;
import static ru.tinkoff.qa.neptune.rabbit.mq.properties.RabbitMQRoutingProperties.DEFAULT_QUEUE_NAME;

public class QueueDeleteTest extends BaseRabbitMqTest {

    @AfterMethod
    @BeforeMethod
    public void clear() {
        DEFAULT_QUEUE_NAME.accept(null);
    }

    @Test(description = "Check delete queue without flag 'ifUnuser'")
    public void deleteTest1() throws IOException {
        rabbitMqStepContext.delete(queue("queue1"));
        verify(channel, times(1)).queueDelete("queue1");
    }

    @Test(description = "Check delete queue without flag empty = true")
    public void deleteTest2() throws IOException {
        rabbitMqStepContext.delete(queue(
                "queue")
                .ifEmpty());
        verify(channel, times(1)).queueDelete("queue", false, true);
    }

    @Test(description = "Check delete queue without flag ifUnuser = true")
    public void deleteTest3() throws IOException {
        rabbitMqStepContext.delete(queue(
                "queue")
                .ifUnused());
        verify(channel, times(1)).queueDelete("queue", true, false);
    }

    @Test(description = "Check delete default named queue")
    public void deleteTest4() throws IOException {
        DEFAULT_QUEUE_NAME.accept("default_queue_name");
        rabbitMqStepContext.delete(queue());
        verify(channel, times(1)).queueDelete("default_queue_name");
    }
}
