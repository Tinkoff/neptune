package ru.tinkoff.qa.neptune.kafka.poll;

import org.apache.kafka.common.serialization.StringDeserializer;
import org.testng.annotations.Test;
import ru.tinkoff.qa.neptune.kafka.DraftDto;
import ru.tinkoff.qa.neptune.kafka.KafkaBasePreparations;
import ru.tinkoff.qa.neptune.kafka.SomeDeserializer;

import static java.util.List.of;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static ru.tinkoff.qa.neptune.kafka.functions.poll.KafkaPollIterableItemSupplier.*;
import static ru.tinkoff.qa.neptune.kafka.properties.KafkaDefaultTopicsForPollProperty.DEFAULT_TOPICS_FOR_POLL;

public class PollListItemTest extends KafkaBasePreparations {

    @Test
    public void consumedItemTest() {
        var result = kafka.poll(consumedItem("Some item",
            new SomeDeserializer(),
            new SomeDeserializer(),
            cr -> cr.key().getName() + ";" + cr.value().getName())
            .fromTopics("testTopic"));

        assertThat(result, equalTo("Some Key;Some Value"));
    }

    @Test
    public void consumedItemKeyDataTest() {
        var result = kafka.poll(consumedItemKeyData("Some item",
            new SomeDeserializer(),
            DraftDto::getName)
            .fromTopics("testTopic"));

        assertThat(result, equalTo("Some Key"));
    }

    @Test
    public void consumedKeyTest() {
        var result = kafka.poll(consumedKey(
            new StringDeserializer())
            .fromTopics("testTopic"));

        assertThat(result, equalTo("Some String Key 1"));
    }

    @Test
    public void consumedKeyRawTest() {
        var result = kafka.poll(consumedKey()
            .fromTopics("testTopic"));

        assertThat(result, equalTo("Some String Key 1"));
    }

    @Test
    public void consumedItemValueDataTest() {
        var result = kafka.poll(consumedItemValueData("Some item",
            new SomeDeserializer(),
            DraftDto::getName)
            .fromTopics("testTopic"));

        assertThat(result, equalTo("Some Value"));
    }

    @Test
    public void consumedValueTest() {
        var result = kafka.poll(consumedValue(
            new StringDeserializer())
            .fromTopics("testTopic"));

        assertThat(result, equalTo("Some String Value 1"));
    }

    @Test
    public void consumedValuesRawTest() {
        var result = kafka.poll(consumedValue()
            .fromTopics("testTopic"));

        assertThat(result, equalTo("Some String Value 1"));
    }

    @Test
    public void defineTopicTest() {
        kafka.poll(consumedKey(new StringDeserializer())
            .fromTopics("someTopic", "someTopic2"));

        verify(consumerRaw, times(1)).subscribe(of("someTopic", "someTopic2"));
    }

    @Test
    public void defaultTopicTest() {
        DEFAULT_TOPICS_FOR_POLL.accept("someTopic,someTopic2");
        kafka.poll(consumedKey(new StringDeserializer()));
        verify(consumerRaw, times(1)).subscribe(of("someTopic", "someTopic2"));
    }
}
