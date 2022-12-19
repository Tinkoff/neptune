package ru.tinkoff.qa.neptune.kafka.captors;

import com.fasterxml.jackson.core.type.TypeReference;
import org.testng.annotations.Test;
import ru.tinkoff.qa.neptune.kafka.DraftDto;

import static java.time.Duration.ofSeconds;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anEmptyMap;
import static org.testng.Assert.fail;
import static ru.tinkoff.qa.neptune.core.api.properties.general.events.CapturedEvents.*;
import static ru.tinkoff.qa.neptune.core.api.properties.general.events.DoCapturesOf.DO_CAPTURES_OF_INSTANCE;
import static ru.tinkoff.qa.neptune.kafka.captors.TestStringInjector.CAUGHT_MESSAGES;
import static ru.tinkoff.qa.neptune.kafka.functions.poll.KafkaPollArraySupplier.kafkaArray;

public class GetArrayCaptorTest extends BaseCaptorTest {

    @Test
    public void test1() {
        kafka.poll(kafkaArray("test description",
            new TypeReference<DraftDto>() {
            },
            "testTopic"));

        assertThat(CAUGHT_MESSAGES, anEmptyMap());
    }

    @Test
    public void test2() {
        DO_CAPTURES_OF_INSTANCE.accept(SUCCESS);

        var result = kafka.poll(kafkaArray("test description",
            new TypeReference<DraftDto>() {
            },
            "testTopic"));

        assertThat(CAUGHT_MESSAGES, not(anEmptyMap()));
    }

    @Test
    public void test3() {
        DO_CAPTURES_OF_INSTANCE.accept(FAILURE);

        kafka.poll(kafkaArray("test description",
            new TypeReference<DraftDto>() {
            },
            "testTopic"));

        assertThat(CAUGHT_MESSAGES, anEmptyMap());
    }


    @Test
    public void test4() {
        DO_CAPTURES_OF_INSTANCE.accept(SUCCESS_AND_FAILURE);

        kafka.poll(kafkaArray("test description",
            new TypeReference<DraftDto>() {
            },
            "testTopic"));

        assertThat(CAUGHT_MESSAGES, not(anEmptyMap()));
    }

    @Test
    public void test5() {
        kafka.poll(kafkaArray("test description",
            new TypeReference<DraftDto>() {
            },
            "testTopic")
            .criteria("Contains name = 'fail'", d -> d.getName().equals("fail")));

        assertThat(CAUGHT_MESSAGES, anEmptyMap());
    }

    @Test
    public void test6() {
        DO_CAPTURES_OF_INSTANCE.accept(SUCCESS);

        kafka.poll(kafkaArray("test description",
            new TypeReference<DraftDto>() {
            },
            "testTopic")
            .criteria("Contains name = 'fail'", d -> d.getName().equals("fail")));

        assertThat(CAUGHT_MESSAGES, anEmptyMap());
    }

    @Test
    public void test7() {
        DO_CAPTURES_OF_INSTANCE.accept(FAILURE);

        kafka.poll(kafkaArray("test description",
            new TypeReference<DraftDto>() {
            },
            "testTopic")
            .criteria("Contains name = 'fail'", d -> d.getName().equals("fail")));

        assertThat(CAUGHT_MESSAGES, anEmptyMap());
    }

    @Test
    public void test8() {
        DO_CAPTURES_OF_INSTANCE.accept(SUCCESS_AND_FAILURE);

        kafka.poll(kafkaArray("test description",
            new TypeReference<DraftDto>() {
            },
            "testTopic")
            .criteria("Contains name = 'fail'", d -> d.getName().equals("fail"))
            .timeOut(ofSeconds(5)));

        assertThat(CAUGHT_MESSAGES, anEmptyMap());
    }

    @Test
    public void test9() {
        try {
            kafka.poll(kafkaArray("TestDescription",
                new TypeReference<>() {
                },
                String.class,
                DraftDto::getName,
                "testTopic")
                .criteria("Value contains 'test", s -> s.contains("fail"))
                .timeOut(ofSeconds(5))
                .throwOnNoResult());
        } catch (Exception e) {
            assertThat(CAUGHT_MESSAGES, anEmptyMap());
            return;
        }

        fail("Exception was expected");
    }


    @Test
    public void test10() {
        DO_CAPTURES_OF_INSTANCE.accept(SUCCESS);

        try {
            kafka.poll(kafkaArray("TestDescription",
                new TypeReference<>() {
                },
                String.class,
                DraftDto::getName,
                "testTopic")
                .criteria("Value contains 'test", s -> s.contains("fail"))
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
        DO_CAPTURES_OF_INSTANCE.accept(FAILURE);

        try {
            kafka.poll(kafkaArray("TestDescription",
                new TypeReference<>() {
                },
                String.class, DraftDto::getName, "testTopic")
                .criteria("Value contains 'test", s -> s.contains("fail"))
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
            kafka.poll(kafkaArray("TestDescription",
                new TypeReference<>() {
                },
                String.class, DraftDto::getName, "testTopic")
                .criteria("Value contains 'test", s -> s.contains("fail"))
                .timeOut(ofSeconds(5))
                .throwOnNoResult());
        } catch (Exception e) {
            assertThat(CAUGHT_MESSAGES, anEmptyMap());
            return;
        }

        fail("Exception was expected");
    }
}
