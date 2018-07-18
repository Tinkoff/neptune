package ru.tinkoff.qa.neptune.selenium.functions.searching;

import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.lang.reflect.Method;

import static java.lang.String.format;
import static org.apache.commons.lang3.StringUtils.isBlank;

class WebElementInterceptor implements MethodInterceptor {

    private final WebElement element;
    private final By by;
    private final String description;

    WebElementInterceptor(WebElement element, By by, String description) {
        this.element = element;
        this.by = by;
        this.description = description;
    }

    @Override
    public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
        if ("toString".equals(method.getName()) &&
                method.getParameterTypes().length == 0
                && String.class.equals(method.getReturnType())) {
            String stringDescription = format("Web element found %s", by);
            if (!isBlank(description)) {
                stringDescription = format("%s on conditions '%s'", stringDescription, description);
            }
            return stringDescription;
        }

        Class<?>[] parameters;
        if ("equals".equals(method.getName()) && (parameters = method.getParameterTypes()).length == 1
                && parameters[0].equals(Object.class)) {
            boolean result = element.equals(args[0]);
            //it may be another proxy
            if (!result) {
                result = (boolean) proxy.invokeSuper(obj, args);
            }
            return result;
        }

        return method.invoke(element, args);
    }
}
