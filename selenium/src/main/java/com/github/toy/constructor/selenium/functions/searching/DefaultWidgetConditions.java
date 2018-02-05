package com.github.toy.constructor.selenium.functions.searching;

import com.github.toy.constructor.selenium.api.widget.Labeled;
import com.github.toy.constructor.selenium.api.widget.Widget;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.FluentWait;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.github.toy.constructor.core.api.StoryWriter.condition;
import static com.github.toy.constructor.selenium.PropertySupplier.TimeUnitProperty.ELEMENT_WAITING_TIME_UNIT;
import static com.github.toy.constructor.selenium.PropertySupplier.TimeValueProperty.ELEMENT_WAITING_TIME_VALUE;
import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static java.lang.String.format;
import static java.util.Arrays.asList;
import static java.util.Optional.ofNullable;
import static org.apache.commons.lang3.StringUtils.isBlank;

public final class DefaultWidgetConditions {

    private DefaultWidgetConditions(){
        super();
    }

    /**
     * @param <T> type of the input value
     * @return predicate that checks visibility of a complex element
     */
    public static <T extends Widget> Predicate<T> widgetShouldBeVisible() {
        return condition("Should be visible on the page",
                Widget::isVisible);
    }

    /**
     * @param <T> type of the input value
     * @return predicate that checks is complex element enabled or not
     */
    public static <T extends Widget> Predicate<T> widgetShouldBeEnabled() {
        return condition("Should be enabled",
                Widget::isEnabled);
    }

    /**
     * @param labels which can be used for the seeking element
     * @param <T> type of the input value
     * @return predicate that checks labels of a complex element
     */
    public static  <T extends Widget & Labeled> Predicate<T> widgetShouldBeLabeledBy(String...labels) {
        checkNotNull(labels);
        checkArgument(labels.length > 0, "At least one label should be defined");
        return condition(format("Should have string label(s) %s", Arrays.toString(labels)),
                t -> t.labels().containsAll(asList(labels)));
    }

    /**
     * @param attribute of an element to check
     * @param attrValue desired value of the attribute
     * @param <T> type of the input value
     * @return predicate that checks value of the attribute.
     */
    public static <T extends Widget> Predicate<T> widgetShouldHaveAttribute(String attribute, String attrValue) {
        checkArgument(!isBlank(attribute), "Attribute name should not be empty or null.");
        checkArgument(!isBlank(attrValue), "Attribute value should not be empty or null.");

        return condition(format("Should have attribute '%s=\"%s\"'", attribute, attrValue),
                t -> attrValue.equals(t.getAttribute(attribute)));
    }

    /**
     * @param attribute of an element to check
     * @param attrValue substring that should be contained by value of the attribute
     * @param <T> <T> type of the input value
     * @return predicate that checks value of the attribute.
     */
    public static <T extends Widget> Predicate<T> widgetShouldHaveAttributeContains(String attribute, String attrValue) {
        checkArgument(!isBlank(attribute), "Attribute name should not be empty or null.");
        checkArgument(!isBlank(attrValue), "Attribute value should not be empty or null.");

        return condition(format("Should have attribute '%s' which contains value '%s'", attribute, attrValue),
                t -> ofNullable(t.getAttribute(attribute)).map(s -> s.contains(attrValue)).orElse(false));
    }

    /**
     * @param attribute of an element to check
     * @param pattern is a regex pattern to check the attribute value
     * @param <T> <T> type of the input value
     * @return predicate that checks value of the attribute.
     */
    public static <T extends Widget> Predicate<T> widgetShouldHaveAttributeContains(String attribute, Pattern pattern) {
        checkArgument(!isBlank(attribute), "Attribute name should not be empty or null.");
        checkArgument(pattern != null, "RegEx pattern of the desired " +
                "attribute value should be defined.");

        return condition(format("Should have attribute '%s' which matches " +
                        "regExp pattern '%s'", attribute, pattern),
                t -> ofNullable(t.getAttribute(attribute))
                        .map(s -> {
                            Matcher m = pattern.matcher(s);
                            return m.find();
                        }).orElse(false));
    }

    /**
     * @param locatedBy is a locator strategy to find child elements
     * @param timeUnit used to define the time of the seeking for the element
     * @param time used to define the time of the seeking for the element
     * @param <T> type of the input value
     * @return predicate that checks presence of child elements inside the complex element.
     */
    public static <T extends Widget> Predicate<T> widgetShouldHaveElements(By locatedBy,
                                                                        TimeUnit timeUnit,
                                                                        long time) {
        checkArgument(locatedBy != null, "Locator strategy should be defined");
        checkArgument(timeUnit != null, "Time unit should be defined");
        checkArgument(time >= 0, "Time should have a positive value");

        return condition(format("Should have nested elements which are located by %s. " +
                "Time to find it: %s %s.", locatedBy, timeUnit, time), t -> {
            try {
                new FluentWait<>(t).ignoring(StaleElementReferenceException.class)
                        .withTimeout(time, timeUnit)
                        .until(t1 -> {
                            List<WebElement> result;
                            if ((result = t1.findElements(locatedBy)).size() > 0) {
                                return result;
                            }
                            return null;
                        });
                return true;
            }
            catch (TimeoutException e) {
                return false;
            }
        });
    }

    /**
     * @param locatedBy is a locator strategy to find child elements
     * @param <T> type of the input value
     * @return predicate that checks presence of child elements inside the complex element.
     */
    public static <T extends Widget> Predicate<T> widgetShouldHaveElements(By locatedBy) {
        return widgetShouldHaveElements(locatedBy,
                ELEMENT_WAITING_TIME_UNIT.get(),
                ELEMENT_WAITING_TIME_VALUE.get());
    }
}
