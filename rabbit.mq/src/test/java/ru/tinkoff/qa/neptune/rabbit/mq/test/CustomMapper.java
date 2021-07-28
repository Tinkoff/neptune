package ru.tinkoff.qa.neptune.rabbit.mq.test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import ru.tinkoff.qa.neptune.core.api.data.format.DataTransformer;
import ru.tinkoff.qa.neptune.core.api.data.format.TypeRef;

import java.util.Map;

import static java.util.stream.Collectors.toMap;

public class CustomMapper implements DataTransformer {

    @Override
    public <T> T deserialize(String message, Class<T> cls) {
        try {
            var map = new ObjectMapper().readValue(message, new TypeReference<Map<String, String>>() {
            });

            var newMap = map.entrySet()
                    .stream()
                    .collect(toMap(Map.Entry::getKey, v -> "PREFIX" + v.getValue()));

            var json = new ObjectMapper().writeValueAsString(newMap);
            return new ObjectMapper().readValue(json, cls);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public <T> T deserialize(String string, TypeRef<T> type) {
        try {
            var map = new ObjectMapper().readValue(string, new TypeReference<Map<String, String>>() {
            });

            var newMap = map.entrySet()
                    .stream()
                    .collect(toMap(Map.Entry::getKey, v -> "PREFIX" + v.getValue()));

            var json = new ObjectMapper().writeValueAsString(newMap);
            return new ObjectMapper().readValue(json, type.jacksonTypeReference());
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String serialize(Object obj) {
        return "customSerialize";
    }
}