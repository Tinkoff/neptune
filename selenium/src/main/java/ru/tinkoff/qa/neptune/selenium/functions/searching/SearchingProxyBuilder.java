package ru.tinkoff.qa.neptune.selenium.functions.searching;

import net.sf.cglib.proxy.Callback;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import org.objenesis.Objenesis;
import org.objenesis.ObjenesisStd;

import static net.sf.cglib.proxy.Enhancer.registerCallbacks;

class SearchingProxyBuilder {
    static  <T> T createProxy(Class<T> tClass, MethodInterceptor interceptor) {
        Enhancer enhancer = new Enhancer();

        enhancer.setUseCache(false);
        enhancer.setCallbackType(interceptor.getClass());
        enhancer.setSuperclass(tClass);
        Class<?> proxyClass = enhancer.createClass();
        registerCallbacks(proxyClass, new Callback[]{interceptor});
        enhancer.setClassLoader(tClass.getClassLoader());

        Objenesis objenesis = new ObjenesisStd();
        Object proxy = objenesis.newInstance(proxyClass);
        return (T) proxy;
    }
}
