package com.github.toy.constructor.selenium.test.steps.tests.searching;

import com.github.toy.constructor.selenium.test.BaseWebDriverTest;
import org.openqa.selenium.By;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.function.Predicate;
import java.util.regex.Pattern;

import static com.github.toy.constructor.selenium.functions.searching.CommonConditions.*;
import static com.github.toy.constructor.selenium.functions.searching.MultipleSearchSupplier.webElements;
import static com.github.toy.constructor.selenium.functions.searching.SearchSupplier.webElement;
import static com.github.toy.constructor.selenium.properties.FlagProperties.FIND_ONLY_VISIBLE_ELEMENTS_WHEN_NO_CONDITION;
import static com.github.toy.constructor.selenium.properties.WaitingProperties.TimeUnitProperties.ELEMENT_WAITING_TIME_UNIT;
import static com.github.toy.constructor.selenium.properties.WaitingProperties.TimeValueProperties.ELEMENT_WAITING_TIME_VALUE;
import static com.github.toy.constructor.selenium.test.FakeDOMModel.*;
import static java.lang.String.format;
import static java.time.Duration.ofMillis;
import static java.util.regex.Pattern.compile;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.lessThan;
import static org.openqa.selenium.By.*;

@SuppressWarnings("unchecked")
public class SearchForWebElementPositiveTest extends BaseWebDriverTest {

    private static final String FOUND_BY_PATTERN = "Web element found %s";
    private static final String FOUND_ON_CONDITION = FOUND_BY_PATTERN + " on condition '%s'";

    private static String expectedDescriptionOfTheFoundElement(By by, Predicate<? extends SearchContext> condition) {
        return format(FOUND_ON_CONDITION, by, condition);
    }

    private static String expectedDescriptionOfTheFoundElement(By by) {
        return format(FOUND_BY_PATTERN, by);
    }

    @Test
    public void findWebElementFirstLevelWithoutConditionTest() {
        WebElement webElement = seleniumSteps.find(webElement(tagName(BUTTON_TAG)));
        assertThat(webElement, equalTo(COMMON_BUTTON1));
        assertThat(webElement.toString(), is(expectedDescriptionOfTheFoundElement(tagName(BUTTON_TAG))));
    }

    @Test
    public void findWebElementFirstLevelOnlyVisibleImplicitConditionTest() {
        setProperty(FIND_ONLY_VISIBLE_ELEMENTS_WHEN_NO_CONDITION.getPropertyName(), "true");
        try {
            WebElement webElement = seleniumSteps.find(webElement(tagName(BUTTON_TAG)));
            assertThat(webElement, equalTo(COMMON_BUTTON2));
            assertThat(webElement.toString(), is(expectedDescriptionOfTheFoundElement(tagName(BUTTON_TAG),
                    shouldBeVisible())));
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
        assertThat(webElement.toString(), is(expectedDescriptionOfTheFoundElement(tagName(TABLE))));
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
            assertThat(webElement.toString(), is(expectedDescriptionOfTheFoundElement(tagName(TABLE))));
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
        assertThat(webElement.toString(), is(expectedDescriptionOfTheFoundElement(className(CUSTOM_BUTTON_CLASS))));
    }

    @Test
    public void findWebElementChainedOnlyVisibleImplicitConditionTest() {
        setProperty(FIND_ONLY_VISIBLE_ELEMENTS_WHEN_NO_CONDITION.getPropertyName(), "true");
        try {
            WebElement webElement = seleniumSteps.find(webElement(className(CUSTOM_BUTTON_CLASS))
                    .foundFrom(webElement(className(SPREAD_SHEET_CLASS))));
            assertThat(webElement, equalTo(CUSTOM_LABELED_BUTTON1));
            assertThat(webElement.toString(), is(expectedDescriptionOfTheFoundElement(className(CUSTOM_BUTTON_CLASS),
                    shouldBeVisible())));
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
        assertThat(webElement.toString(), is(expectedDescriptionOfTheFoundElement(className(CUSTOM_BUTTON_CLASS))));
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
            assertThat(webElement.toString(), is(expectedDescriptionOfTheFoundElement(className(CUSTOM_BUTTON_CLASS))));
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
        assertThat(webElement.toString(), is(expectedDescriptionOfTheFoundElement(className(CUSTOM_BUTTON_CLASS))));
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
            assertThat(webElement.toString(), is(expectedDescriptionOfTheFoundElement(className(CUSTOM_BUTTON_CLASS),
                    shouldBeVisible())));
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
        assertThat(webElement.toString(), is(expectedDescriptionOfTheFoundElement(className(CUSTOM_BUTTON_CLASS))));
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
            assertThat(webElement.toString(), is(expectedDescriptionOfTheFoundElement(className(CUSTOM_BUTTON_CLASS))));
        }
        finally {
            removeProperty(ELEMENT_WAITING_TIME_UNIT.getPropertyName());
            removeProperty(ELEMENT_WAITING_TIME_VALUE.getPropertyName());
        }
    }

    @DataProvider(name = "search criteria1")
    public static Object[][] searchCriteria() {
        return new Object[][] {
                {tagName(BUTTON_TAG), shouldBeEnabled(), COMMON_BUTTON3,
                        expectedDescriptionOfTheFoundElement(tagName(BUTTON_TAG), shouldBeEnabled())},

                {xpath(RADIO_BUTTON_XPATH), shouldBeEnabled().negate(), COMMON_RADIOBUTTON2,
                        expectedDescriptionOfTheFoundElement(xpath(RADIO_BUTTON_XPATH), shouldBeEnabled().negate())},

                {tagName(LINK_TAG), shouldBeVisible(), COMMON_LABELED_LINK1,
                        expectedDescriptionOfTheFoundElement(tagName(LINK_TAG), shouldBeVisible())},

                {tagName(LINK_TAG), shouldBeVisible().negate(), COMMON_LABELED_LINK2,
                        expectedDescriptionOfTheFoundElement(tagName(LINK_TAG), shouldBeVisible().negate())},

                {className(MULTI_SELECT_CLASS), shouldContainElements(webElements(className(ITEM_OPTION_CLASS), FIVE_SECONDS), 2), MULTI_SELECT4,
                        expectedDescriptionOfTheFoundElement(className(MULTI_SELECT_CLASS),
                                shouldContainElements(webElements(className(ITEM_OPTION_CLASS), FIVE_SECONDS), 2))},

                {className(MULTI_SELECT_CLASS), shouldContainElements(webElements(className(ITEM_OPTION_CLASS), FIVE_SECONDS), 2).negate(), MULTI_SELECT1,
                        expectedDescriptionOfTheFoundElement(className(MULTI_SELECT_CLASS),
                                shouldContainElements(webElements(className(ITEM_OPTION_CLASS), FIVE_SECONDS), 2).negate())},

                {className(MULTI_SELECT_CLASS), shouldContainElements(webElements(className(ITEM_OPTION_CLASS), FIVE_SECONDS)), MULTI_SELECT1,
                        expectedDescriptionOfTheFoundElement(className(MULTI_SELECT_CLASS),
                                shouldContainElements(webElements(className(ITEM_OPTION_CLASS), FIVE_SECONDS)))},

                {tagName(SELECT), shouldContainElements(webElements(className(ITEM_OPTION_CLASS), ofMillis(50))).negate(), COMMON_LABELED_SELECT1,
                        expectedDescriptionOfTheFoundElement(tagName(SELECT),
                                shouldContainElements(webElements(className(ITEM_OPTION_CLASS), ofMillis(50))).negate())},

                {tagName(TEXT_AREA_TAG), shouldHaveAttribute(ATTR11, VALUE13), TEXT_AREA2,
                        expectedDescriptionOfTheFoundElement(tagName(TEXT_AREA_TAG),
                                shouldHaveAttribute(ATTR11, VALUE13))},

                {tagName(TEXT_AREA_TAG), shouldHaveAttribute(ATTR11, VALUE13).negate(), TEXT_AREA1,
                        expectedDescriptionOfTheFoundElement(tagName(TEXT_AREA_TAG),
                                shouldHaveAttribute(ATTR11, VALUE13).negate())},

                {tagName(TEXT_AREA_TAG), shouldHaveAttributeContains(ATTR11, VALUE13), TEXT_AREA2,
                        expectedDescriptionOfTheFoundElement(tagName(TEXT_AREA_TAG),
                                shouldHaveAttributeContains(ATTR11, VALUE13))},

                {tagName(TEXT_AREA_TAG), shouldHaveAttributeContains(ATTR11, VALUE13).negate(), TEXT_AREA1,
                        expectedDescriptionOfTheFoundElement(tagName(TEXT_AREA_TAG),
                                shouldHaveAttributeContains(ATTR11, VALUE13).negate())},

                {tagName(TEXT_AREA_TAG), shouldHaveAttributeContains(ATTR11, compile(VALUE13)), TEXT_AREA2,
                        expectedDescriptionOfTheFoundElement(tagName(TEXT_AREA_TAG),
                                shouldHaveAttributeContains(ATTR11, compile(VALUE13)))},

                {tagName(TEXT_AREA_TAG), shouldHaveAttributeContains(ATTR11, compile(VALUE13)).negate(), TEXT_AREA1,
                        expectedDescriptionOfTheFoundElement(tagName(TEXT_AREA_TAG),
                                shouldHaveAttributeContains(ATTR11, compile(VALUE13)).negate())},

                {xpath(RADIO_BUTTON_XPATH), shouldHaveCssValue(CSS18, CSS_VALUE9), COMMON_RADIOBUTTON3,
                        expectedDescriptionOfTheFoundElement(xpath(RADIO_BUTTON_XPATH), shouldHaveCssValue(CSS18, CSS_VALUE9))},

                {xpath(RADIO_BUTTON_XPATH), shouldHaveCssValue(CSS18, CSS_VALUE9).negate(), COMMON_RADIOBUTTON1,
                        expectedDescriptionOfTheFoundElement(xpath(RADIO_BUTTON_XPATH), shouldHaveCssValue(CSS18, CSS_VALUE9).negate())},

                {xpath(RADIO_BUTTON_XPATH), shouldHaveCssValueContains(CSS18, CSS_VALUE9), COMMON_RADIOBUTTON3,
                        expectedDescriptionOfTheFoundElement(xpath(RADIO_BUTTON_XPATH), shouldHaveCssValueContains(CSS18, CSS_VALUE9))},

                {xpath(RADIO_BUTTON_XPATH), shouldHaveCssValueContains(CSS18, CSS_VALUE9).negate(), COMMON_RADIOBUTTON1,
                        expectedDescriptionOfTheFoundElement(xpath(RADIO_BUTTON_XPATH),
                                shouldHaveCssValueContains(CSS18, CSS_VALUE9).negate())},

                {xpath(RADIO_BUTTON_XPATH), shouldHaveCssValueContains(CSS18, compile(CSS_VALUE9)), COMMON_RADIOBUTTON3,
                        expectedDescriptionOfTheFoundElement(xpath(RADIO_BUTTON_XPATH),
                                shouldHaveCssValueContains(CSS18, compile(CSS_VALUE9)))},

                {xpath(RADIO_BUTTON_XPATH), shouldHaveCssValueContains(CSS18, compile(CSS_VALUE9)).negate(), COMMON_RADIOBUTTON1,
                        expectedDescriptionOfTheFoundElement(xpath(RADIO_BUTTON_XPATH),
                                shouldHaveCssValueContains(CSS18, compile(CSS_VALUE9)).negate())},

                {xpath(TEXT_FIELD_XPATH), shouldHaveText(INPUT_TEXT2), COMMON_TEXT_INPUT2,
                        expectedDescriptionOfTheFoundElement(xpath(TEXT_FIELD_XPATH), shouldHaveText(INPUT_TEXT2))},

                {xpath(TEXT_FIELD_XPATH), shouldHaveText(INPUT_TEXT2).negate(), COMMON_LABELED_INPUT1,
                        expectedDescriptionOfTheFoundElement(xpath(TEXT_FIELD_XPATH), shouldHaveText(INPUT_TEXT2).negate())},

                {xpath(TEXT_FIELD_XPATH), shouldHaveText(compile(INPUT_TEXT2)), COMMON_TEXT_INPUT2,
                        expectedDescriptionOfTheFoundElement(xpath(TEXT_FIELD_XPATH), shouldHaveText(compile(INPUT_TEXT2)))},

                {xpath(TEXT_FIELD_XPATH), shouldHaveText(compile(INPUT_TEXT2)).negate(), COMMON_LABELED_INPUT1,
                        expectedDescriptionOfTheFoundElement(xpath(TEXT_FIELD_XPATH), shouldHaveText(compile(INPUT_TEXT2)).negate())}
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

    @DataProvider(name = "search criteria2")
    public static Object[][] searchCriteriaForText() {
        return new Object[][] {
                {tagName(BUTTON_TAG), BUTTON_TEXT4, shouldBeEnabled(), COMMON_BUTTON4,
                        expectedDescriptionOfTheFoundElement(tagName(BUTTON_TAG),
                                shouldHaveText(BUTTON_TEXT4).and(shouldBeEnabled()))},

                {tagName(BUTTON_TAG), BUTTON_TEXT5, shouldBeEnabled().negate(), COMMON_LABELED_BUTTON1,
                        expectedDescriptionOfTheFoundElement(tagName(BUTTON_TAG),
                                shouldHaveText(BUTTON_TEXT5).and(shouldBeEnabled().negate()))},

                {tagName(LINK_TAG), LINK_TEXT2, shouldBeVisible(), COMMON_LINK2,
                        expectedDescriptionOfTheFoundElement(tagName(LINK_TAG),
                                shouldHaveText(LINK_TEXT2).and(shouldBeVisible()))},

                {tagName(LINK_TAG), LINK_TEXT4,  shouldBeVisible().negate(), COMMON_LINK4,
                        expectedDescriptionOfTheFoundElement(tagName(LINK_TAG),
                                shouldHaveText(LINK_TEXT4).and(shouldBeVisible().negate()))},

                {tagName(SELECT), OPTION_TEXT23,
                        shouldContainElements(webElements(tagName(OPTION), OPTION_TEXT22, FIVE_SECONDS), 2), COMMON_LABELED_SELECT4,
                        expectedDescriptionOfTheFoundElement(tagName(SELECT), shouldHaveText(OPTION_TEXT23)
                                .and(shouldContainElements(webElements(tagName(OPTION), OPTION_TEXT22, FIVE_SECONDS), 2)))},

                {tagName(SELECT), OPTION_TEXT19,
                        shouldContainElements(webElements(tagName(OPTION), FIVE_SECONDS), 3).negate(), COMMON_LABELED_SELECT3,
                        expectedDescriptionOfTheFoundElement(tagName(SELECT),
                                shouldHaveText(OPTION_TEXT19).and(shouldContainElements(webElements(tagName(OPTION), FIVE_SECONDS), 3).negate()))},

                {tagName(BUTTON_TAG), BUTTON_TEXT5,
                        shouldContainElements(webElements(tagName(LABEL_TAG), BUTTON_LABEL_TEXT1, FIVE_SECONDS)),
                        COMMON_LABELED_BUTTON1,
                        expectedDescriptionOfTheFoundElement(tagName(BUTTON_TAG),
                                shouldHaveText(BUTTON_TEXT5).and(shouldContainElements(webElements(tagName(LABEL_TAG), BUTTON_LABEL_TEXT1, FIVE_SECONDS))))},

                {tagName(BUTTON_TAG), BUTTON_TEXT3,
                        shouldContainElements(webElements(tagName(LABEL_TAG), BUTTON_LABEL_TEXT1, ofMillis(50))).negate(),
                        COMMON_BUTTON3,
                        expectedDescriptionOfTheFoundElement(tagName(BUTTON_TAG),
                                shouldHaveText(BUTTON_TEXT3).and(shouldContainElements(webElements(tagName(LABEL_TAG), BUTTON_LABEL_TEXT1, ofMillis(50))).negate()))},

                {CHAINED_FIND_TAB, TAB_TEXT2, shouldHaveAttribute(ATTR19, VALUE12), COMMON_TAB2,
                        expectedDescriptionOfTheFoundElement(CHAINED_FIND_TAB,
                                shouldHaveText(TAB_TEXT2).and(shouldHaveAttribute(ATTR19, VALUE12)))},

                {CHAINED_FIND_TAB, TAB_TEXT3, shouldHaveAttribute(ATTR19, VALUE12).negate(), COMMON_TAB3,
                        expectedDescriptionOfTheFoundElement(CHAINED_FIND_TAB,
                                shouldHaveText(TAB_TEXT3).and(shouldHaveAttribute(ATTR19, VALUE12).negate()))},

                {CHAINED_FIND_TAB, TAB_TEXT3, shouldHaveAttributeContains(ATTR20, VALUE14), COMMON_TAB3,
                        expectedDescriptionOfTheFoundElement(CHAINED_FIND_TAB,
                                shouldHaveText(TAB_TEXT3).and(shouldHaveAttributeContains(ATTR20, VALUE14)))},

                {CHAINED_FIND_TAB, TAB_TEXT4, shouldHaveAttributeContains(ATTR20, VALUE14).negate(), COMMON_TAB4,
                        expectedDescriptionOfTheFoundElement(CHAINED_FIND_TAB,
                                shouldHaveText(TAB_TEXT4).and(shouldHaveAttributeContains(ATTR20, VALUE14).negate()))},

                {CHAINED_FIND_TAB, TAB_TEXT1, shouldHaveAttributeContains(ATTR20, compile(VALUE12)), COMMON_TAB1,
                        expectedDescriptionOfTheFoundElement(CHAINED_FIND_TAB,
                                shouldHaveText(TAB_TEXT1).and(shouldHaveAttributeContains(ATTR20, compile(VALUE12))))},

                {CHAINED_FIND_TAB, TAB_TEXT3, shouldHaveAttributeContains(ATTR20, compile(VALUE12)).negate(), COMMON_TAB3,
                        expectedDescriptionOfTheFoundElement(CHAINED_FIND_TAB,
                                shouldHaveText(TAB_TEXT3).and(shouldHaveAttributeContains(ATTR20, compile(VALUE12)).negate()))},

                {xpath(TEXT_FIELD_XPATH), INPUT_TEXT3, shouldHaveCssValue(CSS8, CSS_VALUE6), COMMON_TEXT_INPUT3,
                        expectedDescriptionOfTheFoundElement(xpath(TEXT_FIELD_XPATH),
                                shouldHaveText(INPUT_TEXT3).and(shouldHaveCssValue(CSS8, CSS_VALUE6)))},

                {xpath(TEXT_FIELD_XPATH), INPUT_TEXT4, shouldHaveCssValue(CSS8, CSS_VALUE6).negate(), COMMON_TEXT_INPUT4,
                        expectedDescriptionOfTheFoundElement(xpath(TEXT_FIELD_XPATH),
                                shouldHaveText(INPUT_TEXT4).and(shouldHaveCssValue(CSS8, CSS_VALUE6).negate()))},

                {xpath(TEXT_FIELD_XPATH), INPUT_TEXT1, shouldHaveCssValueContains(CSS8, "4")
                        .and(shouldHaveCssValueContains(CSS9, "5")), COMMON_TEXT_INPUT1,
                        expectedDescriptionOfTheFoundElement(xpath(TEXT_FIELD_XPATH),
                                shouldHaveText(INPUT_TEXT1).and(shouldHaveCssValueContains(CSS8, "4")
                                        .and(shouldHaveCssValueContains(CSS9, "5"))))},

                {xpath(TEXT_FIELD_XPATH), INPUT_TEXT4, shouldHaveCssValueContains(CSS8, "4")
                        .and(shouldHaveCssValueContains(CSS9, "5")).negate(), COMMON_TEXT_INPUT4,
                        expectedDescriptionOfTheFoundElement(xpath(TEXT_FIELD_XPATH),
                                shouldHaveText(INPUT_TEXT4).and(shouldHaveCssValueContains(CSS8, "4")
                                        .and(shouldHaveCssValueContains(CSS9, "5")).negate()))},

                {xpath(TEXT_FIELD_XPATH), INPUT_TEXT1, shouldHaveCssValueContains(CSS8, compile("4"))
                        .and(shouldHaveCssValueContains(CSS9, compile("5"))), COMMON_TEXT_INPUT1,
                        expectedDescriptionOfTheFoundElement(xpath(TEXT_FIELD_XPATH),
                                shouldHaveText(INPUT_TEXT1).and(shouldHaveCssValueContains(CSS8, compile("4"))
                                        .and(shouldHaveCssValueContains(CSS9, compile("5")))))},

                {xpath(TEXT_FIELD_XPATH), INPUT_TEXT4, shouldHaveCssValueContains(CSS8, compile("4"))
                        .and(shouldHaveCssValueContains(CSS9, compile("5"))).negate(), COMMON_TEXT_INPUT4,
                        expectedDescriptionOfTheFoundElement(xpath(TEXT_FIELD_XPATH),
                                shouldHaveText(INPUT_TEXT4).and(shouldHaveCssValueContains(CSS8, compile("4"))
                                        .and(shouldHaveCssValueContains(CSS9, compile("5"))).negate()))},
        };
    }

    @Test(dataProvider = "search criteria2")
    public void findElementByCriteriaAndTextTest(By by, String text, Predicate<? extends SearchContext> criteria, WebElement expected,
                                          String expectedElementDescription) {
        setStartBenchMark();
        WebElement webElement = seleniumSteps.find(webElement(by, text, (Predicate<? super WebElement>) criteria));
        setEndBenchMark();
        assertThat(getTimeDifference(), lessThan(HALF_SECOND.toMillis()));
        assertThat(webElement, equalTo(expected));
        assertThat(webElement.toString(), is(expectedElementDescription));
    }

    @Test(dataProvider = "search criteria2")
    public void findElementByCriteriaAndTextWithDefinedTimeTest(By by, String text, Predicate<? extends SearchContext> criteria, WebElement expected,
                                                         String expectedElementDescription) {
        setStartBenchMark();
        WebElement webElement = seleniumSteps.find(webElement(by, text, FIVE_SECONDS, (Predicate<? super WebElement>) criteria));
        setEndBenchMark();
        assertThat(getTimeDifference(), lessThan(HALF_SECOND.toMillis()));
        assertThat(webElement, equalTo(expected));
        assertThat(webElement.toString(), is(expectedElementDescription));
    }

    @Test(dataProvider = "search criteria2")
    public void findElementByCriteriaAndTextWithTimeDefinedImplicitlyTest(By by, String text, Predicate<? extends SearchContext> criteria, WebElement expected,
                                                                   String expectedElementDescription) {
        setProperty(ELEMENT_WAITING_TIME_UNIT.getPropertyName(), "SECONDS");
        setProperty(ELEMENT_WAITING_TIME_VALUE.getPropertyName(), "5");
        try {
            setStartBenchMark();
            WebElement webElement = seleniumSteps.find(webElement(by, text, (Predicate<? super WebElement>) criteria));
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

    @DataProvider(name = "search criteria3")
    public static Object[][] searchCriteriaForTextPattern() {
        return new Object[][] {
                {tagName(BUTTON_TAG), compile(BUTTON_TEXT4), shouldBeEnabled(), COMMON_BUTTON4,
                        expectedDescriptionOfTheFoundElement(tagName(BUTTON_TAG),
                                shouldHaveText(compile(BUTTON_TEXT4)).and(shouldBeEnabled()))},

                {tagName(BUTTON_TAG), compile(BUTTON_TEXT5), shouldBeEnabled().negate(), COMMON_LABELED_BUTTON1,
                        expectedDescriptionOfTheFoundElement(tagName(BUTTON_TAG),
                                shouldHaveText(compile(BUTTON_TEXT5)).and(shouldBeEnabled().negate()))},

                {tagName(LINK_TAG), compile("2"), shouldBeVisible(), COMMON_LINK2,
                        expectedDescriptionOfTheFoundElement(tagName(LINK_TAG),
                                shouldHaveText(compile("2")).and(shouldBeVisible()))},

                {tagName(LINK_TAG), compile("4"),  shouldBeVisible().negate(), COMMON_LINK4,
                        expectedDescriptionOfTheFoundElement(tagName(LINK_TAG),
                                shouldHaveText(compile("4")).and(shouldBeVisible().negate()))},

                {tagName(SELECT), compile(OPTION_TEXT23),
                        shouldContainElements(webElements(tagName(OPTION), OPTION_TEXT22, FIVE_SECONDS), 2), COMMON_LABELED_SELECT4,
                        expectedDescriptionOfTheFoundElement(tagName(SELECT),
                                shouldHaveText(compile(OPTION_TEXT23))
                                        .and(shouldContainElements(webElements(tagName(OPTION), OPTION_TEXT22, FIVE_SECONDS), 2)))},

                {tagName(SELECT), compile("19"),
                        shouldContainElements(webElements(tagName(OPTION), FIVE_SECONDS), 3).negate(), COMMON_LABELED_SELECT3,
                        expectedDescriptionOfTheFoundElement(tagName(SELECT),
                                shouldHaveText(compile("19"))
                                        .and(shouldContainElements(webElements(tagName(OPTION), FIVE_SECONDS), 3).negate()))},

                {tagName(BUTTON_TAG), compile("Text5"),
                        shouldContainElements(webElements(tagName(LABEL_TAG), BUTTON_LABEL_TEXT1, FIVE_SECONDS)),
                        COMMON_LABELED_BUTTON1,
                        expectedDescriptionOfTheFoundElement(tagName(BUTTON_TAG),
                                shouldHaveText(compile("Text5"))
                                        .and(shouldContainElements(webElements(tagName(LABEL_TAG), BUTTON_LABEL_TEXT1, FIVE_SECONDS))))},

                {tagName(BUTTON_TAG), compile("Text3"),
                        shouldContainElements(webElements(tagName(LABEL_TAG), BUTTON_LABEL_TEXT1, ofMillis(50))).negate(),
                        COMMON_BUTTON3,
                        expectedDescriptionOfTheFoundElement(tagName(BUTTON_TAG),
                                shouldHaveText(compile("Text3"))
                                        .and(shouldContainElements(webElements(tagName(LABEL_TAG), BUTTON_LABEL_TEXT1, ofMillis(50))).negate()))},

                {CHAINED_FIND_TAB, compile(TAB_TEXT2), shouldHaveAttribute(ATTR19, VALUE12), COMMON_TAB2,
                        expectedDescriptionOfTheFoundElement(CHAINED_FIND_TAB,
                                shouldHaveText(compile(TAB_TEXT2))
                                        .and(shouldHaveAttribute(ATTR19, VALUE12)))},

                {CHAINED_FIND_TAB, compile("Text3"), shouldHaveAttribute(ATTR19, VALUE12).negate(), COMMON_TAB3,
                        expectedDescriptionOfTheFoundElement(CHAINED_FIND_TAB,
                                shouldHaveText(compile("Text3"))
                                        .and(shouldHaveAttribute(ATTR19, VALUE12).negate()))},

                {CHAINED_FIND_TAB, compile("Text3"), shouldHaveAttributeContains(ATTR20, VALUE14), COMMON_TAB3,
                        expectedDescriptionOfTheFoundElement(CHAINED_FIND_TAB,
                                shouldHaveText(compile("Text3"))
                                        .and(shouldHaveAttributeContains(ATTR20, VALUE14)))},

                {CHAINED_FIND_TAB, compile("Tab text "), shouldHaveAttributeContains(ATTR20, VALUE14).negate(), COMMON_TAB1,
                        expectedDescriptionOfTheFoundElement(CHAINED_FIND_TAB,
                                shouldHaveText(compile("Tab text "))
                                        .and(shouldHaveAttributeContains(ATTR20, VALUE14).negate()))},

                {CHAINED_FIND_TAB, compile("1"), shouldHaveAttributeContains(ATTR20, compile(VALUE12)), COMMON_TAB1,
                        expectedDescriptionOfTheFoundElement(CHAINED_FIND_TAB,
                                shouldHaveText(compile("1"))
                                        .and(shouldHaveAttributeContains(ATTR20, compile(VALUE12))))},

                {CHAINED_FIND_TAB, compile("3"), shouldHaveAttributeContains(ATTR20, compile(VALUE12)).negate(), COMMON_TAB3,
                        expectedDescriptionOfTheFoundElement(CHAINED_FIND_TAB,
                                shouldHaveText(compile("3"))
                                        .and(shouldHaveAttributeContains(ATTR20, compile(VALUE12)).negate()))},

                {xpath(TEXT_FIELD_XPATH), compile("3"), shouldHaveCssValue(CSS8, CSS_VALUE6), COMMON_TEXT_INPUT3,
                        expectedDescriptionOfTheFoundElement(xpath(TEXT_FIELD_XPATH),
                                shouldHaveText(compile("3"))
                                        .and(shouldHaveCssValue(CSS8, CSS_VALUE6)))},

                {xpath(TEXT_FIELD_XPATH), compile("4"), shouldHaveCssValue(CSS8, CSS_VALUE6).negate(), COMMON_TEXT_INPUT4,
                        expectedDescriptionOfTheFoundElement(xpath(TEXT_FIELD_XPATH),
                                shouldHaveText(compile("4"))
                                        .and(shouldHaveCssValue(CSS8, CSS_VALUE6).negate()))},

                {xpath(TEXT_FIELD_XPATH), compile(INPUT_TEXT1), shouldHaveCssValueContains(CSS8, "4")
                        .and(shouldHaveCssValueContains(CSS9, "5")), COMMON_TEXT_INPUT1,
                        expectedDescriptionOfTheFoundElement(xpath(TEXT_FIELD_XPATH),
                                shouldHaveText(compile(INPUT_TEXT1))
                                        .and(shouldHaveCssValueContains(CSS8, "4")
                                                .and(shouldHaveCssValueContains(CSS9, "5"))))},

                {xpath(TEXT_FIELD_XPATH), compile(INPUT_TEXT4), shouldHaveCssValueContains(CSS8, "4")
                        .and(shouldHaveCssValueContains(CSS9, "5")).negate(), COMMON_TEXT_INPUT4,
                        expectedDescriptionOfTheFoundElement(xpath(TEXT_FIELD_XPATH),
                                shouldHaveText(compile(INPUT_TEXT4))
                                        .and(shouldHaveCssValueContains(CSS8, "4")
                                                .and(shouldHaveCssValueContains(CSS9, "5")).negate()))},

                {xpath(TEXT_FIELD_XPATH), compile("1"), shouldHaveCssValueContains(CSS8, compile("4"))
                        .and(shouldHaveCssValueContains(CSS9, compile("5"))), COMMON_TEXT_INPUT1,
                        expectedDescriptionOfTheFoundElement(xpath(TEXT_FIELD_XPATH),
                                shouldHaveText(compile("1"))
                                        .and(shouldHaveCssValueContains(CSS8, compile("4"))
                                                .and(shouldHaveCssValueContains(CSS9, compile("5")))))},

                {xpath(TEXT_FIELD_XPATH), compile(INPUT_TEXT4), shouldHaveCssValueContains(CSS8, compile("4"))
                        .and(shouldHaveCssValueContains(CSS9, compile("5"))).negate(), COMMON_TEXT_INPUT4,
                        expectedDescriptionOfTheFoundElement(xpath(TEXT_FIELD_XPATH),
                                shouldHaveText(compile(INPUT_TEXT4))
                                        .and(shouldHaveCssValueContains(CSS8, compile("4"))
                                                .and(shouldHaveCssValueContains(CSS9, compile("5"))).negate()))},
        };
    }


    @Test(dataProvider = "search criteria3")
    public void findElementByCriteriaAndTextPatternTest(By by, Pattern pattern, Predicate<? extends SearchContext> criteria, WebElement expected,
                                                        String expectedElementDescription) {
        setStartBenchMark();
        WebElement webElement = seleniumSteps.find(webElement(by, pattern, (Predicate<? super WebElement>) criteria));
        setEndBenchMark();
        assertThat(getTimeDifference(), lessThan(HALF_SECOND.toMillis()));
        assertThat(webElement, equalTo(expected));
        assertThat(webElement.toString(), is(expectedElementDescription));
    }

    @Test(dataProvider = "search criteria3")
    public void findElementByCriteriaAndTextPatternWithDefinedTimeTest(By by, Pattern pattern, Predicate<? extends SearchContext> criteria, WebElement expected,
                                                                String expectedElementDescription) {
        setStartBenchMark();
        WebElement webElement = seleniumSteps.find(webElement(by, pattern, FIVE_SECONDS, (Predicate<? super WebElement>) criteria));
        setEndBenchMark();
        assertThat(getTimeDifference(), lessThan(HALF_SECOND.toMillis()));
        assertThat(webElement, equalTo(expected));
        assertThat(webElement.toString(), is(expectedElementDescription));
    }

    @Test(dataProvider = "search criteria3")
    public void findElementByCriteriaAndTexPatterntWithTimeDefinedImplicitlyTest(By by, Pattern pattern, Predicate<? extends SearchContext> criteria, WebElement expected,
                                                                          String expectedElementDescription) {
        setProperty(ELEMENT_WAITING_TIME_UNIT.getPropertyName(), "SECONDS");
        setProperty(ELEMENT_WAITING_TIME_VALUE.getPropertyName(), "5");
        try {
            setStartBenchMark();
            WebElement webElement = seleniumSteps.find(webElement(by, pattern, (Predicate<? super WebElement>) criteria));
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
