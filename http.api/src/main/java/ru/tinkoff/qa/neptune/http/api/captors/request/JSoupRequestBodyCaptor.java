package ru.tinkoff.qa.neptune.http.api.captors.request;

import ru.tinkoff.qa.neptune.core.api.event.firing.captors.StringCaptor;
import ru.tinkoff.qa.neptune.http.api.request.body.JSoupDocumentBody;

public final class JSoupRequestBodyCaptor extends StringCaptor<JSoupDocumentBody> implements BaseRequestBodyCaptor<JSoupDocumentBody> {

    public JSoupRequestBodyCaptor() {
        super("Request body. Html (or xml) document");
    }

    @Override
    public StringBuilder getData(JSoupDocumentBody caught) {
        return new StringBuilder(caught.toString());
    }

    @Override
    public JSoupDocumentBody getCaptured(Object toBeCaptured) {
        return getCaptured(toBeCaptured, JSoupDocumentBody.class);
    }
}
