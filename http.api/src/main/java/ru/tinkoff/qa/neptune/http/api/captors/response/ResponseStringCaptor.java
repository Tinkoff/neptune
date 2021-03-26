package ru.tinkoff.qa.neptune.http.api.captors.response;

import ru.tinkoff.qa.neptune.core.api.event.firing.captors.StringCaptor;
import ru.tinkoff.qa.neptune.core.api.steps.Description;

import java.net.http.HttpResponse;

import static org.apache.commons.lang3.StringUtils.isBlank;

@Description("Response Body. String")
public final class ResponseStringCaptor extends StringCaptor<HttpResponse<String>> implements BaseResponseObjectBodyCaptor<String> {

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
