package ru.tinkoff.qa.neptune.rabbit.mq.test.bind;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import ru.tinkoff.qa.neptune.rabbit.mq.test.BaseRabbitMqTest;

import java.util.Map;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static ru.tinkoff.qa.neptune.rabbit.mq.function.binding.ExchangesBindUnbindParameters.*;
import static ru.tinkoff.qa.neptune.rabbit.mq.properties.RabbitMQRoutingProperties.DEFAULT_EXCHANGE_NAME;
import static ru.tinkoff.qa.neptune.rabbit.mq.properties.RabbitMQRoutingProperties.DEFAULT_ROUTING_KEY_NAME;

public class ExchangeBindTest extends BaseRabbitMqTest {

    @AfterMethod
    @BeforeMethod
    public void clear() {
        DEFAULT_EXCHANGE_NAME.accept(null);
        DEFAULT_ROUTING_KEY_NAME.accept(null);
    }

    @Test(description = "Check bind exchange to exchange without additional arguments")
    public void bindTest1() throws Exception {
        rabbitMqStepContext.bind(exchanges(
                "source",
                "destination")
                .withRoutingKey("routingKey"));

        verify(channel, times(1))
                .exchangeBind("destination", "source", "routingKey");
    }


    @Test(description = "Check bind exchange to exchange with additional arguments")
    public void bindTest2() throws Exception {
        rabbitMqStepContext.bind(exchanges(
                "source",
                "destination")
                .withRoutingKey("routingKey")
                .argument("key", "value"));

        verify(channel, times(1)).exchangeBind(
                "destination",
                "source",
                "routingKey",
                Map.of("key", "value"));
    }

    @Test(description = "Check bind exchange to exchange without additional arguments and routing key")
    public void bindTest3() throws Exception {
        rabbitMqStepContext.bind(exchanges(
                "source",
                "destination"));

        verify(channel, times(1))
                .exchangeBind("destination", "source", "");
    }

    @Test(description = "Check bind source exchange to default named exchange")
    public void bindTest4() throws Exception {
        DEFAULT_EXCHANGE_NAME.accept("destination_exchange");
        rabbitMqStepContext.bind(sourceExchange("source"));

        verify(channel, times(1))
                .exchangeBind("destination_exchange", "source", "");
    }

    @Test(description = "Check bind destination exchange to default named exchange")
    public void bindTest5() throws Exception {
        DEFAULT_EXCHANGE_NAME.accept("source_exchange");
        rabbitMqStepContext.bind(destinationExchange("destination"));

        verify(channel, times(1))
                .exchangeBind("destination", "source_exchange", "");
    }

    @Test(description = "Check bind exchanges with default named routing key")
    public void bindTest6() throws Exception {
        DEFAULT_ROUTING_KEY_NAME.accept("default.routing");
        rabbitMqStepContext.bind(exchanges(
                "source",
                "destination")
                .withDefaultRoutingKey());

        verify(channel, times(1))
                .exchangeBind("destination", "source", "default.routing");
    }
}
