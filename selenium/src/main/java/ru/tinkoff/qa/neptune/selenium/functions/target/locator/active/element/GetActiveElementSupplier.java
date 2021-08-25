package ru.tinkoff.qa.neptune.selenium.functions.target.locator.active.element;

import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import ru.tinkoff.qa.neptune.core.api.event.firing.annotations.CaptureOnFailure;
import ru.tinkoff.qa.neptune.core.api.event.firing.annotations.CaptureOnSuccess;
import ru.tinkoff.qa.neptune.core.api.event.firing.annotations.MaxDepthOfReporting;
import ru.tinkoff.qa.neptune.core.api.steps.SequentialGetStepSupplier;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.ThrowWhenNoData;
import ru.tinkoff.qa.neptune.selenium.SeleniumStepContext;
import ru.tinkoff.qa.neptune.selenium.captors.WebDriverImageCaptor;
import ru.tinkoff.qa.neptune.selenium.captors.WebElementImageCaptor;
import ru.tinkoff.qa.neptune.selenium.functions.target.locator.TargetLocatorSupplier;

import static ru.tinkoff.qa.neptune.selenium.SeleniumStepContext.CurrentContentFunction.currentContent;

@CaptureOnFailure(by = WebDriverImageCaptor.class)
@CaptureOnSuccess(by = WebElementImageCaptor.class)
@Description("Active/Focused web element")
@MaxDepthOfReporting(1)
@ThrowWhenNoData(toThrow = NoSuchElementException.class)
public final class GetActiveElementSupplier extends SequentialGetStepSupplier
        .GetSimpleStepSupplier<SeleniumStepContext, WebElement, GetActiveElementSupplier>
        implements TargetLocatorSupplier<WebElement> {

    private GetActiveElementSupplier() {
        super(currentContent().andThen(webDriver -> {
            try {
                return webDriver.switchTo().activeElement();
            } catch (WebDriverException e) {
                return null;
            }
        }));
        throwOnNoResult();
    }


    /**
     * Builds a function which performs the switching to the active element and returns it.
     * Taken from Selenium documentation:
     * <p>
     *     Switches to the element that currently has focus within the document currently "switched to",
     *     or the body element if this cannot be detected. This matches the semantics of calling
     *     "document.activeElement" in Javascript.
     *
     *      Returns the WebElement with focus, or the body element if no element with focus can be
     *               detected.
     * </p>
     *
     * @return an instance of {@link GetActiveElementSupplier} which wraps a function. This function
     * performs the switching to the active element and returns it.
     */
    public static GetActiveElementSupplier activeElement() {
        return new GetActiveElementSupplier();
    }
}
