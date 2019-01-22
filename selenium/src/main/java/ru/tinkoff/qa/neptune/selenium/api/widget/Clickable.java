package ru.tinkoff.qa.neptune.selenium.api.widget;

import org.openqa.selenium.SearchContext;

public interface Clickable extends SearchContext {

    /**
     * Performs click on some element (button, link etc.)
     */
    @NeedToScrollIntoView
    void click();
}
