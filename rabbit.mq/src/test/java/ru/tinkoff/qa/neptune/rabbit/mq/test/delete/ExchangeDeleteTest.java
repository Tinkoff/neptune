package ru.tinkoff.qa.neptune.rabbit.mq.test.delete;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import ru.tinkoff.qa.neptune.rabbit.mq.test.BaseRabbitMqTest;

import java.io.IOException;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static ru.tinkoff.qa.neptune.rabbit.mq.function.delete.ExchangeDeleteParameters.exchange;
import static ru.tinkoff.qa.neptune.rabbit.mq.properties.RabbitMQRoutingProperties.DEFAULT_EXCHANGE_NAME;

public class ExchangeDeleteTest extends BaseRabbitMqTest {

    @AfterMethod
    @BeforeMethod
    public void clear() {
        DEFAULT_EXCHANGE_NAME.accept(null);
    }

    @Test(description = "Check delete exchange without flag 'ifUnuser'")
    public void deleteTest1() throws IOException {
        rabbitMqStepContext.delete(exchange("exchange1"));
        verify(channel, times(1)).exchangeDelete("exchange1", false);
    }

    @Test(description = "Check delete exchange with flag ifUnuser = true")
    public void deleteTest2() throws IOException {
        rabbitMqStepContext.delete(exchange(
                "exchange2")
                .ifUnused());

        verify(channel, times(1)).exchangeDelete("exchange2", true);
    }

    @Test(description = "Check delete default named exchange")
    public void deleteTest3() throws IOException {
        DEFAULT_EXCHANGE_NAME.accept("default_exchange");
        rabbitMqStepContext.delete(exchange());
        verify(channel, times(1)).exchangeDelete("default_exchange", false);
    }
}
