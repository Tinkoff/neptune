package ru.tinkoff.qa.neptune.selenium.functions.searching;

import net.sf.cglib.proxy.MethodProxy;
import org.openqa.selenium.WebElement;

import java.lang.reflect.Method;

import static ru.tinkoff.qa.neptune.selenium.api.widget.WidgetDescriptionFormer.getDescription;

class WebElementInterceptor extends AbstractElementInterceptor {

    WebElementInterceptor(WebElement element) {
        super(element);
    }

    public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
        if ("toString".equals(method.getName()) &&
                method.getParameterTypes().length == 0
                && String.class.equals(method.getReturnType())) {
            return getDescription(createRealObject());
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
