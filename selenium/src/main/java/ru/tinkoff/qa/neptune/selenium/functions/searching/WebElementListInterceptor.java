package ru.tinkoff.qa.neptune.selenium.functions.searching;

import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.lang.reflect.Method;
import java.util.List;

import static java.lang.String.format;
import static org.apache.commons.lang3.StringUtils.isBlank;

class WebElementListInterceptor implements MethodInterceptor {

    private final List<WebElement> elements;
    private final By by;
    private final String description;

    WebElementListInterceptor(List<WebElement> elements, By by, String description) {
        this.elements = elements;
        this.by = by;
        this.description = description;
    }

    @Override
    public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
        if ("toString".equals(method.getName()) &&
                method.getParameterTypes().length == 0
                && String.class.equals(method.getReturnType())) {
            String stringDescription = format("%s web elements found %s", elements.size(), by);
            if (!isBlank(description)) {
                stringDescription = format("%s on conditions '%s'", stringDescription, description);
            }
            return stringDescription;
        }

        Class<?>[] parameters;
        if ("equals".equals(method.getName()) && (parameters = method.getParameterTypes()).length == 1
                && parameters[0].equals(Object.class)) {
            boolean result = elements.equals(args[0]);
            //it may be another proxy
            if (!result) {
                result = (boolean) proxy.invokeSuper(obj, args);
            }
            return result;
        }

        return method.invoke(elements, args);
    }
}
