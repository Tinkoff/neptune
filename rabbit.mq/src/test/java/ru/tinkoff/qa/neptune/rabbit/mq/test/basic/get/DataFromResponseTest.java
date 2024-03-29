package ru.tinkoff.qa.neptune.rabbit.mq.test.basic.get;

import com.fasterxml.jackson.core.type.TypeReference;
import com.rabbitmq.client.GetResponse;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import ru.tinkoff.qa.neptune.rabbit.mq.BaseRabbitMqPreparations;
import ru.tinkoff.qa.neptune.rabbit.mq.test.CustomMapper;
import ru.tinkoff.qa.neptune.rabbit.mq.test.DefaultMapper;
import ru.tinkoff.qa.neptune.rabbit.mq.test.DraftDto;

import java.nio.charset.StandardCharsets;

import static java.time.Duration.ofSeconds;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.when;
import static ru.tinkoff.qa.neptune.rabbit.mq.function.get.GetResponseSupplier.responses;
import static ru.tinkoff.qa.neptune.rabbit.mq.properties.RabbitMQRoutingProperties.DEFAULT_QUEUE_NAME;

public class DataFromResponseTest extends BaseRabbitMqPreparations {
    private final String body1 = new DefaultMapper().serialize(new DraftDto().setName("test1"));
    private final String body2 = new DefaultMapper().serialize(new DraftDto().setName("test2"));

    @BeforeMethod
    public void beforeClass() throws Exception {
        when(channel.basicGet("test_queue3", true))
                .thenReturn(new GetResponse(null, null, body1.getBytes(StandardCharsets.UTF_8), 1))
                .thenReturn(new GetResponse(null, null, body2.getBytes(StandardCharsets.UTF_8), 0));

        DEFAULT_QUEUE_NAME.accept("test_queue3");
    }

    @Test
    public void test1() {
        var responses = rabbitMqStepContext.read(responses()
                .timeOut(ofSeconds(5)));

        assertThat(responses, hasSize(0));
    }

    @Test
    public void test2() {
        var responses = rabbitMqStepContext.read(responses("test_queue3")
                .criteria("Body contains test2", r -> new String(r.getBody()).contains("test2"))
                .autoAck()
                .timeOut(ofSeconds(5)));

        assertThat(responses, hasSize(1));
    }

    @Test
    public void test3() {
        var responses = rabbitMqStepContext.read(responses()
                .autoAck()
                .timeOut(ofSeconds(50)));

        assertThat(responses, hasSize(1));
    }

    @Test
    public void test4() {
        var responses = rabbitMqStepContext.read(responses()
                .autoAck()
                .thenGetItem("description", DraftDto.class));

        assertThat(responses.getName(), is("test1"));
    }

    @Test
    public void test5() {
        var responses = rabbitMqStepContext.read(responses()
                .autoAck()
                .thenGetItem("description", new TypeReference<DraftDto>() {
                }));

        assertThat(responses.getName(), is("test1"));
    }

    @Test
    public void test6() {
        var responses = rabbitMqStepContext.read(responses()
                .autoAck()
                .timeOut(ofSeconds(5))
                .thenGetList("description", DraftDto.class));

        assertThat(responses, hasSize(1));
    }

    @Test
    public void test7() {
        var responses = rabbitMqStepContext.read(responses()
                .autoAck()
                .timeOut(ofSeconds(5))
                .thenGetList("description", new TypeReference<DraftDto>() {
                }));

        assertThat(responses, hasSize(1));
    }

    @Test
    public void test8() {
        var responses = rabbitMqStepContext.read(responses()
                .autoAck()
                .timeOut(ofSeconds(5))
                .thenGetList("description", GetResponse::getBody));

        assertThat(responses, hasSize(1));
    }

    @Test
    public void test9() {
        var responses = rabbitMqStepContext.read(responses()
                .autoAck()
                .thenGetItem("description", GetResponse::getMessageCount));

        assertThat(responses, is(1));
    }

    @Test
    public void test10() {
        var responses = rabbitMqStepContext.read(responses()
                .autoAck()
                .timeOut(ofSeconds(5))
                .thenGetList("description", new TypeReference<DraftDto>() {
                }).withDataTransformer(new CustomMapper()));

        assertThat(responses.get(0).getName(), containsString("PREFIX"));
    }

    @Test
    public void test11() {
        var responses = rabbitMqStepContext.read(responses()
                .autoAck()
                .thenGetItem("description", new TypeReference<DraftDto>() {})
                .withDataTransformer(new CustomMapper()));

        assertThat(responses.getName(), is("PREFIXtest1"));
    }
}
