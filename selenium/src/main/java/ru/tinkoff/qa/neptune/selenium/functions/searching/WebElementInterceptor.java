package ru.tinkoff.qa.neptune.selenium.functions.searching;

import net.sf.cglib.proxy.MethodProxy;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.lang.reflect.Method;

import static ru.tinkoff.qa.neptune.selenium.functions.searching.ToStringFormer.webElementToString;

class WebElementInterceptor extends AbstractElementInterceptor {

    private final By by;

    WebElementInterceptor(WebElement element, By by) {
        super(element);
        this.by = by;
    }

    public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
        if ("toString".equals(method.getName()) &&
                method.getParameterTypes().length == 0
                && String.class.equals(method.getReturnType())) {
            return webElementToString((WebElement) createRealObject(), by);
        } else {
            return super.intercept(obj, method, args, proxy);
        }
    }

    @Override
    Object createRealObject() {
        return element;
    }

    @Override
    boolean toPerformTheScrolling(Method method) {
        var name = method.getName();
        return name.equals("click")
                || name.equals("submit")
                || name.equals("sendKeys")
                || name.equals("clear")
                || name.equals("getLocation")
                || name.equals("getSize")
                || name.equals("getRect")
                || name.equals("getCoordinates");
    }
}
