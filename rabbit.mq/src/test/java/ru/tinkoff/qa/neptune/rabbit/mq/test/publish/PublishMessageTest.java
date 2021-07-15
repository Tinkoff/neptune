package ru.tinkoff.qa.neptune.rabbit.mq.test.publish;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.rabbitmq.client.AMQP;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import ru.tinkoff.qa.neptune.rabbit.mq.test.BaseRabbitMqTest;
import ru.tinkoff.qa.neptune.rabbit.mq.test.DraftDto;

import java.io.IOException;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static ru.tinkoff.qa.neptune.rabbit.mq.function.publish.ParametersForPublish.parameters;

public class PublishMessageTest extends BaseRabbitMqTest {
    DraftDto draftDto;
    byte[] bytesWithJsonMapper;
    byte[] bytesWithXmlMapper;

    @BeforeClass(dependsOnMethods = "setUp")
    public void prepare() throws JsonProcessingException {
        draftDto = new DraftDto().setName("Petr");
        bytesWithJsonMapper = new JsonMapper().writeValueAsBytes(draftDto);
        bytesWithXmlMapper = new XmlMapper().writeValueAsBytes(draftDto);
    }

    @Test
    public void purgeTest1() throws IOException {
        rabbitMqStepContext.publishMessage("exchange1", "routingKey1", draftDto);

        verify(channel, times(1))
                .basicPublish("exchange1", "routingKey1", false, false, new AMQP.BasicProperties(), bytesWithJsonMapper);
    }

    @Test
    public void purgeTest2() throws IOException {
        rabbitMqStepContext.publishMessage("exchange2", "routingKey2", draftDto, new XmlMapper());

        verify(channel, times(1))
                .basicPublish("exchange2", "routingKey2", false, false, new AMQP.BasicProperties(), bytesWithXmlMapper);
    }

    @Test
    public void purgeTest3() throws IOException {
        rabbitMqStepContext.publishMessage("exchange2", "routingKey2", parameters(), draftDto);

        verify(channel, times(1))
                .basicPublish("exchange2", "routingKey2", false, false, new AMQP.BasicProperties(), bytesWithJsonMapper);
    }

    @Test
    public void purgeTest4() throws IOException {
        rabbitMqStepContext.publishMessage("exchange2", "routingKey2", parameters().mandatory(), draftDto, new XmlMapper());

        verify(channel, times(1))
                .basicPublish("exchange2", "routingKey2", true, false, new AMQP.BasicProperties(), bytesWithXmlMapper);
    }
}
