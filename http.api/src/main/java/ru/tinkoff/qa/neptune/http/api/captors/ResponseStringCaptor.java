package ru.tinkoff.qa.neptune.http.api.captors;

import ru.tinkoff.qa.neptune.core.api.event.firing.captors.StringCaptor;

import java.net.http.HttpResponse;

import static org.apache.commons.lang3.StringUtils.isBlank;

public class ResponseStringCaptor extends StringCaptor<HttpResponse<String>> implements BaseResponseObjectBodyCaptor<String> {

    public ResponseStringCaptor() {
        super("Response Body. String");
    }

    @Override
    public StringBuilder getData(HttpResponse<String> caught) {
        var body = caught.body();
        var stringBuilder = new StringBuilder();

        if (isBlank(body)) {
            stringBuilder.append("<EMPTY STRING>");
        } else {
            stringBuilder.append(body);
        }

        return stringBuilder;
    }

    @Override
    public HttpResponse<String> getCaptured(Object toBeCaptured) {
        return getCaptured(toBeCaptured, String.class);
    }
}
