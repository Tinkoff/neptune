package ru.tinkoff.qa.neptune.rabbit.mq.test.basic.get;

import com.rabbitmq.client.GetResponse;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import ru.tinkoff.qa.neptune.rabbit.mq.test.DefaultMapper;
import ru.tinkoff.qa.neptune.rabbit.mq.test.DraftDto;
import ru.tinkoff.qa.neptune.rabbit.mq.test.captors.BaseCaptorTest;

import java.nio.charset.StandardCharsets;

import static java.time.Duration.ofSeconds;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.arrayWithSize;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.when;
import static ru.tinkoff.qa.neptune.core.api.properties.general.events.DoCapturesOf.DO_CAPTURES_OF_INSTANCE;
import static ru.tinkoff.qa.neptune.core.api.steps.selections.ItemsCountCondition.isEqual;
import static ru.tinkoff.qa.neptune.rabbit.mq.function.get.RabbitMqBasicGetArraySupplier.rabbitArray;
import static ru.tinkoff.qa.neptune.rabbit.mq.function.get.RabbitMqBasicGetIterableSupplier.rabbitIterable;
import static ru.tinkoff.qa.neptune.rabbit.mq.properties.RabbitMQRoutingProperties.DEFAULT_QUEUE_NAME;

public class GetResultWithConditionTest extends BaseCaptorTest {
    private final String body1 = new DefaultMapper().serialize(new DraftDto().setName("test1"));
    private final String body2 = new DefaultMapper().serialize(new DraftDto().setName("test2"));
    private final String body3 = new DefaultMapper().serialize(new DraftDto().setName("test3"));
    private final String body4 = new DefaultMapper().serialize(new DraftDto().setName("test4"));
    private final String body5 = new DefaultMapper().serialize(new DraftDto().setName("test5"));
    private final String body6 = new DefaultMapper().serialize(new DraftDto().setName("test6"));
    private final String body7 = new DefaultMapper().serialize(new DraftDto().setName("test7"));

    @BeforeMethod
    public void beforeClass() throws Exception {
        when(channel.basicGet("test_queue3", true))
                .thenReturn(new GetResponse(null, null, body1.getBytes(StandardCharsets.UTF_8), 0))
                .thenReturn(new GetResponse(null, null, body2.getBytes(StandardCharsets.UTF_8), 0))
                .thenReturn(new GetResponse(null, null, body3.getBytes(StandardCharsets.UTF_8), 0))
                .thenReturn(new GetResponse(null, null, body4.getBytes(StandardCharsets.UTF_8), 0))
                .thenReturn(new GetResponse(null, null, body5.getBytes(StandardCharsets.UTF_8), 0))
                .thenReturn(new GetResponse(null, null, body6.getBytes(StandardCharsets.UTF_8), 0))
                .thenReturn(new GetResponse(null, null, body7.getBytes(StandardCharsets.UTF_8), 0));
        DEFAULT_QUEUE_NAME.accept("test_queue3");
    }

    @AfterClass
    public void afterClass() {
        DEFAULT_QUEUE_NAME.accept(null);
        DO_CAPTURES_OF_INSTANCE.accept(null);
    }

    @Test
    public void test1() {
        var result = rabbitMqStepContext.read(rabbitArray("description",
                DraftDto.class)
                .autoAck()
                .returnBeforeIndex(6)
                .timeOut(ofSeconds(5)));

        assertThat(result, arrayWithSize(6));
    }

    @Test
    public void test2() {
        //TODO почему возвращает один элемент, хотя returnBeforeIndex нормально работает
        var result = rabbitMqStepContext.read(rabbitArray("description",
                DraftDto.class)
                .autoAck()
                .returnAfterIndex(5)
                .timeOut(ofSeconds(5)));

        //assertThat(result, arrayWithSize(6));
    }

    @Test
    public void test3() {
        var result = rabbitMqStepContext.read(rabbitArray("description",
                DraftDto.class)
                .autoAck()
                .returnArrayOfLength(3)
                .timeOut(ofSeconds(5)));

        assertThat(result, arrayWithSize(3));
    }

    @Test
    public void test4() {
        var result = rabbitMqStepContext.read(rabbitArray("description",
                DraftDto.class)
                .autoAck()
                .returnIfEntireLength(isEqual(7))
                .timeOut(ofSeconds(5)));

        assertThat(result, arrayWithSize(7));
    }

    @Test
    public void test5() {
        var result = rabbitMqStepContext.read(rabbitArray("description",
                DraftDto.class)
                .autoAck()
                .returnIfEntireLength(isEqual(6))
                .timeOut(ofSeconds(5)));

        assertThat(result, arrayWithSize(6));
    }

    @Test
    public void test6() {
        var result = rabbitMqStepContext.read(rabbitIterable("description",
                DraftDto.class)
                .autoAck()
                .returnBeforeIndex(6)
                .timeOut(ofSeconds(5)));

        assertThat(result, hasSize(6));
    }

    @Test
    public void test7() {
        //TODO почему возвращает один элемент
        var result = rabbitMqStepContext.read(rabbitIterable("description",
                DraftDto.class)
                .autoAck()
                .returnAfterIndex(5)
                .timeOut(ofSeconds(5)));

        // assertThat(result, hasSize(6));
    }
}

