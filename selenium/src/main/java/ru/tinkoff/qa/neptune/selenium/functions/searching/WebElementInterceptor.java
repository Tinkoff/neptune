package ru.tinkoff.qa.neptune.selenium.functions.searching;

import net.sf.cglib.proxy.MethodProxy;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;

import java.lang.reflect.Method;

import static java.lang.String.format;
import static org.apache.commons.lang3.StringUtils.isBlank;

class WebElementInterceptor extends AbstractElementInterceptor {

    private final String description;

    WebElementInterceptor(WebElement element, String description) {
        super(element);
        this.description = description;
    }

    public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
        if ("toString".equals(method.getName()) &&
                method.getParameterTypes().length == 0
                && String.class.equals(method.getReturnType())) {
            try {
                var text = ((WebElement) createRealObject()).getText().trim();
                if (isBlank(text)) {
                    return description;
                }

                if (text.length() < 30) {
                    return format("%s text: %s", description, text);
                }

                return format("%s text: %s...", description, text.substring(0, 30));
            }
            catch (WebDriverException e) {
                return description;
            }
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
