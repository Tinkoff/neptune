package ru.tinkoff.qa.neptune.http.api.captors;

import ru.tinkoff.qa.neptune.core.api.event.firing.captors.StringCaptor;
import ru.tinkoff.qa.neptune.http.api.response.RequestResponseLogCollector;
import ru.tinkoff.qa.neptune.http.api.response.ResponseExecutionInfo;

import java.util.List;
import java.util.logging.SimpleFormatter;

import static java.lang.System.lineSeparator;

public class RequestResponseLogCaptor extends StringCaptor<List<RequestResponseLogCollector>> {

    private static final String LINE_SEPARATOR = lineSeparator();

    public RequestResponseLogCaptor() {
        super("Logs that have been captured during the sending of a request");
    }

    @Override
    public StringBuilder getData(List<RequestResponseLogCollector> caught) {
        var result = new StringBuilder();
        var logMessageFormatter = new SimpleFormatter();

        int i = 0;
        for (var log : caught) {
            i++;
            result.append("Log #").append(i).append(LINE_SEPARATOR);
            log.getCollected().forEach(logRecord -> result
                    .append(logMessageFormatter.format(logRecord))
                    .append(LINE_SEPARATOR));
        }
        return result;
    }

    @Override
    public List<RequestResponseLogCollector> getCaptured(Object toBeCaptured) {
        var clazz = toBeCaptured.getClass();

        List<RequestResponseLogCollector> result;
        if (ResponseExecutionInfo.class.isAssignableFrom(clazz)) {
            result = ((ResponseExecutionInfo) toBeCaptured).getLogs();
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
