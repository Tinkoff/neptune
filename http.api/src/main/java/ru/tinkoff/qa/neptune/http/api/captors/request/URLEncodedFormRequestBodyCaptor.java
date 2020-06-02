package ru.tinkoff.qa.neptune.http.api.captors.request;

import ru.tinkoff.qa.neptune.core.api.event.firing.captors.StringCaptor;
import ru.tinkoff.qa.neptune.http.api.request.body.URLEncodedForm;

public final class URLEncodedFormRequestBodyCaptor extends StringCaptor<URLEncodedForm> implements BaseRequestBodyCaptor<URLEncodedForm> {

    public URLEncodedFormRequestBodyCaptor() {
        super("Request body. Form (URL-encoded)");
    }

    @Override
    public StringBuilder getData(URLEncodedForm caught) {
        return new StringBuilder(caught.toString());
    }

    @Override
    public URLEncodedForm getCaptured(Object toBeCaptured) {
        return getCaptured(toBeCaptured, URLEncodedForm.class);
    }
}
