package ru.tinkoff.qa.neptune.rabbit.mq.test.declare;

import org.testng.annotations.Test;
import ru.tinkoff.qa.neptune.rabbit.mq.test.BaseRabbitMqTest;

import java.io.IOException;
import java.util.Map;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static ru.tinkoff.qa.neptune.rabbit.mq.function.declare.exchange.ParametersForDeclareExchange.exchangeParams;

public class ExchangeDeclareTest extends BaseRabbitMqTest {

    @Test(description = "Check passive declare exchange")
    public void declareTest1() throws IOException {
        rabbitMqStepContext.exchangeDeclare("passive");
        verify(channel, times(1)).exchangeDeclarePassive("passive");
    }

    @Test(description = "Check declare exchange")
    public void declareTest2() throws Exception {
        rabbitMqStepContext.exchangeDeclare("exchange", "type");
        verify(channel, times(1)).exchangeDeclare(
                "exchange",
                "type",
                false,
                false,
                false,
                null);
    }

    @Test(description = "Check declare exchange with params")
    public void declareTest3() throws Exception {
        rabbitMqStepContext.exchangeDeclare(
                "exchange",
                "type",
                exchangeParams().durable().autoDelete().internal());

        verify(channel, times(1)).exchangeDeclare(
                "exchange",
                "type",
                true,
                true,
                true,
                null);
    }

    @Test(description = "Check declare exchange with params and additional arguments")
    public void declareTest4() throws IOException {
        rabbitMqStepContext.exchangeDeclare(
                "exchange",
                "type",
                exchangeParams().argument("name", "value"));

        verify(channel, times(1)).exchangeDeclare(
                "exchange",
                "type",
                false,
                false,
                false,
                Map.of("name", "value"));
    }
}
