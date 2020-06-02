package ru.tinkoff.qa.neptune.http.api.captors.request;

import ru.tinkoff.qa.neptune.core.api.event.firing.captors.StringCaptor;
import ru.tinkoff.qa.neptune.http.api.request.body.W3CDocumentBody;

public final class W3CDocumentRequestBodyCaptor extends StringCaptor<W3CDocumentBody> implements BaseRequestBodyCaptor<W3CDocumentBody> {

    public W3CDocumentRequestBodyCaptor() {
        super("Request body. XML (W3C) document");
    }

    @Override
    public StringBuilder getData(W3CDocumentBody caught) {
        return new StringBuilder(caught.toString());
    }

    @Override
    public W3CDocumentBody getCaptured(Object toBeCaptured) {
        return getCaptured(toBeCaptured, W3CDocumentBody.class);
    }
}
