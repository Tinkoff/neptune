package ru.tinkoff.qa.neptune.kafka.captors;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.common.TopicPartition;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import ru.tinkoff.qa.neptune.kafka.KafkaBasePreparations;

import java.time.Duration;
import java.util.Map;

import static java.util.List.of;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static ru.tinkoff.qa.neptune.core.api.properties.general.events.DoCapturesOf.DO_CAPTURES_OF_INSTANCE;
import static ru.tinkoff.qa.neptune.kafka.captors.TestStringInjector.CAUGHT_MESSAGES;

@SuppressWarnings({"rawtypes", "unchecked"})
public class BaseCaptorTest extends KafkaBasePreparations {

    TopicPartition topicPartition;

    @BeforeMethod
    public void prepare() {
        DO_CAPTURES_OF_INSTANCE.accept(null);
        CAUGHT_MESSAGES.clear();
    }

    @BeforeClass(dependsOnMethods = "setUp")
    public void beforeClass() {
        topicPartition = new TopicPartition("testTopic", 1);
        ConsumerRecord consumerRecord1 = new ConsumerRecord("testTopic", 1, 0, null, "{\"name\":\"testName1\"}");
        ConsumerRecord consumerRecord2 = new ConsumerRecord("testTopic", 1, 0, null, "{\"name\":\"testName2\"}");

        when(kafkaConsumer.poll(any(Duration.class)))
            .thenReturn(new ConsumerRecords<>(Map.of(topicPartition, of(consumerRecord1, consumerRecord2))));

    }
}