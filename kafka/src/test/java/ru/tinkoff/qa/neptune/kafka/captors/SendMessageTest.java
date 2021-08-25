package ru.tinkoff.qa.neptune.kafka.captors;

import org.testng.annotations.Test;
import ru.tinkoff.qa.neptune.kafka.DraftDto;
import ru.tinkoff.qa.neptune.kafka.functions.send.KafkaSendRecordsActionSupplier;

import static org.hamcrest.MatcherAssert.assertThat;
import static ru.tinkoff.qa.neptune.core.api.hamcrest.iterables.MapEntryMatcher.mapEntry;
import static ru.tinkoff.qa.neptune.core.api.hamcrest.iterables.SetOfObjectsConsistsOfMatcher.mapOf;
import static ru.tinkoff.qa.neptune.kafka.captors.TestStringInjector.CAUGHT_MESSAGES;

public class SendMessageTest extends BaseCaptorTest {
    @Test
    public void test() {
        kafka.send(KafkaSendRecordsActionSupplier.serializedMessage(new DraftDto().setName("test")).topic("Test"));

        assertThat(CAUGHT_MESSAGES, mapOf(mapEntry("Kafka message", "{\"name\":\"test\"}")));
    }
}
