package ru.tinkoff.qa.neptune.rabbit.mq.test.captors;

import com.fasterxml.jackson.core.type.TypeReference;
import com.rabbitmq.client.GetResponse;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import ru.tinkoff.qa.neptune.rabbit.mq.test.DefaultMapper;
import ru.tinkoff.qa.neptune.rabbit.mq.test.DraftDto;

import java.nio.charset.StandardCharsets;

import static java.time.Duration.ofSeconds;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anEmptyMap;
import static org.hamcrest.Matchers.containsString;
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
    private final String body1 = new DefaultMapper().serialize(new DraftDto().setName("test1"));
    private final String body2 = new DefaultMapper().serialize(new DraftDto().setName("test2"));

    @BeforeMethod
    public void beforeClass() throws Exception {
        when(channel.basicGet("test_queue3", true))
                .thenReturn(new GetResponse(null, null, body1.getBytes(StandardCharsets.UTF_8), 0))
                .thenReturn(new GetResponse(null, null, body2.getBytes(StandardCharsets.UTF_8), 0));

        DEFAULT_QUEUE_NAME.accept("test_queue3");
    }

    @AfterClass
    public void afterClass() {
        DEFAULT_QUEUE_NAME.accept(null);
        DO_CAPTURES_OF_INSTANCE.accept(null);
    }

    @Test
    public void test1() {
        rabbitMqStepContext.read(rabbitArrayItem("description",
                new TypeReference<DraftDto>() {
                })
                .autoAck()
                .criteria("Name equals test1", ss -> ss.getName().contains("test1"))
                .timeOut(ofSeconds(5)));

        assertThat(CAUGHT_MESSAGES, anEmptyMap());
    }

    @Test
    public void test2() {
        DO_CAPTURES_OF_INSTANCE.accept(SUCCESS);

        rabbitMqStepContext.read(rabbitArrayItem("description",
                new TypeReference<DraftDto>() {
                })
                .autoAck()
                .criteria("Name equals test1", ss -> ss.getName().contains("test1"))
                .timeOut(ofSeconds(5)));

        assertThat(CAUGHT_MESSAGES, mapOf(mapEntry("RabbitMQ message",
                "{\"name\":\"test1\"}")));
    }

    @Test
    public void test3() {
        DO_CAPTURES_OF_INSTANCE.accept(FAILURE);

        rabbitMqStepContext.read(rabbitArrayItem("description",
                new TypeReference<DraftDto>() {
                })
                .autoAck()
                .criteria("Name equals test1", ss -> ss.getName().contains("test1"))
                .timeOut(ofSeconds(5)));

        assertThat(CAUGHT_MESSAGES, anEmptyMap());
    }

    @Test
    public void test4() {
        DO_CAPTURES_OF_INSTANCE.accept(SUCCESS_AND_FAILURE);

        rabbitMqStepContext.read(rabbitArrayItem("description",
                new TypeReference<DraftDto>() {
                })
                .autoAck()
                .criteria("Name equals test1", ss -> ss.getName().contains("test1"))
                .timeOut(ofSeconds(5)));


        assertThat(CAUGHT_MESSAGES, mapOf(mapEntry("RabbitMQ message",
                "{\"name\":\"test1\"}")));
    }

    @Test
    public void test5() {
        DO_CAPTURES_OF_INSTANCE.accept(SUCCESS_AND_FAILURE);

        rabbitMqStepContext.read(rabbitArrayItem("description",
                new TypeReference<DraftDto>() {
                })
                .autoAck()
                .criteria("Name equals test1", ss -> ss.getName().contains("test2"))
                .timeOut(ofSeconds(5)));


        assertThat(CAUGHT_MESSAGES, mapOf(mapEntry("RabbitMQ message",
                "{\"name\":\"test2\"}")));
    }

    @Test
    public void test6() {
        rabbitMqStepContext.read(rabbitArrayItem("description",
                new TypeReference<DraftDto>() {
                })
                .autoAck()
                .criteria("Name equals fail", ss -> ss.getName().contains("fail"))
                .timeOut(ofSeconds(5)));

        assertThat(CAUGHT_MESSAGES, anEmptyMap());

    }

    @Test
    public void test7() {
        DO_CAPTURES_OF_INSTANCE.accept(SUCCESS);

        rabbitMqStepContext.read(rabbitArrayItem("description",
                new TypeReference<DraftDto>() {
                })
                .autoAck()
                .criteria("Name equals fail", ss -> ss.getName().contains("fail"))
                .timeOut(ofSeconds(5)));

        assertThat(CAUGHT_MESSAGES, mapOf(mapEntry("Read RabbitMQ messages",
                "#1\r\n\r\n{\"name\":\"test1\"}\r\n\r\n#2\r\n\r\n{\"name\":\"test2\"}")));
    }

    @Test
    public void test8() {
        DO_CAPTURES_OF_INSTANCE.accept(FAILURE);

        rabbitMqStepContext.read(rabbitArrayItem("description",
                new TypeReference<DraftDto>() {
                })
                .autoAck()
                .criteria("Name equals fail", ss -> ss.getName().contains("fail"))
                .timeOut(ofSeconds(5)));

        assertThat(CAUGHT_MESSAGES, anEmptyMap());
    }

    @Test
    public void test9() {
        DO_CAPTURES_OF_INSTANCE.accept(SUCCESS_AND_FAILURE);

        rabbitMqStepContext.read(rabbitArrayItem("description",
                new TypeReference<DraftDto>() {
                })
                .autoAck()
                .criteria("Name equals fail", ss -> ss.getName().contains("fail"))
                .timeOut(ofSeconds(5)));

        assertThat(CAUGHT_MESSAGES, mapOf(mapEntry("Read RabbitMQ messages",
                "#1\r\n\r\n{\"name\":\"test1\"}\r\n\r\n#2\r\n\r\n{\"name\":\"test2\"}")));
    }

    @Test
    public void test10() {
        try {
            rabbitMqStepContext.read(rabbitArrayItem("description",
                    new TypeReference<DraftDto>() {
                    })
                    .autoAck()
                    .criteria("Name equals fail", ss -> ss.getName().contains("fail"))
                    .timeOut(ofSeconds(5))
                    .throwOnNoResult());
        } catch (Exception e) {
            assertThat(CAUGHT_MESSAGES, anEmptyMap());
            return;
        }

        fail("Exception was expected");

    }

    @Test
    public void test11() {
        DO_CAPTURES_OF_INSTANCE.accept(SUCCESS);

        try {
            rabbitMqStepContext.read(rabbitArrayItem("description",
                    new TypeReference<DraftDto>() {
                    })
                    .autoAck()
                    .criteria("Name equals fail", ss -> ss.getName().contains("fail"))
                    .timeOut(ofSeconds(5))
                    .throwOnNoResult());
        } catch (Exception e) {
            assertThat(CAUGHT_MESSAGES, anEmptyMap());
            return;
        }

        fail("Exception was expected");

    }

    @Test
    public void test12() {
        DO_CAPTURES_OF_INSTANCE.accept(FAILURE);

        try {
            rabbitMqStepContext.read(rabbitArrayItem("description",
                    new TypeReference<DraftDto>() {
                    })
                    .autoAck()
                    .criteria("Name equals fail", ss -> ss.getName().contains("fail"))
                    .timeOut(ofSeconds(5))
                    .throwOnNoResult());
        } catch (Exception e) {
            assertThat(CAUGHT_MESSAGES.get("Read RabbitMQ messages"),
                    containsString("{\"name\":\"test1\"}"));
            assertThat(CAUGHT_MESSAGES.get("Read RabbitMQ messages"),
                    containsString("{\"name\":\"test2\"}"));
            return;
        }

        fail("Exception was expected");

    }

    @Test
    public void test13() {
        DO_CAPTURES_OF_INSTANCE.accept(SUCCESS_AND_FAILURE);

        try {
            rabbitMqStepContext.read(rabbitArrayItem("description",
                    new TypeReference<DraftDto>() {
                    })
                    .autoAck()
                    .criteria("Name equals fail", ss -> ss.getName().contains("fail"))
                    .timeOut(ofSeconds(5))
                    .throwOnNoResult());
        } catch (Exception e) {
            assertThat(CAUGHT_MESSAGES.get("Read RabbitMQ messages"),
                    containsString("{\"name\":\"test1\"}"));
            assertThat(CAUGHT_MESSAGES.get("Read RabbitMQ messages"),
                    containsString("{\"name\":\"test2\"}"));
            return;
        }

        fail("Exception was expected");

    }
}
