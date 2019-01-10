package ru.tinkoff.qa.neptune.selenium.functions.intreraction;

import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import ru.tinkoff.qa.neptune.core.api.SequentialActionSupplier;
import ru.tinkoff.qa.neptune.core.api.event.firing.annotation.MakeImageCapturesOnFinishing;
import ru.tinkoff.qa.neptune.selenium.SeleniumStepPerformer;
import ru.tinkoff.qa.neptune.selenium.api.widget.Widget;
import ru.tinkoff.qa.neptune.selenium.functions.searching.SearchSupplier;

import java.util.function.BiFunction;
import java.util.function.Function;

import static com.google.common.base.Preconditions.checkArgument;
import static java.lang.String.format;
import static java.util.Arrays.stream;
import static java.util.Objects.nonNull;
import static java.util.stream.Collectors.joining;
import static ru.tinkoff.qa.neptune.selenium.CurrentContentFunction.currentContent;

@MakeImageCapturesOnFinishing
public abstract class InteractiveAction extends SequentialActionSupplier<SeleniumStepPerformer, WebDriver, InteractiveAction> {

    InteractiveAction(String actionName) {
        super(actionName);
    }

    private static String getKeyNameDescription(String description, CharSequence... keys) {
        return format("%s. Key(s) %s", description, stream(keys).map(charSequence -> {
            checkArgument(nonNull(charSequence), "Null value is defined as a char sequence");
            if (charSequence.getClass().equals(Keys.class)) {
                return ((Keys) charSequence).name();
            }
            else {
                return String.valueOf(charSequence);
            }
        }).collect(joining(",")));
    }

    private static String getOffsetDescription(String actionDescription, int xOffset, int yOffset) {
        if (xOffset != 0 || yOffset != 0) {
            return format("%s. Offset x:%s y:%s", actionDescription, xOffset, yOffset);
        }
        return actionDescription;
    }

    private static InteractiveAction performActionOnWebDriverOnly(String actionName,
                                                                  Function<Actions, Actions> actionsFunction) {
        return new InteractiveAction(actionName) {
            @Override
            protected void performActionOn(WebDriver value) {
                actionsFunction.apply(new Actions(value)).perform();
            }
        }.performOn(currentContent());
    }

    private static InteractiveAction performActionOnWebElement(String actionName,
                                                               WebElement target,
                                                               BiFunction<Actions, WebElement,  Actions> actionsFunction) {
        checkArgument(nonNull(target), "Web element should be a not null value");
        return new InteractiveAction(actionName) {
            @Override
            protected void performActionOn(WebDriver value) {
                actionsFunction.apply(new Actions(value), target).perform();
            }
        }.performOn(currentContent());
    }

    private static InteractiveAction performActionWithDefinitionHowToFindElement(String actionName,
                                                                                 SearchSupplier<?> howToFind,
                                                                                 BiFunction<Actions, WebElement, Actions> actionsFunction) {
        checkArgument(nonNull(howToFind), "The definition how to find the " +
                "element should be a not null value");
        return new InteractiveAction(actionName) {
            @Override
            protected void performActionOn(WebDriver value) {
                var actions = new Actions(value);
                var result = howToFind.get().apply(value);
                var clazz = result.getClass();
                if (WebElement.class.isAssignableFrom(clazz)) {
                    actionsFunction.apply(actions, (WebElement) result).perform();
                    return;
                }

                if (WrapsElement.class.isAssignableFrom(clazz)) {
                    actionsFunction.apply(actions, ((WrapsElement) result).getWrappedElement()).perform();
                    return;
                }

                throw new IllegalArgumentException(format("It is impossible to perform '%s' action on an object " +
                                "of type %s. It doesn't implement %s nor %s", actionName, clazz.getName(),
                        WebElement.class.getName(),
                        WrapsElement.class.getName()));
            }
        }.performOn(currentContent());
    }

    /**
     * <p>Builds an action that performs the modifier key pressing.</p>
     * <p><b>Content below taken from Selenium documentation</b></p>
     * <p>Does not release the modifier key - subsequent interactions may assume it's kept pressed.</p>
     * Note that the modifier key is <b>never</b> released implicitly - either
     * <i>keyUp(theKey)</i> or <i>sendKeys(Keys.NULL)</i>
     * must be called to release the modifier.
     *
     * @param key Either {@link Keys#SHIFT}, {@link Keys#ALT} or {@link Keys#CONTROL}. If the
     *            provided key is none of those, {@link IllegalArgumentException} is thrown.
     * @return an anonymous instance of {@link InteractiveAction}
     */
    public static InteractiveAction keyDown(CharSequence key) {
        checkArgument(nonNull(key), "Key should be a not null value");
        return performActionOnWebDriverOnly(getKeyNameDescription("The pressing of the modifier key", key),
                actions -> actions.keyDown(key));
    }

    /**
     * <p>Builds an action that performs the modifier key pressing on some web element.</p>
     * <p><b>Content below taken from Selenium documentation</b></p>
     * <p>Performs a modifier key press after focusing on an element. Equivalent to:</p>
     * <i>Actions.click(element).sendKeys(theKey);</i>
     *
     * @param key Either {@link Keys#SHIFT}, {@link Keys#ALT} or {@link Keys#CONTROL}. If the
     * provided key is none of those, {@link IllegalArgumentException} is thrown.
     * @param target WebElement to perform the action
     * @return an anonymous instance of {@link InteractiveAction}
     */
    public static InteractiveAction keyDown(WebElement target, CharSequence key) {
        checkArgument(nonNull(key), "Key should be a not null value");
        return performActionOnWebElement(getKeyNameDescription(format("The pressing of the modifier key on %s", target), key),
                target, (actions, webElement) -> actions.keyDown(webElement, key));
    }

    /**
     * <p>Builds an action that performs the modifier key pressing on some widget element.</p>
     * @see #keyDown(WebElement, CharSequence)
     *
     * @param key Either {@link Keys#SHIFT}, {@link Keys#ALT} or {@link Keys#CONTROL}. If the
     * provided key is none of those, {@link IllegalArgumentException} is thrown.
     * @param target widget to perform the action
     * @return an anonymous instance of {@link InteractiveAction}
     */
    public static InteractiveAction keyDown(Widget target, CharSequence key) {
        checkArgument(nonNull(target), "Target widget should be a not null value");
        return keyDown(target.getWrappedElement(), key);
    }

    /**
     * <p>Builds an action that performs the modifier key pressing on some element.</p>
     * @see #keyDown(WebElement, CharSequence)
     * @see #keyDown(Widget, CharSequence)
     *
     * @param howToFind is the definition of a way how to find the target element
     * @param key Either {@link Keys#SHIFT}, {@link Keys#ALT} or {@link Keys#CONTROL}. If the
     *           provided key is none of those, {@link IllegalArgumentException} is thrown.
     * @return an anonymous instance of {@link InteractiveAction}
     */
    public static InteractiveAction keyDown(SearchSupplier<?> howToFind, CharSequence key) {
        checkArgument(nonNull(key), "Key should be a not null value");
        return performActionWithDefinitionHowToFindElement(getKeyNameDescription(format("The pressing of the modifier key on %s", howToFind), key),
                howToFind, (actions, webElement) -> actions.keyDown(webElement, key));
    }

    /**
     * <p>Builds an action that performs the modifier key releasing.</p>
     * <p><b>Content below taken from Selenium documentation</b></p>
     * <p>Releasing a not-pressed modifier key will yield undefined behaviour.</p>
     *
     * @param key Either {@link Keys#SHIFT}, {@link Keys#ALT} or {@link Keys#CONTROL}.
     * @return an anonymous instance of {@link InteractiveAction}
     */
    public static InteractiveAction keyUp(CharSequence key) {
        checkArgument(nonNull(key), "Key should be a not null value");
        return performActionOnWebDriverOnly(getKeyNameDescription("The releasing of the modifier key", key),
                actions -> actions.keyUp(key));
    }

    /**
     * <p>Builds an action that performs the modifier key releasing after focusing on some web element.</p>
     * <p><b>Content below taken from Selenium documentation</b></p>
     * <p>Performs a modifier key release after focusing on an element. Equivalent to:</p>
     * <i><i>Actions.click(element).sendKeys(theKey);</i></i>
     *
     * @param key Either {@link Keys#SHIFT}, {@link Keys#ALT} or {@link Keys#CONTROL}.
     * @param target WebElement to perform the action on
     * @return an anonymous instance of {@link InteractiveAction}
     */
    public static InteractiveAction keyUp(WebElement target, CharSequence key) {
        checkArgument(nonNull(key), "Key should be a not null value");
        return performActionOnWebElement(getKeyNameDescription(format("The releasing of the modifier key on %s", target), key), target,
                (actions, webElement) -> actions.keyUp(webElement, key));
    }

    /**
     * <p>Builds an action that performs the modifier key releasing after focusing on some widget element.</p>
     * @see #keyUp(WebElement, CharSequence)
     *
     * @param key Either {@link Keys#SHIFT}, {@link Keys#ALT} or {@link Keys#CONTROL}.
     * @param target widget to perform the action
     * @return an anonymous instance of {@link InteractiveAction}
     */
    public static InteractiveAction keyUp(Widget target, CharSequence key) {
        checkArgument(nonNull(target), "Target widget should be a not null value");
        return keyUp(target.getWrappedElement(), key);
    }

    /**
     * <p>Builds an action that performs the modifier key releasing on some element.</p>
     * @see #keyUp(WebElement, CharSequence)
     * @see #keyUp(Widget, CharSequence)
     *
     * @param howToFind is the definition of a way how to find the target element
     * @param key Either {@link Keys#SHIFT}, {@link Keys#ALT} or {@link Keys#CONTROL}. If the
     *           provided key is none of those, {@link IllegalArgumentException} is thrown.
     * @return an anonymous instance of {@link InteractiveAction}
     */
    public static InteractiveAction keyUp(SearchSupplier<?> howToFind, CharSequence key) {
        checkArgument(nonNull(key), "Key should be a not null value");
        return performActionWithDefinitionHowToFindElement(
                getKeyNameDescription(format("The releasing of the modifier key on %s", howToFind), key),
                howToFind, (actions, webElement) -> actions.keyUp(webElement, key));
    }

    /**
     * <p>Builds an action that performs the sending keys to the active element</p>
     * <p><b>Content below taken from Selenium documentation</b></p>
     * Sends keys to the active element. This differs from calling
     * {@link WebElement#sendKeys(CharSequence...)} on the active element in two ways:
     * <ul>
     * <li>The modifier keys included in this call are not released.</li>
     * <li>There is no attempt to re-focus the element - so sendKeys(Keys.TAB) for switching
     * elements should work. </li>
     * </ul>
     *
     * @param keys to be sent.
     * @return an anonymous instance of {@link InteractiveAction}
     */
    public static InteractiveAction sendKeys(CharSequence... keys) {
        checkArgument(nonNull(keys), "Keys to send should be a not null value");
        checkArgument(keys.length > 0, "Should be defined at least one key");
        return performActionOnWebDriverOnly(getKeyNameDescription("The sending keys to the active element", keys),
                actions -> actions.sendKeys(keys));
    }

    /**
     * <p>Builds an action that performs the sending keys to the given web element</p>
     * Equivalent to calling:
     * <i>Actions.click(element).sendKeys(keysToSend).</i>
     * This method is different from {@link WebElement#sendKeys(CharSequence...)}
     *
     * @param target element to focus on.
     * @param keys to be sent.
     * @return an anonymous instance of {@link InteractiveAction}
     */
    public static InteractiveAction sendKeys(WebElement target, CharSequence... keys) {
        checkArgument(nonNull(keys), "Keys to send should be a not null value");
        checkArgument(keys.length > 0, "Should be defined at least one key");
        return performActionOnWebElement(getKeyNameDescription(format("The sending keys to %s", target), keys),
                target, (actions, webElement) -> actions.sendKeys(webElement, keys));
    }

    /**
     * <p>Builds an action that performs the sending keys to the given widget element.</p>
     * @see #keyUp(WebElement, CharSequence)
     *
     * @param keys to be sent.
     * @param target widget to perform the action
     * @return an anonymous instance of {@link InteractiveAction}
     */
    public static InteractiveAction sendKeys(Widget target, CharSequence... keys) {
        checkArgument(nonNull(target), "Target widget should be a not null value");
        return sendKeys(target.getWrappedElement(), keys);
    }

    /**
     * <p>Builds an action that performs the sending keys to some element.</p>
     * @see #sendKeys(WebElement, CharSequence...)
     * @see #sendKeys(Widget, CharSequence...)
     *
     * @param howToFind is the definition of a way how to find the target element
     * @param keys to be sent.
     * @return an anonymous instance of {@link InteractiveAction}
     */
    public static InteractiveAction sendKeys(SearchSupplier<?> howToFind, CharSequence... keys) {
        checkArgument(nonNull(keys), "Keys to send should be a not null value");
        checkArgument(keys.length > 0, "Should be defined at least one key");
        return performActionWithDefinitionHowToFindElement(getKeyNameDescription(format("The sending keys to %s", howToFind), keys),
                howToFind, (actions, webElement) -> actions.sendKeys(webElement, keys));
    }

    /**
     * <p>Builds an action that performs the clicking (without releasing) at the current mouse location.</p>
     *
     * @return an anonymous instance of {@link InteractiveAction}
     */
    public static InteractiveAction clickAndHold() {
        return performActionOnWebDriverOnly("Clicking and hold", Actions::clickAndHold);
    }

    /**
     * <p>Builds an action that performs the clicking (without releasing) at the center of the given web element</p>
     *
     * @param target element to be clicked and held.
     * @return an anonymous instance of {@link InteractiveAction}
     */
    public static InteractiveAction clickAndHold(WebElement target) {
        return performActionOnWebElement(format("Clicking and hold %s", target), target, Actions::clickAndHold);
    }

    /**
     * <p>Builds an action that performs the clicking (without releasing) at the center of the given widget element.</p>
     * @see #clickAndHold(WebElement)
     *
     * @param target widget to be clicked and held.
     * @return an anonymous instance of {@link InteractiveAction}
     */
    public static InteractiveAction clickAndHold(Widget target) {
        checkArgument(nonNull(target), "Target widget should be a not null value");
        return clickAndHold(target.getWrappedElement());
    }

    /**
     * <p>Builds an action that performs the clicking (without releasing) at the center of some element.</p>
     * @see #clickAndHold(WebElement)
     * @see #clickAndHold(Widget)
     *
     * @param howToFind is the definition of a way how to find the target element
     * @return an anonymous instance of {@link InteractiveAction}
     */
    public static InteractiveAction clickAndHold(SearchSupplier<?> howToFind) {
        return performActionWithDefinitionHowToFindElement(format("Clicking and hold %s", howToFind),
                howToFind, Actions::clickAndHold);
    }

    /**
     * <p>Builds an action that releases the pressed left mouse button at the current mouse location</p>
     *
     * @return an anonymous instance of {@link InteractiveAction}
     */
    public static InteractiveAction release() {
        return performActionOnWebDriverOnly("Releasing of left mouse button", Actions::release);
    }

    /**
     * <p>Builds an action that releases the pressed left mouse button at the center of the given element</p>
     *
     * @param target to release the mouse button above.
     * @return an anonymous instance of {@link InteractiveAction}
     */
    public static InteractiveAction release(WebElement target) {
        return performActionOnWebElement(format("Releasing of left mouse button on %s", target), target, Actions::release);
    }

    /**
     * <p>Builds an action that releases the pressed left mouse button at the center of the widget element.</p>
     * @see #release(WebElement)
     *
     * @param target to release the mouse button above.
     * @return an anonymous instance of {@link InteractiveAction}
     */
    public static InteractiveAction release(Widget target) {
        checkArgument(nonNull(target), "Target widget should be a not null value");
        return release(target.getWrappedElement());
    }

    /**
     * <p>Builds an action that releases the pressed left mouse button at the center of some element.</p>
     * @see #release(WebElement)
     * @see #release(Widget)
     *
     * @param howToFind is the definition of a way how to find the target element
     * @return an anonymous instance of {@link InteractiveAction}
     */
    public static InteractiveAction release(SearchSupplier<?> howToFind) {
        return performActionWithDefinitionHowToFindElement(format("Releasing of left mouse button on %s", howToFind),
                howToFind, Actions::release);
    }

    /**
     * <p>Builds an action that clicks at the current mouse location</p>
     *
     * @return an anonymous instance of {@link InteractiveAction}
     */
    public static InteractiveAction click() {
        return performActionOnWebDriverOnly("Left mouse button click on current mouse location", Actions::click);
    }

    /**
     * <p>Builds an action that clicks at the center of the given web element.</p>
     *
     * @param target is the element to be clicked.
     * @return an anonymous instance of {@link InteractiveAction}
     */
    public static InteractiveAction click(WebElement target) {
        return performActionOnWebElement(format("Left mouse button click on %s", target), target, Actions::click);
    }

    /**
     * <p>Builds an action that clicks at the center of the given widget element.</p>
     * @see #click(WebElement)
     *
     * @param target is the element to be clicked.
     * @return an anonymous instance of {@link InteractiveAction}
     */
    public static InteractiveAction click(Widget target) {
        checkArgument(nonNull(target), "Target widget should be a not null value");
        return click(target.getWrappedElement());
    }

    /**
     * <p>Builds an action that clicks at the center of some element.</p>
     * @see #click(WebElement)
     * @see #click(Widget)
     *
     * @param howToFind is the definition of a way how to find the target element
     * @return an anonymous instance of {@link InteractiveAction}
     */
    public static InteractiveAction click(SearchSupplier<?> howToFind) {
        return performActionWithDefinitionHowToFindElement(format("Left mouse button click on %s", howToFind),
                howToFind, Actions::click);
    }

    /**
     * Builds an action that moves the mouse to an offset from the top-left corner of the web element.
     *
     * @param target element to move to.
     * @param xOffset Offset from the top-left corner. A negative value means coordinates left from
     * the element.
     * @param yOffset Offset from the top-left corner. A negative value means coordinates above
     * the element.
     * @return an anonymous instance of {@link InteractiveAction}
     */
    public static InteractiveAction moveToElement(WebElement target, int xOffset, int yOffset) {
        return performActionOnWebElement(getOffsetDescription(format("Moving mouse to the %s", target),  xOffset, yOffset), target,
                (actions, webElement) -> actions.moveToElement(webElement, xOffset, yOffset));
    }

    /**
     * Builds an action that moves the mouse to an offset from the top-left corner of the web element.
     * @see #moveToElement(WebElement, int, int)
     *
     * @param target element to move to.
     * @return an anonymous instance of {@link InteractiveAction}
     */
    public static InteractiveAction moveToElement(WebElement target) {
        return moveToElement(target, 0, 0);
    }

    /**
     * Builds an action that moves the mouse to an offset from the top-left corner of the widget element.
     * @see #moveToElement(WebElement, int, int)
     * @see #moveToElement(WebElement)
     *
     * @param target element to move to.
     * @param xOffset Offset from the top-left corner. A negative value means coordinates left from
     * the element.
     * @param yOffset Offset from the top-left corner. A negative value means coordinates above
     * the element.
     * @return an anonymous instance of {@link InteractiveAction}
     */
    public static InteractiveAction moveToElement(Widget target, int xOffset, int yOffset) {
        checkArgument(nonNull(target), "Target widget should be a not null value");
        return moveToElement(target.getWrappedElement(), xOffset, yOffset);
    }

    /**
     * Builds an action that moves the mouse to an offset from the top-left corner of the widget element.
     * @see #moveToElement(WebElement, int, int)
     * @see #moveToElement(WebElement)
     * @see #moveToElement(Widget, int, int)
     *
     * @param target element to move to.
     * @return an anonymous instance of {@link InteractiveAction}
     */
    public static InteractiveAction moveToElement(Widget target) {
        return moveToElement(target, 0, 0);
    }

    /**
     * Builds an action that moves the mouse to an offset from the top-left corner of some element.
     * @see #moveToElement(WebElement, int, int)
     * @see #moveToElement(WebElement)
     * @see #moveToElement(Widget, int, int)
     * @see #moveToElement(Widget)
     *
     * @param howToFind is the definition of a way how to find the target element
     * @param xOffset Offset from the top-left corner. A negative value means coordinates left from
     * the element.
     * @param yOffset Offset from the top-left corner. A negative value means coordinates above
     * the element.
     * @return an anonymous instance of {@link InteractiveAction}
     */
    public static InteractiveAction moveToElement(SearchSupplier<?> howToFind, int xOffset, int yOffset) {
        return performActionWithDefinitionHowToFindElement(getOffsetDescription(format("Moving mouse to the %s", howToFind), xOffset, yOffset),
                howToFind, (actions, webElement) -> actions.moveToElement(webElement, xOffset, yOffset));
    }

    /**
     * Builds an action that moves the mouse to an offset from the top-left corner of some element.
     *
     * @param howToFind is the definition of a way how to find the target element
     * @return an anonymous instance of {@link InteractiveAction}
     */
    public static InteractiveAction moveToElement(SearchSupplier<?> howToFind) {
        return moveToElement(howToFind, 0, 0);
    }

    /**
     * Builds an action that moves the mouse from the current position (or 0,0) by the given offset. If the coordinates
     * provided are outside the viewport (the mouse will end up outside the browser window) then
     * the viewport is scrolled to match.
     *
     * @param xOffset horizontal offset. A negative value means moving the mouse left.
     * @param yOffset vertical offset. A negative value means moving the mouse up.
     * @return an anonymous instance of {@link InteractiveAction}
     */
    public static InteractiveAction moveByOffset(int xOffset, int yOffset) {
        return performActionOnWebDriverOnly(getOffsetDescription("Moving mouse", xOffset, yOffset),
                actions -> actions.moveByOffset(xOffset, yOffset));
    }

    /**
     * Builds an action that performs context click at the current mouse location
     *
     * @return an anonymous instance of {@link InteractiveAction}
     */
    public static InteractiveAction contextClick() {
        return performActionOnWebDriverOnly("Context click on current mouse location", Actions::contextClick);
    }

    /**
     * Builds an action that performs context click at the center of the given web element.
     *
     * @param target element to move to.
     * @return an anonymous instance of {@link InteractiveAction}
     */
    public static InteractiveAction contextClick(WebElement target) {
        return performActionOnWebElement(format("Context click on %s", target), target, Actions::contextClick);
    }

    /**
     * Builds an action that performs context click at the center of the given widget element.
     * @see #contextClick(WebElement)
     *
     * @param target element to move to.
     * @return an anonymous instance of {@link InteractiveAction}
     */
    public static InteractiveAction contextClick(Widget target) {
        checkArgument(nonNull(target), "Target widget should be a not null value");
        return contextClick(target.getWrappedElement());
    }

    /**
     * <p>Builds an action that clicks at the center of some element.</p>
     * @see #contextClick(WebElement)
     * @see #contextClick(Widget)
     *
     * @param howToFind is the definition of a way how to find the target element
     * @return an anonymous instance of {@link InteractiveAction}
     */
    public static InteractiveAction contextClick(SearchSupplier<?> howToFind) {
        return performActionWithDefinitionHowToFindElement(format("Context click on %s", howToFind),
                howToFind, Actions::contextClick);
    }

    /**
     * Builds an action that performs click-and-hold at the location of the source element,
     * moves to the location of the target element, then releases the mouse.
     *
     * @param source element to emulate button down at.
     * @param target element to move to and release the mouse at.
     *
     * @return an anonymous instance of {@link InteractiveAction}
     */
    public static InteractiveAction dragAndDrop(WebElement source, WebElement target) {
        checkArgument(nonNull(source), "Source web element should be a not null value");
        checkArgument(nonNull(target), "Target web element should be a not null value");
        return performActionOnWebDriverOnly(format("Drag and drop from %s to %s", source, target),
                actions -> actions.dragAndDrop(source, target));
    }

    /**
     * Builds an action that performs click-and-hold at the location of the source element,
     * moves to the location of the target element, then releases the mouse.
     * @see #dragAndDrop(WebElement, WebElement)
     *
     * @param source element to emulate button down at.
     * @param target element to move to and release the mouse at.
     *
     * @return an anonymous instance of {@link InteractiveAction}
     */
    public static InteractiveAction dragAndDrop(Widget source, WebElement target) {
        checkArgument(nonNull(source), "Source widget element should be a not null value");
        return dragAndDrop(source.getWrappedElement(), target);
    }

    /**
     * Builds an action that performs click-and-hold at the location of the source element,
     * moves to the location of the target element, then releases the mouse.
     * @see #dragAndDrop(WebElement, WebElement)
     *
     * @param source element to emulate button down at.
     * @param target element to move to and release the mouse at.
     *
     * @return an anonymous instance of {@link InteractiveAction}
     */
    public static InteractiveAction dragAndDrop(WebElement source, Widget target) {
        checkArgument(nonNull(target), "Target widget element should be a not null value");
        return dragAndDrop(source, target.getWrappedElement());
    }

    /**
     * Builds an action that performs click-and-hold at the location of the source element,
     * moves to the location of the target element, then releases the mouse.
     * @see #dragAndDrop(WebElement, WebElement)
     *
     * @param source element to emulate button down at.
     * @param target element to move to and release the mouse at.
     *
     * @return an anonymous instance of {@link InteractiveAction}
     */
    public static InteractiveAction dragAndDrop(Widget source, Widget target) {
        checkArgument(nonNull(source), "Source widget element should be a not null value");
        checkArgument(nonNull(target), "Target widget element should be a not null value");
        return dragAndDrop(source.getWrappedElement(), target.getWrappedElement());
    }

    /**
     * Builds an action that performs click-and-hold at the location of the source element,
     * moves to the location of the target element, then releases the mouse.
     * @see #dragAndDrop(WebElement, WebElement)
     * @see #dragAndDrop(Widget, WebElement)
     *
     * @param howToFindSourceElement is the definition of a way how to find the element to emulate button down at.
     * @param target element to move to and release the mouse at.
     *
     * @return an anonymous instance of {@link InteractiveAction}
     */
    public static InteractiveAction dragAndDrop(SearchSupplier<?> howToFindSourceElement, WebElement target) {
        checkArgument(nonNull(target), "Target web element should be a not null value");
        return performActionWithDefinitionHowToFindElement(format("Drag and drop from %s to %s", howToFindSourceElement, target),
                howToFindSourceElement, (actions, webElement) -> actions.dragAndDrop(webElement, target));
    }

    /**
     * Builds an action that performs click-and-hold at the location of the source element,
     * moves to the location of the target element, then releases the mouse.
     * @see #dragAndDrop(WebElement, Widget)
     * @see #dragAndDrop(Widget, Widget)
     *
     * @param howToFindSourceElement is the definition of a way how to find the element to emulate button down at.
     * @param target element to move to and release the mouse at.
     *
     * @return an anonymous instance of {@link InteractiveAction}
     */
    public static InteractiveAction dragAndDrop(SearchSupplier<?> howToFindSourceElement, Widget target) {
        checkArgument(nonNull(target), "Target widget element should be a not null value");
        return dragAndDrop(howToFindSourceElement, target.getWrappedElement());
    }

    /**
     * Builds an action that performs click-and-hold at the location of the source element,
     * moves to the location of the target element, then releases the mouse.
     * @see #dragAndDrop(WebElement, WebElement)
     * @see #dragAndDrop(Widget, WebElement)
     *
     * @param source element to emulate button down at.
     * @param howToFindTargetElement is the definition of a way how to find the element to move to and release the mouse at.
     *
     * @return an anonymous instance of {@link InteractiveAction}
     */
    public static InteractiveAction dragAndDrop(WebElement source, SearchSupplier<?> howToFindTargetElement) {
        checkArgument(nonNull(source), "Source web element should be a not null value");
        return performActionWithDefinitionHowToFindElement(format("Drag and drop from %s to %s", source, howToFindTargetElement),
                howToFindTargetElement, (actions, webElement) -> actions.dragAndDrop(source, webElement));
    }

    /**
     * Builds an action that performs click-and-hold at the location of the source element,
     * moves to the location of the target element, then releases the mouse.
     * @see #dragAndDrop(WebElement, Widget)
     * @see #dragAndDrop(Widget, Widget)
     *
     * @param source element to emulate button down at.
     * @param howToFindTargetElement is the definition of a way how to find the element to move to and release the mouse at.
     *
     * @return an anonymous instance of {@link InteractiveAction}
     */
    public static InteractiveAction dragAndDrop(Widget source, SearchSupplier<?> howToFindTargetElement) {
        checkArgument(nonNull(source), "Target widget element should be a not null value");
        return dragAndDrop(source.getWrappedElement(), howToFindTargetElement);
    }

    /**
     * Builds an action that performs click-and-hold at the location of the source element,
     * moves to the location of the target element, then releases the mouse.
     * @see #dragAndDrop(WebElement, Widget)
     * @see #dragAndDrop(Widget, Widget)
     *
     * @param howToFindSourceElement is the definition of a way how to find the element to emulate button down at..
     * @param howToFindTargetElement is the definition of a way how to find the element to move to and release the mouse at.
     *
     * @return an anonymous instance of {@link InteractiveAction}
     */
    public static InteractiveAction dragAndDrop(SearchSupplier<?> howToFindSourceElement,
                                                SearchSupplier<?> howToFindTargetElement) {
        checkArgument(nonNull(howToFindSourceElement), "The definition how to find the source " +
                "element should be a not null value");
        checkArgument(nonNull(howToFindTargetElement), "The definition how to find the target " +
                "element should be a not null value");
        return new InteractiveAction(format("Drag and drop. From: %s. To: %s", howToFindSourceElement, howToFindTargetElement)) {
            @Override
            protected void performActionOn(WebDriver value) {
                var actions = new Actions(value);
                var source = howToFindSourceElement.get().apply(value);
                var target = howToFindTargetElement.get().apply(value);
                var sourceClass = source.getClass();
                var targetClass = target.getClass();
                if (WebElement.class.isAssignableFrom(sourceClass) && WrapsElement.class.isAssignableFrom(sourceClass)) {
                    throw new IllegalArgumentException(format("It is impossible to perform drag & drop action on an object " +
                                    "of type %s as a source. It doesn't implement %s nor %s", sourceClass.getName(),
                            WebElement.class.getName(),
                            WrapsElement.class.getName()));
                }

                if (WebElement.class.isAssignableFrom(targetClass) && WrapsElement.class.isAssignableFrom(targetClass)) {
                    throw new IllegalArgumentException(format("It is impossible to perform drag & drop action on an object " +
                                    "of type %s as a target. It doesn't implement %s nor %s", targetClass.getName(),
                            WebElement.class.getName(),
                            WrapsElement.class.getName()));
                }

                if (WrapsElement.class.isAssignableFrom(sourceClass)) {
                    source = ((WrapsElement) source).getWrappedElement();
                }

                if (WrapsElement.class.isAssignableFrom(targetClass)) {
                    target = ((WrapsElement) target).getWrappedElement();
                }

                actions.dragAndDrop((WebElement) source, (WebElement) target).perform();
            }
        }.performOn(currentContent());
    }

    /**
     * Builds an action that performs click-and-hold at the location of the source element,
     * moves by a given offset, then releases the mouse.
     *
     * @param source element to emulate button down at.
     * @param xOffset horizontal move offset.
     * @param yOffset vertical move offset.
     * @return an anonymous instance of {@link InteractiveAction}
     */
    public static InteractiveAction dragAndDropBy(WebElement source, int xOffset, int yOffset) {
        return performActionOnWebElement(getOffsetDescription(format("Drag and drop from %s", source), xOffset, yOffset), source,
                (actions, webElement) -> actions.dragAndDropBy(webElement, xOffset, yOffset));
    }

    /**
     * Builds an action that performs click-and-hold at the location of the source element,
     * moves by a given offset, then releases the mouse.
     * @see #dragAndDropBy(WebElement, int, int)
     *
     * @param source element to emulate button down at.
     * @param xOffset horizontal move offset.
     * @param yOffset vertical move offset.
     * @return an anonymous instance of {@link InteractiveAction}
     */
    public static InteractiveAction dragAndDropBy(Widget source, int xOffset, int yOffset) {
        checkArgument(nonNull(source), "Source widget element should be a not null value");
        return dragAndDropBy(source.getWrappedElement(), xOffset, yOffset);
    }

    /**
     * Builds an action that performs click-and-hold at the location of the source element,
     * moves by a given offset, then releases the mouse.
     * @see #dragAndDropBy(WebElement, int, int)
     *
     * @param howToFindSourceElement is the definition of a way how to find the element to emulate button down at.
     * @param xOffset horizontal move offset.
     * @param yOffset vertical move offset.
     * @return an anonymous instance of {@link InteractiveAction}
     */
    public static InteractiveAction dragAndDropBy(SearchSupplier<?> howToFindSourceElement, int xOffset, int yOffset) {
        return performActionWithDefinitionHowToFindElement(getOffsetDescription(format("Drag and drop from %s", howToFindSourceElement),
                xOffset, yOffset),
                howToFindSourceElement, (actions, webElement) -> actions.dragAndDropBy(webElement, xOffset, yOffset));
    }
}
