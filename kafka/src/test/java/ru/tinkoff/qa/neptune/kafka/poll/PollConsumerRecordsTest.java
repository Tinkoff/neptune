package ru.tinkoff.qa.neptune.kafka.poll;

import org.testng.annotations.Test;
import ru.tinkoff.qa.neptune.kafka.DraftDto;
import ru.tinkoff.qa.neptune.kafka.KafkaBasePreparations;
import ru.tinkoff.qa.neptune.kafka.SomeDeserializer;

import static java.time.Duration.ofSeconds;
import static java.util.List.of;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static ru.tinkoff.qa.neptune.core.api.hamcrest.iterables.SetOfObjectsConsistsOfMatcher.iterableOf;
import static ru.tinkoff.qa.neptune.core.api.hamcrest.iterables.SetOfObjectsEachItemMatcher.eachOfIterable;
import static ru.tinkoff.qa.neptune.core.api.hamcrest.pojo.PojoGetterReturnsMatcher.getterReturns;
import static ru.tinkoff.qa.neptune.kafka.functions.poll.GetRecordSupplier.consumerRecords;
import static ru.tinkoff.qa.neptune.kafka.properties.KafkaDefaultTopicsForPollProperty.DEFAULT_TOPICS_FOR_POLL;

public class PollConsumerRecordsTest extends KafkaBasePreparations {

    @Test
    public void pollStringKeyValueRecords() {
        var consumedRecords = kafka.poll(consumerRecords()
            .fromTopics("testTopic"));
        assertThat(consumedRecords, hasSize(2));
        assertThat(consumedRecords, eachOfIterable(getterReturns("key", instanceOf(String.class))));
        assertThat(consumedRecords, eachOfIterable(getterReturns("value", instanceOf(String.class))));
    }

    @Test
    public void pollDeserializedKeyValueRecords() {
        var consumedRecords = kafka.poll(consumerRecords(
            new SomeDeserializer(),
            new SomeDeserializer())
            .fromTopics("testTopic"));
        assertThat(consumedRecords, hasSize(2));
        assertThat(consumedRecords, eachOfIterable(getterReturns("key", instanceOf(DraftDto.class))));
        assertThat(consumedRecords, eachOfIterable(getterReturns("value", instanceOf(DraftDto.class))));
    }

    @Test
    public void defineTopicTest() {
        kafka.poll(consumerRecords()
            .fromTopics("someTopic", "someTopic2"));

        verify(consumerRaw, times(1)).subscribe(of("someTopic", "someTopic2"));
    }

    @Test
    public void defaultTopicTest() {
        DEFAULT_TOPICS_FOR_POLL.accept("someTopic,someTopic2");
        kafka.poll(consumerRecords());
        verify(consumerRaw, times(1)).subscribe(of("someTopic", "someTopic2"));
    }

    @Test
    public void getListTest() {
        var names = kafka.poll(consumerRecords(new SomeDeserializer(),
            new SomeDeserializer())
            .fromTopics("testTopic")
            .criteria("Has value contains \"Some Value\"", cr -> cr.value().getName().contains("Some Value"))
            .timeOut(ofSeconds(5))
            .thenGetList("Collected names", cr -> cr.value().getName())
            .criteria("Has value == \"Some Value\"", s -> s.equals("Some Value")));

        assertThat(names, iterableOf("Some Value"));
    }

    @Test
    public void getItemTest() {
        var names = kafka.poll(consumerRecords(new SomeDeserializer(),
            new SomeDeserializer())
            .fromTopics("testTopic")
            .criteria("Has value contains \"Some Value\"", cr -> cr.value().getName().contains("Some Value"))
            .timeOut(ofSeconds(5))
            .thenGetItem("Collected name", cr -> cr.value().getName())
            .criteria("Has value == \"Some Value\"", s -> s.equals("Some Value")));

        assertThat(names, equalTo("Some Value"));
    }
}
