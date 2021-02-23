package ru.tinkoff.qa.neptune.selenium.functions.searching;

import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.WrapsDriver;
import ru.tinkoff.qa.neptune.selenium.auto.scrolling.AutoScroller;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static java.util.Optional.ofNullable;
import static ru.tinkoff.qa.neptune.selenium.properties.DefaultScrollerProperty.DEFAULT_SCROLLER_PROPERTY;

abstract class AbstractElementInterceptor implements MethodInterceptor {

    final WebElement element;

    Object realObject;
    private final AutoScroller defaultScroller;

    AbstractElementInterceptor(WebElement element) {
        this.element = element;
        defaultScroller = getAutoScroller(element);
    }

    private static AutoScroller getAutoScroller(WebElement element) {
        return ofNullable(DEFAULT_SCROLLER_PROPERTY.get())
                .map(aClass -> {
                    try {
                        var c = aClass.getDeclaredConstructor(WebDriver.class);
                        c.setAccessible(true);
                        return (AutoScroller) c.newInstance(((WrapsDriver) element).getWrappedDriver());
                    } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
                        throw new RuntimeException(e);
                    }
                })
                .orElseGet(() -> new AutoScroller(((WrapsDriver) element).getWrappedDriver()) {
                    @Override
                    protected void scrollIntoView(WebElement e) {
                        //The default scrolling won't be performed
                    }
                });
    }

    public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
        realObject = ofNullable(realObject).orElseGet(this::createRealObject);

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
            ofNullable(defaultScroller)
                    .ifPresent(autoScroller -> autoScroller.scrollIntoView(realObject));
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

    abstract Object createRealObject();

    abstract boolean toPerformTheScrolling(Method method);
}
