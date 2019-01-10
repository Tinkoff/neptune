package ru.tinkoff.qa.neptune.http.api.captors;

import ru.tinkoff.qa.neptune.core.api.event.firing.captors.StringCaptor;

import java.net.http.HttpResponse;

import static java.lang.String.format;
import static org.apache.commons.lang3.time.DurationFormatUtils.formatDuration;

public class ResponseCaptor extends StringCaptor<HttpResponse<?>> {

    public ResponseCaptor() {
        super("Response");
    }

    @Override
    public StringBuilder getData(HttpResponse<?> caught) {
        var request = caught.request();
        var stringBuilder = new StringBuilder()
                .append(format("Status code: %s\n", caught.statusCode()))
                .append("Request data: \n")
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
