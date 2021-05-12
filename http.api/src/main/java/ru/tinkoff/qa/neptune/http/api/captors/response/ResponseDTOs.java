package ru.tinkoff.qa.neptune.http.api.captors.response;

import com.fasterxml.jackson.core.JsonProcessingException;
import ru.tinkoff.qa.neptune.core.api.event.firing.captors.CapturedStringInjector;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;
import ru.tinkoff.qa.neptune.http.api.mapping.MappedObject;

import java.util.List;

import static ru.tinkoff.qa.neptune.core.api.utils.SPIUtil.loadSPI;
import static ru.tinkoff.qa.neptune.http.api.mapping.DefaultMapper.JSON;

@Description("Response Body. DTO collection. Described by json-formatted string")
public final class ResponseDTOs extends AbstractResponseBodyObjectsCaptor<MappedObject, StringBuilder> {

    public ResponseDTOs() {
        super(loadSPI(CapturedStringInjector.class), MappedObject.class, mappedObject -> true);
    }

    @Override
    public StringBuilder getData(List<MappedObject> caught) {
        try {
            return new StringBuilder(JSON.getMapper().writerWithDefaultPrettyPrinter().writeValueAsString(caught));
        } catch (JsonProcessingException e) {
            return null;
        }
    }
}
