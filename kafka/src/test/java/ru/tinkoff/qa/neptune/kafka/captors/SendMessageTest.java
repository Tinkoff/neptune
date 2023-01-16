package ru.tinkoff.qa.neptune.kafka.captors;

import org.testng.annotations.Test;
import ru.tinkoff.qa.neptune.kafka.DraftDto;
import ru.tinkoff.qa.neptune.kafka.SomeSerializer;

import static org.hamcrest.MatcherAssert.assertThat;
import static ru.tinkoff.qa.neptune.core.api.hamcrest.iterables.MapEntryMatcher.mapEntry;
import static ru.tinkoff.qa.neptune.core.api.hamcrest.iterables.SetOfObjectsConsistsOfMatcher.mapOf;
import static ru.tinkoff.qa.neptune.kafka.captors.TestStringInjector.CAUGHT_MESSAGES;
import static ru.tinkoff.qa.neptune.kafka.functions.send.KafkaSendRecordsActionSupplier.producerRecord;

public class SendMessageTest extends BaseCaptorTest {

    @Test
    public void test() {
        kafka.send(producerRecord(new SomeSerializer(),
            new DraftDto().setName("test"))
            .topic("Test"));

        assertThat(CAUGHT_MESSAGES, mapOf(mapEntry("Producer Record Value", "{\n" +
            "  \"name\": \"test\"\n" +
            "}")));
    }

    @Test
    public void test2() {
        kafka.send(producerRecord(new SomeSerializer(),
            new DraftDto().setName("test"))
            .setKey("Some key")
            .topic("Test"));

        assertThat(CAUGHT_MESSAGES, mapOf(
            mapEntry("Producer Record Key", "Some key"),
            mapEntry("Producer Record Value", "{\n" +
                "  \"name\": \"test\"\n" +
                "}")));
    }
}
