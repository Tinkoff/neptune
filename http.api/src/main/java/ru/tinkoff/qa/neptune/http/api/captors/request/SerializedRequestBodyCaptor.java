package ru.tinkoff.qa.neptune.http.api.captors.request;

import ru.tinkoff.qa.neptune.core.api.event.firing.captors.StringCaptor;
import ru.tinkoff.qa.neptune.http.api.request.body.SerializedBody;

public final class SerializedRequestBodyCaptor extends StringCaptor<SerializedBody> implements BaseRequestBodyCaptor<SerializedBody> {

    public SerializedRequestBodyCaptor() {
        super("Request body. Serialized object(s)");
    }

    @Override
    public StringBuilder getData(SerializedBody caught) {
        return new StringBuilder(caught.toString());
    }

    @Override
    public SerializedBody getCaptured(Object toBeCaptured) {
        return getCaptured(toBeCaptured, SerializedBody.class);
    }
}
