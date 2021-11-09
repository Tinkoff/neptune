package ru.tinkoff.qa.neptune.spring.mock.mvc;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import ru.tinkoff.qa.neptune.core.api.data.format.DataTransformer;

public class TestDataTransformer implements DataTransformer {

    private final ObjectMapper mapper;

    public TestDataTransformer() {
        this.mapper = new ObjectMapper();
    }

    @Override
    public <T> T deserialize(String string, Class<T> cls) {
        try {
            return mapper.readValue(string, cls);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public <T> T deserialize(String string, TypeReference<T> type) {
        try {
            return mapper.readValue(string, type);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String serialize(Object obj) {
        try {
            return mapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
