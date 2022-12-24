package ru.tinkoff.qa.neptune.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.serialization.Serializer;

public class SomeSerializer implements Serializer<DraftDto> {

    @Override
    public byte[] serialize(String topic, DraftDto data) {
        try {
            return new ObjectMapper().writeValueAsBytes(data);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
