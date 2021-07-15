package ru.tinkoff.qa.neptune.rabbit.mq.test.delete;

import org.testng.annotations.Test;
import ru.tinkoff.qa.neptune.rabbit.mq.test.BaseRabbitMqTest;

import java.io.IOException;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class ExchangeDeleteTest extends BaseRabbitMqTest {
    @Test
    public void deleteTest1() throws IOException {
        rabbitMqStepContext.exchangeDelete("exchange1");
        verify(channel, times(1)).exchangeDelete("exchange1", false);
    }

    @Test
    public void deleteTest2() throws IOException {
        rabbitMqStepContext.exchangeDelete("exchange2", false);
        verify(channel, times(1)).exchangeDelete("exchange2", false);
    }

    @Test
    public void deleteTest3() throws IOException {
        rabbitMqStepContext.exchangeDelete("exchange3", true);
        verify(channel, times(1)).exchangeDelete("exchange3", true);
    }
}
