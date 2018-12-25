package ru.tinkoff.qa.neptune.http.api.captors;

import ru.tinkoff.qa.neptune.core.api.event.firing.captors.StringCaptor;

import java.net.http.HttpResponse;

import static java.lang.String.format;
import static org.apache.commons.lang3.time.DurationFormatUtils.formatDuration;

public class ResponseCaptor extends StringCaptor<HttpResponse<?>> {

    @Override
    protected StringBuilder getData(HttpResponse<?> caught) {
        var stringBuilder = new StringBuilder("Response: \n");
        stringBuilder.append(format("Status code: %s\n", caught.statusCode()));

        var request = caught.request();

        stringBuilder.append("Request data: \n")
                .append(format(" - URI: %s \n", request.uri()))
                .append(format(" - Method: %s \n", request.method()))
                .append(format(" - Headers: %s \n", request.headers()))
                .append(format(" - Expect continue: %s \n", request.expectContinue()));

        request.version().ifPresent(version -> stringBuilder.append(format(" - HTTP version: %s \n", version.name())));
        request.timeout().ifPresent(duration -> stringBuilder.append(format(" - Duration: %s \n", formatDuration(duration.toMillis(),
                "**H:mm:ss**", true))));

        return stringBuilder;
    }

    @Override
    public HttpResponse<?> getCaptured(Object toBeCaptured) {
        if (HttpResponse.class.isAssignableFrom(toBeCaptured.getClass())) {
            return (HttpResponse<?>) toBeCaptured;
        }
        return null;
    }
}
