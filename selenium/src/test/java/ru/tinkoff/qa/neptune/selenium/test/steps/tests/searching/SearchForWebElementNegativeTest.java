package ru.tinkoff.qa.neptune.selenium.test.steps.tests.searching;

import ru.tinkoff.qa.neptune.selenium.test.BaseWebDriverTest;
import ru.tinkoff.qa.neptune.selenium.test.RetryAnalyzer;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.math.BigDecimal;
import java.util.function.Predicate;
import java.util.regex.Pattern;

import static ru.tinkoff.qa.neptune.selenium.functions.searching.CommonConditions.*;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.CommonConditions.shouldHaveAttribute;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.MultipleSearchSupplier.webElements;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.SearchSupplier.*;
import static ru.tinkoff.qa.neptune.selenium.properties.SessionFlagProperties.FIND_ONLY_VISIBLE_ELEMENTS_WHEN_NO_CONDITION;
import static ru.tinkoff.qa.neptune.selenium.properties.WaitingProperties.TimeUnitProperties.ELEMENT_WAITING_TIME_UNIT;
import static ru.tinkoff.qa.neptune.selenium.properties.WaitingProperties.TimeValueProperties.ELEMENT_WAITING_TIME_VALUE;
import static ru.tinkoff.qa.neptune.selenium.test.FakeDOMModel.*;
import static java.lang.String.format;
import static java.time.Duration.ofMillis;
import static java.util.function.Predicate.not;
import static java.util.regex.Pattern.compile;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.openqa.selenium.By.className;
import static org.openqa.selenium.By.tagName;
import static org.openqa.selenium.By.xpath;

public class SearchForWebElementNegativeTest extends BaseWebDriverTest {

    private static final String FOUND_BY_PATTERN_ERROR = "Nothing was found. Attempt to get Web element located %s";
    private static final String FOUND_ON_CONDITION_ERROR = FOUND_BY_PATTERN_ERROR + " [Criteria: %s]";
    private static final By CLASS_THAT_DOES_NOT_EXIST = className("fakeClass");

    private static String expectedDescriptionOfNotFoundElementError(By by, Predicate<? extends SearchContext> condition) {
        return format(FOUND_ON_CONDITION_ERROR, by, condition);
    }

    private static String expectedDescriptionOfNotFoundElementError() {
        return format(FOUND_BY_PATTERN_ERROR, CLASS_THAT_DOES_NOT_EXIST);
    }

    @Test(expectedExceptions = NoSuchElementException.class, retryAnalyzer = RetryAnalyzer.class)
    public void findWebElementFirstLevelWithoutConditionWithDefinedTimeTest() {
        setStartBenchMark();
        try {
            seleniumSteps.find(webElement(CLASS_THAT_DOES_NOT_EXIST)
                    .timeOut(ONE_SECOND));
        }
        catch (Exception e) {
            setEndBenchMark();
            assertThat(e.getMessage(), containsString(expectedDescriptionOfNotFoundElementError()));
            throw e;
        }
        finally {
            assertThat(getTimeDifference(), greaterThanOrEqualTo(ONE_SECOND.toMillis()));
            assertThat(getTimeDifference() - ONE_SECOND.toMillis(), lessThan(HALF_SECOND.toMillis()));
        }
    }

    @Test(expectedExceptions = NoSuchElementException.class, retryAnalyzer = RetryAnalyzer.class)
    public void findWebElementFirstLevelWithoutConditionWithTimeDefinedImplicitlyTest() {
        setProperty(ELEMENT_WAITING_TIME_UNIT.getPropertyName(), "SECONDS");
        setProperty(ELEMENT_WAITING_TIME_VALUE.getPropertyName(), "1");
        setStartBenchMark();
        try {
            seleniumSteps.find(webElement(CLASS_THAT_DOES_NOT_EXIST));
        }
        catch (Exception e) {
            setEndBenchMark();
            assertThat(e.getMessage(), containsString(expectedDescriptionOfNotFoundElementError()));
            throw e;
        }
        finally {
            removeProperty(ELEMENT_WAITING_TIME_UNIT.getPropertyName());
            removeProperty(ELEMENT_WAITING_TIME_VALUE.getPropertyName());
            assertThat(getTimeDifference(), greaterThanOrEqualTo(ONE_SECOND.toMillis()));
            assertThat(getTimeDifference() - ONE_SECOND.toMillis(), lessThan(HALF_SECOND.toMillis()));
        }
    }

    @Test(expectedExceptions = NoSuchElementException.class, retryAnalyzer = RetryAnalyzer.class)
    public void findWebElementFirstLevelOnlyVisibleImplicitConditionAndDefinedTimeTest() {
        setProperty(FIND_ONLY_VISIBLE_ELEMENTS_WHEN_NO_CONDITION.getPropertyName(), "true");
        setStartBenchMark();
        try {
            seleniumSteps.find(webElement(INVISIBLE_SPAN_BY)
                    .timeOut(ONE_SECOND));
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

    @Test(expectedExceptions = NoSuchElementException.class, retryAnalyzer = RetryAnalyzer.class)
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

    @Test(expectedExceptions = NoSuchElementException.class, retryAnalyzer = RetryAnalyzer.class)
    public void findWebElementFirstLevelOnlyVisibleImplicitConditionAndTimeConflictTest() {
        setProperty(FIND_ONLY_VISIBLE_ELEMENTS_WHEN_NO_CONDITION.getPropertyName(), "true");
        setProperty(ELEMENT_WAITING_TIME_UNIT.getPropertyName(), "SECONDS");
        setProperty(ELEMENT_WAITING_TIME_VALUE.getPropertyName(), "5");
        setStartBenchMark();
        try {
            seleniumSteps.find(webElement(INVISIBLE_SPAN_BY)
                    .timeOut(ONE_SECOND));
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

    @Test(expectedExceptions = NoSuchElementException.class, retryAnalyzer = RetryAnalyzer.class)
    public void findWebElementChainedWithoutConditionWithDefinedTimeTest() {
        setStartBenchMark();
        try {
            seleniumSteps.find(webElement(CLASS_THAT_DOES_NOT_EXIST)
                    .timeOut(ONE_SECOND)
                    .foundFrom(webElement(tagName(BUTTON_TAG))));
        }
        catch (Exception e) {
            setEndBenchMark();
            assertThat(e.getMessage(), containsString(expectedDescriptionOfNotFoundElementError()));
            throw e;
        }
        finally {
            assertThat(getTimeDifference(), greaterThanOrEqualTo(ONE_SECOND.toMillis()));
            assertThat(getTimeDifference() - ONE_SECOND.toMillis(), lessThan(HALF_SECOND.toMillis()));
        }
    }

    @Test(expectedExceptions = NoSuchElementException.class, retryAnalyzer = RetryAnalyzer.class)
    public void findWebElementChainedWithoutConditionWithTimeDefinedImplicitlyTest() {
        setProperty(ELEMENT_WAITING_TIME_UNIT.getPropertyName(), "SECONDS");
        setProperty(ELEMENT_WAITING_TIME_VALUE.getPropertyName(), "1");
        setStartBenchMark();
        try {
            seleniumSteps.find(webElement(CLASS_THAT_DOES_NOT_EXIST)
                    .foundFrom(webElement(tagName(BUTTON_TAG))));
        }
        catch (Exception e) {
            setEndBenchMark();
            assertThat(e.getMessage(), containsString(expectedDescriptionOfNotFoundElementError()));
            throw e;
        }
        finally {
            removeProperty(ELEMENT_WAITING_TIME_UNIT.getPropertyName());
            removeProperty(ELEMENT_WAITING_TIME_VALUE.getPropertyName());
            assertThat(getTimeDifference(), greaterThanOrEqualTo(ONE_SECOND.toMillis()));
            assertThat(getTimeDifference() - ONE_SECOND.toMillis(), lessThan(HALF_SECOND.toMillis()));
        }
    }

    @Test(expectedExceptions = NoSuchElementException.class, retryAnalyzer = RetryAnalyzer.class)
    public void findWebElementChainedOnlyVisibleImplicitConditionAndDefinedTimeTest() {
        setProperty(FIND_ONLY_VISIBLE_ELEMENTS_WHEN_NO_CONDITION.getPropertyName(), "true");
        setStartBenchMark();
        try {
            seleniumSteps.find(webElement(INVISIBLE_SPAN_BY)
                    .timeOut(ONE_SECOND)
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

    @Test(expectedExceptions = NoSuchElementException.class, retryAnalyzer = RetryAnalyzer.class)
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

    @Test(expectedExceptions = NoSuchElementException.class, retryAnalyzer = RetryAnalyzer.class)
    public void findWebElementChainedOnlyVisibleImplicitConditionAndTimeConflictTest() {
        setProperty(FIND_ONLY_VISIBLE_ELEMENTS_WHEN_NO_CONDITION.getPropertyName(), "true");
        setProperty(ELEMENT_WAITING_TIME_UNIT.getPropertyName(), "SECONDS");
        setProperty(ELEMENT_WAITING_TIME_VALUE.getPropertyName(), "5");
        setStartBenchMark();
        try {
            seleniumSteps.find(webElement(INVISIBLE_SPAN_BY)
                    .timeOut(ONE_SECOND)
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

    @Test(expectedExceptions = NoSuchElementException.class, retryAnalyzer = RetryAnalyzer.class)
    public void findFromWebElementWithoutConditionWithDefinedTimeTest() {
        WebElement parent = seleniumSteps.find(webElement(tagName(BUTTON_TAG)));
        setStartBenchMark();
        try {
            seleniumSteps.find(webElement(CLASS_THAT_DOES_NOT_EXIST)
                    .timeOut(ONE_SECOND)
                    .foundFrom(parent));
        }
        catch (Exception e) {
            setEndBenchMark();
            assertThat(e.getMessage(), containsString(expectedDescriptionOfNotFoundElementError()));
            throw e;
        }
        finally {
            assertThat(getTimeDifference(), greaterThanOrEqualTo(ONE_SECOND.toMillis()));
            assertThat(getTimeDifference() - ONE_SECOND.toMillis(), lessThan(HALF_SECOND.toMillis()));
        }
    }

    @Test(expectedExceptions = NoSuchElementException.class, retryAnalyzer = RetryAnalyzer.class)
    public void findFromWebElementWithoutConditionWithTimeDefinedImplicitlyTest() {
        WebElement parent = seleniumSteps.find(webElement(tagName(BUTTON_TAG)));
        setProperty(ELEMENT_WAITING_TIME_UNIT.getPropertyName(), "SECONDS");
        setProperty(ELEMENT_WAITING_TIME_VALUE.getPropertyName(), "1");
        setStartBenchMark();
        try {
            seleniumSteps.find(webElement(CLASS_THAT_DOES_NOT_EXIST)
                    .foundFrom(parent));
        }
        catch (Exception e) {
            setEndBenchMark();
            assertThat(e.getMessage(), containsString(expectedDescriptionOfNotFoundElementError()));
            throw e;
        }
        finally {
            removeProperty(ELEMENT_WAITING_TIME_UNIT.getPropertyName());
            removeProperty(ELEMENT_WAITING_TIME_VALUE.getPropertyName());
            assertThat(getTimeDifference(), greaterThanOrEqualTo(ONE_SECOND.toMillis()));
            assertThat(getTimeDifference() - ONE_SECOND.toMillis(), lessThan(HALF_SECOND.toMillis()));
        }
    }

    @Test(expectedExceptions = NoSuchElementException.class, retryAnalyzer = RetryAnalyzer.class)
    public void findFromWebElementOnlyVisibleImplicitConditionAndDefinedTimeTest() {
        setProperty(FIND_ONLY_VISIBLE_ELEMENTS_WHEN_NO_CONDITION.getPropertyName(), "true");
        WebElement parent = seleniumSteps.find(webElement(className(SPREAD_SHEET_CLASS)));
        setStartBenchMark();
        try {
            seleniumSteps.find(webElement(INVISIBLE_SPAN_BY)
                    .timeOut(ONE_SECOND)
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

    @Test(expectedExceptions = NoSuchElementException.class, retryAnalyzer = RetryAnalyzer.class)
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

    @Test(expectedExceptions = NoSuchElementException.class, retryAnalyzer = RetryAnalyzer.class)
    public void findFromWebElementOnlyVisibleImplicitConditionAndTimeConflictTest() {
        setProperty(FIND_ONLY_VISIBLE_ELEMENTS_WHEN_NO_CONDITION.getPropertyName(), "true");
        setProperty(ELEMENT_WAITING_TIME_UNIT.getPropertyName(), "SECONDS");
        setProperty(ELEMENT_WAITING_TIME_VALUE.getPropertyName(), "5");
        WebElement parent = seleniumSteps.find(webElement(className(SPREAD_SHEET_CLASS)));
        setStartBenchMark();
        try {
            seleniumSteps.find(webElement(INVISIBLE_SPAN_BY)
                    .timeOut(ONE_SECOND)
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

                {VISIBLE_DIV_BY, not(shouldBeEnabled()),
                        expectedDescriptionOfNotFoundElementError(VISIBLE_DIV_BY,
                                not(shouldBeEnabled()))},

                {INVISIBLE_SPAN_BY, shouldBeVisible(),
                        expectedDescriptionOfNotFoundElementError(INVISIBLE_SPAN_BY, shouldBeVisible())},

                {VISIBLE_DIV_BY, not(shouldBeVisible()),
                        expectedDescriptionOfNotFoundElementError(VISIBLE_DIV_BY,
                                not(shouldBeVisible()))},

                {className(MULTI_SELECT_CLASS), shouldContainElements(webElements(className(ITEM_OPTION_CLASS)).timeOut(ofMillis(10)), 4),
                        expectedDescriptionOfNotFoundElementError(className(MULTI_SELECT_CLASS),
                                shouldContainElements(webElements(className(ITEM_OPTION_CLASS)).timeOut(ofMillis(10)), 4))},

                {className(MULTI_SELECT_CLASS),
                        not(shouldContainElements(webElements(className(ITEM_OPTION_CLASS)).timeOut(ofMillis(10)), 3)
                                .or(shouldContainElements(webElements(className(ITEM_OPTION_CLASS)).timeOut(ofMillis(10)), 2))),

                        expectedDescriptionOfNotFoundElementError(className(MULTI_SELECT_CLASS),
                                not(shouldContainElements(webElements(className(ITEM_OPTION_CLASS))
                                        .timeOut(ofMillis(10)), 3)
                                        .or(shouldContainElements(webElements(className(ITEM_OPTION_CLASS))
                                                .timeOut(ofMillis(10)), 2))))},

                {className(MULTI_SELECT_CLASS),
                        shouldContainElements(webElements(tagName(OPTION)).timeOut(ofMillis(50))),

                        expectedDescriptionOfNotFoundElementError(className(MULTI_SELECT_CLASS),
                                shouldContainElements(webElements(tagName(OPTION)).timeOut(ofMillis(50))))},

                {tagName(SELECT), not(shouldContainElements(webElements(tagName(OPTION)).timeOut(ofMillis(50)))),

                        expectedDescriptionOfNotFoundElementError(tagName(SELECT),
                                not(shouldContainElements(webElements(tagName(OPTION)).timeOut(ofMillis(50)))))},

                {tagName(TEXT_AREA_TAG), shouldHaveAttribute(ATTR11, VALUE10),
                        expectedDescriptionOfNotFoundElementError(tagName(TEXT_AREA_TAG),
                                shouldHaveAttribute(ATTR11, VALUE10))},

                {tagName(TEXT_AREA_TAG), not(shouldHaveAttribute(ATTR11, VALUE12)
                        .or(shouldHaveAttribute(ATTR11, VALUE13))
                        .or(shouldHaveAttribute(ATTR11, VALUE14))
                        .or(shouldHaveAttribute(ATTR11, VALUE15))),

                        expectedDescriptionOfNotFoundElementError(tagName(TEXT_AREA_TAG),
                                not(shouldHaveAttribute(ATTR11, VALUE12)
                                        .or(shouldHaveAttribute(ATTR11, VALUE13))
                                        .or(shouldHaveAttribute(ATTR11, VALUE14))
                                        .or(shouldHaveAttribute(ATTR11, VALUE15))))},

                {tagName(TEXT_AREA_TAG), shouldHaveAttributeContains(ATTR11, "10"),
                        expectedDescriptionOfNotFoundElementError(tagName(TEXT_AREA_TAG),
                                shouldHaveAttributeContains(ATTR11, "10"))},

                {tagName(TEXT_AREA_TAG), not(shouldHaveAttributeContains(ATTR11, "12")
                        .or(shouldHaveAttributeContains(ATTR11, "13"))
                        .or(shouldHaveAttributeContains(ATTR11, VALUE14))
                        .or(shouldHaveAttributeContains(ATTR11, VALUE15))),

                        expectedDescriptionOfNotFoundElementError(tagName(TEXT_AREA_TAG),
                                not(shouldHaveAttributeContains(ATTR11, "12")
                                        .or(shouldHaveAttributeContains(ATTR11, "13"))
                                        .or(shouldHaveAttributeContains(ATTR11, VALUE14))
                                        .or(shouldHaveAttributeContains(ATTR11, VALUE15))))},

                {tagName(TEXT_AREA_TAG), shouldHaveAttributeContains(ATTR11, compile("10")),
                        expectedDescriptionOfNotFoundElementError(tagName(TEXT_AREA_TAG),
                                shouldHaveAttributeContains(ATTR11, compile("10")))},

                {tagName(TEXT_AREA_TAG), not(shouldHaveAttributeContains(ATTR11, compile("12"))
                        .or(shouldHaveAttributeContains(ATTR11, compile("13")))
                        .or(shouldHaveAttributeContains(ATTR11, compile(VALUE14)))
                        .or(shouldHaveAttributeContains(ATTR11, compile(VALUE15)))),

                        expectedDescriptionOfNotFoundElementError(tagName(TEXT_AREA_TAG),
                                not(shouldHaveAttributeContains(ATTR11, compile("12"))
                                        .or(shouldHaveAttributeContains(ATTR11, compile("13")))
                                        .or(shouldHaveAttributeContains(ATTR11, compile(VALUE14)))
                                        .or(shouldHaveAttributeContains(ATTR11, compile(VALUE15)))))},

                {xpath(RADIO_BUTTON_XPATH), shouldHaveCssValue(CSS18, CSS_VALUE6),
                        expectedDescriptionOfNotFoundElementError(xpath(RADIO_BUTTON_XPATH),
                                shouldHaveCssValue(CSS18, CSS_VALUE6))},

                {xpath(RADIO_BUTTON_XPATH),
                        not(shouldHaveCssValue(CSS18, CSS_VALUE7)
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
                                .or(shouldHaveCssValue(CSS18, CSS_VALUE19))),

                        expectedDescriptionOfNotFoundElementError(xpath(RADIO_BUTTON_XPATH),
                                not(shouldHaveCssValue(CSS18, CSS_VALUE7)
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
                                        .or(shouldHaveCssValue(CSS18, CSS_VALUE19))))},

                {xpath(RADIO_BUTTON_XPATH), shouldHaveCssValueContains(CSS18, "value6"),
                        expectedDescriptionOfNotFoundElementError(xpath(RADIO_BUTTON_XPATH),
                                shouldHaveCssValueContains(CSS18, "value6"))},

                {xpath(RADIO_BUTTON_XPATH),
                        not(shouldHaveCssValueContains(CSS18, "value")),
                        expectedDescriptionOfNotFoundElementError(xpath(RADIO_BUTTON_XPATH),
                                not(shouldHaveCssValueContains(CSS18, "value")))},

                {xpath(RADIO_BUTTON_XPATH), shouldHaveCssValueContains(CSS18, compile("value6")),
                        expectedDescriptionOfNotFoundElementError(xpath(RADIO_BUTTON_XPATH),
                                shouldHaveCssValueContains(CSS18, compile("value6")))},

                {xpath(RADIO_BUTTON_XPATH),
                        not(shouldHaveCssValueContains(CSS18, compile("value"))),
                        expectedDescriptionOfNotFoundElementError(xpath(RADIO_BUTTON_XPATH),
                                not(shouldHaveCssValueContains(CSS18, compile("value"))))},

                {INVISIBLE_SPAN_BY, shouldHaveText(VISIBLE_DIV),
                        expectedDescriptionOfNotFoundElementError(INVISIBLE_SPAN_BY,
                                shouldHaveText(VISIBLE_DIV))},

                {VISIBLE_DIV_BY, not(shouldHaveText(VISIBLE_DIV)),
                        expectedDescriptionOfNotFoundElementError(VISIBLE_DIV_BY,
                                not(shouldHaveText(VISIBLE_DIV)))},

                {INVISIBLE_SPAN_BY, shouldHaveText(compile("div")),
                        expectedDescriptionOfNotFoundElementError(INVISIBLE_SPAN_BY,
                                shouldHaveText(compile("div")))},

                {VISIBLE_DIV_BY, not(shouldHaveText(compile("div"))),
                        expectedDescriptionOfNotFoundElementError(VISIBLE_DIV_BY,
                                not(shouldHaveText(compile("div"))))}
        };
    }

    @Test(dataProvider = "search criteria1", expectedExceptions = NoSuchElementException.class, retryAnalyzer = RetryAnalyzer.class)
    public void findElementByCriteriaWithDefinedTimeTest(By by,
                                                         Predicate<WebElement> criteria,
                                                         String expectedErrorMessage) {
        setStartBenchMark();
        try {
            seleniumSteps.find(webElement(by)
                    .timeOut(ONE_SECOND)
                    .criteria(criteria));
        }
        catch (Exception e) {
            setEndBenchMark();
            assertThat(e.getMessage(), containsString(expectedErrorMessage));
            throw e;
        }
        finally {
            assertThat(getTimeDifference(), greaterThanOrEqualTo(ONE_SECOND.toMillis()));
            assertThat(new BigDecimal(getTimeDifference() - ONE_SECOND.toMillis()),
                    either(lessThan(new BigDecimal(HALF_SECOND.toMillis())))
                            .or(closeTo(new BigDecimal(HALF_SECOND.toMillis()), new BigDecimal(200))));
        }
    }

    @Test(dataProvider = "search criteria1", expectedExceptions = NoSuchElementException.class, retryAnalyzer = RetryAnalyzer.class)
    public void findElementByCriteriaWithTimeDefinedImplicitlyTest(By by,
                                                                   Predicate<WebElement> criteria,
                                                                   String expectedErrorMessage) {
        setProperty(ELEMENT_WAITING_TIME_UNIT.getPropertyName(), "SECONDS");
        setProperty(ELEMENT_WAITING_TIME_VALUE.getPropertyName(), "1");
        setStartBenchMark();
        try {
            seleniumSteps.find(webElement(by)
                    .criteria(criteria));
        }
        catch (Exception e) {
            setEndBenchMark();
            assertThat(e.getMessage(), containsString(expectedErrorMessage));
            throw e;
        }
        finally {
            removeProperty(ELEMENT_WAITING_TIME_UNIT.getPropertyName());
            removeProperty(ELEMENT_WAITING_TIME_VALUE.getPropertyName());
            assertThat(getTimeDifference(), greaterThanOrEqualTo(ONE_SECOND.toMillis()));
            assertThat(getTimeDifference() - ONE_SECOND.toMillis(),
                    lessThan(ONE_SECOND.toMillis()));
        }
    }

    @DataProvider(name = "search criteria2")
    public static Object[][] searchCriteriaForText() {
        return new Object[][]{
                {tagName(BUTTON_TAG), BUTTON_TEXT1, shouldBeEnabled(),

                        expectedDescriptionOfNotFoundElementError(tagName(BUTTON_TAG),
                                shouldHaveText(BUTTON_TEXT1).and(shouldBeEnabled()))},

                {tagName(BUTTON_TAG), BUTTON_TEXT3, not(shouldBeEnabled()),

                        expectedDescriptionOfNotFoundElementError(tagName(BUTTON_TAG),
                                shouldHaveText(BUTTON_TEXT3).and(not(shouldBeEnabled())))},

                {tagName(LINK_TAG), LINK_TEXT1, shouldBeVisible(),

                        expectedDescriptionOfNotFoundElementError(tagName(LINK_TAG),
                                shouldHaveText(LINK_TEXT1).and(shouldBeVisible()))},

                {tagName(LINK_TAG), LINK_TEXT2,  not(shouldBeVisible()),

                        expectedDescriptionOfNotFoundElementError(tagName(LINK_TAG),
                                shouldHaveText(LINK_TEXT2).and(not(shouldBeVisible())))},

                {tagName(SELECT), OPTION_TEXT23,
                        shouldContainElements(webElements(tagName(OPTION), OPTION_TEXT22)
                                .timeOut(ofMillis(10)), 3),

                        expectedDescriptionOfNotFoundElementError(tagName(SELECT), shouldHaveText(OPTION_TEXT23)
                                .and(shouldContainElements(webElements(tagName(OPTION), OPTION_TEXT22)
                                        .timeOut(ofMillis(10)), 3)))},

                {tagName(SELECT), OPTION_TEXT20,
                        not(shouldContainElements(webElements(tagName(OPTION))
                                .timeOut(ofMillis(10)), 3)),

                        expectedDescriptionOfNotFoundElementError(tagName(SELECT),
                                shouldHaveText(OPTION_TEXT20)
                                        .and(not(shouldContainElements(webElements(tagName(OPTION))
                                                .timeOut(ofMillis(10)), 3))))},

                {tagName(BUTTON_TAG), BUTTON_TEXT4,
                        shouldContainElements(webElements(tagName(LABEL_TAG), BUTTON_LABEL_TEXT1)
                                .timeOut(ofMillis(10))),

                        expectedDescriptionOfNotFoundElementError(tagName(BUTTON_TAG),
                                shouldHaveText(BUTTON_TEXT4)
                                        .and(shouldContainElements(webElements(tagName(LABEL_TAG), BUTTON_LABEL_TEXT1)
                                                .timeOut(ofMillis(10)))))},

                {tagName(BUTTON_TAG), BUTTON_TEXT5,
                        not(shouldContainElements(webElements(tagName(LABEL_TAG), BUTTON_LABEL_TEXT1)
                                .timeOut(ofMillis(10)))),

                        expectedDescriptionOfNotFoundElementError(tagName(BUTTON_TAG),
                                shouldHaveText(BUTTON_TEXT5)
                                        .and(not(shouldContainElements(webElements(tagName(LABEL_TAG),
                                                BUTTON_LABEL_TEXT1)
                                                .timeOut(ofMillis(10))))))},

                {CHAINED_FIND_TAB, TAB_TEXT3, shouldHaveAttribute(ATTR19, VALUE12),

                        expectedDescriptionOfNotFoundElementError(CHAINED_FIND_TAB,
                                shouldHaveText(TAB_TEXT3)
                                        .and(shouldHaveAttribute(ATTR19, VALUE12)))},

                {CHAINED_FIND_TAB, TAB_TEXT2, not(shouldHaveAttribute(ATTR19, VALUE12)),

                        expectedDescriptionOfNotFoundElementError(CHAINED_FIND_TAB,
                                shouldHaveText(TAB_TEXT2)
                                        .and(not(shouldHaveAttribute(ATTR19, VALUE12))))},

                {CHAINED_FIND_TAB, TAB_TEXT4, shouldHaveAttributeContains(ATTR20, VALUE14),

                        expectedDescriptionOfNotFoundElementError(CHAINED_FIND_TAB,
                                shouldHaveText(TAB_TEXT4).and(shouldHaveAttributeContains(ATTR20, VALUE14)))},

                {CHAINED_FIND_TAB, TAB_TEXT3, not(shouldHaveAttributeContains(ATTR20, VALUE14)),

                        expectedDescriptionOfNotFoundElementError(CHAINED_FIND_TAB,
                                shouldHaveText(TAB_TEXT3)
                                        .and(not(shouldHaveAttributeContains(ATTR20, VALUE14))))},

                {CHAINED_FIND_TAB, TAB_TEXT3, shouldHaveAttributeContains(ATTR20, compile(VALUE12)),
                        expectedDescriptionOfNotFoundElementError(CHAINED_FIND_TAB,
                                shouldHaveText(TAB_TEXT3)
                                        .and(shouldHaveAttributeContains(ATTR20, compile(VALUE12))))},

                {CHAINED_FIND_TAB, TAB_TEXT1, not(shouldHaveAttributeContains(ATTR20, compile(VALUE12))),

                        expectedDescriptionOfNotFoundElementError(CHAINED_FIND_TAB,
                                shouldHaveText(TAB_TEXT1)
                                        .and(not(shouldHaveAttributeContains(ATTR20, compile(VALUE12)))))},

                {xpath(TEXT_FIELD_XPATH), INPUT_TEXT4, shouldHaveCssValue(CSS8, CSS_VALUE6),

                        expectedDescriptionOfNotFoundElementError(xpath(TEXT_FIELD_XPATH),
                                shouldHaveText(INPUT_TEXT4)
                                        .and(shouldHaveCssValue(CSS8, CSS_VALUE6)))},

                {xpath(TEXT_FIELD_XPATH), INPUT_TEXT3, not(shouldHaveCssValue(CSS8, CSS_VALUE6)),

                        expectedDescriptionOfNotFoundElementError(xpath(TEXT_FIELD_XPATH),
                                shouldHaveText(INPUT_TEXT3)
                                        .and(not(shouldHaveCssValue(CSS8, CSS_VALUE6))))},

                {xpath(TEXT_FIELD_XPATH), INPUT_TEXT4, shouldHaveCssValueContains(CSS8, "4")
                        .and(shouldHaveCssValueContains(CSS9, "5")),

                        expectedDescriptionOfNotFoundElementError(xpath(TEXT_FIELD_XPATH),
                                shouldHaveText(INPUT_TEXT4).and(shouldHaveCssValueContains(CSS8, "4")
                                        .and(shouldHaveCssValueContains(CSS9, "5"))))},

                {xpath(TEXT_FIELD_XPATH), INPUT_TEXT1, not(shouldHaveCssValueContains(CSS8, "4")
                        .and(shouldHaveCssValueContains(CSS9, "5"))),

                        expectedDescriptionOfNotFoundElementError(xpath(TEXT_FIELD_XPATH),
                                shouldHaveText(INPUT_TEXT1).and(not(shouldHaveCssValueContains(CSS8, "4")
                                        .and(shouldHaveCssValueContains(CSS9, "5")))))},

                {xpath(TEXT_FIELD_XPATH), INPUT_TEXT4, shouldHaveCssValueContains(CSS8, compile("4"))
                        .and(shouldHaveCssValueContains(CSS9, compile("5"))),

                        expectedDescriptionOfNotFoundElementError(xpath(TEXT_FIELD_XPATH),
                                shouldHaveText(INPUT_TEXT4).and(shouldHaveCssValueContains(CSS8, compile("4"))
                                        .and(shouldHaveCssValueContains(CSS9, compile("5")))))},

                {xpath(TEXT_FIELD_XPATH), INPUT_TEXT1, not(shouldHaveCssValueContains(CSS8, compile("4"))
                        .and(shouldHaveCssValueContains(CSS9, compile("5")))),

                        expectedDescriptionOfNotFoundElementError(xpath(TEXT_FIELD_XPATH),
                                shouldHaveText(INPUT_TEXT1).and(not(shouldHaveCssValueContains(CSS8, compile("4"))
                                        .and(shouldHaveCssValueContains(CSS9, compile("5"))))))},
        };
    }

    @Test(dataProvider = "search criteria2", expectedExceptions = NoSuchElementException.class, retryAnalyzer = RetryAnalyzer.class)
    public void findElementByCriteriaAndTextWithDefinedTimeTest(By by,
                                                                String text,
                                                                Predicate<WebElement> criteria,
                                                                String expectedErrorMessage) {
        setStartBenchMark();
        try {
            seleniumSteps.find(webElement(by, text)
                    .timeOut(ONE_SECOND)
                    .criteria(criteria));
        }
        catch (Exception e) {
            setEndBenchMark();
            assertThat(e.getMessage(), containsString(expectedErrorMessage));
            throw e;
        }
        finally {
            assertThat(getTimeDifference(), greaterThanOrEqualTo(ONE_SECOND.toMillis()));
            assertThat(getTimeDifference() - ONE_SECOND.toMillis(),
                    lessThan(ONE_SECOND.toMillis()));
        }
    }

    @Test(dataProvider = "search criteria2", expectedExceptions = NoSuchElementException.class, retryAnalyzer = RetryAnalyzer.class)
    public void findElementByCriteriaAndTextWithTimeDefinedImplicitlyTest(By by,
                                                                          String text,
                                                                          Predicate<WebElement> criteria,
                                                                          String expectedErrorMessage) {
        setProperty(ELEMENT_WAITING_TIME_UNIT.getPropertyName(), "SECONDS");
        setProperty(ELEMENT_WAITING_TIME_VALUE.getPropertyName(), "1");
        setStartBenchMark();
        try {
            seleniumSteps.find(webElement(by, text)
                    .criteria(criteria));
        }
        catch (Exception e) {
            setEndBenchMark();
            assertThat(e.getMessage(), containsString(expectedErrorMessage));
            throw e;
        }
        finally {
            removeProperty(ELEMENT_WAITING_TIME_UNIT.getPropertyName());
            removeProperty(ELEMENT_WAITING_TIME_VALUE.getPropertyName());
            assertThat(getTimeDifference(), greaterThanOrEqualTo(ONE_SECOND.toMillis()));
            assertThat(getTimeDifference() - ONE_SECOND.toMillis(),
                    lessThan(ONE_SECOND.toMillis()));
        }
    }

    @DataProvider(name = "search criteria3")
    public static Object[][] searchCriteriaForTextPattern() {
        return new Object[][]{
                {tagName(BUTTON_TAG), compile("Text1"), shouldBeEnabled(),
                        expectedDescriptionOfNotFoundElementError(tagName(BUTTON_TAG),
                                shouldHaveText(compile("Text1")).and(shouldBeEnabled()))},

                {tagName(BUTTON_TAG), compile("Text3"), not(shouldBeEnabled()),
                        expectedDescriptionOfNotFoundElementError(tagName(BUTTON_TAG),
                                shouldHaveText(compile("Text3")).and(not(shouldBeEnabled())))},

                {tagName(LINK_TAG), compile("Text1"), shouldBeVisible(),
                        expectedDescriptionOfNotFoundElementError(tagName(LINK_TAG),
                                shouldHaveText(compile("Text1")).and(shouldBeVisible()))},

                {tagName(LINK_TAG), compile("Text2"),  not(shouldBeVisible()),
                        expectedDescriptionOfNotFoundElementError(tagName(LINK_TAG),
                                shouldHaveText(compile("Text2")).and(not(shouldBeVisible())))},

                {tagName(SELECT), compile("Text23"),
                        shouldContainElements(webElements(tagName(OPTION), OPTION_TEXT22)
                                .timeOut(ofMillis(10)), 3),

                        expectedDescriptionOfNotFoundElementError(tagName(SELECT), shouldHaveText(compile("Text23"))
                                .and(shouldContainElements(webElements(tagName(OPTION), OPTION_TEXT22)
                                        .timeOut(ofMillis(10)), 3)))},

                {tagName(SELECT), compile(OPTION_TEXT20),
                        not(shouldContainElements(webElements(tagName(OPTION))
                                .timeOut(ofMillis(10)), 3)),

                        expectedDescriptionOfNotFoundElementError(tagName(SELECT),
                                shouldHaveText(compile(OPTION_TEXT20))
                                        .and(not(shouldContainElements(webElements(tagName(OPTION))
                                                .timeOut(ofMillis(10)), 3))))},

                {tagName(BUTTON_TAG), compile("Text4"),
                        shouldContainElements(webElements(tagName(LABEL_TAG), BUTTON_LABEL_TEXT1).timeOut(ofMillis(10))),

                        expectedDescriptionOfNotFoundElementError(tagName(BUTTON_TAG),
                                shouldHaveText(compile("Text4"))
                                        .and(shouldContainElements(webElements(tagName(LABEL_TAG), BUTTON_LABEL_TEXT1)
                                                .timeOut(ofMillis(10)))))},

                {tagName(BUTTON_TAG), compile("Text5"),
                        not(shouldContainElements(webElements(tagName(LABEL_TAG), BUTTON_LABEL_TEXT1)
                                .timeOut(ofMillis(10)))),

                        expectedDescriptionOfNotFoundElementError(tagName(BUTTON_TAG),
                                shouldHaveText(compile("Text5"))
                                        .and(not(shouldContainElements(webElements(tagName(LABEL_TAG),
                                                BUTTON_LABEL_TEXT1)
                                                .timeOut(ofMillis(10))))))},

                {CHAINED_FIND_TAB, compile("Text3"), shouldHaveAttribute(ATTR19, VALUE12),

                        expectedDescriptionOfNotFoundElementError(CHAINED_FIND_TAB,
                                shouldHaveText(compile("Text3"))
                                        .and(shouldHaveAttribute(ATTR19, VALUE12)))},

                {CHAINED_FIND_TAB, compile("Text2"), not(shouldHaveAttribute(ATTR19, VALUE12)),

                        expectedDescriptionOfNotFoundElementError(CHAINED_FIND_TAB,
                                shouldHaveText(compile("Text2"))
                                        .and(not(shouldHaveAttribute(ATTR19, VALUE12))))},

                {CHAINED_FIND_TAB, compile("Text4"), shouldHaveAttributeContains(ATTR20, VALUE14),

                        expectedDescriptionOfNotFoundElementError(CHAINED_FIND_TAB,
                                shouldHaveText(compile("Text4")).and(shouldHaveAttributeContains(ATTR20, VALUE14)))},

                {CHAINED_FIND_TAB, compile("Text3"), not(shouldHaveAttributeContains(ATTR20, VALUE14)),

                        expectedDescriptionOfNotFoundElementError(CHAINED_FIND_TAB,
                                shouldHaveText(compile("Text3"))
                                        .and(not(shouldHaveAttributeContains(ATTR20, VALUE14))))},

                {CHAINED_FIND_TAB, compile("Text3"), shouldHaveAttributeContains(ATTR20, compile(VALUE12)),

                        expectedDescriptionOfNotFoundElementError(CHAINED_FIND_TAB,
                                shouldHaveText(compile("Text3"))
                                        .and(shouldHaveAttributeContains(ATTR20, compile(VALUE12))))},

                {CHAINED_FIND_TAB, compile(TAB_TEXT1), not(shouldHaveAttributeContains(ATTR20, compile(VALUE12))),

                        expectedDescriptionOfNotFoundElementError(CHAINED_FIND_TAB,
                                shouldHaveText(compile(TAB_TEXT1))
                                        .and(not(shouldHaveAttributeContains(ATTR20, compile(VALUE12)))))},

                {xpath(TEXT_FIELD_XPATH), compile(INPUT_TEXT4), shouldHaveCssValue(CSS8, CSS_VALUE6),

                        expectedDescriptionOfNotFoundElementError(xpath(TEXT_FIELD_XPATH),
                                shouldHaveText(compile(INPUT_TEXT4))
                                        .and(shouldHaveCssValue(CSS8, CSS_VALUE6)))},

                {xpath(TEXT_FIELD_XPATH), compile(INPUT_TEXT3), not(shouldHaveCssValue(CSS8, CSS_VALUE6)),

                        expectedDescriptionOfNotFoundElementError(xpath(TEXT_FIELD_XPATH),
                                shouldHaveText(compile(INPUT_TEXT3))
                                        .and(not(shouldHaveCssValue(CSS8, CSS_VALUE6))))},

                {xpath(TEXT_FIELD_XPATH), compile(INPUT_TEXT4), shouldHaveCssValueContains(CSS8, "4")
                        .and(shouldHaveCssValueContains(CSS9, "5")),

                        expectedDescriptionOfNotFoundElementError(xpath(TEXT_FIELD_XPATH),
                                shouldHaveText(compile(INPUT_TEXT4)).and(shouldHaveCssValueContains(CSS8, "4")
                                        .and(shouldHaveCssValueContains(CSS9, "5"))))},

                {xpath(TEXT_FIELD_XPATH), compile(INPUT_TEXT1), shouldHaveCssValueContains(CSS8, "4")
                        .and(not(shouldHaveCssValueContains(CSS9, "5"))),

                        expectedDescriptionOfNotFoundElementError(xpath(TEXT_FIELD_XPATH),
                                shouldHaveText(compile(INPUT_TEXT1)).and(shouldHaveCssValueContains(CSS8, "4")
                                        .and(not(shouldHaveCssValueContains(CSS9, "5")))))},

                {xpath(TEXT_FIELD_XPATH), compile(INPUT_TEXT4), shouldHaveCssValueContains(CSS8, compile("4"))
                        .and(shouldHaveCssValueContains(CSS9, compile("5"))),

                        expectedDescriptionOfNotFoundElementError(xpath(TEXT_FIELD_XPATH),
                                shouldHaveText(compile(INPUT_TEXT4)).and(shouldHaveCssValueContains(CSS8, compile("4"))
                                        .and(shouldHaveCssValueContains(CSS9, compile("5")))))},

                {xpath(TEXT_FIELD_XPATH), compile(INPUT_TEXT1), shouldHaveCssValueContains(CSS8, compile("4"))
                        .and(not(shouldHaveCssValueContains(CSS9, compile("5")))),

                        expectedDescriptionOfNotFoundElementError(xpath(TEXT_FIELD_XPATH),
                                shouldHaveText(compile(INPUT_TEXT1)).and(shouldHaveCssValueContains(CSS8, compile("4"))
                                        .and(not(shouldHaveCssValueContains(CSS9, compile("5"))))))},
        };
    }

    @Test(dataProvider = "search criteria3", expectedExceptions = NoSuchElementException.class, retryAnalyzer = RetryAnalyzer.class)
    public void findElementByCriteriaAndTexPatternWithDefinedTimeTest(By by,
                                                                      Pattern pattern,
                                                                      Predicate<WebElement> criteria,
                                                                      String expectedErrorMessage) {
        setStartBenchMark();
        try {
            seleniumSteps.find(webElement(by, pattern)
                    .timeOut(ONE_SECOND)
                    .criteria(criteria));
        }
        catch (Exception e) {
            setEndBenchMark();
            assertThat(e.getMessage(), containsString(expectedErrorMessage));
            throw e;
        }
        finally {
            assertThat(getTimeDifference(), greaterThanOrEqualTo(ONE_SECOND.toMillis()));
            assertThat(getTimeDifference() - ONE_SECOND.toMillis(),
                    lessThan(ONE_SECOND.toMillis()));
        }
    }

    @Test(dataProvider = "search criteria3", expectedExceptions = NoSuchElementException.class, retryAnalyzer = RetryAnalyzer.class)
    public void findElementByCriteriaAndTexPatternWithTimeDefinedImplicitlyTest(By by,
                                                                                Pattern pattern,
                                                                                Predicate<WebElement> criteria,
                                                                                String expectedErrorMessage) {
        setProperty(ELEMENT_WAITING_TIME_UNIT.getPropertyName(), "SECONDS");
        setProperty(ELEMENT_WAITING_TIME_VALUE.getPropertyName(), "1");
        setStartBenchMark();
        try {
            seleniumSteps.find(webElement(by, pattern).criteria(criteria));
        }
        catch (Exception e) {
            setEndBenchMark();
            assertThat(e.getMessage(), containsString(expectedErrorMessage));
            throw e;
        }
        finally {
            removeProperty(ELEMENT_WAITING_TIME_UNIT.getPropertyName());
            removeProperty(ELEMENT_WAITING_TIME_VALUE.getPropertyName());
            assertThat(getTimeDifference(), greaterThanOrEqualTo(ONE_SECOND.toMillis()));
            assertThat(getTimeDifference() - ONE_SECOND.toMillis(),
                    lessThan(ONE_SECOND.toMillis()));
        }
    }
}
