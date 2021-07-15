package ru.tinkoff.qa.neptune.rabbit.mq.test.delete;

import org.testng.annotations.Test;
import ru.tinkoff.qa.neptune.rabbit.mq.test.BaseRabbitMqTest;

import java.io.IOException;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static ru.tinkoff.qa.neptune.rabbit.mq.function.delete.queue.ParametersForDelete.deleteParams;

public class QueueDeleteTest extends BaseRabbitMqTest {

    @Test
    public void deleteTest1() throws IOException {
        rabbitMqStepContext.deleteQueue("queue1");
        verify(channel, times(1)).queueDelete("queue1");
    }

    @Test
    public void deleteTest2() throws IOException {
        rabbitMqStepContext.deleteQueue("queue", deleteParams().empty());
        verify(channel, times(1)).queueDelete("queue", false, true);
    }
}
