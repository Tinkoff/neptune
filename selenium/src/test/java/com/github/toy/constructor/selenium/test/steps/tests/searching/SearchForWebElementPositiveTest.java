package com.github.toy.constructor.selenium.test.steps.tests.searching;

import com.github.toy.constructor.selenium.test.BaseWebDriverTest;
import org.openqa.selenium.By;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.function.Predicate;

import static com.github.toy.constructor.selenium.functions.searching.CommonConditions.*;
import static com.github.toy.constructor.selenium.functions.searching.MultipleSearchSupplier.webElements;
import static com.github.toy.constructor.selenium.functions.searching.SearchSupplier.webElement;
import static com.github.toy.constructor.selenium.properties.FlagProperties.FIND_ONLY_VISIBLE_ELEMENTS_WHEN_NO_CONDITION;
import static com.github.toy.constructor.selenium.properties.WaitingProperties.TimeUnitProperties.ELEMENT_WAITING_TIME_UNIT;
import static com.github.toy.constructor.selenium.properties.WaitingProperties.TimeValueProperties.ELEMENT_WAITING_TIME_VALUE;
import static com.github.toy.constructor.selenium.test.FakeDOMModel.*;
import static java.time.Duration.ofMillis;
import static java.util.regex.Pattern.compile;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.lessThan;
import static org.openqa.selenium.By.className;
import static org.openqa.selenium.By.tagName;
import static org.openqa.selenium.By.xpath;

@SuppressWarnings("unchecked")
public class SearchForWebElementPositiveTest extends BaseWebDriverTest {

    @Test
    public void findWebElementFirstLevelWithoutConditionTest() {
        WebElement webElement = seleniumSteps.find(webElement(tagName(BUTTON_TAG)));
        assertThat(webElement, equalTo(COMMON_BUTTON1));
        assertThat(webElement.toString(), is("Web element found By.tagName: button"));
    }

    @Test
    public void findWebElementFirstLevelOnlyVisibleImplicitConditionTest() {
        setProperty(FIND_ONLY_VISIBLE_ELEMENTS_WHEN_NO_CONDITION.getPropertyName(), "true");
        try {
            WebElement webElement = seleniumSteps.find(webElement(tagName(BUTTON_TAG)));
            assertThat(webElement, equalTo(COMMON_BUTTON2));
            assertThat(webElement.toString(), is("Web element found By.tagName: button on condition 'Should be visible'"));
        }
        finally {
            removeProperty(FIND_ONLY_VISIBLE_ELEMENTS_WHEN_NO_CONDITION.getPropertyName());
        }
    }

    @Test
    public void findWebElementFirstLevelOnlyWhenTimeIsDefinedTest() {
        setStartBenchMark();
        WebElement webElement = seleniumSteps.find(webElement(tagName(TABLE), FIVE_SECONDS));
        setEndBenchMark();
        assertThat(getTimeDifference(), lessThan(HALF_SECOND.toMillis()));
        assertThat(webElement, equalTo(COMMON_LABELED_TABLE1));
        assertThat(webElement.toString(), is("Web element found By.tagName: table"));
    }

    @Test
    public void findWebElementFirstLevelOnlyWhenTimeIsDefinedImplicitlyTest() {
        setProperty(ELEMENT_WAITING_TIME_UNIT.getPropertyName(), "SECONDS");
        setProperty(ELEMENT_WAITING_TIME_VALUE.getPropertyName(), "5");
        try {
            setStartBenchMark();
            WebElement webElement = seleniumSteps.find(webElement(tagName(TABLE)));
            setEndBenchMark();
            assertThat(getTimeDifference(), lessThan(HALF_SECOND.toMillis()));
            assertThat(webElement, equalTo(COMMON_LABELED_TABLE1));
            assertThat(webElement.toString(), is("Web element found By.tagName: table"));
        }
        finally {
            removeProperty(ELEMENT_WAITING_TIME_UNIT.getPropertyName());
            removeProperty(ELEMENT_WAITING_TIME_VALUE.getPropertyName());
        }
    }

    @Test
    public void findWebElementChainedWithoutConditionTest() {
        WebElement webElement = seleniumSteps.find(webElement(className(CUSTOM_BUTTON_CLASS))
                .foundFrom(webElement(className(SPREAD_SHEET_CLASS))));
        assertThat(webElement, equalTo(CUSTOM_LABELED_BUTTON1));
        assertThat(webElement.toString(), is("Web element found By.className: CustomButton"));
    }

    @Test
    public void findWebElementChainedOnlyVisibleImplicitConditionTest() {
        setProperty(FIND_ONLY_VISIBLE_ELEMENTS_WHEN_NO_CONDITION.getPropertyName(), "true");
        try {
            WebElement webElement = seleniumSteps.find(webElement(className(CUSTOM_BUTTON_CLASS))
                    .foundFrom(webElement(className(SPREAD_SHEET_CLASS))));
            assertThat(webElement, equalTo(CUSTOM_LABELED_BUTTON1));
            assertThat(webElement.toString(), is("Web element found By.className: CustomButton on condition 'Should be visible'"));
        }
        finally {
            removeProperty(FIND_ONLY_VISIBLE_ELEMENTS_WHEN_NO_CONDITION.getPropertyName());
        }
    }

    @Test
    public void findWebElementChainedOnlyWhenTimeIsDefinedTest() {
        setStartBenchMark();
        WebElement webElement = seleniumSteps.find(webElement(className(CUSTOM_BUTTON_CLASS), FIVE_SECONDS)
                .foundFrom(webElement(className(SPREAD_SHEET_CLASS), FIVE_SECONDS)));
        setEndBenchMark();
        assertThat(getTimeDifference(), lessThan(HALF_SECOND.toMillis()));
        assertThat(webElement, equalTo(CUSTOM_LABELED_BUTTON1));
        assertThat(webElement.toString(), is("Web element found By.className: CustomButton"));
    }

    @Test
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
            assertThat(webElement.toString(), is("Web element found By.className: CustomButton"));
        }
        finally {
            removeProperty(ELEMENT_WAITING_TIME_UNIT.getPropertyName());
            removeProperty(ELEMENT_WAITING_TIME_VALUE.getPropertyName());
        }
    }

    @Test
    public void findWebElementFromAnotherWithoutConditionTest() {
        WebElement spreadSheet = seleniumSteps.find(webElement(className(SPREAD_SHEET_CLASS)));
        WebElement webElement = seleniumSteps.find(webElement(className(CUSTOM_BUTTON_CLASS))
                .foundFrom(spreadSheet));
        assertThat(webElement, equalTo(CUSTOM_LABELED_BUTTON1));
        assertThat(webElement.toString(), is("Web element found By.className: CustomButton"));
    }

    @Test
    public void findWebElementFromAnotherOnlyVisibleImplicitConditionTest() {
        setProperty(FIND_ONLY_VISIBLE_ELEMENTS_WHEN_NO_CONDITION.getPropertyName(), "true");
        try {
            WebElement spreadSheet = seleniumSteps.find(webElement(className(SPREAD_SHEET_CLASS)));
            assertThat(spreadSheet.isDisplayed(), is(true));
            WebElement webElement = seleniumSteps.find(webElement(className(CUSTOM_BUTTON_CLASS))
                    .foundFrom(spreadSheet));
            assertThat(webElement, equalTo(CUSTOM_LABELED_BUTTON1));
            assertThat(webElement.toString(), is("Web element found By.className: CustomButton on condition 'Should be visible'"));
        }
        finally {
            removeProperty(FIND_ONLY_VISIBLE_ELEMENTS_WHEN_NO_CONDITION.getPropertyName());
        }
    }

    @Test
    public void findWebElementFromAnotherOnlyWhenTimeIsDefinedTest() {
        WebElement spreadSheet = seleniumSteps.find(webElement(className(SPREAD_SHEET_CLASS)));
        setStartBenchMark();
        WebElement webElement = seleniumSteps.find(webElement(className(CUSTOM_BUTTON_CLASS), FIVE_SECONDS)
                .foundFrom(spreadSheet));
        setEndBenchMark();
        assertThat(getTimeDifference(), lessThan(HALF_SECOND.toMillis()));
        assertThat(webElement, equalTo(CUSTOM_LABELED_BUTTON1));
        assertThat(webElement.toString(), is("Web element found By.className: CustomButton"));
    }

    @Test
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
            assertThat(webElement.toString(), is("Web element found By.className: CustomButton"));
        }
        finally {
            removeProperty(ELEMENT_WAITING_TIME_UNIT.getPropertyName());
            removeProperty(ELEMENT_WAITING_TIME_VALUE.getPropertyName());
        }
    }

    @DataProvider(name = "search criteria1")
    public static Object[][] searchCriteria() {
        return new Object[][] {
                {tagName(BUTTON_TAG), shouldBeEnabled(), COMMON_BUTTON3, "Web element found By.tagName: button on condition 'Should be enabled'"},

                {xpath(RADIO_BUTTON_XPATH), shouldBeEnabled().negate(), COMMON_RADIOBUTTON2, "Web element found By.xpath: .//input[@type = 'radio'] on condition 'NOT Should be enabled'"},

                {tagName(LINK_TAG), shouldBeVisible(), COMMON_LABELED_LINK1, "Web element found By.tagName: a on condition 'Should be visible'"},

                {tagName(LINK_TAG), shouldBeVisible().negate(), COMMON_LABELED_LINK2, "Web element found By.tagName: a on condition 'NOT Should be visible'"},

                {className(MULTI_SELECT_CLASS), shouldContainElements(webElements(className(ITEM_OPTION_CLASS), FIVE_SECONDS), 2), MULTI_SELECT4,
                        "Web element found By.className: multiSelect on condition 'Should have 2 nested Web elements located [By.className: item]. Time to get valuable result: 0:00:05:000'"},

                {className(MULTI_SELECT_CLASS), shouldContainElements(webElements(className(ITEM_OPTION_CLASS), FIVE_SECONDS), 2).negate(), MULTI_SELECT1,
                        "Web element found By.className: multiSelect on condition 'NOT Should have 2 nested Web elements located [By.className: item]. Time to get valuable result: 0:00:05:000'"},

                {className(MULTI_SELECT_CLASS), shouldContainElements(webElements(className(ITEM_OPTION_CLASS), FIVE_SECONDS)), MULTI_SELECT1,
                        "Web element found By.className: multiSelect on condition 'Should have nested Web elements located [By.className: item]. Time to get valuable result: 0:00:05:000'"},

                {tagName(SELECT), shouldContainElements(webElements(className(ITEM_OPTION_CLASS), ofMillis(50))).negate(), COMMON_LABELED_SELECT1,
                        "Web element found By.tagName: select on condition 'NOT Should have nested Web elements located [By.className: item]. Time to get valuable result: 0:00:00:050'"},

                {tagName(TEXT_AREA_TAG), shouldHaveAttribute(ATTR11, VALUE13), TEXT_AREA2,
                        "Web element found By.tagName: textarea on condition 'Should have attribute 'attribute11=\"value13\"''"},

                {tagName(TEXT_AREA_TAG), shouldHaveAttribute(ATTR11, VALUE13).negate(), TEXT_AREA1,
                        "Web element found By.tagName: textarea on condition 'NOT Should have attribute 'attribute11=\"value13\"''"},

                {tagName(TEXT_AREA_TAG), shouldHaveAttributeContains(ATTR11, VALUE13), TEXT_AREA2,
                        "Web element found By.tagName: textarea on condition 'Should have attribute 'attribute11' which contains value 'value13''"},

                {tagName(TEXT_AREA_TAG), shouldHaveAttributeContains(ATTR11, VALUE13).negate(), TEXT_AREA1,
                        "Web element found By.tagName: textarea on condition 'NOT Should have attribute 'attribute11' which contains value 'value13''"},

                {tagName(TEXT_AREA_TAG), shouldHaveAttributeContains(ATTR11, compile(VALUE13)), TEXT_AREA2,
                        "Web element found By.tagName: textarea on condition 'Should have attribute 'attribute11' which matches regExp pattern 'value13''"},

                {tagName(TEXT_AREA_TAG), shouldHaveAttributeContains(ATTR11, compile(VALUE13)).negate(), TEXT_AREA1,
                        "Web element found By.tagName: textarea on condition 'NOT Should have attribute 'attribute11' which matches regExp pattern 'value13''"},

                {xpath(RADIO_BUTTON_XPATH), shouldHaveCssValue(CSS18, CSS_VALUE9), COMMON_RADIOBUTTON3,
                        "Web element found By.xpath: .//input[@type = 'radio'] on condition 'Should have css property 'css18=\"css_value9\"''"},

                {xpath(RADIO_BUTTON_XPATH), shouldHaveCssValue(CSS18, CSS_VALUE9).negate(), COMMON_RADIOBUTTON1,
                        "Web element found By.xpath: .//input[@type = 'radio'] on condition 'NOT Should have css property 'css18=\"css_value9\"''"},

                {xpath(RADIO_BUTTON_XPATH), shouldHaveCssValueContains(CSS18, CSS_VALUE9), COMMON_RADIOBUTTON3,
                        "Web element found By.xpath: .//input[@type = 'radio'] on condition 'Should have css property  'css18' which contains value 'css_value9''"},

                {xpath(RADIO_BUTTON_XPATH), shouldHaveCssValueContains(CSS18, CSS_VALUE9).negate(), COMMON_RADIOBUTTON1,
                        "Web element found By.xpath: .//input[@type = 'radio'] on condition 'NOT Should have css property  'css18' which contains value 'css_value9''"},

                {xpath(RADIO_BUTTON_XPATH), shouldHaveCssValueContains(CSS18, compile(CSS_VALUE9)), COMMON_RADIOBUTTON3,
                        "Web element found By.xpath: .//input[@type = 'radio'] on condition 'Should have css property 'css18' which matches regExp pattern 'css_value9''"},

                {xpath(RADIO_BUTTON_XPATH), shouldHaveCssValueContains(CSS18, compile(CSS_VALUE9)).negate(), COMMON_RADIOBUTTON1,
                        "Web element found By.xpath: .//input[@type = 'radio'] on condition 'NOT Should have css property 'css18' which matches regExp pattern 'css_value9''"},

                {xpath(TEXT_FIELD_XPATH), shouldHaveText(INPUT_TEXT2), COMMON_TEXT_INPUT2,
                        "Web element found By.xpath: .//input[@type = 'text'] on condition 'Should have text 'Input Text2''"},

                {xpath(TEXT_FIELD_XPATH), shouldHaveText(INPUT_TEXT2).negate(), COMMON_LABELED_INPUT1,
                        "Web element found By.xpath: .//input[@type = 'text'] on condition 'NOT Should have text 'Input Text2''"},

                {xpath(TEXT_FIELD_XPATH), shouldHaveText(compile(INPUT_TEXT2)), COMMON_TEXT_INPUT2,
                        "Web element found By.xpath: .//input[@type = 'text'] on condition 'Should have text which contains regExp pattern 'Input Text2''"},

                {xpath(TEXT_FIELD_XPATH), shouldHaveText(compile(INPUT_TEXT2)).negate(), COMMON_LABELED_INPUT1,
                        "Web element found By.xpath: .//input[@type = 'text'] on condition 'NOT Should have text which contains regExp pattern 'Input Text2''"}
        };
    }

    @Test(dataProvider = "search criteria1")
    public void findElementByCriteriaTest(By by, Predicate<? extends SearchContext> criteria, WebElement expected,
                                      String expectedElementDescription) {
        setStartBenchMark();
        WebElement webElement = seleniumSteps.find(webElement(by, (Predicate<? super WebElement>) criteria));
        setEndBenchMark();
        assertThat(getTimeDifference(), lessThan(HALF_SECOND.toMillis()));
        assertThat(webElement, equalTo(expected));
        assertThat(webElement.toString(), is(expectedElementDescription));
    }

    @Test(dataProvider = "search criteria1")
    public void findElementByCriteriaWithDefinedTimeTest(By by, Predicate<? extends SearchContext> criteria, WebElement expected,
                                      String expectedElementDescription) {
        setStartBenchMark();
        WebElement webElement = seleniumSteps.find(webElement(by, FIVE_SECONDS, (Predicate<? super WebElement>) criteria));
        setEndBenchMark();
        assertThat(getTimeDifference(), lessThan(HALF_SECOND.toMillis()));
        assertThat(webElement, equalTo(expected));
        assertThat(webElement.toString(), is(expectedElementDescription));
    }

    @Test(dataProvider = "search criteria1")
    public void findElementByCriteriaWithTimeDefinedImplicitlyTest(By by, Predicate<? extends SearchContext> criteria, WebElement expected,
                                                                   String expectedElementDescription) {
        setProperty(ELEMENT_WAITING_TIME_UNIT.getPropertyName(), "SECONDS");
        setProperty(ELEMENT_WAITING_TIME_VALUE.getPropertyName(), "5");
        try {
            setStartBenchMark();
            WebElement webElement = seleniumSteps.find(webElement(by, (Predicate<? super WebElement>) criteria));
            setEndBenchMark();
            assertThat(getTimeDifference(), lessThan(HALF_SECOND.toMillis()));
            assertThat(webElement, equalTo(expected));
            assertThat(webElement.toString(), is(expectedElementDescription));
        }
        finally {
            removeProperty(ELEMENT_WAITING_TIME_UNIT.getPropertyName());
            removeProperty(ELEMENT_WAITING_TIME_VALUE.getPropertyName());
        }
    }
}
