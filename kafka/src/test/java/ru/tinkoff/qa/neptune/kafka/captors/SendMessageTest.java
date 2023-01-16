package ru.tinkoff.qa.neptune.kafka.captors;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import ru.tinkoff.qa.neptune.kafka.DraftDto;

import static org.hamcrest.MatcherAssert.assertThat;
import static ru.tinkoff.qa.neptune.core.api.hamcrest.iterables.MapEntryMatcher.mapEntry;
import static ru.tinkoff.qa.neptune.core.api.hamcrest.iterables.SetOfObjectsConsistsOfMatcher.mapOf;
import static ru.tinkoff.qa.neptune.kafka.captors.TestStringInjector.CAUGHT_MESSAGES;
import static ru.tinkoff.qa.neptune.kafka.functions.send.KafkaSendRecordsActionSupplier.kafkaSerializedMessage;

public class SendMessageTest extends BaseCaptorTest {


    @BeforeClass(dependsOnMethods = "setUp")
    @Override
    public void beforeClass() {
    }

    @Test
    public void test() {
        kafka.send(kafkaSerializedMessage(new DraftDto().setName("test")).topic("Test"));

        assertThat(CAUGHT_MESSAGES, mapOf(mapEntry("Producer Record Value", "{\"name\":\"test\"}")));
    }

    @Test
    public void test2() {
        kafka.send(kafkaSerializedMessage(new DraftDto().setName("test"))
            .key("Some key")
            .topic("Test"));

        assertThat(CAUGHT_MESSAGES, mapOf(
            mapEntry("Producer Record Key", "Some key"),
            mapEntry("Producer Record Value", "{\"name\":\"test\"}")));
    }
}
