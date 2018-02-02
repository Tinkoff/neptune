package com.github.toy.constructor.selenium.api.widget;

import org.openqa.selenium.SearchContext;

public interface Clickable extends SearchContext {

    /**
     * Performs click on some element (button, link etc.)
     */
    void click();
}
