package com.github.toy.constructor.selenium.test.steps.tests.searching;

import com.github.toy.constructor.selenium.test.BaseWebDriverTest;
import com.github.toy.constructor.selenium.test.RetryAnalyzer;
import org.openqa.selenium.By;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.function.Predicate;
import java.util.regex.Pattern;

import static com.github.toy.constructor.selenium.functions.searching.CommonConditions.*;
import static com.github.toy.constructor.selenium.functions.searching.MultipleSearchSupplier.webElements;
import static com.github.toy.constructor.selenium.functions.searching.SearchSupplier.webElement;
import static com.github.toy.constructor.selenium.properties.FlagProperties.FIND_ONLY_VISIBLE_ELEMENTS_WHEN_NO_CONDITION;
import static com.github.toy.constructor.selenium.properties.WaitingProperties.TimeUnitProperties.ELEMENT_WAITING_TIME_UNIT;
import static com.github.toy.constructor.selenium.properties.WaitingProperties.TimeValueProperties.ELEMENT_WAITING_TIME_VALUE;
import static com.github.toy.constructor.selenium.test.FakeDOMModel.*;
import static java.lang.String.format;
import static java.time.Duration.ofMillis;
import static java.util.regex.Pattern.compile;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.openqa.selenium.By.*;

@SuppressWarnings("unchecked")
public class SearchForWebElementsNegativeTest extends BaseWebDriverTest {

    private static final String FOUND_BY_PATTERN = "0 web elements found %s";
    private static final String FOUND_ON_CONDITION = FOUND_BY_PATTERN + " on condition '%s'";
    private static final By CLASS_THAT_DOES_NOT_EXIST = className("fakeClass");

    private static String expectedDescriptionOfTheFoundElements(By by, Predicate<? extends SearchContext> condition) {
        return format(FOUND_ON_CONDITION, by, condition);
    }

    private static String expectedDescriptionOfTheFoundElements(By by) {
        return format(FOUND_BY_PATTERN, by);
    }

    @Test(retryAnalyzer = RetryAnalyzer.class)
    public void findWebElementsFirstLevelWithoutConditionWithDefinedTimeTest() {
        setStartBenchMark();
        List<WebElement> webElements = seleniumSteps.find(webElements(CLASS_THAT_DOES_NOT_EXIST, ONE_SECOND));
        setEndBenchMark();
        assertThat(getTimeDifference(), greaterThanOrEqualTo(ONE_SECOND.toMillis()));
        assertThat(getTimeDifference() - ONE_SECOND.toMillis(), lessThan(HALF_SECOND.toMillis()));
        assertThat(webElements.size(), is(0));
        assertThat(webElements.toString(), is(expectedDescriptionOfTheFoundElements(CLASS_THAT_DOES_NOT_EXIST)));
    }

    @Test(retryAnalyzer = RetryAnalyzer.class)
    public void findWebElementsFirstLevelWithoutConditionWithTimeDefinedImplicitlyTest() {
        setProperty(ELEMENT_WAITING_TIME_UNIT.getPropertyName(), "SECONDS");
        setProperty(ELEMENT_WAITING_TIME_VALUE.getPropertyName(), "1");
        setStartBenchMark();
        try {
            List<WebElement> webElements = seleniumSteps.find(webElements(CLASS_THAT_DOES_NOT_EXIST));
            setEndBenchMark();
            assertThat(getTimeDifference(), greaterThanOrEqualTo(ONE_SECOND.toMillis()));
            assertThat(getTimeDifference() - ONE_SECOND.toMillis(), lessThan(HALF_SECOND.toMillis()));
            assertThat(webElements.size(), is(0));
            assertThat(webElements.toString(), is(expectedDescriptionOfTheFoundElements(CLASS_THAT_DOES_NOT_EXIST)));
        }
        finally {
            removeProperty(ELEMENT_WAITING_TIME_UNIT.getPropertyName());
            removeProperty(ELEMENT_WAITING_TIME_VALUE.getPropertyName());
        }
    }

    @Test(retryAnalyzer = RetryAnalyzer.class)
    public void findWebElementsFirstLevelOnlyVisibleImplicitConditionAndDefinedTimeTest() {
        setProperty(FIND_ONLY_VISIBLE_ELEMENTS_WHEN_NO_CONDITION.getPropertyName(), "true");
        setStartBenchMark();
        try {
            List<WebElement> webElements = seleniumSteps.find(webElements(INVISIBLE_SPAN_BY, ONE_SECOND));
            setEndBenchMark();
            assertThat(getTimeDifference(), greaterThanOrEqualTo(ONE_SECOND.toMillis()));
            assertThat(getTimeDifference() - ONE_SECOND.toMillis(), lessThan(HALF_SECOND.toMillis()));
            assertThat(webElements.size(), is(0));
            assertThat(webElements.toString(), is(expectedDescriptionOfTheFoundElements(INVISIBLE_SPAN_BY,
                    shouldBeVisible())));
        }
        finally {
            removeProperty(FIND_ONLY_VISIBLE_ELEMENTS_WHEN_NO_CONDITION.getPropertyName());
        }
    }

    @Test(retryAnalyzer = RetryAnalyzer.class)
    public void findWebElementsFirstLevelOnlyVisibleImplicitConditionAndImplicitTimeTest() {
        setProperty(FIND_ONLY_VISIBLE_ELEMENTS_WHEN_NO_CONDITION.getPropertyName(), "true");
        setProperty(ELEMENT_WAITING_TIME_UNIT.getPropertyName(), "SECONDS");
        setProperty(ELEMENT_WAITING_TIME_VALUE.getPropertyName(), "1");
        setStartBenchMark();
        try {
            List<WebElement> webElements = seleniumSteps.find(webElements(INVISIBLE_SPAN_BY));
            setEndBenchMark();
            assertThat(getTimeDifference(), greaterThanOrEqualTo(ONE_SECOND.toMillis()));
            assertThat(getTimeDifference() - ONE_SECOND.toMillis(), lessThan(HALF_SECOND.toMillis()));
            assertThat(webElements.size(), is(0));
            assertThat(webElements.toString(), is(expectedDescriptionOfTheFoundElements(INVISIBLE_SPAN_BY,
                    shouldBeVisible())));
        }
        finally {
            removeProperty(FIND_ONLY_VISIBLE_ELEMENTS_WHEN_NO_CONDITION.getPropertyName());
            removeProperty(ELEMENT_WAITING_TIME_UNIT.getPropertyName());
            removeProperty(ELEMENT_WAITING_TIME_VALUE.getPropertyName());
        }
    }

    @Test(retryAnalyzer = RetryAnalyzer.class)
    public void findWebElementsFirstLevelOnlyVisibleImplicitConditionAndTimeConflictTest() {
        setProperty(FIND_ONLY_VISIBLE_ELEMENTS_WHEN_NO_CONDITION.getPropertyName(), "true");
        setProperty(ELEMENT_WAITING_TIME_UNIT.getPropertyName(), "SECONDS");
        setProperty(ELEMENT_WAITING_TIME_VALUE.getPropertyName(), "5");
        setStartBenchMark();
        try {
            List<WebElement> webElements = seleniumSteps.find(webElements(INVISIBLE_SPAN_BY, ONE_SECOND));
            setEndBenchMark();
            assertThat(getTimeDifference(), greaterThanOrEqualTo(ONE_SECOND.toMillis()));
            assertThat(getTimeDifference() - ONE_SECOND.toMillis(), lessThan(HALF_SECOND.toMillis()));
            assertThat(webElements.size(), is(0));
            assertThat(webElements.toString(), is(expectedDescriptionOfTheFoundElements(INVISIBLE_SPAN_BY,
                    shouldBeVisible())));
        }
        finally {
            removeProperty(FIND_ONLY_VISIBLE_ELEMENTS_WHEN_NO_CONDITION.getPropertyName());
            removeProperty(ELEMENT_WAITING_TIME_UNIT.getPropertyName());
            removeProperty(ELEMENT_WAITING_TIME_VALUE.getPropertyName());
        }
    }

    @Test(retryAnalyzer = RetryAnalyzer.class)
    public void findWebElementsChainedWithoutConditionWithDefinedTimeTest() {
        setStartBenchMark();
        List<WebElement> webElements = seleniumSteps.find(webElements(CLASS_THAT_DOES_NOT_EXIST, ONE_SECOND)
                .foundFrom(webElement(tagName(BUTTON_TAG))));
        setEndBenchMark();
        assertThat(getTimeDifference(), greaterThanOrEqualTo(ONE_SECOND.toMillis()));
        assertThat(getTimeDifference() - ONE_SECOND.toMillis(), lessThan(HALF_SECOND.toMillis()));
        assertThat(webElements.size(), is(0));
        assertThat(webElements.toString(), is(expectedDescriptionOfTheFoundElements(CLASS_THAT_DOES_NOT_EXIST)));
    }

    @Test(retryAnalyzer = RetryAnalyzer.class)
    public void findWebElementsChainedWithoutConditionWithTimeDefinedImplicitlyTest() {
        setProperty(ELEMENT_WAITING_TIME_UNIT.getPropertyName(), "SECONDS");
        setProperty(ELEMENT_WAITING_TIME_VALUE.getPropertyName(), "1");
        setStartBenchMark();
        try {
            List<WebElement> webElements = seleniumSteps.find(webElements(CLASS_THAT_DOES_NOT_EXIST)
                    .foundFrom(webElement(tagName(BUTTON_TAG))));
            setEndBenchMark();
            assertThat(getTimeDifference(), greaterThanOrEqualTo(ONE_SECOND.toMillis()));
            assertThat(getTimeDifference() - ONE_SECOND.toMillis(), lessThan(HALF_SECOND.toMillis()));
            assertThat(webElements.size(), is(0));
            assertThat(webElements.toString(), is(expectedDescriptionOfTheFoundElements(CLASS_THAT_DOES_NOT_EXIST)));
        }
        finally {
            removeProperty(ELEMENT_WAITING_TIME_UNIT.getPropertyName());
            removeProperty(ELEMENT_WAITING_TIME_VALUE.getPropertyName());
        }
    }

    @Test(retryAnalyzer = RetryAnalyzer.class)
    public void findWebElementsChainedOnlyVisibleImplicitConditionAndDefinedTimeTest() {
        setProperty(FIND_ONLY_VISIBLE_ELEMENTS_WHEN_NO_CONDITION.getPropertyName(), "true");
        setStartBenchMark();
        try {
            List<WebElement> webElements = seleniumSteps.find(webElements(INVISIBLE_SPAN_BY, ONE_SECOND)
                    .foundFrom(webElement(className(SPREAD_SHEET_CLASS))));
            setEndBenchMark();
            assertThat(getTimeDifference(), greaterThanOrEqualTo(ONE_SECOND.toMillis()));
            assertThat(getTimeDifference() - ONE_SECOND.toMillis(), lessThan(HALF_SECOND.toMillis()));
            assertThat(webElements.size(), is(0));
            assertThat(webElements.toString(), is(expectedDescriptionOfTheFoundElements(INVISIBLE_SPAN_BY,
                    shouldBeVisible())));
        }
        finally {
            removeProperty(FIND_ONLY_VISIBLE_ELEMENTS_WHEN_NO_CONDITION.getPropertyName());
        }
    }

    @Test(retryAnalyzer = RetryAnalyzer.class)
    public void findWebElementsChainedOnlyVisibleImplicitConditionAndImplicitTimeTest() {
        setProperty(FIND_ONLY_VISIBLE_ELEMENTS_WHEN_NO_CONDITION.getPropertyName(), "true");
        setProperty(ELEMENT_WAITING_TIME_UNIT.getPropertyName(), "SECONDS");
        setProperty(ELEMENT_WAITING_TIME_VALUE.getPropertyName(), "1");
        setStartBenchMark();
        try {
            List<WebElement> webElements = seleniumSteps.find(webElements(INVISIBLE_SPAN_BY)
                    .foundFrom(webElement(className(SPREAD_SHEET_CLASS))));
            setEndBenchMark();
            assertThat(getTimeDifference(), greaterThanOrEqualTo(ONE_SECOND.toMillis()));
            assertThat(getTimeDifference() - ONE_SECOND.toMillis(), lessThan(HALF_SECOND.toMillis()));
            assertThat(webElements.size(), is(0));
            assertThat(webElements.toString(), is(expectedDescriptionOfTheFoundElements(INVISIBLE_SPAN_BY,
                    shouldBeVisible())));
        }
        finally {
            removeProperty(FIND_ONLY_VISIBLE_ELEMENTS_WHEN_NO_CONDITION.getPropertyName());
            removeProperty(ELEMENT_WAITING_TIME_UNIT.getPropertyName());
            removeProperty(ELEMENT_WAITING_TIME_VALUE.getPropertyName());
        }
    }

    @Test(retryAnalyzer = RetryAnalyzer.class)
    public void findWebElementsChainedOnlyVisibleImplicitConditionAndTimeConflictTest() {
        setProperty(FIND_ONLY_VISIBLE_ELEMENTS_WHEN_NO_CONDITION.getPropertyName(), "true");
        setProperty(ELEMENT_WAITING_TIME_UNIT.getPropertyName(), "SECONDS");
        setProperty(ELEMENT_WAITING_TIME_VALUE.getPropertyName(), "5");
        setStartBenchMark();
        try {
            List<WebElement> webElements = seleniumSteps.find(webElements(INVISIBLE_SPAN_BY, ONE_SECOND)
                    .foundFrom(webElement(className(SPREAD_SHEET_CLASS))));
            setEndBenchMark();
            assertThat(getTimeDifference(), greaterThanOrEqualTo(ONE_SECOND.toMillis()));
            assertThat(getTimeDifference() - ONE_SECOND.toMillis(), lessThan(HALF_SECOND.toMillis()));
            assertThat(webElements.size(), is(0));
            assertThat(webElements.toString(), is(expectedDescriptionOfTheFoundElements(INVISIBLE_SPAN_BY,
                    shouldBeVisible())));
        }
        finally {
            removeProperty(FIND_ONLY_VISIBLE_ELEMENTS_WHEN_NO_CONDITION.getPropertyName());
            removeProperty(ELEMENT_WAITING_TIME_UNIT.getPropertyName());
            removeProperty(ELEMENT_WAITING_TIME_VALUE.getPropertyName());
        }
    }

    @Test(retryAnalyzer = RetryAnalyzer.class)
    public void findFromWebElementsWithoutConditionWithDefinedTimeTest() {
        WebElement parent = seleniumSteps.find(webElement(tagName(BUTTON_TAG)));
        setStartBenchMark();
        List<WebElement> webElements = seleniumSteps.find(webElements(CLASS_THAT_DOES_NOT_EXIST, ONE_SECOND)
                .foundFrom(parent));
        setEndBenchMark();
        assertThat(getTimeDifference(), greaterThanOrEqualTo(ONE_SECOND.toMillis()));
        assertThat(getTimeDifference() - ONE_SECOND.toMillis(), lessThan(HALF_SECOND.toMillis()));
        assertThat(webElements.size(), is(0));
        assertThat(webElements.toString(), is(expectedDescriptionOfTheFoundElements(CLASS_THAT_DOES_NOT_EXIST)));
    }

    @Test(retryAnalyzer = RetryAnalyzer.class)
    public void findFromWebElementsWithoutConditionWithTimeDefinedImplicitlyTest() {
        WebElement parent = seleniumSteps.find(webElement(tagName(BUTTON_TAG)));
        setProperty(ELEMENT_WAITING_TIME_UNIT.getPropertyName(), "SECONDS");
        setProperty(ELEMENT_WAITING_TIME_VALUE.getPropertyName(), "1");
        setStartBenchMark();
        try {
            List<WebElement> webElements = seleniumSteps.find(webElements(CLASS_THAT_DOES_NOT_EXIST)
                    .foundFrom(parent));
            setEndBenchMark();
            assertThat(getTimeDifference(), greaterThanOrEqualTo(ONE_SECOND.toMillis()));
            assertThat(getTimeDifference() - ONE_SECOND.toMillis(), lessThan(HALF_SECOND.toMillis()));
            assertThat(webElements.size(), is(0));
            assertThat(webElements.toString(), is(expectedDescriptionOfTheFoundElements(CLASS_THAT_DOES_NOT_EXIST)));
        }
        finally {
            removeProperty(ELEMENT_WAITING_TIME_UNIT.getPropertyName());
            removeProperty(ELEMENT_WAITING_TIME_VALUE.getPropertyName());
        }
    }

    @Test(retryAnalyzer = RetryAnalyzer.class)
    public void findFromWebElementsOnlyVisibleImplicitConditionAndDefinedTimeTest() {
        setProperty(FIND_ONLY_VISIBLE_ELEMENTS_WHEN_NO_CONDITION.getPropertyName(), "true");
        WebElement parent = seleniumSteps.find(webElement(className(SPREAD_SHEET_CLASS)));
        setStartBenchMark();
        try {
            List<WebElement> webElements = seleniumSteps.find(webElements(INVISIBLE_SPAN_BY, ONE_SECOND)
                    .foundFrom(parent));
            setEndBenchMark();
            assertThat(getTimeDifference(), greaterThanOrEqualTo(ONE_SECOND.toMillis()));
            assertThat(getTimeDifference() - ONE_SECOND.toMillis(), lessThan(HALF_SECOND.toMillis()));
            assertThat(webElements.size(), is(0));
            assertThat(webElements.toString(), is(expectedDescriptionOfTheFoundElements(INVISIBLE_SPAN_BY,
                    shouldBeVisible())));
        }
        finally {
            removeProperty(FIND_ONLY_VISIBLE_ELEMENTS_WHEN_NO_CONDITION.getPropertyName());
        }
    }

    @Test(retryAnalyzer = RetryAnalyzer.class)
    public void findFromWebElementsOnlyVisibleImplicitConditionAndImplicitTimeTest() {
        setProperty(FIND_ONLY_VISIBLE_ELEMENTS_WHEN_NO_CONDITION.getPropertyName(), "true");
        setProperty(ELEMENT_WAITING_TIME_UNIT.getPropertyName(), "SECONDS");
        setProperty(ELEMENT_WAITING_TIME_VALUE.getPropertyName(), "1");
        WebElement parent = seleniumSteps.find(webElement(className(SPREAD_SHEET_CLASS)));
        setStartBenchMark();
        try {
            List<WebElement> webElements = seleniumSteps.find(webElements(INVISIBLE_SPAN_BY)
                    .foundFrom(parent));
            setEndBenchMark();
            assertThat(getTimeDifference(), greaterThanOrEqualTo(ONE_SECOND.toMillis()));
            assertThat(getTimeDifference() - ONE_SECOND.toMillis(), lessThan(HALF_SECOND.toMillis()));
            assertThat(webElements.size(), is(0));
            assertThat(webElements.toString(), is(expectedDescriptionOfTheFoundElements(INVISIBLE_SPAN_BY,
                    shouldBeVisible())));
        }
        finally {
            removeProperty(FIND_ONLY_VISIBLE_ELEMENTS_WHEN_NO_CONDITION.getPropertyName());
            removeProperty(ELEMENT_WAITING_TIME_UNIT.getPropertyName());
            removeProperty(ELEMENT_WAITING_TIME_VALUE.getPropertyName());
        }
    }

    @Test(retryAnalyzer = RetryAnalyzer.class)
    public void findFromWebElementsOnlyVisibleImplicitConditionAndTimeConflictTest() {
        setProperty(FIND_ONLY_VISIBLE_ELEMENTS_WHEN_NO_CONDITION.getPropertyName(), "true");
        setProperty(ELEMENT_WAITING_TIME_UNIT.getPropertyName(), "SECONDS");
        setProperty(ELEMENT_WAITING_TIME_VALUE.getPropertyName(), "5");
        WebElement parent = seleniumSteps.find(webElement(className(SPREAD_SHEET_CLASS)));
        setStartBenchMark();
        try {
            List<WebElement> webElements = seleniumSteps.find(webElements(INVISIBLE_SPAN_BY, ONE_SECOND)
                    .foundFrom(parent));
            setEndBenchMark();
            assertThat(getTimeDifference(), greaterThanOrEqualTo(ONE_SECOND.toMillis()));
            assertThat(getTimeDifference() - ONE_SECOND.toMillis(), lessThan(HALF_SECOND.toMillis()));
            assertThat(webElements.size(), is(0));
            assertThat(webElements.toString(), is(expectedDescriptionOfTheFoundElements(INVISIBLE_SPAN_BY,
                    shouldBeVisible())));
        }
        finally {
            removeProperty(FIND_ONLY_VISIBLE_ELEMENTS_WHEN_NO_CONDITION.getPropertyName());
            removeProperty(ELEMENT_WAITING_TIME_UNIT.getPropertyName());
            removeProperty(ELEMENT_WAITING_TIME_VALUE.getPropertyName());
        }
    }


    @DataProvider(name = "search criteria1")
    public static Object[][] searchCriteria() {
        return new Object[][]{
                {INVISIBLE_SPAN_BY, shouldBeEnabled(),
                        expectedDescriptionOfTheFoundElements(INVISIBLE_SPAN_BY, shouldBeEnabled())},

                {VISIBLE_DIV_BY, shouldBeEnabled().negate(),
                        expectedDescriptionOfTheFoundElements(VISIBLE_DIV_BY, shouldBeEnabled().negate())},

                {INVISIBLE_SPAN_BY, shouldBeVisible(),
                        expectedDescriptionOfTheFoundElements(INVISIBLE_SPAN_BY, shouldBeVisible())},

                {VISIBLE_DIV_BY, shouldBeVisible().negate(),
                        expectedDescriptionOfTheFoundElements(VISIBLE_DIV_BY,  shouldBeVisible().negate())},

                {className(MULTI_SELECT_CLASS), shouldContainElements(webElements(className(ITEM_OPTION_CLASS), ofMillis(10)), 4),
                        expectedDescriptionOfTheFoundElements(className(MULTI_SELECT_CLASS),
                                shouldContainElements(webElements(className(ITEM_OPTION_CLASS), ofMillis(10)), 4))},

                {className(MULTI_SELECT_CLASS),
                        shouldContainElements(webElements(className(ITEM_OPTION_CLASS), ofMillis(10)), 3)
                                .or(shouldContainElements(webElements(className(ITEM_OPTION_CLASS), ofMillis(10)), 2))
                                .negate(),
                        expectedDescriptionOfTheFoundElements(className(MULTI_SELECT_CLASS),
                                shouldContainElements(webElements(className(ITEM_OPTION_CLASS), ofMillis(10)), 3)
                                        .or(shouldContainElements(webElements(className(ITEM_OPTION_CLASS), ofMillis(10)), 2))
                                        .negate())},

                {className(MULTI_SELECT_CLASS),
                        shouldContainElements(webElements(tagName(OPTION), ofMillis(10))),
                        expectedDescriptionOfTheFoundElements(className(MULTI_SELECT_CLASS),
                                shouldContainElements(webElements(tagName(OPTION), ofMillis(10))))},

                {tagName(SELECT), shouldContainElements(webElements(tagName(OPTION), ofMillis(10))).negate(),
                        expectedDescriptionOfTheFoundElements(tagName(SELECT),
                                shouldContainElements(webElements(tagName(OPTION), ofMillis(10))).negate())},

                {tagName(TEXT_AREA_TAG), shouldHaveAttribute(ATTR11, VALUE10),
                        expectedDescriptionOfTheFoundElements(tagName(TEXT_AREA_TAG),
                                shouldHaveAttribute(ATTR11, VALUE10))},

                {tagName(TEXT_AREA_TAG), shouldHaveAttribute(ATTR11, VALUE12)
                        .or(shouldHaveAttribute(ATTR11, VALUE13))
                        .or(shouldHaveAttribute(ATTR11, VALUE14))
                        .or(shouldHaveAttribute(ATTR11, VALUE15)).negate(),
                        expectedDescriptionOfTheFoundElements(tagName(TEXT_AREA_TAG),
                                shouldHaveAttribute(ATTR11, VALUE12)
                                        .or(shouldHaveAttribute(ATTR11, VALUE13))
                                        .or(shouldHaveAttribute(ATTR11, VALUE14))
                                        .or(shouldHaveAttribute(ATTR11, VALUE15)).negate())},

                {tagName(TEXT_AREA_TAG), shouldHaveAttributeContains(ATTR11, "10"),
                        expectedDescriptionOfTheFoundElements(tagName(TEXT_AREA_TAG),
                                shouldHaveAttributeContains(ATTR11, "10"))},

                {tagName(TEXT_AREA_TAG), shouldHaveAttributeContains(ATTR11, "12")
                        .or(shouldHaveAttributeContains(ATTR11, "13"))
                        .or(shouldHaveAttributeContains(ATTR11, VALUE14))
                        .or(shouldHaveAttributeContains(ATTR11, VALUE15)).negate(),
                        expectedDescriptionOfTheFoundElements(tagName(TEXT_AREA_TAG),
                                shouldHaveAttributeContains(ATTR11, "12")
                                        .or(shouldHaveAttributeContains(ATTR11, "13"))
                                        .or(shouldHaveAttributeContains(ATTR11, VALUE14))
                                        .or(shouldHaveAttributeContains(ATTR11, VALUE15)).negate())},

                {tagName(TEXT_AREA_TAG), shouldHaveAttributeContains(ATTR11, compile("10")),
                        expectedDescriptionOfTheFoundElements(tagName(TEXT_AREA_TAG),
                                shouldHaveAttributeContains(ATTR11, compile("10")))},

                {tagName(TEXT_AREA_TAG), shouldHaveAttributeContains(ATTR11, compile("12"))
                        .or(shouldHaveAttributeContains(ATTR11, compile("13")))
                        .or(shouldHaveAttributeContains(ATTR11, compile(VALUE14)))
                        .or(shouldHaveAttributeContains(ATTR11, compile(VALUE15))).negate(),
                        expectedDescriptionOfTheFoundElements(tagName(TEXT_AREA_TAG),
                                shouldHaveAttributeContains(ATTR11, compile("12"))
                                        .or(shouldHaveAttributeContains(ATTR11, compile("13")))
                                        .or(shouldHaveAttributeContains(ATTR11, compile(VALUE14)))
                                        .or(shouldHaveAttributeContains(ATTR11, compile(VALUE15))).negate())},

                {xpath(RADIO_BUTTON_XPATH), shouldHaveCssValue(CSS18, CSS_VALUE6),
                        expectedDescriptionOfTheFoundElements(xpath(RADIO_BUTTON_XPATH),
                                shouldHaveCssValue(CSS18, CSS_VALUE6))},

                {xpath(RADIO_BUTTON_XPATH),
                        shouldHaveCssValue(CSS18, CSS_VALUE7)
                                .or(shouldHaveCssValue(CSS18, CSS_VALUE8))
                                .or(shouldHaveCssValue(CSS18, CSS_VALUE9))
                                .or(shouldHaveCssValue(CSS18, CSS_VALUE10))
                                .or(shouldHaveCssValue(CSS18, CSS_VALUE11))
                                .or(shouldHaveCssValue(CSS18, CSS_VALUE12))
                                .or(shouldHaveCssValue(CSS18, CSS_VALUE13))
                                .or(shouldHaveCssValue(CSS18, CSS_VALUE14))
                                .or(shouldHaveCssValue(CSS18, CSS_VALUE15))
                                .or(shouldHaveCssValue(CSS18, CSS_VALUE16))
                                .or(shouldHaveCssValue(CSS18, CSS_VALUE17))
                                .or(shouldHaveCssValue(CSS18, CSS_VALUE18))
                                .or(shouldHaveCssValue(CSS18, CSS_VALUE19)).negate(),
                        expectedDescriptionOfTheFoundElements(xpath(RADIO_BUTTON_XPATH),
                                shouldHaveCssValue(CSS18, CSS_VALUE7)
                                        .or(shouldHaveCssValue(CSS18, CSS_VALUE8))
                                        .or(shouldHaveCssValue(CSS18, CSS_VALUE9))
                                        .or(shouldHaveCssValue(CSS18, CSS_VALUE10))
                                        .or(shouldHaveCssValue(CSS18, CSS_VALUE11))
                                        .or(shouldHaveCssValue(CSS18, CSS_VALUE12))
                                        .or(shouldHaveCssValue(CSS18, CSS_VALUE13))
                                        .or(shouldHaveCssValue(CSS18, CSS_VALUE14))
                                        .or(shouldHaveCssValue(CSS18, CSS_VALUE15))
                                        .or(shouldHaveCssValue(CSS18, CSS_VALUE16))
                                        .or(shouldHaveCssValue(CSS18, CSS_VALUE17))
                                        .or(shouldHaveCssValue(CSS18, CSS_VALUE18))
                                        .or(shouldHaveCssValue(CSS18, CSS_VALUE19)).negate())},

                {xpath(RADIO_BUTTON_XPATH), shouldHaveCssValueContains(CSS18, "value6"),
                        expectedDescriptionOfTheFoundElements(xpath(RADIO_BUTTON_XPATH),
                                shouldHaveCssValueContains(CSS18, "value6"))},

                {xpath(RADIO_BUTTON_XPATH),
                        shouldHaveCssValueContains(CSS18, "value").negate(),
                        expectedDescriptionOfTheFoundElements(xpath(RADIO_BUTTON_XPATH),
                                shouldHaveCssValueContains(CSS18, "value").negate())},

                {xpath(RADIO_BUTTON_XPATH), shouldHaveCssValueContains(CSS18, compile("value6")),
                        expectedDescriptionOfTheFoundElements(xpath(RADIO_BUTTON_XPATH),
                                shouldHaveCssValueContains(CSS18, compile("value6")))},

                {xpath(RADIO_BUTTON_XPATH),
                        shouldHaveCssValueContains(CSS18, compile("value")).negate(),
                        expectedDescriptionOfTheFoundElements(xpath(RADIO_BUTTON_XPATH),
                                shouldHaveCssValueContains(CSS18, compile("value")).negate())},

                {INVISIBLE_SPAN_BY, shouldHaveText(VISIBLE_DIV),
                        expectedDescriptionOfTheFoundElements(INVISIBLE_SPAN_BY,
                                shouldHaveText(VISIBLE_DIV))},

                {VISIBLE_DIV_BY, shouldHaveText(VISIBLE_DIV).negate(),
                        expectedDescriptionOfTheFoundElements(VISIBLE_DIV_BY,
                                shouldHaveText(VISIBLE_DIV).negate())},

                {INVISIBLE_SPAN_BY, shouldHaveText(compile("div")),
                        expectedDescriptionOfTheFoundElements(INVISIBLE_SPAN_BY,
                                shouldHaveText(compile("div")))},

                {VISIBLE_DIV_BY, shouldHaveText(compile("div")).negate(),
                        expectedDescriptionOfTheFoundElements(VISIBLE_DIV_BY,
                                shouldHaveText(compile("div")).negate())}
        };
    }

    @Test(dataProvider = "search criteria1", retryAnalyzer = RetryAnalyzer.class)
    public void findElementsByCriteriaWithDefinedTimeTest(By by,
                                                         Predicate<? extends SearchContext> criteria,
                                                         String expectedListDescription) {
        setStartBenchMark();
        List<WebElement> webElements = seleniumSteps.find(webElements(by, ONE_SECOND, (Predicate<? super WebElement>) criteria));
        setEndBenchMark();
        assertThat(getTimeDifference(), greaterThanOrEqualTo(ONE_SECOND.toMillis()));
        assertThat(new BigDecimal(getTimeDifference() - ONE_SECOND.toMillis()),
                either(lessThan(new BigDecimal(HALF_SECOND.toMillis())))
                        .or(closeTo(new BigDecimal(HALF_SECOND.toMillis()), new BigDecimal(200))));
        assertThat(webElements.size(), is(0));
        assertThat(webElements.toString(), is(expectedListDescription));
    }

    @Test(dataProvider = "search criteria1", retryAnalyzer = RetryAnalyzer.class)
    public void findElementsByCriteriaWithTimeDefinedImplicitlyTest(By by,
                                                                   Predicate<? extends SearchContext> criteria,
                                                                   String expectedListDescription) {
        setProperty(ELEMENT_WAITING_TIME_UNIT.getPropertyName(), "SECONDS");
        setProperty(ELEMENT_WAITING_TIME_VALUE.getPropertyName(), "1");
        setStartBenchMark();
        try {
            List<WebElement> webElements = seleniumSteps.find(webElements(by, (Predicate<? super WebElement>) criteria));
            setEndBenchMark();
            assertThat(getTimeDifference(), greaterThanOrEqualTo(ONE_SECOND.toMillis()));
            assertThat(new BigDecimal(getTimeDifference() - ONE_SECOND.toMillis()),
                    either(lessThan(new BigDecimal(HALF_SECOND.toMillis())))
                            .or(closeTo(new BigDecimal(HALF_SECOND.toMillis()), new BigDecimal(200))));
            assertThat(webElements.size(), is(0));
            assertThat(webElements.toString(), is(expectedListDescription));
        }
        finally {
            removeProperty(ELEMENT_WAITING_TIME_UNIT.getPropertyName());
            removeProperty(ELEMENT_WAITING_TIME_VALUE.getPropertyName());
        }
    }

    @DataProvider(name = "search criteria2")
    public static Object[][] searchCriteriaForText() {
        return new Object[][]{
                {tagName(BUTTON_TAG), BUTTON_TEXT1, shouldBeEnabled(),
                        expectedDescriptionOfTheFoundElements(tagName(BUTTON_TAG),
                                shouldHaveText(BUTTON_TEXT1).and(shouldBeEnabled()))},

                {tagName(BUTTON_TAG), BUTTON_TEXT3, shouldBeEnabled().negate(),
                        expectedDescriptionOfTheFoundElements(tagName(BUTTON_TAG),
                                shouldHaveText(BUTTON_TEXT3).and(shouldBeEnabled().negate()))},

                {tagName(LINK_TAG), LINK_TEXT1, shouldBeVisible(),
                        expectedDescriptionOfTheFoundElements(tagName(LINK_TAG),
                                shouldHaveText(LINK_TEXT1).and(shouldBeVisible()))},

                {tagName(LINK_TAG), LINK_TEXT2,  shouldBeVisible().negate(),
                        expectedDescriptionOfTheFoundElements(tagName(LINK_TAG),
                                shouldHaveText(LINK_TEXT2).and(shouldBeVisible().negate()))},

                {tagName(SELECT), OPTION_TEXT23,
                        shouldContainElements(webElements(tagName(OPTION), OPTION_TEXT22, ofMillis(10)), 3),
                        expectedDescriptionOfTheFoundElements(tagName(SELECT), shouldHaveText(OPTION_TEXT23)
                                .and(shouldContainElements(webElements(tagName(OPTION), OPTION_TEXT22, ofMillis(10)), 3)))},

                {tagName(SELECT), OPTION_TEXT20,
                        shouldContainElements(webElements(tagName(OPTION), ofMillis(10)), 3).negate(),
                        expectedDescriptionOfTheFoundElements(tagName(SELECT),
                                shouldHaveText(OPTION_TEXT20)
                                        .and(shouldContainElements(webElements(tagName(OPTION), ofMillis(10)), 3).negate()))},

                {tagName(BUTTON_TAG), BUTTON_TEXT4,
                        shouldContainElements(webElements(tagName(LABEL_TAG), BUTTON_LABEL_TEXT1, ofMillis(10))),
                        expectedDescriptionOfTheFoundElements(tagName(BUTTON_TAG),
                                shouldHaveText(BUTTON_TEXT4)
                                        .and(shouldContainElements(webElements(tagName(LABEL_TAG), BUTTON_LABEL_TEXT1, ofMillis(10)))))},

                {tagName(BUTTON_TAG), BUTTON_TEXT5,
                        shouldContainElements(webElements(tagName(LABEL_TAG), BUTTON_LABEL_TEXT1, ofMillis(10))).negate(),
                        expectedDescriptionOfTheFoundElements(tagName(BUTTON_TAG),
                                shouldHaveText(BUTTON_TEXT5)
                                        .and(shouldContainElements(webElements(tagName(LABEL_TAG),
                                                BUTTON_LABEL_TEXT1, ofMillis(10))).negate()))},

                {CHAINED_FIND_TAB, TAB_TEXT3, shouldHaveAttribute(ATTR19, VALUE12),
                        expectedDescriptionOfTheFoundElements(CHAINED_FIND_TAB,
                                shouldHaveText(TAB_TEXT3)
                                        .and(shouldHaveAttribute(ATTR19, VALUE12)))},

                {CHAINED_FIND_TAB, TAB_TEXT2, shouldHaveAttribute(ATTR19, VALUE12).negate(),
                        expectedDescriptionOfTheFoundElements(CHAINED_FIND_TAB,
                                shouldHaveText(TAB_TEXT2)
                                        .and(shouldHaveAttribute(ATTR19, VALUE12).negate()))},

                {CHAINED_FIND_TAB, TAB_TEXT4, shouldHaveAttributeContains(ATTR20, VALUE14),
                        expectedDescriptionOfTheFoundElements(CHAINED_FIND_TAB,
                                shouldHaveText(TAB_TEXT4).and(shouldHaveAttributeContains(ATTR20, VALUE14)))},

                {CHAINED_FIND_TAB, TAB_TEXT3, shouldHaveAttributeContains(ATTR20, VALUE14).negate(),
                        expectedDescriptionOfTheFoundElements(CHAINED_FIND_TAB,
                                shouldHaveText(TAB_TEXT3)
                                        .and(shouldHaveAttributeContains(ATTR20, VALUE14).negate()))},

                {CHAINED_FIND_TAB, TAB_TEXT3, shouldHaveAttributeContains(ATTR20, compile(VALUE12)),
                        expectedDescriptionOfTheFoundElements(CHAINED_FIND_TAB,
                                shouldHaveText(TAB_TEXT3)
                                        .and(shouldHaveAttributeContains(ATTR20, compile(VALUE12))))},

                {CHAINED_FIND_TAB, TAB_TEXT1, shouldHaveAttributeContains(ATTR20, compile(VALUE12)).negate(),
                        expectedDescriptionOfTheFoundElements(CHAINED_FIND_TAB,
                                shouldHaveText(TAB_TEXT1)
                                        .and(shouldHaveAttributeContains(ATTR20, compile(VALUE12)).negate()))},

                {xpath(TEXT_FIELD_XPATH), INPUT_TEXT4, shouldHaveCssValue(CSS8, CSS_VALUE6),
                        expectedDescriptionOfTheFoundElements(xpath(TEXT_FIELD_XPATH),
                                shouldHaveText(INPUT_TEXT4)
                                        .and(shouldHaveCssValue(CSS8, CSS_VALUE6)))},

                {xpath(TEXT_FIELD_XPATH), INPUT_TEXT3, shouldHaveCssValue(CSS8, CSS_VALUE6).negate(),
                        expectedDescriptionOfTheFoundElements(xpath(TEXT_FIELD_XPATH),
                                shouldHaveText(INPUT_TEXT3)
                                        .and(shouldHaveCssValue(CSS8, CSS_VALUE6).negate()))},

                {xpath(TEXT_FIELD_XPATH), INPUT_TEXT4, shouldHaveCssValueContains(CSS8, "4")
                        .and(shouldHaveCssValueContains(CSS9, "5")),
                        expectedDescriptionOfTheFoundElements(xpath(TEXT_FIELD_XPATH),
                                shouldHaveText(INPUT_TEXT4).and(shouldHaveCssValueContains(CSS8, "4")
                                        .and(shouldHaveCssValueContains(CSS9, "5"))))},

                {xpath(TEXT_FIELD_XPATH), INPUT_TEXT1, shouldHaveCssValueContains(CSS8, "4")
                        .and(shouldHaveCssValueContains(CSS9, "5")).negate(),
                        expectedDescriptionOfTheFoundElements(xpath(TEXT_FIELD_XPATH),
                                shouldHaveText(INPUT_TEXT1).and(shouldHaveCssValueContains(CSS8, "4")
                                        .and(shouldHaveCssValueContains(CSS9, "5")).negate()))},

                {xpath(TEXT_FIELD_XPATH), INPUT_TEXT4, shouldHaveCssValueContains(CSS8, compile("4"))
                        .and(shouldHaveCssValueContains(CSS9, compile("5"))),
                        expectedDescriptionOfTheFoundElements(xpath(TEXT_FIELD_XPATH),
                                shouldHaveText(INPUT_TEXT4).and(shouldHaveCssValueContains(CSS8, compile("4"))
                                        .and(shouldHaveCssValueContains(CSS9, compile("5")))))},

                {xpath(TEXT_FIELD_XPATH), INPUT_TEXT1, shouldHaveCssValueContains(CSS8, compile("4"))
                        .and(shouldHaveCssValueContains(CSS9, compile("5"))).negate(),
                        expectedDescriptionOfTheFoundElements(xpath(TEXT_FIELD_XPATH),
                                shouldHaveText(INPUT_TEXT1).and(shouldHaveCssValueContains(CSS8, compile("4"))
                                        .and(shouldHaveCssValueContains(CSS9, compile("5"))).negate()))},
        };
    }

    @Test(dataProvider = "search criteria2", retryAnalyzer = RetryAnalyzer.class)
    public void findElementsByCriteriaAndTextWithDefinedTimeTest(By by,
                                                                 String text,
                                                                 Predicate<? extends SearchContext> criteria,
                                                                 String expectedListDescription) {
        setStartBenchMark();
        List<WebElement> webElements = seleniumSteps.find(webElements(by, text, ONE_SECOND, (Predicate<? super WebElement>) criteria));
        setEndBenchMark();
        assertThat(getTimeDifference(), greaterThanOrEqualTo(ONE_SECOND.toMillis()));
        assertThat(new BigDecimal(getTimeDifference() - ONE_SECOND.toMillis()),
                either(lessThan(new BigDecimal(HALF_SECOND.toMillis())))
                        .or(closeTo(new BigDecimal(HALF_SECOND.toMillis()), new BigDecimal(200))));
        assertThat(webElements.size(), is(0));
        assertThat(webElements.toString(), is(expectedListDescription));
    }

    @Test(dataProvider = "search criteria2", retryAnalyzer = RetryAnalyzer.class)
    public void findElementsByCriteriaAndTextWithTimeDefinedImplicitlyTest(By by,
                                                                          String text,
                                                                          Predicate<? extends SearchContext> criteria,
                                                                          String expectedListDescription) {
        setProperty(ELEMENT_WAITING_TIME_UNIT.getPropertyName(), "SECONDS");
        setProperty(ELEMENT_WAITING_TIME_VALUE.getPropertyName(), "1");
        setStartBenchMark();
        try {
            List<WebElement> webElements = seleniumSteps.find(webElements(by, text, (Predicate<? super WebElement>) criteria));
            setEndBenchMark();
            assertThat(getTimeDifference(), greaterThanOrEqualTo(ONE_SECOND.toMillis()));
            assertThat(new BigDecimal(getTimeDifference() - ONE_SECOND.toMillis()),
                    either(lessThan(new BigDecimal(HALF_SECOND.toMillis())))
                            .or(closeTo(new BigDecimal(HALF_SECOND.toMillis()), new BigDecimal(200))));
            assertThat(webElements.size(), is(0));
            assertThat(webElements.toString(), is(expectedListDescription));
        }
        finally {
            removeProperty(ELEMENT_WAITING_TIME_UNIT.getPropertyName());
            removeProperty(ELEMENT_WAITING_TIME_VALUE.getPropertyName());
        }
    }

    @DataProvider(name = "search criteria3")
    public static Object[][] searchCriteriaForTextPattern() {
        return new Object[][]{
                {tagName(BUTTON_TAG), compile("Text1"), shouldBeEnabled(),
                        expectedDescriptionOfTheFoundElements(tagName(BUTTON_TAG),
                                shouldHaveText(compile("Text1")).and(shouldBeEnabled()))},

                {tagName(BUTTON_TAG), compile("Text3"), shouldBeEnabled().negate(),
                        expectedDescriptionOfTheFoundElements(tagName(BUTTON_TAG),
                                shouldHaveText(compile("Text3")).and(shouldBeEnabled().negate()))},

                {tagName(LINK_TAG), compile("Text1"), shouldBeVisible(),
                        expectedDescriptionOfTheFoundElements(tagName(LINK_TAG),
                                shouldHaveText(compile("Text1")).and(shouldBeVisible()))},

                {tagName(LINK_TAG), compile("Text2"),  shouldBeVisible().negate(),
                        expectedDescriptionOfTheFoundElements(tagName(LINK_TAG),
                                shouldHaveText(compile("Text2")).and(shouldBeVisible().negate()))},

                {tagName(SELECT), compile("Text23"),
                        shouldContainElements(webElements(tagName(OPTION), OPTION_TEXT22, ofMillis(10)), 3),
                        expectedDescriptionOfTheFoundElements(tagName(SELECT), shouldHaveText(compile("Text23"))
                                .and(shouldContainElements(webElements(tagName(OPTION), OPTION_TEXT22, ofMillis(10)), 3)))},

                {tagName(SELECT), compile(OPTION_TEXT20),
                        shouldContainElements(webElements(tagName(OPTION), ofMillis(10)), 3).negate(),
                        expectedDescriptionOfTheFoundElements(tagName(SELECT),
                                shouldHaveText(compile(OPTION_TEXT20))
                                        .and(shouldContainElements(webElements(tagName(OPTION), ofMillis(10)), 3).negate()))},

                {tagName(BUTTON_TAG), compile("Text4"),
                        shouldContainElements(webElements(tagName(LABEL_TAG), BUTTON_LABEL_TEXT1, ofMillis(10))),
                        expectedDescriptionOfTheFoundElements(tagName(BUTTON_TAG),
                                shouldHaveText(compile("Text4"))
                                        .and(shouldContainElements(webElements(tagName(LABEL_TAG), BUTTON_LABEL_TEXT1, ofMillis(10)))))},

                {tagName(BUTTON_TAG), compile("Text5"),
                        shouldContainElements(webElements(tagName(LABEL_TAG), BUTTON_LABEL_TEXT1, ofMillis(10))).negate(),
                        expectedDescriptionOfTheFoundElements(tagName(BUTTON_TAG),
                                shouldHaveText(compile("Text5"))
                                        .and(shouldContainElements(webElements(tagName(LABEL_TAG),
                                                BUTTON_LABEL_TEXT1, ofMillis(10))).negate()))},

                {CHAINED_FIND_TAB, compile("Text3"), shouldHaveAttribute(ATTR19, VALUE12),
                        expectedDescriptionOfTheFoundElements(CHAINED_FIND_TAB,
                                shouldHaveText(compile("Text3"))
                                        .and(shouldHaveAttribute(ATTR19, VALUE12)))},

                {CHAINED_FIND_TAB, compile("Text2"), shouldHaveAttribute(ATTR19, VALUE12).negate(),
                        expectedDescriptionOfTheFoundElements(CHAINED_FIND_TAB,
                                shouldHaveText(compile("Text2"))
                                        .and(shouldHaveAttribute(ATTR19, VALUE12).negate()))},

                {CHAINED_FIND_TAB, compile("Text4"), shouldHaveAttributeContains(ATTR20, VALUE14),
                        expectedDescriptionOfTheFoundElements(CHAINED_FIND_TAB,
                                shouldHaveText(compile("Text4")).and(shouldHaveAttributeContains(ATTR20, VALUE14)))},

                {CHAINED_FIND_TAB, compile("Text3"), shouldHaveAttributeContains(ATTR20, VALUE14).negate(),
                        expectedDescriptionOfTheFoundElements(CHAINED_FIND_TAB,
                                shouldHaveText(compile("Text3"))
                                        .and(shouldHaveAttributeContains(ATTR20, VALUE14).negate()))},

                {CHAINED_FIND_TAB, compile("Text3"), shouldHaveAttributeContains(ATTR20, compile(VALUE12)),
                        expectedDescriptionOfTheFoundElements(CHAINED_FIND_TAB,
                                shouldHaveText(compile("Text3"))
                                        .and(shouldHaveAttributeContains(ATTR20, compile(VALUE12))))},

                {CHAINED_FIND_TAB, compile(TAB_TEXT1), shouldHaveAttributeContains(ATTR20, compile(VALUE12)).negate(),
                        expectedDescriptionOfTheFoundElements(CHAINED_FIND_TAB,
                                shouldHaveText(compile(TAB_TEXT1))
                                        .and(shouldHaveAttributeContains(ATTR20, compile(VALUE12)).negate()))},

                {xpath(TEXT_FIELD_XPATH), compile(INPUT_TEXT4), shouldHaveCssValue(CSS8, CSS_VALUE6),
                        expectedDescriptionOfTheFoundElements(xpath(TEXT_FIELD_XPATH),
                                shouldHaveText(compile(INPUT_TEXT4))
                                        .and(shouldHaveCssValue(CSS8, CSS_VALUE6)))},

                {xpath(TEXT_FIELD_XPATH), compile(INPUT_TEXT3), shouldHaveCssValue(CSS8, CSS_VALUE6).negate(),
                        expectedDescriptionOfTheFoundElements(xpath(TEXT_FIELD_XPATH),
                                shouldHaveText(compile(INPUT_TEXT3))
                                        .and(shouldHaveCssValue(CSS8, CSS_VALUE6).negate()))},

                {xpath(TEXT_FIELD_XPATH), compile(INPUT_TEXT4), shouldHaveCssValueContains(CSS8, "4")
                        .and(shouldHaveCssValueContains(CSS9, "5")),
                        expectedDescriptionOfTheFoundElements(xpath(TEXT_FIELD_XPATH),
                                shouldHaveText(compile(INPUT_TEXT4)).and(shouldHaveCssValueContains(CSS8, "4")
                                        .and(shouldHaveCssValueContains(CSS9, "5"))))},

                {xpath(TEXT_FIELD_XPATH), compile(INPUT_TEXT1), shouldHaveCssValueContains(CSS8, "4")
                        .and(shouldHaveCssValueContains(CSS9, "5")).negate(),
                        expectedDescriptionOfTheFoundElements(xpath(TEXT_FIELD_XPATH),
                                shouldHaveText(compile(INPUT_TEXT1)).and(shouldHaveCssValueContains(CSS8, "4")
                                        .and(shouldHaveCssValueContains(CSS9, "5")).negate()))},

                {xpath(TEXT_FIELD_XPATH), compile(INPUT_TEXT4), shouldHaveCssValueContains(CSS8, compile("4"))
                        .and(shouldHaveCssValueContains(CSS9, compile("5"))),
                        expectedDescriptionOfTheFoundElements(xpath(TEXT_FIELD_XPATH),
                                shouldHaveText(compile(INPUT_TEXT4)).and(shouldHaveCssValueContains(CSS8, compile("4"))
                                        .and(shouldHaveCssValueContains(CSS9, compile("5")))))},

                {xpath(TEXT_FIELD_XPATH), compile(INPUT_TEXT1), shouldHaveCssValueContains(CSS8, compile("4"))
                        .and(shouldHaveCssValueContains(CSS9, compile("5"))).negate(),
                        expectedDescriptionOfTheFoundElements(xpath(TEXT_FIELD_XPATH),
                                shouldHaveText(compile(INPUT_TEXT1)).and(shouldHaveCssValueContains(CSS8, compile("4"))
                                        .and(shouldHaveCssValueContains(CSS9, compile("5"))).negate()))},
        };
    }

    @Test(dataProvider = "search criteria3", retryAnalyzer = RetryAnalyzer.class)
    public void findElementsByCriteriaAndTexPatternWithDefinedTimeTest(By by,
                                                                      Pattern pattern,
                                                                      Predicate<? extends SearchContext> criteria,
                                                                      String expectedListDescription) {
        setStartBenchMark();
        List<WebElement> webElements = seleniumSteps.find(webElements(by, pattern, ONE_SECOND, (Predicate<? super WebElement>) criteria));
        setEndBenchMark();
        assertThat(getTimeDifference(), greaterThanOrEqualTo(ONE_SECOND.toMillis()));
        assertThat(new BigDecimal(getTimeDifference() - ONE_SECOND.toMillis()),
                either(lessThan(new BigDecimal(HALF_SECOND.toMillis())))
                        .or(closeTo(new BigDecimal(HALF_SECOND.toMillis()), new BigDecimal(200))));
        assertThat(webElements.size(), is(0));
        assertThat(webElements.toString(), is(expectedListDescription));
    }

    @Test(dataProvider = "search criteria3", retryAnalyzer = RetryAnalyzer.class)
    public void findElementsByCriteriaAndTexPatternWithTimeDefinedImplicitlyTest(By by,
                                                                                Pattern pattern,
                                                                                Predicate<? extends SearchContext> criteria,
                                                                                String expectedListDescription) {
        setProperty(ELEMENT_WAITING_TIME_UNIT.getPropertyName(), "SECONDS");
        setProperty(ELEMENT_WAITING_TIME_VALUE.getPropertyName(), "1");
        setStartBenchMark();
        try {
            List<WebElement> webElements = seleniumSteps.find(webElements(by, pattern, (Predicate<? super WebElement>) criteria));
            setEndBenchMark();
            assertThat(getTimeDifference(), greaterThanOrEqualTo(ONE_SECOND.toMillis()));
            assertThat(new BigDecimal(getTimeDifference() - ONE_SECOND.toMillis()),
                    either(lessThan(new BigDecimal(HALF_SECOND.toMillis())))
                            .or(closeTo(new BigDecimal(HALF_SECOND.toMillis()), new BigDecimal(200))));
            assertThat(webElements.size(), is(0));
            assertThat(webElements.toString(), is(expectedListDescription));
        }
        finally {
            removeProperty(ELEMENT_WAITING_TIME_UNIT.getPropertyName());
            removeProperty(ELEMENT_WAITING_TIME_VALUE.getPropertyName());
        }
    }
}
