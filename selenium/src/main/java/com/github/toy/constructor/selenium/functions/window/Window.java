package com.github.toy.constructor.selenium.functions.window;

import org.openqa.selenium.WebDriver;

public interface Window extends WebDriver.Window, WebDriver.Navigation {
    /**
     * Closes the window.
     */
    void close();

    /**
     * Is window currently present or not.
     *
     * @return {@code true} if the window currently present. {@code false} is returned
     * otherwise.
     *
     */
    boolean isPresent();

    /**
     * Gets title of a window.
     *
     * @return title of the window
     */
    String getTitle();

    /**
     * Gets url of a page which is loaded.
     *
     * @return url of a page which is loaded
     */
    String getCurrentUrl();
}
