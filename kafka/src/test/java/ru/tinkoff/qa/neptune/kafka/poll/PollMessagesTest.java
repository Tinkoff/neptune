package ru.tinkoff.qa.neptune.kafka.poll;

import com.fasterxml.jackson.core.type.TypeReference;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.TopicPartition;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import ru.tinkoff.qa.neptune.kafka.CustomMapper;
import ru.tinkoff.qa.neptune.kafka.DraftDto;
import ru.tinkoff.qa.neptune.kafka.KafkaBaseTest;

import java.util.Map;

import static java.time.Duration.ofNanos;
import static java.time.Duration.ofSeconds;
import static java.util.Arrays.asList;
import static java.util.List.of;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static ru.tinkoff.qa.neptune.core.api.steps.Criteria.condition;
import static ru.tinkoff.qa.neptune.kafka.functions.poll.KafkaPollArrayItemSupplier.kafkaArrayItem;
import static ru.tinkoff.qa.neptune.kafka.functions.poll.KafkaPollArraySupplier.kafkaArray;
import static ru.tinkoff.qa.neptune.kafka.functions.poll.KafkaPollArraySupplier.kafkaRawMessagesArray;
import static ru.tinkoff.qa.neptune.kafka.functions.poll.KafkaPollIterableItemSupplier.kafkaIterableItem;
import static ru.tinkoff.qa.neptune.kafka.functions.poll.KafkaPollIterableItemSupplier.kafkaRawMessageIterableItem;
import static ru.tinkoff.qa.neptune.kafka.functions.poll.KafkaPollIterableSupplier.kafkaIterable;
import static ru.tinkoff.qa.neptune.kafka.functions.poll.KafkaPollIterableSupplier.kafkaRawMessagesIterable;
import static ru.tinkoff.qa.neptune.kafka.properties.KafkaDefaultTopicsForPollSupplier.DEFAULT_TOPICS_FOR_POLL;

public class PollMessagesTest extends KafkaBaseTest {
    KafkaConsumer<Object, Object> consumer;
    TopicPartition topicPartition;
    ConsumerRecord consumerRecord1;
    ConsumerRecord consumerRecord2;
    ConsumerRecord consumerRecord3;

    @BeforeClass(dependsOnMethods = "setUp")
    public void beforeClass() {
        consumer = kafka.getConsumer();
        topicPartition = new TopicPartition("testTopic", 1);
        consumerRecord1 = new ConsumerRecord("testTopic", 1, 0, null,
                "{\"name\":\"testName\", \"name1\":29, \"name2\": true}");
        consumerRecord2 = new ConsumerRecord("testTopic", 1, 0, null, "{\"1\":1}");
        consumerRecord3 = new ConsumerRecord("testTopic", 1, 0, null, "{\"name\":\"Condition\"}");

        when(consumer.poll(ofNanos(1)))
                .thenReturn(new ConsumerRecords<>(Map.of(topicPartition, of(consumerRecord1, consumerRecord2, consumerRecord3))));

    }

    @Test
    public void test0() {
        var result = kafka.poll(kafkaArrayItem(
                "testDescription",
                DraftDto.class,
                "testTopic")
                .withDataTransformer(new CustomMapper()));

        assertThat(result.getName(), is("PREFIXCondition"));
    }


    @Test
    public void test1() {
        var result = kafka.poll(kafkaArrayItem(
                "testTopic",
                DraftDto.class,
                "testTopic"));

        assertThat(result.getName(), is("testName"));
    }

    @Test
    public void test2() {
        var result = kafka.poll(kafkaArrayItem(
                "testTopic",
                DraftDto.class,
                DraftDto::getName,
                "testTopic"));

        assertThat(result, is("testName"));
    }

    @Test
    public void test3() {
        var result = kafka.poll(kafkaArrayItem(
                "testTopic",
                new TypeReference<DraftDto>() {
                },
                "testTopic"));

        assertThat(result.getName(), is("testName"));
    }

    @Test
    public void test4() {
        var result = kafka.poll(kafkaArrayItem(
                "testTopic",
                new TypeReference<>() {
                },
                DraftDto::getName,
                "testTopic"));

        assertThat(result, is("testName"));
    }

    @Test
    public void test5() {
        var result = kafka.poll(kafkaIterableItem(
                "testTopic",
                DraftDto.class,
                "testTopic"));

        assertThat(result.getName(), is("testName"));
    }

    @Test
    public void test6() {
        var result = kafka.poll(kafkaIterableItem(
                "testTopic",
                DraftDto.class,
                DraftDto::getName,
                "testTopic"));

        assertThat(result, is("testName"));
    }

    @Test
    public void test7() {
        var result = kafka.poll(kafkaIterableItem(
                "testTopic",
                new TypeReference<DraftDto>() {
                },
                "testTopic"));

        assertThat(result.getName(), is("testName"));
    }

    @Test
    public void test8() {
        var result = kafka.poll(kafkaIterableItem(
                "testTopic",
                new TypeReference<>() {
                },
                DraftDto::getName,
                "testTopic"));

        assertThat(result, is("testName"));
    }

    @Test
    public void test9() {
        var results = kafka.poll(kafkaIterable(
                "testTopic",
                DraftDto.class,
                "testTopic")
                .criteria(condition(t -> t.getName().equals("Condition")))
                .timeOut(ofSeconds(1)));

        assertThat(results, hasSize(1));
    }

    @Test
    public void test10() {
        var results = kafka.poll(kafkaIterable(
                "testTopic",
                DraftDto.class,
                t -> t,
                "testTopic"));

        assertThat(results, hasSize(3));
    }

    @Test
    public void test11() {
        var results = kafka.poll(kafkaIterable(
                "testTopic",
                new TypeReference<>() {
                },
                DraftDto::getName,
                "testTopic"));

        assertThat(results, hasSize(2));
    }

    @Test
    public void test12() {
        var results = kafka.poll(kafkaIterable(
                "testTopic",
                new TypeReference<DraftDto>() {
                },
                t -> t,
                "testTopic"));

        assertThat(results, hasSize(3));
    }

    @Test
    public void test13() {
        var result = kafka.poll(kafkaIterable(
                "testTopic",
                DraftDto.class,
                "testTopic"));

        assertThat(result.get(0).getName(), is("testName"));
    }

    @Test
    public void test14() {
        var result = kafka.poll(kafkaIterable(
                "testTopic",
                DraftDto.class,
                t -> t,
                "testTopic"));

        assertThat(result.get(0).getName(), is("testName"));
    }

    @Test
    public void test15() {
        var result = kafka.poll(kafkaIterable(
                "testTopic",
                new TypeReference<DraftDto>() {
                },
                t -> t,
                "testTopic"));

        assertThat(result.get(0).getName(), is("testName"));
    }

    @Test
    public void test16() {
        var result = kafka.poll(kafkaIterable(
                "testTopic",
                new TypeReference<DraftDto>() {
                },
                "testTopic"));

        assertThat(result.get(0).getName(), is("testName"));
    }

    @Test
    public void test17() {
        var result = kafka.poll(kafkaArray(
                "testTopic",
                DraftDto.class,
                "testTopic"));

        assertThat(result[0].getName(), is("testName"));
    }

    @Test
    public void test18() {
        var result = kafka.poll(kafkaArray(
                "testTopic",
                new TypeReference<DraftDto>() {
                },
                "testTopic"));

        assertThat(result[0].getName(), is("testName"));
    }

    @Test
    public void test19() {
        var result = kafka.poll(kafkaArray(
                "testTopic",
                DraftDto.class,
                String.class,
                DraftDto::getName,
                "testTopic"));

        assertThat(result[0], is("testName"));
    }

    @Test
    public void test20() {
        var result = kafka.poll(kafkaArray(
                "testTopic",
                new TypeReference<>() {
                },
                String.class,
                DraftDto::getName,
                "testTopic"));

        assertThat(result[0], is("testName"));
    }

    @Test
    public void test21() {
        DEFAULT_TOPICS_FOR_POLL.accept("topic1,topic2,topic3");

        kafka.poll(kafkaArrayItem(
                "testTopic",
                DraftDto.class));

        verify(consumer, times(1)).subscribe(asList(DEFAULT_TOPICS_FOR_POLL.get()));
    }

    @Test
    public void test22() {
        var result = kafka.poll(kafkaRawMessageIterableItem("testTopic"));

        assertThat(result, is(consumerRecord1.value()));
    }

    @Test
    public void test23() {
        DEFAULT_TOPICS_FOR_POLL.accept("tt");
        kafka.poll(kafkaRawMessageIterableItem());

        verify(consumer, times(1)).subscribe(asList("tt"));
    }

    @Test
    public void test24() {
        var result = kafka.poll(kafkaRawMessagesIterable("testTopic"));

        assertThat(result, containsInAnyOrder(consumerRecord1.value(), consumerRecord2.value(), consumerRecord3.value()));
    }

    @Test
    public void test25() {
        DEFAULT_TOPICS_FOR_POLL.accept("ttt");
        kafka.poll(kafkaRawMessagesIterable());

        verify(consumer, times(1)).subscribe(asList("ttt"));
    }

    @Test
    public void test26() {
        var result = kafka.poll(kafkaRawMessagesArray("testTopic"));

        assertThat(result, arrayContainingInAnyOrder(consumerRecord1.value(), consumerRecord2.value(), consumerRecord3.value()));
    }

    @Test
    public void test27() {
        DEFAULT_TOPICS_FOR_POLL.accept("tttt");
        kafka.poll(kafkaRawMessagesArray());

        verify(consumer, times(1)).subscribe(asList("tttt"));
    }
}
