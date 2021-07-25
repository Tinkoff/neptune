package ru.tinkoff.qa.neptune.selenium.test.elements.searching;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import ru.tinkoff.qa.neptune.core.api.steps.Criteria;
import ru.tinkoff.qa.neptune.selenium.test.BaseWebDriverTest;
import ru.tinkoff.qa.neptune.selenium.test.RetryAnalyzer;

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

    @Test(retryAnalyzer = RetryAnalyzer.class)
    public void findWebElementFirstLevelWithoutConditionTest() {
        WebElement webElement = seleniumSteps.find(webElement(tagName(BUTTON_TAG)));
        assertThat(webElement, equalTo(COMMON_BUTTON1));
    }

    @Test(retryAnalyzer = RetryAnalyzer.class)
    public void findWebElementFirstLevelOnlyVisibleImplicitConditionTest() {
        setProperty(FIND_ONLY_VISIBLE_ELEMENTS.getName(), "true");
        try {
            WebElement webElement = seleniumSteps.find(webElement(tagName(BUTTON_TAG)));
            assertThat(webElement, equalTo(COMMON_BUTTON2));
        } finally {
            removeProperty(FIND_ONLY_VISIBLE_ELEMENTS.getName());
        }
    }

    @Test(retryAnalyzer = RetryAnalyzer.class)
    public void findWebElementFirstLevelOnlyWhenTimeIsDefinedTest() {
        setStartBenchMark();
        WebElement webElement = seleniumSteps.find(webElement(tagName(TABLE))
                .timeOut(FIVE_SECONDS));
        setEndBenchMark();
        assertThat(getTimeDifference(), lessThan(150L));
        assertThat(webElement, equalTo(COMMON_LABELED_TABLE1));
    }

    @Test(retryAnalyzer = RetryAnalyzer.class)
    public void findWebElementFirstLevelOnlyWhenTimeIsDefinedImplicitlyTest() {
        setProperty(ELEMENT_WAITING_TIME_UNIT.getName(), "SECONDS");
        setProperty(ELEMENT_WAITING_TIME_VALUE.getName(), "5");
        try {
            setStartBenchMark();
            WebElement webElement = seleniumSteps.find(webElement(tagName(TABLE)));
            setEndBenchMark();
            assertThat(getTimeDifference(), lessThan(150L));
            assertThat(webElement, equalTo(COMMON_LABELED_TABLE1));
        } finally {
            removeProperty(ELEMENT_WAITING_TIME_UNIT.getName());
            removeProperty(ELEMENT_WAITING_TIME_VALUE.getName());
        }
    }

    @Test(retryAnalyzer = RetryAnalyzer.class)
    public void findWebElementChainedWithoutConditionTest() {
        WebElement webElement = seleniumSteps.find(webElement(className(CUSTOM_BUTTON_CLASS))
                .foundFrom(webElement(className(SPREAD_SHEET_CLASS))));
        assertThat(webElement, equalTo(CUSTOM_LABELED_BUTTON1));
    }

    @Test(retryAnalyzer = RetryAnalyzer.class)
    public void findWebElementChainedOnlyVisibleImplicitConditionTest() {
        setProperty(FIND_ONLY_VISIBLE_ELEMENTS.getName(), "true");
        try {
            WebElement webElement = seleniumSteps.find(webElement(className(CUSTOM_BUTTON_CLASS))
                    .foundFrom(webElement(className(SPREAD_SHEET_CLASS))));
            assertThat(webElement, equalTo(CUSTOM_LABELED_BUTTON1));
        } finally {
            removeProperty(FIND_ONLY_VISIBLE_ELEMENTS.getName());
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
        assertThat(getTimeDifference(), lessThan(150L));
        assertThat(webElement, equalTo(CUSTOM_LABELED_BUTTON1));
    }

    @Test(retryAnalyzer = RetryAnalyzer.class)
    public void findWebElementChainedOnlyWhenTimeIsDefinedImplicitlyTest() {
        setProperty(ELEMENT_WAITING_TIME_UNIT.getName(), "SECONDS");
        setProperty(ELEMENT_WAITING_TIME_VALUE.getName(), "5");
        try {
            setStartBenchMark();
            WebElement webElement = seleniumSteps.find(webElement(className(CUSTOM_BUTTON_CLASS))
                    .foundFrom(webElement(className(SPREAD_SHEET_CLASS))));
            setEndBenchMark();
            assertThat(getTimeDifference(), lessThan(150L));
            assertThat(webElement, equalTo(CUSTOM_LABELED_BUTTON1));
        } finally {
            removeProperty(ELEMENT_WAITING_TIME_UNIT.getName());
            removeProperty(ELEMENT_WAITING_TIME_VALUE.getName());
        }
    }

    @Test(retryAnalyzer = RetryAnalyzer.class)
    public void findWebElementFromAnotherWithoutConditionTest() {
        WebElement spreadSheet = seleniumSteps.find(webElement(className(SPREAD_SHEET_CLASS)));
        WebElement webElement = seleniumSteps.find(webElement(className(CUSTOM_BUTTON_CLASS))
                .foundFrom(spreadSheet));
        assertThat(webElement, equalTo(CUSTOM_LABELED_BUTTON1));
    }

    @Test(retryAnalyzer = RetryAnalyzer.class)
    public void findWebElementFromAnotherOnlyVisibleImplicitConditionTest() {
        setProperty(FIND_ONLY_VISIBLE_ELEMENTS.getName(), "true");
        try {
            WebElement spreadSheet = seleniumSteps.find(webElement(className(SPREAD_SHEET_CLASS)));
            assertThat(spreadSheet.isDisplayed(), is(true));
            WebElement webElement = seleniumSteps.find(webElement(className(CUSTOM_BUTTON_CLASS))
                    .foundFrom(spreadSheet));
            assertThat(webElement, equalTo(CUSTOM_LABELED_BUTTON1));
        } finally {
            removeProperty(FIND_ONLY_VISIBLE_ELEMENTS.getName());
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
        assertThat(getTimeDifference(), lessThan(150L));
        assertThat(webElement, equalTo(CUSTOM_LABELED_BUTTON1));
    }

    @Test(retryAnalyzer = RetryAnalyzer.class)
    public void findWebElementFromAnotherOnlyWhenTimeIsDefinedImplicitlyTest() {
        setProperty(ELEMENT_WAITING_TIME_UNIT.getName(), "SECONDS");
        setProperty(ELEMENT_WAITING_TIME_VALUE.getName(), "5");
        try {
            WebElement spreadSheet = seleniumSteps.find(webElement(className(SPREAD_SHEET_CLASS)));
            setStartBenchMark();
            WebElement webElement = seleniumSteps.find(webElement(className(CUSTOM_BUTTON_CLASS))
                    .foundFrom(spreadSheet));
            setEndBenchMark();
            assertThat(getTimeDifference(), lessThan(150L));
            assertThat(webElement, equalTo(CUSTOM_LABELED_BUTTON1));
        } finally {
            removeProperty(ELEMENT_WAITING_TIME_UNIT.getName());
            removeProperty(ELEMENT_WAITING_TIME_VALUE.getName());
        }
    }

    @DataProvider(name = "search criteria1")
    public static Object[][] searchCriteria() {
        return new Object[][]{
                {tagName(BUTTON_TAG), enabled(), COMMON_BUTTON3},

                {xpath(RADIO_BUTTON_XPATH), NOT(enabled()), COMMON_RADIOBUTTON2},

                {tagName(LINK_TAG), visible(), COMMON_LABELED_LINK1},

                {tagName(LINK_TAG), NOT(visible()), COMMON_LABELED_LINK2},

                {className(MULTI_SELECT_CLASS), nested(webElements(className(ITEM_OPTION_CLASS)), 2), MULTI_SELECT4},

                {className(MULTI_SELECT_CLASS), NOT(nested(webElements(className(ITEM_OPTION_CLASS)), 2)), MULTI_SELECT1},

                {className(MULTI_SELECT_CLASS), nested(webElements(className(ITEM_OPTION_CLASS))), MULTI_SELECT1},

                {tagName(SELECT), NOT(nested(webElements(className(ITEM_OPTION_CLASS)))), COMMON_LABELED_SELECT1},

                {tagName(TEXT_AREA_TAG), attr(ATTR11, VALUE13), TEXT_AREA2},

                {tagName(TEXT_AREA_TAG), NOT(attr(ATTR11, VALUE13)), TEXT_AREA1},

                {tagName(TEXT_AREA_TAG), attrMatches(ATTR11, VALUE13), TEXT_AREA2},

                {tagName(TEXT_AREA_TAG), NOT(attrMatches(ATTR11, VALUE13)), TEXT_AREA1},

                {tagName(TEXT_AREA_TAG), attrMatches(ATTR11, VALUE13), TEXT_AREA2},

                {tagName(TEXT_AREA_TAG), NOT(attrMatches(ATTR11, VALUE13)), TEXT_AREA1},

                {xpath(RADIO_BUTTON_XPATH), css(CSS18, CSS_VALUE9), COMMON_RADIOBUTTON3},

                {xpath(RADIO_BUTTON_XPATH), NOT(css(CSS18, CSS_VALUE9)), COMMON_RADIOBUTTON1},

                {xpath(RADIO_BUTTON_XPATH), cssMatches(CSS18, CSS_VALUE9), COMMON_RADIOBUTTON3},

                {xpath(RADIO_BUTTON_XPATH), NOT(cssMatches(CSS18, CSS_VALUE9)), COMMON_RADIOBUTTON1},

                {xpath(RADIO_BUTTON_XPATH), cssMatches(CSS18, CSS_VALUE9), COMMON_RADIOBUTTON3},

                {xpath(RADIO_BUTTON_XPATH), NOT(cssMatches(CSS18, CSS_VALUE9)), COMMON_RADIOBUTTON1},

                {xpath(TEXT_FIELD_XPATH), textMatches(INPUT_TEXT2), COMMON_TEXT_INPUT2},

                {xpath(TEXT_FIELD_XPATH), NOT(textMatches(INPUT_TEXT2)), COMMON_LABELED_INPUT1}
        };
    }

    @Test(dataProvider = "search criteria1", retryAnalyzer = RetryAnalyzer.class)
    public void findElementByCriteriaTest(By by,
                                          Criteria<WebElement> criteria,
                                          WebElement expected) {
        setStartBenchMark();
        WebElement webElement = seleniumSteps.find(webElement(by).criteria(criteria));
        setEndBenchMark();
        assertThat(getTimeDifference(), lessThan(150L));
        assertThat(webElement, equalTo(expected));
    }

    @Test(dataProvider = "search criteria1", retryAnalyzer = RetryAnalyzer.class)
    public void findElementByCriteriaWithDefinedTimeTest(By by,
                                                         Criteria<WebElement> criteria,
                                                         WebElement expected) {
        setStartBenchMark();
        WebElement webElement = seleniumSteps.find(webElement(by)
                .timeOut(FIVE_SECONDS)
                .criteria(criteria));
        setEndBenchMark();
        assertThat(getTimeDifference(), lessThan(150L));
        assertThat(webElement, equalTo(expected));
    }

    @Test(dataProvider = "search criteria1", retryAnalyzer = RetryAnalyzer.class)
    public void findElementByCriteriaWithTimeDefinedImplicitlyTest(By by,
                                                                   Criteria<WebElement> criteria,
                                                                   WebElement expected) {
        setProperty(ELEMENT_WAITING_TIME_UNIT.getName(), "SECONDS");
        setProperty(ELEMENT_WAITING_TIME_VALUE.getName(), "5");
        try {
            setStartBenchMark();
            WebElement webElement = seleniumSteps.find(webElement(by).criteria(criteria));
            setEndBenchMark();
            assertThat(getTimeDifference(), lessThan(150L));
            assertThat(webElement, equalTo(expected));
        } finally {
            removeProperty(ELEMENT_WAITING_TIME_UNIT.getName());
            removeProperty(ELEMENT_WAITING_TIME_VALUE.getName());
        }
    }
}
