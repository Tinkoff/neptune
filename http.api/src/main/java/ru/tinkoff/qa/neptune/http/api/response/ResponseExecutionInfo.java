package ru.tinkoff.qa.neptune.http.api.response;

import java.net.http.HttpResponse;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

public final class ResponseExecutionInfo {

    private static final Logger LOGGER = Logger.getLogger("jdk.httpclient.HttpClient");
    private final LinkedList<RequestResponseLogCollector> collectors = new LinkedList<>();
    private HttpResponse<?> lastReceived;

    void startExecutionLogging() {
        var collector = new RequestResponseLogCollector();
        collectors.addLast(collector);
        LOGGER.addHandler(collector);
    }

    public HttpResponse<?> getLastReceived() {
        return lastReceived;
    }

    void setLastReceived(HttpResponse<?> lastReceived) {
        this.lastReceived = lastReceived;
    }

    void stopExecutionLogging() {
        LOGGER.removeHandler(collectors.getLast());
    }

    public List<RequestResponseLogCollector> getLogs() {
        return collectors;
    }
}
