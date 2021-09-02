package ru.tinkoff.qa.neptune.selenium.functions.target.locator.frame.parent;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import ru.tinkoff.qa.neptune.core.api.steps.SequentialGetStepSupplier;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.ThrowWhenNoData;
import ru.tinkoff.qa.neptune.selenium.SeleniumStepContext;
import ru.tinkoff.qa.neptune.selenium.functions.target.locator.TargetLocatorSupplier;

import static ru.tinkoff.qa.neptune.selenium.SeleniumStepContext.CurrentContentFunction.currentContent;

@Description("Parent frame")
@ThrowWhenNoData(toThrow = WebDriverException.class)
public final class ParentFrameSupplier extends SequentialGetStepSupplier
        .GetSimpleStepSupplier<SeleniumStepContext, WebDriver, ParentFrameSupplier>
        implements TargetLocatorSupplier<WebDriver> {


    private ParentFrameSupplier() {
        super(currentContent().andThen(webDriver -> {
            try {
                return webDriver.switchTo().parentFrame();
            } catch (WebDriverException e) {
                return null;
            }
        }));
        throwOnNoResult();
    }

    /**
     * Builds a function which performs the switching to the parent frame and returns it.
     * Taken from Selenium documentation:
     * <p>
     *     Change focus to the parent context. If the current context is the top level browsing context,
     *     the context remains unchanged.
     * </p>
     *
     * @return an instance of {@link ParentFrameSupplier} which wraps a function. This function
     *      performs the switching to the parent frame and returns it.
     */
    public static ParentFrameSupplier parentFrame() {
        return new ParentFrameSupplier();
    }
}
