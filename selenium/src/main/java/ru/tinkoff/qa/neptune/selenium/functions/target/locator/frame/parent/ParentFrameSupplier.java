package ru.tinkoff.qa.neptune.selenium.functions.target.locator.frame.parent;

import ru.tinkoff.qa.neptune.core.api.GetStepSupplier;
import ru.tinkoff.qa.neptune.selenium.SeleniumSteps;
import ru.tinkoff.qa.neptune.selenium.functions.target.locator.TargetLocatorSupplier;
import org.openqa.selenium.NoSuchFrameException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;

import java.time.Duration;
import java.util.function.Function;
import java.util.function.Supplier;

import static ru.tinkoff.qa.neptune.core.api.conditions.ToGetSingleCheckedObject.getSingle;
import static ru.tinkoff.qa.neptune.selenium.CurrentContentFunction.currentContent;

public final class ParentFrameSupplier extends GetStepSupplier<SeleniumSteps, WebDriver, ParentFrameSupplier>
        implements TargetLocatorSupplier<WebDriver> {

    private static final Function<WebDriver, WebDriver> GET_PARENT_FRAME =
            driver -> {
                try {
                    return driver.switchTo().parentFrame();
                }
                catch (WebDriverException e) {
                    return null;
                }
            };

    private static final Supplier<WebDriverException> CAN_NOT_SWITCH_TO_PARENT_FRAME =
            () -> new NoSuchFrameException("It was impossible to switch to the parent frame for some reason");

    private ParentFrameSupplier() {
        super();
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
        return new ParentFrameSupplier().set(getSingle("Parent frame",
                currentContent().andThen(GET_PARENT_FRAME), CAN_NOT_SWITCH_TO_PARENT_FRAME));
    }

    /**
     * Builds a function which performs the switching to the parent frame and returns it.
     * Taken from Selenium documentation:
     * <p>
     *     Change focus to the parent context. If the current context is the top level browsing context,
     *     the context remains unchanged.
     * </p>
     *
     * @param duration is the time value of the waiting for the switching to the parent frame is succeeded.
     *
     * @return an instance of {@link ParentFrameSupplier} which wraps a function. This function
     *      performs the switching to the parent frame and returns it.
     */
    public static ParentFrameSupplier parentFrame(Duration duration) {
        return new ParentFrameSupplier().set(getSingle("Parent frame", currentContent().andThen(GET_PARENT_FRAME),
                duration, CAN_NOT_SWITCH_TO_PARENT_FRAME));
    }
}
