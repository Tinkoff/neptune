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

public class SearchForWebElementPositiveTest extends BaseWebDriverTest {

    private static final String FOUND_BY_PATTERN = "Web element found %s";
    private static final String FOUND_ON_CONDITION = FOUND_BY_PATTERN + " ['%s']";

    private static String expectedDescriptionOfTheFoundElement(By by, Criteria<? extends SearchContext> condition) {
        return format(FOUND_ON_CONDITION, by, condition);
    }

    private static String expectedDescriptionOfTheFoundElement(By by) {
        return format(FOUND_BY_PATTERN, by);
    }

    @Test(retryAnalyzer = RetryAnalyzer.class)
    public void findWebElementFirstLevelWithoutConditionTest() {
        WebElement webElement = seleniumSteps.find(webElement(tagName(BUTTON_TAG)));
        assertThat(webElement, equalTo(COMMON_BUTTON1));
        assertThat(webElement.toString(), is(expectedDescriptionOfTheFoundElement(tagName(BUTTON_TAG))));
    }

    @Test(retryAnalyzer = RetryAnalyzer.class)
    public void findWebElementFirstLevelOnlyVisibleImplicitConditionTest() {
        setProperty(FIND_ONLY_VISIBLE_ELEMENTS.getPropertyName(), "true");
        try {
            WebElement webElement = seleniumSteps.find(webElement(tagName(BUTTON_TAG)));
            assertThat(webElement, equalTo(COMMON_BUTTON2));
            assertThat(webElement.toString(), is(expectedDescriptionOfTheFoundElement(tagName(BUTTON_TAG),
                    visible())));
        } finally {
            removeProperty(FIND_ONLY_VISIBLE_ELEMENTS.getPropertyName());
        }
    }

    @Test(retryAnalyzer = RetryAnalyzer.class)
    public void findWebElementFirstLevelOnlyWhenTimeIsDefinedTest() {
        setStartBenchMark();
        WebElement webElement = seleniumSteps.find(webElement(tagName(TABLE))
                .timeOut(FIVE_SECONDS));
        setEndBenchMark();
        assertThat(getTimeDifference(), lessThan(HALF_SECOND.toMillis()));
        assertThat(webElement, equalTo(COMMON_LABELED_TABLE1));
        assertThat(webElement.toString(), is(expectedDescriptionOfTheFoundElement(tagName(TABLE))));
    }

    @Test(retryAnalyzer = RetryAnalyzer.class)
    public void findWebElementFirstLevelOnlyWhenTimeIsDefinedImplicitlyTest() {
        setProperty(ELEMENT_WAITING_TIME_UNIT.getPropertyName(), "SECONDS");
        setProperty(ELEMENT_WAITING_TIME_VALUE.getPropertyName(), "5");
        try {
            setStartBenchMark();
            WebElement webElement = seleniumSteps.find(webElement(tagName(TABLE)));
            setEndBenchMark();
            assertThat(getTimeDifference(), lessThan(HALF_SECOND.toMillis()));
            assertThat(webElement, equalTo(COMMON_LABELED_TABLE1));
            assertThat(webElement.toString(), is(expectedDescriptionOfTheFoundElement(tagName(TABLE))));
        } finally {
            removeProperty(ELEMENT_WAITING_TIME_UNIT.getPropertyName());
            removeProperty(ELEMENT_WAITING_TIME_VALUE.getPropertyName());
        }
    }

    @Test(retryAnalyzer = RetryAnalyzer.class)
    public void findWebElementChainedWithoutConditionTest() {
        WebElement webElement = seleniumSteps.find(webElement(className(CUSTOM_BUTTON_CLASS))
                .foundFrom(webElement(className(SPREAD_SHEET_CLASS))));
        assertThat(webElement, equalTo(CUSTOM_LABELED_BUTTON1));
        assertThat(webElement.toString(), is(expectedDescriptionOfTheFoundElement(className(CUSTOM_BUTTON_CLASS))));
    }

    @Test(retryAnalyzer = RetryAnalyzer.class)
    public void findWebElementChainedOnlyVisibleImplicitConditionTest() {
        setProperty(FIND_ONLY_VISIBLE_ELEMENTS.getPropertyName(), "true");
        try {
            WebElement webElement = seleniumSteps.find(webElement(className(CUSTOM_BUTTON_CLASS))
                    .foundFrom(webElement(className(SPREAD_SHEET_CLASS))));
            assertThat(webElement, equalTo(CUSTOM_LABELED_BUTTON1));
            assertThat(webElement.toString(), is(expectedDescriptionOfTheFoundElement(className(CUSTOM_BUTTON_CLASS),
                    visible())));
        } finally {
            removeProperty(FIND_ONLY_VISIBLE_ELEMENTS.getPropertyName());
        }
    }

    @Test(retryAnalyzer = RetryAnalyzer.class)
    public void findWebElementChainedOnlyWhenTimeIsDefinedTest() {
        setStartBenchMark();
        WebElement webElement = seleniumSteps.find(webElement(className(CUSTOM_BUTTON_CLASS))
                .timeOut(FIVE_SECONDS)
                .foundFrom(webElement(className(SPREAD_SHEET_CLASS))
                        .timeOut(FIVE_SECONDS)));
        setEndBenchMark();
        assertThat(getTimeDifference(), lessThan(HALF_SECOND.toMillis()));
        assertThat(webElement, equalTo(CUSTOM_LABELED_BUTTON1));
        assertThat(webElement.toString(), is(expectedDescriptionOfTheFoundElement(className(CUSTOM_BUTTON_CLASS))));
    }

    @Test(retryAnalyzer = RetryAnalyzer.class)
    public void findWebElementChainedOnlyWhenTimeIsDefinedImplicitlyTest() {
        setProperty(ELEMENT_WAITING_TIME_UNIT.getPropertyName(), "SECONDS");
        setProperty(ELEMENT_WAITING_TIME_VALUE.getPropertyName(), "5");
        try {
            setStartBenchMark();
            WebElement webElement = seleniumSteps.find(webElement(className(CUSTOM_BUTTON_CLASS))
                    .foundFrom(webElement(className(SPREAD_SHEET_CLASS))));
            setEndBenchMark();
            assertThat(getTimeDifference(), lessThan(HALF_SECOND.toMillis()));
            assertThat(webElement, equalTo(CUSTOM_LABELED_BUTTON1));
            assertThat(webElement.toString(), is(expectedDescriptionOfTheFoundElement(className(CUSTOM_BUTTON_CLASS))));
        } finally {
            removeProperty(ELEMENT_WAITING_TIME_UNIT.getPropertyName());
            removeProperty(ELEMENT_WAITING_TIME_VALUE.getPropertyName());
        }
    }

    @Test(retryAnalyzer = RetryAnalyzer.class)
    public void findWebElementFromAnotherWithoutConditionTest() {
        WebElement spreadSheet = seleniumSteps.find(webElement(className(SPREAD_SHEET_CLASS)));
        WebElement webElement = seleniumSteps.find(webElement(className(CUSTOM_BUTTON_CLASS))
                .foundFrom(spreadSheet));
        assertThat(webElement, equalTo(CUSTOM_LABELED_BUTTON1));
        assertThat(webElement.toString(), is(expectedDescriptionOfTheFoundElement(className(CUSTOM_BUTTON_CLASS))));
    }

    @Test(retryAnalyzer = RetryAnalyzer.class)
    public void findWebElementFromAnotherOnlyVisibleImplicitConditionTest() {
        setProperty(FIND_ONLY_VISIBLE_ELEMENTS.getPropertyName(), "true");
        try {
            WebElement spreadSheet = seleniumSteps.find(webElement(className(SPREAD_SHEET_CLASS)));
            assertThat(spreadSheet.isDisplayed(), is(true));
            WebElement webElement = seleniumSteps.find(webElement(className(CUSTOM_BUTTON_CLASS))
                    .foundFrom(spreadSheet));
            assertThat(webElement, equalTo(CUSTOM_LABELED_BUTTON1));
            assertThat(webElement.toString(), is(expectedDescriptionOfTheFoundElement(className(CUSTOM_BUTTON_CLASS),
                    visible())));
        } finally {
            removeProperty(FIND_ONLY_VISIBLE_ELEMENTS.getPropertyName());
        }
    }

    @Test(retryAnalyzer = RetryAnalyzer.class)
    public void findWebElementFromAnotherOnlyWhenTimeIsDefinedTest() {
        WebElement spreadSheet = seleniumSteps.find(webElement(className(SPREAD_SHEET_CLASS)));
        setStartBenchMark();
        WebElement webElement = seleniumSteps.find(webElement(className(CUSTOM_BUTTON_CLASS))
                .timeOut(FIVE_SECONDS)
                .foundFrom(spreadSheet));
        setEndBenchMark();
        assertThat(getTimeDifference(), lessThan(HALF_SECOND.toMillis()));
        assertThat(webElement, equalTo(CUSTOM_LABELED_BUTTON1));
        assertThat(webElement.toString(), is(expectedDescriptionOfTheFoundElement(className(CUSTOM_BUTTON_CLASS))));
    }

    @Test(retryAnalyzer = RetryAnalyzer.class)
    public void findWebElementFromAnotherOnlyWhenTimeIsDefinedImplicitlyTest() {
        setProperty(ELEMENT_WAITING_TIME_UNIT.getPropertyName(), "SECONDS");
        setProperty(ELEMENT_WAITING_TIME_VALUE.getPropertyName(), "5");
        try {
            WebElement spreadSheet = seleniumSteps.find(webElement(className(SPREAD_SHEET_CLASS)));
            setStartBenchMark();
            WebElement webElement = seleniumSteps.find(webElement(className(CUSTOM_BUTTON_CLASS))
                    .foundFrom(spreadSheet));
            setEndBenchMark();
            assertThat(getTimeDifference(), lessThan(HALF_SECOND.toMillis()));
            assertThat(webElement, equalTo(CUSTOM_LABELED_BUTTON1));
            assertThat(webElement.toString(), is(expectedDescriptionOfTheFoundElement(className(CUSTOM_BUTTON_CLASS))));
        } finally {
            removeProperty(ELEMENT_WAITING_TIME_UNIT.getPropertyName());
            removeProperty(ELEMENT_WAITING_TIME_VALUE.getPropertyName());
        }
    }

    @DataProvider(name = "search criteria1")
    public static Object[][] searchCriteria() {
        return new Object[][]{
                {tagName(BUTTON_TAG), enabled(), COMMON_BUTTON3,
                        expectedDescriptionOfTheFoundElement(tagName(BUTTON_TAG), enabled())},

                {xpath(RADIO_BUTTON_XPATH), NOT(enabled()), COMMON_RADIOBUTTON2,
                        expectedDescriptionOfTheFoundElement(xpath(RADIO_BUTTON_XPATH), NOT(enabled()))},

                {tagName(LINK_TAG), visible(), COMMON_LABELED_LINK1,
                        expectedDescriptionOfTheFoundElement(tagName(LINK_TAG), visible())},

                {tagName(LINK_TAG), NOT(visible()), COMMON_LABELED_LINK2,
                        expectedDescriptionOfTheFoundElement(tagName(LINK_TAG), NOT(visible()))},

                {className(MULTI_SELECT_CLASS), nestedElements(webElements(className(ITEM_OPTION_CLASS)), 2), MULTI_SELECT4,

                        expectedDescriptionOfTheFoundElement(className(MULTI_SELECT_CLASS),
                                nestedElements(webElements(className(ITEM_OPTION_CLASS)), 2))},

                {className(MULTI_SELECT_CLASS), NOT(nestedElements(webElements(className(ITEM_OPTION_CLASS)), 2)), MULTI_SELECT1,

                        expectedDescriptionOfTheFoundElement(className(MULTI_SELECT_CLASS),
                                NOT(nestedElements(webElements(className(ITEM_OPTION_CLASS)), 2)))},

                {className(MULTI_SELECT_CLASS), nestedElements(webElements(className(ITEM_OPTION_CLASS))), MULTI_SELECT1,

                        expectedDescriptionOfTheFoundElement(className(MULTI_SELECT_CLASS),
                                nestedElements(webElements(className(ITEM_OPTION_CLASS))))},

                {tagName(SELECT), NOT(nestedElements(webElements(className(ITEM_OPTION_CLASS)))), COMMON_LABELED_SELECT1,

                        expectedDescriptionOfTheFoundElement(tagName(SELECT),
                                NOT(nestedElements(webElements(className(ITEM_OPTION_CLASS)))))},

                {tagName(TEXT_AREA_TAG), attribute(ATTR11, VALUE13), TEXT_AREA2,
                        expectedDescriptionOfTheFoundElement(tagName(TEXT_AREA_TAG),
                                attribute(ATTR11, VALUE13))},

                {tagName(TEXT_AREA_TAG), NOT(attribute(ATTR11, VALUE13)), TEXT_AREA1,
                        expectedDescriptionOfTheFoundElement(tagName(TEXT_AREA_TAG),
                                NOT(attribute(ATTR11, VALUE13)))},

                {tagName(TEXT_AREA_TAG), attributeContains(ATTR11, VALUE13), TEXT_AREA2,
                        expectedDescriptionOfTheFoundElement(tagName(TEXT_AREA_TAG),
                                attributeContains(ATTR11, VALUE13))},

                {tagName(TEXT_AREA_TAG), NOT(attributeContains(ATTR11, VALUE13)), TEXT_AREA1,
                        expectedDescriptionOfTheFoundElement(tagName(TEXT_AREA_TAG),
                                NOT(attributeContains(ATTR11, VALUE13)))},

                {tagName(TEXT_AREA_TAG), attributeMatches(ATTR11, compile(VALUE13)), TEXT_AREA2,
                        expectedDescriptionOfTheFoundElement(tagName(TEXT_AREA_TAG),
                                attributeMatches(ATTR11, compile(VALUE13)))},

                {tagName(TEXT_AREA_TAG), NOT(attributeMatches(ATTR11, compile(VALUE13))), TEXT_AREA1,
                        expectedDescriptionOfTheFoundElement(tagName(TEXT_AREA_TAG),
                                NOT(attributeMatches(ATTR11, compile(VALUE13))))},

                {xpath(RADIO_BUTTON_XPATH), css(CSS18, CSS_VALUE9), COMMON_RADIOBUTTON3,
                        expectedDescriptionOfTheFoundElement(xpath(RADIO_BUTTON_XPATH), css(CSS18, CSS_VALUE9))},

                {xpath(RADIO_BUTTON_XPATH), NOT(css(CSS18, CSS_VALUE9)), COMMON_RADIOBUTTON1,
                        expectedDescriptionOfTheFoundElement(xpath(RADIO_BUTTON_XPATH), NOT(css(CSS18, CSS_VALUE9)))},

                {xpath(RADIO_BUTTON_XPATH), cssContains(CSS18, CSS_VALUE9), COMMON_RADIOBUTTON3,
                        expectedDescriptionOfTheFoundElement(xpath(RADIO_BUTTON_XPATH), cssContains(CSS18, CSS_VALUE9))},

                {xpath(RADIO_BUTTON_XPATH), NOT(cssContains(CSS18, CSS_VALUE9)), COMMON_RADIOBUTTON1,
                        expectedDescriptionOfTheFoundElement(xpath(RADIO_BUTTON_XPATH),
                                NOT(cssContains(CSS18, CSS_VALUE9)))},

                {xpath(RADIO_BUTTON_XPATH), cssMatches(CSS18, compile(CSS_VALUE9)), COMMON_RADIOBUTTON3,
                        expectedDescriptionOfTheFoundElement(xpath(RADIO_BUTTON_XPATH),
                                cssMatches(CSS18, compile(CSS_VALUE9)))},

                {xpath(RADIO_BUTTON_XPATH), NOT(cssMatches(CSS18, compile(CSS_VALUE9))), COMMON_RADIOBUTTON1,
                        expectedDescriptionOfTheFoundElement(xpath(RADIO_BUTTON_XPATH),
                                NOT(cssMatches(CSS18, compile(CSS_VALUE9))))},

                {xpath(TEXT_FIELD_XPATH), text(INPUT_TEXT2), COMMON_TEXT_INPUT2,
                        expectedDescriptionOfTheFoundElement(xpath(TEXT_FIELD_XPATH), text(INPUT_TEXT2))},

                {xpath(TEXT_FIELD_XPATH), NOT(text(INPUT_TEXT2)), COMMON_LABELED_INPUT1,
                        expectedDescriptionOfTheFoundElement(xpath(TEXT_FIELD_XPATH), NOT(text(INPUT_TEXT2)))},

                {xpath(TEXT_FIELD_XPATH), textMatches(compile(INPUT_TEXT2)), COMMON_TEXT_INPUT2,
                        expectedDescriptionOfTheFoundElement(xpath(TEXT_FIELD_XPATH), textMatches(compile(INPUT_TEXT2)))},

                {xpath(TEXT_FIELD_XPATH), NOT(textMatches(compile(INPUT_TEXT2))), COMMON_LABELED_INPUT1,
                        expectedDescriptionOfTheFoundElement(xpath(TEXT_FIELD_XPATH), NOT(textMatches(compile(INPUT_TEXT2))))}
        };
    }

    @Test(dataProvider = "search criteria1", retryAnalyzer = RetryAnalyzer.class)
    public void findElementByCriteriaTest(By by,
                                          Criteria<WebElement> criteria,
                                          WebElement expected,
                                          String expectedElementDescription) {
        setStartBenchMark();
        WebElement webElement = seleniumSteps.find(webElement(by).criteria(criteria));
        setEndBenchMark();
        assertThat(new BigDecimal(getTimeDifference()),
                either(closeTo(new BigDecimal(HALF_SECOND.toMillis()), new BigDecimal(450)))
                        .or(closeTo(new BigDecimal(ONE_SECOND.toMillis()), new BigDecimal(200))));
        assertThat(webElement, equalTo(expected));
        assertThat(webElement.toString(), is(expectedElementDescription));
    }

    @Test(dataProvider = "search criteria1", retryAnalyzer = RetryAnalyzer.class)
    public void findElementByCriteriaWithDefinedTimeTest(By by,
                                                         Criteria<WebElement> criteria,
                                                         WebElement expected,
                                                         String expectedElementDescription) {
        setStartBenchMark();
        WebElement webElement = seleniumSteps.find(webElement(by)
                .timeOut(FIVE_SECONDS)
                .criteria(criteria));
        setEndBenchMark();
        assertThat(new BigDecimal(getTimeDifference()),
                either(closeTo(new BigDecimal(HALF_SECOND.toMillis()), new BigDecimal(450)))
                        .or(closeTo(new BigDecimal(ONE_SECOND.toMillis()), new BigDecimal(200))));
        assertThat(webElement, equalTo(expected));
        assertThat(webElement.toString(), is(expectedElementDescription));
    }

    @Test(dataProvider = "search criteria1", retryAnalyzer = RetryAnalyzer.class)
    public void findElementByCriteriaWithTimeDefinedImplicitlyTest(By by,
                                                                   Criteria<WebElement> criteria,
                                                                   WebElement expected,
                                                                   String expectedElementDescription) {
        setProperty(ELEMENT_WAITING_TIME_UNIT.getPropertyName(), "SECONDS");
        setProperty(ELEMENT_WAITING_TIME_VALUE.getPropertyName(), "5");
        try {
            setStartBenchMark();
            WebElement webElement = seleniumSteps.find(webElement(by).criteria(criteria));
            setEndBenchMark();
            assertThat(new BigDecimal(getTimeDifference()),
                    either(closeTo(new BigDecimal(HALF_SECOND.toMillis()), new BigDecimal(450)))
                            .or(closeTo(new BigDecimal(ONE_SECOND.toMillis()), new BigDecimal(200))));
            assertThat(webElement, equalTo(expected));
            assertThat(webElement.toString(), is(expectedElementDescription));
        } finally {
            removeProperty(ELEMENT_WAITING_TIME_UNIT.getPropertyName());
            removeProperty(ELEMENT_WAITING_TIME_VALUE.getPropertyName());
        }
    }
}
