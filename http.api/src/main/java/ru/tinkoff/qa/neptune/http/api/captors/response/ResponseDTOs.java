package ru.tinkoff.qa.neptune.http.api.captors.response;

import com.fasterxml.jackson.core.JsonProcessingException;
import ru.tinkoff.qa.neptune.core.api.event.firing.captors.StringCaptor;
import ru.tinkoff.qa.neptune.http.api.mapping.MappedObject;

import java.util.List;

import static ru.tinkoff.qa.neptune.http.api.mapping.DefaultMapper.JSON;

public final class ResponseDTOs extends StringCaptor<List<MappedObject>> implements BaseResponseObjectsBodyCaptor<MappedObject> {

    public ResponseDTOs() {
        super("Response Body. DTO collection. Described as JSON array");
    }

    @Override
    public StringBuilder getData(List<MappedObject> caught) {
        try {
            return new StringBuilder(JSON.getMapper().writerWithDefaultPrettyPrinter().writeValueAsString(caught));
        } catch (JsonProcessingException e) {
            return null;
        }
    }

    @Override
    public List<MappedObject> getCaptured(Object toBeCaptured) {
        return getCaptured(toBeCaptured, MappedObject.class);
    }
}
