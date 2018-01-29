package com.github.toy.constructor.selenium.api.widget;

public interface HasValue<T> {

    /**
     * @return value of some element (something like text in a text input field,
     * is some checkbox/radiobutton selected or not etc.)
     */
    T getValue();
}
