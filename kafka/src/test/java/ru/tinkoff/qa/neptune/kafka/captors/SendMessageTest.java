package ru.tinkoff.qa.neptune.kafka.captors;

import org.testng.annotations.Test;
import ru.tinkoff.qa.neptune.kafka.DraftDto;

import static org.hamcrest.MatcherAssert.assertThat;
import static ru.tinkoff.qa.neptune.core.api.hamcrest.iterables.MapEntryMatcher.mapEntry;
import static ru.tinkoff.qa.neptune.core.api.hamcrest.iterables.SetOfObjectsConsistsOfMatcher.mapOf;
import static ru.tinkoff.qa.neptune.kafka.captors.TestStringInjector.CAUGHT_MESSAGES;

public class SendMessageTest extends BaseCaptorTest {
    @Test
    public void test() {
        kafka.sendMessage("Test", new DraftDto().setName("test"));

        assertThat(CAUGHT_MESSAGES, mapOf(mapEntry("Kafka message", "{\"name\":\"test\"}")));
    }
}
