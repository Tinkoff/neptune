package ru.tinkoff.qa.neptune.selenium.functions.target.locator.content;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import ru.tinkoff.qa.neptune.core.api.event.firing.annotations.MaxDepthOfReporting;
import ru.tinkoff.qa.neptune.core.api.steps.Description;
import ru.tinkoff.qa.neptune.core.api.steps.SequentialGetStepSupplier;
import ru.tinkoff.qa.neptune.selenium.SeleniumStepContext;
import ru.tinkoff.qa.neptune.selenium.functions.target.locator.TargetLocatorSupplier;

import static ru.tinkoff.qa.neptune.selenium.SeleniumStepContext.CurrentContentFunction.currentContent;

@Description("Default content")
@MaxDepthOfReporting(0)
public final class DefaultContentSupplier extends SequentialGetStepSupplier
        .GetObjectChainedStepSupplier<SeleniumStepContext, WebDriver, WebDriver, DefaultContentSupplier>
        implements TargetLocatorSupplier<WebDriver> {

    private DefaultContentSupplier() {
        super(driver -> {
            try {
                return driver.switchTo().defaultContent();
            } catch (WebDriverException e) {
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
}
