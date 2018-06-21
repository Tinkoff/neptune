package com.github.toy.constructor.core.api;

import java.lang.reflect.Method;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

import static com.github.toy.constructor.core.api.SPIUtil.loadSPI;
import static com.github.toy.constructor.core.api.properties.DoCapturesOf.catchFailureEvent;
import static com.github.toy.constructor.core.api.properties.DoCapturesOf.catchSuccessEvent;
import static java.lang.String.format;
import static java.util.Optional.ofNullable;

@SuppressWarnings("unchecked")
public class CaptorStatic {
    private static final ThreadLocal<List<Captor>> LIST_THREAD_LOCAL = new ThreadLocal<>();

    public static <T> void catchResult(T caught, String message) {
        if (caught == null) {
            return;
        }

        List<Captor> captorList = ofNullable(LIST_THREAD_LOCAL.get()).orElseGet(() -> {
            List<Captor> captorss = loadSPI(Captor.class);
            LIST_THREAD_LOCAL.set(captorss);
            return captorss;
        });

        captorList.stream().filter(captor -> {
            Class<? extends Captor> captorClass = captor.getClass();
            Method[] methods = captorClass.getDeclaredMethods();

            for (Method m : methods) {
                Class<?>[] types = m.getParameterTypes();
                if ("doCapture".equals(m.getName()) && types.length == 2
                        && types[0].isAssignableFrom(caught.getClass()) && String.class.equals(types[1])) {
                    return true;
                }
            }
            return false;
        }).forEach(captor -> captor.doCapture(caught, message));
    }

    static <T, S> T catchResult(S input, Function<S, T> function) {
        try {
            T result = function.apply(input);
            if (catchSuccessEvent()) {
                catchResult(result, format("Getting of '%s' succeed", function.toString()));
            }
            return result;
        }
        catch (Throwable e){
            if (catchFailureEvent()) {
                catchResult(input, format("Getting of '%s' failed", function.toString()));
            }
            throw e;
        }
    }

    static <T> void catchResult(T input, Consumer<T> consumer) {
        try {
            consumer.accept(input);
            if (catchSuccessEvent()) {
                catchResult(input, format("Performing of '%s' succeed", consumer.toString()));
            }
        }
        catch (Throwable e) {
            if (catchFailureEvent()) {
                catchResult(input, format("'%s' failed", consumer.toString()));
            }
            throw e;
        }
    }
}
