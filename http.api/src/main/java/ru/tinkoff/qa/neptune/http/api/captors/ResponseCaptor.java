package ru.tinkoff.qa.neptune.http.api.captors;

import ru.tinkoff.qa.neptune.core.api.event.firing.captors.StringCaptor;

import java.net.http.HttpResponse;

import static java.lang.String.format;
import static java.lang.System.lineSeparator;
import static org.apache.commons.lang3.time.DurationFormatUtils.formatDuration;

public class ResponseCaptor extends StringCaptor<HttpResponse<?>> {

    private static final String LINE_SEPARATOR = lineSeparator();

    public ResponseCaptor() {
        super("Response");
    }

    @Override
    public StringBuilder getData(HttpResponse<?> caught) {
        var request = caught.request();
        var stringBuilder = new StringBuilder()
                .append(format("Status code: %s%s", caught.statusCode(), LINE_SEPARATOR))
                .append("Request data: ").append(LINE_SEPARATOR)
                .append(format(" - URI: %s %s", request.uri(), LINE_SEPARATOR))
                .append(format(" - Method: %s %s", request.method(), LINE_SEPARATOR))
                .append(format(" - Headers: %s %s", request.headers(), LINE_SEPARATOR))
                .append(format(" - Expect continue: %s %s", request.expectContinue(), LINE_SEPARATOR));

        request.version().ifPresent(version -> stringBuilder.append(format(" - HTTP version: %s %s", version.name(), LINE_SEPARATOR)));
        request.timeout().ifPresent(duration -> stringBuilder.append(format(" - Duration: %s %s", formatDuration(duration.toMillis(),
                "**H:mm:ss**", true), LINE_SEPARATOR)));

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
