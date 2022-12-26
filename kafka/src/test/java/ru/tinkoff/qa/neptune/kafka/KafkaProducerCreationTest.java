package ru.tinkoff.qa.neptune.kafka;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.common.serialization.LongSerializer;
import org.apache.kafka.common.serialization.ShortSerializer;
import org.testng.annotations.Test;

import java.util.Map;
import java.util.Properties;
import java.util.function.Supplier;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;
import static ru.tinkoff.qa.neptune.kafka.KafkaStepContext.kafka;
import static ru.tinkoff.qa.neptune.kafka.properties.DefaultKafkaProperties.KAFKA_PRODUCER_PROPERTIES;

public class KafkaProducerCreationTest {

    @Test
    public void createProducerTest() throws Exception {
        try {
            KAFKA_PRODUCER_PROPERTIES.accept(TestProducerProperties.class);
            var producer = kafka().createProducer(new ShortSerializer(), new LongSerializer(), (Map<String, String>) null);

            var keySerializer = KafkaProducer.class.getDeclaredField("keySerializer");
            var valueSerializer = KafkaProducer.class.getDeclaredField("valueSerializer");
            keySerializer.setAccessible(true);
            valueSerializer.setAccessible(true);

            assertThat(keySerializer.get(producer), instanceOf(ShortSerializer.class));
            assertThat(valueSerializer.get(producer), instanceOf(LongSerializer.class));
        } finally {
            KAFKA_PRODUCER_PROPERTIES.accept(null);
        }
    }

    public static class TestProducerProperties implements Supplier<Properties> {

        @Override
        public Properties get() {
            var properties = new Properties();
            properties.put("bootstrap.servers", "localhost:9092");
            return properties;
        }
    }
}
