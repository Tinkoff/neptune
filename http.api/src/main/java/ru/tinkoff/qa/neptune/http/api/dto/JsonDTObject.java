package ru.tinkoff.qa.neptune.http.api.dto;

import com.fasterxml.jackson.databind.ObjectMapper;

import static java.lang.String.format;
import static java.util.Optional.ofNullable;
import static ru.tinkoff.qa.neptune.http.api.properties.dto.DefaultJsonDTObjectMapper.DEFAULT_JSON_DT_OBJECT_MAPPER;

public class JsonDTObject extends DTObject {

    private static ObjectMapper jsonObjectMapper() {
        return ofNullable(DEFAULT_JSON_DT_OBJECT_MAPPER.get())
                .map(s -> ofNullable(s.get())
                        .orElseThrow(
                                () -> new IllegalStateException(format("An instance of %s supplied null-value",
                                        s.getClass().getName()))
                        ))
                .orElseGet(ObjectMapper::new);
    }

    /**
     * Transforms object to json string.
     * Annotations of the {@link com.fasterxml.jackson.databind.annotation} package are allowed for json's.
     *
     * @return String value of serialized object
     */
    public String serialize() {
        try {
            return jsonObjectMapper().writeValueAsString(this);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
