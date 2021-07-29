package ru.tinkoff.qa.neptune.rabbit.mq.test.captors;

import com.rabbitmq.client.GetResponse;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import ru.tinkoff.qa.neptune.rabbit.mq.test.DefaultMapper;
import ru.tinkoff.qa.neptune.rabbit.mq.test.DraftDto;

import java.nio.charset.StandardCharsets;
import java.time.Duration;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anEmptyMap;
import static org.mockito.Mockito.when;
import static org.testng.Assert.fail;
import static ru.tinkoff.qa.neptune.core.api.hamcrest.iterables.MapEntryMatcher.mapEntry;
import static ru.tinkoff.qa.neptune.core.api.hamcrest.iterables.SetOfObjectsConsistsOfMatcher.mapOf;
import static ru.tinkoff.qa.neptune.core.api.properties.general.events.CapturedEvents.*;
import static ru.tinkoff.qa.neptune.core.api.properties.general.events.DoCapturesOf.DO_CAPTURES_OF_INSTANCE;
import static ru.tinkoff.qa.neptune.rabbit.mq.function.get.RabbitMqBasicGetSupplier.rabbitBody;
import static ru.tinkoff.qa.neptune.rabbit.mq.test.captors.TestStringInjector.CAUGHT_MESSAGES;

public class GetObjectCaptorTest extends BaseCaptorTest {

    private final DraftDto dto = new DraftDto().setName("test");
    private final String body = new DefaultMapper().serialize(dto);

    @BeforeClass
    public void beforeClass() throws Exception {
        when(channel.basicGet("test_queue", true))
                .thenReturn(new GetResponse(null, null, body.getBytes(StandardCharsets.UTF_8), 0));
    }

    @Test
    public void test1() {
        rabbitMqStepContext.read(rabbitBody(
                "test_queue",
                true,
                DraftDto.class));

        assertThat(CAUGHT_MESSAGES, anEmptyMap());
    }

    @Test
    public void test2() {
        DO_CAPTURES_OF_INSTANCE.accept(SUCCESS);

        rabbitMqStepContext.read(rabbitBody(
                "test_queue",
                true,
                DraftDto.class));

        assertThat(CAUGHT_MESSAGES, mapOf(mapEntry("RabbitMQ message", "{\"name\":\"test\"}")));
    }

    @Test
    public void test3() {
        DO_CAPTURES_OF_INSTANCE.accept(FAILURE);

        rabbitMqStepContext.read(rabbitBody(
                "test_queue",
                true,
                DraftDto.class));

        assertThat(CAUGHT_MESSAGES, anEmptyMap());
    }

    @Test
    public void test4() {
        DO_CAPTURES_OF_INSTANCE.accept(SUCCESS_AND_FAILURE);

        rabbitMqStepContext.read(rabbitBody(
                "test_queue",
                true,
                DraftDto.class));

        assertThat(CAUGHT_MESSAGES, mapOf(mapEntry("RabbitMQ message", "{\"name\":\"test\"}")));
    }

    @Test
    public void test5() {
        rabbitMqStepContext.read(rabbitBody(
                "test_queue",
                true,
                DraftDto.class)
                .criteria("name=test2", draftDto -> draftDto.getName().equals("test2"))
                .timeOut(Duration.ofSeconds(5)));

        assertThat(CAUGHT_MESSAGES, anEmptyMap());
    }

    @Test
    public void test6() {
        DO_CAPTURES_OF_INSTANCE.accept(SUCCESS);
        rabbitMqStepContext.read(rabbitBody(
                "test_queue",
                true,
                DraftDto.class)
                .criteria("name=test2", draftDto -> draftDto.getName().equals("test2"))
                .timeOut(Duration.ofSeconds(5)));

        assertThat(CAUGHT_MESSAGES, mapOf(mapEntry("Read RabbitMQ messages", "#1\r\n" +
                "\r\n" +
                "{\"name\":\"test\"}")));
    }

    @Test
    public void test7() {
        DO_CAPTURES_OF_INSTANCE.accept(FAILURE);
        rabbitMqStepContext.read(rabbitBody(
                "test_queue",
                true,
                DraftDto.class)
                .criteria("name=test2", draftDto -> draftDto.getName().equals("test2"))
                .timeOut(Duration.ofSeconds(5)));

        assertThat(CAUGHT_MESSAGES, anEmptyMap());
    }

    @Test
    public void test8() {
        DO_CAPTURES_OF_INSTANCE.accept(SUCCESS_AND_FAILURE);
        rabbitMqStepContext.read(rabbitBody(
                "test_queue",
                true,
                DraftDto.class)
                .criteria("name=test2", draftDto -> draftDto.getName().equals("test2"))
                .timeOut(Duration.ofSeconds(5)));

        assertThat(CAUGHT_MESSAGES, mapOf(mapEntry("Read RabbitMQ messages", "#1\r\n" +
                "\r\n" +
                "{\"name\":\"test\"}")));
    }

    @Test
    public void test9() {
        try {
            rabbitMqStepContext.read(rabbitBody(
                    "test_queue",
                    true,
                    DraftDto.class)
                    .criteria("name=test2", draftDto -> draftDto.getName().equals("test2"))
                    .timeOut(Duration.ofSeconds(5))
                    .throwOnNoResult());
        }
        catch (Exception e) {
            assertThat(CAUGHT_MESSAGES, anEmptyMap());
            return;
        }

        fail("Exception was expected");
    }

    @Test
    public void test10() {
        DO_CAPTURES_OF_INSTANCE.accept(SUCCESS);

        try {
            rabbitMqStepContext.read(rabbitBody(
                    "test_queue",
                    true,
                    DraftDto.class)
                    .criteria("name=test2", draftDto -> draftDto.getName().equals("test2"))
                    .timeOut(Duration.ofSeconds(5))
                    .throwOnNoResult());
        }
        catch (Exception e) {
            assertThat(CAUGHT_MESSAGES, anEmptyMap());
            return;
        }

        fail("Exception was expected");

    }

    @Test
    public void test11() {
        DO_CAPTURES_OF_INSTANCE.accept(FAILURE);

        try {
            rabbitMqStepContext.read(rabbitBody(
                    "test_queue",
                    true,
                    DraftDto.class)
                    .criteria("name=test2", draftDto -> draftDto.getName().equals("test2"))
                    .timeOut(Duration.ofSeconds(5))
                    .throwOnNoResult());
        }
        catch (Exception e) {
            assertThat(CAUGHT_MESSAGES, mapOf(mapEntry("Read RabbitMQ messages", "#1\r\n" +
                    "\r\n" +
                    "{\"name\":\"test\"}")));
            return;
        }

        fail("Exception was expected");

    }

    @Test
    public void test12() {
        DO_CAPTURES_OF_INSTANCE.accept(SUCCESS_AND_FAILURE);

        try {
            rabbitMqStepContext.read(rabbitBody(
                    "test_queue",
                    true,
                    DraftDto.class)
                    .criteria("name=test2", draftDto -> draftDto.getName().equals("test2"))
                    .timeOut(Duration.ofSeconds(5))
                    .throwOnNoResult());
        }
        catch (Exception e) {
            assertThat(CAUGHT_MESSAGES, mapOf(mapEntry("Read RabbitMQ messages", "#1\r\n" +
                    "\r\n" +
                    "{\"name\":\"test\"}")));
            return;
        }

        fail("Exception was expected");

    }
}
