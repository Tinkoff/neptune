package ru.tinkoff.qa.neptune.core.api.event.firing;

import io.github.classgraph.ClassGraph;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import static java.lang.reflect.Modifier.isAbstract;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toUnmodifiableList;

public class StaticEventFiring {

    private static final ThreadLocal<List<EventLogger>> LIST_THREAD_LOCAL_EVENT_LOGGERS = new ThreadLocal<>();
    private static final List<Class<? extends EventLogger>> LOGGERS = getEventLoggerClasses();

    private static List<Class<? extends EventLogger>> getEventLoggerClasses() {
        return new ClassGraph()
                .enableAllInfo()
                .scan().getClassesImplementing(EventLogger.class.getName())
                .loadClasses(EventLogger.class)
                .stream()
                .filter(c -> !isAbstract(c.getModifiers()))
                .collect(toUnmodifiableList());
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
                    var loggers = LOGGERS
                            .stream()
                            .filter(c -> !isAbstract(c.getModifiers()))
                            .map(c -> {
                                try {
                                    var constructor = c.getConstructor();
                                    constructor.setAccessible(true);
                                    return (EventLogger) constructor.newInstance();
                                } catch (Exception e) {
                                    throw new RuntimeException(e);
                                }
                            }).collect(toList());
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

        if (addParameters.size() == 0) {
            return;
        }
        initEventLoggersIfNecessary().forEach(eventLogger -> eventLogger.addParameters(addParameters));
    }
}
