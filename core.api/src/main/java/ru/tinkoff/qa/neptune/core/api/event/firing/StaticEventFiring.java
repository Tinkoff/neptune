package ru.tinkoff.qa.neptune.core.api.event.firing;

import ru.tinkoff.qa.neptune.core.api.event.firing.annotation.CaptorFilterByProducedType;
import ru.tinkoff.qa.neptune.core.api.utils.SPIUtil;

import java.util.List;
import java.util.Set;

import static java.util.Optional.ofNullable;

@SuppressWarnings("unchecked")
public class StaticEventFiring {
    private static final ThreadLocal<List<Captor>> LIST_THREAD_LOCAL_CAPTORS = new ThreadLocal<>();
    private static final ThreadLocal<List<EventLogger>> LIST_THREAD_LOCAL_EVENT_LOGGERS = new ThreadLocal<>();

    private static List<Captor> getCaptors() {
        return ofNullable(LIST_THREAD_LOCAL_CAPTORS.get()).orElseGet(() -> {
            var captors = SPIUtil.loadSPI(Captor.class);
            LIST_THREAD_LOCAL_CAPTORS.set(captors);
            return captors;
        });
    }

    public static void addCaptors(List<Captor<?, ?>> captors) {
        getCaptors().addAll(captors);
    }

    public static <T> void catchValue(T caught, Set<CaptorFilterByProducedType> captorFilters) {
        if (caught == null) {
            return;
        }

        getCaptors().stream().filter(captor -> {
            for (CaptorFilterByProducedType captorFilterByProducedType : captorFilters) {
                if (captorFilterByProducedType.matches(captor)) {
                    return true;
                }
            }
            return false;
        }).forEach(captor -> ofNullable(captor.getCaptured(caught)).ifPresent(captor::capture));
    }

    private static List<EventLogger> initEventLoggersIfNecessary() {
        return ofNullable(LIST_THREAD_LOCAL_EVENT_LOGGERS.get())
                .orElseGet(() -> {
                    var loggers = SPIUtil.loadSPI(EventLogger.class);
                    LIST_THREAD_LOCAL_EVENT_LOGGERS.set(loggers);
                    return loggers;
                });
    }

    public static void fireEventStarting(String message) {
        initEventLoggersIfNecessary().forEach(eventLogger ->
                eventLogger.fireTheEventStarting(message));
    }

    public static void fireThrownException(Throwable throwable) {
        initEventLoggersIfNecessary().forEach(eventLogger ->
                eventLogger.fireThrownException(throwable));
    }

    public static void fireReturnedValue(Object returned) {
        initEventLoggersIfNecessary().forEach(eventLogger ->
                eventLogger.fireReturnedValue(returned));
    }

    public static void fireEventFinishing() {
        initEventLoggersIfNecessary().forEach(EventLogger::fireEventFinishing);
    }
}
