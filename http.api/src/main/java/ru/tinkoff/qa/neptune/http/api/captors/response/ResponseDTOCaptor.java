package ru.tinkoff.qa.neptune.http.api.captors.response;

import ru.tinkoff.qa.neptune.core.api.event.firing.captors.StringCaptor;
import ru.tinkoff.qa.neptune.http.api.mapping.MappedObject;

import java.net.http.HttpResponse;

public final class ResponseDTOCaptor extends StringCaptor<HttpResponse<MappedObject>> implements BaseResponseObjectBodyCaptor<MappedObject> {

    public ResponseDTOCaptor() {
        super("Response Body. DTO. Described as JSON string");
    }

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
