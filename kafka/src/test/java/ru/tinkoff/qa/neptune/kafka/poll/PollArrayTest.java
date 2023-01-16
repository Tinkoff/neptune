package ru.tinkoff.qa.neptune.kafka.poll;

import com.fasterxml.jackson.core.type.TypeReference;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import ru.tinkoff.qa.neptune.kafka.DraftDto;
import ru.tinkoff.qa.neptune.kafka.KafkaBasePreparations;
import ru.tinkoff.qa.neptune.kafka.SomeDeserializer;

import static java.util.List.of;
import static java.util.Objects.nonNull;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.apache.kafka.clients.consumer.ConsumerConfig.GROUP_ID_CONFIG;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasEntry;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static ru.tinkoff.qa.neptune.core.api.hamcrest.iterables.SetOfObjectsConsistsOfMatcher.arrayInOrder;
import static ru.tinkoff.qa.neptune.kafka.functions.poll.KafkaPollArraySupplier.*;
import static ru.tinkoff.qa.neptune.kafka.properties.KafkaDefaultTopicsForPollProperty.DEFAULT_TOPICS_FOR_POLL;

@SuppressWarnings("unchecked")
public class PollArrayTest extends KafkaBasePreparations {

    @DataProvider
    public static Object[][] data() {
        return new Object[][]{
            {String.class},
            {new TypeReference<String>() {
            }}
        };
    }

    @Test(dataProvider = "data")
    public void consumeArrayFullParametersTest(Object param) {
        Class<String> strClas = param instanceof Class<?> ? (Class<String>) param : null;
        TypeReference<String> strType = param instanceof TypeReference ? (TypeReference<String>) param : null;

        String[] array;

        if (nonNull(strClas)) {
            array = kafka.poll(consumedArray("Some array",
                new StringDeserializer(),
                new SomeDeserializer(),
                strClas,
                cr -> cr.value().getName())
                .fromTopics("testTopic"));
        } else {
            array = kafka.poll(consumedArray("Some array",
                new StringDeserializer(),
                new SomeDeserializer(),
                strType,
                cr -> cr.value().getName())
                .fromTopics("testTopic"));
        }

        assertThat(array, arrayInOrder("Some Value", "Some Value2"));
    }

    @Test(dataProvider = "data")
    public void consumedArrayKeyDataTest(Object param) {
        Class<String> strClas = param instanceof Class<?> ? (Class<String>) param : null;
        TypeReference<String> strType = param instanceof TypeReference ? (TypeReference<String>) param : null;

        String[] array;

        if (nonNull(strClas)) {
            array = kafka.poll(consumedArrayKeyData("Some array",
                new SomeDeserializer(),
                strClas,
                DraftDto::getName)
                .fromTopics("testTopic"));
        } else {
            array = kafka.poll(consumedArrayKeyData("Some array",
                new SomeDeserializer(),
                strType,
                DraftDto::getName)
                .fromTopics("testTopic"));
        }

        assertThat(array, arrayInOrder("Some Key", "Some Key2"));
    }

    @Test(dataProvider = "data")
    public void consumedArrayKeysTest(Object param) {
        Class<String> strClas = param instanceof Class<?> ? (Class<String>) param : null;
        TypeReference<String> strType = param instanceof TypeReference ? (TypeReference<String>) param : null;

        String[] array;

        if (nonNull(strClas)) {
            array = kafka.poll(consumedArrayKeys(strClas,
                new StringDeserializer())
                .fromTopics("testTopic"));
        } else {
            array = kafka.poll(consumedArrayKeys(strType,
                new StringDeserializer())
                .fromTopics("testTopic"));
        }

        assertThat(array, arrayInOrder("Some String Key 1", "Some String Key 2"));
    }

    @Test
    public void consumedArrayRawKeys() {
        var array = kafka.poll(consumedArrayKeys()
            .fromTopics("testTopic"));
        assertThat(array, arrayInOrder("Some String Key 1", "Some String Key 2"));
    }

    @Test(dataProvider = "data")
    public void consumedArrayValueDataTest(Object param) {
        Class<String> strClas = param instanceof Class<?> ? (Class<String>) param : null;
        TypeReference<String> strType = param instanceof TypeReference ? (TypeReference<String>) param : null;

        String[] array;

        if (nonNull(strClas)) {
            array = kafka.poll(consumedArrayValueData("Some array",
                new SomeDeserializer(),
                strClas,
                DraftDto::getName)
                .fromTopics("testTopic"));
        } else {
            array = kafka.poll(consumedArrayValueData("Some array",
                new SomeDeserializer(),
                strType,
                DraftDto::getName)
                .fromTopics("testTopic"));
        }

        assertThat(array, arrayInOrder("Some Value", "Some Value2"));
    }

    @Test(dataProvider = "data")
    public void consumedArrayValuesTest(Object param) {
        Class<String> strClas = param instanceof Class<?> ? (Class<String>) param : null;
        TypeReference<String> strType = param instanceof TypeReference ? (TypeReference<String>) param : null;

        String[] array;

        if (nonNull(strClas)) {
            array = kafka.poll(consumedArrayValues(strClas,
                new StringDeserializer())
                .fromTopics("testTopic"));
        } else {
            array = kafka.poll(consumedArrayValues(strType,
                new StringDeserializer())
                .fromTopics("testTopic"));
        }

        assertThat(array, arrayInOrder("Some String Value 1", "Some String Value 2"));
    }

    @Test
    public void consumedArrayRawValues() {
        var array = kafka.poll(consumedArrayValues()
            .fromTopics("testTopic"));
        assertThat(array, arrayInOrder("Some String Value 1", "Some String Value 2"));
    }

    @Test
    public void defineTopicTest() {
        kafka.poll(consumedArrayValues()
            .fromTopics("someTopic", "someTopic2"));

        verify(consumerRaw, times(1)).subscribe(of("someTopic", "someTopic2"));
    }

    @Test
    public void defaultTopicTest() {
        DEFAULT_TOPICS_FOR_POLL.accept("someTopic,someTopic2");
        kafka.poll(consumedArrayValues());
        verify(consumerRaw, times(1)).subscribe(of("someTopic", "someTopic2"));
    }

    @Test
    public void defineAdditionalProperty() {
        var groupId = randomAlphabetic(20);
        kafka.poll(consumedArrayValues()
            .fromTopics("testTopic")
            .setProperty(GROUP_ID_CONFIG, groupId));

        assertThat(consumerProps, hasEntry(
            equalTo(GROUP_ID_CONFIG),
            equalTo(groupId)));
    }
}
