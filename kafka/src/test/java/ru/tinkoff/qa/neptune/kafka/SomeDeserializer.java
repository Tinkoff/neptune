package ru.tinkoff.qa.neptune.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.serialization.Deserializer;

import java.io.IOException;

import static com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES;

public class SomeDeserializer implements Deserializer<DraftDto> {

    @Override
    public DraftDto deserialize(String topic, byte[] data) {
        try {
            return new ObjectMapper().configure(FAIL_ON_UNKNOWN_PROPERTIES, false).readValue(data, DraftDto.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
