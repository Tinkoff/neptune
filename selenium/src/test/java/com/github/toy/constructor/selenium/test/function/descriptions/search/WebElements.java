package com.github.toy.constructor.selenium.test.function.descriptions.search;

import org.openqa.selenium.By;
import org.testng.annotations.Test;

import java.util.regex.Pattern;

import static com.github.toy.constructor.selenium.functions.searching.CommonConditions.shouldBeEnabled;
import static com.github.toy.constructor.selenium.functions.searching.MultipleSearchSupplier.webElements;
import static com.github.toy.constructor.selenium.functions.searching.SearchSupplier.webElement;
import static com.github.toy.constructor.selenium.properties.FlagProperties.FIND_ONLY_VISIBLE_ELEMENTS_WHEN_NO_CONDITION;
import static com.github.toy.constructor.selenium.properties.WaitingProperties.TimeUnitProperties.ELEMENT_WAITING_TIME_UNIT;
import static com.github.toy.constructor.selenium.properties.WaitingProperties.TimeValueProperties.ELEMENT_WAITING_TIME_VALUE;
import static java.time.Duration.ofSeconds;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.openqa.selenium.By.xpath;

/**
 * This test checks descriptions of functions which are built by
 * {@code 'com.github.toy.constructor.selenium.functions.searching.MultipleSearchSupplier.webElement*'} methods
 */
public class WebElements {

    private static final By BY_SOME_PATH = xpath(".//some/path");
    private static final String TEST_TEXT = "Test text";
    private static final Pattern TEST_TEXT_PATTERN = Pattern.compile("^[a-z0-9_-]{3,15}$");

    @Test
    public void descriptionOfASingleElementWithDuration() {
        assertThat(webElement(BY_SOME_PATH, ofSeconds(5), shouldBeEnabled()).toString(),
                is("Web element with condition Should be enabled. Time to get valuable result: 0:00:05:000"));
    }

    @Test
    public void descriptionOfASingleElementWithoutAnyDurationSpecified() {
        assertThat(webElement(BY_SOME_PATH, shouldBeEnabled()).toString(),
                is("Web element with condition Should be enabled. Time to get valuable result: 0:01:00:000"));
    }

    @Test
    public void descriptionOfASingleElementWithDurationSpecifiedInProperties() {
        System.setProperty(ELEMENT_WAITING_TIME_UNIT.getPropertyName(), "MINUTES");
        System.setProperty(ELEMENT_WAITING_TIME_VALUE.getPropertyName(), "3");
        try {
            assertThat(webElement(BY_SOME_PATH, shouldBeEnabled()).toString(),
                    is("Web element with condition Should be enabled. Time to get valuable result: 0:03:00:000"));
        }
        finally {
            System.getProperties().remove(ELEMENT_WAITING_TIME_UNIT.getPropertyName());
            System.getProperties().remove(ELEMENT_WAITING_TIME_VALUE.getPropertyName());
        }
    }

    @Test
    public void descriptionOfASingleElementWithoutAnyConditionSpecified() {
        assertThat(webElement(BY_SOME_PATH, ofSeconds(5)).toString(),
                is("Web element. Time to get valuable result: 0:00:05:000"));
    }

    @Test
    public void descriptionOfASingleElementWithoutAnyConditionAndTimeSpecified() {
        assertThat(webElement(BY_SOME_PATH).toString(),
                is("Web element. Time to get valuable result: 0:01:00:000"));
    }

    @Test
    public void descriptionOfASingleElementWithoutAnyConditionAndTimeSpecifiedInProperties() {
        System.setProperty(ELEMENT_WAITING_TIME_UNIT.getPropertyName(), "MINUTES");
        System.setProperty(ELEMENT_WAITING_TIME_VALUE.getPropertyName(), "3");

        try {
            assertThat(webElement(BY_SOME_PATH).toString(),
                    is("Web element. Time to get valuable result: 0:03:00:000"));
        }
        finally {
            System.getProperties().remove(ELEMENT_WAITING_TIME_UNIT.getPropertyName());
            System.getProperties().remove(ELEMENT_WAITING_TIME_VALUE.getPropertyName());
        }
    }

    @Test
    public void descriptionOfASingleElementWithDefinedConditionAndTimeSpecifiedInProperties() {
        System.setProperty(ELEMENT_WAITING_TIME_UNIT.getPropertyName(), "MINUTES");
        System.setProperty(ELEMENT_WAITING_TIME_VALUE.getPropertyName(), "3");

        try {
            assertThat(webElement(BY_SOME_PATH, shouldBeEnabled()).toString(),
                    is("Web element with condition Should be enabled. Time to get valuable result: 0:03:00:000"));
        }
        finally {
            System.getProperties().remove(ELEMENT_WAITING_TIME_UNIT.getPropertyName());
            System.getProperties().remove(ELEMENT_WAITING_TIME_VALUE.getPropertyName());
        }
    }

    @Test
    public void descriptionOfASingleElementWithVisibilityConditionAndTimeSpecifiedInProperties() {
        System.setProperty(ELEMENT_WAITING_TIME_UNIT.getPropertyName(), "MINUTES");
        System.setProperty(ELEMENT_WAITING_TIME_VALUE.getPropertyName(), "3");
        System.setProperty(FIND_ONLY_VISIBLE_ELEMENTS_WHEN_NO_CONDITION.getPropertyName(), "true");

        try {
            assertThat(webElement(BY_SOME_PATH).toString(),
                    is("Web element with condition Should be visible. Time to get valuable result: 0:03:00:000"));
        }
        finally {
            System.getProperties().remove(ELEMENT_WAITING_TIME_UNIT.getPropertyName());
            System.getProperties().remove(ELEMENT_WAITING_TIME_VALUE.getPropertyName());
            System.getProperties().remove(FIND_ONLY_VISIBLE_ELEMENTS_WHEN_NO_CONDITION.getPropertyName());
        }
    }

    @Test
    public void descriptionOfMultipleElementsWithDuration() {
        assertThat(webElements(BY_SOME_PATH, ofSeconds(5), shouldBeEnabled()).toString(),
                is("Web elements located By.xpath: .//some/path with condition Should be enabled. " +
                        "Time to get valuable result: 0:00:05:000"));
    }

    @Test
    public void descriptionOfMultipleElementsWithoutAnyDurationSpecified() {
        assertThat(webElements(BY_SOME_PATH, shouldBeEnabled()).toString(),
                is("Web elements located By.xpath: .//some/path with condition Should be enabled. " +
                        "Time to get valuable result: 0:01:00:000"));
    }

    @Test
    public void descriptionOfMultipleElementsWithDurationSpecifiedInProperties() {
        System.setProperty(ELEMENT_WAITING_TIME_UNIT.getPropertyName(), "MINUTES");
        System.setProperty(ELEMENT_WAITING_TIME_VALUE.getPropertyName(), "3");
        try {
            assertThat(webElements(BY_SOME_PATH, shouldBeEnabled()).toString(),
                    is("Web elements located By.xpath: .//some/path with condition Should be enabled. " +
                            "Time to get valuable result: 0:03:00:000"));
        }
        finally {
            System.getProperties().remove(ELEMENT_WAITING_TIME_UNIT.getPropertyName());
            System.getProperties().remove(ELEMENT_WAITING_TIME_VALUE.getPropertyName());
        }
    }

    @Test
    public void descriptionOfMultipleElementsWithoutAnyConditionSpecified() {
        assertThat(webElements(BY_SOME_PATH, ofSeconds(5)).toString(),
                is("Web elements located By.xpath: .//some/path. Time to get valuable result: 0:00:05:000"));
    }

    @Test
    public void descriptionOfMultipleElementsWithoutAnyConditionAndTimeSpecified() {
        assertThat(webElements(BY_SOME_PATH).toString(),
                is("Web elements located By.xpath: .//some/path. Time to get valuable result: 0:01:00:000"));
    }

    @Test
    public void descriptionOfMultipleElementsWithoutAnyConditionAndTimeSpecifiedInProperties() {
        System.setProperty(ELEMENT_WAITING_TIME_UNIT.getPropertyName(), "MINUTES");
        System.setProperty(ELEMENT_WAITING_TIME_VALUE.getPropertyName(), "3");

        try {
            assertThat(webElements(BY_SOME_PATH).toString(),
                    is("Web elements located By.xpath: .//some/path. Time to get valuable result: 0:03:00:000"));
        }
        finally {
            System.getProperties().remove(ELEMENT_WAITING_TIME_UNIT.getPropertyName());
            System.getProperties().remove(ELEMENT_WAITING_TIME_VALUE.getPropertyName());
        }
    }

    @Test
    public void descriptionOfMultipleElementsWithDefinedConditionAndTimeSpecifiedInProperties() {
        System.setProperty(ELEMENT_WAITING_TIME_UNIT.getPropertyName(), "MINUTES");
        System.setProperty(ELEMENT_WAITING_TIME_VALUE.getPropertyName(), "3");

        try {
            assertThat(webElements(BY_SOME_PATH, shouldBeEnabled()).toString(),
                    is("Web elements located By.xpath: .//some/path with condition Should be enabled. " +
                            "Time to get valuable result: 0:03:00:000"));
        }
        finally {
            System.getProperties().remove(ELEMENT_WAITING_TIME_UNIT.getPropertyName());
            System.getProperties().remove(ELEMENT_WAITING_TIME_VALUE.getPropertyName());
        }
    }

    @Test
    public void descriptionOfMultipleElementsWithVisibilityConditionAndTimeSpecifiedInProperties() {
        System.setProperty(ELEMENT_WAITING_TIME_UNIT.getPropertyName(), "MINUTES");
        System.setProperty(ELEMENT_WAITING_TIME_VALUE.getPropertyName(), "3");
        System.setProperty(FIND_ONLY_VISIBLE_ELEMENTS_WHEN_NO_CONDITION.getPropertyName(), "true");

        try {
            assertThat(webElements(BY_SOME_PATH).toString(),
                    is("Web elements located By.xpath: .//some/path with condition Should be visible. " +
                            "Time to get valuable result: 0:03:00:000"));
        }
        finally {
            System.getProperties().remove(ELEMENT_WAITING_TIME_UNIT.getPropertyName());
            System.getProperties().remove(ELEMENT_WAITING_TIME_VALUE.getPropertyName());
            System.getProperties().remove(FIND_ONLY_VISIBLE_ELEMENTS_WHEN_NO_CONDITION.getPropertyName());
        }
    }

    @Test
    public void descriptionOfASingleElementWithDurationAndSpecifiedText() {
        assertThat(webElement(BY_SOME_PATH, TEST_TEXT, ofSeconds(5), shouldBeEnabled()).toString(),
                is("Web element with condition (Should have text 'Test text') AND (Should be enabled). " +
                        "Time to get valuable result: 0:00:05:000"));
    }

    @Test
    public void descriptionOfASingleElementWithoutAnyDurationAndSpecifiedText() {
        assertThat(webElement(BY_SOME_PATH, TEST_TEXT, shouldBeEnabled()).toString(),
                is("Web element with condition (Should have text 'Test text') AND (Should be enabled). " +
                        "Time to get valuable result: 0:01:00:000"));
    }

    @Test
    public void descriptionOfASingleElementWithDurationSpecifiedInPropertiesAndSpecifiedText() {
        System.setProperty(ELEMENT_WAITING_TIME_UNIT.getPropertyName(), "MINUTES");
        System.setProperty(ELEMENT_WAITING_TIME_VALUE.getPropertyName(), "3");
        try {
            assertThat(webElement(BY_SOME_PATH, TEST_TEXT, shouldBeEnabled()).toString(),
                    is("Web element with condition (Should have text 'Test text') AND (Should be enabled). " +
                            "Time to get valuable result: 0:03:00:000"));
        }
        finally {
            System.getProperties().remove(ELEMENT_WAITING_TIME_UNIT.getPropertyName());
            System.getProperties().remove(ELEMENT_WAITING_TIME_VALUE.getPropertyName());
        }
    }

    @Test
    public void descriptionOfASingleElementWithoutAnyConditionSpecifiedAndSpecifiedText() {
        assertThat(webElement(BY_SOME_PATH, TEST_TEXT, ofSeconds(5)).toString(),
                is("Web element with condition Should have text 'Test text'. Time to get valuable result: 0:00:05:000"));
    }

    @Test
    public void descriptionOfASingleElementWithoutAnyConditionAndTimeSpecifiedAndSpecifiedText() {
        assertThat(webElement(BY_SOME_PATH, TEST_TEXT).toString(),
                is("Web element with condition Should have text 'Test text'. Time to get valuable result: 0:01:00:000"));
    }

    @Test
    public void descriptionOfASingleElementWithoutAnyConditionAndTimeSpecifiedInPropertiesAndSpecifiedText() {
        System.setProperty(ELEMENT_WAITING_TIME_UNIT.getPropertyName(), "MINUTES");
        System.setProperty(ELEMENT_WAITING_TIME_VALUE.getPropertyName(), "3");

        try {
            assertThat(webElement(BY_SOME_PATH, TEST_TEXT).toString(),
                    is("Web element with condition Should have text 'Test text'. " +
                            "Time to get valuable result: 0:03:00:000"));
        }
        finally {
            System.getProperties().remove(ELEMENT_WAITING_TIME_UNIT.getPropertyName());
            System.getProperties().remove(ELEMENT_WAITING_TIME_VALUE.getPropertyName());
        }
    }

    @Test
    public void descriptionOfASingleElementWithDefinedConditionAndTimeSpecifiedInPropertiesAndSpecifiedText() {
        System.setProperty(ELEMENT_WAITING_TIME_UNIT.getPropertyName(), "MINUTES");
        System.setProperty(ELEMENT_WAITING_TIME_VALUE.getPropertyName(), "3");

        try {
            assertThat(webElement(BY_SOME_PATH, TEST_TEXT, shouldBeEnabled()).toString(),
                    is("Web element with condition (Should have text 'Test text') AND (Should be enabled). " +
                            "Time to get valuable result: 0:03:00:000"));
        }
        finally {
            System.getProperties().remove(ELEMENT_WAITING_TIME_UNIT.getPropertyName());
            System.getProperties().remove(ELEMENT_WAITING_TIME_VALUE.getPropertyName());
        }
    }

    @Test
    public void descriptionOfASingleElementWithVisibilityConditionAndTimeSpecifiedInPropertiesAndSpecifiedText() {
        System.setProperty(ELEMENT_WAITING_TIME_UNIT.getPropertyName(), "MINUTES");
        System.setProperty(ELEMENT_WAITING_TIME_VALUE.getPropertyName(), "3");
        System.setProperty(FIND_ONLY_VISIBLE_ELEMENTS_WHEN_NO_CONDITION.getPropertyName(), "true");

        try {
            assertThat(webElement(BY_SOME_PATH, TEST_TEXT).toString(),
                    is("Web element with condition (Should have text 'Test text') AND (Should be visible). " +
                            "Time to get valuable result: 0:03:00:000"));
        }
        finally {
            System.getProperties().remove(ELEMENT_WAITING_TIME_UNIT.getPropertyName());
            System.getProperties().remove(ELEMENT_WAITING_TIME_VALUE.getPropertyName());
            System.getProperties().remove(FIND_ONLY_VISIBLE_ELEMENTS_WHEN_NO_CONDITION.getPropertyName());
        }
    }

    @Test
    public void descriptionOfMultipleElementsWithDurationAndSpecifiedText() {
        assertThat(webElements(BY_SOME_PATH, TEST_TEXT, ofSeconds(5), shouldBeEnabled()).toString(),
                is("Web elements located By.xpath: .//some/path " +
                        "with condition (Should have text 'Test text') AND (Should be enabled). " +
                        "Time to get valuable result: 0:00:05:000"));
    }

    @Test
    public void descriptionOfMultipleElementsWithoutAnyDurationSpecifiedAndSpecifiedText() {
        assertThat(webElements(BY_SOME_PATH, TEST_TEXT, shouldBeEnabled()).toString(),
                is("Web elements located By.xpath: .//some/path with condition " +
                        "(Should have text 'Test text') AND (Should be enabled). " +
                        "Time to get valuable result: 0:01:00:000"));
    }

    @Test
    public void descriptionOfMultipleElementsWithDurationSpecifiedInPropertiesAndSpecifiedText() {
        System.setProperty(ELEMENT_WAITING_TIME_UNIT.getPropertyName(), "MINUTES");
        System.setProperty(ELEMENT_WAITING_TIME_VALUE.getPropertyName(), "3");
        try {
            assertThat(webElements(BY_SOME_PATH, TEST_TEXT, shouldBeEnabled()).toString(),
                    is("Web elements located By.xpath: .//some/path with condition " +
                            "(Should have text 'Test text') AND (Should be enabled). " +
                            "Time to get valuable result: 0:03:00:000"));
        }
        finally {
            System.getProperties().remove(ELEMENT_WAITING_TIME_UNIT.getPropertyName());
            System.getProperties().remove(ELEMENT_WAITING_TIME_VALUE.getPropertyName());
        }
    }

    @Test
    public void descriptionOfMultipleElementsWithoutAnyConditionSpecifiedAndSpecifiedText() {
        assertThat(webElements(BY_SOME_PATH, TEST_TEXT, ofSeconds(5)).toString(),
                is("Web elements located By.xpath: .//some/path " +
                        "with condition Should have text 'Test text'. Time to get valuable result: 0:00:05:000"));
    }

    @Test
    public void descriptionOfMultipleElementsWithoutAnyConditionAndTimeSpecifiedAndSpecifiedText() {
        assertThat(webElements(BY_SOME_PATH, TEST_TEXT).toString(),
                is("Web elements located By.xpath: .//some/path " +
                        "with condition Should have text 'Test text'. Time to get valuable result: 0:01:00:000"));
    }

    @Test
    public void descriptionOfMultipleElementsWithoutAnyConditionAndTimeSpecifiedInPropertiesAndSpecifiedText() {
        System.setProperty(ELEMENT_WAITING_TIME_UNIT.getPropertyName(), "MINUTES");
        System.setProperty(ELEMENT_WAITING_TIME_VALUE.getPropertyName(), "3");

        try {
            assertThat(webElements(BY_SOME_PATH, TEST_TEXT).toString(),
                    is("Web elements located By.xpath: .//some/path with condition Should have text 'Test text'. " +
                            "Time to get valuable result: 0:03:00:000"));
        }
        finally {
            System.getProperties().remove(ELEMENT_WAITING_TIME_UNIT.getPropertyName());
            System.getProperties().remove(ELEMENT_WAITING_TIME_VALUE.getPropertyName());
        }
    }

    @Test
    public void descriptionOfMultipleElementsWithDefinedConditionAndTimeSpecifiedInPropertiesAndSpecifiedText() {
        System.setProperty(ELEMENT_WAITING_TIME_UNIT.getPropertyName(), "MINUTES");
        System.setProperty(ELEMENT_WAITING_TIME_VALUE.getPropertyName(), "3");

        try {
            assertThat(webElements(BY_SOME_PATH, TEST_TEXT, shouldBeEnabled()).toString(),
                    is("Web elements located By.xpath: .//some/path with condition " +
                            "(Should have text 'Test text') AND (Should be enabled). " +
                            "Time to get valuable result: 0:03:00:000"));
        }
        finally {
            System.getProperties().remove(ELEMENT_WAITING_TIME_UNIT.getPropertyName());
            System.getProperties().remove(ELEMENT_WAITING_TIME_VALUE.getPropertyName());
        }
    }

    @Test
    public void descriptionOfMultipleElementsWithVisibilityConditionAndTimeSpecifiedInPropertiesAndSpecifiedText() {
        System.setProperty(ELEMENT_WAITING_TIME_UNIT.getPropertyName(), "MINUTES");
        System.setProperty(ELEMENT_WAITING_TIME_VALUE.getPropertyName(), "3");
        System.setProperty(FIND_ONLY_VISIBLE_ELEMENTS_WHEN_NO_CONDITION.getPropertyName(), "true");

        try {
            assertThat(webElements(BY_SOME_PATH, TEST_TEXT).toString(),
                    is("Web elements located By.xpath: .//some/path with condition " +
                            "(Should have text 'Test text') AND (Should be visible). " +
                            "Time to get valuable result: 0:03:00:000"));
        }
        finally {
            System.getProperties().remove(ELEMENT_WAITING_TIME_UNIT.getPropertyName());
            System.getProperties().remove(ELEMENT_WAITING_TIME_VALUE.getPropertyName());
            System.getProperties().remove(FIND_ONLY_VISIBLE_ELEMENTS_WHEN_NO_CONDITION.getPropertyName());
        }
    }

    @Test
    public void descriptionOfASingleElementWithDurationAndSpecifiedTextPattern() {
        assertThat(webElement(BY_SOME_PATH, TEST_TEXT_PATTERN, ofSeconds(5), shouldBeEnabled()).toString(),
                is("Web element with condition (Should have text which contains regExp pattern '^[a-z0-9_-]{3,15}$') " +
                        "AND (Should be enabled). Time to get valuable result: 0:00:05:000"));
    }

    @Test
    public void descriptionOfASingleElementWithoutAnyDurationAndSpecifiedTextPattern() {
        assertThat(webElement(BY_SOME_PATH, TEST_TEXT_PATTERN, shouldBeEnabled()).toString(),
                is("Web element " +
                        "with condition (Should have text which contains regExp pattern '^[a-z0-9_-]{3,15}$') " +
                        "AND (Should be enabled). Time to get valuable result: 0:01:00:000"));
    }

    @Test
    public void descriptionOfASingleElementWithDurationSpecifiedInPropertiesAndSpecifiedTextPattern() {
        System.setProperty(ELEMENT_WAITING_TIME_UNIT.getPropertyName(), "MINUTES");
        System.setProperty(ELEMENT_WAITING_TIME_VALUE.getPropertyName(), "3");
        try {
            assertThat(webElement(BY_SOME_PATH, TEST_TEXT_PATTERN, shouldBeEnabled()).toString(),
                    is("Web element with condition (Should have text which contains regExp pattern '^[a-z0-9_-]{3,15}$') " +
                            "AND (Should be enabled). Time to get valuable result: 0:03:00:000"));
        }
        finally {
            System.getProperties().remove(ELEMENT_WAITING_TIME_UNIT.getPropertyName());
            System.getProperties().remove(ELEMENT_WAITING_TIME_VALUE.getPropertyName());
        }
    }

    @Test
    public void descriptionOfASingleElementWithoutAnyConditionSpecifiedAndSpecifiedTextPattern() {
        assertThat(webElement(BY_SOME_PATH, TEST_TEXT_PATTERN, ofSeconds(5)).toString(),
                is("Web element with condition Should have text which contains regExp pattern '^[a-z0-9_-]{3,15}$'. " +
                        "Time to get valuable result: 0:00:05:000"));
    }

    @Test
    public void descriptionOfASingleElementWithoutAnyConditionAndTimeSpecifiedAndSpecifiedTextPattern() {
        assertThat(webElement(BY_SOME_PATH, TEST_TEXT_PATTERN).toString(),
                is("Web element with condition Should have text which contains regExp pattern '^[a-z0-9_-]{3,15}$'. " +
                        "Time to get valuable result: 0:01:00:000"));
    }

    @Test
    public void descriptionOfASingleElementWithoutAnyConditionAndTimeSpecifiedInPropertiesAndSpecifiedTextPattern() {
        System.setProperty(ELEMENT_WAITING_TIME_UNIT.getPropertyName(), "MINUTES");
        System.setProperty(ELEMENT_WAITING_TIME_VALUE.getPropertyName(), "3");

        try {
            assertThat(webElement(BY_SOME_PATH, TEST_TEXT_PATTERN).toString(),
                    is("Web element with condition Should have text which contains regExp pattern '^[a-z0-9_-]{3,15}$'. " +
                            "Time to get valuable result: 0:03:00:000"));
        }
        finally {
            System.getProperties().remove(ELEMENT_WAITING_TIME_UNIT.getPropertyName());
            System.getProperties().remove(ELEMENT_WAITING_TIME_VALUE.getPropertyName());
        }
    }

    @Test
    public void descriptionOfASingleElementWithDefinedConditionAndTimeSpecifiedInPropertiesAndSpecifiedTextPattern() {
        System.setProperty(ELEMENT_WAITING_TIME_UNIT.getPropertyName(), "MINUTES");
        System.setProperty(ELEMENT_WAITING_TIME_VALUE.getPropertyName(), "3");

        try {
            assertThat(webElement(BY_SOME_PATH, TEST_TEXT_PATTERN, shouldBeEnabled()).toString(),
                    is("Web element with condition (Should have text which contains regExp pattern '^[a-z0-9_-]{3,15}$') " +
                            "AND (Should be enabled). Time to get valuable result: 0:03:00:000"));
        }
        finally {
            System.getProperties().remove(ELEMENT_WAITING_TIME_UNIT.getPropertyName());
            System.getProperties().remove(ELEMENT_WAITING_TIME_VALUE.getPropertyName());
        }
    }

    @Test
    public void descriptionOfASingleElementWithVisibilityConditionAndTimeSpecifiedInPropertiesAndSpecifiedTextPattern() {
        System.setProperty(ELEMENT_WAITING_TIME_UNIT.getPropertyName(), "MINUTES");
        System.setProperty(ELEMENT_WAITING_TIME_VALUE.getPropertyName(), "3");
        System.setProperty(FIND_ONLY_VISIBLE_ELEMENTS_WHEN_NO_CONDITION.getPropertyName(), "true");

        try {
            assertThat(webElement(BY_SOME_PATH, TEST_TEXT_PATTERN).toString(),
                    is("Web element with condition (Should have text which contains regExp pattern '^[a-z0-9_-]{3,15}$') " +
                            "AND (Should be visible). Time to get valuable result: 0:03:00:000"));
        }
        finally {
            System.getProperties().remove(ELEMENT_WAITING_TIME_UNIT.getPropertyName());
            System.getProperties().remove(ELEMENT_WAITING_TIME_VALUE.getPropertyName());
            System.getProperties().remove(FIND_ONLY_VISIBLE_ELEMENTS_WHEN_NO_CONDITION.getPropertyName());
        }
    }

    @Test
    public void descriptionOfMultipleElementsWithDurationAndSpecifiedTexPattern() {
        assertThat(webElements(BY_SOME_PATH, TEST_TEXT_PATTERN, ofSeconds(5), shouldBeEnabled()).toString(),
                is("Web elements located By.xpath: .//some/path with condition " +
                        "(Should have text which contains regExp pattern '^[a-z0-9_-]{3,15}$') AND (Should be enabled). " +
                        "Time to get valuable result: 0:00:05:000"));
    }

    @Test
    public void descriptionOfMultipleElementsWithoutAnyDurationSpecifiedAndSpecifiedTextPattern() {
        assertThat(webElements(BY_SOME_PATH, TEST_TEXT_PATTERN, shouldBeEnabled()).toString(),
                is("Web elements located By.xpath: .//some/path with condition " +
                        "(Should have text which contains regExp pattern '^[a-z0-9_-]{3,15}$') AND (Should be enabled). " +
                        "Time to get valuable result: 0:01:00:000"));
    }

    @Test
    public void descriptionOfMultipleElementsWithDurationSpecifiedInPropertiesAndSpecifiedTextPattern() {
        System.setProperty(ELEMENT_WAITING_TIME_UNIT.getPropertyName(), "MINUTES");
        System.setProperty(ELEMENT_WAITING_TIME_VALUE.getPropertyName(), "3");
        try {
            assertThat(
                    webElements(BY_SOME_PATH, TEST_TEXT_PATTERN, shouldBeEnabled()).toString(),
                    is("Web elements located By.xpath: .//some/path with condition " +
                            "(Should have text which contains regExp pattern '^[a-z0-9_-]{3,15}$') " +
                            "AND (Should be enabled). Time to get valuable result: 0:03:00:000"));
        }
        finally {
            System.getProperties().remove(ELEMENT_WAITING_TIME_UNIT.getPropertyName());
            System.getProperties().remove(ELEMENT_WAITING_TIME_VALUE.getPropertyName());
        }
    }

    @Test
    public void descriptionOfMultipleElementsWithoutAnyConditionSpecifiedAndSpecifiedTextPattern() {
        assertThat(webElements(BY_SOME_PATH, TEST_TEXT_PATTERN, ofSeconds(5)).toString(),
                is("Web elements located By.xpath: .//some/path " +
                        "with condition Should have text which contains regExp pattern '^[a-z0-9_-]{3,15}$'. " +
                        "Time to get valuable result: 0:00:05:000"));
    }

    @Test
    public void descriptionOfMultipleElementsWithoutAnyConditionAndTimeSpecifiedAndSpecifiedTextPattern() {
        assertThat(webElements(BY_SOME_PATH, TEST_TEXT_PATTERN).toString(),
                is("Web elements located By.xpath: .//some/path " +
                        "with condition Should have text which contains regExp pattern '^[a-z0-9_-]{3,15}$'. " +
                        "Time to get valuable result: 0:01:00:000"));
    }

    @Test
    public void descriptionOfMultipleElementsWithoutAnyConditionAndTimeSpecifiedInPropertiesAndSpecifiedTextPattern() {
        System.setProperty(ELEMENT_WAITING_TIME_UNIT.getPropertyName(), "MINUTES");
        System.setProperty(ELEMENT_WAITING_TIME_VALUE.getPropertyName(), "3");

        try {
            assertThat(webElements(BY_SOME_PATH, TEST_TEXT_PATTERN).toString(),
                    is("Web elements located By.xpath: .//some/path " +
                            "with condition Should have text which contains regExp pattern '^[a-z0-9_-]{3,15}$'. " +
                            "Time to get valuable result: 0:03:00:000"));
        }
        finally {
            System.getProperties().remove(ELEMENT_WAITING_TIME_UNIT.getPropertyName());
            System.getProperties().remove(ELEMENT_WAITING_TIME_VALUE.getPropertyName());
        }
    }

    @Test
    public void descriptionOfMultipleElementsWithDefinedConditionAndTimeSpecifiedInPropertiesAndSpecifiedTextPattern() {
        System.setProperty(ELEMENT_WAITING_TIME_UNIT.getPropertyName(), "MINUTES");
        System.setProperty(ELEMENT_WAITING_TIME_VALUE.getPropertyName(), "3");

        try {
            assertThat(webElements(BY_SOME_PATH, TEST_TEXT_PATTERN, shouldBeEnabled()).toString(),
                    is("Web elements located By.xpath: .//some/path with condition " +
                            "(Should have text which contains regExp pattern '^[a-z0-9_-]{3,15}$') AND (Should be enabled). " +
                            "Time to get valuable result: 0:03:00:000"));
        }
        finally {
            System.getProperties().remove(ELEMENT_WAITING_TIME_UNIT.getPropertyName());
            System.getProperties().remove(ELEMENT_WAITING_TIME_VALUE.getPropertyName());
        }
    }

    @Test
    public void descriptionOfMultipleElementsWithVisibilityConditionAndTimeSpecifiedInPropertiesAndSpecifiedTextPattern() {
        System.setProperty(ELEMENT_WAITING_TIME_UNIT.getPropertyName(), "MINUTES");
        System.setProperty(ELEMENT_WAITING_TIME_VALUE.getPropertyName(), "3");
        System.setProperty(FIND_ONLY_VISIBLE_ELEMENTS_WHEN_NO_CONDITION.getPropertyName(), "true");

        try {
            assertThat(webElements(BY_SOME_PATH, TEST_TEXT_PATTERN).toString(),
                    is("Web elements located By.xpath: .//some/path with condition " +
                            "(Should have text which contains regExp pattern '^[a-z0-9_-]{3,15}$') " +
                            "AND (Should be visible). Time to get valuable result: 0:03:00:000"));
        }
        finally {
            System.getProperties().remove(ELEMENT_WAITING_TIME_UNIT.getPropertyName());
            System.getProperties().remove(ELEMENT_WAITING_TIME_VALUE.getPropertyName());
            System.getProperties().remove(FIND_ONLY_VISIBLE_ELEMENTS_WHEN_NO_CONDITION.getPropertyName());
        }
    }
}
