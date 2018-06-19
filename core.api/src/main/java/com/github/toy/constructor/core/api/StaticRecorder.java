package com.github.toy.constructor.core.api;

import java.lang.reflect.Method;
import java.util.List;
import java.util.ServiceLoader;
import java.util.function.Consumer;
import java.util.function.Function;

import static java.lang.String.format;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;

@SuppressWarnings("unchecked")
public class StaticRecorder {
    private static final ThreadLocal<List<ReportLogger>> LIST_THREAD_LOCAL = new ThreadLocal<>();

    private static List<ReportLogger> loadSPI() {
        return ServiceLoader.load(ReportLogger.class)
                .stream()
                .map(ServiceLoader.Provider::get).collect(toList());
    }

    public static <T> void recordResult(T objectToLog, String message) {
        if (objectToLog == null) {
            return;
        }

        List<ReportLogger> loggerList = ofNullable(LIST_THREAD_LOCAL.get()).orElseGet(() -> {
            List<ReportLogger> loggers = loadSPI();
            LIST_THREAD_LOCAL.set(loggers);
            return loggers;
        });

        loggerList.stream().filter(logger -> {
            Class<? extends ReportLogger> loggerClass = logger.getClass();
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

    static <T, S> T recordResult(S input, Function<S, T> function) {
        try {
            T result = function.apply(input);
            recordResult(result, format("Getting of '%s' succeed", function.toString()));
            return result;
        }
        catch (Throwable e){
            recordResult(input, format("Getting of '%s' failed", function.toString()));
            throw e;
        }
    }

    static <T> void recordResult(T input, Consumer<T> consumer) {
        try {
            consumer.accept(input);
            recordResult(input, format("Performing of '%s' succeed", consumer.toString()));
        }
        catch (Throwable e) {
            recordResult(input, format("'%s' failed", consumer.toString()));
            throw e;
        }
    }
}
