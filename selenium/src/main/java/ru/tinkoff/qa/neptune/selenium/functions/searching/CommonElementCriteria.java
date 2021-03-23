package ru.tinkoff.qa.neptune.selenium.functions.searching;

import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.internal.WrapsElement;
import ru.tinkoff.qa.neptune.core.api.steps.Criteria;
import ru.tinkoff.qa.neptune.core.api.steps.Description;
import ru.tinkoff.qa.neptune.core.api.steps.DescriptionFragment;
import ru.tinkoff.qa.neptune.core.api.steps.StepFunction;
import ru.tinkoff.qa.neptune.selenium.api.widget.*;
import ru.tinkoff.qa.neptune.selenium.api.widget.drafts.Tab;

import java.lang.reflect.Method;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
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
    @Description("visible")
    public static <T extends SearchContext> Criteria<T> visible() {
        return condition(t -> {
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
    @Description("enabled")
    public static <T extends SearchContext> Criteria<T> enabled() {
        return condition(t -> {
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
    @Description("has text {text}")
    static <T extends SearchContext> Criteria<T> text(@DescriptionFragment("text") String text) {
        checkArgument(isNotBlank(text), "Text should be defined");

        return condition(t -> {
            var clazz = t.getClass();

            if (WebElement.class.isAssignableFrom(clazz)) {
                return Objects.equals(((WebElement) t).getText(), text);
            } else if (HasTextContent.class.isAssignableFrom(clazz)) {
                return Objects.equals(((HasTextContent) t).getText(), text);
            } else if (WrapsElement.class.isAssignableFrom(clazz)) {
                return Objects.equals(ofNullable(((WrapsElement) t).getWrappedElement())
                        .map(WebElement::getText)
                        .orElse(null), text);
            } else {
                return false;
            }
        });
    }

    private static List<String> labelsFromMethods(Method[] methods, Object from) {
        return stream(methods)
                .filter(method -> !isStatic(method.getModifiers())
                        && method.getParameterTypes().length == 0
                        && method.getReturnType().equals(String.class)
                        && method.getAnnotation(Label.class) != null)
                .map(method -> {
                    try {
                        method.setAccessible(true);
                        return (String) method.invoke(from);
                    } catch (Exception e) {
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .collect(toList());
    }

    @Description("has label {label}")
    static <T extends Widget> Criteria<T> labeled(@DescriptionFragment("label") String label) {
        checkNotNull(label);
        return condition(t -> {
            Class<?> cls = t.getClass();
            var labels = new ArrayList<String>();

            while (cls != null) {
                labels.addAll(labelsFromMethods(cls.getDeclaredMethods(), t));
                stream(cls.getInterfaces()).forEach(aClass -> labels.addAll(labelsFromMethods(aClass.getDeclaredMethods(), t)));

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
    @Description("has text that contains '{expression}' or fits regExp pattern '{expression}'")
    public static <T extends SearchContext> Criteria<T> textMatches(@DescriptionFragment("expression") String expression) {
        checkArgument(isNotBlank(expression), "Substring/RegEx pattern should be defined");

        return condition(t -> {
            var clazz = t.getClass();
            String elementText = null;

            if (WebElement.class.isAssignableFrom(clazz)) {
                elementText = valueOf(((WebElement) t).getText());
            } else if (HasTextContent.class.isAssignableFrom(clazz)) {
                elementText = valueOf(((HasTextContent) t).getText());
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
    @Description("has attribute '{attribute}=\"{attrValue}\"'")
    public static <T extends SearchContext> Criteria<T> attr(@DescriptionFragment("attribute") String attribute,
                                                             @DescriptionFragment("attrValue") String attrValue) {
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
    @Description("has attribute '{attribute}' that contains '{expression}' or fits regExp pattern '{expression}'")
    public static <T extends SearchContext> Criteria<T> attrMatches(@DescriptionFragment("attribute") String attribute,
                                                                    @DescriptionFragment("expression") String expression) {
        checkArgument(!isBlank(attribute), "Attribute name should be defined");
        checkArgument(!isBlank(expression), "Substring/RegEx pattern should be defined");

        return condition(t -> {
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
    @Description("has css property '{cssProperty}=\"{cssValue}\"'")
    public static <T extends SearchContext> Criteria<T> css(@DescriptionFragment("cssProperty") String cssProperty,
                                                            @DescriptionFragment("cssValue") String cssValue) {
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
    @Description("has css property '{cssProperty}' that contains '{expression}' or fits regExp pattern '{expression}'")
    public static <T extends SearchContext> Criteria<T> cssMatches(@DescriptionFragment("cssProperty") String cssProperty,
                                                                   @DescriptionFragment("expression") String expression) {
        checkArgument(!isBlank(cssProperty), "Css property should be defined");
        checkArgument(!isBlank(expression), "Substring/RegEx pattern should be defined");

        return condition(format(cssProperty, expression, expression), t -> {
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
    @Description("has nested {howToFind}")
    public static <T extends SearchContext> Criteria<T> nested(@DescriptionFragment("howToFind") MultipleSearchSupplier<?> howToFind) {
        checkArgument(nonNull(howToFind), "The way how to find nested elements should be defined");
        var func = howToFind.clone().timeOut(ofMillis(0)).get();
        ((StepFunction<?, ?>) func).turnReportingOff();
        return condition(t -> func.apply(t).size() > 0);
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
    @Description("has {expected} nested {howToFind}")
    public static <T extends SearchContext> Criteria<T> nested(@DescriptionFragment("howToFind") MultipleSearchSupplier<?> howToFind,
                                                               @DescriptionFragment("expected") int expected) {
        checkArgument(nonNull(howToFind), "The way how to find nested elements should be defined");
        checkArgument(expected >= 0, "Count of expected nested elements can't be a negative or zero value.");
        var func = howToFind.clone().timeOut(ofMillis(0)).get();
        ((StepFunction<?, ?>) func).turnReportingOff();
        return condition(t -> func.apply(t).size() == expected);
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
    @Description("has nested {howToFind}")
    public static <T extends SearchContext> Criteria<T> nested(@DescriptionFragment("howToFind") SearchSupplier<?> howToFind) {
        checkArgument(nonNull(howToFind), "The way how to find nested elements should be defined");
        var func = (StepFunction<SearchContext, ? extends SearchContext>) howToFind.clone().timeOut(ofMillis(0)).get();
        func.turnReportingOff();
        return condition(t -> {
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
    public static <T extends SearchContext> Criteria<T> noText(String text) {
        return NOT(text(text));
    }

    /**
     * The checking of an element/widget by its value
     *
     * @param value is an expected value
     * @param <R>   is a type of the value
     * @param <T>   is a type of element/widget
     * @return criteria that checks/filters an element/widget
     */
    @Description("has value '{value}'")
    public static <R, T extends SearchContext & HasValue<R>> Criteria<T> valueIs(@DescriptionFragment("value") R value) {
        return condition(t -> Objects.equals(value, t.getValue()));
    }

    /**
     * Checks {@link Tab} by its activeness.
     *
     * @param <T> is a type of a {@link Tab}
     * @return criteria that checks/filters an element/widget
     */
    @Description("is active tab")
    public static <T extends Tab> Criteria<T> isActive() {
        return condition(Tab::isActive);
    }
}
