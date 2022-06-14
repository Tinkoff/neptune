package ru.tinkoff.qa.neptune.selenium.test.elements.searching;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import ru.tinkoff.qa.neptune.core.api.steps.Criteria;
import ru.tinkoff.qa.neptune.selenium.BaseWebDriverPreparations;
import ru.tinkoff.qa.neptune.selenium.test.RetryAnalyzer;

import java.math.BigDecimal;
import java.util.List;

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

public class SearchForWebElementsNegativeTest extends BaseWebDriverPreparations {

    private static final By CLASS_THAT_DOES_NOT_EXIST = className("fakeClass");


    @Test(retryAnalyzer = RetryAnalyzer.class)
    public void findWebElementsFirstLevelWithoutConditionWithDefinedTimeTest() {
        setStartBenchMark();
        List<WebElement> webElements = seleniumSteps.find(webElements(CLASS_THAT_DOES_NOT_EXIST)
            .timeOut(ONE_SECOND));
        setEndBenchMark();
        assertThat(getTimeDifference(), greaterThanOrEqualTo(ONE_SECOND.toMillis()));
        assertThat(getTimeDifference() - ONE_SECOND.toMillis(), lessThan(250L));
        assertThat(webElements.size(), is(0));
    }

    @Test(retryAnalyzer = RetryAnalyzer.class)
    public void findWebElementsFirstLevelWithoutConditionWithTimeDefinedImplicitlyTest() {
        setProperty(ELEMENT_WAITING_TIME_UNIT.getName(), "SECONDS");
        setProperty(ELEMENT_WAITING_TIME_VALUE.getName(), "1");
        setStartBenchMark();
        try {
            List<WebElement> webElements = seleniumSteps.find(webElements(CLASS_THAT_DOES_NOT_EXIST));
            setEndBenchMark();
            assertThat(getTimeDifference(), greaterThanOrEqualTo(ONE_SECOND.toMillis()));
            assertThat(getTimeDifference() - ONE_SECOND.toMillis(), lessThan(250L));
            assertThat(webElements.size(), is(0));
        } finally {
            removeProperty(ELEMENT_WAITING_TIME_UNIT.getName());
            removeProperty(ELEMENT_WAITING_TIME_VALUE.getName());
        }
    }

    @Test(retryAnalyzer = RetryAnalyzer.class)
    public void findWebElementsFirstLevelOnlyVisibleImplicitConditionAndDefinedTimeTest() {
        setProperty(FIND_ONLY_VISIBLE_ELEMENTS.getName(), "true");
        setStartBenchMark();
        try {
            List<WebElement> webElements = seleniumSteps.find(webElements(INVISIBLE_SPAN_BY)
                    .timeOut(ONE_SECOND));
            setEndBenchMark();
            assertThat(getTimeDifference(), greaterThanOrEqualTo(ONE_SECOND.toMillis()));
            assertThat(getTimeDifference() - ONE_SECOND.toMillis(), lessThan(250L));
            assertThat(webElements.size(), is(0));
        } finally {
            removeProperty(FIND_ONLY_VISIBLE_ELEMENTS.getName());
        }
    }

    @Test(retryAnalyzer = RetryAnalyzer.class)
    public void findWebElementsFirstLevelOnlyVisibleImplicitConditionAndImplicitTimeTest() {
        setProperty(FIND_ONLY_VISIBLE_ELEMENTS.getName(), "true");
        setProperty(ELEMENT_WAITING_TIME_UNIT.getName(), "SECONDS");
        setProperty(ELEMENT_WAITING_TIME_VALUE.getName(), "1");
        setStartBenchMark();
        try {
            List<WebElement> webElements = seleniumSteps.find(webElements(INVISIBLE_SPAN_BY));
            setEndBenchMark();
            assertThat(getTimeDifference(), greaterThanOrEqualTo(ONE_SECOND.toMillis()));
            assertThat(getTimeDifference() - ONE_SECOND.toMillis(), lessThan(250L));
            assertThat(webElements.size(), is(0));
        } finally {
            removeProperty(FIND_ONLY_VISIBLE_ELEMENTS.getName());
            removeProperty(ELEMENT_WAITING_TIME_UNIT.getName());
            removeProperty(ELEMENT_WAITING_TIME_VALUE.getName());
        }
    }

    @Test(retryAnalyzer = RetryAnalyzer.class)
    public void findWebElementsFirstLevelOnlyVisibleImplicitConditionAndTimeConflictTest() {
        setProperty(FIND_ONLY_VISIBLE_ELEMENTS.getName(), "true");
        setProperty(ELEMENT_WAITING_TIME_UNIT.getName(), "SECONDS");
        setProperty(ELEMENT_WAITING_TIME_VALUE.getName(), "5");
        setStartBenchMark();
        try {
            List<WebElement> webElements = seleniumSteps.find(webElements(INVISIBLE_SPAN_BY)
                    .timeOut(ONE_SECOND));
            setEndBenchMark();
            assertThat(getTimeDifference(), greaterThanOrEqualTo(ONE_SECOND.toMillis()));
            assertThat(getTimeDifference() - ONE_SECOND.toMillis(), lessThan(250L));
            assertThat(webElements.size(), is(0));
        } finally {
            removeProperty(FIND_ONLY_VISIBLE_ELEMENTS.getName());
            removeProperty(ELEMENT_WAITING_TIME_UNIT.getName());
            removeProperty(ELEMENT_WAITING_TIME_VALUE.getName());
        }
    }

    @Test(retryAnalyzer = RetryAnalyzer.class)
    public void findWebElementsChainedWithoutConditionWithDefinedTimeTest() {
        setStartBenchMark();
        List<WebElement> webElements = seleniumSteps.find(webElements(CLASS_THAT_DOES_NOT_EXIST)
                .timeOut(ONE_SECOND)
                .foundFrom(webElement(tagName(BUTTON_TAG))));
        setEndBenchMark();
        assertThat(getTimeDifference(), greaterThanOrEqualTo(ONE_SECOND.toMillis()));
        assertThat(getTimeDifference() - ONE_SECOND.toMillis(), lessThan(250L));
        assertThat(webElements.size(), is(0));
    }

    @Test(retryAnalyzer = RetryAnalyzer.class)
    public void findWebElementsChainedWithoutConditionWithTimeDefinedImplicitlyTest() {
        setProperty(ELEMENT_WAITING_TIME_UNIT.getName(), "SECONDS");
        setProperty(ELEMENT_WAITING_TIME_VALUE.getName(), "1");
        setStartBenchMark();
        try {
            List<WebElement> webElements = seleniumSteps.find(webElements(CLASS_THAT_DOES_NOT_EXIST)
                    .foundFrom(webElement(tagName(BUTTON_TAG))));
            setEndBenchMark();
            assertThat(getTimeDifference(), greaterThanOrEqualTo(ONE_SECOND.toMillis()));
            assertThat(getTimeDifference() - ONE_SECOND.toMillis(), lessThan(250L));
            assertThat(webElements.size(), is(0));
        } finally {
            removeProperty(ELEMENT_WAITING_TIME_UNIT.getName());
            removeProperty(ELEMENT_WAITING_TIME_VALUE.getName());
        }
    }

    @Test(retryAnalyzer = RetryAnalyzer.class)
    public void findWebElementsChainedOnlyVisibleImplicitConditionAndDefinedTimeTest() {
        setProperty(FIND_ONLY_VISIBLE_ELEMENTS.getName(), "true");
        setStartBenchMark();
        try {
            List<WebElement> webElements = seleniumSteps.find(webElements(INVISIBLE_SPAN_BY)
                    .timeOut(ONE_SECOND)
                    .foundFrom(webElement(className(SPREAD_SHEET_CLASS))));
            setEndBenchMark();
            assertThat(getTimeDifference(), greaterThanOrEqualTo(ONE_SECOND.toMillis()));
            assertThat(getTimeDifference() - ONE_SECOND.toMillis(), lessThan(250L));
            assertThat(webElements.size(), is(0));
        } finally {
            removeProperty(FIND_ONLY_VISIBLE_ELEMENTS.getName());
        }
    }

    @Test(retryAnalyzer = RetryAnalyzer.class)
    public void findWebElementsChainedOnlyVisibleImplicitConditionAndImplicitTimeTest() {
        setProperty(FIND_ONLY_VISIBLE_ELEMENTS.getName(), "true");
        setProperty(ELEMENT_WAITING_TIME_UNIT.getName(), "SECONDS");
        setProperty(ELEMENT_WAITING_TIME_VALUE.getName(), "1");
        setStartBenchMark();
        try {
            List<WebElement> webElements = seleniumSteps.find(webElements(INVISIBLE_SPAN_BY)
                    .foundFrom(webElement(className(SPREAD_SHEET_CLASS))));
            setEndBenchMark();
            assertThat(getTimeDifference(), greaterThanOrEqualTo(ONE_SECOND.toMillis()));
            assertThat(getTimeDifference() - ONE_SECOND.toMillis(), lessThan(250L));
            assertThat(webElements.size(), is(0));
        } finally {
            removeProperty(FIND_ONLY_VISIBLE_ELEMENTS.getName());
            removeProperty(ELEMENT_WAITING_TIME_UNIT.getName());
            removeProperty(ELEMENT_WAITING_TIME_VALUE.getName());
        }
    }

    @Test(retryAnalyzer = RetryAnalyzer.class)
    public void findWebElementsChainedOnlyVisibleImplicitConditionAndTimeConflictTest() {
        setProperty(FIND_ONLY_VISIBLE_ELEMENTS.getName(), "true");
        setProperty(ELEMENT_WAITING_TIME_UNIT.getName(), "SECONDS");
        setProperty(ELEMENT_WAITING_TIME_VALUE.getName(), "5");
        setStartBenchMark();
        try {
            List<WebElement> webElements = seleniumSteps.find(webElements(INVISIBLE_SPAN_BY)
                    .timeOut(ONE_SECOND)
                    .foundFrom(webElement(className(SPREAD_SHEET_CLASS))));
            setEndBenchMark();
            assertThat(getTimeDifference(), greaterThanOrEqualTo(ONE_SECOND.toMillis()));
            assertThat(getTimeDifference() - ONE_SECOND.toMillis(), lessThan(250L));
            assertThat(webElements.size(), is(0));
        } finally {
            removeProperty(FIND_ONLY_VISIBLE_ELEMENTS.getName());
            removeProperty(ELEMENT_WAITING_TIME_UNIT.getName());
            removeProperty(ELEMENT_WAITING_TIME_VALUE.getName());
        }
    }

    @Test(retryAnalyzer = RetryAnalyzer.class)
    public void findFromWebElementsWithoutConditionWithDefinedTimeTest() {
        WebElement parent = seleniumSteps.find(webElement(tagName(BUTTON_TAG)));
        setStartBenchMark();
        List<WebElement> webElements = seleniumSteps.find(webElements(CLASS_THAT_DOES_NOT_EXIST)
                .timeOut(ONE_SECOND)
                .foundFrom(parent));
        setEndBenchMark();
        assertThat(getTimeDifference(), greaterThanOrEqualTo(ONE_SECOND.toMillis()));
        assertThat(getTimeDifference() - ONE_SECOND.toMillis(), lessThan(250L));
        assertThat(webElements.size(), is(0));
    }

    @Test(retryAnalyzer = RetryAnalyzer.class)
    public void findFromWebElementsWithoutConditionWithTimeDefinedImplicitlyTest() {
        WebElement parent = seleniumSteps.find(webElement(tagName(BUTTON_TAG)));
        setProperty(ELEMENT_WAITING_TIME_UNIT.getName(), "SECONDS");
        setProperty(ELEMENT_WAITING_TIME_VALUE.getName(), "1");
        setStartBenchMark();
        try {
            List<WebElement> webElements = seleniumSteps.find(webElements(CLASS_THAT_DOES_NOT_EXIST)
                    .foundFrom(parent));
            setEndBenchMark();
            assertThat(getTimeDifference(), greaterThanOrEqualTo(ONE_SECOND.toMillis()));
            assertThat(getTimeDifference() - ONE_SECOND.toMillis(), lessThan(250L));
            assertThat(webElements.size(), is(0));
        } finally {
            removeProperty(ELEMENT_WAITING_TIME_UNIT.getName());
            removeProperty(ELEMENT_WAITING_TIME_VALUE.getName());
        }
    }

    @Test(retryAnalyzer = RetryAnalyzer.class)
    public void findFromWebElementsOnlyVisibleImplicitConditionAndDefinedTimeTest() {
        setProperty(FIND_ONLY_VISIBLE_ELEMENTS.getName(), "true");
        WebElement parent = seleniumSteps.find(webElement(className(SPREAD_SHEET_CLASS)));
        setStartBenchMark();
        try {
            List<WebElement> webElements = seleniumSteps.find(webElements(INVISIBLE_SPAN_BY)
                    .timeOut(ONE_SECOND)
                    .foundFrom(parent));
            setEndBenchMark();
            assertThat(getTimeDifference(), greaterThanOrEqualTo(ONE_SECOND.toMillis()));
            assertThat(getTimeDifference() - ONE_SECOND.toMillis(), lessThan(250L));
            assertThat(webElements.size(), is(0));
        } finally {
            removeProperty(FIND_ONLY_VISIBLE_ELEMENTS.getName());
        }
    }

    @Test(retryAnalyzer = RetryAnalyzer.class)
    public void findFromWebElementsOnlyVisibleImplicitConditionAndImplicitTimeTest() {
        setProperty(FIND_ONLY_VISIBLE_ELEMENTS.getName(), "true");
        setProperty(ELEMENT_WAITING_TIME_UNIT.getName(), "SECONDS");
        setProperty(ELEMENT_WAITING_TIME_VALUE.getName(), "1");
        WebElement parent = seleniumSteps.find(webElement(className(SPREAD_SHEET_CLASS)));
        setStartBenchMark();
        try {
            List<WebElement> webElements = seleniumSteps.find(webElements(INVISIBLE_SPAN_BY)
                    .foundFrom(parent));
            setEndBenchMark();
            assertThat(getTimeDifference(), greaterThanOrEqualTo(ONE_SECOND.toMillis()));
            assertThat(getTimeDifference() - ONE_SECOND.toMillis(), lessThan(250L));
            assertThat(webElements.size(), is(0));
        } finally {
            removeProperty(FIND_ONLY_VISIBLE_ELEMENTS.getName());
            removeProperty(ELEMENT_WAITING_TIME_UNIT.getName());
            removeProperty(ELEMENT_WAITING_TIME_VALUE.getName());
        }
    }

    @Test(retryAnalyzer = RetryAnalyzer.class)
    public void findFromWebElementsOnlyVisibleImplicitConditionAndTimeConflictTest() {
        setProperty(FIND_ONLY_VISIBLE_ELEMENTS.getName(), "true");
        setProperty(ELEMENT_WAITING_TIME_UNIT.getName(), "SECONDS");
        setProperty(ELEMENT_WAITING_TIME_VALUE.getName(), "5");
        WebElement parent = seleniumSteps.find(webElement(className(SPREAD_SHEET_CLASS)));
        setStartBenchMark();
        try {
            List<WebElement> webElements = seleniumSteps.find(webElements(INVISIBLE_SPAN_BY)
                    .timeOut(ONE_SECOND)
                    .foundFrom(parent));
            setEndBenchMark();
            assertThat(getTimeDifference(), greaterThanOrEqualTo(ONE_SECOND.toMillis()));
            assertThat(getTimeDifference() - ONE_SECOND.toMillis(), lessThan(250L));
            assertThat(webElements.size(), is(0));
        } finally {
            removeProperty(FIND_ONLY_VISIBLE_ELEMENTS.getName());
            removeProperty(ELEMENT_WAITING_TIME_UNIT.getName());
            removeProperty(ELEMENT_WAITING_TIME_VALUE.getName());
        }
    }


    @DataProvider(name = "search criteria1")
    public static Object[][] searchCriteria() {
        return new Object[][]{
                {INVISIBLE_SPAN_BY, enabled()},

                {VISIBLE_DIV_BY, NOT(enabled())},

                {INVISIBLE_SPAN_BY, visible()},

                {VISIBLE_DIV_BY, NOT(visible())},

                {className(MULTI_SELECT_CLASS),
                        nested(webElements(className(ITEM_OPTION_CLASS)), 4)},

                {className(MULTI_SELECT_CLASS),
                        NOT(nested(webElements(className(ITEM_OPTION_CLASS)), 3),
                                nested(webElements(className(ITEM_OPTION_CLASS)), 2))},

                {className(MULTI_SELECT_CLASS), nested(webElements(tagName(OPTION)))},

                {tagName(SELECT), NOT(nested(webElements(tagName(OPTION))))},

                {tagName(TEXT_AREA_TAG), attr(ATTR11, VALUE10)},

                {tagName(TEXT_AREA_TAG), NOT(attr(ATTR11, VALUE12),
                        attr(ATTR11, VALUE13),
                        attr(ATTR11, VALUE14),
                        attr(ATTR11, VALUE15))},

                {tagName(TEXT_AREA_TAG), attrMatches(ATTR11, "10")},

                {tagName(TEXT_AREA_TAG), NOT(attrMatches(ATTR11, "12"),
                        attrMatches(ATTR11, "13"),
                        attrMatches(ATTR11, VALUE14),
                        attrMatches(ATTR11, VALUE15))},

                {tagName(TEXT_AREA_TAG), attrMatches(ATTR11, "10")},

                {tagName(TEXT_AREA_TAG), NOT(attrMatches(ATTR11, "12"),
                        attrMatches(ATTR11, "13"),
                        attrMatches(ATTR11, VALUE14),
                        attrMatches(ATTR11, VALUE15))},

                {xpath(RADIO_BUTTON_XPATH), css(CSS18, CSS_VALUE6)},

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
                                css(CSS18, CSS_VALUE19))},

                {xpath(RADIO_BUTTON_XPATH), cssMatches(CSS18, "value6")},

                {xpath(RADIO_BUTTON_XPATH), NOT(cssMatches(CSS18, "value"))},

                {xpath(RADIO_BUTTON_XPATH), cssMatches(CSS18, "value6")},

                {xpath(RADIO_BUTTON_XPATH), NOT(cssMatches(CSS18, "value"))},

                {INVISIBLE_SPAN_BY, textMatches("div")},

                {VISIBLE_DIV_BY, NOT(textMatches("div"))}
        };
    }

    @Test(dataProvider = "search criteria1", retryAnalyzer = RetryAnalyzer.class)
    public void findElementsByCriteriaWithDefinedTimeTest(By by,
                                                          Criteria<WebElement> criteria) {
        setStartBenchMark();
        List<WebElement> webElements = seleniumSteps.find(webElements(by)
                .timeOut(ONE_SECOND)
                .criteria(criteria));
        setEndBenchMark();
        assertThat(getTimeDifference(), greaterThanOrEqualTo(ONE_SECOND.toMillis()));
        assertThat(new BigDecimal(getTimeDifference() - ONE_SECOND.toMillis()),
                either(lessThan(new BigDecimal(150L)))
                        .or(closeTo(new BigDecimal(150L), new BigDecimal(150L))));
        assertThat(webElements.size(), is(0));
    }

    @Test(dataProvider = "search criteria1", retryAnalyzer = RetryAnalyzer.class)
    public void findElementsByCriteriaWithTimeDefinedImplicitlyTest(By by,
                                                                    Criteria<WebElement> criteria) {
        setProperty(ELEMENT_WAITING_TIME_UNIT.getName(), "SECONDS");
        setProperty(ELEMENT_WAITING_TIME_VALUE.getName(), "1");
        setStartBenchMark();
        try {
            List<WebElement> webElements = seleniumSteps.find(webElements(by)
                    .criteria(criteria));
            setEndBenchMark();
            assertThat(getTimeDifference(), greaterThanOrEqualTo(ONE_SECOND.toMillis()));
            assertThat(new BigDecimal(getTimeDifference() - ONE_SECOND.toMillis()),
                    either(lessThan(new BigDecimal(150L)))
                            .or(closeTo(new BigDecimal(150L), new BigDecimal(150L))));
            assertThat(webElements.size(), is(0));
        } finally {
            removeProperty(ELEMENT_WAITING_TIME_UNIT.getName());
            removeProperty(ELEMENT_WAITING_TIME_VALUE.getName());
        }
    }
}
