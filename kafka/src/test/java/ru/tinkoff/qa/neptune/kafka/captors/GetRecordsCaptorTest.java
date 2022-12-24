package ru.tinkoff.qa.neptune.kafka.captors;

import org.hamcrest.Matcher;
import org.testng.annotations.Test;
import ru.tinkoff.qa.neptune.core.api.properties.general.events.CapturedEvents;
import ru.tinkoff.qa.neptune.kafka.SomeDeserializer;

import java.util.Map;

import static java.time.Duration.ofSeconds;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anEmptyMap;
import static org.testng.Assert.fail;
import static ru.tinkoff.qa.neptune.core.api.properties.general.events.DoCapturesOf.DO_CAPTURES_OF_INSTANCE;
import static ru.tinkoff.qa.neptune.kafka.captors.TestStringInjector.CAUGHT_MESSAGES;
import static ru.tinkoff.qa.neptune.kafka.functions.poll.GetRecordSupplier.consumerRecords;

public class GetRecordsCaptorTest extends BaseCaptorTest {

    @Test(dataProvider = "resultData")
    public void positiveTestRecords(CapturedEvents toMakeCaptureOn,
                                    Matcher<Map<String, String>> matcher) {
        DO_CAPTURES_OF_INSTANCE.accept(toMakeCaptureOn);
        kafka.poll(consumerRecords().fromTopics("testTopic"));
        assertThat(CAUGHT_MESSAGES, matcher);
    }

    @Test(dataProvider = "noResultData")
    public void emptyResultTest(CapturedEvents toMakeCaptureOn) {
        DO_CAPTURES_OF_INSTANCE.accept(toMakeCaptureOn);

        kafka.poll(consumerRecords(
            new SomeDeserializer(),
            new SomeDeserializer())
            .criteria("Key and value contain '\"Fail\"'", cr ->
                cr.key().getName().contains("Fail") && cr.value().getName().contains("Fail"))
            .fromTopics("testTopic"));

        assertThat(CAUGHT_MESSAGES, anEmptyMap());
    }

    @Test(dataProvider = "noResultData")
    public void negativeTest(CapturedEvents toMakeCaptureOn) {
        DO_CAPTURES_OF_INSTANCE.accept(toMakeCaptureOn);

        try {
            kafka.poll(consumerRecords(
                new SomeDeserializer(),
                new SomeDeserializer())
                .criteria("Key and value contain '\"Fail\"'", cr ->
                    cr.key().getName().contains("Fail") && cr.value().getName().contains("Fail"))
                .fromTopics("testTopic")
                .timeOut(ofSeconds(5))
                .throwOnNoResult());
        } catch (Exception e) {
            assertThat(CAUGHT_MESSAGES, anEmptyMap());
            return;
        }

        fail("Exception was expected");
    }
}
