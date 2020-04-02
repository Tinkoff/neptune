package ru.tinkoff.qa.neptune.http.api.captors;

import ru.tinkoff.qa.neptune.core.api.event.firing.captors.StringCaptor;
import ru.tinkoff.qa.neptune.http.api.response.RequestResponseLogCollector;

import java.util.List;
import java.util.logging.LogRecord;
import java.util.logging.SimpleFormatter;

import static java.lang.System.lineSeparator;

public class RequestResponseLogCaptor extends StringCaptor<List<LogRecord>> {

    private static final String LINE_SEPARATOR = lineSeparator();

    public RequestResponseLogCaptor() {
        super("Logs that have been captured during the sending of a request");
    }

    @Override
    public StringBuilder getData(List<LogRecord> caught) {
        var result = new StringBuilder();
        var logMessageFormatter = new SimpleFormatter();
        caught.forEach(logRecord -> result.append(logMessageFormatter.format(logRecord)).append(LINE_SEPARATOR));
        return result;
    }

    @Override
    public List<LogRecord> getCaptured(Object toBeCaptured) {
        var clazz = toBeCaptured.getClass();

        List<LogRecord> result;
        if (RequestResponseLogCollector.class.isAssignableFrom(clazz)) {
            result = ((RequestResponseLogCollector) toBeCaptured).getCollected();
        } else {
            return null;
        }

        if (result.size() > 0) {
            return result;
        } else {
            return null;
        }
    }
}
