package ru.tinkoff.qa.neptune.selenium.functions.target.locator.content;

import ru.tinkoff.qa.neptune.core.api.steps.SequentialGetStepSupplier;
import ru.tinkoff.qa.neptune.selenium.SeleniumStepContext;
import ru.tinkoff.qa.neptune.selenium.functions.target.locator.TargetLocatorSupplier;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;

import java.time.Duration;

import static ru.tinkoff.qa.neptune.selenium.CurrentContentFunction.currentContent;

public final class DefaultContentSupplier extends SequentialGetStepSupplier
        .GetObjectChainedStepSupplier<SeleniumStepContext, WebDriver, WebDriver, DefaultContentSupplier>
        implements TargetLocatorSupplier<WebDriver> {

    private DefaultContentSupplier() {
        super("Default content", driver -> {
            try {
                return driver.switchTo().defaultContent();
            }
            catch (WebDriverException e) {
                return null;
            }
        });
        throwOnEmptyResult(() -> new WebDriverException("It was impossible to switch to default content for some reason"));
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
        return new DefaultContentSupplier().from(currentContent());
    }

    @Override
    public DefaultContentSupplier timeOut(Duration timeOut) {
        return super.timeOut(timeOut);
    }
}
