package ru.tinkoff.qa.neptune.core.api.event.firing;

import ru.tinkoff.qa.neptune.core.api.event.firing.console.DefaultConsoleEventLogger;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.StreamSupport;

import static java.util.Optional.ofNullable;
import static java.util.ServiceLoader.load;
import static java.util.stream.Collectors.toList;

public class StaticEventFiring {

    private static final ThreadLocal<List<EventLogger>> LIST_THREAD_LOCAL_EVENT_LOGGERS = new ThreadLocal<>();

    private static List<EventLogger> getEventLoggers() {
        var result = new LinkedList<EventLogger>();
        result.addFirst(new DefaultConsoleEventLogger());
        var iterator = load(EventLogger.class).iterator();
        Iterable<EventLogger> iterable = () -> iterator;
        result.addAll(StreamSupport.stream(iterable.spliterator(), false).collect(toList()));
        return result;
    }


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
                    var loggers = getEventLoggers();
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

    public static void fireAdditionalParameters(Map<String, String> addParameters) {
        if (addParameters == null) {
            return;
        }

        if (addParameters.isEmpty()) {
            return;
        }
        initEventLoggersIfNecessary().forEach(eventLogger -> eventLogger.addParameters(addParameters));
    }
}
