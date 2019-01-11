package ru.tinkoff.qa.neptune.selenium.functions.target.locator.active.element;

import ru.tinkoff.qa.neptune.core.api.GetStepSupplier;
import ru.tinkoff.qa.neptune.core.api.event.firing.annotation.MakeImageCapturesOnFinishing;
import ru.tinkoff.qa.neptune.selenium.SeleniumStepContext;
import ru.tinkoff.qa.neptune.selenium.functions.target.locator.TargetLocatorSupplier;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;

import java.time.Duration;
import java.util.function.Function;
import java.util.function.Supplier;

import static ru.tinkoff.qa.neptune.core.api.conditions.ToGetSingleCheckedObject.getSingle;
import static ru.tinkoff.qa.neptune.selenium.CurrentContentFunction.currentContent;

@MakeImageCapturesOnFinishing
public final class GetActiveElementSupplier extends GetStepSupplier<SeleniumStepContext, WebElement, GetActiveElementSupplier>
        implements TargetLocatorSupplier<WebElement> {

    private static final Function<WebDriver, WebElement> GET_ACTIVE_ELEMENT =
            webDriver -> {
                try {
                    return webDriver.switchTo().activeElement();
                }
                catch (WebDriverException e) {
                    return null;
                }
            };

    private static final Supplier<NoSuchElementException> NO_ACTIVE_ELEMENT_EXCEPTION = () ->
            new NoSuchElementException("It was impossible to detect the active element for some reason");

    private GetActiveElementSupplier() {
        super();
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
        return new GetActiveElementSupplier().set(getSingle("Active element",
                currentContent().andThen(GET_ACTIVE_ELEMENT), NO_ACTIVE_ELEMENT_EXCEPTION));
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
     * @param duration is the time value to get the result which differs from {@code null}
     *
     * @return an instance of {@link GetActiveElementSupplier} which wraps a function. This function
     * performs the switching to the active element and returns it.
     */
    public static GetActiveElementSupplier activeElement(Duration duration) {
        return new GetActiveElementSupplier().set(getSingle("Active element",
                currentContent().andThen(GET_ACTIVE_ELEMENT), duration, NO_ACTIVE_ELEMENT_EXCEPTION));
    }
}
