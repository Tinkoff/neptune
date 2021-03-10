package ru.tinkoff.qa.neptune.selenium.functions.target.locator.active.element;

import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import ru.tinkoff.qa.neptune.core.api.event.firing.annotation.MakeFileCapturesOnFinishing;
import ru.tinkoff.qa.neptune.core.api.event.firing.annotation.MakeImageCapturesOnFinishing;
import ru.tinkoff.qa.neptune.core.api.steps.SequentialGetStepSupplier;
import ru.tinkoff.qa.neptune.selenium.SeleniumStepContext;
import ru.tinkoff.qa.neptune.selenium.functions.target.locator.TargetLocatorSupplier;

import static ru.tinkoff.qa.neptune.selenium.SeleniumStepContext.CurrentContentFunction.currentContent;

@MakeImageCapturesOnFinishing
@MakeFileCapturesOnFinishing
public final class GetActiveElementSupplier extends SequentialGetStepSupplier
        .GetObjectChainedStepSupplier<SeleniumStepContext, WebElement, WebDriver, GetActiveElementSupplier>
        implements TargetLocatorSupplier<WebElement> {

    private GetActiveElementSupplier() {
        super("Active element", webDriver -> {
            try {
                return webDriver.switchTo().activeElement();
            }
            catch (WebDriverException e) {
                return null;
            }
        });
        throwOnEmptyResult(() ->
                new NoSuchElementException("It was impossible to detect the active element for some reason"));
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
        return new GetActiveElementSupplier().from(currentContent());
    }
}
