package ru.tinkoff.qa.neptune.kafka.jackson.desrializer;

import com.fasterxml.jackson.databind.module.SimpleModule;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.header.Headers;

public class KafkaJacksonModule extends SimpleModule {

    public KafkaJacksonModule() {
        addSerializer(Headers.class, new HeaderJsonSerializer());
        addSerializer(ConsumerRecord.class, new ConsumerRecordSerializer());
    }
}
