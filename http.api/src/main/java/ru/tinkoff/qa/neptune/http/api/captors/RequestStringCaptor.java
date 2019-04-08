package ru.tinkoff.qa.neptune.http.api.captors;

import ru.tinkoff.qa.neptune.core.api.event.firing.captors.StringCaptor;

import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static java.lang.String.format;
import static java.lang.System.lineSeparator;
import static org.apache.commons.lang3.time.DurationFormatUtils.formatDuration;

public class RequestStringCaptor extends StringCaptor<HttpRequest> {
    private static final String LINE_SEPARATOR = lineSeparator();

    public RequestStringCaptor() {
        this("Request");
    }

    public RequestStringCaptor(String desciption) {
        super(desciption);
    }

    @Override
    public StringBuilder getData(HttpRequest caught) {
        var stringBuilder = new StringBuilder()
                .append(format("URI: %s %s", caught.uri(), LINE_SEPARATOR))
                .append(format("Method: %s %s", caught.method(), LINE_SEPARATOR))
                .append(format("Headers: %s %s", caught.headers(), LINE_SEPARATOR))
                .append(format("Expect continue: %s %s", caught.expectContinue(), LINE_SEPARATOR));

        caught.version().ifPresent(version -> stringBuilder.append(format("HTTP version: %s %s", version.name(), LINE_SEPARATOR)));
        caught.timeout().ifPresent(duration -> stringBuilder.append(format("Duration: %s %s", formatDuration(duration.toMillis(),
                "**H:mm:ss**", true), LINE_SEPARATOR)));
        return stringBuilder;
    }

    @Override
    public HttpRequest getCaptured(Object toBeCaptured) {
        var clazz = toBeCaptured.getClass();
        if (HttpRequest.class.isAssignableFrom(clazz)) {
            return (HttpRequest) toBeCaptured;
        }

        if (HttpResponse.class.isAssignableFrom(clazz)) {
            return ((HttpResponse) toBeCaptured).request();
        }

        return null;
    }
}
