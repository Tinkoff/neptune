package ru.tinkoff.qa.neptune.selenium.functions.searching;

import net.sf.cglib.proxy.MethodProxy;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;

import java.lang.reflect.Method;

import static java.lang.String.format;
import static java.util.Optional.ofNullable;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.ScrollWebElementIntoView.getDefaultScrollerIntoView;

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
    void setScroller() {
        try {
            scrollsIntoView = getDefaultScrollerIntoView(element);
        } catch (Throwable throwable) {
            throw new RuntimeException(throwable);
        }
    }

    @Override
    Object createRealObject() {
        return element;
    }

    @Override
    boolean toPerformTheScrolling(Method method) {
        return ofNullable(scrollsIntoView)
                .map(scrolls -> ((ScrollWebElementIntoView) scrolls).needsForTheScrolling(method))
                .orElse(false);
    }
}
