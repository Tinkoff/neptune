package ru.tinkoff.qa.neptune.http.api.response.body.data;

import java.util.function.Function;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import static com.google.common.base.Preconditions.checkArgument;
import static java.util.Objects.nonNull;
import static org.apache.commons.lang3.StringUtils.isBlank;

/**
 *
 * @param <T>
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
     * @param toReturn
     * @param mapper
     * @param <T>
     * @return
     */
    public static <T> GetMapped<T> getMapped(Class<T> toReturn, ObjectMapper mapper) {
        return new GetMapped<>(toReturn, mapper);
    }

    /**
     * 
     * @param toReturn
     * @param <T>
     * @return
     */
    public static <T> GetMapped<T> getMapped(Class<T> toReturn) {
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
