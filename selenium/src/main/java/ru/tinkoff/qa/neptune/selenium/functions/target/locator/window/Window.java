package ru.tinkoff.qa.neptune.selenium.functions.target.locator.window;

import org.openqa.selenium.WrapsDriver;
import ru.tinkoff.qa.neptune.selenium.functions.target.locator.SwitchesToItself;
import org.openqa.selenium.WebDriver;

public interface Window extends WebDriver.Window, WebDriver.Navigation, SwitchesToItself, WrapsDriver {
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
