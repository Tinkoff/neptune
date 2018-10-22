package ru.tinkoff.qa.neptune.core.api.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * This class is designed to provide the ability to override {@link Object#toString} dynamically.
 * It is possible to use it in case of the using {@link Proxy#newProxyInstance(ClassLoader, Class[], InvocationHandler)}
 */
public final class ToStringDelegateInvocationHandler implements InvocationHandler {

    private final Object delegateTo;
    private final String toStringDelegate;

    private ToStringDelegateInvocationHandler(Object delegateTo, String toStringDelegate) {
        this.delegateTo = delegateTo;
        this.toStringDelegate = toStringDelegate;
    }

    public static ToStringDelegateInvocationHandler getToStringDelegateInvocationHandler(Object delegateTo, String toStringDelegate) {
        return new ToStringDelegateInvocationHandler(delegateTo, toStringDelegate);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (method.getName().equals("toString") && method.getParameterTypes().length == 0) {
            return toStringDelegate;
        }
        return method.invoke(delegateTo, args);
    }
}
