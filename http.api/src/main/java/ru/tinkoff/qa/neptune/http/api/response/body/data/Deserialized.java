package ru.tinkoff.qa.neptune.http.api.response.body.data;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.function.Function;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

/**
 * Deserializes string to object
 *
 * @param <T> is a type of an object deserialized from string
 */
@SuppressWarnings("unchecked")
final class Deserialized<T> implements Function<String, T> {

    private final Object deserializeTo;
    private final ObjectMapper mapper;

    private Deserialized(Object deserializeTo, ObjectMapper mapper) {
        checkNotNull(deserializeTo);
        checkNotNull(mapper);
        this.deserializeTo = deserializeTo;
        this.mapper = mapper;
    }

    public Deserialized(Class<T> deserializeTo, ObjectMapper mapper) {
        this((Object) deserializeTo, mapper);
    }

    public Deserialized(TypeReference<T> deserializeTo, ObjectMapper mapper) {
        this((Object) deserializeTo, mapper);
    }

    @Override
    public T apply(String s) {
        try {
            if (isNotBlank(s)) {
                if (deserializeTo instanceof Class<?>) {
                    return mapper.readValue(s, (Class<T>) deserializeTo);
                } else {
                    return mapper.readValue(s, (TypeReference<T>) deserializeTo);
                }
            } else {
                return null;
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }
}
