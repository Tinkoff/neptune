package ru.tinkoff.qa.neptune.kafka.captors;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.common.TopicPartition;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import ru.tinkoff.qa.neptune.kafka.DraftDto;

import java.util.Map;

import static java.time.Duration.ofNanos;
import static java.time.Duration.ofSeconds;
import static java.util.List.of;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.when;
import static org.testng.Assert.fail;
import static ru.tinkoff.qa.neptune.core.api.hamcrest.iterables.MapEntryMatcher.mapEntry;
import static ru.tinkoff.qa.neptune.core.api.hamcrest.iterables.SetOfObjectsConsistsOfMatcher.arrayInOrder;
import static ru.tinkoff.qa.neptune.core.api.hamcrest.iterables.SetOfObjectsConsistsOfMatcher.mapOf;
import static ru.tinkoff.qa.neptune.core.api.hamcrest.text.StringContainsWithSeparator.withSeparator;
import static ru.tinkoff.qa.neptune.core.api.properties.general.events.CapturedEvents.*;
import static ru.tinkoff.qa.neptune.core.api.properties.general.events.DoCapturesOf.DO_CAPTURES_OF_INSTANCE;
import static ru.tinkoff.qa.neptune.kafka.captors.TestStringInjector.CAUGHT_MESSAGES;
import static ru.tinkoff.qa.neptune.kafka.functions.poll.KafkaPollIterableItemSupplier.kafkaIterableItem;

@SuppressWarnings({"rawtypes", "unchecked"})
public class GetIterableItemCaptorTest extends BaseCaptorTest {
    TopicPartition topicPartition;

    @BeforeClass(dependsOnMethods = "setUp")
    public void beforeClass() {
        topicPartition = new TopicPartition("testTopic", 1);
        ConsumerRecord consumerRecord1 = new ConsumerRecord("testTopic", 1, 0, null, "{\"name\":\"testName1\"}");
        ConsumerRecord consumerRecord2 = new ConsumerRecord("testTopic", 1, 0, null, "{\"name\":\"testName2\"}");

        when(kafkaConsumer.poll(ofNanos(1)))
            .thenReturn(new ConsumerRecords<>(Map.of(topicPartition, of(consumerRecord1, consumerRecord2))));

    }

    @Test
    public void test1() {
        kafka.poll(kafkaIterableItem("kafkaArrayItem",
            DraftDto.class,
            "testTopic"));

        assertThat(CAUGHT_MESSAGES, anEmptyMap());
    }

    @Test
    public void test2() {
        DO_CAPTURES_OF_INSTANCE.accept(SUCCESS);

        var s = kafka.poll(kafkaIterableItem("kafkaArrayItem",
            DraftDto.class,
            "testTopic"));

        assertThat(CAUGHT_MESSAGES, anEmptyMap());
    }

    @Test
    public void test3() {
        DO_CAPTURES_OF_INSTANCE.accept(FAILURE);

        kafka.poll(kafkaIterableItem("kafkaArrayItem",
            DraftDto.class,
            "testTopic"));

        assertThat(CAUGHT_MESSAGES, anEmptyMap());
    }

    @Test
    public void test4() {
        DO_CAPTURES_OF_INSTANCE.accept(SUCCESS_AND_FAILURE);

        kafka.poll(kafkaIterableItem("kafkaArrayItem",
            DraftDto.class,
            "testTopic"));

        assertThat(CAUGHT_MESSAGES, anEmptyMap());
    }

    @Test
    public void test5() {
        kafka.poll(kafkaIterableItem("kafkaArrayItem",
            DraftDto.class,
            "testTopic")
            .criteria("name = 'kek'", d -> d.getName().equals("kek")));

        assertThat(CAUGHT_MESSAGES, anEmptyMap());
    }

    @Test
    public void test6() {
        DO_CAPTURES_OF_INSTANCE.accept(SUCCESS);

        kafka.poll(kafkaIterableItem("kafkaArrayItem",
            DraftDto.class,
            "testTopic")
            .criteria("name = 'kek'", d -> d.getName().equals("kek")));

        assertThat(CAUGHT_MESSAGES,
            mapOf(mapEntry(is("All received Kafka messages"),
                withSeparator(
                    "\r\n\r\n",
                    arrayInOrder(startsWith("ConsumerRecord("), startsWith("ConsumerRecord("))
                ))));
    }

    @Test
    public void test7() {
        DO_CAPTURES_OF_INSTANCE.accept(FAILURE);

        kafka.poll(kafkaIterableItem("kafkaArrayItem",
            DraftDto.class,
            "testTopic")
            .criteria("name = 'kek'", d -> d.getName().equals("kek")));

        assertThat(CAUGHT_MESSAGES, anEmptyMap());
    }

    @Test
    public void test8() {
        DO_CAPTURES_OF_INSTANCE.accept(SUCCESS_AND_FAILURE);

        kafka.poll(kafkaIterableItem("kafkaArrayItem",
            DraftDto.class,
            "testTopic")
            .criteria("name = 'kek'", d -> d.getName().equals("kek")));

        assertThat(CAUGHT_MESSAGES,
            mapOf(mapEntry(is("All received Kafka messages"),
                withSeparator(
                    "\r\n\r\n",
                    arrayInOrder(startsWith("ConsumerRecord("), startsWith("ConsumerRecord("))
                ))));
    }

    @Test
    public void test9() {
        try {
            kafka.poll(kafkaIterableItem("kafkaArrayItem",
                DraftDto.class,
                "testTopic")
                .criteria("name = 'kek'", d -> d.getName().equals("kek"))
                .timeOut(ofSeconds(5))
                .throwOnNoResult());
        } catch (Exception e) {
            assertThat(CAUGHT_MESSAGES, anEmptyMap());
            return;
        }

        fail("Exception was expected");
    }

    @Test
    public void test10() {
        DO_CAPTURES_OF_INSTANCE.accept(SUCCESS);

        try {
            kafka.poll(kafkaIterableItem("kafkaArrayItem",
                DraftDto.class,
                "testTopic")
                .criteria("name = 'kek'", d -> d.getName().equals("kek"))
                .timeOut(ofSeconds(5))
                .throwOnNoResult());
        } catch (Exception e) {
            assertThat(CAUGHT_MESSAGES, anEmptyMap());
            return;
        }

        fail("Exception was expected");
    }

    @Test
    public void test11() {
        DO_CAPTURES_OF_INSTANCE.accept(FAILURE);

        try {
            kafka.poll(kafkaIterableItem("kafkaArrayItem",
                DraftDto.class,
                "testTopic")
                .criteria("name = 'kek'", d -> d.getName().equals("kek"))
                .timeOut(ofSeconds(5))
                .throwOnNoResult());
        } catch (Exception e) {
            assertThat(CAUGHT_MESSAGES, mapOf(mapEntry(
                "All received Kafka messages",
                withSeparator(
                    "\r\n\r\n",
                    arrayInOrder(startsWith("ConsumerRecord("), startsWith("ConsumerRecord("))
                ))));
            return;
        }

        fail("Exception was expected");
    }

    @Test
    public void test12() {
        DO_CAPTURES_OF_INSTANCE.accept(SUCCESS_AND_FAILURE);

        try {
            kafka.poll(kafkaIterableItem("kafkaArrayItem",
                DraftDto.class,
                "testTopic")
                .criteria("name = 'kek'", d -> d.getName().equals("kek"))
                .timeOut(ofSeconds(5))
                .throwOnNoResult());
        } catch (Exception e) {
            assertThat(CAUGHT_MESSAGES, mapOf(mapEntry(
                "All received Kafka messages",
                withSeparator(
                    "\r\n\r\n",
                    arrayInOrder(startsWith("ConsumerRecord("), startsWith("ConsumerRecord("))
                ))));
            return;
        }

        fail("Exception was expected");
    }

}