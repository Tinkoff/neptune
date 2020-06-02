package ru.tinkoff.qa.neptune.http.api.captors.request;

import ru.tinkoff.qa.neptune.core.api.event.firing.captors.StringCaptor;
import ru.tinkoff.qa.neptune.http.api.request.body.StringBody;

import static java.util.Optional.ofNullable;

public final class StringRequestBodyCaptor extends StringCaptor<StringBody> implements BaseRequestBodyCaptor<StringBody> {

    public StringRequestBodyCaptor() {
        super("Request body. String value");
    }

    @Override
    public StringBuilder getData(StringBody caught) {
        return new StringBuilder(caught.body());
    }

    @Override
    public StringBody getCaptured(Object toBeCaptured) {
        var stringBody = getCaptured(toBeCaptured, StringBody.class);

        return ofNullable(stringBody)
                .map(b -> {
                    if (b.body().length() > 50) {
                        return b;
                    }
                    return null;
                }).orElse(null);
    }
}
