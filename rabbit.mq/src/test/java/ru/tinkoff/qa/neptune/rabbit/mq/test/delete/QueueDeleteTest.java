package ru.tinkoff.qa.neptune.rabbit.mq.test.delete;

import org.testng.annotations.Test;
import ru.tinkoff.qa.neptune.rabbit.mq.test.BaseRabbitMqTest;

import java.io.IOException;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static ru.tinkoff.qa.neptune.rabbit.mq.function.delete.queue.ParametersForDelete.deleteParams;

public class QueueDeleteTest extends BaseRabbitMqTest {

    @Test(description = "Check delete queue without flag 'ifUnuser'")
    public void deleteTest1() throws IOException {
        rabbitMqStepContext.deleteQueue("queue1");
        verify(channel, times(1)).queueDelete("queue1");
    }

    @Test(description = "Check delete queue without flag empty = true")
    public void deleteTest2() throws IOException {
        rabbitMqStepContext.deleteQueue("queue", deleteParams().empty());
        verify(channel, times(1)).queueDelete("queue", false, true);
    }

    @Test(description = "Check delete queue without flag ifUnuser = true")
    public void deleteTest3() throws IOException {
        rabbitMqStepContext.deleteQueue("queue", deleteParams().unused());
        verify(channel, times(1)).queueDelete("queue", true, false);
    }
}
