package ru.tinkoff.qa.neptune.selenium.test.steps.tests.searching;

import org.hamcrest.Matcher;
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
import static java.time.Duration.ofMillis;
import static java.util.regex.Pattern.compile;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.openqa.selenium.By.*;
import static ru.tinkoff.qa.neptune.core.api.steps.Criteria.*;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.CommonElementCriteria.*;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.MultipleSearchSupplier.webElements;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.SearchSupplier.webElement;
import static ru.tinkoff.qa.neptune.selenium.properties.SessionFlagProperties.FIND_ONLY_VISIBLE_ELEMENTS;
import static ru.tinkoff.qa.neptune.selenium.properties.WaitingProperties.TimeUnitProperties.ELEMENT_WAITING_TIME_UNIT;
import static ru.tinkoff.qa.neptune.selenium.properties.WaitingProperties.TimeValueProperties.ELEMENT_WAITING_TIME_VALUE;
import static ru.tinkoff.qa.neptune.selenium.test.FakeDOMModel.*;

public class SearchForWebElementsPositiveTest extends BaseWebDriverTest {

    private static final String FOUND_BY_PATTERN = "%s web elements found %s";
    private static final String FOUND_ON_CONDITION = FOUND_BY_PATTERN + " and meet criteria ['%s']";

    private static String expectedDescriptionOfTheFoundElements(int count, By by, Criteria<? extends SearchContext> condition) {
        return format(FOUND_ON_CONDITION, count, by, condition);
    }

    private static String expectedDescriptionOfTheFoundElements(int count, By by) {
        return format(FOUND_BY_PATTERN, count, by);
    }

    @Test(retryAnalyzer = RetryAnalyzer.class)
    public void findWebElementsWithoutConditionTest() {
        List<WebElement> webElements = seleniumSteps.find(webElements(tagName(BUTTON_TAG)));
        assertThat(webElements, contains(equalTo(COMMON_BUTTON1),
                equalTo(COMMON_BUTTON2),
                equalTo(COMMON_BUTTON3),
                equalTo(COMMON_BUTTON4),
                equalTo(COMMON_LABELED_BUTTON1),
                equalTo(COMMON_LABELED_BUTTON2),
                equalTo(COMMON_LABELED_BUTTON3),
                equalTo(COMMON_LABELED_BUTTON4)));
        assertThat(webElements.toString(), is(expectedDescriptionOfTheFoundElements(8, tagName(BUTTON_TAG))));
    }

    @Test(retryAnalyzer = RetryAnalyzer.class)
    public void findWebElementsOnlyVisibleImplicitConditionTest() {
        setProperty(FIND_ONLY_VISIBLE_ELEMENTS.getPropertyName(), "true");
        try {
            List<WebElement> webElements = seleniumSteps.find(webElements(tagName(BUTTON_TAG)));
            assertThat(webElements, contains(equalTo(COMMON_BUTTON2), equalTo(COMMON_BUTTON3),
                    equalTo(COMMON_LABELED_BUTTON1), equalTo(COMMON_LABELED_BUTTON4)));
            assertThat(webElements.toString(), is(expectedDescriptionOfTheFoundElements(4, tagName(BUTTON_TAG),
                    visible())));
        } finally {
            removeProperty(FIND_ONLY_VISIBLE_ELEMENTS.getPropertyName());
        }
    }

    @Test(retryAnalyzer = RetryAnalyzer.class)
    public void findWebElementsOnlyWhenTimeIsDefinedTest() {
        setStartBenchMark();
        List<WebElement> webElements = seleniumSteps.find(webElements(tagName(TABLE)).timeOut(FIVE_SECONDS));
        setEndBenchMark();
        assertThat(getTimeDifference(), lessThan(HALF_SECOND.toMillis()));
        assertThat(webElements,
                contains(equalTo(COMMON_LABELED_TABLE1),
                        equalTo(COMMON_LABELED_TABLE2),
                        equalTo(COMMON_LABELED_TABLE3),
                        equalTo(COMMON_LABELED_TABLE4),
                        equalTo(COMMON_TABLE1),
                        equalTo(COMMON_TABLE2),
                        equalTo(COMMON_TABLE3),
                        equalTo(COMMON_TABLE4)));
        assertThat(webElements.toString(), is(expectedDescriptionOfTheFoundElements(8, tagName(TABLE))));
    }

    @Test(retryAnalyzer = RetryAnalyzer.class)
    public void findWebElementsOnlyWhenTimeIsDefinedImplicitlyTest() {
        setProperty(ELEMENT_WAITING_TIME_UNIT.getPropertyName(), "SECONDS");
        setProperty(ELEMENT_WAITING_TIME_VALUE.getPropertyName(), "5");
        try {
            setStartBenchMark();
            List<WebElement> webElements = seleniumSteps.find(webElements(tagName(TABLE)));
            setEndBenchMark();
            assertThat(getTimeDifference(), lessThan(HALF_SECOND.toMillis()));
            assertThat(webElements,
                    contains(equalTo(COMMON_LABELED_TABLE1),
                            equalTo(COMMON_LABELED_TABLE2),
                            equalTo(COMMON_LABELED_TABLE3),
                            equalTo(COMMON_LABELED_TABLE4),
                            equalTo(COMMON_TABLE1),
                            equalTo(COMMON_TABLE2),
                            equalTo(COMMON_TABLE3),
                            equalTo(COMMON_TABLE4)));
            assertThat(webElements.toString(), is(expectedDescriptionOfTheFoundElements(8, tagName(TABLE))));
        } finally {
            removeProperty(ELEMENT_WAITING_TIME_UNIT.getPropertyName());
            removeProperty(ELEMENT_WAITING_TIME_VALUE.getPropertyName());
        }
    }

    @Test(retryAnalyzer = RetryAnalyzer.class)
    public void findWebElementsChainedWithoutConditionTest() {
        List<WebElement> webElements = seleniumSteps.find(webElements(className(CUSTOM_BUTTON_CLASS))
                .foundFrom(webElement(className(SPREAD_SHEET_CLASS))));
        assertThat(webElements, contains(equalTo(CUSTOM_LABELED_BUTTON1)));
        assertThat(webElements.toString(), is(expectedDescriptionOfTheFoundElements(1, className(CUSTOM_BUTTON_CLASS))));
    }

    @Test(retryAnalyzer = RetryAnalyzer.class)
    public void findWebElementsChainedOnlyVisibleImplicitConditionTest() {
        setProperty(FIND_ONLY_VISIBLE_ELEMENTS.getPropertyName(), "true");
        try {
            List<WebElement> webElements = seleniumSteps.find(webElements(className(CUSTOM_BUTTON_CLASS))
                    .foundFrom(webElement(className(SPREAD_SHEET_CLASS))));
            assertThat(webElements, contains(equalTo(CUSTOM_LABELED_BUTTON1)));
            assertThat(webElements.toString(), is(expectedDescriptionOfTheFoundElements(1, className(CUSTOM_BUTTON_CLASS),
                    visible())));
        } finally {
            removeProperty(FIND_ONLY_VISIBLE_ELEMENTS.getPropertyName());
        }
    }

    @Test(retryAnalyzer = RetryAnalyzer.class)
    public void findWebElementsChainedOnlyWhenTimeIsDefinedTest() {
        setStartBenchMark();
        List<WebElement> webElements = seleniumSteps.find(webElements(className(CUSTOM_BUTTON_CLASS)).timeOut(FIVE_SECONDS)
                .foundFrom(webElement(className(SPREAD_SHEET_CLASS)).timeOut(FIVE_SECONDS)));
        setEndBenchMark();
        assertThat(getTimeDifference(), lessThan(HALF_SECOND.toMillis()));
        assertThat(webElements, contains(equalTo(CUSTOM_LABELED_BUTTON1)));
        assertThat(webElements.toString(), is(expectedDescriptionOfTheFoundElements(1, className(CUSTOM_BUTTON_CLASS))));
    }

    @Test(retryAnalyzer = RetryAnalyzer.class)
    public void findWebElementsChainedOnlyWhenTimeIsDefinedImplicitlyTest() {
        setProperty(ELEMENT_WAITING_TIME_UNIT.getPropertyName(), "SECONDS");
        setProperty(ELEMENT_WAITING_TIME_VALUE.getPropertyName(), "5");
        try {
            setStartBenchMark();
            List<WebElement> webElements = seleniumSteps.find(webElements(className(CUSTOM_BUTTON_CLASS))
                    .foundFrom(webElement(className(SPREAD_SHEET_CLASS))));
            setEndBenchMark();
            assertThat(getTimeDifference(), lessThan(HALF_SECOND.toMillis()));
            assertThat(webElements, contains(equalTo(CUSTOM_LABELED_BUTTON1)));
            assertThat(webElements.toString(), is(expectedDescriptionOfTheFoundElements(1, className(CUSTOM_BUTTON_CLASS))));
        } finally {
            removeProperty(ELEMENT_WAITING_TIME_UNIT.getPropertyName());
            removeProperty(ELEMENT_WAITING_TIME_VALUE.getPropertyName());
        }
    }

    @Test(retryAnalyzer = RetryAnalyzer.class)
    public void findWebElementsFromAnotherWithoutConditionTest() {
        WebElement spreadSheet = seleniumSteps.find(webElement(className(SPREAD_SHEET_CLASS)));
        List<WebElement> webElements = seleniumSteps.find(webElements(className(CUSTOM_BUTTON_CLASS))
                .foundFrom(spreadSheet));
        assertThat(webElements, contains(equalTo(CUSTOM_LABELED_BUTTON1)));
        assertThat(webElements.toString(), is(expectedDescriptionOfTheFoundElements(1, className(CUSTOM_BUTTON_CLASS))));
    }

    @Test(retryAnalyzer = RetryAnalyzer.class)
    public void findWebElementsFromAnotherOnlyVisibleImplicitConditionTest() {
        setProperty(FIND_ONLY_VISIBLE_ELEMENTS.getPropertyName(), "true");
        try {
            WebElement spreadSheet = seleniumSteps.find(webElement(className(SPREAD_SHEET_CLASS)));
            assertThat(spreadSheet.isDisplayed(), is(true));
            List<WebElement> webElements = seleniumSteps.find(webElements(className(CUSTOM_BUTTON_CLASS))
                    .foundFrom(spreadSheet));
            assertThat(webElements, contains(equalTo(CUSTOM_LABELED_BUTTON1)));
            assertThat(webElements.toString(), is(expectedDescriptionOfTheFoundElements(1, className(CUSTOM_BUTTON_CLASS),
                    visible())));
        } finally {
            removeProperty(FIND_ONLY_VISIBLE_ELEMENTS.getPropertyName());
        }
    }

    @Test(retryAnalyzer = RetryAnalyzer.class)
    public void findWebElementsFromAnotherOnlyWhenTimeIsDefinedTest() {
        WebElement spreadSheet = seleniumSteps.find(webElement(className(SPREAD_SHEET_CLASS)));
        setStartBenchMark();
        List<WebElement> webElements = seleniumSteps.find(webElements(className(CUSTOM_BUTTON_CLASS)).timeOut(FIVE_SECONDS)
                .foundFrom(spreadSheet));
        setEndBenchMark();
        assertThat(getTimeDifference(), lessThan(HALF_SECOND.toMillis()));
        assertThat(webElements, contains(equalTo(CUSTOM_LABELED_BUTTON1)));
        assertThat(webElements.toString(), is(expectedDescriptionOfTheFoundElements(1, className(CUSTOM_BUTTON_CLASS))));
    }

    @Test(retryAnalyzer = RetryAnalyzer.class)
    public void findWebElementsFromAnotherOnlyWhenTimeIsDefinedImplicitlyTest() {
        setProperty(ELEMENT_WAITING_TIME_UNIT.getPropertyName(), "SECONDS");
        setProperty(ELEMENT_WAITING_TIME_VALUE.getPropertyName(), "5");
        try {
            WebElement spreadSheet = seleniumSteps.find(webElement(className(SPREAD_SHEET_CLASS)));
            setStartBenchMark();
            List<WebElement> webElements = seleniumSteps.find(webElements(className(CUSTOM_BUTTON_CLASS))
                    .foundFrom(spreadSheet));
            setEndBenchMark();
            assertThat(getTimeDifference(), lessThan(HALF_SECOND.toMillis()));
            assertThat(webElements, contains(equalTo(CUSTOM_LABELED_BUTTON1)));
            assertThat(webElements.toString(), is(expectedDescriptionOfTheFoundElements(1, className(CUSTOM_BUTTON_CLASS))));
        } finally {
            removeProperty(ELEMENT_WAITING_TIME_UNIT.getPropertyName());
            removeProperty(ELEMENT_WAITING_TIME_VALUE.getPropertyName());
        }
    }

    @DataProvider(name = "search criteria1")
    public static Object[][] searchCriteria() {
        return new Object[][]{
                {tagName(BUTTON_TAG), enabled(),
                        contains(equalTo(COMMON_BUTTON3),
                                equalTo(COMMON_BUTTON4),
                                equalTo(COMMON_LABELED_BUTTON2),
                                equalTo(COMMON_LABELED_BUTTON4)),
                        expectedDescriptionOfTheFoundElements(4, tagName(BUTTON_TAG), enabled())},

                {xpath(RADIO_BUTTON_XPATH), NOT(enabled()),
                        contains(equalTo(COMMON_RADIOBUTTON2),
                                equalTo(COMMON_LABELED_RADIOBUTTON4),
                                equalTo(COMMON_LABELED_RADIOBUTTON5),
                                equalTo(COMMON_LABELED_RADIOBUTTON8)),
                        expectedDescriptionOfTheFoundElements(4, xpath(RADIO_BUTTON_XPATH), NOT(enabled()))},

                {tagName(LINK_TAG), visible(),
                        contains(
                                equalTo(COMMON_LABELED_LINK1),
                                equalTo(COMMON_LABELED_LINK4),
                                equalTo(COMMON_LINK2),
                                equalTo(COMMON_LINK3)),
                        expectedDescriptionOfTheFoundElements(4, tagName(LINK_TAG), visible())},

                {tagName(LINK_TAG), NOT(visible()),
                        contains(
                                equalTo(COMMON_LABELED_LINK2),
                                equalTo(COMMON_LABELED_LINK3),
                                equalTo(COMMON_LINK1),
                                equalTo(COMMON_LINK4)),
                        expectedDescriptionOfTheFoundElements(4, tagName(LINK_TAG), NOT(visible()))},

                {className(MULTI_SELECT_CLASS),
                        nestedElements(webElements(className(ITEM_OPTION_CLASS)).timeOut(FIVE_SECONDS), 2),
                        contains(equalTo(MULTI_SELECT4)),
                        expectedDescriptionOfTheFoundElements(1, className(MULTI_SELECT_CLASS),
                                nestedElements(webElements(className(ITEM_OPTION_CLASS)).timeOut(FIVE_SECONDS), 2))},

                {className(MULTI_SELECT_CLASS),
                        NOT(nestedElements(webElements(className(ITEM_OPTION_CLASS)).timeOut(FIVE_SECONDS), 2)),
                        contains(equalTo(MULTI_SELECT1),
                                equalTo(MULTI_SELECT2),
                                equalTo(MULTI_SELECT3)),
                        expectedDescriptionOfTheFoundElements(3, className(MULTI_SELECT_CLASS),
                                NOT(nestedElements(webElements(className(ITEM_OPTION_CLASS)).timeOut(FIVE_SECONDS), 2)))},

                {className(MULTI_SELECT_CLASS),
                        nestedElements(webElements(className(ITEM_OPTION_CLASS)).timeOut(FIVE_SECONDS)),
                        contains(equalTo(MULTI_SELECT1),
                                equalTo(MULTI_SELECT2),
                                equalTo(MULTI_SELECT3),
                                equalTo(MULTI_SELECT4)),
                        expectedDescriptionOfTheFoundElements(4, className(MULTI_SELECT_CLASS),
                                nestedElements(webElements(className(ITEM_OPTION_CLASS)).timeOut(FIVE_SECONDS)))},

                {tagName(SELECT),
                        NOT(nestedElements(webElements(className(ITEM_OPTION_CLASS)).timeOut(ofMillis(1)))),
                        contains(equalTo(COMMON_LABELED_SELECT1),
                                equalTo(COMMON_LABELED_SELECT2),
                                equalTo(COMMON_LABELED_SELECT3),
                                equalTo(COMMON_LABELED_SELECT4),
                                equalTo(COMMON_SELECT1),
                                equalTo(COMMON_SELECT2),
                                equalTo(COMMON_SELECT3),
                                equalTo(COMMON_SELECT4)),
                        expectedDescriptionOfTheFoundElements(8, tagName(SELECT),
                                NOT(nestedElements(webElements(className(ITEM_OPTION_CLASS)).timeOut(ofMillis(1)))))},

                {tagName(TEXT_AREA_TAG),
                        attribute(ATTR11, VALUE13),
                        contains(equalTo(TEXT_AREA2),
                                equalTo(TEXT_AREA2)),
                        expectedDescriptionOfTheFoundElements(2, tagName(TEXT_AREA_TAG),
                                attribute(ATTR11, VALUE13))},

                {tagName(TEXT_AREA_TAG),
                        NOT(attribute(ATTR11, VALUE13)),
                        contains(equalTo(TEXT_AREA1),
                                equalTo(TEXT_AREA3),
                                equalTo(TEXT_AREA4),
                                equalTo(TEXT_AREA1)),
                        expectedDescriptionOfTheFoundElements(4, tagName(TEXT_AREA_TAG),
                                NOT(attribute(ATTR11, VALUE13)))},

                {tagName(TEXT_AREA_TAG), attributeContains(ATTR11, "13"),
                        contains(equalTo(TEXT_AREA2),
                                equalTo(TEXT_AREA2)),
                        expectedDescriptionOfTheFoundElements(2, tagName(TEXT_AREA_TAG),
                                attributeContains(ATTR11, "13"))},

                {tagName(TEXT_AREA_TAG),
                        NOT(attributeContains(ATTR11, "13")),
                        contains(equalTo(TEXT_AREA1),
                                equalTo(TEXT_AREA3),
                                equalTo(TEXT_AREA4),
                                equalTo(TEXT_AREA1)),
                        expectedDescriptionOfTheFoundElements(4, tagName(TEXT_AREA_TAG),
                                NOT(attributeContains(ATTR11, "13")))},

                {tagName(TEXT_AREA_TAG), attributeMatches(ATTR11, compile("13")),
                        contains(equalTo(TEXT_AREA2),
                                equalTo(TEXT_AREA2)),
                        expectedDescriptionOfTheFoundElements(2, tagName(TEXT_AREA_TAG),
                                attributeMatches(ATTR11, compile("13")))},

                {tagName(TEXT_AREA_TAG),
                        NOT(attributeMatches(ATTR11, compile("13"))),
                        contains(equalTo(TEXT_AREA1),
                                equalTo(TEXT_AREA3),
                                equalTo(TEXT_AREA4),
                                equalTo(TEXT_AREA1)),
                        expectedDescriptionOfTheFoundElements(4, tagName(TEXT_AREA_TAG),
                                NOT(attributeMatches(ATTR11, compile("13"))))},

                {xpath(RADIO_BUTTON_XPATH),
                        css(CSS18, CSS_VALUE9),
                        contains(equalTo(COMMON_RADIOBUTTON3)),
                        expectedDescriptionOfTheFoundElements(1, xpath(RADIO_BUTTON_XPATH),
                                css(CSS18, CSS_VALUE9))},

                {xpath(RADIO_BUTTON_XPATH),
                        NOT(css(CSS18, CSS_VALUE9)),
                        contains(equalTo(COMMON_RADIOBUTTON1),
                                equalTo(COMMON_RADIOBUTTON2),
                                equalTo(COMMON_RADIOBUTTON4),
                                equalTo(COMMON_LABELED_RADIOBUTTON1),
                                equalTo(COMMON_LABELED_RADIOBUTTON2),
                                equalTo(COMMON_LABELED_RADIOBUTTON3),
                                equalTo(COMMON_LABELED_RADIOBUTTON4),
                                equalTo(COMMON_LABELED_RADIOBUTTON5),
                                equalTo(COMMON_LABELED_RADIOBUTTON6),
                                equalTo(COMMON_LABELED_RADIOBUTTON7),
                                equalTo(COMMON_LABELED_RADIOBUTTON8)),
                        expectedDescriptionOfTheFoundElements(11,
                                xpath(RADIO_BUTTON_XPATH), NOT(css(CSS18, CSS_VALUE9)))},

                {xpath(CHECK_BOX_XPATH),
                        OR(cssContains(CSS13, "15"),
                                cssContains(CSS2, "17")),
                        contains(
                                equalTo(COMMON_CHECKBOX4),
                                equalTo(COMMON_LABELED_CHECKBOX1)),
                        expectedDescriptionOfTheFoundElements(2,
                                xpath(CHECK_BOX_XPATH), OR(cssContains(CSS13, "15"),
                                        cssContains(CSS2, "17")))},

                {xpath(CHECK_BOX_XPATH),
                        NOT(cssContains(CSS13, "15"),
                                cssContains(CSS2, "17")),
                        contains(equalTo(COMMON_CHECKBOX1),
                                equalTo(COMMON_CHECKBOX2),
                                equalTo(COMMON_CHECKBOX3),
                                equalTo(COMMON_LABELED_CHECKBOX2),
                                equalTo(COMMON_LABELED_CHECKBOX3),
                                equalTo(COMMON_LABELED_CHECKBOX4),
                                equalTo(COMMON_LABELED_CHECKBOX5),
                                equalTo(COMMON_LABELED_CHECKBOX6),
                                equalTo(COMMON_LABELED_CHECKBOX7),
                                equalTo(COMMON_LABELED_CHECKBOX8),
                                equalTo(COMMON_LABELED_CHECKBOX5),
                                equalTo(COMMON_LABELED_CHECKBOX6)),
                        expectedDescriptionOfTheFoundElements(12, xpath(CHECK_BOX_XPATH),
                                NOT(cssContains(CSS13, "15"),
                                        cssContains(CSS2, "17")))},

                {className(TAB_CLASS),
                        AND(cssMatches(CSS15, compile("1")),
                                cssMatches(CSS20, compile(CSS_VALUE2))),
                        contains(equalTo(CUSTOM_LABELED_TAB1),
                                equalTo(CUSTOM_LABELED_TAB3)),
                        expectedDescriptionOfTheFoundElements(2, className(TAB_CLASS),
                                AND(cssMatches(CSS15, compile("1")),
                                        cssMatches(CSS20, compile(CSS_VALUE2))))},

                {className(TAB_CLASS),
                        NOT(cssMatches(CSS15, compile("1")),
                                cssMatches(CSS20, compile(CSS_VALUE2))),
                        contains(equalTo(CUSTOM_LABELED_TAB2),
                                equalTo(CUSTOM_LABELED_TAB4)),
                        expectedDescriptionOfTheFoundElements(2, className(TAB_CLASS),
                                NOT(cssMatches(CSS15, compile("1")),
                                        cssMatches(CSS20, compile(CSS_VALUE2))))},

                {xpath(TEXT_FIELD_XPATH),
                        text(INPUT_TEXT2),
                        contains(equalTo(COMMON_TEXT_INPUT2)),
                        expectedDescriptionOfTheFoundElements(1,
                                xpath(TEXT_FIELD_XPATH), text(INPUT_TEXT2))},

                {xpath(TEXT_FIELD_XPATH),
                        NOT(text(INPUT_TEXT2)),
                        contains(equalTo(COMMON_LABELED_INPUT1),
                                equalTo(COMMON_LABELED_INPUT2),
                                equalTo(COMMON_LABELED_INPUT3),
                                equalTo(COMMON_LABELED_INPUT4),
                                equalTo(COMMON_TEXT_INPUT1),
                                equalTo(COMMON_TEXT_INPUT3),
                                equalTo(COMMON_TEXT_INPUT4)),
                        expectedDescriptionOfTheFoundElements(7, xpath(TEXT_FIELD_XPATH), NOT(text(INPUT_TEXT2)))},

                {xpath(TEXT_FIELD_XPATH),
                        textMatches(compile("Text")),
                        contains(equalTo(COMMON_TEXT_INPUT1),
                                equalTo(COMMON_TEXT_INPUT2),
                                equalTo(COMMON_TEXT_INPUT3),
                                equalTo(COMMON_TEXT_INPUT4)),
                        expectedDescriptionOfTheFoundElements(4,
                                xpath(TEXT_FIELD_XPATH),
                                textMatches(compile("Text")))},

                {xpath(TEXT_FIELD_XPATH), NOT(textMatches(compile("Text"))),
                        contains(equalTo(COMMON_LABELED_INPUT1),
                                equalTo(COMMON_LABELED_INPUT2),
                                equalTo(COMMON_LABELED_INPUT3),
                                equalTo(COMMON_LABELED_INPUT4)),
                        expectedDescriptionOfTheFoundElements(4,
                                xpath(TEXT_FIELD_XPATH),
                                NOT(textMatches(compile("Text"))))}
        };
    }

    @Test(dataProvider = "search criteria1", retryAnalyzer = RetryAnalyzer.class)
    public void findElementsByCriteriaTest(By by, Criteria<WebElement> criteria,
                                           Matcher<List<WebElement>> matcher,
                                           String expectedListDescription) {
        setStartBenchMark();
        List<WebElement> webElements = seleniumSteps.find(webElements(by).criteria(criteria));
        setEndBenchMark();
        assertThat(new BigDecimal(getTimeDifference()),
                either(closeTo(new BigDecimal(HALF_SECOND.toMillis()), new BigDecimal(450)))
                        .or(closeTo(new BigDecimal(ONE_SECOND.toMillis()), new BigDecimal(200))));
        assertThat(webElements, matcher);
        assertThat(webElements.toString(), is(expectedListDescription));
    }

    @Test(dataProvider = "search criteria1", retryAnalyzer = RetryAnalyzer.class)
    public void findElementsByCriteriaWithDefinedTimeTest(By by, Criteria<WebElement> criteria,
                                                          Matcher<List<WebElement>> matcher,
                                                          String expectedListDescription) {
        setStartBenchMark();
        List<WebElement> webElements = seleniumSteps.find(webElements(by)
                .timeOut(FIVE_SECONDS)
                .criteria(criteria));
        setEndBenchMark();
        assertThat(new BigDecimal(getTimeDifference()),
                either(closeTo(new BigDecimal(HALF_SECOND.toMillis()), new BigDecimal(450)))
                        .or(closeTo(new BigDecimal(ONE_SECOND.toMillis()), new BigDecimal(200))));
        assertThat(webElements, matcher);
        assertThat(webElements.toString(), is(expectedListDescription));
    }

    @Test(dataProvider = "search criteria1", retryAnalyzer = RetryAnalyzer.class)
    public void findElementsByCriteriaWithTimeDefinedImplicitlyTest(By by, Criteria<WebElement> criteria,
                                                                    Matcher<List<WebElement>> matcher,
                                                                    String expectedListDescription) {
        setProperty(ELEMENT_WAITING_TIME_UNIT.getPropertyName(), "SECONDS");
        setProperty(ELEMENT_WAITING_TIME_VALUE.getPropertyName(), "5");
        try {
            setStartBenchMark();
            List<WebElement> webElements = seleniumSteps.find(webElements(by).criteria(criteria));
            setEndBenchMark();
            assertThat(new BigDecimal(getTimeDifference()),
                    either(closeTo(new BigDecimal(HALF_SECOND.toMillis()), new BigDecimal(450)))
                            .or(closeTo(new BigDecimal(ONE_SECOND.toMillis()), new BigDecimal(200))));
            assertThat(webElements, matcher);
            assertThat(webElements.toString(), is(expectedListDescription));
        } finally {
            removeProperty(ELEMENT_WAITING_TIME_UNIT.getPropertyName());
            removeProperty(ELEMENT_WAITING_TIME_VALUE.getPropertyName());
        }
    }
}
