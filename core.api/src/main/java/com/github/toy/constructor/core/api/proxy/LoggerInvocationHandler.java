package com.github.toy.constructor.core.api.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.List;

class LoggerInvocationHandler implements InvocationHandler {
    private final List<Logger> listeners;

    LoggerInvocationHandler(List<Logger> listeners) {
        this.listeners = listeners;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        for (Logger logger: listeners) {
            method.invoke(logger, args);
        }
        return null;
    }
}
