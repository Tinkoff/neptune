package com.github.toy.constructor.core.api.event.firing;

import com.github.toy.constructor.core.api.event.firing.Captor;
import com.github.toy.constructor.core.api.event.firing.EventLogger;

import java.util.List;
import java.util.stream.Stream;

import static com.github.toy.constructor.core.api.utils.SPIUtil.loadSPI;
import static java.util.Optional.ofNullable;

@SuppressWarnings("unchecked")
public class StaticEventFiring {
    private static final ThreadLocal<List<Captor>> LIST_THREAD_LOCAL_CAPTORS = new ThreadLocal<>();
    private static final ThreadLocal<List<EventLogger>> LIST_THREAD_LOCAL_EVENT_LOGGERS = new ThreadLocal<>();

    private static List<Captor> getCaptors() {
        return  ofNullable(LIST_THREAD_LOCAL_CAPTORS.get()).orElseGet(() -> {
            List<Captor> captors = loadSPI(Captor.class);
            LIST_THREAD_LOCAL_CAPTORS.set(captors);
            return captors;
        });
    }

    private static <T> Stream<Captor> filter(T caught) {
        return getCaptors().stream().filter(captor -> captor.getTypeToBeCaptured()
                .isAssignableFrom(caught.getClass()));
    }

    public static void addCaptors(List<Captor<?, ?>> captors) {
        getCaptors().addAll(captors);
    }

    public static <T> void catchResult(T caught, String message) {
        if (caught == null) {
            return;
        }

        filter(caught).forEach(captor -> captor.capture(caught, message));
    }

    private static List<EventLogger> initEventLoggersIfNecessary() {
        return ofNullable(LIST_THREAD_LOCAL_EVENT_LOGGERS.get())
                .orElseGet(() -> {
                    List<EventLogger> loggers = loadSPI(EventLogger.class);
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
