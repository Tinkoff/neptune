package ru.tinkoff.qa.neptune.rabbit.mq.test.publish;

import com.rabbitmq.client.AMQP;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import ru.tinkoff.qa.neptune.rabbit.mq.BaseRabbitMqPreparations;
import ru.tinkoff.qa.neptune.rabbit.mq.test.CustomMapper;
import ru.tinkoff.qa.neptune.rabbit.mq.test.DefaultMapper;
import ru.tinkoff.qa.neptune.rabbit.mq.test.DraftDto;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Map;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static ru.tinkoff.qa.neptune.rabbit.mq.function.publish.RabbitMqPublishSupplier.rabbitSerializedMessage;
import static ru.tinkoff.qa.neptune.rabbit.mq.function.publish.RabbitMqPublishSupplier.rabbitTextMessage;
import static ru.tinkoff.qa.neptune.rabbit.mq.properties.RabbitMQRoutingProperties.DEFAULT_EXCHANGE_NAME;
import static ru.tinkoff.qa.neptune.rabbit.mq.properties.RabbitMQRoutingProperties.DEFAULT_ROUTING_KEY_NAME;

public class PublishMessageTest extends BaseRabbitMqPreparations {
    byte[] bytesWithDefaultMapper;
    byte[] bytesWithCustomMapper;

    @AfterMethod
    @BeforeMethod
    public void clear() {
        DEFAULT_EXCHANGE_NAME.accept(null);
        DEFAULT_ROUTING_KEY_NAME.accept(null);
    }

    @BeforeClass(dependsOnMethods = "setUp")
    public void prepare() {
        bytesWithDefaultMapper = new DefaultMapper().serialize(new DraftDto().setName("test")).getBytes(StandardCharsets.UTF_8);
        bytesWithCustomMapper = "customSerialize".getBytes();
    }

    @Test(description = "Check publish message with default mapper and no-parameters")
    public void publishTest1() throws IOException {
        rabbitMqStepContext.publish(rabbitSerializedMessage(
                new DraftDto().setName("test"))
                .exchange("exchange1")
                .routingKey("routingKey1"));

        verify(channel, times(1))
                .basicPublish("exchange1", "routingKey1",
                        false,
                        false,
                        new AMQP.BasicProperties(), bytesWithDefaultMapper);
    }

    @Test(description = "Check publish message with custom mapper and no-parameters")
    public void publishTest2() throws IOException {
        rabbitMqStepContext.publish(rabbitSerializedMessage(new Object())
                .exchange("exchange2")
                .routingKey("routingKey2")
                .withDataTransformer(new CustomMapper()));

        verify(channel, times(1))
                .basicPublish("exchange2", "routingKey2",
                        false,
                        false, new AMQP.BasicProperties(),
                        bytesWithCustomMapper);
    }

    @Test(description = "Check publish message with default mapper and parameters")
    public void publishTest3() throws IOException {
        rabbitMqStepContext.publish(rabbitSerializedMessage(
                new DraftDto().setName("test"))
                .exchange("exchange2")
                .routingKey("routingKey2")
                .immediate());

        verify(channel, times(1))
                .basicPublish("exchange2",
                        "routingKey2",
                        false,
                        true,
                        new AMQP.BasicProperties(), bytesWithDefaultMapper);
    }

    @Test(description = "Check publish message with default mapper and parameters")
    public void publishTest4() throws IOException {
        rabbitMqStepContext.publish(rabbitSerializedMessage(
                new DraftDto().setName("test"))
                .exchange("exchange2")
                .routingKey("routingKey2")
                .mandatory());

        verify(channel, times(1))
                .basicPublish("exchange2",
                        "routingKey2",
                        true,
                        false,
                        new AMQP.BasicProperties(),
                        bytesWithDefaultMapper);
    }

    @Test(description = "Check publish message with default exchange value")
    public void publishTest5() throws Exception {
        DEFAULT_EXCHANGE_NAME.accept("default_exchange");

        rabbitMqStepContext.publish(rabbitTextMessage("Hello world")
                .toDefaultExchange());

        verify(channel, times(1))
                .basicPublish("default_exchange",
                        "",
                        false,
                        false,
                        new AMQP.BasicProperties(),
                        "Hello world".getBytes());
    }

    @Test(description = "Check publish message with default routing key value")
    public void publishTest6() throws Exception {
        DEFAULT_ROUTING_KEY_NAME.accept("default_routing_key");

        rabbitMqStepContext.publish(rabbitTextMessage("Hello world")
                .useDefaultRoutingKey());

        verify(channel, times(1))
                .basicPublish("",
                        "default_routing_key",
                        false,
                        false,
                        new AMQP.BasicProperties(),
                        "Hello world".getBytes());
    }

    @Test(description = "Check publish message with additional properties")
    public void publishTest7() throws Exception {
        var timeStamp = new Date();

        rabbitMqStepContext.publish(rabbitTextMessage("Hello world")
                .exchange("exchange1")
                .routingKey("routing_key1")
                .header("header1", "value1")
                .replyTo("ReplyTo")
                .deliveryMode(1)
                .priority(2)
                .contentType("plain text")
                .contentEncoding("UTF-8")
                .correlationId("!@#4")
                .expiration("1234567")
                .messageId("hjhjkhjk")
                .timestamp(timeStamp)
                .userId("UserId")
                .appId("AppId")
                .clusterId("ClasterId"));

        verify(channel, times(1))
                .basicPublish("exchange1",
                        "routing_key1",
                        false,
                        false,
                        new AMQP.BasicProperties()
                                .builder()
                                .headers(Map.of("header1", "value1"))
                                .replyTo("ReplyTo")
                                .deliveryMode(1)
                                .priority(2)
                                .contentType("plain text")
                                .contentEncoding("UTF-8")
                                .correlationId("!@#4")
                                .expiration("1234567")
                                .messageId("hjhjkhjk")
                                .timestamp(timeStamp)
                                .userId("UserId")
                                .appId("AppId")
                                .clusterId("ClasterId")
                                .build(),
                        "Hello world".getBytes());
    }
}
