package ru.tinkoff.qa.neptune.kafka.poll;

import com.fasterxml.jackson.core.type.TypeReference;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.TopicPartition;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import ru.tinkoff.qa.neptune.kafka.DraftDto;
import ru.tinkoff.qa.neptune.kafka.KafkaBaseTest;

import java.util.Map;

import static java.time.Duration.ofNanos;
import static java.time.Duration.ofSeconds;
import static java.util.List.of;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static ru.tinkoff.qa.neptune.core.api.steps.Criteria.condition;
import static ru.tinkoff.qa.neptune.kafka.functions.poll.KafkaPollArrayItemSupplier.kafkaArrayItem;
import static ru.tinkoff.qa.neptune.kafka.functions.poll.KafkaPollArraySupplier.kafkaArray;
import static ru.tinkoff.qa.neptune.kafka.functions.poll.KafkaPollIterableItemSupplier.kafkaIterableItem;
import static ru.tinkoff.qa.neptune.kafka.functions.poll.KafkaPollIterableSupplier.kafkaIterable;

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
                "{\"name\":\"testName\"," +
                        "\"name1\":29," +
                        "\"name2\": true," +
                        " \"member\" : {\n" +
                        "      \"name\": \"Madame Uppercut\",\n" +
                        "      \"age\": 39,\n" +
                        "      \"secretIdentity\": \"Jane Wilson\",\n" +
                        "      \"powers\": [\n" +
                        "        \"Million tonne punch\",\n" +
                        "        \"Damage resistance\",\n" +
                        "        \"Superhuman reflexes\"\n" +
                        "      ]\n" +
                        "    }}");
        consumerRecord2 = new ConsumerRecord("testTopic", 1, 0, null, "{\"1\":1}");
        consumerRecord3 = new ConsumerRecord("testTopic", 1, 0, null, "{\"name\":\"Condition\"}");

        when(consumer.poll(ofNanos(1)))
                .thenReturn(new ConsumerRecords<>(Map.of(topicPartition, of(consumerRecord1, consumerRecord2, consumerRecord3))));

    }

    @Test
    public void test1() {
        var result = kafka.poll(kafkaArrayItem(
                "testTopic",
                of("testTopic"),
                DraftDto.class));

        assertThat(result.getName(), is("testName"));
    }

    @Test
    public void test2() {
        var result = kafka.poll(kafkaArrayItem(
                "testTopic",
                of("testTopic"),
                DraftDto.class,
                DraftDto::getName));

        assertThat(result, is("testName"));
    }

    @Test
    public void test3() {
        var result = kafka.poll(kafkaArrayItem(
                "testTopic",
                of("testTopic"),
                new TypeReference<DraftDto>() {
                }));

        assertThat(result.getName(), is("testName"));
    }

    @Test
    public void test4() {
        var result = kafka.poll(kafkaArrayItem(
                "testTopic",
                of("testTopic"),
                new TypeReference<>() {
                },
                DraftDto::getName));

        assertThat(result, is("testName"));
    }

    @Test
    public void test5() {
        var result = kafka.poll(kafkaIterableItem(
                "testTopic",
                of("testTopic"),
                DraftDto.class));

        assertThat(result.getName(), is("testName"));
    }

    @Test
    public void test6() {
        var result = kafka.poll(kafkaIterableItem(
                "testTopic",
                of("testTopic"),
                DraftDto.class,
                DraftDto::getName));

        assertThat(result, is("testName"));
    }

    @Test
    public void test7() {
        var result = kafka.poll(kafkaIterableItem(
                "testTopic",
                of("testTopic"),
                new TypeReference<DraftDto>() {
                }));

        assertThat(result.getName(), is("testName"));
    }

    @Test
    public void test8() {
        var result = kafka.poll(kafkaIterableItem(
                "testTopic",
                of("testTopic"),
                new TypeReference<>() {
                },
                DraftDto::getName));

        assertThat(result, is("testName"));
    }

    @Test
    public void test9() {
        var results = kafka.poll(kafkaIterable(
                "testTopic",
                of("testTopic"),
                DraftDto.class)
                .criteria(condition(t -> t.getName().equals("Condition")))
                .timeOut(ofSeconds(1)));

        assertThat(results, hasSize(1));
    }

    @Test
    public void test10() {
        var results = kafka.poll(kafkaIterable(
                "testTopic",
                of("testTopic"),
                DraftDto.class,
                t -> t));

        assertThat(results, hasSize(3));
    }

    @Test
    public void test11() {
        var results = kafka.poll(kafkaIterable(
                "testTopic",
                of("testTopic"),
                new TypeReference<>() {
                },
                DraftDto::getName));

        assertThat(results, hasSize(2));
    }

    @Test
    public void test12() {
        var results = kafka.poll(kafkaIterable(
                "testTopic",
                of("testTopic"),
                new TypeReference<DraftDto>() {
                },
                t -> t));

        assertThat(results, hasSize(3));
    }

    @Test
    public void test13() {
        var result = kafka.poll(kafkaIterable(
                "testTopic",
                of("testTopic"),
                DraftDto.class));

        assertThat(result.get(0).getName(), is("testName"));
    }

    @Test
    public void test14() {
        var result = kafka.poll(kafkaIterable(
                "testTopic",
                of("testTopic"),
                DraftDto.class,
                t -> t));

        assertThat(result.get(0).getName(), is("testName"));
    }

    @Test
    public void test15() {
        var result = kafka.poll(kafkaIterable(
                "testTopic",
                of("testTopic"),
                new TypeReference<DraftDto>() {
                },
                t -> t));

        assertThat(result.get(0).getName(), is("testName"));
    }

    @Test
    public void test16() {
        var result = kafka.poll(kafkaIterable(
                "testTopic",
                of("testTopic"),
                new TypeReference<DraftDto>() {
                }));

        assertThat(result.get(0).getName(), is("testName"));
    }

    @Test
    public void test17() {
        var result = kafka.poll(kafkaArray(
                "testTopic",
                of("testTopic"),
                DraftDto.class));

        assertThat(result[0].getName(), is("testName"));
    }

    @Test
    public void test18() {
        var result = kafka.poll(kafkaArray(
                "testTopic",
                of("testTopic"),
                new TypeReference<DraftDto>() {
                }));

        assertThat(result[0].getName(), is("testName"));
    }

    @Test
    public void test19() {
        var result = kafka.poll(kafkaArray(
                "testTopic",
                of("testTopic"),
                DraftDto.class,
                String.class,
                DraftDto::getName));

        assertThat(result[0], is("testName"));
    }

    @Test
    public void test20() {
        var result = kafka.poll(kafkaArray(
                "testTopic",
                of("testTopic"),
                new TypeReference<>() {
                },
                String.class,
                DraftDto::getName));

        assertThat(result[0], is("testName"));
    }
}
