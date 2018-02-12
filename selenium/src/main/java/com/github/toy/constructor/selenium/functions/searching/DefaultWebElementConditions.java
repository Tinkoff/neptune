package com.github.toy.constructor.selenium.functions.searching;

import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.FluentWait;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.github.toy.constructor.core.api.StoryWriter.condition;
import static com.github.toy.constructor.selenium.properties.FlagProperties.FIND_ONLY_VISIBLE_ELEMENTS_WHEN_NO_CONDITION;
import static com.github.toy.constructor.selenium.properties.TimeProperties.ELEMENT_WAITING_TIME_VALUE;
import static com.github.toy.constructor.selenium.properties.TimeUnitProperties.ELEMENT_WAITING_TIME_UNIT;
import static com.google.common.base.Preconditions.checkArgument;
import static java.lang.String.format;
import static java.util.Optional.ofNullable;
import static org.apache.commons.lang3.StringUtils.isBlank;

public final class DefaultWebElementConditions {

    private DefaultWebElementConditions(){
        super();
    }

    /**
     * @return predicate that checks is web element displayed or not
     */
    public static Predicate<WebElement> elementShouldBeDisplayed() {
        return condition("Should be displayed on the page",
                WebElement::isDisplayed);
    }

    /**
     * @return predicate that checks is element enabled or not
     */
    public static Predicate<WebElement> elementShouldBeEnabled() {
        return condition("Should be enabled",
                WebElement::isEnabled);
    }

    /**
     * @param text which element should have
     * @return predicate that checks web element by text
     */
    public static  Predicate<WebElement> elementShouldHaveText(String text) {
        checkArgument(!isBlank(text), "String which is used to check text " +
                "of an element should not be null or empty. ");
        return condition(format("Should have text '%s'", text),
                webElement -> text.equals(webElement.getText()));
    }

    /**
     * @param pattern is a regExp pattern to check text of an element
     * @return predicate that checks web element by text and reg exp pattern
     */
    public static  Predicate<WebElement> elementShouldHaveText(Pattern pattern) {
        checkArgument(pattern != null, "RegEx pattern should be defined");
        return condition(format("Should have text which contains " +
                        "regExp pattern '%s'", pattern),
                webElement -> {
                    Matcher m = pattern.matcher(webElement.getText());
                    return m.find();
                });
    }

    /**
     * @param attribute of an element to check
     * @param attrValue desired value of the attribute
     * @return predicate that checks value of the attribute.
     */
    public static Predicate<WebElement> elementShouldHaveAttribute(String attribute, String attrValue) {
        checkArgument(!isBlank(attribute), "Attribute name should not be empty or null.");
        checkArgument(!isBlank(attrValue), "Attribute value should not be empty or null.");

        return condition(format("Should have attribute '%s=\"%s\"'", attribute, attrValue),
                webElement -> attrValue.equals(webElement.getAttribute(attribute)));
    }

    /**
     * @param attribute of an element to check
     * @param attrValue substring that should be contained by value of the attribute
     * @return predicate that checks value of the attribute.
     */
    public static Predicate<WebElement> elementShouldHaveAttributeContains(String attribute, String attrValue) {
        checkArgument(!isBlank(attribute), "Attribute name should not be empty or null.");
        checkArgument(!isBlank(attrValue), "Attribute value should not be empty or null.");

        return condition(format("Should have attribute '%s' which contains value '%s'", attribute, attrValue),
                webElement -> ofNullable(webElement.getAttribute(attribute))
                        .map(s -> s.contains(attrValue)).orElse(false));
    }

    /**
     * @param attribute of an element to check
     * @param pattern is a regex pattern to check the attribute value
     * @return predicate that checks value of the attribute.
     */
    public static Predicate<WebElement> elementShouldHaveAttributeContains(String attribute, Pattern pattern) {
        checkArgument(!isBlank(attribute), "Attribute name should not be empty or null.");
        checkArgument(pattern != null, "RegEx pattern of the desired " +
                "attribute value should be defined.");

        return condition(format("Should have attribute '%s' which matches " +
                        "regExp pattern '%s'", attribute, pattern),
                webElement -> ofNullable(webElement.getAttribute(attribute))
                        .map(s -> {
                            Matcher m = pattern.matcher(s);
                            return m.find();
                        }).orElse(false));
    }

    /**
     * @param locatedBy is a locator strategy to find child elements
     * @param timeUnit used to define the time of the seeking for the element
     * @param time used to define the time of the seeking for the element
     * @return predicate that checks presence of child elements inside an element.
     */
    public static Predicate<WebElement> widgetShouldHaveElements(By locatedBy,
                                                        TimeUnit timeUnit,
                                                        long time) {
        checkArgument(locatedBy != null, "Locator strategy should be defined");
        checkArgument(timeUnit != null, "Time unit should be defined");
        checkArgument(time >= 0, "Time should have a positive value");

        return condition(format("Should have nested elements which are located by %s. " +
                "Time to find it: %s %s.", locatedBy, timeUnit, time), webElement -> {
            try {
                new FluentWait<>(webElement).ignoring(StaleElementReferenceException.class)
                        .withTimeout(time, timeUnit)
                        .until(element -> {
                            List<WebElement> result;
                            if ((result = element.findElements(locatedBy)).size() > 0) {
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
     * @return predicate that checks presence of child elements inside an element.
     */
    public static Predicate<WebElement> widgetShouldHaveElements(By locatedBy) {
        return widgetShouldHaveElements(locatedBy,
                ELEMENT_WAITING_TIME_UNIT.get(),
                ELEMENT_WAITING_TIME_VALUE.get());
    }

    static Predicate<WebElement> defaultPredicateForElements() {
        if (FIND_ONLY_VISIBLE_ELEMENTS_WHEN_NO_CONDITION.get()) {
            return elementShouldBeDisplayed();
        }
        else {
            return condition("with no other restriction", t -> true);
        }
    }
}
