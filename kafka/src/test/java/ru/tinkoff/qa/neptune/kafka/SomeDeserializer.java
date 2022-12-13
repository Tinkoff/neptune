package ru.tinkoff.qa.neptune.kafka;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.kafka.common.serialization.Deserializer;

public class SomeDeserializer implements Deserializer<DraftDto> {

    private final Gson gson;

    public SomeDeserializer() {
        this.gson = new GsonBuilder()
            .create();
    }

    @Override
    public DraftDto deserialize(String topic, byte[] data) {
        return gson.fromJson(new String(data), DraftDto.class);
    }
}
