package ru.tinkoff.qa.neptune.http.api.mapping;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.LinkedHashMap;
import java.util.Map;

import static java.util.Objects.isNull;
import static org.apache.commons.lang3.builder.EqualsBuilder.reflectionEquals;
import static ru.tinkoff.qa.neptune.http.api.mapping.DefaultMapper.JSON;

/**
 * Tris class is designed to represent mapped http-objects such as bodies of http requests/responses,
 * parameters of queries, path variables and headers.
 *
 * @see ru.tinkoff.qa.neptune.http.api.service.mapping.annotations.parameters
 * @see #toMap()
 */
public abstract class MappedObject {


    @Override
    public boolean equals(Object obj) {

        if (obj == null) {
            return false;
        }

        if (obj == this) {
            return true;
        }

        if (obj.getClass() == this.getClass()) {
            return reflectionEquals(this, obj);
        }
        return false;
    }

    /**
     * Transforms this object to a map. Keys are names of fields and values are field values there.
     *
     * @return a map of this object
     */
    public Map<String, Object> toMap() {
        try {
            var s = JSON.getMapper().writeValueAsString(this);
            var map = new ObjectMapper()
                    .readValue(s, new TypeReference<Map<String, Object>>() {
                    });

            var result = new LinkedHashMap<>(map);
            map.forEach((k, v) -> {
                if (isNull(v)) {
                    result.remove(k);
                }
            });
            return result;
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public String toString() {
        try {
            return JSON.getMapper().writerWithDefaultPrettyPrinter().writeValueAsString(this);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
