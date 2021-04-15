package ru.tinkoff.qa.neptune.core.api.event.firing;

import ru.tinkoff.qa.neptune.core.api.utils.SPIUtil;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import static java.util.Optional.ofNullable;

public class StaticEventFiring {
    private static final ThreadLocal<List<EventLogger>> LIST_THREAD_LOCAL_EVENT_LOGGERS = new ThreadLocal<>();

    public static <T> void catchValue(T caught, Collection<Captor<Object, Object>> captorList) {
        if (caught == null) {
            return;
        }

        ofNullable(captorList).ifPresent(captors -> {
            for (var captor : captors) {
                var captured = captor.getCaptured(caught);
                if (captured != null) {
                    captor.capture(captured);
                }
            }
        });
    }

    private static List<EventLogger> initEventLoggersIfNecessary() {
        return ofNullable(LIST_THREAD_LOCAL_EVENT_LOGGERS.get())
                .orElseGet(() -> {
                    var loggers = SPIUtil.loadSPI(EventLogger.class);
                    LIST_THREAD_LOCAL_EVENT_LOGGERS.set(loggers);
                    return loggers;
                });
    }

    public static void fireEventStarting(String message, Map<String, String> parameters) {
        initEventLoggersIfNecessary().forEach(eventLogger ->
                eventLogger.fireTheEventStarting(message, parameters));
    }

    public static void fireThrownException(Throwable throwable) {
        initEventLoggersIfNecessary().forEach(eventLogger ->
                eventLogger.fireThrownException(throwable));
    }

    public static void fireReturnedValue(String resultDescription, Object returned) {
        initEventLoggersIfNecessary().forEach(eventLogger ->
                eventLogger.fireReturnedValue(resultDescription, returned));
    }

    public static void fireEventFinishing() {
        initEventLoggersIfNecessary().forEach(EventLogger::fireEventFinishing);
    }
}
