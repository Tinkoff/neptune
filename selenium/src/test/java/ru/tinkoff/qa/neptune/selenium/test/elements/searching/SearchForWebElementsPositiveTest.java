package ru.tinkoff.qa.neptune.selenium.test.elements.searching;

import org.hamcrest.Matcher;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import ru.tinkoff.qa.neptune.core.api.steps.Criteria;
import ru.tinkoff.qa.neptune.selenium.test.BaseWebDriverTest;
import ru.tinkoff.qa.neptune.selenium.test.RetryAnalyzer;

import java.math.BigDecimal;
import java.util.List;

import static java.time.Duration.ofMillis;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.openqa.selenium.By.*;
import static ru.tinkoff.qa.neptune.core.api.hamcrest.iterables.SetOfObjectsItemsMatcher.iterableHasItem;
import static ru.tinkoff.qa.neptune.core.api.steps.Criteria.*;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.CommonElementCriteria.*;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.MultipleSearchSupplier.webElements;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.SearchSupplier.webElement;
import static ru.tinkoff.qa.neptune.selenium.hamcrest.matchers.elements.HasChildElementMatcher.hasChildElement;
import static ru.tinkoff.qa.neptune.selenium.hamcrest.matchers.elements.HasChildElementsMatcher.hasChildElements;
import static ru.tinkoff.qa.neptune.selenium.properties.SessionFlagProperties.FIND_ONLY_VISIBLE_ELEMENTS;
import static ru.tinkoff.qa.neptune.selenium.properties.WaitingProperties.TimeUnitProperties.ELEMENT_WAITING_TIME_UNIT;
import static ru.tinkoff.qa.neptune.selenium.properties.WaitingProperties.TimeValueProperties.ELEMENT_WAITING_TIME_VALUE;
import static ru.tinkoff.qa.neptune.selenium.test.FakeDOMModel.*;

public class SearchForWebElementsPositiveTest extends BaseWebDriverTest {

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
    }

    @Test(retryAnalyzer = RetryAnalyzer.class)
    public void findWebElementsOnlyVisibleImplicitConditionTest() {
        setProperty(FIND_ONLY_VISIBLE_ELEMENTS.getName(), "true");
        try {
            List<WebElement> webElements = seleniumSteps.find(webElements(tagName(BUTTON_TAG)));
            assertThat(webElements, contains(equalTo(COMMON_BUTTON2), equalTo(COMMON_BUTTON3),
                    equalTo(COMMON_LABELED_BUTTON1), equalTo(COMMON_LABELED_BUTTON4)));
        } finally {
            removeProperty(FIND_ONLY_VISIBLE_ELEMENTS.getName());
        }
    }

    @Test(retryAnalyzer = RetryAnalyzer.class)
    public void findWebElementsOnlyWhenTimeIsDefinedTest() {
        setStartBenchMark();
        List<WebElement> webElements = seleniumSteps.find(webElements(tagName(TABLE)).timeOut(FIVE_SECONDS));
        setEndBenchMark();
        assertThat(getTimeDifference(), lessThan(250L));
        assertThat(webElements,
            contains(equalTo(COMMON_LABELED_TABLE1),
                equalTo(COMMON_LABELED_TABLE2),
                equalTo(COMMON_LABELED_TABLE3),
                equalTo(COMMON_LABELED_TABLE4),
                equalTo(COMMON_TABLE1),
                equalTo(COMMON_TABLE2),
                equalTo(COMMON_TABLE3),
                equalTo(COMMON_TABLE4)));
    }

    @Test(retryAnalyzer = RetryAnalyzer.class)
    public void findWebElementsOnlyWhenTimeIsDefinedImplicitlyTest() {
        setProperty(ELEMENT_WAITING_TIME_UNIT.getName(), "SECONDS");
        setProperty(ELEMENT_WAITING_TIME_VALUE.getName(), "5");
        try {
            setStartBenchMark();
            List<WebElement> webElements = seleniumSteps.find(webElements(tagName(TABLE)));
            setEndBenchMark();
            assertThat(getTimeDifference(), lessThan(250L));
            assertThat(webElements,
                contains(equalTo(COMMON_LABELED_TABLE1),
                    equalTo(COMMON_LABELED_TABLE2),
                    equalTo(COMMON_LABELED_TABLE3),
                    equalTo(COMMON_LABELED_TABLE4),
                    equalTo(COMMON_TABLE1),
                    equalTo(COMMON_TABLE2),
                    equalTo(COMMON_TABLE3),
                    equalTo(COMMON_TABLE4)));
        } finally {
            removeProperty(ELEMENT_WAITING_TIME_UNIT.getName());
            removeProperty(ELEMENT_WAITING_TIME_VALUE.getName());
        }
    }

    @Test(retryAnalyzer = RetryAnalyzer.class)
    public void findWebElementsChainedWithoutConditionTest() {
        List<WebElement> webElements = seleniumSteps.find(webElements(className(CUSTOM_BUTTON_CLASS))
                .foundFrom(webElement(className(SPREAD_SHEET_CLASS))));
        assertThat(webElements, contains(equalTo(CUSTOM_LABELED_BUTTON1)));
    }

    @Test(retryAnalyzer = RetryAnalyzer.class)
    public void findWebElementsChainedOnlyVisibleImplicitConditionTest() {
        setProperty(FIND_ONLY_VISIBLE_ELEMENTS.getName(), "true");
        try {
            List<WebElement> webElements = seleniumSteps.find(webElements(className(CUSTOM_BUTTON_CLASS))
                    .foundFrom(webElement(className(SPREAD_SHEET_CLASS))));

            assertThat(webElements, contains(equalTo(CUSTOM_LABELED_BUTTON1)));

            assertThat(seleniumSteps.find(webElement(className(SPREAD_SHEET_CLASS))),
                    hasChildElements(webElements(className(CUSTOM_BUTTON_CLASS)), iterableHasItem(CUSTOM_LABELED_BUTTON1)));

            assertThat(seleniumSteps.find(webElement(className(SPREAD_SHEET_CLASS))),
                    hasChildElements(webElements(className(CUSTOM_BUTTON_CLASS))));

            assertThat(seleniumSteps.find(webElement(className(SPREAD_SHEET_CLASS))),
                    hasChildElement(webElement(className(CUSTOM_BUTTON_CLASS)), equalTo(CUSTOM_LABELED_BUTTON1)));

            assertThat(seleniumSteps.find(webElement(className(SPREAD_SHEET_CLASS))),
                    hasChildElement(webElement(className(CUSTOM_BUTTON_CLASS))));
        } finally {
            removeProperty(FIND_ONLY_VISIBLE_ELEMENTS.getName());
        }
    }

    @Test(retryAnalyzer = RetryAnalyzer.class)
    public void findWebElementsChainedOnlyWhenTimeIsDefinedTest() {
        setStartBenchMark();
        List<WebElement> webElements = seleniumSteps.find(webElements(className(CUSTOM_BUTTON_CLASS)).timeOut(FIVE_SECONDS)
                .foundFrom(webElement(className(SPREAD_SHEET_CLASS)).timeOut(FIVE_SECONDS)));
        setEndBenchMark();
        assertThat(getTimeDifference(), lessThan(250L));
        assertThat(webElements, contains(equalTo(CUSTOM_LABELED_BUTTON1)));
    }

    @Test(retryAnalyzer = RetryAnalyzer.class)
    public void findWebElementsChainedOnlyWhenTimeIsDefinedImplicitlyTest() {
        setProperty(ELEMENT_WAITING_TIME_UNIT.getName(), "SECONDS");
        setProperty(ELEMENT_WAITING_TIME_VALUE.getName(), "5");
        try {
            setStartBenchMark();
            List<WebElement> webElements = seleniumSteps.find(webElements(className(CUSTOM_BUTTON_CLASS))
                    .foundFrom(webElement(className(SPREAD_SHEET_CLASS))));
            setEndBenchMark();
            assertThat(getTimeDifference(), lessThan(250L));
            assertThat(webElements, contains(equalTo(CUSTOM_LABELED_BUTTON1)));
        } finally {
            removeProperty(ELEMENT_WAITING_TIME_UNIT.getName());
            removeProperty(ELEMENT_WAITING_TIME_VALUE.getName());
        }
    }

    @Test(retryAnalyzer = RetryAnalyzer.class)
    public void findWebElementsFromAnotherWithoutConditionTest() {
        WebElement spreadSheet = seleniumSteps.find(webElement(className(SPREAD_SHEET_CLASS)));
        List<WebElement> webElements = seleniumSteps.find(webElements(className(CUSTOM_BUTTON_CLASS))
                .foundFrom(spreadSheet));
        assertThat(webElements, contains(equalTo(CUSTOM_LABELED_BUTTON1)));
    }

    @Test(retryAnalyzer = RetryAnalyzer.class)
    public void findWebElementsFromAnotherOnlyVisibleImplicitConditionTest() {
        setProperty(FIND_ONLY_VISIBLE_ELEMENTS.getName(), "true");
        try {
            WebElement spreadSheet = seleniumSteps.find(webElement(className(SPREAD_SHEET_CLASS)));
            assertThat(spreadSheet.isDisplayed(), is(true));
            List<WebElement> webElements = seleniumSteps.find(webElements(className(CUSTOM_BUTTON_CLASS))
                    .foundFrom(spreadSheet));
            assertThat(webElements, contains(equalTo(CUSTOM_LABELED_BUTTON1)));
        } finally {
            removeProperty(FIND_ONLY_VISIBLE_ELEMENTS.getName());
        }
    }

    @Test(retryAnalyzer = RetryAnalyzer.class)
    public void findWebElementsFromAnotherOnlyWhenTimeIsDefinedTest() {
        WebElement spreadSheet = seleniumSteps.find(webElement(className(SPREAD_SHEET_CLASS)));
        setStartBenchMark();
        List<WebElement> webElements = seleniumSteps.find(webElements(className(CUSTOM_BUTTON_CLASS)).timeOut(FIVE_SECONDS)
                .foundFrom(spreadSheet));
        setEndBenchMark();
        assertThat(getTimeDifference(), lessThan(250L));
        assertThat(webElements, contains(equalTo(CUSTOM_LABELED_BUTTON1)));
    }

    @Test(retryAnalyzer = RetryAnalyzer.class)
    public void findWebElementsFromAnotherOnlyWhenTimeIsDefinedImplicitlyTest() {
        setProperty(ELEMENT_WAITING_TIME_UNIT.getName(), "SECONDS");
        setProperty(ELEMENT_WAITING_TIME_VALUE.getName(), "5");
        try {
            WebElement spreadSheet = seleniumSteps.find(webElement(className(SPREAD_SHEET_CLASS)));
            setStartBenchMark();
            List<WebElement> webElements = seleniumSteps.find(webElements(className(CUSTOM_BUTTON_CLASS))
                    .foundFrom(spreadSheet));
            setEndBenchMark();
            assertThat(getTimeDifference(), lessThan(250L));
            assertThat(webElements, contains(equalTo(CUSTOM_LABELED_BUTTON1)));
        } finally {
            removeProperty(ELEMENT_WAITING_TIME_UNIT.getName());
            removeProperty(ELEMENT_WAITING_TIME_VALUE.getName());
        }
    }

    @DataProvider(name = "search criteria1")
    public static Object[][] searchCriteria() {
        return new Object[][]{
                {tagName(BUTTON_TAG), enabled(),
                        contains(equalTo(COMMON_BUTTON3),
                                equalTo(COMMON_BUTTON4),
                                equalTo(COMMON_LABELED_BUTTON2),
                                equalTo(COMMON_LABELED_BUTTON4))},

                {xpath(RADIO_BUTTON_XPATH), NOT(enabled()),
                        contains(equalTo(COMMON_RADIOBUTTON2),
                                equalTo(COMMON_LABELED_RADIOBUTTON4),
                                equalTo(COMMON_LABELED_RADIOBUTTON5),
                                equalTo(COMMON_LABELED_RADIOBUTTON8))},

                {tagName(LINK_TAG), visible(),
                        contains(
                                equalTo(COMMON_LABELED_LINK1),
                                equalTo(COMMON_LABELED_LINK4),
                                equalTo(COMMON_LINK2),
                                equalTo(COMMON_LINK3))},

                {tagName(LINK_TAG), NOT(visible()),
                        contains(
                                equalTo(COMMON_LABELED_LINK2),
                                equalTo(COMMON_LABELED_LINK3),
                                equalTo(COMMON_LINK1),
                                equalTo(COMMON_LINK4))},

                {className(MULTI_SELECT_CLASS),
                        nested(webElements(className(ITEM_OPTION_CLASS)).timeOut(FIVE_SECONDS), 2),
                        contains(equalTo(MULTI_SELECT4))},

                {className(MULTI_SELECT_CLASS),
                        NOT(nested(webElements(className(ITEM_OPTION_CLASS)).timeOut(FIVE_SECONDS), 2)),
                        contains(equalTo(MULTI_SELECT1),
                                equalTo(MULTI_SELECT2),
                                equalTo(MULTI_SELECT3))},

                {className(MULTI_SELECT_CLASS),
                        nested(webElements(className(ITEM_OPTION_CLASS)).timeOut(FIVE_SECONDS)),
                        contains(equalTo(MULTI_SELECT1),
                                equalTo(MULTI_SELECT2),
                                equalTo(MULTI_SELECT3),
                                equalTo(MULTI_SELECT4))},

                {tagName(SELECT),
                        NOT(nested(webElements(className(ITEM_OPTION_CLASS)).timeOut(ofMillis(1)))),
                        contains(equalTo(COMMON_LABELED_SELECT1),
                                equalTo(COMMON_LABELED_SELECT2),
                                equalTo(COMMON_LABELED_SELECT3),
                                equalTo(COMMON_LABELED_SELECT4),
                                equalTo(COMMON_SELECT1),
                                equalTo(COMMON_SELECT2),
                                equalTo(COMMON_SELECT3),
                                equalTo(COMMON_SELECT4))},

                {tagName(TEXT_AREA_TAG),
                        attr(ATTR11, VALUE13),
                        contains(equalTo(TEXT_AREA2),
                                equalTo(TEXT_AREA2))},

                {tagName(TEXT_AREA_TAG),
                        NOT(attr(ATTR11, VALUE13)),
                        contains(equalTo(TEXT_AREA1),
                                equalTo(TEXT_AREA3),
                                equalTo(TEXT_AREA4),
                                equalTo(TEXT_AREA1))},

                {tagName(TEXT_AREA_TAG), attrMatches(ATTR11, "13"),
                        contains(equalTo(TEXT_AREA2),
                                equalTo(TEXT_AREA2))},

                {tagName(TEXT_AREA_TAG),
                        NOT(attrMatches(ATTR11, "13")),
                        contains(equalTo(TEXT_AREA1),
                                equalTo(TEXT_AREA3),
                                equalTo(TEXT_AREA4),
                                equalTo(TEXT_AREA1))},

                {tagName(TEXT_AREA_TAG), attrMatches(ATTR11, "13"),
                        contains(equalTo(TEXT_AREA2),
                                equalTo(TEXT_AREA2))},

                {tagName(TEXT_AREA_TAG),
                        NOT(attrMatches(ATTR11, "13")),
                        contains(equalTo(TEXT_AREA1),
                                equalTo(TEXT_AREA3),
                                equalTo(TEXT_AREA4),
                                equalTo(TEXT_AREA1))},

                {xpath(RADIO_BUTTON_XPATH),
                        css(CSS18, CSS_VALUE9),
                        contains(equalTo(COMMON_RADIOBUTTON3))},

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
                                equalTo(COMMON_LABELED_RADIOBUTTON8))},

                {xpath(CHECK_BOX_XPATH),
                        OR(cssMatches(CSS13, "15"),
                                cssMatches(CSS2, "17")),
                        contains(
                                equalTo(COMMON_CHECKBOX4),
                                equalTo(COMMON_LABELED_CHECKBOX1))},

                {xpath(CHECK_BOX_XPATH),
                        NOT(cssMatches(CSS13, "15"),
                                cssMatches(CSS2, "17")),
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
                                equalTo(COMMON_LABELED_CHECKBOX6))},

                {className(TAB_CLASS),
                        AND(cssMatches(CSS15, "1"),
                                cssMatches(CSS20, CSS_VALUE2)),
                        contains(equalTo(CUSTOM_LABELED_TAB1),
                                equalTo(CUSTOM_LABELED_TAB3))},

                {className(TAB_CLASS),
                        NOT(cssMatches(CSS15, "1"),
                                cssMatches(CSS20, CSS_VALUE2)),
                        contains(equalTo(CUSTOM_LABELED_TAB2),
                                equalTo(CUSTOM_LABELED_TAB4))},

                {xpath(TEXT_FIELD_XPATH),
                        textMatches("Text"),
                        contains(equalTo(COMMON_TEXT_INPUT1),
                                equalTo(COMMON_TEXT_INPUT2),
                                equalTo(COMMON_TEXT_INPUT3),
                                equalTo(COMMON_TEXT_INPUT4))},

                {xpath(TEXT_FIELD_XPATH), NOT(textMatches("Text")),
                        contains(equalTo(COMMON_LABELED_INPUT1),
                                equalTo(COMMON_LABELED_INPUT2),
                                equalTo(COMMON_LABELED_INPUT3),
                                equalTo(COMMON_LABELED_INPUT4))}
        };
    }

    @Test(dataProvider = "search criteria1", retryAnalyzer = RetryAnalyzer.class)
    public void findElementsByCriteriaTest(By by, Criteria<WebElement> criteria,
                                           Matcher<List<WebElement>> matcher) {
        setStartBenchMark();
        List<WebElement> webElements = seleniumSteps.find(webElements(by).criteria(criteria));
        setEndBenchMark();
        assertThat(new BigDecimal(getTimeDifference()),
                either(closeTo(new BigDecimal(150L), new BigDecimal(450)))
                        .or(closeTo(new BigDecimal(ONE_SECOND.toMillis()), new BigDecimal(200))));
        assertThat(webElements, matcher);
    }

    @Test(dataProvider = "search criteria1", retryAnalyzer = RetryAnalyzer.class)
    public void findElementsByCriteriaWithDefinedTimeTest(By by, Criteria<WebElement> criteria,
                                                          Matcher<List<WebElement>> matcher) {
        setStartBenchMark();
        List<WebElement> webElements = seleniumSteps.find(webElements(by)
                .timeOut(FIVE_SECONDS)
                .criteria(criteria));
        setEndBenchMark();
        assertThat(new BigDecimal(getTimeDifference()),
                either(closeTo(new BigDecimal(150L), new BigDecimal(450)))
                        .or(closeTo(new BigDecimal(ONE_SECOND.toMillis()), new BigDecimal(200))));
        assertThat(webElements, matcher);
    }

    @Test(dataProvider = "search criteria1", retryAnalyzer = RetryAnalyzer.class)
    public void findElementsByCriteriaWithTimeDefinedImplicitlyTest(By by, Criteria<WebElement> criteria,
                                                                    Matcher<List<WebElement>> matcher) {
        setProperty(ELEMENT_WAITING_TIME_UNIT.getName(), "SECONDS");
        setProperty(ELEMENT_WAITING_TIME_VALUE.getName(), "5");
        try {
            setStartBenchMark();
            List<WebElement> webElements = seleniumSteps.find(webElements(by).criteria(criteria));
            setEndBenchMark();
            assertThat(new BigDecimal(getTimeDifference()),
                    either(closeTo(new BigDecimal(150L), new BigDecimal(450)))
                            .or(closeTo(new BigDecimal(ONE_SECOND.toMillis()), new BigDecimal(200))));
            assertThat(webElements, matcher);
        } finally {
            removeProperty(ELEMENT_WAITING_TIME_UNIT.getName());
            removeProperty(ELEMENT_WAITING_TIME_VALUE.getName());
        }
    }
}
