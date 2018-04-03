package com.github.toy.constructor.selenium.functions.target.locator.content;

import com.github.toy.constructor.core.api.GetSupplier;
import com.github.toy.constructor.selenium.SeleniumSteps;
import com.github.toy.constructor.selenium.functions.target.locator.TargetLocatorSupplier;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;

import java.time.Duration;
import java.util.function.Function;
import java.util.function.Supplier;

import static com.github.toy.constructor.core.api.StoryWriter.toGet;
import static com.github.toy.constructor.core.api.ToGetSingleCheckedObject.getSingle;

public final class DefaultContentSupplier extends GetSupplier<SeleniumSteps, WebDriver, DefaultContentSupplier>
        implements TargetLocatorSupplier<WebDriver> {

    private static final Function<SeleniumSteps, WebDriver> GET_DEFAULT_CONTENT =
            toGet("Default content", seleniumSteps -> {
                try {
                    return seleniumSteps.getWrappedDriver().switchTo().defaultContent();
                }
                catch (WebDriverException e) {
                   return null;
                }
            });

    private static final Supplier<WebDriverException> CAN_NOT_SWITCH_TO_DEFAULT_CONTENT =
            () -> new WebDriverException("It was impossible to switch to default content for some reason");


    private DefaultContentSupplier() {
        super();
    }

    /**
     * Builds a function which performs the switching to the default content and returns it.
     * Taken from Selenium documentation:
     * <p>
     *     Selects either the first frame on the page, or the main document when a page contains
     *     iframes.
     *
     *     Returns the instant of {@link WebDriver} focused on the top window/first frame.
     * </p>
     *
     * @return an instance of {@link DefaultContentSupplier} which wraps a function. This function
     * performs the switching to the default content and returns it.
     */
    public static DefaultContentSupplier defaultContent() {
        return new DefaultContentSupplier().set(getSingle(GET_DEFAULT_CONTENT,
                CAN_NOT_SWITCH_TO_DEFAULT_CONTENT));
    }

    /**
     * Builds a function which performs the switching to the default content and returns it.
     * Taken from Selenium documentation:
     * <p>
     *     Selects either the first frame on the page, or the main document when a page contains
     *     iframes.
     *
     *     Returns the instant of {@link WebDriver} focused on the top window/first frame.
     * </p>
     *
     * @param duration is the time value of the waiting for the switching to default content is succeeded.
     *
     * @return an instance of {@link DefaultContentSupplier} which wraps a function. This function
     * performs the switching to the default content and returns it.
     */
    public static DefaultContentSupplier defaultContent(Duration duration) {
        return new DefaultContentSupplier().set(getSingle(GET_DEFAULT_CONTENT, duration,
                CAN_NOT_SWITCH_TO_DEFAULT_CONTENT));
    }
}
