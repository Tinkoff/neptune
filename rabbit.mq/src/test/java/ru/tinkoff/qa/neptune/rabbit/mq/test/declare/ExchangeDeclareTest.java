package ru.tinkoff.qa.neptune.rabbit.mq.test.declare;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import ru.tinkoff.qa.neptune.rabbit.mq.BaseRabbitMqPreparations;

import java.io.IOException;
import java.util.Map;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static ru.tinkoff.qa.neptune.rabbit.mq.function.declare.DeclareExchangeParameters.newExchange;
import static ru.tinkoff.qa.neptune.rabbit.mq.properties.RabbitMQRoutingProperties.DEFAULT_EXCHANGE_NAME;

public class ExchangeDeclareTest extends BaseRabbitMqPreparations {

    @AfterMethod
    @BeforeMethod
    public void clear() {
        DEFAULT_EXCHANGE_NAME.accept(null);
    }

    @Test(description = "Check passive declare exchange")
    public void declareTest1() throws IOException {
        rabbitMqStepContext.declare(newExchange("passive")
                .argument("key", "value")
                .passive());
        verify(channel, times(1)).exchangeDeclarePassive("passive");
    }

    @Test(description = "Check declare exchange with no parameters")
    public void declareTest2() throws Exception {
        rabbitMqStepContext.declare(newExchange("exchange"));
        verify(channel, times(1)).exchangeDeclare(
                "exchange",
                "direct",
                false,
                false,
                false,
                null);
    }

    @Test(description = "Check declare exchange with full set of params")
    public void declareTest3() throws Exception {
        rabbitMqStepContext.declare(newExchange("exchange")
                .type("type")
                .durable()
                .autoDelete()
                .internal()
                .argument("key", "value"));

        verify(channel, times(1)).exchangeDeclare(
                "exchange",
                "type",
                true,
                true,
                true,
                Map.of("key", "value"));
    }

    @Test(description = "Check declare exchange with default name")
    public void declareTest4() throws Exception {
        DEFAULT_EXCHANGE_NAME.accept("default_exchange");
        rabbitMqStepContext.declare(newExchange());
        verify(channel, times(1)).exchangeDeclare(
                "default_exchange",
                "direct",
                false,
                false,
                false,
                null);
    }
}
