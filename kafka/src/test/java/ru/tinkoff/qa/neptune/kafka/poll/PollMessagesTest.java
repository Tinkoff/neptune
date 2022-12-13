package ru.tinkoff.qa.neptune.kafka.poll;

import com.fasterxml.jackson.core.type.TypeReference;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.common.TopicPartition;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import ru.tinkoff.qa.neptune.kafka.CustomMapper;
import ru.tinkoff.qa.neptune.kafka.DraftDto;
import ru.tinkoff.qa.neptune.kafka.KafkaBasePreparations;

import java.time.Duration;
import java.util.Map;

import static java.time.Duration.ofSeconds;
import static java.util.List.of;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static ru.tinkoff.qa.neptune.core.api.hamcrest.iterables.SetOfObjectsEachItemMatcher.eachOfArray;
import static ru.tinkoff.qa.neptune.core.api.hamcrest.iterables.SetOfObjectsEachItemMatcher.eachOfIterable;
import static ru.tinkoff.qa.neptune.core.api.hamcrest.pojo.PojoGetterReturnsMatcher.getterReturns;
import static ru.tinkoff.qa.neptune.core.api.steps.Criteria.condition;
import static ru.tinkoff.qa.neptune.kafka.functions.poll.GetRecordSupplier.consumerRecords;
import static ru.tinkoff.qa.neptune.kafka.functions.poll.GetRecordSupplier.records;
import static ru.tinkoff.qa.neptune.kafka.functions.poll.KafkaPollArraySupplier.kafkaArray;
import static ru.tinkoff.qa.neptune.kafka.functions.poll.KafkaPollArraySupplier.kafkaArrayOfRawMessages;
import static ru.tinkoff.qa.neptune.kafka.functions.poll.KafkaPollIterableItemSupplier.kafkaIterableItem;
import static ru.tinkoff.qa.neptune.kafka.functions.poll.KafkaPollIterableItemSupplier.kafkaRawMessage;
import static ru.tinkoff.qa.neptune.kafka.functions.poll.KafkaPollIterableSupplier.kafkaIterable;
import static ru.tinkoff.qa.neptune.kafka.functions.poll.KafkaPollIterableSupplier.kafkaRawMessages;
import static ru.tinkoff.qa.neptune.kafka.properties.KafkaDefaultTopicsForPollProperty.DEFAULT_TOPICS_FOR_POLL;

@Deprecated(forRemoval = true)
@SuppressWarnings({"rawtypes", "unchecked"})
public class PollMessagesTest extends KafkaBasePreparations {
    TopicPartition topicPartition;
    ConsumerRecord consumerRecord1;
    ConsumerRecord consumerRecord2;
    ConsumerRecord consumerRecord3;

    @BeforeClass(dependsOnMethods = "setUp")
    public void beforeClass() {
        topicPartition = new TopicPartition("testTopic", 1);
        consumerRecord1 = new ConsumerRecord("testTopic", 1, 0, null,
            "{\"name\":\"testName\", \"name1\":29, \"name2\": true}");
        consumerRecord2 = new ConsumerRecord("testTopic", 1, 0, null, "{\"1\":1}");
        consumerRecord3 = new ConsumerRecord("testTopic", 1, 0, null, "{\"name\":\"Condition\"}");

        when(kafkaConsumer.poll(any(Duration.class)))
            .thenReturn(new ConsumerRecords<>(Map.of(topicPartition, of(consumerRecord1, consumerRecord2, consumerRecord3))));

    }

    @Test
    public void test1() {
        var result = kafka.poll(kafkaIterableItem(
            "testTopic",
            DraftDto.class,
            "testTopic"));

        assertThat(result.getName(), is("testName"));
    }

    @Test
    public void test2() {
        var result = kafka.poll(kafkaIterableItem(
            "testTopic",
            DraftDto.class,
            DraftDto::getName,
            "testTopic"));

        assertThat(result, is("testName"));
    }

    @Test
    public void test3() {
        var result = kafka.poll(kafkaIterableItem(
            "testTopic",
            new TypeReference<DraftDto>() {},
            "testTopic"));

        assertThat(result.getName(), is("testName"));
    }

    @Test
    public void test4() {
        var result = kafka.poll(kafkaIterableItem(
            "testTopic",
            new TypeReference<>() {},
            DraftDto::getName,
            "testTopic"));

        assertThat(result, is("testName"));
    }

    @Test
    public void test5() {
        var result = kafka.poll(kafkaIterableItem(
            "testTopic",
            DraftDto.class,
            "testTopic")
            .withDataTransformer(new CustomMapper()));

        assertThat(result, getterReturns("getName", "PREFIXCondition"));
    }

    @Test
    public void test6() {
        var results = kafka.poll(kafkaIterable(
            "testTopic",
            DraftDto.class,
            "testTopic")
            .criteria(condition(t -> t.getName().equals("Condition")))
            .timeOut(ofSeconds(1)));

        assertThat(results, hasSize(1));
    }

    @Test
    public void test7() {
        var results = kafka.poll(kafkaIterable(
            "testTopic",
            DraftDto.class,
            t -> t,
            "testTopic"));

        assertThat(results, hasSize(3));
    }

    @Test
    public void test8() {
        var results = kafka.poll(kafkaIterable(
            "testTopic",
            new TypeReference<>() {},
            DraftDto::getName,
            "testTopic"));

        assertThat(results, hasSize(2));
    }

    @Test
    public void test9() {
        var results = kafka.poll(kafkaIterable(
            "testTopic",
            new TypeReference<DraftDto>() {
            },
            t -> t,
            "testTopic"));

        assertThat(results, hasSize(3));
    }

    @Test
    public void test10() {
        var result = kafka.poll(kafkaIterable(
            "testTopic",
            DraftDto.class,
            "testTopic"));

        assertThat(result, hasSize(3));
    }

    @Test
    public void test11() {
        var result = kafka.poll(kafkaIterable(
            "testTopic",
            DraftDto.class,
            t -> t,
            "testTopic"));

        assertThat(result, hasSize(3));
    }

    @Test
    public void test12() {
        var result = kafka.poll(kafkaIterable(
            "testTopic",
            new TypeReference<DraftDto>() {
            },
            t -> t,
            "testTopic"));

        assertThat(result, hasSize(3));
    }

    @Test
    public void test13() {
        var result = kafka.poll(kafkaIterable(
            "testTopic",
            new TypeReference<DraftDto>() {
            },
            "testTopic"));

        assertThat(result, hasSize(3));
    }

    @Test
    public void test14() {
        var result = kafka.poll(kafkaIterable(
            "testTopic",
            DraftDto.class,
            "testTopic")
            .withDataTransformer(new CustomMapper()));

        assertThat(result, eachOfIterable(getterReturns("getName", "PREFIXCondition")));
    }

    @Test
    public void test15() {
        var result = kafka.poll(kafkaArray(
            "testTopic",
            DraftDto.class,
            "testTopic"));

        assertThat(result[0].getName(), is("testName"));
    }

    @Test
    public void test16() {
        var result = kafka.poll(kafkaArray(
            "testTopic",
            new TypeReference<DraftDto>() {
            },
            "testTopic"));

        assertThat(result[0].getName(), is("testName"));
    }

    @Test
    public void test17() {
        var result = kafka.poll(kafkaArray(
            "testTopic",
            DraftDto.class,
            String.class,
            DraftDto::getName,
            "testTopic"));

        assertThat(result[0], is("testName"));
    }

    @Test
    public void test18() {
        var result = kafka.poll(kafkaArray(
            "testTopic",
            new TypeReference<>() {},
            String.class,
            DraftDto::getName,
            "testTopic"));

        assertThat(result[0], is("testName"));
    }

    @Test
    public void test19() {
        var result = kafka.poll(kafkaArray(
            "testTopic",
            DraftDto.class,
            "testTopic")
            .withDataTransformer(new CustomMapper()));

        assertThat(result, eachOfArray(getterReturns("getName", "PREFIXCondition")));
    }

    @Test
    public void test20() {
        var result = kafka.poll(kafkaRawMessage("testTopic"));

        assertThat(result, is(consumerRecord1.value()));
    }

    @Test
    public void test21() {
        DEFAULT_TOPICS_FOR_POLL.accept("tt");
        kafka.poll(kafkaRawMessage());

        verify(kafkaConsumer, times(1)).subscribe(of("tt"));
    }

    @Test
    public void test22() {
        var result = kafka.poll(kafkaRawMessages("testTopic"));

        assertThat(result, containsInAnyOrder(consumerRecord1.value(), consumerRecord2.value(), consumerRecord3.value()));
    }

    @Test
    public void test23() {
        DEFAULT_TOPICS_FOR_POLL.accept("ttt");
        kafka.poll(kafkaRawMessages());

        verify(kafkaConsumer, times(1)).subscribe(of("ttt"));
    }

    @Test
    public void test24() {
        var result = kafka.poll(kafkaArrayOfRawMessages("testTopic"));

        assertThat(result, arrayContainingInAnyOrder(consumerRecord1.value(), consumerRecord2.value(), consumerRecord3.value()));
    }

    @Test
    public void test25() {
        DEFAULT_TOPICS_FOR_POLL.accept("tttt");
        kafka.poll(kafkaArrayOfRawMessages());

        verify(kafkaConsumer, times(1)).subscribe(of("tttt"));
    }

    @Test
    public void test26() {
        var result = kafka.poll(consumerRecords("TestDescription", "testTopic")

            .timeOut(ofSeconds(1)));

        assertThat(result, hasItems(consumerRecord1, consumerRecord2, consumerRecord3));
    }

    @Test
    public void test27() {
        var result = kafka.poll(consumerRecords("TestDescription", "testTopic")
            .thenGetList("value from ConsumerRecords", ConsumerRecord::value));

        assertThat(result, containsInAnyOrder(consumerRecord1.value(), consumerRecord2.value(), consumerRecord3.value()));
    }

    @Test
    public void test28() {
        var result = kafka.poll(consumerRecords("TestDescription", "testTopic")
            .thenGetList("value from ConsumerRecords", ConsumerRecord::value));

        assertThat(result, containsInAnyOrder(consumerRecord1.value(), consumerRecord2.value(), consumerRecord3.value()));
    }

    @Test
    public void test29() {
        var result = kafka.poll(consumerRecords("TestDescription", "testTopic")
            .criteria("description", c -> c.value().contains("29")));

        assertThat(result, containsInAnyOrder(consumerRecord1));
    }

    @Test
    public void test30() {
        var result = kafka.poll(consumerRecords("TestDescription", "testTopic")
            .criteria("description", c -> c.value().contains("29"))
            .thenGetList("value from ConsumerRecords", ConsumerRecord::value));

        assertThat(result, containsInAnyOrder(consumerRecord1.value()));
    }

    @Test
    public void test31() {
        var result = kafka.poll(consumerRecords("TestDescription", "testTopic")
            .criteria("description", c -> c.value().contains("29"))
            .thenGetList("value from ConsumerRecords", DraftDto.class));

        assertThat(result, hasSize(1));
    }

    @Test
    public void test32() {
        var result = kafka.poll(consumerRecords("TestDescription", "testTopic")
            .criteria("description", c -> c.value().contains("29"))
            .thenGetList("DraftDto-> name", DraftDto.class, DraftDto::getName));

        assertThat(result.get(0), is("testName"));
    }

    @Test
    public void test33() {
        var result = kafka.poll(consumerRecords("TestDescription", "testTopic")
            .criteria("description", c -> c.value().contains("29"))
            .thenGetList("value from ConsumerRecords", new TypeReference<>() {
            }, DraftDto::getName));

        assertThat(result.get(0), is("testName"));
    }

    @Test
    public void test34() {
        var result = kafka.poll(consumerRecords("TestDescription", "testTopic")
            .criteria("description", c -> c.value().contains("29"))
            .thenGetList("value from ConsumerRecords", new TypeReference<DraftDto>() {
            }));

        assertThat(result, hasSize(1));
    }

    @Test
    public void test35() {
        var result = kafka.poll(consumerRecords("TestDescription", "testTopic")
            .thenGetItem("value from ConsumerRecords", new TypeReference<DraftDto>() {
            }));

        assertThat(result.getName(), is("testName"));
    }

    @Test
    public void test36() {
        var result = kafka.poll(consumerRecords("TestDescription", "testTopic")
            .thenGetItem("value from ConsumerRecords", DraftDto.class));

        assertThat(result.getName(), is("testName"));
    }

    @Test
    public void test37() {
        var result = kafka.poll(consumerRecords("TestDescription", "testTopic")
            .criteria("testDescription", c -> c.value().contains("Condition"))
            .thenGetItem("value from ConsumerRecords", new TypeReference<DraftDto>() {
            }));

        assertThat(result.getName(), is("Condition"));
    }

    @Test
    public void test38() {
        var result = kafka.poll(consumerRecords("TestDescription", "testTopic")
            .thenGetItem("value from ConsumerRecords", DraftDto.class, DraftDto::getName));

        assertThat(result, is("testName"));
    }

    @Test
    public void test39() {
        var result = kafka.poll(consumerRecords("TestDescription", "testTopic")
            .thenGetItem("value from ConsumerRecords", new TypeReference<>() {
            }, DraftDto::getName));

        assertThat(result, is("testName"));
    }

    @Test
    public void test40() {
        var result = kafka.poll(consumerRecords("TestDescription", "testTopic")
            .thenGetItem("value from ConsumerRecords", ConsumerRecord::value));

        assertThat(result, is("{\"name\":\"testName\", \"name1\":29, \"name2\": true}"));
    }

    @Test
    public void test41() {
        var result = kafka.poll(consumerRecords("TestDescription", "testTopic")
            .thenGetItem("value from ConsumerRecords", DraftDto.class)
            .withDataTransformer(new CustomMapper()));

        assertThat(result, getterReturns("getName", "PREFIXCondition"));
    }

    @Test
    public void test42() {
        var result = kafka.poll(records("testTopic"));
        assertThat(result, hasItems(consumerRecord1, consumerRecord2, consumerRecord3));
    }
}
