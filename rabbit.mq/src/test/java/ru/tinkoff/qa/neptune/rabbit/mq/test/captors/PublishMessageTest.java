package ru.tinkoff.qa.neptune.rabbit.mq.test.captors;

import org.testng.annotations.Test;
import ru.tinkoff.qa.neptune.rabbit.mq.test.DraftDto;

import static org.hamcrest.MatcherAssert.assertThat;
import static ru.tinkoff.qa.neptune.core.api.hamcrest.iterables.MapEntryMatcher.mapEntry;
import static ru.tinkoff.qa.neptune.core.api.hamcrest.iterables.SetOfObjectsConsistsOfMatcher.mapOf;
import static ru.tinkoff.qa.neptune.rabbit.mq.test.captors.TestStringInjector.CAUGHT_MESSAGES;

public class PublishMessageTest extends BaseCaptorTest {

    @Test
    public void test() {
        rabbitMqStepContext.publishMessage("exchange1",
                "routingKey1",
                new DraftDto().setName("test"));

        assertThat(CAUGHT_MESSAGES, mapOf(mapEntry("RabbitMQ message", "{\"name\":\"test\"}")));
    }
}
