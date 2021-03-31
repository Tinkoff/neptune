package ru.tinkoff.qa.neptune.http.api.captors.response;

import ru.tinkoff.qa.neptune.core.api.event.firing.captors.StringCaptor;
import ru.tinkoff.qa.neptune.core.api.steps.Description;
import ru.tinkoff.qa.neptune.http.api.mapping.MappedObject;

import java.net.http.HttpResponse;

@Description("Response Body. DTO. Described by json-formatted string")
public final class ResponseDTOCaptor extends StringCaptor<HttpResponse<MappedObject>> implements BaseResponseObjectBodyCaptor<MappedObject> {

    @Override
    public StringBuilder getData(HttpResponse<MappedObject> caught) {
        try {
            return new StringBuilder(caught.body().toString());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public HttpResponse<MappedObject> getCaptured(Object toBeCaptured) {
        return getCaptured(toBeCaptured, MappedObject.class);
    }
}
