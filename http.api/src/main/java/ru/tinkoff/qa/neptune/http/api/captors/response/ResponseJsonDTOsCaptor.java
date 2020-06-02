package ru.tinkoff.qa.neptune.http.api.captors.response;

import com.fasterxml.jackson.core.JsonProcessingException;
import ru.tinkoff.qa.neptune.core.api.event.firing.captors.StringCaptor;
import ru.tinkoff.qa.neptune.http.api.dto.JsonDTObject;

import java.util.List;

import static ru.tinkoff.qa.neptune.http.api.mapper.DefaultBodyMappers.JSON;

public final class ResponseJsonDTOsCaptor extends StringCaptor<List<JsonDTObject>> implements BaseResponseObjectsBodyCaptor<JsonDTObject> {

    public ResponseJsonDTOsCaptor() {
        super("Response Body. Dto. Json Array");
    }

    @Override
    public StringBuilder getData(List<JsonDTObject> caught) {
        try {
            return new StringBuilder(JSON.getMapper().writerWithDefaultPrettyPrinter().writeValueAsString(caught));
        } catch (JsonProcessingException e) {
            return null;
        }
    }

    @Override
    public List<JsonDTObject> getCaptured(Object toBeCaptured) {
        return getCaptured(toBeCaptured, JsonDTObject.class);
    }
}
