package ru.tinkoff.qa.neptune.rabbit.mq.test.purge;

import org.testng.annotations.Test;
import ru.tinkoff.qa.neptune.rabbit.mq.test.BaseRabbitMqTest;

import java.io.IOException;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class PurgeQueueTest extends BaseRabbitMqTest {

    @Test(description = "Check ")
    public void purgeTest() throws IOException {
        rabbitMqStepContext.purgeQueue("queue1");
        verify(channel, times(1)).queuePurge("queue1");
    }
}
