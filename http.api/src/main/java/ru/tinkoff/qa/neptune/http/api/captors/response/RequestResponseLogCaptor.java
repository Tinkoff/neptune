package ru.tinkoff.qa.neptune.http.api.captors.response;

import ru.tinkoff.qa.neptune.core.api.event.firing.captors.FileCaptor;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;
import ru.tinkoff.qa.neptune.http.api.response.RequestResponseLogCollector;
import ru.tinkoff.qa.neptune.http.api.response.ResponseExecutionInfo;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.logging.SimpleFormatter;

import static java.io.File.createTempFile;
import static java.util.UUID.randomUUID;

@Description("Logs that have been captured during the sending of a request")
public final class RequestResponseLogCaptor extends FileCaptor<List<RequestResponseLogCollector>> {

    private static final String LINE_SEPARATOR = "\r\n";

    @Override
    public File getData(List<RequestResponseLogCollector> caught) {
        var result = new StringBuilder();
        var randomUUID = randomUUID().toString();
        var logMessageFormatter = new SimpleFormatter();

        int i = 0;
        for (var log : caught) {
            i++;
            result.append("Log #").append(i).append(LINE_SEPARATOR);
            log.getCollected().forEach(logRecord -> result
                    .append(logMessageFormatter.format(logRecord))
                    .append(LINE_SEPARATOR));
        }
        try {
            var tmpFile = createTempFile(randomUUID, ".txt");
            var writer = new FileWriter(tmpFile);
            writer.write(result.toString());
            writer.close();
            tmpFile.deleteOnExit();
            return tmpFile;
        } catch (IOException e) {
            return null;
        }
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
