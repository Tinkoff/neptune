package ru.tinkoff.qa.neptune.kafka.captors;

import org.testng.annotations.Test;

import static java.time.Duration.ofSeconds;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anEmptyMap;
import static org.testng.Assert.fail;
import static ru.tinkoff.qa.neptune.core.api.properties.general.events.CapturedEvents.*;
import static ru.tinkoff.qa.neptune.core.api.properties.general.events.DoCapturesOf.DO_CAPTURES_OF_INSTANCE;
import static ru.tinkoff.qa.neptune.kafka.captors.TestStringInjector.CAUGHT_MESSAGES;
import static ru.tinkoff.qa.neptune.kafka.functions.poll.GetRecordSupplier.consumerRecords;

public class GetRecordsCaptorTest extends BaseCaptorTest {

    @Test
    public void test1() {
        kafka.poll(consumerRecords("TestDescription", "testTopic"));
        assertThat(CAUGHT_MESSAGES, anEmptyMap());
    }

    @Test
    public void test2() {
        DO_CAPTURES_OF_INSTANCE.accept(SUCCESS);
        kafka.poll(consumerRecords("test description", "testTopic"));
        assertThat(CAUGHT_MESSAGES, not(anEmptyMap()));
    }

    @Test
    public void test3() {
        DO_CAPTURES_OF_INSTANCE.accept(FAILURE);
        kafka.poll(consumerRecords("test description", "testTopic"));
        assertThat(CAUGHT_MESSAGES, anEmptyMap());
    }

    @Test
    public void test4() {
        DO_CAPTURES_OF_INSTANCE.accept(SUCCESS_AND_FAILURE);
        kafka.poll(consumerRecords("test description", "testTopic"));
        assertThat(CAUGHT_MESSAGES, not(anEmptyMap()));
    }

    @Test
    public void test5() {
        kafka.poll(consumerRecords("test description", "testTopic")
            .criteria("Some Criteria", r -> r.value().equals("Some Value")));
        assertThat(CAUGHT_MESSAGES, anEmptyMap());
    }

    @Test
    public void test6() {
        DO_CAPTURES_OF_INSTANCE.accept(SUCCESS);
        kafka.poll(consumerRecords("test description", "testTopic")
            .criteria("Some Criteria", r -> r.value().equals("Some Value")));
        assertThat(CAUGHT_MESSAGES, anEmptyMap());
    }

    @Test
    public void test7() {
        DO_CAPTURES_OF_INSTANCE.accept(FAILURE);
        kafka.poll(consumerRecords("test description", "testTopic")
            .criteria("Some Criteria", r -> r.value().equals("Some Value")));
        assertThat(CAUGHT_MESSAGES, anEmptyMap());
    }

    @Test
    public void test9() {
        DO_CAPTURES_OF_INSTANCE.accept(SUCCESS_AND_FAILURE);
        kafka.poll(consumerRecords("test description", "testTopic")
            .criteria("Some Criteria", r -> r.value().equals("Some Value")));
        assertThat(CAUGHT_MESSAGES, anEmptyMap());
    }

    @Test
    public void test10() {
        DO_CAPTURES_OF_INSTANCE.accept(SUCCESS);

        try {
            kafka.poll(consumerRecords("test description", "testTopic")
                .criteria("Some Criteria", r -> r.value().equals("Some Value"))
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
            kafka.poll(consumerRecords("test description", "testTopic")
                .criteria("Some Criteria", r -> r.value().equals("Some Value"))
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
        DO_CAPTURES_OF_INSTANCE.accept(SUCCESS_AND_FAILURE);

        try {
            kafka.poll(consumerRecords("test description", "testTopic")
                .criteria("Some Criteria", r -> r.value().equals("Some Value"))
                .timeOut(ofSeconds(5))
                .throwOnNoResult());
        } catch (Exception e) {
            assertThat(CAUGHT_MESSAGES, anEmptyMap());
            return;
        }

        fail("Exception was expected");
    }
}
