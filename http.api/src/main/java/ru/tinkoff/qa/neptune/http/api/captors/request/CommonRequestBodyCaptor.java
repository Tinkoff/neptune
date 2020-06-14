package ru.tinkoff.qa.neptune.http.api.captors.request;

import ru.tinkoff.qa.neptune.core.api.event.firing.captors.StringCaptor;
import ru.tinkoff.qa.neptune.http.api.request.body.*;

import static java.util.List.of;

public class CommonRequestBodyCaptor extends StringCaptor<RequestBody<?>> implements BaseRequestBodyCaptor {

    public CommonRequestBodyCaptor() {
        super("Request body.");
    }

    @Override
    public StringBuilder getData(RequestBody<?> caught) {
        return new StringBuilder(caught.toString());
    }

    @Override
    public RequestBody<?> getCaptured(Object toBeCaptured) {
        return getCaptured(toBeCaptured, of(JSoupDocumentBody.class,
                SerializedBody.class,
                URLEncodedForm.class,
                W3CDocumentBody.class,
                MultiPartBody.class));
    }
}
