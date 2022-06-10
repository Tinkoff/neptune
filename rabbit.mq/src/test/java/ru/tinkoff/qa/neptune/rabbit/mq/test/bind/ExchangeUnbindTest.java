package ru.tinkoff.qa.neptune.rabbit.mq.test.bind;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import ru.tinkoff.qa.neptune.rabbit.mq.BaseRabbitMqPreparations;

import java.util.Map;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static ru.tinkoff.qa.neptune.rabbit.mq.function.binding.ExchangesBindUnbindParameters.*;
import static ru.tinkoff.qa.neptune.rabbit.mq.properties.RabbitMQRoutingProperties.DEFAULT_EXCHANGE_NAME;
import static ru.tinkoff.qa.neptune.rabbit.mq.properties.RabbitMQRoutingProperties.DEFAULT_ROUTING_KEY_NAME;

public class ExchangeUnbindTest extends BaseRabbitMqPreparations {

    @AfterMethod
    @BeforeMethod
    public void clear() {
        DEFAULT_EXCHANGE_NAME.accept(null);
        DEFAULT_ROUTING_KEY_NAME.accept(null);
    }

    @Test(description = "Check unbind exchange from exchange")
    public void unbindTest1() throws Exception {
        rabbitMqStepContext.unbind(exchanges(
                "source",
                "destination")
                .withRoutingKey("routingKey"));

        verify(channel, times(1)).exchangeUnbind(
                "destination",
                "source",
                "routingKey");
    }

    @Test(description = "Check unbind exchange from exchange with arguments")
    public void unbindTest2() throws Exception {
        rabbitMqStepContext.unbind(exchanges(
                "source",
                "destination")
                .withRoutingKey("routingKey")
                .argument("key", "value"));

        verify(channel, times(1)).exchangeUnbind(
                "destination",
                "source",
                "routingKey",
                Map.of("key", "value"));
    }


    @Test(description = "Check unbind exchange to exchange without additional arguments and routing key")
    public void unbindTest3() throws Exception {
        rabbitMqStepContext.unbind(exchanges(
                "source",
                "destination"));

        verify(channel, times(1))
                .exchangeUnbind("destination", "source", "");
    }

    @Test(description = "Check unbind source exchange to default named exchange")
    public void unbindTest4() throws Exception {
        DEFAULT_EXCHANGE_NAME.accept("destination_exchange");
        rabbitMqStepContext.unbind(sourceExchange("source"));

        verify(channel, times(1))
                .exchangeUnbind("destination_exchange", "source", "");
    }

    @Test(description = "Check unbind destination exchange to default named exchange")
    public void unbindTest5() throws Exception {
        DEFAULT_EXCHANGE_NAME.accept("source_exchange");
        rabbitMqStepContext.unbind(destinationExchange("destination"));

        verify(channel, times(1))
                .exchangeUnbind("destination", "source_exchange", "");
    }

    @Test(description = "Check unbind exchanges with default named routing key")
    public void unbindTest6() throws Exception {
        DEFAULT_ROUTING_KEY_NAME.accept("default.routing");
        rabbitMqStepContext.unbind(exchanges(
                "source",
                "destination")
                .withDefaultRoutingKey());

        verify(channel, times(1))
                .exchangeUnbind("destination", "source", "default.routing");
    }
}
