package ru.tinkoff.qa.neptune.core.api.utils;

import net.sf.cglib.proxy.Callback;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import org.objenesis.ObjenesisStd;
import ru.tinkoff.qa.neptune.core.api.LoggableObject;

import static net.sf.cglib.proxy.Enhancer.registerCallbacks;

public class CGLibProxyBuilder {

    /**
     * Creates a proxy object using the binding of CGLIB and Objenesis
     *
     * @param tClass is a class of a proxy
     * @param interceptor is an instance of {@link MethodInterceptor}
     * @param additionalInterfacesToImplement is an array of interfaces to be implemented by a proxy
     * @param <T> is a type of a proxy
     * @return the resulted object
     */
    public static  <T> T createProxy(Class<T> tClass, MethodInterceptor interceptor, Class<?>... additionalInterfacesToImplement) {
        var enhancer = new Enhancer();

        enhancer.setUseCache(false);
        enhancer.setCallbackType(interceptor.getClass());
        enhancer.setSuperclass(tClass);

        if (!LoggableObject.class.isAssignableFrom(tClass)) {
            enhancer.setInterfaces(additionalInterfacesToImplement);
        }

        var proxyClass = enhancer.createClass();
        registerCallbacks(proxyClass, new Callback[]{interceptor});
        enhancer.setClassLoader(tClass.getClassLoader());

        var objenesis = new ObjenesisStd();
        var proxy = objenesis.newInstance(proxyClass);
        return (T) proxy;
    }
}
