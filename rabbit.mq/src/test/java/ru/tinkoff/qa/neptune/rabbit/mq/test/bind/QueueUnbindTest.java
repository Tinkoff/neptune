package ru.tinkoff.qa.neptune.rabbit.mq.test.bind;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import ru.tinkoff.qa.neptune.rabbit.mq.BaseRabbitMqPreparations;

import java.util.Map;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static ru.tinkoff.qa.neptune.rabbit.mq.function.binding.QueueBindUnbindParameters.*;
import static ru.tinkoff.qa.neptune.rabbit.mq.properties.RabbitMQRoutingProperties.*;

public class QueueUnbindTest extends BaseRabbitMqPreparations {

    @AfterMethod
    @BeforeMethod
    public void clear() {
        DEFAULT_EXCHANGE_NAME.accept(null);
        DEFAULT_QUEUE_NAME.accept(null);
        DEFAULT_ROUTING_KEY_NAME.accept(null);
    }

    @Test(description = "Check unbind queue from exchange")
    public void unbindTest1() throws Exception {
        rabbitMqStepContext.unbind(queueAndExchange(
                "queue",
                "exchange")
                .withRoutingKey("routingKey"));

        verify(channel, times(1))
                .queueUnbind("queue", "exchange", "routingKey");
    }

    @Test(description = "Check unbind queue from exchange with arguments")
    public void unbindTest2() throws Exception {
        rabbitMqStepContext.unbind(queueAndExchange(
                "queue",
                "exchange")
                .withRoutingKey("routingKey")
                .argument("testKey", "testValue"));

        verify(channel, times(1))
                .queueUnbind("queue", "exchange", "routingKey", Map.of("testKey", "testValue"));
    }

    @Test(description = "Check unbind a queue to exchange without additional arguments and routing key")
    public void unbindTest3() throws Exception {
        rabbitMqStepContext.unbind(queueAndExchange(
                "queue",
                "exchange"));

        verify(channel, times(1))
                .queueUnbind("queue",
                        "exchange",
                        "");
    }

    @Test(description = "Check unbind queue to default named exchange")
    public void unbindTest4() throws Exception {
        DEFAULT_EXCHANGE_NAME.accept("destination_exchange");
        rabbitMqStepContext.unbind(queueAndDefaultExchange("queue"));

        verify(channel, times(1))
                .queueUnbind("queue",
                        "destination_exchange",
                        "");
    }

    @Test(description = "Check unbind exchange exchange to default named queue")
    public void unbindTest5() throws Exception {
        DEFAULT_QUEUE_NAME.accept("destination_queue");
        rabbitMqStepContext.unbind(defaultQueueAndExchange("exchange"));

        verify(channel, times(1))
                .queueUnbind("destination_queue",
                        "exchange",
                        "");
    }

    @Test(description = "Check unbind exchanges with default named routing key")
    public void unbindTest6() throws Exception {
        DEFAULT_ROUTING_KEY_NAME.accept("default.routing");
        rabbitMqStepContext.unbind(queueAndExchange(
                "queue",
                "exchange")
                .withDefaultRoutingKey());

        verify(channel, times(1))
                .queueUnbind("queue",
                        "exchange",
                        "default.routing");
    }
}
