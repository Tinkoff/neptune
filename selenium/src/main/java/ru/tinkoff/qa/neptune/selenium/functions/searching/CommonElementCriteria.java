package ru.tinkoff.qa.neptune.selenium.functions.searching;

import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.internal.WrapsElement;
import ru.tinkoff.qa.neptune.core.api.steps.Criteria;
import ru.tinkoff.qa.neptune.core.api.steps.StepFunction;
import ru.tinkoff.qa.neptune.selenium.api.widget.*;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Objects;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static java.lang.String.format;
import static java.lang.String.valueOf;
import static java.lang.reflect.Modifier.isStatic;
import static java.time.Duration.ofMillis;
import static java.util.Arrays.stream;
import static java.util.Objects.nonNull;
import static java.util.Optional.ofNullable;
import static java.util.regex.Pattern.compile;
import static java.util.stream.Collectors.toList;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static ru.tinkoff.qa.neptune.core.api.steps.Criteria.NOT;
import static ru.tinkoff.qa.neptune.core.api.steps.Criteria.condition;

public final class CommonElementCriteria {

    private CommonElementCriteria() {
        super();
    }

    /**
     * The checking of an element/widget visibility.
     *
     * @param <T> is a type of element/widget to be visible
     * @return criteria that checks/filters an element/widget
     */
    public static <T extends SearchContext> Criteria<T> visible() {
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
     * @return criteria that checks/filters an element/widget
     */
    public static <T extends SearchContext> Criteria<T> enabled() {
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
     * @param <T>  is a type of element/widget
     * @return criteria that checks/filters an element/widget
     */
    static <T extends SearchContext> Criteria<T> text(String text) {
        checkArgument(isNotBlank(text), "Text should be defined");

        return condition(format("has text '%s'", text), t -> {
            var clazz = t.getClass();
            String elementText = null;

            if (WebElement.class.isAssignableFrom(clazz)) {
                elementText = ((WebElement) t).getText();
            }

            if (WrapsElement.class.isAssignableFrom(clazz)) {
                elementText = ofNullable(((WrapsElement) t).getWrappedElement())
                        .map(WebElement::getText)
                        .orElse(null);
            }

            return Objects.equals(elementText, text);
        });
    }

    static <T extends Widget> Criteria<T> labeled(String label) {
        checkNotNull(label);
        return condition(format("has label '%s'", label), t -> {
            Class<?> cls = t.getClass();
            var labels = new ArrayList<String>();

            while (cls != null) {
                labels.addAll(stream(cls.getDeclaredMethods())
                        .filter(method -> !isStatic(method.getModifiers())
                                && method.getParameterTypes().length == 0
                                && method.getReturnType().equals(String.class)
                                && method.getAnnotation(Label.class) != null)
                        .map(method -> {
                            try {
                                method.setAccessible(true);
                                return (String) method.invoke(t);
                            } catch (Exception e) {
                                return null;
                            }
                        })
                        .filter(Objects::nonNull)
                        .collect(toList()));

                labels.addAll(stream(cls.getDeclaredFields())
                        .filter(field -> !isStatic(field.getModifiers())
                                && WebElement.class.isAssignableFrom(field.getType())
                                && field.getAnnotation(Label.class) != null)
                        .map(field -> {
                            try {
                                field.setAccessible(true);
                                return ((WebElement) field.get(t.selfReference())).getText();
                            } catch (Exception e) {
                                return null;
                            }
                        })
                        .filter(Objects::nonNull)
                        .collect(toList()));

                cls = cls.getSuperclass();
            }

            return labels.contains(label);
        });
    }

    /**
     * The checking of an element/widget text.
     *
     * @param expression is a substring of text that an element/widget is supposed to have.
     *                   It is possible to pass reg exp pattern that text of an element should fit.
     * @param <T>        is a type of element/widget
     * @return criteria that checks/filters an element/widget
     */
    public static <T extends SearchContext> Criteria<T> textMatches(String expression) {
        checkArgument(isNotBlank(expression), "Substring/RegEx pattern should be defined");

        return condition(format("has text that contains '%s' or fits regExp pattern '%s'", expression, expression), t -> {
            var clazz = t.getClass();
            String elementText = null;

            if (WebElement.class.isAssignableFrom(clazz)) {
                elementText = valueOf(((WebElement) t).getText());
            } else if (WrapsElement.class.isAssignableFrom(clazz)) {
                elementText = ofNullable(((WrapsElement) t).getWrappedElement())
                        .map(webElement -> valueOf(webElement.getText()))
                        .orElse(null);
            }

            return ofNullable(elementText)
                    .map(s -> {
                        if (s.contains(expression)) {
                            return true;
                        }

                        try {
                            var p = compile(expression);
                            var mather = p.matcher(s);
                            return mather.matches();
                        } catch (Throwable thrown) {
                            thrown.printStackTrace();
                            return false;
                        }
                    })
                    .orElse(false);
        });
    }

    /**
     * The checking of an element/widget by attribute value.
     *
     * @param <T>       is a type of element/widget
     * @param attribute that an element/widget is supposed to have
     * @param attrValue desired value of the attribute
     * @return criteria that checks/filters an element/widget
     */
    public static <T extends SearchContext> Criteria<T> attr(String attribute, String attrValue) {
        checkArgument(!isBlank(attribute), "Attribute name should be defined");
        checkArgument(!isBlank(attrValue), "Attribute value should be defined");

        return condition(format("has attribute '%s=\"%s\"'", attribute, attrValue), t -> {
            var tClass = t.getClass();
            String attrVal = null;

            if (WebElement.class.isAssignableFrom(tClass)) {
                attrVal = ((WebElement) t).getAttribute(attribute);
            }

            if (HasAttribute.class.isAssignableFrom(tClass)) {
                attrVal = ((HasAttribute) t).getAttribute(attribute);
            }

            if (WrapsElement.class.isAssignableFrom(tClass)) {
                attrVal = ofNullable(((WrapsElement) t).getWrappedElement())
                        .map(webElement -> webElement.getAttribute(attribute))
                        .orElse(null);
            }

            return Objects.equals(attrVal, attrValue);
        });
    }

    /**
     * The checking of an element/widget by attribute value.
     *
     * @param <T>        is a type of element/widget
     * @param attribute  that an element/widget is supposed to have
     * @param expression is a substring of the attribute value that an element/widget is supposed to have.
     *                   It is possible to pass reg exp pattern that attr value of an element should fit.
     * @return criteria that checks/filters an element/widget
     */
    public static <T extends SearchContext> Criteria<T> attrMatches(String attribute, String expression) {
        checkArgument(!isBlank(attribute), "Attribute name should be defined");
        checkArgument(!isBlank(expression), "Substring/RegEx pattern should be defined");

        return condition(format("has attribute '%s' that contains '%s' or " +
                "fits regExp pattern '%s'", attribute, expression, expression), t -> {
            var tClass = t.getClass();
            String attrVal = null;

            if (WebElement.class.isAssignableFrom(tClass)) {
                attrVal = ((WebElement) t).getAttribute(attribute);
            }

            if (HasAttribute.class.isAssignableFrom(tClass)) {
                attrVal = ((HasAttribute) t).getAttribute(attribute);
            }

            if (WrapsElement.class.isAssignableFrom(tClass)) {
                attrVal = ofNullable(((WrapsElement) t).getWrappedElement())
                        .map(webElement -> webElement.getAttribute(attribute))
                        .orElse(null);
            }

            return ofNullable(attrVal)
                    .map(s -> {
                        if (s.contains(expression)) {
                            return true;
                        }

                        try {
                            var p = compile(expression);
                            var mather = p.matcher(s);
                            return mather.matches();
                        } catch (Throwable thrown) {
                            thrown.printStackTrace();
                            return false;
                        }
                    })
                    .orElse(false);
        });
    }

    /**
     * The checking of an element/widget by css value.
     *
     * @param <T>         is a type of element/widget
     * @param cssProperty that an element/widget is supposed to have
     * @param cssValue    desired value of the css property
     * @return criteria that checks/filters an element/widget
     */
    public static <T extends SearchContext> Criteria<T> css(String cssProperty, String cssValue) {
        checkArgument(!isBlank(cssProperty), "Css property should be defined");
        checkArgument(!isBlank(cssValue), "Css value should be defined");

        return condition(format("has css property '%s=\"%s\"'", cssProperty, cssValue), t -> {
            var tClass = t.getClass();
            String val = null;

            if (WebElement.class.isAssignableFrom(tClass)) {
                val = ((WebElement) t).getCssValue(cssProperty);
            }

            if (HasCssValue.class.isAssignableFrom(tClass)) {
                val = ((HasCssValue) t).getCssValue(cssProperty);
            }

            if (WrapsElement.class.isAssignableFrom(tClass)) {
                val = ofNullable(((WrapsElement) t).getWrappedElement())
                        .map(webElement -> webElement.getCssValue(cssProperty))
                        .orElse(null);
            }

            return Objects.equals(val, cssValue);
        });
    }

    /**
     * The checking of an element/widget by css value.
     *
     * @param <T>         is a type of element/widget
     * @param cssProperty that an element/widget is supposed to have
     * @param expression  is a substring of the css value that an element/widget is supposed to have.
     *                    It is possible to pass reg exp pattern that css value of an element should fit.
     * @return criteria that checks/filters an element/widget
     */
    public static <T extends SearchContext> Criteria<T> cssMatches(String cssProperty, String expression) {
        checkArgument(!isBlank(cssProperty), "Css property should be defined");
        checkArgument(!isBlank(expression), "Substring/RegEx pattern should be defined");

        return condition(format("has css property '%s' that contains '%s' or " +
                "fits regExp pattern '%s'", cssProperty, expression, expression), t -> {
            Class<?> tClass = t.getClass();
            String val = null;

            if (WebElement.class.isAssignableFrom(tClass)) {
                val = ((WebElement) t).getCssValue(cssProperty);
            }

            if (HasCssValue.class.isAssignableFrom(tClass)) {
                val = ((HasCssValue) t).getCssValue(cssProperty);
            }

            if (WrapsElement.class.isAssignableFrom(tClass)) {
                val = ofNullable(((WrapsElement) t).getWrappedElement())
                        .map(webElement -> webElement.getCssValue(cssProperty))
                        .orElse(null);
            }

            return ofNullable(val)
                    .map(s -> {
                        if (s.contains(expression)) {
                            return true;
                        }

                        try {
                            var p = compile(expression);
                            var mather = p.matcher(s);
                            return mather.matches();
                        } catch (Throwable thrown) {
                            thrown.printStackTrace();
                            return false;
                        }
                    })
                    .orElse(false);
        });
    }

    /**
     * The checking of an element/widget by presence of nested elements.
     *
     * @param <T>       is a type of element/widget
     * @param howToFind is the way to find nested elements.
     *                  <p>NOTE!!!</p>
     *                  When there is a timeout passed through {@link MultipleSearchSupplier#timeOut(Duration)} of the given
     *                  {@code howToFind} then it is ignored here.
     * @return criteria that checks/filters an element/widget
     */
    public static <T extends SearchContext> Criteria<T> nested(MultipleSearchSupplier<?> howToFind) {
        checkArgument(nonNull(howToFind), "The way how to find nested elements should be defined");
        var func = howToFind.clone().timeOut(ofMillis(0)).get();
        ((StepFunction<?, ?>) func).turnReportingOff();
        return condition(format("has nested %s", howToFind), t -> func.apply(t).size() > 0);
    }

    /**
     * The checking of an element/widget by presence of nested elements
     *
     * @param <T>       is a type of element/widget
     * @param howToFind is the way to find nested elements.
     *                  <p>NOTE!!!</p>
     *                  When there is a timeout passed through {@link MultipleSearchSupplier#timeOut(Duration)} of the given
     *                  {@code howToFind} then it is ignored here.
     * @param expected  is the count of expected nested elements
     * @return criteria that checks/filters an element/widget
     */
    public static <T extends SearchContext> Criteria<T> nested(MultipleSearchSupplier<?> howToFind, int expected) {
        checkArgument(nonNull(howToFind), "The way how to find nested elements should be defined");
        checkArgument(expected >= 0, "Count of expected nested elements can't be a negative or zero value.");
        var func = howToFind.clone().timeOut(ofMillis(0)).get();
        ((StepFunction<?, ?>) func).turnReportingOff();
        return condition(format("has %s nested %s", expected, howToFind),
                t -> func.apply(t).size() == expected);
    }

    /**
     * The checking of an element/widget by presence of a nested element
     *
     * @param <T>       is a type of element/widget
     * @param howToFind is the way to find a nested element.
     *                  <p>NOTE!!!</p>
     *                  When there is a timeout passed through {@link SearchSupplier#timeOut(Duration)} of the given
     *                  {@code howToFind} then it is ignored here.
     * @return criteria that checks/filters an element/widget
     */
    @SuppressWarnings("unchecked")
    public static <T extends SearchContext> Criteria<T> nested(SearchSupplier<?> howToFind) {
        checkArgument(nonNull(howToFind), "The way how to find nested elements should be defined");
        var func = (StepFunction<SearchContext, ? extends SearchContext>) howToFind.clone().timeOut(ofMillis(0)).get();
        func.turnReportingOff();
        return condition(format("has nested %s", howToFind), t -> {
            try {
                return func.apply(t) != null;
            } catch (NoSuchElementException e) {
                return false;
            }
        });
    }

    /**
     * The checking of an element/widget text.
     *
     * @param text is a text (full) that an element should not have
     * @param <T>  is a type of element/widget
     * @return criteria that checks/filters an element/widget
     */
    public <T extends SearchContext> Criteria<T> noText(String text) {
        return NOT(text(text));
    }
}
