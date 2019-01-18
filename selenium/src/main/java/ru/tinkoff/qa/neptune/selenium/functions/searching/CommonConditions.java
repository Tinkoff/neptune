package ru.tinkoff.qa.neptune.selenium.functions.searching;

import ru.tinkoff.qa.neptune.selenium.api.widget.*;
import org.openqa.selenium.*;
import org.openqa.selenium.internal.WrapsElement;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.Objects.nonNull;
import static ru.tinkoff.qa.neptune.core.api.steps.AsIsCondition.AS_IS;
import static ru.tinkoff.qa.neptune.core.api.steps.StoryWriter.condition;
import static ru.tinkoff.qa.neptune.selenium.properties.SessionFlagProperties.FIND_ONLY_VISIBLE_ELEMENTS_WHEN_NO_CONDITION;
import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static java.lang.String.format;
import static java.util.Arrays.asList;
import static java.util.Arrays.stream;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;
import static org.apache.commons.lang3.StringUtils.isBlank;

@SuppressWarnings("unchecked")
public final class CommonConditions {

    private CommonConditions() {
        super();
    }

    static Predicate defaultPredicate() {
        if (FIND_ONLY_VISIBLE_ELEMENTS_WHEN_NO_CONDITION.get()) {
            return shouldBeVisible();
        }
        else {
            return AS_IS;
        }
    }

    /**
     * @param <T> is type of elements under the test. It is expected to extend {@link WebElement}
     *           or {@link IsVisible} or {@link WrapsElement}. The {@link Predicate#test(Object)} method throws
     *           {@link UnsupportedOperationException} otherwise.
     * @return predicate that checks is some element visible or not
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
                        .orElseThrow(() -> new NullPointerException("It was expected that wrapped element differs from null. " +
                                "It is impossible to get visibility."));
            }

            throw new UnsupportedOperationException(format("It is impossible to get visibility of the instance of %s. Instance of " +
                            "%s or subclass of %s and %s is expected.", tClass.getName(), WebElement.class.getName(),
                    SearchContext.class.getName(), IsVisible.class.getName()));
        });
    }

    /**
     * @param <T> is type of elements under the test. It is expected to extend {@link WebElement}
     *           or {@link IsEnabled} or {@link WrapsElement}. The {@link Predicate#test(Object)} method throws
     *           {@link UnsupportedOperationException} otherwise.
     * @return predicate that checks is some element enabled or not
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
                        .map(WebElement::isEnabled)
                        .orElseThrow(() -> new NullPointerException("It was expected that wrapped element differs from null. " +
                                "It is impossible to check is it enable or not."));
            }

            throw new UnsupportedOperationException(format("It is impossible to check is instance of %s enable or not. Instance of " +
                            "%s or subclass of %s and %s is expected.", tClass.getName(), WebElement.class.getName(),
                    SearchContext.class.getName(), IsEnabled.class.getName()));
        });
    }

    /**
     * @param text which element should have
     * @param <T> is type of elements under the test. It is expected to extend {@link WebElement}
     *           or {@link WrapsElement}. The {@link Predicate#test(Object)} method returns {@code false}
     *           otherwise.
     * @return predicate that checks element by text
     */
    public static <T extends SearchContext> Predicate<T>  shouldHaveText(String text) {
        checkArgument(!isBlank(text), "String which is used to check text " +
                "of an element should not be null or empty. ");
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
     * @param pattern is a regExp pattern to check text of an element
     * @param <T> is type of elements under the test. It is expected to extend {@link WebElement}
     *           or {@link WrapsElement}. The {@link Predicate#test(Object)} method returns {@code false}
     *           otherwise.
     * @return predicate that checks element by text and reg exp pattern
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
     * @param <T> is type of elements under the test. It is expected to extend {@link WebElement}
     *      or {@link HasAttribute} or {@link WrapsElement}. The {@link Predicate#test(Object)} method throws
     *      {@link UnsupportedOperationException} otherwise.
     * @param attribute of an element to check
     * @param attrValue desired value of the attribute
     * @return predicate that checks value of the attribute.
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
                        .orElseThrow(() -> new NullPointerException(format("It was expected that wrapped element differs from null. " +
                                "It was impossible to value of the attribute %s from the instance of %s", attribute, tClass)));
            }

            throw new UnsupportedOperationException(format("It is impossible to get value of thr attribute %s from " +
                            "the instance of %s. Instance of " +
                            "%s or subclass of %s and %s is expected.", attribute, tClass.getName(), WebElement.class.getName(),
                    SearchContext.class.getName(), HasAttribute.class.getName()));
        });
    }

    /**
     * @param <T> is type of elements under the test. It is expected to extend {@link WebElement}
     *      or {@link HasAttribute} or {@link WrapsElement}. The {@link Predicate#test(Object)} method throws
     *      {@link UnsupportedOperationException} otherwise.
     * @param attribute of an element to check
     * @param attrValue substring that should be contained by value of the attribute
     * @return predicate that checks value of the attribute.
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
                        .orElseThrow(() -> new NullPointerException(format("It was expected that wrapped element differs from null. " +
                                "It was impossible to value of the attribute %s from the instance of %s", attribute, tClass)));
            }
            throw new UnsupportedOperationException(format("It is impossible to get value of thr attribute %s from " +
                            "the instance of %s. Instance of " +
                            "%s or subclass of %s and %s is expected.", attribute, tClass.getName(), WebElement.class.getName(),
                    SearchContext.class.getName(), HasAttribute.class.getName()));
        });
    }

    /**
     * @param <T> is type of elements under the test. It is expected to extend {@link WebElement}
     *      or {@link HasAttribute} or {@link WrapsElement}. The {@link Predicate#test(Object)} method throws
     *      {@link UnsupportedOperationException} otherwise.
     * @param attribute of an element to check
     * @param pattern is a regex pattern to check the attribute value
     * @return predicate that checks value of the attribute.
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
                        .orElseThrow(() -> new NullPointerException(format("It was expected that wrapped element differs from null. " +
                                "It was impossible to value of the attribute %s from the instance of %s", attribute, tClass)));
            }

            throw new UnsupportedOperationException(format("It is impossible to get value of thr attribute %s from " +
                            "the instance of %s. Instance of " +
                            "%s or subclass of %s and %s is expected.", attribute, tClass.getName(), WebElement.class.getName(),
                    SearchContext.class.getName(), HasAttribute.class.getName()));
        });
    }

    /**
     * @param <T> is type of elements under the test. It is expected to extend {@link WebElement}
     *      or {@link HasCssValue} or {@link WrapsElement}. The {@link Predicate#test(Object)} method throws
     *      {@link UnsupportedOperationException} otherwise.
     * @param cssProperty of an element to check
     * @param cssValue desired value of the css property
     * @return predicate that checks value of the css property.
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
                        .orElseThrow(() -> new NullPointerException(format("It was expected that wrapped element differs from null. " +
                                "It was impossible to value of the css property %s from the instance of %s", cssProperty, tClass)));
            }

            throw new UnsupportedOperationException(format("It is impossible to get value of thr css property %s from " +
                            "the instance of %s. Instance of " +
                            "%s or subclass of %s and %s is expected.", cssProperty, tClass.getName(), WebElement.class.getName(),
                    SearchContext.class.getName(), HasCssValue.class.getName()));
        });
    }

    /**
     * @param <T> is type of elements under the test. It is expected to extend {@link WebElement}
     *      or {@link HasCssValue} or {@link WrapsElement}. The {@link Predicate#test(Object)} method throws
     *      {@link UnsupportedOperationException} otherwise.
     * @param cssProperty of an element to check
     * @param cssValue substring that should be contained by value of the css property
     * @return predicate that checks value of the css property.
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
                        .orElseThrow(() -> new NullPointerException(format("It was expected that wrapped element differs from null. " +
                                "It was impossible to value of the css property %s from the instance of %s", cssProperty, tClass)));
            }

            throw new UnsupportedOperationException(format("It is impossible to get value of thr css property %s from " +
                            "the instance of %s. Instance of " +
                            "%s or subclass of %s and %s is expected.", cssProperty, tClass.getName(), WebElement.class.getName(),
                    SearchContext.class.getName(), HasCssValue.class.getName()));
        });
    }

    /**
     * @param <T> is type of elements under the test. It is expected to extend {@link WebElement}
     *      or {@link HasCssValue} or {@link WrapsElement}. The {@link Predicate#test(Object)} method throws
     *      {@link UnsupportedOperationException} otherwise.
     * @param cssProperty of an element to check
     * @param pattern is a regex pattern to check the css value
     * @return predicate that checks value of the css property.
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
                        .orElseThrow(() -> new NullPointerException(format("It was expected that wrapped element differs from null. " +
                                "It was impossible to value of the css property %s from the instance of %s", cssProperty, tClass)));
            }
            throw new UnsupportedOperationException(format("It is impossible to get value of thr css property %s from " +
                            "the instance of %s. Instance of " +
                            "%s or subclass of %s and %s is expected.", cssProperty, tClass.getName(), WebElement.class.getName(),
                    SearchContext.class.getName(), HasCssValue.class.getName()));
        });
    }

    /**
     * @param <T> is type of elements under the test.
     * @param howToFind is the way to find nested elements.
     * @return predicate that checks presence of child elements inside an element.
     */
    public static <T extends SearchContext> Predicate<T> shouldContainElements(MultipleSearchSupplier<?> howToFind) {
        checkArgument(nonNull(howToFind), "The way how to find nested elements should be defined");
        return condition(format("has nested %s", howToFind), t -> howToFind.get().apply(t).size() > 0);
    }

    /**
     * @param <T> is type of elements under the test.
     * @param howToFind is the way to find nested elements.
     * @param expected is the count of expected nested elements.
     * @return predicate that checks presence of child elements inside an element.
     */
    public static <T extends SearchContext> Predicate<T> shouldContainElements(MultipleSearchSupplier<?> howToFind, int expected) {
        checkArgument(nonNull(howToFind), "The way how to find nested elements should be defined");
        checkArgument(expected >=0 , "Count of expected nested elements can't be a negative value.");
        return condition(format("has %s nested %s", expected, howToFind),
                t -> howToFind.get().apply(t).size() == expected);
    }

    /**
     * @param <T> is type of elements under the test.
     * @param labels which can be used for the seeking element
     * @return predicate that checks labels of a complex element
     */
    public static  <T extends SearchContext & Labeled> Predicate<T> shouldBeLabeledBy(String...labels) {
        checkNotNull(labels);
        checkArgument(labels.length > 0, "At least one label should be defined");
        return condition(format("has label(s) %s", String.join("and ", stream(labels).collect(toList()))),
                t -> t.labels().containsAll(asList(labels)));
    }
}
