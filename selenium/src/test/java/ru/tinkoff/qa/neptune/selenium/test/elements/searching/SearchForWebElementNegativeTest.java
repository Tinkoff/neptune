package ru.tinkoff.qa.neptune.selenium.test.elements.searching;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import ru.tinkoff.qa.neptune.core.api.steps.Criteria;
import ru.tinkoff.qa.neptune.selenium.test.BaseWebDriverTest;
import ru.tinkoff.qa.neptune.selenium.test.RetryAnalyzer;

import java.math.BigDecimal;

import static java.lang.String.format;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.openqa.selenium.By.*;
import static ru.tinkoff.qa.neptune.core.api.steps.Criteria.NOT;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.CommonElementCriteria.*;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.MultipleSearchSupplier.webElements;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.SearchSupplier.webElement;
import static ru.tinkoff.qa.neptune.selenium.properties.SessionFlagProperties.FIND_ONLY_VISIBLE_ELEMENTS;
import static ru.tinkoff.qa.neptune.selenium.properties.WaitingProperties.TimeUnitProperties.ELEMENT_WAITING_TIME_UNIT;
import static ru.tinkoff.qa.neptune.selenium.properties.WaitingProperties.TimeValueProperties.ELEMENT_WAITING_TIME_VALUE;
import static ru.tinkoff.qa.neptune.selenium.test.FakeDOMModel.*;

public class SearchForWebElementNegativeTest extends BaseWebDriverTest {

    private static final String FOUND_BY_PATTERN_ERROR = "Nothing was found. Attempt to get Web element located %s";
    private static final String FOUND_ON_CONDITION_ERROR = FOUND_BY_PATTERN_ERROR + ". Criteria: %s";
    private static final By CLASS_THAT_DOES_NOT_EXIST = className("fakeClass");

    private static String expectedDescriptionOfNotFoundElementError(By by, Criteria<? extends SearchContext> condition) {
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
        } catch (Exception e) {
            setEndBenchMark();
            assertThat(e.getMessage(), containsString(expectedDescriptionOfNotFoundElementError()));
            throw e;
        } finally {
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
        } catch (Exception e) {
            setEndBenchMark();
            assertThat(e.getMessage(), containsString(expectedDescriptionOfNotFoundElementError()));
            throw e;
        } finally {
            removeProperty(ELEMENT_WAITING_TIME_UNIT.getPropertyName());
            removeProperty(ELEMENT_WAITING_TIME_VALUE.getPropertyName());
            assertThat(getTimeDifference(), greaterThanOrEqualTo(ONE_SECOND.toMillis()));
            assertThat(getTimeDifference() - ONE_SECOND.toMillis(), lessThan(HALF_SECOND.toMillis()));
        }
    }

    @Test(expectedExceptions = NoSuchElementException.class, retryAnalyzer = RetryAnalyzer.class)
    public void findWebElementFirstLevelOnlyVisibleImplicitConditionAndDefinedTimeTest() {
        setProperty(FIND_ONLY_VISIBLE_ELEMENTS.getPropertyName(), "true");
        setStartBenchMark();
        try {
            seleniumSteps.find(webElement(INVISIBLE_SPAN_BY)
                    .timeOut(ONE_SECOND));
        } catch (Exception e) {
            setEndBenchMark();
            assertThat(e.getMessage(), containsString(expectedDescriptionOfNotFoundElementError(INVISIBLE_SPAN_BY,
                    visible())));
            throw e;
        } finally {
            removeProperty(FIND_ONLY_VISIBLE_ELEMENTS.getPropertyName());
            assertThat(getTimeDifference(), greaterThanOrEqualTo(ONE_SECOND.toMillis()));
            assertThat(getTimeDifference() - ONE_SECOND.toMillis(), lessThan(HALF_SECOND.toMillis()));
        }
    }

    @Test(expectedExceptions = NoSuchElementException.class, retryAnalyzer = RetryAnalyzer.class)
    public void findWebElementFirstLevelOnlyVisibleImplicitConditionAndImplicitTimeTest() {
        setProperty(FIND_ONLY_VISIBLE_ELEMENTS.getPropertyName(), "true");
        setProperty(ELEMENT_WAITING_TIME_UNIT.getPropertyName(), "SECONDS");
        setProperty(ELEMENT_WAITING_TIME_VALUE.getPropertyName(), "1");
        setStartBenchMark();
        try {
            seleniumSteps.find(webElement(INVISIBLE_SPAN_BY));
        } catch (Exception e) {
            setEndBenchMark();
            assertThat(e.getMessage(), containsString(expectedDescriptionOfNotFoundElementError(INVISIBLE_SPAN_BY,
                    visible())));
            throw e;
        } finally {
            removeProperty(FIND_ONLY_VISIBLE_ELEMENTS.getPropertyName());
            removeProperty(ELEMENT_WAITING_TIME_UNIT.getPropertyName());
            removeProperty(ELEMENT_WAITING_TIME_VALUE.getPropertyName());
            assertThat(getTimeDifference(), greaterThanOrEqualTo(ONE_SECOND.toMillis()));
            assertThat(getTimeDifference() - ONE_SECOND.toMillis(), lessThan(HALF_SECOND.toMillis()));
        }
    }

    @Test(expectedExceptions = NoSuchElementException.class, retryAnalyzer = RetryAnalyzer.class)
    public void findWebElementFirstLevelOnlyVisibleImplicitConditionAndTimeConflictTest() {
        setProperty(FIND_ONLY_VISIBLE_ELEMENTS.getPropertyName(), "true");
        setProperty(ELEMENT_WAITING_TIME_UNIT.getPropertyName(), "SECONDS");
        setProperty(ELEMENT_WAITING_TIME_VALUE.getPropertyName(), "5");
        setStartBenchMark();
        try {
            seleniumSteps.find(webElement(INVISIBLE_SPAN_BY)
                    .timeOut(ONE_SECOND));
        } catch (Exception e) {
            setEndBenchMark();
            assertThat(e.getMessage(), containsString(expectedDescriptionOfNotFoundElementError(INVISIBLE_SPAN_BY,
                    visible())));
            throw e;
        } finally {
            removeProperty(FIND_ONLY_VISIBLE_ELEMENTS.getPropertyName());
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
        } catch (Exception e) {
            setEndBenchMark();
            assertThat(e.getMessage(), containsString(expectedDescriptionOfNotFoundElementError()));
            throw e;
        } finally {
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
        } catch (Exception e) {
            setEndBenchMark();
            assertThat(e.getMessage(), containsString(expectedDescriptionOfNotFoundElementError()));
            throw e;
        } finally {
            removeProperty(ELEMENT_WAITING_TIME_UNIT.getPropertyName());
            removeProperty(ELEMENT_WAITING_TIME_VALUE.getPropertyName());
            assertThat(getTimeDifference(), greaterThanOrEqualTo(ONE_SECOND.toMillis()));
            assertThat(getTimeDifference() - ONE_SECOND.toMillis(), lessThan(HALF_SECOND.toMillis()));
        }
    }

    @Test(expectedExceptions = NoSuchElementException.class, retryAnalyzer = RetryAnalyzer.class)
    public void findWebElementChainedOnlyVisibleImplicitConditionAndDefinedTimeTest() {
        setProperty(FIND_ONLY_VISIBLE_ELEMENTS.getPropertyName(), "true");
        setStartBenchMark();
        try {
            seleniumSteps.find(webElement(INVISIBLE_SPAN_BY)
                    .timeOut(ONE_SECOND)
                    .foundFrom(webElement(className(SPREAD_SHEET_CLASS))));
        } catch (Exception e) {
            setEndBenchMark();
            assertThat(e.getMessage(), containsString(expectedDescriptionOfNotFoundElementError(INVISIBLE_SPAN_BY,
                    visible())));
            throw e;
        } finally {
            removeProperty(FIND_ONLY_VISIBLE_ELEMENTS.getPropertyName());
            assertThat(getTimeDifference(), greaterThanOrEqualTo(ONE_SECOND.toMillis()));
            assertThat(getTimeDifference() - ONE_SECOND.toMillis(), lessThan(HALF_SECOND.toMillis()));
        }
    }

    @Test(expectedExceptions = NoSuchElementException.class, retryAnalyzer = RetryAnalyzer.class)
    public void findWebElementChainedOnlyVisibleImplicitConditionAndImplicitTimeTest() {
        setProperty(FIND_ONLY_VISIBLE_ELEMENTS.getPropertyName(), "true");
        setProperty(ELEMENT_WAITING_TIME_UNIT.getPropertyName(), "SECONDS");
        setProperty(ELEMENT_WAITING_TIME_VALUE.getPropertyName(), "1");
        setStartBenchMark();
        try {
            seleniumSteps.find(webElement(INVISIBLE_SPAN_BY)
                    .foundFrom(webElement(className(SPREAD_SHEET_CLASS))));
        } catch (Exception e) {
            setEndBenchMark();
            assertThat(e.getMessage(), containsString(expectedDescriptionOfNotFoundElementError(INVISIBLE_SPAN_BY,
                    visible())));
            throw e;
        } finally {
            removeProperty(FIND_ONLY_VISIBLE_ELEMENTS.getPropertyName());
            removeProperty(ELEMENT_WAITING_TIME_UNIT.getPropertyName());
            removeProperty(ELEMENT_WAITING_TIME_VALUE.getPropertyName());
            assertThat(getTimeDifference(), greaterThanOrEqualTo(ONE_SECOND.toMillis()));
            assertThat(getTimeDifference() - ONE_SECOND.toMillis(), lessThan(HALF_SECOND.toMillis()));
        }
    }

    @Test(expectedExceptions = NoSuchElementException.class, retryAnalyzer = RetryAnalyzer.class)
    public void findWebElementChainedOnlyVisibleImplicitConditionAndTimeConflictTest() {
        setProperty(FIND_ONLY_VISIBLE_ELEMENTS.getPropertyName(), "true");
        setProperty(ELEMENT_WAITING_TIME_UNIT.getPropertyName(), "SECONDS");
        setProperty(ELEMENT_WAITING_TIME_VALUE.getPropertyName(), "5");
        setStartBenchMark();
        try {
            seleniumSteps.find(webElement(INVISIBLE_SPAN_BY)
                    .timeOut(ONE_SECOND)
                    .foundFrom(webElement(className(SPREAD_SHEET_CLASS))));
        } catch (Exception e) {
            setEndBenchMark();
            assertThat(e.getMessage(), containsString(expectedDescriptionOfNotFoundElementError(INVISIBLE_SPAN_BY,
                    visible())));
            throw e;
        } finally {
            removeProperty(FIND_ONLY_VISIBLE_ELEMENTS.getPropertyName());
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
        } catch (Exception e) {
            setEndBenchMark();
            assertThat(e.getMessage(), containsString(expectedDescriptionOfNotFoundElementError()));
            throw e;
        } finally {
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
        } catch (Exception e) {
            setEndBenchMark();
            assertThat(e.getMessage(), containsString(expectedDescriptionOfNotFoundElementError()));
            throw e;
        } finally {
            removeProperty(ELEMENT_WAITING_TIME_UNIT.getPropertyName());
            removeProperty(ELEMENT_WAITING_TIME_VALUE.getPropertyName());
            assertThat(getTimeDifference(), greaterThanOrEqualTo(ONE_SECOND.toMillis()));
            assertThat(getTimeDifference() - ONE_SECOND.toMillis(), lessThan(HALF_SECOND.toMillis()));
        }
    }

    @Test(expectedExceptions = NoSuchElementException.class, retryAnalyzer = RetryAnalyzer.class)
    public void findFromWebElementOnlyVisibleImplicitConditionAndDefinedTimeTest() {
        setProperty(FIND_ONLY_VISIBLE_ELEMENTS.getPropertyName(), "true");
        WebElement parent = seleniumSteps.find(webElement(className(SPREAD_SHEET_CLASS)));
        setStartBenchMark();
        try {
            seleniumSteps.find(webElement(INVISIBLE_SPAN_BY)
                    .timeOut(ONE_SECOND)
                    .foundFrom(parent));
        } catch (Exception e) {
            setEndBenchMark();
            assertThat(e.getMessage(), containsString(expectedDescriptionOfNotFoundElementError(INVISIBLE_SPAN_BY,
                    visible())));
            throw e;
        } finally {
            removeProperty(FIND_ONLY_VISIBLE_ELEMENTS.getPropertyName());
            assertThat(getTimeDifference(), greaterThanOrEqualTo(ONE_SECOND.toMillis()));
            assertThat(getTimeDifference() - ONE_SECOND.toMillis(), lessThan(HALF_SECOND.toMillis()));
        }
    }

    @Test(expectedExceptions = NoSuchElementException.class, retryAnalyzer = RetryAnalyzer.class)
    public void findFromWebElementOnlyVisibleImplicitConditionAndImplicitTimeTest() {
        setProperty(FIND_ONLY_VISIBLE_ELEMENTS.getPropertyName(), "true");
        setProperty(ELEMENT_WAITING_TIME_UNIT.getPropertyName(), "SECONDS");
        setProperty(ELEMENT_WAITING_TIME_VALUE.getPropertyName(), "1");
        WebElement parent = seleniumSteps.find(webElement(className(SPREAD_SHEET_CLASS)));
        setStartBenchMark();
        try {
            seleniumSteps.find(webElement(INVISIBLE_SPAN_BY)
                    .foundFrom(parent));
        } catch (Exception e) {
            setEndBenchMark();
            assertThat(e.getMessage(), containsString(expectedDescriptionOfNotFoundElementError(INVISIBLE_SPAN_BY,
                    visible())));
            throw e;
        } finally {
            removeProperty(FIND_ONLY_VISIBLE_ELEMENTS.getPropertyName());
            removeProperty(ELEMENT_WAITING_TIME_UNIT.getPropertyName());
            removeProperty(ELEMENT_WAITING_TIME_VALUE.getPropertyName());
            assertThat(getTimeDifference(), greaterThanOrEqualTo(ONE_SECOND.toMillis()));
            assertThat(getTimeDifference() - ONE_SECOND.toMillis(), lessThan(HALF_SECOND.toMillis()));
        }
    }

    @Test(expectedExceptions = NoSuchElementException.class, retryAnalyzer = RetryAnalyzer.class)
    public void findFromWebElementOnlyVisibleImplicitConditionAndTimeConflictTest() {
        setProperty(FIND_ONLY_VISIBLE_ELEMENTS.getPropertyName(), "true");
        setProperty(ELEMENT_WAITING_TIME_UNIT.getPropertyName(), "SECONDS");
        setProperty(ELEMENT_WAITING_TIME_VALUE.getPropertyName(), "5");
        WebElement parent = seleniumSteps.find(webElement(className(SPREAD_SHEET_CLASS)));
        setStartBenchMark();
        try {
            seleniumSteps.find(webElement(INVISIBLE_SPAN_BY)
                    .timeOut(ONE_SECOND)
                    .foundFrom(parent));
        } catch (Exception e) {
            setEndBenchMark();
            assertThat(e.getMessage(), containsString(expectedDescriptionOfNotFoundElementError(INVISIBLE_SPAN_BY,
                    visible())));
            throw e;
        } finally {
            removeProperty(FIND_ONLY_VISIBLE_ELEMENTS.getPropertyName());
            removeProperty(ELEMENT_WAITING_TIME_UNIT.getPropertyName());
            removeProperty(ELEMENT_WAITING_TIME_VALUE.getPropertyName());
            assertThat(getTimeDifference(), greaterThanOrEqualTo(ONE_SECOND.toMillis()));
            assertThat(getTimeDifference() - ONE_SECOND.toMillis(), lessThan(HALF_SECOND.toMillis()));
        }
    }

    @DataProvider(name = "search criteria1")
    public static Object[][] searchCriteria() {
        return new Object[][]{
                {INVISIBLE_SPAN_BY, enabled(),
                        expectedDescriptionOfNotFoundElementError(INVISIBLE_SPAN_BY, enabled())},

                {VISIBLE_DIV_BY, NOT(enabled()),
                        expectedDescriptionOfNotFoundElementError(VISIBLE_DIV_BY,
                                NOT(enabled()))},

                {INVISIBLE_SPAN_BY, visible(),
                        expectedDescriptionOfNotFoundElementError(INVISIBLE_SPAN_BY, visible())},

                {VISIBLE_DIV_BY, NOT(visible()),
                        expectedDescriptionOfNotFoundElementError(VISIBLE_DIV_BY,
                                NOT(visible()))},

                {className(MULTI_SELECT_CLASS), nested(webElements(className(ITEM_OPTION_CLASS)), 4),
                        expectedDescriptionOfNotFoundElementError(className(MULTI_SELECT_CLASS),
                                nested(webElements(className(ITEM_OPTION_CLASS)), 4))},

                {className(MULTI_SELECT_CLASS),
                        NOT(nested(webElements(className(ITEM_OPTION_CLASS)), 3),
                                nested(webElements(className(ITEM_OPTION_CLASS)), 2)),

                        expectedDescriptionOfNotFoundElementError(className(MULTI_SELECT_CLASS),
                                NOT(nested(webElements(className(ITEM_OPTION_CLASS)), 3),
                                        nested(webElements(className(ITEM_OPTION_CLASS)), 2)))},

                {className(MULTI_SELECT_CLASS),
                        nested(webElements(tagName(OPTION))),

                        expectedDescriptionOfNotFoundElementError(className(MULTI_SELECT_CLASS),
                                nested(webElements(tagName(OPTION))))},

                {tagName(SELECT), NOT(nested(webElements(tagName(OPTION)))),

                        expectedDescriptionOfNotFoundElementError(tagName(SELECT),
                                NOT(nested(webElements(tagName(OPTION)))))},

                {tagName(TEXT_AREA_TAG), attr(ATTR11, VALUE10),
                        expectedDescriptionOfNotFoundElementError(tagName(TEXT_AREA_TAG),
                                attr(ATTR11, VALUE10))},

                {tagName(TEXT_AREA_TAG), NOT(attr(ATTR11, VALUE12),
                        attr(ATTR11, VALUE13),
                        attr(ATTR11, VALUE14),
                        attr(ATTR11, VALUE15)),

                        expectedDescriptionOfNotFoundElementError(tagName(TEXT_AREA_TAG),
                                NOT(attr(ATTR11, VALUE12),
                                        attr(ATTR11, VALUE13),
                                        attr(ATTR11, VALUE14),
                                        attr(ATTR11, VALUE15)))},

                {tagName(TEXT_AREA_TAG), attrMatches(ATTR11, "10"),
                        expectedDescriptionOfNotFoundElementError(tagName(TEXT_AREA_TAG),
                                attrMatches(ATTR11, "10"))},

                {tagName(TEXT_AREA_TAG), NOT(attrMatches(ATTR11, "12"),
                        attrMatches(ATTR11, "13"),
                        attrMatches(ATTR11, VALUE14),
                        attrMatches(ATTR11, VALUE15)),

                        expectedDescriptionOfNotFoundElementError(tagName(TEXT_AREA_TAG),
                                NOT(attrMatches(ATTR11, "12"),
                                        attrMatches(ATTR11, "13"),
                                        attrMatches(ATTR11, VALUE14),
                                        attrMatches(ATTR11, VALUE15)))},

                {tagName(TEXT_AREA_TAG), attrMatches(ATTR11, "10"),
                        expectedDescriptionOfNotFoundElementError(tagName(TEXT_AREA_TAG),
                                attrMatches(ATTR11, "10"))},

                {tagName(TEXT_AREA_TAG), NOT(attrMatches(ATTR11, "12"),
                        attrMatches(ATTR11, "13"),
                        attrMatches(ATTR11, VALUE14),
                        attrMatches(ATTR11, VALUE15)),

                        expectedDescriptionOfNotFoundElementError(tagName(TEXT_AREA_TAG),
                                NOT(attrMatches(ATTR11, "12"),
                                        attrMatches(ATTR11, "13"),
                                        attrMatches(ATTR11, VALUE14),
                                        attrMatches(ATTR11, VALUE15)))},

                {xpath(RADIO_BUTTON_XPATH), css(CSS18, CSS_VALUE6),
                        expectedDescriptionOfNotFoundElementError(xpath(RADIO_BUTTON_XPATH),
                                css(CSS18, CSS_VALUE6))},

                {xpath(RADIO_BUTTON_XPATH),
                        NOT(css(CSS18, CSS_VALUE7),
                                css(CSS18, CSS_VALUE8),
                                css(CSS18, CSS_VALUE9),
                                css(CSS18, CSS_VALUE10),
                                css(CSS18, CSS_VALUE11),
                                css(CSS18, CSS_VALUE12),
                                css(CSS18, CSS_VALUE13),
                                css(CSS18, CSS_VALUE14),
                                css(CSS18, CSS_VALUE15),
                                css(CSS18, CSS_VALUE16),
                                css(CSS18, CSS_VALUE17),
                                css(CSS18, CSS_VALUE18),
                                css(CSS18, CSS_VALUE19)),

                        expectedDescriptionOfNotFoundElementError(xpath(RADIO_BUTTON_XPATH),
                                NOT(css(CSS18, CSS_VALUE7),
                                        css(CSS18, CSS_VALUE8),
                                        css(CSS18, CSS_VALUE9),
                                        css(CSS18, CSS_VALUE10),
                                        css(CSS18, CSS_VALUE11),
                                        css(CSS18, CSS_VALUE12),
                                        css(CSS18, CSS_VALUE13),
                                        css(CSS18, CSS_VALUE14),
                                        css(CSS18, CSS_VALUE15),
                                        css(CSS18, CSS_VALUE16),
                                        css(CSS18, CSS_VALUE17),
                                        css(CSS18, CSS_VALUE18),
                                        css(CSS18, CSS_VALUE19)))},

                {xpath(RADIO_BUTTON_XPATH), cssMatches(CSS18, "value6"),
                        expectedDescriptionOfNotFoundElementError(xpath(RADIO_BUTTON_XPATH),
                                cssMatches(CSS18, "value6"))},

                {xpath(RADIO_BUTTON_XPATH),
                        NOT(cssMatches(CSS18, "value")),
                        expectedDescriptionOfNotFoundElementError(xpath(RADIO_BUTTON_XPATH),
                                NOT(cssMatches(CSS18, "value")))},

                {xpath(RADIO_BUTTON_XPATH), cssMatches(CSS18, "value6"),
                        expectedDescriptionOfNotFoundElementError(xpath(RADIO_BUTTON_XPATH),
                                cssMatches(CSS18, "value6"))},

                {xpath(RADIO_BUTTON_XPATH),
                        NOT(cssMatches(CSS18, "value")),
                        expectedDescriptionOfNotFoundElementError(xpath(RADIO_BUTTON_XPATH),
                                NOT(cssMatches(CSS18, "value")))},

                {INVISIBLE_SPAN_BY, text(VISIBLE_DIV),
                        expectedDescriptionOfNotFoundElementError(INVISIBLE_SPAN_BY,
                                text(VISIBLE_DIV))},

                {VISIBLE_DIV_BY, NOT(text(VISIBLE_DIV)),
                        expectedDescriptionOfNotFoundElementError(VISIBLE_DIV_BY,
                                NOT(text(VISIBLE_DIV)))},

                {INVISIBLE_SPAN_BY, textMatches("div"),
                        expectedDescriptionOfNotFoundElementError(INVISIBLE_SPAN_BY,
                                textMatches("div"))},

                {VISIBLE_DIV_BY, NOT(textMatches("div")),
                        expectedDescriptionOfNotFoundElementError(VISIBLE_DIV_BY,
                                NOT(textMatches("div")))}
        };
    }

    @Test(dataProvider = "search criteria1", expectedExceptions = NoSuchElementException.class, retryAnalyzer = RetryAnalyzer.class)
    public void findElementByCriteriaWithDefinedTimeTest(By by,
                                                         Criteria<WebElement> criteria,
                                                         String expectedErrorMessage) {
        setStartBenchMark();
        try {
            seleniumSteps.find(webElement(by)
                    .timeOut(ONE_SECOND)
                    .criteria(criteria));
        } catch (Exception e) {
            setEndBenchMark();
            assertThat(e.getMessage(), containsString(expectedErrorMessage));
            throw e;
        } finally {
            assertThat(getTimeDifference(), greaterThanOrEqualTo(ONE_SECOND.toMillis()));
            assertThat(new BigDecimal(getTimeDifference() - ONE_SECOND.toMillis()),
                    either(lessThan(new BigDecimal(HALF_SECOND.toMillis())))
                            .or(closeTo(new BigDecimal(HALF_SECOND.toMillis()), new BigDecimal(200))));
        }
    }

    @Test(dataProvider = "search criteria1", expectedExceptions = NoSuchElementException.class, retryAnalyzer = RetryAnalyzer.class)
    public void findElementByCriteriaWithTimeDefinedImplicitlyTest(By by,
                                                                   Criteria<WebElement> criteria,
                                                                   String expectedErrorMessage) {
        setProperty(ELEMENT_WAITING_TIME_UNIT.getPropertyName(), "SECONDS");
        setProperty(ELEMENT_WAITING_TIME_VALUE.getPropertyName(), "1");
        setStartBenchMark();
        try {
            seleniumSteps.find(webElement(by)
                    .criteria(criteria));
        } catch (Exception e) {
            setEndBenchMark();
            assertThat(e.getMessage(), containsString(expectedErrorMessage));
            throw e;
        } finally {
            removeProperty(ELEMENT_WAITING_TIME_UNIT.getPropertyName());
            removeProperty(ELEMENT_WAITING_TIME_VALUE.getPropertyName());
            assertThat(getTimeDifference(), greaterThanOrEqualTo(ONE_SECOND.toMillis()));
            assertThat(getTimeDifference() - ONE_SECOND.toMillis(),
                    lessThan(ONE_SECOND.toMillis()));
        }
    }
}
