package ru.tinkoff.qa.neptune.http.api.response.body.data;

import java.util.function.Function;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import static com.google.common.base.Preconditions.checkArgument;
import static java.util.Objects.nonNull;
import static org.apache.commons.lang3.StringUtils.isBlank;

/**
 * This function is designed to deserialize json/xml string to some object. Deserialization is
 * performed by {@link com.fasterxml.jackson.databind.ObjectMapper}
 * @param <T> is a type of resulted object
 */
public class GetMapped<T> implements Function<String, T> {

    private final Class<T> toGet;
    private final ObjectMapper mapper;

    private GetMapped(Class<T> toGet, ObjectMapper mapper) {
        checkArgument(nonNull(toGet), "Class of an object to get should not be a null value");
        checkArgument(nonNull(mapper), "Mapper should not be a null value");
        this.toGet = toGet;
        this.mapper = mapper;
    }

    /**
     * Creates an instance of {@link GetMapped}
     *
     * @param toReturn is a type of resulted object to deserialize string body of an http response
     * @param mapper is an instance of {@link ObjectMapper}
     * @param <T> is a type of resulted object
     * @return instance of {@link GetMapped}
     */
    public static <T> Function<String, T> getMapped(Class<T> toReturn, ObjectMapper mapper) {
        return new GetMapped<>(toReturn, mapper);
    }

    /**
     * Creates an instance of {@link GetMapped}
     *
     * @param toReturn is a type of resulted object to deserialize string body of an http response
     * @param <T> is a type of resulted object
     * @return instance of {@link GetMapped}
     */
    public static <T> Function<String, T> getMapped(Class<T> toReturn) {
        return getMapped(toReturn, new ObjectMapper());
    }

    @Override
    public T apply(String s) {
        try {
            if (!isBlank(s)) {
                return mapper.readValue(s, toGet);
            }
            else {
                return null;
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
