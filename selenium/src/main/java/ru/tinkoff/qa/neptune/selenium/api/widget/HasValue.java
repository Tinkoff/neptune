package ru.tinkoff.qa.neptune.selenium.api.widget;

import org.openqa.selenium.SearchContext;

public interface HasValue<T> extends SearchContext {

    /**
     * @return value of some element (something like text in a text input field,
     * is some checkbox/radiobutton selected or not etc.)
     */
    T getValue();
}
