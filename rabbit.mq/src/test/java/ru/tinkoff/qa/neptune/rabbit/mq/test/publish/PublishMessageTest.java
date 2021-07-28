package ru.tinkoff.qa.neptune.rabbit.mq.test.publish;

import com.rabbitmq.client.AMQP;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import ru.tinkoff.qa.neptune.rabbit.mq.test.BaseRabbitMqTest;
import ru.tinkoff.qa.neptune.rabbit.mq.test.CustomMapper;
import ru.tinkoff.qa.neptune.rabbit.mq.test.DefaultMapper;
import ru.tinkoff.qa.neptune.rabbit.mq.test.DraftDto;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static ru.tinkoff.qa.neptune.rabbit.mq.function.publish.ParametersForPublish.parameters;

public class PublishMessageTest extends BaseRabbitMqTest {
    byte[] bytesWithDefaultMapper;
    byte[] bytesWithCustomMapper;

    @BeforeClass(dependsOnMethods = "setUp")
    public void prepare() {
        bytesWithDefaultMapper = new DefaultMapper().serialize(new DraftDto().setName("test")).getBytes(StandardCharsets.UTF_8);
        bytesWithCustomMapper = "customSerialize".getBytes();
    }

    @Test(description = "Check publish message with default mapper and no-parameters")
    public void publishTest1() throws IOException {
        rabbitMqStepContext.publishMessage("exchange1",
                "routingKey1",
                new DraftDto().setName("test"));

        verify(channel, times(1))
                .basicPublish("exchange1", "routingKey1",
                        false,
                        false,
                        new AMQP.BasicProperties(), bytesWithDefaultMapper);
    }

    @Test(description = "Check publish message with custom mapper and no-parameters")
    public void publishTest2() throws IOException {
        rabbitMqStepContext.publishMessage("exchange2",
                "routingKey2",
                new Object(),
                new CustomMapper());

        verify(channel, times(1))
                .basicPublish("exchange2", "routingKey2",
                        false,
                        false, new AMQP.BasicProperties(),
                        bytesWithCustomMapper);
    }

    @Test(description = "Check publish message with default mapper and parameters")
    public void publishTest3() throws IOException {
        rabbitMqStepContext.publishMessage("exchange2", "routingKey2",
                parameters().immediate(),
                new DraftDto().setName("test"));

        verify(channel, times(1))
                .basicPublish("exchange2",
                        "routingKey2",
                        false,
                        true,
                        new AMQP.BasicProperties(), bytesWithDefaultMapper);
    }

    @Test(description = "Check publish message with default mapper and parameters")
    public void publishTest4() throws IOException {
        rabbitMqStepContext.publishMessage("exchange2", "routingKey2",
                parameters().mandatory(),
                new DraftDto().setName("test"));

        verify(channel, times(1))
                .basicPublish("exchange2",
                        "routingKey2",
                        true,
                        false,
                        new AMQP.BasicProperties(),
                        bytesWithDefaultMapper);
    }
}
