package ru.tinkoff.qa.neptune.selenium.functions.intreraction;

import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import ru.tinkoff.qa.neptune.core.api.event.firing.annotation.MakeFileCapturesOnFinishing;
import ru.tinkoff.qa.neptune.core.api.event.firing.annotation.MakeImageCapturesOnFinishing;
import ru.tinkoff.qa.neptune.core.api.steps.SequentialActionSupplier;
import ru.tinkoff.qa.neptune.selenium.SeleniumStepContext;
import ru.tinkoff.qa.neptune.selenium.api.widget.Widget;
import ru.tinkoff.qa.neptune.selenium.functions.searching.SearchSupplier;

import static ru.tinkoff.qa.neptune.selenium.CurrentContentFunction.currentContent;

/**
 * This class is designed to build an interactive action performed on a page.
 */
@MakeImageCapturesOnFinishing
@MakeFileCapturesOnFinishing
public abstract class InteractiveAction extends SequentialActionSupplier<SeleniumStepContext, Actions, InteractiveAction> {

    private WebDriver driver;

    InteractiveAction(String description) {
        super(description);
        performOn(currentContent().andThen(webDriver -> {
            try {
                return new Actions(webDriver);
            } finally {
                driver = webDriver;
            }
        }));
    }

    /**
     * Builds an action that performs the modifier key pressing.
     *
     * @param key Either {@link Keys#SHIFT}, {@link Keys#ALT} or {@link Keys#CONTROL}. If the
     *            provided key is none of those, {@link IllegalArgumentException} is thrown.
     * @return an instance of {@link InteractiveAction}
     */
    public static InteractiveAction keyDown(CharSequence key) {
        return new KeyDownActionSupplier.KeyDownSimpleActionSupplier(key);
    }

    /**
     * Builds an action that performs the modifier key pressing on some web element.
     *
     * @param key    Either {@link Keys#SHIFT}, {@link Keys#ALT} or {@link Keys#CONTROL}. If the
     *               provided key is none of those, {@link IllegalArgumentException} is thrown.
     * @param target WebElement to perform the action
     * @return an instance of {@link InteractiveAction}
     */
    public static InteractiveAction keyDown(WebElement target, CharSequence key) {
        return new KeyDownActionSupplier.KeyDownOnAFoundElement(key, target);
    }

    /**
     * Builds an action that performs the modifier key pressing on some widget element.
     *
     * @param key    Either {@link Keys#SHIFT}, {@link Keys#ALT} or {@link Keys#CONTROL}. If the
     *               provided key is none of those, {@link IllegalArgumentException} is thrown.
     * @param target widget to perform the action
     * @return an instance of {@link InteractiveAction}
     */
    public static InteractiveAction keyDown(Widget target, CharSequence key) {
        return new KeyDownActionSupplier.KeyDownOnAFoundElement(key, target);
    }

    /**
     * Builds an action that performs the modifier key pressing on some element.
     *
     * @param howToFind is description of the element to be found
     * @param key       Either {@link Keys#SHIFT}, {@link Keys#ALT} or {@link Keys#CONTROL}. If the
     *                  provided key is none of those, {@link IllegalArgumentException} is thrown.
     * @return an instance of {@link InteractiveAction}
     */
    public static InteractiveAction keyDown(SearchSupplier<?> howToFind, CharSequence key) {
        return new KeyDownActionSupplier.KeyDownOnElementToBeFound(key, howToFind);
    }

    /**
     * <p>Builds an action that performs the modifier key releasing.</p>
     *
     * @param key Either {@link Keys#SHIFT}, {@link Keys#ALT} or {@link Keys#CONTROL}.If the
     *            provided key is none of those, {@link IllegalArgumentException} is thrown.
     * @return an instance of {@link InteractiveAction}
     */
    public static InteractiveAction keyUp(CharSequence key) {
        return new KeyUpActionSupplier.KeyUpSimpleActionSupplier(key);
    }

    /**
     * <p>Builds an action that performs the modifier key releasing after focusing on some web element.</p>
     *
     * @param key    Either {@link Keys#SHIFT}, {@link Keys#ALT} or {@link Keys#CONTROL}. If the
     *               provided key is none of those, {@link IllegalArgumentException} is thrown.
     * @param target WebElement to perform the action on
     * @return an instance of {@link InteractiveAction}
     */
    public static InteractiveAction keyUp(WebElement target, CharSequence key) {
        return new KeyUpActionSupplier.KeyUpOnAFoundElement(key, target);
    }

    /**
     * <p>Builds an action that performs the modifier key releasing after focusing on some widget element.</p>
     *
     * @param key    Either {@link Keys#SHIFT}, {@link Keys#ALT} or {@link Keys#CONTROL}. If the
     *               provided key is none of those, {@link IllegalArgumentException} is thrown.
     * @param target widget to perform the action
     * @return an instance of {@link InteractiveAction}
     */
    public static InteractiveAction keyUp(Widget target, CharSequence key) {
        return new KeyUpActionSupplier.KeyUpOnAFoundElement(key, target);
    }

    /**
     * <p>Builds an action that performs the modifier key releasing on some element.</p>
     *
     * @param howToFind is description of the element to be found
     * @param key       Either {@link Keys#SHIFT}, {@link Keys#ALT} or {@link Keys#CONTROL}. If the
     *                  provided key is none of those, {@link IllegalArgumentException} is thrown.
     * @return an instance of {@link InteractiveAction}
     */
    public static InteractiveAction keyUp(SearchSupplier<?> howToFind, CharSequence key) {
        return new KeyUpActionSupplier.KeyUpOnElementToBeFound(key, howToFind);
    }

    /**
     * <p>Builds an action that performs the sending keys to the active element</p>
     *
     * @param keys to be sent.
     * @return an instance of {@link InteractiveAction}
     */
    public static InteractiveAction sendKeys(CharSequence... keys) {
        return new SendKeysActionSupplier.SendKeysSimpleActionSupplier(keys);
    }

    /**
     * <p>Builds an action that performs the sending keys to the given web element</p>
     *
     * @param target element to focus on.
     * @param keys   to be sent.
     * @return an instance of {@link InteractiveAction}
     */
    public static InteractiveAction sendKeys(WebElement target, CharSequence... keys) {
        return new SendKeysActionSupplier.SendKeysToAFoundElement(target, keys);
    }

    /**
     * <p>Builds an action that performs the sending keys to the given widget element.</p>
     *
     * @param keys   to be sent.
     * @param target widget to perform the action
     * @return an instance of {@link InteractiveAction}
     */
    public static InteractiveAction sendKeys(Widget target, CharSequence... keys) {
        return new SendKeysActionSupplier.SendKeysToAFoundElement(target, keys);
    }

    /**
     * <p>Builds an action that performs the sending keys to some element.</p>
     *
     * @param howToFind is description of the element to be found
     * @param keys      to be sent.
     * @return an instance of {@link InteractiveAction}
     */
    public static InteractiveAction sendKeys(SearchSupplier<?> howToFind, CharSequence... keys) {
        return new SendKeysActionSupplier.SendKeysToElementToBeFound(howToFind, keys);
    }

    /**
     * <p>Builds an action that performs the clicking (without releasing) at the current mouse location.</p>
     *
     * @return an instance of {@link InteractiveAction}
     */
    public static InteractiveAction clickAndHold() {
        return new ClickAndHoldActionSupplier.ClickAndHoldSimpleActionSupplier();
    }

    /**
     * <p>Builds an action that performs the clicking (without releasing) at the center of the given web element</p>
     *
     * @param target element to be clicked and held.
     * @return an instance of {@link InteractiveAction}
     */
    public static InteractiveAction clickAndHold(WebElement target) {
        return new ClickAndHoldActionSupplier.ClickAndHoldOnAFoundElement(target);
    }

    /**
     * <p>Builds an action that performs the clicking (without releasing) at the center of the given widget element.</p>
     *
     * @param target widget to be clicked and held.
     * @return an instance of {@link InteractiveAction}
     */
    public static InteractiveAction clickAndHold(Widget target) {
        return new ClickAndHoldActionSupplier.ClickAndHoldOnAFoundElement(target);
    }

    /**
     * <p>Builds an action that performs the clicking (without releasing) at the center of some element.</p>
     *
     * @param howToFind is description of the element to be found
     * @return an instance of {@link InteractiveAction}
     */
    public static InteractiveAction clickAndHold(SearchSupplier<?> howToFind) {
        return new ClickAndHoldActionSupplier.ClickAndHoldOnElementToBeFound(howToFind);
    }

    /**
     * <p>Builds an action that releases the pressed left mouse button at the current mouse location</p>
     *
     * @return an instance of {@link InteractiveAction}
     */
    public static InteractiveAction release() {
        return new ReleaseActionSupplier.ReleaseSimpleActionSupplier();
    }

    /**
     * <p>Builds an action that releases the pressed left mouse button at the center of the given element</p>
     *
     * @param target to release the mouse button above.
     * @return an instance of {@link InteractiveAction}
     */
    public static InteractiveAction release(WebElement target) {
        return new ReleaseActionSupplier.ReleaseAFoundElement(target);
    }

    /**
     * <p>Builds an action that releases the pressed left mouse button at the center of the widget element.</p>
     *
     * @param target to release the mouse button above.
     * @return an instance of {@link InteractiveAction}
     * @see #release(WebElement)
     */
    public static InteractiveAction release(Widget target) {
        return new ReleaseActionSupplier.ReleaseAFoundElement(target);
    }

    /**
     * <p>Builds an action that releases the pressed left mouse button at the center of some element.</p>
     *
     * @param howToFind is description of the element to be found
     * @return an instance of {@link InteractiveAction}
     * @see #release(WebElement)
     * @see #release(Widget)
     */
    public static InteractiveAction release(SearchSupplier<?> howToFind) {
        return new ReleaseActionSupplier.ReleaseAnElementToBeFound(howToFind);
    }

    /**
     * <p>Builds an action that clicks at the current mouse location</p>
     *
     * @return an instance of {@link InteractiveAction}
     */
    public static InteractiveAction click() {
        return new ClickActionSupplier.ClickSimpleActionSupplier();
    }

    /**
     * <p>Builds an action that clicks at the center of the given web element.</p>
     *
     * @param target is the element to be clicked.
     * @return an instance of {@link InteractiveAction}
     */
    public static InteractiveAction click(WebElement target) {
        return new ClickActionSupplier.ClickOnAFoundElement(target);
    }

    /**
     * <p>Builds an action that clicks at the center of the given widget element.</p>
     *
     * @param target is the element to be clicked.
     * @return an instance of {@link InteractiveAction}
     * @see #click(WebElement)
     */
    public static InteractiveAction click(Widget target) {
        return new ClickActionSupplier.ClickOnAFoundElement(target);
    }

    /**
     * <p>Builds an action that clicks at the center of some element.</p>
     *
     * @param howToFind is description of the element to be found
     * @return an instance of {@link InteractiveAction}
     * @see #click(WebElement)
     * @see #click(Widget)
     */
    public static InteractiveAction click(SearchSupplier<?> howToFind) {
        return new ClickActionSupplier.ClickOnAnElementToBeFound(howToFind);
    }

    /**
     * Builds an action that moves the mouse to an offset from the top-left corner of the web element.
     *
     * @param target  element to move to.
     * @param xOffset Offset from the top-left corner. A negative value means coordinates left from
     *                the element.
     * @param yOffset Offset from the top-left corner. A negative value means coordinates above
     *                the element.
     * @return an instance of {@link InteractiveAction}
     */
    public static InteractiveAction moveToElement(WebElement target, int xOffset, int yOffset) {
        return new MoveToElementActionSupplier.MoveToAFoundElement(target, xOffset, yOffset);
    }

    /**
     * Builds an action that moves the mouse to the top-left corner of the web element.
     *
     * @param target element to move to.
     * @return an instance of {@link InteractiveAction}
     */
    public static InteractiveAction moveToElement(WebElement target) {
        return new MoveToElementActionSupplier.MoveToAFoundElement(target, null, null);
    }

    /**
     * Builds an action that moves the mouse to an offset from the top-left corner of the widget element.
     *
     * @param target  element to move to.
     * @param xOffset Offset from the top-left corner. A negative value means coordinates left from
     *                the element.
     * @param yOffset Offset from the top-left corner. A negative value means coordinates above
     *                the element.
     * @return an instance of {@link InteractiveAction}
     */
    public static InteractiveAction moveToElement(Widget target, int xOffset, int yOffset) {
        return new MoveToElementActionSupplier.MoveToAFoundElement(target, xOffset, yOffset);
    }

    /**
     * Builds an action that moves the mouse to the top-left corner of the widget element.
     *
     * @param target element to move to.
     * @return an instance of {@link InteractiveAction}
     */
    public static InteractiveAction moveToElement(Widget target) {
        return new MoveToElementActionSupplier.MoveToAFoundElement(target, null, null);
    }

    /**
     * Builds an action that moves the mouse to an offset from the top-left corner of some element.
     *
     * @param howToFind is description of the element to be found
     * @param xOffset   Offset from the top-left corner. A negative value means coordinates left from
     *                  the element.
     * @param yOffset   Offset from the top-left corner. A negative value means coordinates above
     *                  the element.
     * @return an instance of {@link InteractiveAction}
     */
    public static InteractiveAction moveToElement(SearchSupplier<?> howToFind, int xOffset, int yOffset) {
        return new MoveToElementActionSupplier.MoveToAnElementToBeFound(howToFind, xOffset, yOffset);
    }

    /**
     * Builds an action that moves the mouse to the top-left corner of some element.
     *
     * @param howToFind is description of the element to be found
     * @return an instance of {@link InteractiveAction}
     */
    public static InteractiveAction moveToElement(SearchSupplier<?> howToFind) {
        return new MoveToElementActionSupplier.MoveToAnElementToBeFound(howToFind, null, null);
    }

    WebDriver getDriver() {
        return driver;
    }
}
