package ru.tinkoff.qa.neptune.kafka;

import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.testng.annotations.Test;

import java.util.Map;
import java.util.Properties;
import java.util.function.Supplier;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static ru.tinkoff.qa.neptune.kafka.KafkaStepContext.kafka;
import static ru.tinkoff.qa.neptune.kafka.properties.DefaultKafkaProperties.KAFKA_CONSUMER_PROPERTIES;

public class InnerDeserializerTest {

    @Test
    public void innerDeserializerPositiveTest() {
        assertThat("Some name",
            new InnerDeserializer<>(new SomeDeserializer())
                .deserialize("someTopic", "{\"name\":\"testName\", \"name1\":29, \"name2\": true}".getBytes()),
            equalTo(new DraftDto().setName("testName")));
    }

    @Test
    public void innerDeserializerNegativeTest() {
        assertThat("Some name",
            new InnerDeserializer<>((topic, data) -> {
                throw new RuntimeException("Some exception");
            }).deserialize("someTopic", "{\"name\":\"testName\", \"name1\":29, \"name2\": true}".getBytes()),
            nullValue());
    }

    @Test
    public void kafkaConsumerTest() throws Exception {
        try {
            KAFKA_CONSUMER_PROPERTIES.accept(TestConsumerProperties.class);
            var consumer = kafka().createConsumer((topic, data) -> null,
                (topic, data) -> null, (Map<String, String>) null);

            var keyDeserializer = KafkaConsumer.class.getDeclaredField("keyDeserializer");
            var valueDeserializer = KafkaConsumer.class.getDeclaredField("valueDeserializer");
            keyDeserializer.setAccessible(true);
            valueDeserializer.setAccessible(true);

            assertThat(keyDeserializer.get(consumer), instanceOf(InnerDeserializer.class));
            assertThat(valueDeserializer.get(consumer), instanceOf(InnerDeserializer.class));
        } finally {
            KAFKA_CONSUMER_PROPERTIES.accept(null);
        }
    }

    public static class TestConsumerProperties implements Supplier<Properties> {

        @Override
        public Properties get() {
            var properties = new Properties();
            properties.put("bootstrap.servers", "localhost:9092");
            return properties;
        }
    }
}
