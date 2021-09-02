package ru.tinkoff.qa.neptune.rabbit.mq.test.bind;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import ru.tinkoff.qa.neptune.rabbit.mq.test.BaseRabbitMqTest;

import java.util.Map;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static ru.tinkoff.qa.neptune.rabbit.mq.function.binding.QueueBindUnbindParameters.*;
import static ru.tinkoff.qa.neptune.rabbit.mq.properties.RabbitMQRoutingProperties.*;

public class QueueBindTest extends BaseRabbitMqTest {

    @AfterMethod
    @BeforeMethod
    public void clear() {
        DEFAULT_EXCHANGE_NAME.accept(null);
        DEFAULT_QUEUE_NAME.accept(null);
        DEFAULT_ROUTING_KEY_NAME.accept(null);
    }

    @Test(description = "Check bind a queue with exchange without additional arguments")
    public void bindTest1() throws Exception {
        rabbitMqStepContext.bind(queueAndExchange(
                "queue",
                "exchange")
                .withRoutingKey("routingKey"));

        verify(channel, times(1))
                .queueBind("queue",
                        "exchange",
                        "routingKey");
    }


    @Test(description = "Check bind a queue to exchange with additional arguments")
    public void bindTest2() throws Exception {
        rabbitMqStepContext.bind(queueAndExchange(
                "queue",
                "exchange")
                .withRoutingKey("routingKey")
                .argument("testKey", "testValue"));

        verify(channel, times(1))
                .queueBind("queue",
                        "exchange",
                        "routingKey",
                        Map.of("testKey", "testValue"));
    }

    @Test(description = "Check bind a queue to exchange without additional arguments and routing key")
    public void bindTest3() throws Exception {
        rabbitMqStepContext.bind(queueAndExchange(
                "queue",
                "exchange"));

        verify(channel, times(1))
                .queueBind("queue",
                        "exchange",
                        "");
    }

    @Test(description = "Check bind queue to default named exchange")
    public void bindTest4() throws Exception {
        DEFAULT_EXCHANGE_NAME.accept("destination_exchange");
        rabbitMqStepContext.bind(queueAndDefaultExchange("queue"));

        verify(channel, times(1))
                .queueBind("queue",
                        "destination_exchange",
                        "");
    }

    @Test(description = "Check bind exchange exchange to default named queue")
    public void bindTest5() throws Exception {
        DEFAULT_QUEUE_NAME.accept("destination_queue");
        rabbitMqStepContext.bind(defaultQueueAndExchange("exchange"));

        verify(channel, times(1))
                .queueBind("destination_queue",
                        "exchange",
                        "");
    }

    @Test(description = "Check bind exchanges with default named routing key")
    public void bindTest6() throws Exception {
        DEFAULT_ROUTING_KEY_NAME.accept("default.routing");
        rabbitMqStepContext.bind(queueAndExchange(
                "queue",
                "exchange")
                .withDefaultRoutingKey());

        verify(channel, times(1))
                .queueBind("queue",
                        "exchange",
                        "default.routing");
    }
}
