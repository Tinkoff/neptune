package ru.tinkoff.qa.neptune.selenium.auto.scrolling;

import org.openqa.selenium.*;
import ru.tinkoff.qa.neptune.selenium.api.widget.ScrollsIntoView;

import static com.google.common.base.Preconditions.checkNotNull;
import static java.lang.String.format;
import static java.util.Objects.isNull;

/**
 * This class was designed to provide the ability to perform the scrolling into view implicitly.
 * Instances of {@link org.openqa.selenium.WebElement}  or {@link ru.tinkoff.qa.neptune.selenium.api.widget.Widget}
 * or {@link org.openqa.selenium.WrapsElement} are possible to be scrolled into view.
 */
public abstract class AutoScroller implements WrapsDriver, JavascriptExecutor {

    private final WebDriver driver;

    public AutoScroller(WebDriver driver) {
        checkNotNull(driver);
        this.driver = driver;
    }

    /**
     * This method was added to provide ability to execute js if it is necessary.
     *
     * @param script script to execute
     * @param args   script arguments
     * @return result of script execution
     */
    @Override
    public final Object executeScript(String script, Object... args) {
        if (driver instanceof JavascriptExecutor) {
            return ((JavascriptExecutor) driver).executeScript(script, args);
        }

        throw new UnsupportedOperationException(format("Java script execution is not supported by objects of %s",
                driver.getClass()));
    }

    /**
     * This method was added to provide ability to execute asynchronous js if it is necessary.
     *
     * @param script script to execute
     * @param args   script arguments
     * @return result of script execution
     */
    @Override
    public final Object executeAsyncScript(String script, Object... args) {
        if (driver instanceof JavascriptExecutor) {
            return ((JavascriptExecutor) driver).executeAsyncScript(script, args);
        }

        throw new UnsupportedOperationException(format("Java script execution is not supported by objects of %s",
                driver.getClass()));
    }

    /**
     * This method was added to provide access to WebDriver if it is necessary.
     *
     * @return wrapped instance of WebDriver
     */
    @Override
    public final WebDriver getWrappedDriver() {
        return driver;
    }

    /**
     * Scrolling an object into view.
     *
     * @param object to be scrolled into view
     */
    public final void scrollIntoView(Object object) {
        if (isNull(object)) {
            return;
        }

        if (!(object instanceof WebElement) && !(object instanceof WrapsElement) &&
                !(object instanceof ScrollsIntoView)) {
            return;
        }

        if (object instanceof ScrollsIntoView) {
            ((ScrollsIntoView) object).scrollIntoView();
            return;
        }

        WebElement e;
        if (object instanceof WebElement) {
            e = (WebElement) object;
        } else {
            e = ((WrapsElement) object).getWrappedElement();
        }

        if (e == null) {
            return;
        }

        scrollIntoView(e);
    }

    /**
     * Scrolling a {@link WebElement} into view.
     *
     * @param e is a {@link WebElement} to be scrolled into view
     */
    protected abstract void scrollIntoView(WebElement e);
}
