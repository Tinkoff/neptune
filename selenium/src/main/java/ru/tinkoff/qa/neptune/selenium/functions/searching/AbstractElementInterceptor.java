package ru.tinkoff.qa.neptune.selenium.functions.searching;

import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.internal.FindsByLinkText;

import java.lang.reflect.Method;

import static java.util.Optional.ofNullable;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.ScrollsIntoView.ScrollWebElementIntoView.getDefaultScrollerIntoView;

abstract class AbstractElementInterceptor implements MethodInterceptor {

    final String description;
    final WebElement element;

    Object realObject;
    ScrollsIntoView scrollsIntoView;

    AbstractElementInterceptor(String description, WebElement element) {
        this.description = description;
        this.element = element;
    }

    private static boolean isDeclaredBy(Method method, Class<?> supposedToDeclare) {
        try {
            supposedToDeclare.getMethod(method.getName(), method.getParameterTypes());
            return true;
        } catch (NoSuchMethodException e) {
            return false;
        }
    }

    public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
        if ("toString".equals(method.getName()) &&
                method.getParameterTypes().length == 0
                && String.class.equals(method.getReturnType())) {
            return description;
        }

        realObject = createRealObject();

        Class<?>[] parameters;
        if ("equals".equals(method.getName()) && (parameters = method.getParameterTypes()).length == 1
                && parameters[0].equals(Object.class)) {
            var result = realObject.equals(args[0]);
            //it may be another proxy
            if (!result) {
                result = (boolean) proxy.invokeSuper(obj, args);
            }
            return result;
        }

        if (!isDeclaredBy(method, Object.class)
                && !isDeclaredBy(method, SearchContext.class)) {
            ofNullable(scrollsIntoView)
                    .ifPresent(ScrollsIntoView::scrollsIntoView);
        }

        return method.invoke(realObject, args);
    }

    void setScroller() {
        try {
            scrollsIntoView = getDefaultScrollerIntoView(element);
        } catch (Throwable throwable) {
            throw new RuntimeException(throwable);
        }
    }

    abstract Object createRealObject();
}
