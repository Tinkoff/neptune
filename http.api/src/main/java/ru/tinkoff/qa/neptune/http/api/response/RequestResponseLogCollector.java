package ru.tinkoff.qa.neptune.http.api.response;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

import static java.lang.Thread.currentThread;

public final class RequestResponseLogCollector extends Handler {

    private final List<LogRecord> collected = new ArrayList<>();

    @Override
    public void publish(LogRecord record) {
        // Re-direct log calls here (e.g. send record to Log4j2 logger).
        if (record.getThreadID() == (int) currentThread().getId()) {
            collected.add(record);
        }
    }

    @Override
    public void flush() {
    }

    @Override
    public void close() throws SecurityException {
    }

    public List<LogRecord> getCollected() {
        return collected;
    }
}
