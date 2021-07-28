package ru.tinkoff.qa.neptune.rabbit.mq.test;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import ru.tinkoff.qa.neptune.core.api.data.format.DataTransformer;
import ru.tinkoff.qa.neptune.core.api.data.format.TypeRef;


public class DefaultMapper implements DataTransformer {

    @Override
    public <T> T deserialize(String message, Class<T> cls) {
        try {
            return new ObjectMapper().readValue(message, cls);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public <T> T deserialize(String string, TypeRef<T> type) {
        try {
            return new ObjectMapper().readValue(string, type.jacksonTypeReference());
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String serialize(Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
