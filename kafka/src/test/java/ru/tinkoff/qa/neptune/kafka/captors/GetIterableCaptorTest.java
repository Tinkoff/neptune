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
import static ru.tinkoff.qa.neptune.kafka.functions.poll.KafkaPollIterableSupplier.consumedList;

public class GetIterableCaptorTest extends BaseCaptorTest {

    @Test(dataProvider = "resultData")
    public void positiveTest(CapturedEvents toMakeCaptureOn,
                             Matcher<Map<String, String>> matcher) {

        DO_CAPTURES_OF_INSTANCE.accept(toMakeCaptureOn);

        kafka.poll(consumedList("Some list",
            new SomeDeserializer(),
            new SomeDeserializer(),
            cs -> cs.value().getName())
            .criteria("Contains '\"Some Value\"'", s -> s.contains("Some Value"))
            .fromTopics("testTopic"));

        assertThat(CAUGHT_MESSAGES, matcher);
    }

    @Test(dataProvider = "noResultData")
    public void emptyResultTest(CapturedEvents toMakeCaptureOn) {
        DO_CAPTURES_OF_INSTANCE.accept(toMakeCaptureOn);

        kafka.poll(consumedList("Some list",
            new SomeDeserializer(),
            new SomeDeserializer(),
            cs -> cs.value().getName())
            .criteria("Contains '\"Some Value\"'", s -> s.contains("Any Value"))
            .fromTopics("testTopic"));

        assertThat(CAUGHT_MESSAGES, anEmptyMap());
    }

    @Test(dataProvider = "noResultData")
    public void negativeTest(CapturedEvents toMakeCaptureOn) {
        DO_CAPTURES_OF_INSTANCE.accept(toMakeCaptureOn);

        try {
            kafka.poll(consumedList("Some list",
                new SomeDeserializer(),
                new SomeDeserializer(),
                cs -> cs.value().getName())
                .criteria("Contains '\"Some Value\"'", s -> s.contains("Any Value"))
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
