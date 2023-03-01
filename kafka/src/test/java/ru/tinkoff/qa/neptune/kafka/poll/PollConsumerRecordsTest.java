package ru.tinkoff.qa.neptune.kafka.poll;

import org.testng.annotations.Test;
import ru.tinkoff.qa.neptune.core.api.steps.Step;
import ru.tinkoff.qa.neptune.kafka.DraftDto;
import ru.tinkoff.qa.neptune.kafka.KafkaBasePreparations;
import ru.tinkoff.qa.neptune.kafka.SomeDeserializer;

import static java.time.Duration.ofSeconds;
import static java.util.List.of;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.apache.kafka.clients.consumer.ConsumerConfig.AUTO_OFFSET_RESET_CONFIG;
import static org.apache.kafka.clients.consumer.ConsumerConfig.GROUP_ID_CONFIG;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.*;
import static ru.tinkoff.qa.neptune.core.api.hamcrest.iterables.MapEntryMatcher.mapEntry;
import static ru.tinkoff.qa.neptune.core.api.hamcrest.iterables.SetOfObjectsConsistsOfMatcher.iterableInOrder;
import static ru.tinkoff.qa.neptune.core.api.hamcrest.iterables.SetOfObjectsConsistsOfMatcher.iterableOf;
import static ru.tinkoff.qa.neptune.core.api.hamcrest.iterables.SetOfObjectsIncludesMatcher.mapIncludes;
import static ru.tinkoff.qa.neptune.core.api.hamcrest.pojo.PojoGetterReturnsMatcher.getterReturns;
import static ru.tinkoff.qa.neptune.kafka.functions.poll.GetRecordSupplier.consumerRecords;
import static ru.tinkoff.qa.neptune.kafka.properties.KafkaDefaultTopicsForPollProperty.DEFAULT_TOPICS_FOR_POLL;

public class PollConsumerRecordsTest extends KafkaBasePreparations {

    @Test
    public void pollStringKeyValueRecords() {
        var consumedRecords = kafka.poll(consumerRecords()
            .fromTopics("testTopic"));
        assertThat(consumedRecords, iterableInOrder(
            getterReturns("key", instanceOf(String.class)),
            getterReturns("key", instanceOf(String.class)),
            getterReturns("key", nullValue())));
        assertThat(consumedRecords, iterableInOrder(
            getterReturns("value", instanceOf(String.class)),
            getterReturns("value", instanceOf(String.class)),
            getterReturns("value", nullValue())));
    }

    @Test
    public void pollDeserializedKeyValueRecords() {
        var consumedRecords = kafka.poll(consumerRecords(
            new SomeDeserializer(),
            new SomeDeserializer())
            .fromTopics("testTopic"));
        assertThat(consumedRecords, iterableInOrder(
            getterReturns("key", instanceOf(DraftDto.class)),
            getterReturns("key", instanceOf(DraftDto.class)),
            getterReturns("key", nullValue())));
        assertThat(consumedRecords, iterableInOrder(
            getterReturns("value", instanceOf(DraftDto.class)),
            getterReturns("value", instanceOf(DraftDto.class)),
            getterReturns("value", nullValue())));
    }

    @Test
    public void pollWithExclusionOfNullKeysAndValues() {
        var consumedRecords = kafka.poll(consumerRecords(
            new SomeDeserializer(),
            new SomeDeserializer())
            .fromTopics("testTopic")
            .excludeWithNullValues()
            .excludeWithNullKeys());
        assertThat(consumedRecords, iterableInOrder(
            getterReturns("key", instanceOf(DraftDto.class)),
            getterReturns("key", instanceOf(DraftDto.class))));
        assertThat(consumedRecords, iterableInOrder(
            getterReturns("value", instanceOf(DraftDto.class)),
            getterReturns("value", instanceOf(DraftDto.class))));
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
        var names = kafka.poll(consumerRecords(
            new SomeDeserializer(),
            new SomeDeserializer())
            .fromTopics("testTopic")
            .criteria("Has value contains \"Some Value\"", cr -> cr.value().getName().contains("Some Value"))
            .timeOut(ofSeconds(5))
            .thenGetItem("Collected name", cr -> cr.value().getName())
            .criteria("Has value == \"Some Value\"", s -> s.equals("Some Value")));

        assertThat(names, equalTo("Some Value"));
    }

    @Test
    public void defaultAdditionalPropertiesTest() {
        kafka.poll(consumerRecords()
            .fromTopics("testTopic"));

        assertThat(consumerProps,
            mapIncludes(
                mapEntry(GROUP_ID_CONFIG, not(nullValue())),
                mapEntry(AUTO_OFFSET_RESET_CONFIG, "earliest")
            ));
    }

    @Test
    public void defineAdditionalProperty() {
        var groupId = randomAlphabetic(20);
        kafka.poll(consumerRecords()
            .fromTopics("testTopic")
            .setProperty(GROUP_ID_CONFIG, groupId));

        assertThat(consumerProps,
            mapIncludes(
                mapEntry(GROUP_ID_CONFIG, groupId),
                mapEntry(AUTO_OFFSET_RESET_CONFIG, "earliest")
            ));
    }

    @Test
    public void defineRunnableTest() {
        var runnable = new Runnable() {
            @Override
            public void run() {

            }
        };

        try (var stepClass = mockStatic(Step.class)) {
            kafka.poll(consumerRecords()
                .fromTopics("testTopic")
                .pollWith("Some action", runnable));

            var order = inOrder(consumerRaw, Step.class);

            order.verify(consumerRaw, atLeast(1)).poll(any());
            order.verify(stepClass,
                () -> Step.$("Some action", runnable),
                times(1));

            assertThat(consumerProps,
                mapIncludes(
                    mapEntry(GROUP_ID_CONFIG, not(nullValue())),
                    mapEntry(AUTO_OFFSET_RESET_CONFIG, "latest")
                ));
        }
    }
}
