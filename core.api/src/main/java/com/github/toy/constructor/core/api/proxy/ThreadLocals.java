package com.github.toy.constructor.core.api.proxy;

import java.util.HashMap;
import java.util.Map;

import static java.util.Optional.ofNullable;

public final class ThreadLocals {
    private final static Map<Class<?>, ThreadLocal<?>> threadLocalMap = new HashMap<>();

    static <T> T setNewInstance(Class<T> clazz, T instance) {
        ThreadLocal<T> threadLocal;
        if ((threadLocal = (ThreadLocal<T>) threadLocalMap.get(clazz)) == null) {
            threadLocal = new ThreadLocal<>();
            threadLocal.set(instance);
            threadLocalMap.put(clazz, threadLocal);
        }
        else {
            threadLocal.set(instance);
        }
        return instance;
    }

    static <T> T get(Class<T> clazz) {
        return ofNullable((ThreadLocal<T>) threadLocalMap.get(clazz))
                .map(ThreadLocal::get).orElse(null);
    }
}