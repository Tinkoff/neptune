package ru.tinkoff.qa.neptune.selenium.functions.searching;

import net.sf.cglib.proxy.Callback;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import org.objenesis.ObjenesisStd;
import ru.tinkoff.qa.neptune.core.api.LoggableObject;

import static net.sf.cglib.proxy.Enhancer.registerCallbacks;

@SuppressWarnings("unchecked")
class SearchingProxyBuilder {
    static  <T> T createProxy(Class<T> tClass, MethodInterceptor interceptor) {
        var enhancer = new Enhancer();

        enhancer.setUseCache(false);
        enhancer.setCallbackType(interceptor.getClass());
        enhancer.setSuperclass(tClass);

        if (!LoggableObject.class.isAssignableFrom(tClass)) {
            enhancer.setInterfaces(new Class[]{LoggableObject.class});
        }

        var proxyClass = enhancer.createClass();
        registerCallbacks(proxyClass, new Callback[]{interceptor});
        enhancer.setClassLoader(tClass.getClassLoader());

        var objenesis = new ObjenesisStd();
        var proxy = objenesis.newInstance(proxyClass);
        return (T) proxy;
    }
}
