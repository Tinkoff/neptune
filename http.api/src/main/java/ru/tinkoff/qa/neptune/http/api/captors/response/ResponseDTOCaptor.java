package ru.tinkoff.qa.neptune.http.api.captors.response;

import ru.tinkoff.qa.neptune.core.api.event.firing.captors.StringCaptor;
import ru.tinkoff.qa.neptune.http.api.dto.DTObject;

import java.net.http.HttpResponse;

public final class ResponseDTOCaptor extends StringCaptor<HttpResponse<DTObject>> implements BaseResponseObjectBodyCaptor<DTObject> {

    public ResponseDTOCaptor() {
        super("Response Body. Dto");
    }

    @Override
    public StringBuilder getData(HttpResponse<DTObject> caught) {
        try {
            return new StringBuilder(caught.toString());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public HttpResponse<DTObject> getCaptured(Object toBeCaptured) {
        return getCaptured(toBeCaptured, DTObject.class);
    }
}
