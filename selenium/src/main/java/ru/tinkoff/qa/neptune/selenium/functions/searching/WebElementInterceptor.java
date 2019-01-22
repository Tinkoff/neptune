package ru.tinkoff.qa.neptune.selenium.functions.searching;

import org.openqa.selenium.WebElement;

import java.lang.reflect.Method;

import static java.util.Optional.ofNullable;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.ScrollWebElementIntoView.getDefaultScrollerIntoView;

class WebElementInterceptor extends AbstractElementInterceptor {

    WebElementInterceptor(WebElement element, String description) {
        super(description, element);
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
