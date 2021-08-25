package ru.tinkoff.qa.neptune.rabbit.mq.test.captors;

import com.fasterxml.jackson.core.type.TypeReference;
import com.rabbitmq.client.GetResponse;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import ru.tinkoff.qa.neptune.rabbit.mq.test.DefaultMapper;
import ru.tinkoff.qa.neptune.rabbit.mq.test.DraftDto;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static java.time.Duration.ofSeconds;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anEmptyMap;
import static org.mockito.Mockito.when;
import static org.testng.Assert.fail;
import static ru.tinkoff.qa.neptune.core.api.hamcrest.iterables.MapEntryMatcher.mapEntry;
import static ru.tinkoff.qa.neptune.core.api.hamcrest.iterables.SetOfObjectsConsistsOfMatcher.mapOf;
import static ru.tinkoff.qa.neptune.core.api.properties.general.events.CapturedEvents.*;
import static ru.tinkoff.qa.neptune.core.api.properties.general.events.DoCapturesOf.DO_CAPTURES_OF_INSTANCE;
import static ru.tinkoff.qa.neptune.rabbit.mq.function.get.RabbitMqBasicGetArrayItemSupplier.rabbitArrayItem;
import static ru.tinkoff.qa.neptune.rabbit.mq.properties.RabbitMQRoutingProperties.DEFAULT_QUEUE_NAME;
import static ru.tinkoff.qa.neptune.rabbit.mq.test.captors.TestStringInjector.CAUGHT_MESSAGES;

public class GetArrayItemCaptorTest extends BaseCaptorTest {

    private final List<DraftDto> dtos = List.of(new DraftDto().setName("test2"), new DraftDto().setName("test3"));
    private final String body = new DefaultMapper().serialize(dtos);

    @BeforeClass
    public void beforeClass() throws Exception {
        when(channel.basicGet("test_queue5", true))
                .thenReturn(new GetResponse(null, null, body.getBytes(StandardCharsets.UTF_8), 0));

        DEFAULT_QUEUE_NAME.accept("test_queue5");
    }

    @AfterClass
    public void afterClass() {
        DEFAULT_QUEUE_NAME.accept(null);
        DO_CAPTURES_OF_INSTANCE.accept(null);
    }

    @Test
    public void test1() {
        rabbitMqStepContext.read(rabbitArrayItem("Value of fields 'name'",
                new TypeReference<List<DraftDto>>() {
                },
                list -> list.stream().map(DraftDto::getName).toArray(String[]::new))
                .autoAck()
                .criteria("Value contains 'test", s -> s.contains("test")));

        assertThat(CAUGHT_MESSAGES, anEmptyMap());
    }

    @Test
    public void test2() {
        DO_CAPTURES_OF_INSTANCE.accept(SUCCESS);

        rabbitMqStepContext.read(rabbitArrayItem("Value of fields 'name'",
                new TypeReference<List<DraftDto>>() {
                },
                list -> list.stream().map(DraftDto::getName).toArray(String[]::new))
                .autoAck()
                .criteria("Value contains 'test", s -> s.contains("test")));

        assertThat(CAUGHT_MESSAGES, mapOf(mapEntry("RabbitMQ message",
                "[{\"name\":\"test2\"},{\"name\":\"test3\"}]")));
    }

    @Test
    public void test3() {
        DO_CAPTURES_OF_INSTANCE.accept(FAILURE);

        rabbitMqStepContext.read(rabbitArrayItem("Value of fields 'name'",
                new TypeReference<List<DraftDto>>() {
                },
                list -> list.stream().map(DraftDto::getName).toArray(String[]::new))
                .autoAck()
                .criteria("Value contains 'test", s -> s.contains("test")));

        assertThat(CAUGHT_MESSAGES, anEmptyMap());
    }

    @Test
    public void test4() {
        DO_CAPTURES_OF_INSTANCE.accept(SUCCESS_AND_FAILURE);

        rabbitMqStepContext.read(rabbitArrayItem("Value of fields 'name'",
                new TypeReference<List<DraftDto>>() {
                },
                list -> list.stream().map(DraftDto::getName).toArray(String[]::new))
                .autoAck()
                .criteria("Value contains 'test", s -> s.contains("test")));

        assertThat(CAUGHT_MESSAGES, mapOf(mapEntry("RabbitMQ message",
                "[{\"name\":\"test2\"},{\"name\":\"test3\"}]")));
    }

    @Test
    public void test5() {
        rabbitMqStepContext.read(rabbitArrayItem("Value of fields 'name'",
                new TypeReference<List<DraftDto>>() {
                },
                list -> list.stream().map(DraftDto::getName).toArray(String[]::new))
                .autoAck()
                .criteria("Value contains 'test", s -> s.contains("fail")));

        assertThat(CAUGHT_MESSAGES, anEmptyMap());
    }

    @Test
    public void test6() {
        DO_CAPTURES_OF_INSTANCE.accept(SUCCESS);
        rabbitMqStepContext.read(rabbitArrayItem("Value of fields 'name'",
                new TypeReference<List<DraftDto>>() {
                },
                list -> list.stream().map(DraftDto::getName).toArray(String[]::new))
                .autoAck()
                .criteria("Value contains 'test", s -> s.contains("fail"))
                .timeOut(ofSeconds(5)));

        assertThat(CAUGHT_MESSAGES, mapOf(mapEntry("Read RabbitMQ messages", "#1\r\n" +
                "\r\n" +
                "[{\"name\":\"test2\"},{\"name\":\"test3\"}]")));
    }

    @Test
    public void test7() {
        DO_CAPTURES_OF_INSTANCE.accept(FAILURE);
        rabbitMqStepContext.read(rabbitArrayItem("Value of fields 'name'",
                new TypeReference<List<DraftDto>>() {
                },
                list -> list.stream().map(DraftDto::getName).toArray(String[]::new))
                .autoAck()
                .criteria("Value contains 'test", s -> s.contains("fail"))
                .timeOut(ofSeconds(5)));

        assertThat(CAUGHT_MESSAGES, anEmptyMap());
    }

    @Test
    public void test8() {
        DO_CAPTURES_OF_INSTANCE.accept(SUCCESS_AND_FAILURE);
        rabbitMqStepContext.read(rabbitArrayItem("Value of fields 'name'",
                new TypeReference<List<DraftDto>>() {
                },
                list -> list.stream().map(DraftDto::getName).toArray(String[]::new))
                .autoAck()
                .criteria("Value contains 'test", s -> s.contains("fail"))
                .timeOut(ofSeconds(5)));

        assertThat(CAUGHT_MESSAGES, mapOf(mapEntry("Read RabbitMQ messages", "#1\r\n" +
                "\r\n" +
                "[{\"name\":\"test2\"},{\"name\":\"test3\"}]")));
    }
    
    @Test
    public void test9() {
        try {
            rabbitMqStepContext.read(rabbitArrayItem("Value of fields 'name'",
                    new TypeReference<List<DraftDto>>() {
                    },
                    list -> list.stream().map(DraftDto::getName).toArray(String[]::new))
                    .autoAck()
                    .criteria("Value contains 'test", s -> s.contains("fail"))
                    .timeOut(ofSeconds(5))
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
            rabbitMqStepContext.read(rabbitArrayItem("Value of fields 'name'",
                    new TypeReference<List<DraftDto>>() {
                    },
                    list -> list.stream().map(DraftDto::getName).toArray(String[]::new))
                    .autoAck()
                    .criteria("Value contains 'test", s -> s.contains("fail"))
                    .timeOut(ofSeconds(5))
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
            rabbitMqStepContext.read(rabbitArrayItem("Value of fields 'name'",
                    new TypeReference<List<DraftDto>>() {
                    },
                    list -> list.stream().map(DraftDto::getName).toArray(String[]::new))
                    .autoAck()
                    .criteria("Value contains 'test", s -> s.contains("fail"))
                    .timeOut(ofSeconds(5))
                    .throwOnNoResult());
        }
        catch (Exception e) {
            assertThat(CAUGHT_MESSAGES, mapOf(mapEntry("Read RabbitMQ messages", "#1\r\n" +
                    "\r\n" +
                    "[{\"name\":\"test2\"},{\"name\":\"test3\"}]")));
            return;
        }

        fail("Exception was expected");

    }

    @Test
    public void test12() {
        DO_CAPTURES_OF_INSTANCE.accept(SUCCESS_AND_FAILURE);

        try {
            rabbitMqStepContext.read(rabbitArrayItem("Value of fields 'name'",
                    "test_queue5",
                    new TypeReference<List<DraftDto>>() {
                    },
                    list -> list.stream().map(DraftDto::getName).toArray(String[]::new))
                    .autoAck()
                    .criteria("Value contains 'test", s -> s.contains("fail"))
                    .timeOut(ofSeconds(5))
                    .throwOnNoResult());
        }
        catch (Exception e) {
            assertThat(CAUGHT_MESSAGES, mapOf(mapEntry("Read RabbitMQ messages", "#1\r\n" +
                    "\r\n" +
                    "[{\"name\":\"test2\"},{\"name\":\"test3\"}]")));
            return;
        }

        fail("Exception was expected");

    }
}
