package ru.tinkoff.qa.neptune.kafka.poll;

import org.apache.kafka.common.serialization.StringDeserializer;
import org.testng.annotations.Test;
import ru.tinkoff.qa.neptune.core.api.steps.Step;
import ru.tinkoff.qa.neptune.kafka.DraftDto;
import ru.tinkoff.qa.neptune.kafka.KafkaBasePreparations;
import ru.tinkoff.qa.neptune.kafka.SomeDeserializer;

import java.util.Date;

import static java.util.List.of;
import static java.util.Objects.isNull;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.apache.kafka.clients.consumer.ConsumerConfig.AUTO_OFFSET_RESET_CONFIG;
import static org.apache.kafka.clients.consumer.ConsumerConfig.GROUP_ID_CONFIG;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.testng.AssertJUnit.fail;
import static ru.tinkoff.qa.neptune.core.api.hamcrest.iterables.MapEntryMatcher.mapEntry;
import static ru.tinkoff.qa.neptune.core.api.hamcrest.iterables.SetOfObjectsConsistsOfMatcher.iterableInOrder;
import static ru.tinkoff.qa.neptune.core.api.hamcrest.iterables.SetOfObjectsIncludesMatcher.mapIncludes;
import static ru.tinkoff.qa.neptune.kafka.functions.poll.KafkaPollIterableSupplier.*;
import static ru.tinkoff.qa.neptune.kafka.properties.KafkaDefaultTopicsForPollProperty.DEFAULT_TOPICS_FOR_POLL;

public class PollListTest extends KafkaBasePreparations {

    @Test
    public void consumedListTest() {
        var result = kafka.poll(consumedList("Some list",
            new SomeDeserializer(),
            new SomeDeserializer(),
            cr -> cr.key().getName() + ";" + cr.value().getName())
            .fromTopics("testTopic"));

        assertThat(result, iterableInOrder("Some Key;Some Value", "Some Key2;Some Value2"));
    }

    @Test
    public void consumedListKeyDataTest() {
        var result = kafka.poll(consumedListKeyData("Some list",
            new SomeDeserializer(),
            DraftDto::getName)
            .fromTopics("testTopic"));

        assertThat(result, iterableInOrder("Some Key", "Some Key2"));
    }

    @Test
    public void consumedKeysTest() {
        var result = kafka.poll(consumedKeys(
            new StringDeserializer())
            .fromTopics("testTopic"));

        assertThat(result, iterableInOrder("Some String Key 1", "Some String Key 2"));
    }

    @Test
    public void consumedKeysRawTest() {
        var result = kafka.poll(consumedKeys()
            .fromTopics("testTopic"));

        assertThat(result, iterableInOrder("Some String Key 1", "Some String Key 2"));
    }

    @Test
    public void consumedListValueDataTest() {
        var result = kafka.poll(consumedListValueData("Some list",
            new SomeDeserializer(),
            DraftDto::getName)
            .fromTopics("testTopic"));

        assertThat(result, iterableInOrder("Some Value", "Some Value2"));
    }

    @Test
    public void consumedValuesTest() {
        var result = kafka.poll(consumedValues(
            new StringDeserializer())
            .fromTopics("testTopic"));

        assertThat(result, iterableInOrder("Some String Value 1", "Some String Value 2"));
    }

    @Test
    public void consumedValuesRawTest() {
        var result = kafka.poll(consumedValues()
            .fromTopics("testTopic"));

        assertThat(result, iterableInOrder("Some String Value 1", "Some String Value 2"));
    }

    @Test
    public void defineTopicTest() {
        kafka.poll(consumedKeys(new StringDeserializer())
            .fromTopics("someTopic", "someTopic2"));

        verify(consumerRaw, times(1)).subscribe(of("someTopic", "someTopic2"));
    }

    @Test
    public void defaultTopicTest() {
        DEFAULT_TOPICS_FOR_POLL.accept("someTopic,someTopic2");
        kafka.poll(consumedKeys(new StringDeserializer()));
        verify(consumerRaw, times(1)).subscribe(of("someTopic", "someTopic2"));
    }

    @Test
    public void defaultAdditionalPropertiesTest() {
        kafka.poll(consumedKeys()
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
        kafka.poll(consumedKeys()
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

        var invocationContainer = new InvocationContainer();
        doAnswer(invocation -> {
            if (isNull(invocationContainer.getPollingInvokedAt())) {
                invocationContainer.setPollingInvokedAt(new Date());
            }
            return RAW_CONSUMER_RECORDS;
        }).when(consumerRaw).poll(any());

        try (var stepClass = mockStatic(Step.class)) {
            stepClass.when(() -> Step.$("Some action", runnable)).thenAnswer(invocation -> {
                invocationContainer.setStepInvokedAt(new Date());
                return null;
            });

            kafka.poll(consumedKeys()
                .fromTopics("testTopic")
                .pollLatestWith("Some action", runnable));

            verify(consumerRaw, atLeast(1)).poll(any());
            stepClass.verify(() -> Step.$("Some action", runnable), times(1));

            assertThat(invocationContainer.getStepInvokedAt().getTime(),
                greaterThanOrEqualTo(invocationContainer.getPollingInvokedAt().getTime())
            );

            assertThat(consumerProps,
                mapIncludes(
                    mapEntry(GROUP_ID_CONFIG, not(nullValue())),
                    mapEntry(AUTO_OFFSET_RESET_CONFIG, "latest")
                ));
        }
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void defineAutoOffsetReset() {
        kafka.poll(consumedKeys()
            .fromTopics("testTopic")
            .setProperty(AUTO_OFFSET_RESET_CONFIG, "some value"));
        fail("Exception was expected");
    }
}

