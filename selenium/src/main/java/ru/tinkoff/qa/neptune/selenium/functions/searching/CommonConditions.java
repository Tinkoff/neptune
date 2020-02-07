package ru.tinkoff.qa.neptune.selenium.functions.searching;

import ru.tinkoff.qa.neptune.core.api.exception.management.IgnoresThrowable;
import ru.tinkoff.qa.neptune.core.api.steps.StepFunction;
import ru.tinkoff.qa.neptune.core.api.steps.TurnsRetortingOff;
import ru.tinkoff.qa.neptune.selenium.api.widget.*;
import org.openqa.selenium.*;
import org.openqa.selenium.internal.WrapsElement;

import java.time.Duration;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.time.Duration.ofMillis;
import static java.util.Objects.nonNull;
import static ru.tinkoff.qa.neptune.core.api.steps.Condition.condition;
import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static java.lang.String.format;
import static java.util.Arrays.asList;
import static java.util.Optional.ofNullable;
import static org.apache.commons.lang3.StringUtils.isBlank;

public final class CommonConditions {

    private CommonConditions() {
        super();
    }

    /**
     * The checking of an element/widget visibility.
     *
     * @param <T> is a type of element/widget to be visible
     * @return predicate that checks/filters an element/widget
     */
    public static <T extends SearchContext> Predicate<T> shouldBeVisible() {
        return condition("visible", t -> {
            var tClass = t.getClass();
            if (WebElement.class.isAssignableFrom(tClass)) {
                return ((WebElement) t).isDisplayed();
            }

            if (IsVisible.class.isAssignableFrom(tClass)) {
                return ((IsVisible) t).isVisible();
            }

            if (WrapsElement.class.isAssignableFrom(tClass)) {
                return ofNullable(((WrapsElement) t).getWrappedElement())
                        .map(WebElement::isDisplayed)
                        .orElse(false);
            }

            return false;
        });
    }

    /**
     * The checking of an element/widget is enabled or not.
     *
     * @param <T> is a type of element/widget to be enabled
     * @return predicate that checks/filters an element/widget
     */
    public static <T extends SearchContext> Predicate<T> shouldBeEnabled() {
        return condition("enabled", t -> {
            var tClass = t.getClass();
            if (WebElement.class.isAssignableFrom(tClass)) {
                return ((WebElement) t).isEnabled();
            }

            if (IsEnabled.class.isAssignableFrom(tClass)) {
                return ((IsEnabled) t).isEnabled();
            }

            if (WrapsElement.class.isAssignableFrom(tClass)) {
                return ofNullable(((WrapsElement) t).getWrappedElement())
                        .map(WebElement::isDisplayed)
                        .orElse(false);
            }

            return false;
        });
    }

    /**
     * The checking of an element/widget text.
     *
     * @param text that an element/widget is supposed to have
     * @param <T> is a type of element/widget
     * @return predicate that checks/filters an element/widget
     */
    public static <T extends SearchContext> Predicate<T>  shouldHaveText(String text) {
        checkArgument(!isBlank(text), "String is used to check text " +
                "of an element should not be null or empty.");
        return condition(format("has text '%s'", text), t -> {
            var clazz = t.getClass();
            if (WebElement.class.isAssignableFrom(clazz)) {
                return text.equals(((WebElement) t).getText());
            }

            if (WrapsElement.class.isAssignableFrom(clazz)) {
                return ofNullable(((WrapsElement) t).getWrappedElement())
                        .map(webElement -> text.equals(webElement.getText()))
                        .orElse(false);
            }

            return false;
        });
    }

    /**
     * The checking of an element/widget text.
     *
     * @param pattern is a regExp pattern to check text of an element/widget
     * @param <T> is a type of element/widget
     * @return predicate that checks/filters an element/widget
     */
    public static <T extends SearchContext> Predicate<T> shouldHaveText(Pattern pattern) {
        checkArgument(nonNull(pattern), "RegEx pattern should be defined");
        return condition(format("has text that meets regExp pattern '%s'", pattern), t -> {
            var clazz = t.getClass();
            if (WebElement.class.isAssignableFrom(clazz)) {
                return pattern.matcher(((WebElement) t).getText()).find();
            }
            else if (WrapsElement.class.isAssignableFrom(clazz)) {
                return ofNullable(((WrapsElement) t).getWrappedElement())
                        .map(webElement -> pattern.matcher(webElement.getText()).find())
                        .orElse(false);
            }
            return false;
        });
    }

    /**
     * The checking of an element/widget by attribute value.
     *
     * @param <T> is a type of element/widget
     * @param attribute that an element/widget is supposed to have
     * @param attrValue desired value of the attribute
     * @return predicate that checks/filters an element/widget
     */
    public static <T extends SearchContext> Predicate<T> shouldHaveAttribute(String attribute, String attrValue) {
        checkArgument(!isBlank(attribute), "Attribute name should not be empty or null.");
        checkArgument(!isBlank(attrValue), "Attribute value should not be empty or null.");

        return condition(format("has attribute '%s=\"%s\"'", attribute, attrValue), t -> {
            var tClass = t.getClass();
            if (WebElement.class.isAssignableFrom(tClass)) {
                return attrValue.equals(((WebElement) t).getAttribute(attribute));
            }

            if (HasAttribute.class.isAssignableFrom(tClass)) {
                return attrValue.equals(((HasAttribute) t).getAttribute(attribute));
            }

            if (WrapsElement.class.isAssignableFrom(tClass)) {
                return ofNullable(((WrapsElement) t).getWrappedElement())
                        .map(webElement -> attrValue.equals(webElement.getAttribute(attribute)))
                        .orElse(false);
            }

            return false;
        });
    }

    /**
     * The checking of an element/widget by attribute value.
     * 
     * @param <T> is a type of element/widget
     * @param attribute that an element/widget is supposed to have
     * @param attrValue substring that supposed to be contained by value of the attribute
     * @return predicate that checks/filters an element/widget
     */
    public static <T extends SearchContext> Predicate<T> shouldHaveAttributeContains(String attribute, String attrValue) {
        checkArgument(!isBlank(attribute), "Attribute name should not be empty or null.");
        checkArgument(!isBlank(attrValue), "Attribute value should not be empty or null.");

        return condition(format("has attribute '%s' that contains string '%s'", attribute, attrValue), t -> {
            var tClass = t.getClass();
            if (WebElement.class.isAssignableFrom(tClass)) {
                return ofNullable(((WebElement) t).getAttribute(attribute))
                        .map(s -> s.contains(attrValue)).orElse(false);
            }

            if (HasAttribute.class.isAssignableFrom(tClass)) {
                return ofNullable(((HasAttribute) t).getAttribute(attribute))
                        .map(s -> s.contains(attrValue)).orElse(false);
            }

            if (WrapsElement.class.isAssignableFrom(tClass)) {
                return ofNullable(((WrapsElement) t).getWrappedElement())
                        .map(webElement -> ofNullable(webElement.getAttribute(attribute))
                                .map(s -> s.contains(attrValue))
                                .orElse(false))
                        .orElse(false);
            }
            return false;
        });
    }

    /**
     * The checking of an element/widget by attribute value.
     *
     * @param <T> is a type of element/widget
     * @param attribute that an element/widget is supposed to have
     * @param pattern is a regex pattern to check the attribute value
     * @return predicate that checks/filters an element/widget
     */
    public static <T extends SearchContext> Predicate<T> shouldHaveAttributeContains(String attribute, Pattern pattern) {
        checkArgument(!isBlank(attribute), "Attribute name should not be empty or null.");
        checkArgument(nonNull(pattern), "RegEx pattern of the desired " +
                "attribute value should be defined.");

        return condition(format("has attribute '%s' that meets " +
                "regExp pattern '%s'", attribute, pattern), t -> {
            var tClass = t.getClass();
            if (WebElement.class.isAssignableFrom(tClass)) {
                return ofNullable(((WebElement) t).getAttribute(attribute))
                        .map(s -> {
                            Matcher m = pattern.matcher(s);
                            return m.find();
                        }).orElse(false);
            }

            if (HasAttribute.class.isAssignableFrom(tClass)) {
                return ofNullable(((HasAttribute) t).getAttribute(attribute))
                        .map(s -> {
                            Matcher m = pattern.matcher(s);
                            return m.find();
                        }).orElse(false);
            }

            if (WrapsElement.class.isAssignableFrom(tClass)) {
                return ofNullable(((WrapsElement) t).getWrappedElement())
                        .map(webElement -> ofNullable(webElement.getAttribute(attribute))
                                .map(s -> {
                                    Matcher m = pattern.matcher(s);
                                    return m.find();
                                })
                                .orElse(false))
                        .orElse(false);
            }

            return false;
        });
    }

    /**
     * The checking of an element/widget by css value.
     *
     * @param <T> is a type of element/widget
     * @param cssProperty that an element/widget is supposed to have
     * @param cssValue desired value of the css property
     * @return predicate that checks/filters an element/widget
     */
    public static <T extends SearchContext> Predicate<T> shouldHaveCssValue(String cssProperty, String cssValue) {
        checkArgument(!isBlank(cssProperty), "Css property should not be empty or null.");
        checkArgument(!isBlank(cssValue), "Css value should not be empty or null.");

        return condition(format("has css property '%s=\"%s\"'", cssProperty, cssValue), t -> {
            var tClass = t.getClass();
            if (WebElement.class.isAssignableFrom(tClass)) {
                return cssValue.equals(((WebElement) t).getCssValue(cssProperty));
            }

            if (HasCssValue.class.isAssignableFrom(tClass)) {
                return cssValue.equals(((HasCssValue) t).getCssValue(cssProperty));
            }

            if (WrapsElement.class.isAssignableFrom(tClass)) {
                return ofNullable(((WrapsElement) t).getWrappedElement())
                        .map(webElement -> cssValue.equals(webElement.getCssValue(cssProperty)))
                        .orElse(false);
            }

            return false;
        });
    }

    /**
     * The checking of an element/widget by css value.
     *
     * @param <T> is a type of element/widget
     * @param cssProperty that an element/widget is supposed to have
     * @param cssValue substring that supposed to be contained by value of the css property
     * @return predicate that checks/filters an element/widget
     */
    public static <T extends SearchContext> Predicate<T> shouldHaveCssValueContains(String cssProperty, String cssValue) {
        checkArgument(!isBlank(cssProperty), "Css property should not be empty or null.");
        checkArgument(!isBlank(cssValue), "Css value should not be empty or null.");

        return condition(format("has css property '%s' that contains string '%s'", cssProperty, cssValue), t -> {
            var tClass = t.getClass();
            if (WebElement.class.isAssignableFrom(tClass)) {
                return ofNullable(((WebElement) t).getCssValue(cssProperty))
                        .map(s -> s.contains(cssValue)).orElse(false);
            }

            if (HasCssValue.class.isAssignableFrom(tClass)) {
                return ofNullable(((HasCssValue) t).getCssValue(cssProperty))
                        .map(s -> s.contains(cssValue)).orElse(false);
            }

            if (WrapsElement.class.isAssignableFrom(tClass)) {
                return ofNullable(((WrapsElement) t).getWrappedElement())
                        .map(webElement -> ofNullable(webElement.getCssValue(cssProperty))
                                .map(s -> s.contains(cssValue))
                                .orElse(false))
                        .orElse(false);
            }

            return false;
        });
    }

    /**
     * The checking of an element/widget by css value.
     *
     * @param <T> is a type of element/widget
     * @param cssProperty that an element/widget is supposed to have
     * @param pattern is a regex pattern to check the css value
     * @return predicate that checks/filters an element/widget
     */
    public static <T extends SearchContext> Predicate<T> shouldHaveCssValueContains(String cssProperty, Pattern pattern) {
        checkArgument(!isBlank(cssProperty), "Css property should not be empty or null.");
        checkArgument(nonNull(pattern), "RegEx pattern of the desired " +
                "css value should be defined.");

        return condition(format("has css property '%s' that meets " +
                "regExp pattern '%s'", cssProperty, pattern), t -> {
            Class<?> tClass = t.getClass();
            if (WebElement.class.isAssignableFrom(tClass)) {
                return ofNullable(((WebElement) t).getCssValue(cssProperty))
                        .map(s -> {
                            Matcher m = pattern.matcher(s);
                            return m.find();
                        }).orElse(false);
            }

            if (HasCssValue.class.isAssignableFrom(tClass)) {
                return ofNullable(((HasCssValue) t).getCssValue(cssProperty))
                        .map(s -> {
                            Matcher m = pattern.matcher(s);
                            return m.find();
                        }).orElse(false);
            }

            if (WrapsElement.class.isAssignableFrom(tClass)) {
                return ofNullable(((WrapsElement) t).getWrappedElement())
                        .map(webElement -> ofNullable(webElement.getCssValue(cssProperty))
                                .map(s -> {
                                    Matcher m = pattern.matcher(s);
                                    return m.find();
                                })
                                .orElse(false))
                        .orElse(false);
            }

            return false;
        });
    }

    /**
     * The checking of an element/widget by presence of nested elements.
     *
     * @param <T> is a type of element/widget
     * @param howToFind is the way to find nested elements.
     *                  <p>NOTE!!!</p>
     *                  When there is a timeout passed through {@link MultipleSearchSupplier#timeOut(Duration)} of the given
     *                  {@code howToFind} then it is ignored here.
     * @return predicate that checks/filters an element/widget
     */
    public static <T extends SearchContext> Predicate<T> shouldContainElements(MultipleSearchSupplier<?> howToFind) {
        checkArgument(nonNull(howToFind), "The way how to find nested elements should be defined");
        var func = howToFind.clone().timeOut(ofMillis(0)).get();
        if (TurnsRetortingOff.class.isAssignableFrom(func.getClass())) {
            ((TurnsRetortingOff) func).turnReportingOff();
        }
        return condition(format("has nested %s", howToFind), t -> func.apply(t).size() > 0);
    }

    /**
     * The checking of an element/widget by presence of nested elements
     *
     * @param <T> is a type of element/widget
     * @param howToFind is the way to find nested elements.
     *                  <p>NOTE!!!</p>
     *                  When there is a timeout passed through {@link MultipleSearchSupplier#timeOut(Duration)} of the given
     *                  {@code howToFind} then it is ignored here.
     * @param expected is the count of expected nested elements
     * @return predicate that checks/filters an element/widget
     */
    public static <T extends SearchContext> Predicate<T> shouldContainElements(MultipleSearchSupplier<?> howToFind, int expected) {
        checkArgument(nonNull(howToFind), "The way how to find nested elements should be defined");
        checkArgument(expected >=0 , "Count of expected nested elements can't be a negative or zero value.");
        var func = howToFind.clone().timeOut(ofMillis(0)).get();
        if (TurnsRetortingOff.class.isAssignableFrom(func.getClass())) {
            ((TurnsRetortingOff) func).turnReportingOff();
        }
        return condition(format("has %s nested %s", expected, howToFind),
                t -> func.apply(t).size() == expected);
    }

    /**
     * The checking of an element/widget by presence of a nested element
     *
     * @param <T> is a type of element/widget
     * @param howToFind is the way to find a nested element.
     *                  <p>NOTE!!!</p>
     *                  When there is a timeout passed through {@link SearchSupplier#timeOut(Duration)} of the given
     *                  {@code howToFind} then it is ignored here.
     * @return predicate that checks/filters an element/widget
     */
    @SuppressWarnings("unchecked")
    public static <T extends SearchContext> Predicate<T> shouldContainElement(SearchSupplier<?> howToFind) {
        checkArgument(nonNull(howToFind), "The way how to find nested elements should be defined");
        var func = ((IgnoresThrowable<StepFunction<SearchContext, SearchContext>>) howToFind.clone().timeOut(ofMillis(0)).get())
                .addIgnored(NoSuchElementException.class);
        func.turnReportingOff();
        return condition(format("has nested %s", howToFind), t -> func.apply(t) != null);
    }

    static  <T extends SearchContext> Predicate<T> shouldBeLabeledBy(String...labels) {
        checkNotNull(labels);
        checkArgument(labels.length > 0, "At least one label should be defined");
        Predicate<T> condition =  condition(format("has label(s) %s", String.join("and ", labels)),
                t -> Labeled.class.isAssignableFrom(t.getClass()) && ((Labeled) t).labels().containsAll(asList(labels)));
        ((TurnsRetortingOff<?>) condition).turnReportingOff();
        return condition;
    }
}
