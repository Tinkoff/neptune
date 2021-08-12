package ru.tinkoff.qa.neptune.kafka.captors;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.TopicPartition;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import ru.tinkoff.qa.neptune.kafka.DraftDto;

import java.util.Map;

import static java.time.Duration.ofNanos;
import static java.time.Duration.ofSeconds;
import static java.util.List.of;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anEmptyMap;
import static org.mockito.Mockito.when;
import static org.testng.Assert.fail;
import static ru.tinkoff.qa.neptune.core.api.hamcrest.iterables.MapEntryMatcher.mapEntry;
import static ru.tinkoff.qa.neptune.core.api.hamcrest.iterables.SetOfObjectsConsistsOfMatcher.mapOf;
import static ru.tinkoff.qa.neptune.core.api.properties.general.events.CapturedEvents.*;
import static ru.tinkoff.qa.neptune.core.api.properties.general.events.DoCapturesOf.DO_CAPTURES_OF_INSTANCE;
import static ru.tinkoff.qa.neptune.kafka.captors.TestStringInjector.CAUGHT_MESSAGES;
import static ru.tinkoff.qa.neptune.kafka.functions.poll.KafkaPollIterableItemSupplier.kafkaIterableItem;

public class GetIterableItemCaptorTest extends BaseCaptorTest {

    KafkaConsumer<Object, Object> consumer;
    TopicPartition topicPartition;

    @BeforeClass(dependsOnMethods = "setUp")
    public void beforeClass() {
        consumer = kafka.getConsumer();
        topicPartition = new TopicPartition("testTopic", 1);
        ConsumerRecord consumerRecord1 = new ConsumerRecord("testTopic", 1, 0, null, "{\"name\":\"testName1\"}");
        ConsumerRecord consumerRecord2 = new ConsumerRecord("testTopic", 1, 0, null, "{\"name\":\"testName2\"}");

        when(consumer.poll(ofNanos(1)))
                .thenReturn(new ConsumerRecords<>(Map.of(topicPartition, of(consumerRecord1, consumerRecord2))));

    }

    @Test
    public void test1() {
        kafka.poll(kafkaIterableItem("kafkaArrayItem",
                of("testTopic"),
                DraftDto.class));

        assertThat(CAUGHT_MESSAGES, anEmptyMap());
    }

    @Test
    public void test2() {
        DO_CAPTURES_OF_INSTANCE.accept(SUCCESS);

        var s = kafka.poll(kafkaIterableItem("kafkaArrayItem",
                of("testTopic"),
                DraftDto.class));

        assertThat(CAUGHT_MESSAGES, mapOf(mapEntry("Kafka message",
                "{\"name\":\"testName1\"}")));
    }

    @Test
    public void test3() {
        DO_CAPTURES_OF_INSTANCE.accept(FAILURE);

        kafka.poll(kafkaIterableItem("kafkaArrayItem",
                of("testTopic"),
                DraftDto.class));

        assertThat(CAUGHT_MESSAGES, anEmptyMap());
    }

    @Test
    public void test4() {
        DO_CAPTURES_OF_INSTANCE.accept(SUCCESS_AND_FAILURE);

        kafka.poll(kafkaIterableItem("kafkaArrayItem",
                of("testTopic"),
                DraftDto.class));

        assertThat(CAUGHT_MESSAGES, mapOf(mapEntry("Kafka message",
                "{\"name\":\"testName1\"}")));
    }

    @Test
    public void test5() {
        kafka.poll(kafkaIterableItem("kafkaArrayItem",
                of("testTopic"),
                DraftDto.class)
                .criteria("name = 'kek'", d -> d.getName().equals("kek")));

        assertThat(CAUGHT_MESSAGES, anEmptyMap());
    }

    @Test
    public void test6() {
        DO_CAPTURES_OF_INSTANCE.accept(SUCCESS);

        kafka.poll(kafkaIterableItem("kafkaArrayItem",
                of("testTopic"),
                DraftDto.class)
                .criteria("name = 'kek'", d -> d.getName().equals("kek")));

        assertThat(CAUGHT_MESSAGES, mapOf(mapEntry("All received Kafka messages",
                "{\"name\":\"testName1\"}\r\n\r\n{\"name\":\"testName2\"}\r\n\r\n")));
    }

    @Test
    public void test7() {
        DO_CAPTURES_OF_INSTANCE.accept(FAILURE);

        kafka.poll(kafkaIterableItem("kafkaArrayItem",
                of("testTopic"),
                DraftDto.class)
                .criteria("name = 'kek'", d -> d.getName().equals("kek")));

        assertThat(CAUGHT_MESSAGES, anEmptyMap());
    }

    @Test
    public void test8() {
        DO_CAPTURES_OF_INSTANCE.accept(SUCCESS_AND_FAILURE);

        kafka.poll(kafkaIterableItem("kafkaArrayItem",
                of("testTopic"),
                DraftDto.class)
                .criteria("name = 'kek'", d -> d.getName().equals("kek")));

        assertThat(CAUGHT_MESSAGES, mapOf(mapEntry("All received Kafka messages",
                "{\"name\":\"testName1\"}\r\n\r\n{\"name\":\"testName2\"}\r\n\r\n")));
    }

    @Test
    public void test9() {
        try {
            kafka.poll(kafkaIterableItem("kafkaArrayItem",
                    of("testTopic"),
                    DraftDto.class)
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
                    of("testTopic"),
                    DraftDto.class)
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
                    of("testTopic"),
                    DraftDto.class)
                    .criteria("name = 'kek'", d -> d.getName().equals("kek"))
                    .timeOut(ofSeconds(5))
                    .throwOnNoResult());
        } catch (Exception e) {
            assertThat(CAUGHT_MESSAGES, mapOf(mapEntry("All received Kafka messages",
                    "{\"name\":\"testName1\"}\r\n\r\n{\"name\":\"testName2\"}\r\n\r\n")));
            return;
        }

        fail("Exception was expected");
    }

    @Test
    public void test12() {
        DO_CAPTURES_OF_INSTANCE.accept(SUCCESS_AND_FAILURE);

        try {
            kafka.poll(kafkaIterableItem("kafkaArrayItem",
                    of("testTopic"),
                    DraftDto.class)
                    .criteria("name = 'kek'", d -> d.getName().equals("kek"))
                    .timeOut(ofSeconds(5))
                    .throwOnNoResult());
        } catch (Exception e) {
            assertThat(CAUGHT_MESSAGES, mapOf(mapEntry("All received Kafka messages",
                    "{\"name\":\"testName1\"}\r\n\r\n{\"name\":\"testName2\"}\r\n\r\n")));
            return;
        }

        fail("Exception was expected");
    }

}