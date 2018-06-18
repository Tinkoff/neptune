package com.github.toy.constructor.core.api;

import java.lang.reflect.Method;
import java.util.List;
import java.util.ServiceLoader;

import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;

@SuppressWarnings("unchecked")
public class StaticLogger {
    private static final ThreadLocal<List<Logger>> LIST_THREAD_LOCAL = new ThreadLocal<>();

    private static List<Logger> loadSPI() {
        return ServiceLoader.load(Logger.class)
                .stream()
                .map(ServiceLoader.Provider::get).collect(toList());
    }

    public static <T> void log(T objectToLog, String message) {
        if (objectToLog == null) {
            return;
        }

        List<Logger> loggerList = ofNullable(LIST_THREAD_LOCAL.get()).orElseGet(() -> {
            List<Logger> loggers = loadSPI();
            LIST_THREAD_LOCAL.set(loggers);
            return loggers;
        });

        loggerList.stream().filter(logger -> {
            Class<? extends Logger> loggerClass = logger.getClass();
            Method[] methods = loggerClass.getDeclaredMethods();

            for (Method m : methods) {
                Class<?>[] types = m.getParameterTypes();
                if ("log".equals(m.getName()) && types.length == 2
                        && types[0].isAssignableFrom(objectToLog.getClass()) && String.class.equals(types[1])) {
                    return true;
                }
            }
            return false;
        }).forEach(logger -> logger.log(objectToLog, message));
    }
}
