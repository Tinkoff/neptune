package ru.tinkoff.qa.neptune.selenium.functions.searching;

import org.openqa.selenium.WebElement;

class WebElementInterceptor extends AbstractElementInterceptor {

    WebElementInterceptor(WebElement element, String description) {
        super(description, element);
        setScroller();
    }

    @Override
    Object createRealObject() {
        return element;
    }
}
