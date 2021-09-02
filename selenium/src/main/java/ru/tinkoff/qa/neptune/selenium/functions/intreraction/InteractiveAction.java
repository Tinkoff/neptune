package ru.tinkoff.qa.neptune.selenium.functions.intreraction;

import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import ru.tinkoff.qa.neptune.core.api.event.firing.annotations.CaptureOnFailure;
import ru.tinkoff.qa.neptune.core.api.event.firing.annotations.CaptureOnSuccess;
import ru.tinkoff.qa.neptune.core.api.steps.SequentialActionSupplier;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.DescriptionFragment;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.StepParameter;
import ru.tinkoff.qa.neptune.core.api.steps.parameters.ParameterValueGetter;
import ru.tinkoff.qa.neptune.selenium.SeleniumStepContext;
import ru.tinkoff.qa.neptune.selenium.api.widget.Widget;
import ru.tinkoff.qa.neptune.selenium.captors.WebDriverImageCaptor;
import ru.tinkoff.qa.neptune.selenium.functions.searching.SearchSupplier;

import java.time.Duration;

import static java.util.Optional.ofNullable;
import static ru.tinkoff.qa.neptune.selenium.SeleniumStepContext.CurrentContentFunction.currentContent;

/**
 * This class is designed to build an interactive action performed on a page.
 */
@CaptureOnFailure(by = WebDriverImageCaptor.class)
@CaptureOnSuccess(by = WebDriverImageCaptor.class)
public abstract class InteractiveAction extends SequentialActionSupplier<SeleniumStepContext, Actions, InteractiveAction> {

    private WebDriver driver;

    @StepParameter(doNotReportNullValues = true,
            value = "Pause before",
            makeReadableBy = ParameterValueGetter.DurationParameterValueGetter.class)
    private Duration pauseBefore;

    @StepParameter(doNotReportNullValues = true,
            value = "Pause after",
            makeReadableBy = ParameterValueGetter.DurationParameterValueGetter.class)
    private Duration pauseAfter;

    InteractiveAction() {
        super();
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
        return new KeyDownActionSupplier.KeyDownOnElementActionSupplier(key, target);
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
        return new KeyDownActionSupplier.KeyDownOnElementActionSupplier(key, target);
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
        return new KeyDownActionSupplier.KeyDownOnElementActionSupplier(key, howToFind);
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
        return new KeyUpActionSupplier.KeyUpOnElementActionSupplier(key, target);
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
        return new KeyUpActionSupplier.KeyUpOnElementActionSupplier(key, target);
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
        return new KeyUpActionSupplier.KeyUpOnElementActionSupplier(key, howToFind);
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
        return new SendKeysActionSupplier.SendKeysToElementActionSupplier(target, keys);
    }

    /**
     * <p>Builds an action that performs the sending keys to the given widget element.</p>
     *
     * @param keys   to be sent.
     * @param target widget to perform the action
     * @return an instance of {@link InteractiveAction}
     */
    public static InteractiveAction sendKeys(Widget target, CharSequence... keys) {
        return new SendKeysActionSupplier.SendKeysToElementActionSupplier(target, keys);
    }

    /**
     * <p>Builds an action that performs the sending keys to some element.</p>
     *
     * @param howToFind is description of the element to be found
     * @param keys      to be sent.
     * @return an instance of {@link InteractiveAction}
     */
    public static InteractiveAction sendKeys(SearchSupplier<?> howToFind, CharSequence... keys) {
        return new SendKeysActionSupplier.SendKeysToElementActionSupplier(howToFind, keys);
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
        return new ClickAndHoldActionSupplier.ClickAndHoldOnElementActionSupplier(target);
    }

    /**
     * <p>Builds an action that performs the clicking (without releasing) at the center of the given widget element.</p>
     *
     * @param target widget to be clicked and held.
     * @return an instance of {@link InteractiveAction}
     */
    public static InteractiveAction clickAndHold(Widget target) {
        return new ClickAndHoldActionSupplier.ClickAndHoldOnElementActionSupplier(target);
    }

    /**
     * <p>Builds an action that performs the clicking (without releasing) at the center of some element.</p>
     *
     * @param howToFind is description of the element to be found
     * @return an instance of {@link InteractiveAction}
     */
    public static InteractiveAction clickAndHold(SearchSupplier<?> howToFind) {
        return new ClickAndHoldActionSupplier.ClickAndHoldOnElementActionSupplier(howToFind);
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
        return new ReleaseActionSupplier.ReleaseElementActionSupplier(target);
    }

    /**
     * <p>Builds an action that releases the pressed left mouse button at the center of the widget element.</p>
     *
     * @param target to release the mouse button above.
     * @return an instance of {@link InteractiveAction}
     */
    public static InteractiveAction release(Widget target) {
        return new ReleaseActionSupplier.ReleaseElementActionSupplier(target);
    }

    /**
     * <p>Builds an action that releases the pressed left mouse button at the center of some element.</p>
     *
     * @param howToFind is description of the element to be found
     * @return an instance of {@link InteractiveAction}
     */
    public static InteractiveAction release(SearchSupplier<?> howToFind) {
        return new ReleaseActionSupplier.ReleaseElementActionSupplier(howToFind);
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
        return new ClickActionSupplier.ClickOnElementActionSupplier(target);
    }

    /**
     * <p>Builds an action that clicks at the center of the given widget element.</p>
     *
     * @param target is the element to be clicked.
     * @return an instance of {@link InteractiveAction}
     */
    public static InteractiveAction click(Widget target) {
        return new ClickActionSupplier.ClickOnElementActionSupplier(target);
    }

    /**
     * <p>Builds an action that clicks at the center of some element.</p>
     *
     * @param howToFind is description of the element to be found
     * @return an instance of {@link InteractiveAction}
     */
    public static InteractiveAction click(SearchSupplier<?> howToFind) {
        return new ClickActionSupplier.ClickOnElementActionSupplier(howToFind);
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
    @Description("Move mouse to {target} with offset [x={x}, y={y}]")
    public static InteractiveAction moveToElement(@DescriptionFragment("target") WebElement target,
                                                  @DescriptionFragment("x") int xOffset,
                                                  @DescriptionFragment("y") int yOffset) {
        return new MoveToElementActionSupplier(target, xOffset, yOffset);
    }

    /**
     * Builds an action that moves the mouse to the top-left corner of the web element.
     *
     * @param target element to move to.
     * @return an instance of {@link InteractiveAction}
     */
    @Description("Move mouse to {target}")
    public static InteractiveAction moveToElement(@DescriptionFragment("target") WebElement target) {
        return new MoveToElementActionSupplier(target, null, null);
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
    @Description("Move mouse to {target} with offset [x={x}, y={y}]")
    public static InteractiveAction moveToElement(@DescriptionFragment("target") Widget target,
                                                  @DescriptionFragment("x") int xOffset,
                                                  @DescriptionFragment("y") int yOffset) {
        return new MoveToElementActionSupplier(target, xOffset, yOffset);
    }

    /**
     * Builds an action that moves the mouse to the top-left corner of the widget element.
     *
     * @param target element to move to.
     * @return an instance of {@link InteractiveAction}
     */
    @Description("Move mouse to {target}")
    public static InteractiveAction moveToElement(@DescriptionFragment("target") Widget target) {
        return new MoveToElementActionSupplier(target, null, null);
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
    @Description("Move mouse to {target} with offset [x={x}, y={y}]")
    public static InteractiveAction moveToElement(@DescriptionFragment("target") SearchSupplier<?> howToFind,
                                                  @DescriptionFragment("x") int xOffset,
                                                  @DescriptionFragment("y") int yOffset) {
        return new MoveToElementActionSupplier(howToFind, xOffset, yOffset);
    }

    /**
     * Builds an action that moves the mouse to the top-left corner of some element.
     *
     * @param howToFind is description of the element to be found
     * @return an instance of {@link InteractiveAction}
     */
    @Description("Move mouse to {target}")
    public static InteractiveAction moveToElement(@DescriptionFragment("target") SearchSupplier<?> howToFind) {
        return new MoveToElementActionSupplier(howToFind, null, null);
    }

    /**
     * Builds an action that moves the mouse from the current position (or 0,0) by the given offset. If the coordinates
     * provided are outside the viewport (the mouse will end up outside the browser window) then
     * the viewport is scrolled to match.
     *
     * @param xOffset horizontal offset. A negative value means moving the mouse left.
     * @param yOffset vertical offset. A negative value means moving the mouse up.
     * @return an instance of {@link InteractiveAction}
     */
    public static InteractiveAction moveByOffset(int xOffset, int yOffset) {
        return new MouseMoveActionSupplier(xOffset, yOffset);
    }

    /**
     * Builds an action that performs context click at the current mouse location
     *
     * @return an instance of {@link InteractiveAction}
     */
    public static InteractiveAction contextClick() {
        return new ContextClickActionSupplier.ContextClickSimpleActionSupplier();
    }

    /**
     * Builds an action that performs context click at the center of the given web element.
     *
     * @param target element to perform the context clicking.
     * @return an instance of {@link InteractiveAction}
     */
    public static InteractiveAction contextClick(WebElement target) {
        return new ContextClickActionSupplier.ContextClickOnElementActionSupplier(target);
    }

    /**
     * Builds an action that performs context click at the center of the given widget element.
     *
     * @param target element to perform the context clicking.
     * @return an instance of {@link InteractiveAction}
     */
    public static InteractiveAction contextClick(Widget target) {
        return new ContextClickActionSupplier.ContextClickOnElementActionSupplier(target);
    }

    /**
     * <p>Builds an action that performs context click at the center of some element.</p>
     *
     * @param howToFind is description of the element to be found
     * @return an instance of {@link InteractiveAction}
     */
    public static InteractiveAction contextClick(SearchSupplier<?> howToFind) {
        return new ContextClickActionSupplier.ContextClickOnElementActionSupplier(howToFind);
    }


    /**
     * Builds an action that performs double click at the current mouse location
     *
     * @return an instance of {@link InteractiveAction}
     */
    public static InteractiveAction doubleClick() {
        return new DoubleClickActionSupplier.DoubleClickSimpleActionSupplier();
    }

    /**
     * Builds an action that performs double click at the center of the given web element.
     *
     * @param target element to perform the double clicking.
     * @return an instance of {@link InteractiveAction}
     */
    public static InteractiveAction doubleClick(WebElement target) {
        return new DoubleClickActionSupplier.DoubleClickOnElementActionSupplier(target);
    }

    /**
     * Builds an action that performs double click at the center of the given widget element.
     *
     * @param target element to perform the double clicking.
     * @return an instance of {@link InteractiveAction}
     */
    public static InteractiveAction doubleClick(Widget target) {
        return new DoubleClickActionSupplier.DoubleClickOnElementActionSupplier(target);
    }

    /**
     * <p>Builds an action that performs double click at the center of some element.</p>
     *
     * @param howToFind is description of the element to be found
     * @return an instance of {@link InteractiveAction}
     */
    public static InteractiveAction doubleClick(SearchSupplier<?> howToFind) {
        return new DoubleClickActionSupplier.DoubleClickOnElementActionSupplier(howToFind);
    }

    /**
     * Builds an action that performs click-and-hold at the location of the source element,
     * moves by a given offset, then releases the mouse.
     *
     * @param source  element to emulate button down at.
     * @param xOffset horizontal move offset.
     * @param yOffset vertical move offset.
     * @return an instance of {@link InteractiveAction}
     */
    public static InteractiveAction dragAndDropBy(WebElement source, int xOffset, int yOffset) {
        return new DragAndDropByActionSupplier(source, xOffset, yOffset);
    }

    /**
     * Builds an action that performs click-and-hold at the location of the source element,
     * moves by a given offset, then releases the mouse.
     *
     * @param source  element to emulate button down at.
     * @param xOffset horizontal move offset.
     * @param yOffset vertical move offset.
     * @return an instance of {@link InteractiveAction}
     */
    public static InteractiveAction dragAndDropBy(Widget source, int xOffset, int yOffset) {
        return new DragAndDropByActionSupplier(source, xOffset, yOffset);
    }

    /**
     * Builds an action that performs click-and-hold at the location of the source element,
     * moves by a given offset, then releases the mouse.
     *
     * @param howToFindSourceElement is description of the element to be found
     * @param xOffset                horizontal move offset.
     * @param yOffset                vertical move offset.
     * @return an instance of {@link InteractiveAction}
     */
    public static InteractiveAction dragAndDropBy(SearchSupplier<?> howToFindSourceElement, int xOffset, int yOffset) {
        return new DragAndDropByActionSupplier(howToFindSourceElement, xOffset, yOffset);
    }

    /**
     * Builds an action that performs click-and-hold at the location of the source element,
     * moves to the location of the target element, then releases the mouse.
     *
     * @param source element to emulate button down at.
     * @param target element to move to and release the mouse at.
     * @return an instance of {@link InteractiveAction}
     */
    public static InteractiveAction dragAndDrop(WebElement source, WebElement target) {
        return new DragAndDropActionSupplier(source, target);
    }

    /**
     * Builds an action that performs click-and-hold at the location of the source element,
     * moves to the location of the target element, then releases the mouse.
     *
     * @param source element to emulate button down at.
     * @param target element to move to and release the mouse at.
     * @return an instance of {@link InteractiveAction}
     */
    public static InteractiveAction dragAndDrop(Widget source, WebElement target) {
        return new DragAndDropActionSupplier(source, target);
    }

    /**
     * Builds an action that performs click-and-hold at the location of the source element,
     * moves to the location of the target element, then releases the mouse.
     *
     * @param source element to emulate button down at.
     * @param target element to move to and release the mouse at.
     * @return an instance of {@link InteractiveAction}
     */
    public static InteractiveAction dragAndDrop(WebElement source, Widget target) {
        return new DragAndDropActionSupplier(source, target);
    }

    /**
     * Builds an action that performs click-and-hold at the location of the source element,
     * moves to the location of the target element, then releases the mouse.
     *
     * @param source element to emulate button down at.
     * @param target element to move to and release the mouse at.
     * @return an instance of {@link InteractiveAction}
     */
    public static InteractiveAction dragAndDrop(Widget source, Widget target) {
        return new DragAndDropActionSupplier(source, target);
    }

    /**
     * Builds an action that performs click-and-hold at the location of the source element,
     * moves to the location of the target element, then releases the mouse.
     *
     * @param source is description of the element to emulate button down at.
     * @param target element to move to and release the mouse at.
     * @return an instance of {@link InteractiveAction}
     */
    public static InteractiveAction dragAndDrop(SearchSupplier<?> source, WebElement target) {
        return new DragAndDropActionSupplier(source, target);
    }

    /**
     * Builds an action that performs click-and-hold at the location of the source element,
     * moves to the location of the target element, then releases the mouse.
     *
     * @param source is description of the element to emulate button down at.
     * @param target element to move to and release the mouse at.
     * @return an instance of {@link InteractiveAction}
     */
    public static InteractiveAction dragAndDrop(SearchSupplier<?> source,
                                                Widget target) {
        return new DragAndDropActionSupplier(source, target);
    }

    /**
     * Builds an action that performs click-and-hold at the location of the source element,
     * moves to the location of the target element, then releases the mouse.
     *
     * @param source element to emulate button down at.
     * @param target is description of the element to move to and release the mouse at.
     * @return an instance of {@link InteractiveAction}
     */
    public static InteractiveAction dragAndDrop(WebElement source, SearchSupplier<?> target) {
        return new DragAndDropActionSupplier(source, target);
    }

    /**
     * Builds an action that performs click-and-hold at the location of the source element,
     * moves to the location of the target element, then releases the mouse.
     *
     * @param source element to emulate button down at.
     * @param target is description of the element to move to and release the mouse at.
     * @return an instance of {@link InteractiveAction}
     */
    public static InteractiveAction dragAndDrop(Widget source, SearchSupplier<?> target) {
        return new DragAndDropActionSupplier(source, target);
    }

    /**
     * Builds an action that performs click-and-hold at the location of the source element,
     * moves to the location of the target element, then releases the mouse.
     *
     * @param source is description of the element to emulate button down at.
     * @param target is description of the element to move to and release the mouse at.
     * @return an instance of {@link InteractiveAction}
     */
    public static InteractiveAction dragAndDrop(SearchSupplier<?> source, SearchSupplier<?> target) {
        return new DragAndDropActionSupplier(source, target);
    }

    WebElement getElement(Object o) {
        var cls = o.getClass();
        if (WebElement.class.isAssignableFrom(cls)) {
            return (WebElement) o;
        } else if (Widget.class.isAssignableFrom(cls)) {
            return ((Widget) o).getWrappedElement();
        } else {
            var sc = ((SearchSupplier<?>) o).get().apply(driver);
            if (WebElement.class.isAssignableFrom(sc.getClass())) {
                return (WebElement) sc;
            }
            return ((Widget) sc).getWrappedElement();
        }
    }

    /**
     * Sets duration of a pause before the performing of an action
     *
     * @param pauseBefore duration of a pause before the performing of an action
     * @return self-reference
     */
    public InteractiveAction pauseBefore(Duration pauseBefore) {
        this.pauseBefore = pauseBefore;
        return this;
    }

    /**
     * Sets duration of a pause after the performing of an action
     *
     * @param pauseAfter duration of a pause after the performing of an action
     * @return self-reference
     */
    public InteractiveAction pauseAfter(Duration pauseAfter) {
        this.pauseAfter = pauseAfter;
        return this;
    }

    @Override
    protected void howToPerform(Actions value) {
        ofNullable(pauseBefore).ifPresent(value::pause);
        addAction(value);
        ofNullable(pauseAfter).ifPresent(value::pause);
        value.perform();
    }

    abstract void addAction(Actions value);
}
