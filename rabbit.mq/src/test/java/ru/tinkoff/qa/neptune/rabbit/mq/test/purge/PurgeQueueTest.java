package ru.tinkoff.qa.neptune.rabbit.mq.test.purge;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import ru.tinkoff.qa.neptune.rabbit.mq.test.BaseRabbitMqTest;

import java.io.IOException;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static ru.tinkoff.qa.neptune.rabbit.mq.properties.RabbitMQRoutingProperties.DEFAULT_QUEUE_NAME;

public class PurgeQueueTest extends BaseRabbitMqTest {

    @AfterMethod
    @BeforeMethod
    public void clear() {
        DEFAULT_QUEUE_NAME.accept(null);
    }

    @Test
    public void purgeTest() throws IOException {
        rabbitMqStepContext.purgeQueue("queue1");
        verify(channel, times(1)).queuePurge("queue1");
    }

    @Test
    public void purgeTest2() throws IOException {
        DEFAULT_QUEUE_NAME.accept("default_queue");
        rabbitMqStepContext.purgeQueue();
        verify(channel, times(1)).queuePurge("default_queue");
    }
}
