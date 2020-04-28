package ru.tinkoff.qa.neptune.selenium.functions.intreraction;

import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.WrapsElement;
import org.openqa.selenium.interactions.Actions;
import ru.tinkoff.qa.neptune.core.api.event.firing.annotation.MakeFileCapturesOnFinishing;
import ru.tinkoff.qa.neptune.core.api.event.firing.annotation.MakeImageCapturesOnFinishing;
import ru.tinkoff.qa.neptune.core.api.steps.SequentialActionSupplier;
import ru.tinkoff.qa.neptune.selenium.SeleniumStepContext;
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
@MakeFileCapturesOnFinishing
public abstract class InteractiveAction_Deprecated extends SequentialActionSupplier<SeleniumStepContext, WebDriver, InteractiveAction_Deprecated> {

    InteractiveAction_Deprecated(String actionName) {
        super(actionName);
    }

    private static String getKeyNameDescription(String description, CharSequence... keys) {
        return format("%s. Key(s) %s", description, stream(keys).map(charSequence -> {
            checkArgument(nonNull(charSequence), "Null value is defined as a char sequence");
            if (charSequence.getClass().equals(Keys.class)) {
                return ((Keys) charSequence).name();
            } else {
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

    private static InteractiveAction_Deprecated performActionOnWebDriverOnly(String actionName,
                                                                             Function<Actions, Actions> actionsFunction) {
        return new InteractiveAction_Deprecated(actionName) {
            @Override
            protected void performActionOn(WebDriver value) {
                actionsFunction.apply(new Actions(value)).perform();
            }
        }.performOn(currentContent());
    }

    private static InteractiveAction_Deprecated performActionOnWebElement(String actionName,
                                                                          WebElement target,
                                                                          BiFunction<Actions, WebElement, Actions> actionsFunction) {
        checkArgument(nonNull(target), "Web element should be a not null value");
        return new InteractiveAction_Deprecated(actionName) {
            @Override
            protected void performActionOn(WebDriver value) {
                actionsFunction.apply(new Actions(value), target).perform();
            }
        }.performOn(currentContent());
    }

    private static InteractiveAction_Deprecated performActionWithDefinitionHowToFindElement(String actionName,
                                                                                            SearchSupplier<?> howToFind,
                                                                                            BiFunction<Actions, WebElement, Actions> actionsFunction) {
        checkArgument(nonNull(howToFind), "The definition how to find the " +
                "element should be a not null value");
        return new InteractiveAction_Deprecated(actionName) {
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
     * Builds an action that performs context click at the current mouse location
     *
     * @return an anonymous instance of {@link InteractiveAction_Deprecated}
     */
    public static InteractiveAction_Deprecated contextClick() {
        return performActionOnWebDriverOnly("Context click on current mouse location", Actions::contextClick);
    }

    /**
     * Builds an action that performs context click at the center of the given web element.
     *
     * @param target element to move to.
     * @return an anonymous instance of {@link InteractiveAction_Deprecated}
     */
    public static InteractiveAction_Deprecated contextClick(WebElement target) {
        return performActionOnWebElement(format("Context click on %s", target), target, Actions::contextClick);
    }

    /**
     * Builds an action that performs context click at the center of the given widget element.
     *
     * @param target element to move to.
     * @return an anonymous instance of {@link InteractiveAction_Deprecated}
     * @see #contextClick(WebElement)
     */
    public static InteractiveAction_Deprecated contextClick(Widget target) {
        checkArgument(nonNull(target), "Target widget should be a not null value");
        return contextClick(target.getWrappedElement());
    }

    /**
     * <p>Builds an action that clicks at the center of some element.</p>
     *
     * @param howToFind is the definition of a way how to find the target element
     * @return an anonymous instance of {@link InteractiveAction_Deprecated}
     * @see #contextClick(WebElement)
     * @see #contextClick(Widget)
     */
    public static InteractiveAction_Deprecated contextClick(SearchSupplier<?> howToFind) {
        return performActionWithDefinitionHowToFindElement(format("Context click on %s", howToFind),
                howToFind, Actions::contextClick);
    }

    /**
     * Builds an action that performs click-and-hold at the location of the source element,
     * moves to the location of the target element, then releases the mouse.
     *
     * @param source element to emulate button down at.
     * @param target element to move to and release the mouse at.
     * @return an anonymous instance of {@link InteractiveAction_Deprecated}
     */
    public static InteractiveAction_Deprecated dragAndDrop(WebElement source, WebElement target) {
        checkArgument(nonNull(source), "Source web element should be a not null value");
        checkArgument(nonNull(target), "Target web element should be a not null value");
        return performActionOnWebDriverOnly(format("Drag and drop from %s to %s", source, target),
                actions -> actions.dragAndDrop(source, target));
    }

    /**
     * Builds an action that performs click-and-hold at the location of the source element,
     * moves to the location of the target element, then releases the mouse.
     *
     * @param source element to emulate button down at.
     * @param target element to move to and release the mouse at.
     * @return an anonymous instance of {@link InteractiveAction_Deprecated}
     * @see #dragAndDrop(WebElement, WebElement)
     */
    public static InteractiveAction_Deprecated dragAndDrop(Widget source, WebElement target) {
        checkArgument(nonNull(source), "Source widget element should be a not null value");
        return dragAndDrop(source.getWrappedElement(), target);
    }

    /**
     * Builds an action that performs click-and-hold at the location of the source element,
     * moves to the location of the target element, then releases the mouse.
     *
     * @param source element to emulate button down at.
     * @param target element to move to and release the mouse at.
     * @return an anonymous instance of {@link InteractiveAction_Deprecated}
     * @see #dragAndDrop(WebElement, WebElement)
     */
    public static InteractiveAction_Deprecated dragAndDrop(WebElement source, Widget target) {
        checkArgument(nonNull(target), "Target widget element should be a not null value");
        return dragAndDrop(source, target.getWrappedElement());
    }

    /**
     * Builds an action that performs click-and-hold at the location of the source element,
     * moves to the location of the target element, then releases the mouse.
     *
     * @param source element to emulate button down at.
     * @param target element to move to and release the mouse at.
     * @return an anonymous instance of {@link InteractiveAction_Deprecated}
     * @see #dragAndDrop(WebElement, WebElement)
     */
    public static InteractiveAction_Deprecated dragAndDrop(Widget source, Widget target) {
        checkArgument(nonNull(source), "Source widget element should be a not null value");
        checkArgument(nonNull(target), "Target widget element should be a not null value");
        return dragAndDrop(source.getWrappedElement(), target.getWrappedElement());
    }

    /**
     * Builds an action that performs click-and-hold at the location of the source element,
     * moves to the location of the target element, then releases the mouse.
     *
     * @param howToFindSourceElement is the definition of a way how to find the element to emulate button down at.
     * @param target                 element to move to and release the mouse at.
     * @return an anonymous instance of {@link InteractiveAction_Deprecated}
     * @see #dragAndDrop(WebElement, WebElement)
     * @see #dragAndDrop(Widget, WebElement)
     */
    public static InteractiveAction_Deprecated dragAndDrop(SearchSupplier<?> howToFindSourceElement, WebElement target) {
        checkArgument(nonNull(target), "Target web element should be a not null value");
        return performActionWithDefinitionHowToFindElement(format("Drag and drop from %s to %s", howToFindSourceElement, target),
                howToFindSourceElement, (actions, webElement) -> actions.dragAndDrop(webElement, target));
    }

    /**
     * Builds an action that performs click-and-hold at the location of the source element,
     * moves to the location of the target element, then releases the mouse.
     *
     * @param howToFindSourceElement is the definition of a way how to find the element to emulate button down at.
     * @param target                 element to move to and release the mouse at.
     * @return an anonymous instance of {@link InteractiveAction_Deprecated}
     * @see #dragAndDrop(WebElement, Widget)
     * @see #dragAndDrop(Widget, Widget)
     */
    public static InteractiveAction_Deprecated dragAndDrop(SearchSupplier<?> howToFindSourceElement, Widget target) {
        checkArgument(nonNull(target), "Target widget element should be a not null value");
        return dragAndDrop(howToFindSourceElement, target.getWrappedElement());
    }

    /**
     * Builds an action that performs click-and-hold at the location of the source element,
     * moves to the location of the target element, then releases the mouse.
     *
     * @param source                 element to emulate button down at.
     * @param howToFindTargetElement is the definition of a way how to find the element to move to and release the mouse at.
     * @return an anonymous instance of {@link InteractiveAction_Deprecated}
     * @see #dragAndDrop(WebElement, WebElement)
     * @see #dragAndDrop(Widget, WebElement)
     */
    public static InteractiveAction_Deprecated dragAndDrop(WebElement source, SearchSupplier<?> howToFindTargetElement) {
        checkArgument(nonNull(source), "Source web element should be a not null value");
        return performActionWithDefinitionHowToFindElement(format("Drag and drop from %s to %s", source, howToFindTargetElement),
                howToFindTargetElement, (actions, webElement) -> actions.dragAndDrop(source, webElement));
    }

    /**
     * Builds an action that performs click-and-hold at the location of the source element,
     * moves to the location of the target element, then releases the mouse.
     *
     * @param source                 element to emulate button down at.
     * @param howToFindTargetElement is the definition of a way how to find the element to move to and release the mouse at.
     * @return an anonymous instance of {@link InteractiveAction_Deprecated}
     * @see #dragAndDrop(WebElement, Widget)
     * @see #dragAndDrop(Widget, Widget)
     */
    public static InteractiveAction_Deprecated dragAndDrop(Widget source, SearchSupplier<?> howToFindTargetElement) {
        checkArgument(nonNull(source), "Target widget element should be a not null value");
        return dragAndDrop(source.getWrappedElement(), howToFindTargetElement);
    }

    /**
     * Builds an action that performs click-and-hold at the location of the source element,
     * moves to the location of the target element, then releases the mouse.
     *
     * @param howToFindSourceElement is the definition of a way how to find the element to emulate button down at..
     * @param howToFindTargetElement is the definition of a way how to find the element to move to and release the mouse at.
     * @return an anonymous instance of {@link InteractiveAction_Deprecated}
     * @see #dragAndDrop(WebElement, Widget)
     * @see #dragAndDrop(Widget, Widget)
     */
    public static InteractiveAction_Deprecated dragAndDrop(SearchSupplier<?> howToFindSourceElement,
                                                           SearchSupplier<?> howToFindTargetElement) {
        checkArgument(nonNull(howToFindSourceElement), "The definition how to find the source " +
                "element should be a not null value");
        checkArgument(nonNull(howToFindTargetElement), "The definition how to find the target " +
                "element should be a not null value");
        return new InteractiveAction_Deprecated(format("Drag and drop. From: %s. To: %s", howToFindSourceElement, howToFindTargetElement)) {
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
     * @param source  element to emulate button down at.
     * @param xOffset horizontal move offset.
     * @param yOffset vertical move offset.
     * @return an anonymous instance of {@link InteractiveAction_Deprecated}
     */
    public static InteractiveAction_Deprecated dragAndDropBy(WebElement source, int xOffset, int yOffset) {
        return performActionOnWebElement(getOffsetDescription(format("Drag and drop from %s", source), xOffset, yOffset), source,
                (actions, webElement) -> actions.dragAndDropBy(webElement, xOffset, yOffset));
    }

    /**
     * Builds an action that performs click-and-hold at the location of the source element,
     * moves by a given offset, then releases the mouse.
     *
     * @param source  element to emulate button down at.
     * @param xOffset horizontal move offset.
     * @param yOffset vertical move offset.
     * @return an anonymous instance of {@link InteractiveAction_Deprecated}
     * @see #dragAndDropBy(WebElement, int, int)
     */
    public static InteractiveAction_Deprecated dragAndDropBy(Widget source, int xOffset, int yOffset) {
        checkArgument(nonNull(source), "Source widget element should be a not null value");
        return dragAndDropBy(source.getWrappedElement(), xOffset, yOffset);
    }

    /**
     * Builds an action that performs click-and-hold at the location of the source element,
     * moves by a given offset, then releases the mouse.
     *
     * @param howToFindSourceElement is the definition of a way how to find the element to emulate button down at.
     * @param xOffset                horizontal move offset.
     * @param yOffset                vertical move offset.
     * @return an anonymous instance of {@link InteractiveAction_Deprecated}
     * @see #dragAndDropBy(WebElement, int, int)
     */
    public static InteractiveAction_Deprecated dragAndDropBy(SearchSupplier<?> howToFindSourceElement, int xOffset, int yOffset) {
        return performActionWithDefinitionHowToFindElement(getOffsetDescription(format("Drag and drop from %s", howToFindSourceElement),
                xOffset, yOffset),
                howToFindSourceElement, (actions, webElement) -> actions.dragAndDropBy(webElement, xOffset, yOffset));
    }
}
