package com.github.toy.constructor.selenium.test.steps.tests.searching;

import com.github.toy.constructor.selenium.test.BaseWebDriverTest;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.time.Duration;
import java.util.function.Predicate;

import static com.github.toy.constructor.selenium.functions.searching.CommonConditions.*;
import static com.github.toy.constructor.selenium.functions.searching.CommonConditions.shouldHaveAttribute;
import static com.github.toy.constructor.selenium.functions.searching.MultipleSearchSupplier.webElements;
import static com.github.toy.constructor.selenium.functions.searching.SearchSupplier.*;
import static com.github.toy.constructor.selenium.properties.FlagProperties.FIND_ONLY_VISIBLE_ELEMENTS_WHEN_NO_CONDITION;
import static com.github.toy.constructor.selenium.properties.WaitingProperties.TimeUnitProperties.ELEMENT_WAITING_TIME_UNIT;
import static com.github.toy.constructor.selenium.properties.WaitingProperties.TimeValueProperties.ELEMENT_WAITING_TIME_VALUE;
import static com.github.toy.constructor.selenium.test.FakeDOMModel.*;
import static java.lang.String.format;
import static java.time.Duration.ofMillis;
import static java.util.regex.Pattern.compile;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.openqa.selenium.By.className;
import static org.openqa.selenium.By.tagName;
import static org.openqa.selenium.By.xpath;

@SuppressWarnings("unchecked")
public class SearchForWebElementNegativeTest extends BaseWebDriverTest {

    private static final String FOUND_BY_PATTERN_ERROR = "Nothing was found. Attempt to get a single item of Web elements located [%s]";
    private static final String FOUND_ON_CONDITION_ERROR = FOUND_BY_PATTERN_ERROR + ". Condition: %s";
    private static final By CLASS_THAT_DOES_NOT_EXIST = className("fakeClass");

    private static String expectedDescriptionOfNotFoundElementError(By by, Predicate<? extends SearchContext> condition) {
        return format(FOUND_ON_CONDITION_ERROR, by, condition);
    }

    private static String expectedDescriptionOfNotFoundElementError(By by) {
        return format(FOUND_BY_PATTERN_ERROR, by);
    }

    @Test(expectedExceptions = NoSuchElementException.class)
    public void findWebElementFirstLevelWithoutConditionWithDefinedTimeTest() {
        setStartBenchMark();
        try {
            seleniumSteps.find(webElement(CLASS_THAT_DOES_NOT_EXIST, FIVE_SECONDS));
        }
        catch (Exception e) {
            setEndBenchMark();
            assertThat(e.getMessage(), containsString(expectedDescriptionOfNotFoundElementError(CLASS_THAT_DOES_NOT_EXIST)));
            throw e;
        }
        finally {
            assertThat(getTimeDifference(), greaterThanOrEqualTo(FIVE_SECONDS.toMillis()));
            assertThat(getTimeDifference() - FIVE_SECONDS.toMillis(), lessThan(HALF_SECOND.toMillis()));
        }
    }

    @Test(expectedExceptions = NoSuchElementException.class)
    public void findWebElementFirstLevelWithoutConditionWithTimeDefinedImplicitlyTest() {
        setProperty(ELEMENT_WAITING_TIME_UNIT.getPropertyName(), "SECONDS");
        setProperty(ELEMENT_WAITING_TIME_VALUE.getPropertyName(), "5");
        setStartBenchMark();
        try {
            seleniumSteps.find(webElement(CLASS_THAT_DOES_NOT_EXIST));
        }
        catch (Exception e) {
            setEndBenchMark();
            assertThat(e.getMessage(), containsString(expectedDescriptionOfNotFoundElementError(CLASS_THAT_DOES_NOT_EXIST)));
            throw e;
        }
        finally {
            removeProperty(ELEMENT_WAITING_TIME_UNIT.getPropertyName());
            removeProperty(ELEMENT_WAITING_TIME_VALUE.getPropertyName());
            assertThat(getTimeDifference(), greaterThanOrEqualTo(FIVE_SECONDS.toMillis()));
            assertThat(getTimeDifference() - FIVE_SECONDS.toMillis(), lessThan(HALF_SECOND.toMillis()));
        }
    }

    @Test(expectedExceptions = NoSuchElementException.class)
    public void findWebElementFirstLevelOnlyVisibleImplicitConditionAndDefinedTimeTest() {
        setProperty(FIND_ONLY_VISIBLE_ELEMENTS_WHEN_NO_CONDITION.getPropertyName(), "true");
        setStartBenchMark();
        try {
            seleniumSteps.find(webElement(INVISIBLE_SPAN_BY, ONE_SECOND));
        }
        catch (Exception e) {
            setEndBenchMark();
            assertThat(e.getMessage(), containsString(expectedDescriptionOfNotFoundElementError(INVISIBLE_SPAN_BY,
                    shouldBeVisible())));
            throw e;
        }
        finally {
            removeProperty(FIND_ONLY_VISIBLE_ELEMENTS_WHEN_NO_CONDITION.getPropertyName());
            assertThat(getTimeDifference(), greaterThanOrEqualTo(ONE_SECOND.toMillis()));
            assertThat(getTimeDifference() - ONE_SECOND.toMillis(), lessThan(HALF_SECOND.toMillis()));
        }
    }

    @Test(expectedExceptions = NoSuchElementException.class)
    public void findWebElementFirstLevelOnlyVisibleImplicitConditionAndImplicitTimeTest() {
        setProperty(FIND_ONLY_VISIBLE_ELEMENTS_WHEN_NO_CONDITION.getPropertyName(), "true");
        setProperty(ELEMENT_WAITING_TIME_UNIT.getPropertyName(), "SECONDS");
        setProperty(ELEMENT_WAITING_TIME_VALUE.getPropertyName(), "1");
        setStartBenchMark();
        try {
            seleniumSteps.find(webElement(INVISIBLE_SPAN_BY));
        }
        catch (Exception e) {
            setEndBenchMark();
            assertThat(e.getMessage(), containsString(expectedDescriptionOfNotFoundElementError(INVISIBLE_SPAN_BY,
                    shouldBeVisible())));
            throw e;
        }
        finally {
            removeProperty(FIND_ONLY_VISIBLE_ELEMENTS_WHEN_NO_CONDITION.getPropertyName());
            removeProperty(ELEMENT_WAITING_TIME_UNIT.getPropertyName());
            removeProperty(ELEMENT_WAITING_TIME_VALUE.getPropertyName());
            assertThat(getTimeDifference(), greaterThanOrEqualTo(ONE_SECOND.toMillis()));
            assertThat(getTimeDifference() - ONE_SECOND.toMillis(), lessThan(HALF_SECOND.toMillis()));
        }
    }

    @Test(expectedExceptions = NoSuchElementException.class)
    public void findWebElementFirstLevelOnlyVisibleImplicitConditionAndTimeConflictTest() {
        setProperty(FIND_ONLY_VISIBLE_ELEMENTS_WHEN_NO_CONDITION.getPropertyName(), "true");
        setProperty(ELEMENT_WAITING_TIME_UNIT.getPropertyName(), "SECONDS");
        setProperty(ELEMENT_WAITING_TIME_VALUE.getPropertyName(), "5");
        setStartBenchMark();
        try {
            seleniumSteps.find(webElement(INVISIBLE_SPAN_BY, ONE_SECOND));
        }
        catch (Exception e) {
            setEndBenchMark();
            assertThat(e.getMessage(), containsString(expectedDescriptionOfNotFoundElementError(INVISIBLE_SPAN_BY,
                    shouldBeVisible())));
            throw e;
        }
        finally {
            removeProperty(FIND_ONLY_VISIBLE_ELEMENTS_WHEN_NO_CONDITION.getPropertyName());
            removeProperty(ELEMENT_WAITING_TIME_UNIT.getPropertyName());
            removeProperty(ELEMENT_WAITING_TIME_VALUE.getPropertyName());
            assertThat(getTimeDifference(), greaterThanOrEqualTo(ONE_SECOND.toMillis()));
            assertThat(getTimeDifference() - ONE_SECOND.toMillis(), lessThan(HALF_SECOND.toMillis()));
        }
    }

    @Test(expectedExceptions = NoSuchElementException.class)
    public void findWebElementChainedWithoutConditionWithDefinedTimeTest() {
        setStartBenchMark();
        try {
            seleniumSteps.find(webElement(CLASS_THAT_DOES_NOT_EXIST, FIVE_SECONDS)
                    .foundFrom(webElement(tagName(BUTTON_TAG))));
        }
        catch (Exception e) {
            setEndBenchMark();
            assertThat(e.getMessage(), containsString(expectedDescriptionOfNotFoundElementError(CLASS_THAT_DOES_NOT_EXIST)));
            throw e;
        }
        finally {
            assertThat(getTimeDifference(), greaterThanOrEqualTo(FIVE_SECONDS.toMillis()));
            assertThat(getTimeDifference() - FIVE_SECONDS.toMillis(), lessThan(HALF_SECOND.toMillis()));
        }
    }

    @Test(expectedExceptions = NoSuchElementException.class)
    public void findWebElementChainedWithoutConditionWithTimeDefinedImplicitlyTest() {
        setProperty(ELEMENT_WAITING_TIME_UNIT.getPropertyName(), "SECONDS");
        setProperty(ELEMENT_WAITING_TIME_VALUE.getPropertyName(), "5");
        setStartBenchMark();
        try {
            seleniumSteps.find(webElement(CLASS_THAT_DOES_NOT_EXIST)
                    .foundFrom(webElement(tagName(BUTTON_TAG))));
        }
        catch (Exception e) {
            setEndBenchMark();
            assertThat(e.getMessage(), containsString(expectedDescriptionOfNotFoundElementError(CLASS_THAT_DOES_NOT_EXIST)));
            throw e;
        }
        finally {
            removeProperty(ELEMENT_WAITING_TIME_UNIT.getPropertyName());
            removeProperty(ELEMENT_WAITING_TIME_VALUE.getPropertyName());
            assertThat(getTimeDifference(), greaterThanOrEqualTo(FIVE_SECONDS.toMillis()));
            assertThat(getTimeDifference() - FIVE_SECONDS.toMillis(), lessThan(HALF_SECOND.toMillis()));
        }
    }

    @Test(expectedExceptions = NoSuchElementException.class)
    public void findWebElementChainedOnlyVisibleImplicitConditionAndDefinedTimeTest() {
        setProperty(FIND_ONLY_VISIBLE_ELEMENTS_WHEN_NO_CONDITION.getPropertyName(), "true");
        setStartBenchMark();
        try {
            seleniumSteps.find(webElement(INVISIBLE_SPAN_BY, ONE_SECOND)
                    .foundFrom(webElement(className(SPREAD_SHEET_CLASS))));
        }
        catch (Exception e) {
            setEndBenchMark();
            assertThat(e.getMessage(), containsString(expectedDescriptionOfNotFoundElementError(INVISIBLE_SPAN_BY,
                    shouldBeVisible())));
            throw e;
        }
        finally {
            removeProperty(FIND_ONLY_VISIBLE_ELEMENTS_WHEN_NO_CONDITION.getPropertyName());
            assertThat(getTimeDifference(), greaterThanOrEqualTo(ONE_SECOND.toMillis()));
            assertThat(getTimeDifference() - ONE_SECOND.toMillis(), lessThan(HALF_SECOND.toMillis()));
        }
    }

    @Test(expectedExceptions = NoSuchElementException.class)
    public void findWebElementChainedOnlyVisibleImplicitConditionAndImplicitTimeTest() {
        setProperty(FIND_ONLY_VISIBLE_ELEMENTS_WHEN_NO_CONDITION.getPropertyName(), "true");
        setProperty(ELEMENT_WAITING_TIME_UNIT.getPropertyName(), "SECONDS");
        setProperty(ELEMENT_WAITING_TIME_VALUE.getPropertyName(), "1");
        setStartBenchMark();
        try {
            seleniumSteps.find(webElement(INVISIBLE_SPAN_BY)
                    .foundFrom(webElement(className(SPREAD_SHEET_CLASS))));
        }
        catch (Exception e) {
            setEndBenchMark();
            assertThat(e.getMessage(), containsString(expectedDescriptionOfNotFoundElementError(INVISIBLE_SPAN_BY,
                    shouldBeVisible())));
            throw e;
        }
        finally {
            removeProperty(FIND_ONLY_VISIBLE_ELEMENTS_WHEN_NO_CONDITION.getPropertyName());
            removeProperty(ELEMENT_WAITING_TIME_UNIT.getPropertyName());
            removeProperty(ELEMENT_WAITING_TIME_VALUE.getPropertyName());
            assertThat(getTimeDifference(), greaterThanOrEqualTo(ONE_SECOND.toMillis()));
            assertThat(getTimeDifference() - ONE_SECOND.toMillis(), lessThan(HALF_SECOND.toMillis()));
        }
    }

    @Test(expectedExceptions = NoSuchElementException.class)
    public void findWebElementChainedOnlyVisibleImplicitConditionAndTimeConflictTest() {
        setProperty(FIND_ONLY_VISIBLE_ELEMENTS_WHEN_NO_CONDITION.getPropertyName(), "true");
        setProperty(ELEMENT_WAITING_TIME_UNIT.getPropertyName(), "SECONDS");
        setProperty(ELEMENT_WAITING_TIME_VALUE.getPropertyName(), "5");
        setStartBenchMark();
        try {
            seleniumSteps.find(webElement(INVISIBLE_SPAN_BY, ONE_SECOND)
                    .foundFrom(webElement(className(SPREAD_SHEET_CLASS))));
        }
        catch (Exception e) {
            setEndBenchMark();
            assertThat(e.getMessage(), containsString(expectedDescriptionOfNotFoundElementError(INVISIBLE_SPAN_BY,
                    shouldBeVisible())));
            throw e;
        }
        finally {
            removeProperty(FIND_ONLY_VISIBLE_ELEMENTS_WHEN_NO_CONDITION.getPropertyName());
            removeProperty(ELEMENT_WAITING_TIME_UNIT.getPropertyName());
            removeProperty(ELEMENT_WAITING_TIME_VALUE.getPropertyName());
            assertThat(getTimeDifference(), greaterThanOrEqualTo(ONE_SECOND.toMillis()));
            assertThat(getTimeDifference() - ONE_SECOND.toMillis(), lessThan(HALF_SECOND.toMillis()));
        }
    }

    @Test(expectedExceptions = NoSuchElementException.class)
    public void findFromWebElementWithoutConditionWithDefinedTimeTest() {
        WebElement parent = seleniumSteps.find(webElement(tagName(BUTTON_TAG)));
        setStartBenchMark();
        try {
            seleniumSteps.find(webElement(CLASS_THAT_DOES_NOT_EXIST, FIVE_SECONDS)
                    .foundFrom(parent));
        }
        catch (Exception e) {
            setEndBenchMark();
            assertThat(e.getMessage(), containsString(expectedDescriptionOfNotFoundElementError(CLASS_THAT_DOES_NOT_EXIST)));
            throw e;
        }
        finally {
            assertThat(getTimeDifference(), greaterThanOrEqualTo(FIVE_SECONDS.toMillis()));
            assertThat(getTimeDifference() - FIVE_SECONDS.toMillis(), lessThan(HALF_SECOND.toMillis()));
        }
    }

    @Test(expectedExceptions = NoSuchElementException.class)
    public void findFromWebElementWithoutConditionWithTimeDefinedImplicitlyTest() {
        WebElement parent = seleniumSteps.find(webElement(tagName(BUTTON_TAG)));
        setProperty(ELEMENT_WAITING_TIME_UNIT.getPropertyName(), "SECONDS");
        setProperty(ELEMENT_WAITING_TIME_VALUE.getPropertyName(), "5");
        setStartBenchMark();
        try {
            seleniumSteps.find(webElement(CLASS_THAT_DOES_NOT_EXIST)
                    .foundFrom(parent));
        }
        catch (Exception e) {
            setEndBenchMark();
            assertThat(e.getMessage(), containsString(expectedDescriptionOfNotFoundElementError(CLASS_THAT_DOES_NOT_EXIST)));
            throw e;
        }
        finally {
            removeProperty(ELEMENT_WAITING_TIME_UNIT.getPropertyName());
            removeProperty(ELEMENT_WAITING_TIME_VALUE.getPropertyName());
            assertThat(getTimeDifference(), greaterThanOrEqualTo(FIVE_SECONDS.toMillis()));
            assertThat(getTimeDifference() - FIVE_SECONDS.toMillis(), lessThan(HALF_SECOND.toMillis()));
        }
    }

    @Test(expectedExceptions = NoSuchElementException.class)
    public void findFromWebElementOnlyVisibleImplicitConditionAndDefinedTimeTest() {
        setProperty(FIND_ONLY_VISIBLE_ELEMENTS_WHEN_NO_CONDITION.getPropertyName(), "true");
        WebElement parent = seleniumSteps.find(webElement(className(SPREAD_SHEET_CLASS)));
        setStartBenchMark();
        try {
            seleniumSteps.find(webElement(INVISIBLE_SPAN_BY, ONE_SECOND)
                    .foundFrom(parent));
        }
        catch (Exception e) {
            setEndBenchMark();
            assertThat(e.getMessage(), containsString(expectedDescriptionOfNotFoundElementError(INVISIBLE_SPAN_BY,
                    shouldBeVisible())));
            throw e;
        }
        finally {
            removeProperty(FIND_ONLY_VISIBLE_ELEMENTS_WHEN_NO_CONDITION.getPropertyName());
            assertThat(getTimeDifference(), greaterThanOrEqualTo(ONE_SECOND.toMillis()));
            assertThat(getTimeDifference() - ONE_SECOND.toMillis(), lessThan(HALF_SECOND.toMillis()));
        }
    }

    @Test(expectedExceptions = NoSuchElementException.class)
    public void findFromWebElementOnlyVisibleImplicitConditionAndImplicitTimeTest() {
        setProperty(FIND_ONLY_VISIBLE_ELEMENTS_WHEN_NO_CONDITION.getPropertyName(), "true");
        setProperty(ELEMENT_WAITING_TIME_UNIT.getPropertyName(), "SECONDS");
        setProperty(ELEMENT_WAITING_TIME_VALUE.getPropertyName(), "1");
        WebElement parent = seleniumSteps.find(webElement(className(SPREAD_SHEET_CLASS)));
        setStartBenchMark();
        try {
            seleniumSteps.find(webElement(INVISIBLE_SPAN_BY)
                    .foundFrom(parent));
        }
        catch (Exception e) {
            setEndBenchMark();
            assertThat(e.getMessage(), containsString(expectedDescriptionOfNotFoundElementError(INVISIBLE_SPAN_BY,
                    shouldBeVisible())));
            throw e;
        }
        finally {
            removeProperty(FIND_ONLY_VISIBLE_ELEMENTS_WHEN_NO_CONDITION.getPropertyName());
            removeProperty(ELEMENT_WAITING_TIME_UNIT.getPropertyName());
            removeProperty(ELEMENT_WAITING_TIME_VALUE.getPropertyName());
            assertThat(getTimeDifference(), greaterThanOrEqualTo(ONE_SECOND.toMillis()));
            assertThat(getTimeDifference() - ONE_SECOND.toMillis(), lessThan(HALF_SECOND.toMillis()));
        }
    }

    @Test(expectedExceptions = NoSuchElementException.class)
    public void findFromWebElementOnlyVisibleImplicitConditionAndTimeConflictTest() {
        setProperty(FIND_ONLY_VISIBLE_ELEMENTS_WHEN_NO_CONDITION.getPropertyName(), "true");
        setProperty(ELEMENT_WAITING_TIME_UNIT.getPropertyName(), "SECONDS");
        setProperty(ELEMENT_WAITING_TIME_VALUE.getPropertyName(), "5");
        WebElement parent = seleniumSteps.find(webElement(className(SPREAD_SHEET_CLASS)));
        setStartBenchMark();
        try {
            seleniumSteps.find(webElement(INVISIBLE_SPAN_BY, ONE_SECOND)
                    .foundFrom(parent));
        }
        catch (Exception e) {
            setEndBenchMark();
            assertThat(e.getMessage(), containsString(expectedDescriptionOfNotFoundElementError(INVISIBLE_SPAN_BY,
                    shouldBeVisible())));
            throw e;
        }
        finally {
            removeProperty(FIND_ONLY_VISIBLE_ELEMENTS_WHEN_NO_CONDITION.getPropertyName());
            removeProperty(ELEMENT_WAITING_TIME_UNIT.getPropertyName());
            removeProperty(ELEMENT_WAITING_TIME_VALUE.getPropertyName());
            assertThat(getTimeDifference(), greaterThanOrEqualTo(ONE_SECOND.toMillis()));
            assertThat(getTimeDifference() - ONE_SECOND.toMillis(), lessThan(HALF_SECOND.toMillis()));
        }
    }

    @DataProvider(name = "search criteria1")
    public static Object[][] searchCriteria() {
        return new Object[][]{
                {INVISIBLE_SPAN_BY, shouldBeEnabled(),
                        expectedDescriptionOfNotFoundElementError(INVISIBLE_SPAN_BY, shouldBeEnabled())},

                {VISIBLE_DIV_BY, shouldBeEnabled().negate(),
                        expectedDescriptionOfNotFoundElementError(VISIBLE_DIV_BY, shouldBeEnabled().negate())},

                {INVISIBLE_SPAN_BY, shouldBeVisible(),
                        expectedDescriptionOfNotFoundElementError(INVISIBLE_SPAN_BY, shouldBeVisible())},

                {VISIBLE_DIV_BY, shouldBeVisible().negate(),
                        expectedDescriptionOfNotFoundElementError(VISIBLE_DIV_BY,  shouldBeVisible().negate())},

                {className(MULTI_SELECT_CLASS), shouldContainElements(webElements(className(ITEM_OPTION_CLASS), FIVE_SECONDS), 4),
                        expectedDescriptionOfNotFoundElementError(className(MULTI_SELECT_CLASS),
                                shouldContainElements(webElements(className(ITEM_OPTION_CLASS), FIVE_SECONDS), 4))},

                {className(MULTI_SELECT_CLASS),
                        shouldContainElements(webElements(className(ITEM_OPTION_CLASS), FIVE_SECONDS), 3)
                                .or(shouldContainElements(webElements(className(ITEM_OPTION_CLASS), FIVE_SECONDS), 2))
                                .negate(),
                        expectedDescriptionOfNotFoundElementError(className(MULTI_SELECT_CLASS),
                                shouldContainElements(webElements(className(ITEM_OPTION_CLASS), FIVE_SECONDS), 3)
                                        .or(shouldContainElements(webElements(className(ITEM_OPTION_CLASS), FIVE_SECONDS), 2))
                                        .negate())},

                {className(MULTI_SELECT_CLASS),
                        shouldContainElements(webElements(tagName(OPTION), ofMillis(50))),
                        expectedDescriptionOfNotFoundElementError(className(MULTI_SELECT_CLASS),
                                shouldContainElements(webElements(tagName(OPTION), ofMillis(50))))},

                {tagName(SELECT), shouldContainElements(webElements(tagName(OPTION), ofMillis(50))).negate(),
                        expectedDescriptionOfNotFoundElementError(tagName(SELECT),
                                shouldContainElements(webElements(tagName(OPTION), ofMillis(50))).negate())},

                {tagName(TEXT_AREA_TAG), shouldHaveAttribute(ATTR11, VALUE10),
                        expectedDescriptionOfNotFoundElementError(tagName(TEXT_AREA_TAG),
                                shouldHaveAttribute(ATTR11, VALUE10))},

                {tagName(TEXT_AREA_TAG), shouldHaveAttribute(ATTR11, VALUE12)
                        .or(shouldHaveAttribute(ATTR11, VALUE13))
                        .or(shouldHaveAttribute(ATTR11, VALUE14))
                        .or(shouldHaveAttribute(ATTR11, VALUE15)).negate(),
                        expectedDescriptionOfNotFoundElementError(tagName(TEXT_AREA_TAG),
                                shouldHaveAttribute(ATTR11, VALUE12)
                                        .or(shouldHaveAttribute(ATTR11, VALUE13))
                                        .or(shouldHaveAttribute(ATTR11, VALUE14))
                                        .or(shouldHaveAttribute(ATTR11, VALUE15)).negate())},

                {tagName(TEXT_AREA_TAG), shouldHaveAttributeContains(ATTR11, "10"),
                        expectedDescriptionOfNotFoundElementError(tagName(TEXT_AREA_TAG),
                                shouldHaveAttributeContains(ATTR11, "10"))},

                {tagName(TEXT_AREA_TAG), shouldHaveAttributeContains(ATTR11, "12")
                        .or(shouldHaveAttributeContains(ATTR11, "13"))
                        .or(shouldHaveAttributeContains(ATTR11, VALUE14))
                        .or(shouldHaveAttributeContains(ATTR11, VALUE15)).negate(),
                        expectedDescriptionOfNotFoundElementError(tagName(TEXT_AREA_TAG),
                                shouldHaveAttributeContains(ATTR11, "12")
                                        .or(shouldHaveAttributeContains(ATTR11, "13"))
                                        .or(shouldHaveAttributeContains(ATTR11, VALUE14))
                                        .or(shouldHaveAttributeContains(ATTR11, VALUE15)).negate())},

                {tagName(TEXT_AREA_TAG), shouldHaveAttributeContains(ATTR11, compile("10")),
                        expectedDescriptionOfNotFoundElementError(tagName(TEXT_AREA_TAG),
                                shouldHaveAttributeContains(ATTR11, compile("10")))},

                {tagName(TEXT_AREA_TAG), shouldHaveAttributeContains(ATTR11, compile("12"))
                        .or(shouldHaveAttributeContains(ATTR11, compile("13")))
                        .or(shouldHaveAttributeContains(ATTR11, compile(VALUE14)))
                        .or(shouldHaveAttributeContains(ATTR11, compile(VALUE15))).negate(),
                        expectedDescriptionOfNotFoundElementError(tagName(TEXT_AREA_TAG),
                                shouldHaveAttributeContains(ATTR11, compile("12"))
                                        .or(shouldHaveAttributeContains(ATTR11, compile("13")))
                                        .or(shouldHaveAttributeContains(ATTR11, compile(VALUE14)))
                                        .or(shouldHaveAttributeContains(ATTR11, compile(VALUE15))).negate())},

                {xpath(RADIO_BUTTON_XPATH), shouldHaveCssValue(CSS18, CSS_VALUE6),
                        expectedDescriptionOfNotFoundElementError(xpath(RADIO_BUTTON_XPATH),
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
                        expectedDescriptionOfNotFoundElementError(xpath(RADIO_BUTTON_XPATH),
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
                        expectedDescriptionOfNotFoundElementError(xpath(RADIO_BUTTON_XPATH),
                                shouldHaveCssValueContains(CSS18, "value6"))},

                {xpath(RADIO_BUTTON_XPATH),
                        shouldHaveCssValueContains(CSS18, "value").negate(),
                        expectedDescriptionOfNotFoundElementError(xpath(RADIO_BUTTON_XPATH),
                                shouldHaveCssValueContains(CSS18, "value").negate())},

                {xpath(RADIO_BUTTON_XPATH), shouldHaveCssValueContains(CSS18, compile("value6")),
                        expectedDescriptionOfNotFoundElementError(xpath(RADIO_BUTTON_XPATH),
                                shouldHaveCssValueContains(CSS18, compile("value6")))},

                {xpath(RADIO_BUTTON_XPATH),
                        shouldHaveCssValueContains(CSS18, compile("value")).negate(),
                        expectedDescriptionOfNotFoundElementError(xpath(RADIO_BUTTON_XPATH),
                                shouldHaveCssValueContains(CSS18, compile("value")).negate())},
        };
    }

    @Test(dataProvider = "search criteria1", expectedExceptions = NoSuchElementException.class)
    public void findElementByCriteriaWithDefinedTimeTest(By by, Predicate<? extends SearchContext> criteria,
                                                         String expectedErrorMessage) {
        setStartBenchMark();
        try {
            seleniumSteps.find(webElement(by, FIVE_SECONDS, (Predicate<? super WebElement>) criteria));
        }
        catch (Exception e) {
            setEndBenchMark();
            assertThat(e.getMessage(), containsString(expectedErrorMessage));
            throw e;
        }
        finally {
            assertThat(getTimeDifference(), greaterThanOrEqualTo(FIVE_SECONDS.toMillis()));
            assertThat(getTimeDifference() - FIVE_SECONDS.toMillis(), lessThan(ONE_SECOND.toMillis()));
        }
    }

    @Test(dataProvider = "search criteria1", expectedExceptions = NoSuchElementException.class)
    public void findElementByCriteriaWithTimeDefinedImplicitlyTest(By by, Predicate<? extends SearchContext> criteria,
                                                                   String expectedErrorMessage) {
        setProperty(ELEMENT_WAITING_TIME_UNIT.getPropertyName(), "SECONDS");
        setProperty(ELEMENT_WAITING_TIME_VALUE.getPropertyName(), "5");
        setStartBenchMark();
        try {
            seleniumSteps.find(webElement(by, (Predicate<? super WebElement>) criteria));
        }
        catch (Exception e) {
            setEndBenchMark();
            assertThat(e.getMessage(), containsString(expectedErrorMessage));
            throw e;
        }
        finally {
            removeProperty(ELEMENT_WAITING_TIME_UNIT.getPropertyName());
            removeProperty(ELEMENT_WAITING_TIME_VALUE.getPropertyName());
            assertThat(getTimeDifference(), greaterThanOrEqualTo(FIVE_SECONDS.toMillis()));
            assertThat(getTimeDifference() - FIVE_SECONDS.toMillis(), lessThan(ONE_SECOND.toMillis()));
        }
    }
}
