package ru.tinkoff.qa.neptune.http.api.dto;

import static ru.tinkoff.qa.neptune.http.api.mapper.DefaultBodyMappers.JSON;

public class JsonDTObject extends DTObject {

    /**
     * Transforms object to json string.
     * Annotations of the {@link com.fasterxml.jackson.databind.annotation} package are allowed for json's.
     *
     * @return String value of serialized object
     */
    public String serialize() {
        try {
            return JSON.getMapper().writeValueAsString(this);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String toString() {
        try {
            return JSON.getMapper().writerWithDefaultPrettyPrinter().writeValueAsString(this);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
