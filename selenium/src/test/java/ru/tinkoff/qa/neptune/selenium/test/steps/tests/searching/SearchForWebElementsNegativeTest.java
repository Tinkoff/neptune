package ru.tinkoff.qa.neptune.selenium.test.steps.tests.searching;

import org.openqa.selenium.By;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import ru.tinkoff.qa.neptune.core.api.steps.Criteria;
import ru.tinkoff.qa.neptune.selenium.test.BaseWebDriverTest;
import ru.tinkoff.qa.neptune.selenium.test.RetryAnalyzer;

import java.math.BigDecimal;
import java.util.List;

import static java.lang.String.format;
import static java.util.regex.Pattern.compile;
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

public class SearchForWebElementsNegativeTest extends BaseWebDriverTest {

    private static final String FOUND_BY_PATTERN = "0 web elements found %s";
    private static final String FOUND_ON_CONDITION = FOUND_BY_PATTERN + " and meet criteria ['%s']";
    private static final By CLASS_THAT_DOES_NOT_EXIST = className("fakeClass");

    private static String expectedDescriptionOfTheFoundElements(By by, Criteria<? extends SearchContext> condition) {
        return format(FOUND_ON_CONDITION, by, condition);
    }

    private static String expectedDescriptionOfTheFoundElements() {
        return format(FOUND_BY_PATTERN, CLASS_THAT_DOES_NOT_EXIST);
    }

    @Test(retryAnalyzer = RetryAnalyzer.class)
    public void findWebElementsFirstLevelWithoutConditionWithDefinedTimeTest() {
        setStartBenchMark();
        List<WebElement> webElements = seleniumSteps.find(webElements(CLASS_THAT_DOES_NOT_EXIST)
                .timeOut(ONE_SECOND));
        setEndBenchMark();
        assertThat(getTimeDifference(), greaterThanOrEqualTo(ONE_SECOND.toMillis()));
        assertThat(getTimeDifference() - ONE_SECOND.toMillis(), lessThan(HALF_SECOND.toMillis()));
        assertThat(webElements.size(), is(0));
        assertThat(webElements.toString(), is(expectedDescriptionOfTheFoundElements()));
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
            assertThat(webElements.toString(), is(expectedDescriptionOfTheFoundElements()));
        } finally {
            removeProperty(ELEMENT_WAITING_TIME_UNIT.getPropertyName());
            removeProperty(ELEMENT_WAITING_TIME_VALUE.getPropertyName());
        }
    }

    @Test(retryAnalyzer = RetryAnalyzer.class)
    public void findWebElementsFirstLevelOnlyVisibleImplicitConditionAndDefinedTimeTest() {
        setProperty(FIND_ONLY_VISIBLE_ELEMENTS.getPropertyName(), "true");
        setStartBenchMark();
        try {
            List<WebElement> webElements = seleniumSteps.find(webElements(INVISIBLE_SPAN_BY)
                    .timeOut(ONE_SECOND));
            setEndBenchMark();
            assertThat(getTimeDifference(), greaterThanOrEqualTo(ONE_SECOND.toMillis()));
            assertThat(getTimeDifference() - ONE_SECOND.toMillis(), lessThan(HALF_SECOND.toMillis()));
            assertThat(webElements.size(), is(0));
            assertThat(webElements.toString(), is(expectedDescriptionOfTheFoundElements(INVISIBLE_SPAN_BY,
                    visible())));
        } finally {
            removeProperty(FIND_ONLY_VISIBLE_ELEMENTS.getPropertyName());
        }
    }

    @Test(retryAnalyzer = RetryAnalyzer.class)
    public void findWebElementsFirstLevelOnlyVisibleImplicitConditionAndImplicitTimeTest() {
        setProperty(FIND_ONLY_VISIBLE_ELEMENTS.getPropertyName(), "true");
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
                    visible())));
        } finally {
            removeProperty(FIND_ONLY_VISIBLE_ELEMENTS.getPropertyName());
            removeProperty(ELEMENT_WAITING_TIME_UNIT.getPropertyName());
            removeProperty(ELEMENT_WAITING_TIME_VALUE.getPropertyName());
        }
    }

    @Test(retryAnalyzer = RetryAnalyzer.class)
    public void findWebElementsFirstLevelOnlyVisibleImplicitConditionAndTimeConflictTest() {
        setProperty(FIND_ONLY_VISIBLE_ELEMENTS.getPropertyName(), "true");
        setProperty(ELEMENT_WAITING_TIME_UNIT.getPropertyName(), "SECONDS");
        setProperty(ELEMENT_WAITING_TIME_VALUE.getPropertyName(), "5");
        setStartBenchMark();
        try {
            List<WebElement> webElements = seleniumSteps.find(webElements(INVISIBLE_SPAN_BY)
                    .timeOut(ONE_SECOND));
            setEndBenchMark();
            assertThat(getTimeDifference(), greaterThanOrEqualTo(ONE_SECOND.toMillis()));
            assertThat(getTimeDifference() - ONE_SECOND.toMillis(), lessThan(HALF_SECOND.toMillis()));
            assertThat(webElements.size(), is(0));
            assertThat(webElements.toString(), is(expectedDescriptionOfTheFoundElements(INVISIBLE_SPAN_BY,
                    visible())));
        } finally {
            removeProperty(FIND_ONLY_VISIBLE_ELEMENTS.getPropertyName());
            removeProperty(ELEMENT_WAITING_TIME_UNIT.getPropertyName());
            removeProperty(ELEMENT_WAITING_TIME_VALUE.getPropertyName());
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
        assertThat(getTimeDifference() - ONE_SECOND.toMillis(), lessThan(HALF_SECOND.toMillis()));
        assertThat(webElements.size(), is(0));
        assertThat(webElements.toString(), is(expectedDescriptionOfTheFoundElements()));
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
            assertThat(webElements.toString(), is(expectedDescriptionOfTheFoundElements()));
        } finally {
            removeProperty(ELEMENT_WAITING_TIME_UNIT.getPropertyName());
            removeProperty(ELEMENT_WAITING_TIME_VALUE.getPropertyName());
        }
    }

    @Test(retryAnalyzer = RetryAnalyzer.class)
    public void findWebElementsChainedOnlyVisibleImplicitConditionAndDefinedTimeTest() {
        setProperty(FIND_ONLY_VISIBLE_ELEMENTS.getPropertyName(), "true");
        setStartBenchMark();
        try {
            List<WebElement> webElements = seleniumSteps.find(webElements(INVISIBLE_SPAN_BY)
                    .timeOut(ONE_SECOND)
                    .foundFrom(webElement(className(SPREAD_SHEET_CLASS))));
            setEndBenchMark();
            assertThat(getTimeDifference(), greaterThanOrEqualTo(ONE_SECOND.toMillis()));
            assertThat(getTimeDifference() - ONE_SECOND.toMillis(), lessThan(HALF_SECOND.toMillis()));
            assertThat(webElements.size(), is(0));
            assertThat(webElements.toString(), is(expectedDescriptionOfTheFoundElements(INVISIBLE_SPAN_BY,
                    visible())));
        } finally {
            removeProperty(FIND_ONLY_VISIBLE_ELEMENTS.getPropertyName());
        }
    }

    @Test(retryAnalyzer = RetryAnalyzer.class)
    public void findWebElementsChainedOnlyVisibleImplicitConditionAndImplicitTimeTest() {
        setProperty(FIND_ONLY_VISIBLE_ELEMENTS.getPropertyName(), "true");
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
                    visible())));
        } finally {
            removeProperty(FIND_ONLY_VISIBLE_ELEMENTS.getPropertyName());
            removeProperty(ELEMENT_WAITING_TIME_UNIT.getPropertyName());
            removeProperty(ELEMENT_WAITING_TIME_VALUE.getPropertyName());
        }
    }

    @Test(retryAnalyzer = RetryAnalyzer.class)
    public void findWebElementsChainedOnlyVisibleImplicitConditionAndTimeConflictTest() {
        setProperty(FIND_ONLY_VISIBLE_ELEMENTS.getPropertyName(), "true");
        setProperty(ELEMENT_WAITING_TIME_UNIT.getPropertyName(), "SECONDS");
        setProperty(ELEMENT_WAITING_TIME_VALUE.getPropertyName(), "5");
        setStartBenchMark();
        try {
            List<WebElement> webElements = seleniumSteps.find(webElements(INVISIBLE_SPAN_BY)
                    .timeOut(ONE_SECOND)
                    .foundFrom(webElement(className(SPREAD_SHEET_CLASS))));
            setEndBenchMark();
            assertThat(getTimeDifference(), greaterThanOrEqualTo(ONE_SECOND.toMillis()));
            assertThat(getTimeDifference() - ONE_SECOND.toMillis(), lessThan(HALF_SECOND.toMillis()));
            assertThat(webElements.size(), is(0));
            assertThat(webElements.toString(), is(expectedDescriptionOfTheFoundElements(INVISIBLE_SPAN_BY,
                    visible())));
        } finally {
            removeProperty(FIND_ONLY_VISIBLE_ELEMENTS.getPropertyName());
            removeProperty(ELEMENT_WAITING_TIME_UNIT.getPropertyName());
            removeProperty(ELEMENT_WAITING_TIME_VALUE.getPropertyName());
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
        assertThat(getTimeDifference() - ONE_SECOND.toMillis(), lessThan(HALF_SECOND.toMillis()));
        assertThat(webElements.size(), is(0));
        assertThat(webElements.toString(), is(expectedDescriptionOfTheFoundElements()));
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
            assertThat(webElements.toString(), is(expectedDescriptionOfTheFoundElements()));
        } finally {
            removeProperty(ELEMENT_WAITING_TIME_UNIT.getPropertyName());
            removeProperty(ELEMENT_WAITING_TIME_VALUE.getPropertyName());
        }
    }

    @Test(retryAnalyzer = RetryAnalyzer.class)
    public void findFromWebElementsOnlyVisibleImplicitConditionAndDefinedTimeTest() {
        setProperty(FIND_ONLY_VISIBLE_ELEMENTS.getPropertyName(), "true");
        WebElement parent = seleniumSteps.find(webElement(className(SPREAD_SHEET_CLASS)));
        setStartBenchMark();
        try {
            List<WebElement> webElements = seleniumSteps.find(webElements(INVISIBLE_SPAN_BY)
                    .timeOut(ONE_SECOND)
                    .foundFrom(parent));
            setEndBenchMark();
            assertThat(getTimeDifference(), greaterThanOrEqualTo(ONE_SECOND.toMillis()));
            assertThat(getTimeDifference() - ONE_SECOND.toMillis(), lessThan(HALF_SECOND.toMillis()));
            assertThat(webElements.size(), is(0));
            assertThat(webElements.toString(), is(expectedDescriptionOfTheFoundElements(INVISIBLE_SPAN_BY,
                    visible())));
        } finally {
            removeProperty(FIND_ONLY_VISIBLE_ELEMENTS.getPropertyName());
        }
    }

    @Test(retryAnalyzer = RetryAnalyzer.class)
    public void findFromWebElementsOnlyVisibleImplicitConditionAndImplicitTimeTest() {
        setProperty(FIND_ONLY_VISIBLE_ELEMENTS.getPropertyName(), "true");
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
                    visible())));
        } finally {
            removeProperty(FIND_ONLY_VISIBLE_ELEMENTS.getPropertyName());
            removeProperty(ELEMENT_WAITING_TIME_UNIT.getPropertyName());
            removeProperty(ELEMENT_WAITING_TIME_VALUE.getPropertyName());
        }
    }

    @Test(retryAnalyzer = RetryAnalyzer.class)
    public void findFromWebElementsOnlyVisibleImplicitConditionAndTimeConflictTest() {
        setProperty(FIND_ONLY_VISIBLE_ELEMENTS.getPropertyName(), "true");
        setProperty(ELEMENT_WAITING_TIME_UNIT.getPropertyName(), "SECONDS");
        setProperty(ELEMENT_WAITING_TIME_VALUE.getPropertyName(), "5");
        WebElement parent = seleniumSteps.find(webElement(className(SPREAD_SHEET_CLASS)));
        setStartBenchMark();
        try {
            List<WebElement> webElements = seleniumSteps.find(webElements(INVISIBLE_SPAN_BY)
                    .timeOut(ONE_SECOND)
                    .foundFrom(parent));
            setEndBenchMark();
            assertThat(getTimeDifference(), greaterThanOrEqualTo(ONE_SECOND.toMillis()));
            assertThat(getTimeDifference() - ONE_SECOND.toMillis(), lessThan(HALF_SECOND.toMillis()));
            assertThat(webElements.size(), is(0));
            assertThat(webElements.toString(), is(expectedDescriptionOfTheFoundElements(INVISIBLE_SPAN_BY,
                    visible())));
        } finally {
            removeProperty(FIND_ONLY_VISIBLE_ELEMENTS.getPropertyName());
            removeProperty(ELEMENT_WAITING_TIME_UNIT.getPropertyName());
            removeProperty(ELEMENT_WAITING_TIME_VALUE.getPropertyName());
        }
    }


    @DataProvider(name = "search criteria1")
    public static Object[][] searchCriteria() {
        return new Object[][]{
                {INVISIBLE_SPAN_BY, enabled(),
                        expectedDescriptionOfTheFoundElements(INVISIBLE_SPAN_BY, enabled())},

                {VISIBLE_DIV_BY, NOT(enabled()),
                        expectedDescriptionOfTheFoundElements(VISIBLE_DIV_BY, NOT(enabled()))},

                {INVISIBLE_SPAN_BY, visible(),
                        expectedDescriptionOfTheFoundElements(INVISIBLE_SPAN_BY, visible())},

                {VISIBLE_DIV_BY, NOT(visible()),
                        expectedDescriptionOfTheFoundElements(VISIBLE_DIV_BY, NOT(visible()))},

                {className(MULTI_SELECT_CLASS),
                        nestedElements(webElements(className(ITEM_OPTION_CLASS)), 4),
                        expectedDescriptionOfTheFoundElements(className(MULTI_SELECT_CLASS),
                                nestedElements(webElements(className(ITEM_OPTION_CLASS)), 4))},

                {className(MULTI_SELECT_CLASS),
                        NOT(nestedElements(webElements(className(ITEM_OPTION_CLASS)), 3),
                                nestedElements(webElements(className(ITEM_OPTION_CLASS)), 2)),
                        expectedDescriptionOfTheFoundElements(className(MULTI_SELECT_CLASS),
                                NOT(nestedElements(webElements(className(ITEM_OPTION_CLASS)), 3),
                                        nestedElements(webElements(className(ITEM_OPTION_CLASS)), 2)))},

                {className(MULTI_SELECT_CLASS),
                        nestedElements(webElements(tagName(OPTION))),
                        expectedDescriptionOfTheFoundElements(className(MULTI_SELECT_CLASS),
                                nestedElements(webElements(tagName(OPTION))))},

                {tagName(SELECT), NOT(nestedElements(webElements(tagName(OPTION)))),
                        expectedDescriptionOfTheFoundElements(tagName(SELECT),
                                NOT(nestedElements(webElements(tagName(OPTION)))))},

                {tagName(TEXT_AREA_TAG), attribute(ATTR11, VALUE10),
                        expectedDescriptionOfTheFoundElements(tagName(TEXT_AREA_TAG),
                                attribute(ATTR11, VALUE10))},

                {tagName(TEXT_AREA_TAG), NOT(attribute(ATTR11, VALUE12),
                        attribute(ATTR11, VALUE13),
                        attribute(ATTR11, VALUE14),
                        attribute(ATTR11, VALUE15)),
                        expectedDescriptionOfTheFoundElements(tagName(TEXT_AREA_TAG),
                                NOT(attribute(ATTR11, VALUE12),
                                        attribute(ATTR11, VALUE13),
                                        attribute(ATTR11, VALUE14),
                                        attribute(ATTR11, VALUE15)))},

                {tagName(TEXT_AREA_TAG), attributeContains(ATTR11, "10"),
                        expectedDescriptionOfTheFoundElements(tagName(TEXT_AREA_TAG),
                                attributeContains(ATTR11, "10"))},

                {tagName(TEXT_AREA_TAG), NOT(attributeContains(ATTR11, "12"),
                        attributeContains(ATTR11, "13"),
                        attributeContains(ATTR11, VALUE14),
                        attributeContains(ATTR11, VALUE15)),
                        expectedDescriptionOfTheFoundElements(tagName(TEXT_AREA_TAG),
                                NOT(attributeContains(ATTR11, "12"),
                                        attributeContains(ATTR11, "13"),
                                        attributeContains(ATTR11, VALUE14),
                                        attributeContains(ATTR11, VALUE15)))},

                {tagName(TEXT_AREA_TAG), attributeMatches(ATTR11, compile("10")),
                        expectedDescriptionOfTheFoundElements(tagName(TEXT_AREA_TAG),
                                attributeMatches(ATTR11, compile("10")))},

                {tagName(TEXT_AREA_TAG), NOT(attributeMatches(ATTR11, compile("12")),
                        attributeMatches(ATTR11, compile("13")),
                        attributeMatches(ATTR11, compile(VALUE14)),
                        attributeMatches(ATTR11, compile(VALUE15))),
                        expectedDescriptionOfTheFoundElements(tagName(TEXT_AREA_TAG),
                                NOT(attributeMatches(ATTR11, compile("12")),
                                        attributeMatches(ATTR11, compile("13")),
                                        attributeMatches(ATTR11, compile(VALUE14)),
                                        attributeMatches(ATTR11, compile(VALUE15))))},

                {xpath(RADIO_BUTTON_XPATH), css(CSS18, CSS_VALUE6),
                        expectedDescriptionOfTheFoundElements(xpath(RADIO_BUTTON_XPATH),
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
                        expectedDescriptionOfTheFoundElements(xpath(RADIO_BUTTON_XPATH),
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

                {xpath(RADIO_BUTTON_XPATH), cssContains(CSS18, "value6"),
                        expectedDescriptionOfTheFoundElements(xpath(RADIO_BUTTON_XPATH),
                                cssContains(CSS18, "value6"))},

                {xpath(RADIO_BUTTON_XPATH),
                        NOT(cssContains(CSS18, "value")),
                        expectedDescriptionOfTheFoundElements(xpath(RADIO_BUTTON_XPATH),
                                NOT(cssContains(CSS18, "value")))},

                {xpath(RADIO_BUTTON_XPATH), cssMatches(CSS18, compile("value6")),
                        expectedDescriptionOfTheFoundElements(xpath(RADIO_BUTTON_XPATH),
                                cssMatches(CSS18, compile("value6")))},

                {xpath(RADIO_BUTTON_XPATH),
                        NOT(cssMatches(CSS18, compile("value"))),
                        expectedDescriptionOfTheFoundElements(xpath(RADIO_BUTTON_XPATH),
                                NOT(cssMatches(CSS18, compile("value"))))},

                {INVISIBLE_SPAN_BY, text(VISIBLE_DIV),
                        expectedDescriptionOfTheFoundElements(INVISIBLE_SPAN_BY,
                                text(VISIBLE_DIV))},

                {VISIBLE_DIV_BY, NOT(text(VISIBLE_DIV)),
                        expectedDescriptionOfTheFoundElements(VISIBLE_DIV_BY,
                                NOT(text(VISIBLE_DIV)))},

                {INVISIBLE_SPAN_BY, textMatches(compile("div")),
                        expectedDescriptionOfTheFoundElements(INVISIBLE_SPAN_BY,
                                textMatches(compile("div")))},

                {VISIBLE_DIV_BY, NOT(textMatches(compile("div"))),
                        expectedDescriptionOfTheFoundElements(VISIBLE_DIV_BY,
                                NOT(textMatches(compile("div"))))}
        };
    }

    @Test(dataProvider = "search criteria1", retryAnalyzer = RetryAnalyzer.class)
    public void findElementsByCriteriaWithDefinedTimeTest(By by,
                                                          Criteria<WebElement> criteria,
                                                          String expectedListDescription) {
        setStartBenchMark();
        List<WebElement> webElements = seleniumSteps.find(webElements(by)
                .timeOut(ONE_SECOND)
                .criteria(criteria));
        setEndBenchMark();
        assertThat(getTimeDifference(), greaterThanOrEqualTo(ONE_SECOND.toMillis()));
        assertThat(new BigDecimal(getTimeDifference() - ONE_SECOND.toMillis()),
                either(lessThan(new BigDecimal(HALF_SECOND.toMillis())))
                        .or(closeTo(new BigDecimal(HALF_SECOND.toMillis()), new BigDecimal(HALF_SECOND.toMillis()))));
        assertThat(webElements.size(), is(0));
        assertThat(webElements.toString(), is(expectedListDescription));
    }

    @Test(dataProvider = "search criteria1", retryAnalyzer = RetryAnalyzer.class)
    public void findElementsByCriteriaWithTimeDefinedImplicitlyTest(By by,
                                                                    Criteria<WebElement> criteria,
                                                                    String expectedListDescription) {
        setProperty(ELEMENT_WAITING_TIME_UNIT.getPropertyName(), "SECONDS");
        setProperty(ELEMENT_WAITING_TIME_VALUE.getPropertyName(), "1");
        setStartBenchMark();
        try {
            List<WebElement> webElements = seleniumSteps.find(webElements(by)
                    .criteria(criteria));
            setEndBenchMark();
            assertThat(getTimeDifference(), greaterThanOrEqualTo(ONE_SECOND.toMillis()));
            assertThat(new BigDecimal(getTimeDifference() - ONE_SECOND.toMillis()),
                    either(lessThan(new BigDecimal(HALF_SECOND.toMillis())))
                            .or(closeTo(new BigDecimal(HALF_SECOND.toMillis()), new BigDecimal(HALF_SECOND.toMillis()))));
            assertThat(webElements.size(), is(0));
            assertThat(webElements.toString(), is(expectedListDescription));
        } finally {
            removeProperty(ELEMENT_WAITING_TIME_UNIT.getPropertyName());
            removeProperty(ELEMENT_WAITING_TIME_VALUE.getPropertyName());
        }
    }
}
