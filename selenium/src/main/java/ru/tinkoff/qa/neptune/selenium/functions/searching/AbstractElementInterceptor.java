package ru.tinkoff.qa.neptune.selenium.functions.searching;

import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import org.openqa.selenium.WebElement;
import ru.tinkoff.qa.neptune.selenium.api.widget.ScrollsIntoView;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static java.util.Optional.ofNullable;

abstract class AbstractElementInterceptor implements MethodInterceptor {

    final WebElement element;
    private boolean isScrollerSetUp;

    Object realObject;
    ScrollsIntoView scrollsIntoView;

    AbstractElementInterceptor(WebElement element) {
        this.element = element;
    }

    public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
        realObject = ofNullable(realObject)
                .orElseGet(this::createRealObject);

        if (!isScrollerSetUp) {
            setScroller();
            isScrollerSetUp = true;
        }

        Class<?>[] parameters;
        if ("equals".equals(method.getName()) && (parameters = method.getParameterTypes()).length == 1
                && parameters[0].equals(Object.class)) {
            var result = realObject.equals(args[0]);
            //it may be another proxy
            if (!result) {
                result = ofNullable(args[0])
                        .map(o -> o.equals(realObject))
                        .orElse(false);
                //(boolean) proxy.invokeSuper(obj, args);
            }
            return result;
        }

        if ("getClass".equals(method.getName()) && (method.getParameterTypes()).length == 0) {
           return realObject.getClass();
        }

        if (toPerformTheScrolling(method)) {
            ofNullable(scrollsIntoView)
                    .ifPresent(ScrollsIntoView::scrollIntoView);
        }

        try {
            return method.invoke(realObject, args);
        }
        catch (InvocationTargetException e) {
            var cause = e.getCause();
            if (cause != null) {
                throw cause;
            }
            throw e;
        }
    }

    abstract void setScroller();

    abstract Object createRealObject();

    abstract boolean toPerformTheScrolling(Method method);
}
