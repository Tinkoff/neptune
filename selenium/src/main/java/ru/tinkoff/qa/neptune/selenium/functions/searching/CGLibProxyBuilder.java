package ru.tinkoff.qa.neptune.selenium.functions.searching;

import net.sf.cglib.proxy.Callback;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import org.objenesis.ObjenesisStd;

import static net.sf.cglib.proxy.Enhancer.registerCallbacks;

class CGLibProxyBuilder {

    /**
     * Creates a proxy object using the binding of CGLIB and Objenesis
     *
     * @param tClass is a class of a proxy
     * @param interceptor is an instance of {@link MethodInterceptor}
     * @param <T> is a type of a proxy
     * @return the resulted object
     */
    @SuppressWarnings("unchecked")
    static  <T> T createProxy(Class<T> tClass, MethodInterceptor interceptor) {
        var enhancer = new Enhancer();

        enhancer.setUseCache(false);
        enhancer.setCallbackType(interceptor.getClass());
        enhancer.setSuperclass(tClass);

        var proxyClass = enhancer.createClass();
        registerCallbacks(proxyClass, new Callback[]{interceptor});
        enhancer.setClassLoader(tClass.getClassLoader());

        var objenesis = new ObjenesisStd();
        var proxy = objenesis.newInstance(proxyClass);
        return (T) proxy;
    }
}
