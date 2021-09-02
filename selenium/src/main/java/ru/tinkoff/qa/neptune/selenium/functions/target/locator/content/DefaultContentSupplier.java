package ru.tinkoff.qa.neptune.selenium.functions.target.locator.content;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import ru.tinkoff.qa.neptune.core.api.event.firing.annotations.MaxDepthOfReporting;
import ru.tinkoff.qa.neptune.core.api.steps.SequentialGetStepSupplier;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.ThrowWhenNoData;
import ru.tinkoff.qa.neptune.selenium.SeleniumStepContext;
import ru.tinkoff.qa.neptune.selenium.functions.target.locator.TargetLocatorSupplier;

import static ru.tinkoff.qa.neptune.selenium.SeleniumStepContext.CurrentContentFunction.currentContent;

@Description("Default content")
@MaxDepthOfReporting(0)
@ThrowWhenNoData(toThrow = WebDriverException.class)
public final class DefaultContentSupplier extends SequentialGetStepSupplier
        .GetSimpleStepSupplier<SeleniumStepContext, WebDriver, DefaultContentSupplier>
        implements TargetLocatorSupplier<WebDriver> {

    private DefaultContentSupplier() {
        super(currentContent().andThen(webDriver -> {
            try {
                return webDriver.switchTo().defaultContent();
            } catch (WebDriverException e) {
                return null;
            }
        }));
        throwOnNoResult();
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
        return new DefaultContentSupplier();
    }
}
