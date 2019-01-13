package ru.tinkoff.qa.neptune.http.api.captors;

import ru.tinkoff.qa.neptune.core.api.event.firing.captors.StringCaptor;
import ru.tinkoff.qa.neptune.http.api.HowToGetResponse;

import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static java.lang.String.format;
import static org.apache.commons.lang3.time.DurationFormatUtils.formatDuration;

public class RequestStringCaptor extends StringCaptor<HttpRequest> {

    public RequestStringCaptor() {
        super("Request");
    }

    @Override
    public StringBuilder getData(HttpRequest caught) {
        var stringBuilder = new StringBuilder()
                .append(format("URI: %s \n", caught.uri()))
                .append(format("Method: %s \n", caught.method()))
                .append(format("Headers: %s \n", caught.headers()))
                .append(format("Expect continue: %s \n", caught.expectContinue()));

        caught.version().ifPresent(version -> stringBuilder.append(format("HTTP version: %s \n", version.name())));
        caught.timeout().ifPresent(duration -> stringBuilder.append(format("Duration: %s \n", formatDuration(duration.toMillis(),
                "**H:mm:ss**", true))));
        return stringBuilder;
    }

    @Override
    public HttpRequest getCaptured(Object toBeCaptured) {
        var clazz = toBeCaptured.getClass();
        if (HttpRequest.class.isAssignableFrom(clazz)) {
            return (HttpRequest) toBeCaptured;
        }

        if (HowToGetResponse.class.isAssignableFrom(clazz)) {
            return ((HowToGetResponse) toBeCaptured).getRequest();
        }

        if (HttpResponse.class.isAssignableFrom(clazz)) {
            return ((HttpResponse) toBeCaptured).request();
        }

        return null;
    }
}
