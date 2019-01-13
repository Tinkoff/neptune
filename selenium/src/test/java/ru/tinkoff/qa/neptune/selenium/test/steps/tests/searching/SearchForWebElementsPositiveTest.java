package ru.tinkoff.qa.neptune.selenium.test.steps.tests.searching;

import ru.tinkoff.qa.neptune.selenium.test.BaseWebDriverTest;
import ru.tinkoff.qa.neptune.selenium.test.RetryAnalyzer;
import org.hamcrest.Matcher;
import org.openqa.selenium.By;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.function.Predicate;
import java.util.regex.Pattern;

import static ru.tinkoff.qa.neptune.selenium.functions.searching.CommonConditions.*;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.CommonConditions.shouldHaveCssValueContains;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.MultipleSearchSupplier.webElements;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.SearchSupplier.webElement;
import static ru.tinkoff.qa.neptune.selenium.properties.SessionFlagProperties.FIND_ONLY_VISIBLE_ELEMENTS_WHEN_NO_CONDITION;
import static ru.tinkoff.qa.neptune.selenium.properties.WaitingProperties.TimeUnitProperties.ELEMENT_WAITING_TIME_UNIT;
import static ru.tinkoff.qa.neptune.selenium.properties.WaitingProperties.TimeValueProperties.ELEMENT_WAITING_TIME_VALUE;
import static ru.tinkoff.qa.neptune.selenium.test.FakeDOMModel.*;
import static java.lang.String.format;
import static java.time.Duration.ofMillis;
import static java.util.regex.Pattern.compile;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.openqa.selenium.By.className;
import static org.openqa.selenium.By.tagName;
import static org.openqa.selenium.By.xpath;

@SuppressWarnings("unchecked")
public class SearchForWebElementsPositiveTest extends BaseWebDriverTest {

    private static final String FOUND_BY_PATTERN = "%s web elements found %s";
    private static final String FOUND_ON_CONDITION = FOUND_BY_PATTERN + " and meet criteria ['%s']";

    private static String expectedDescriptionOfTheFoundElements(int count, By by, Predicate<? extends SearchContext> condition) {
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
        setProperty(FIND_ONLY_VISIBLE_ELEMENTS_WHEN_NO_CONDITION.getPropertyName(), "true");
        try {
            List<WebElement> webElements = seleniumSteps.find(webElements(tagName(BUTTON_TAG)));
            assertThat(webElements, contains(equalTo(COMMON_BUTTON2), equalTo(COMMON_BUTTON3),
                    equalTo(COMMON_LABELED_BUTTON1),  equalTo(COMMON_LABELED_BUTTON4)));
            assertThat(webElements.toString(), is(expectedDescriptionOfTheFoundElements(4, tagName(BUTTON_TAG),
                    shouldBeVisible())));
        }
        finally {
            removeProperty(FIND_ONLY_VISIBLE_ELEMENTS_WHEN_NO_CONDITION.getPropertyName());
        }
    }

    @Test(retryAnalyzer = RetryAnalyzer.class)
    public void findWebElementsOnlyWhenTimeIsDefinedTest() {
        setStartBenchMark();
        List<WebElement> webElements = seleniumSteps.find(webElements(tagName(TABLE), FIVE_SECONDS));
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
        }
        finally {
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
        setProperty(FIND_ONLY_VISIBLE_ELEMENTS_WHEN_NO_CONDITION.getPropertyName(), "true");
        try {
            List<WebElement> webElements = seleniumSteps.find(webElements(className(CUSTOM_BUTTON_CLASS))
                    .foundFrom(webElement(className(SPREAD_SHEET_CLASS))));
            assertThat(webElements, contains(equalTo(CUSTOM_LABELED_BUTTON1)));
            assertThat(webElements.toString(), is(expectedDescriptionOfTheFoundElements(1, className(CUSTOM_BUTTON_CLASS),
                    shouldBeVisible())));
        }
        finally {
            removeProperty(FIND_ONLY_VISIBLE_ELEMENTS_WHEN_NO_CONDITION.getPropertyName());
        }
    }

    @Test(retryAnalyzer = RetryAnalyzer.class)
    public void findWebElementsChainedOnlyWhenTimeIsDefinedTest() {
        setStartBenchMark();
        List<WebElement> webElements = seleniumSteps.find(webElements(className(CUSTOM_BUTTON_CLASS), FIVE_SECONDS)
                .foundFrom(webElement(className(SPREAD_SHEET_CLASS), FIVE_SECONDS)));
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
        }
        finally {
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
        setProperty(FIND_ONLY_VISIBLE_ELEMENTS_WHEN_NO_CONDITION.getPropertyName(), "true");
        try {
            WebElement spreadSheet = seleniumSteps.find(webElement(className(SPREAD_SHEET_CLASS)));
            assertThat(spreadSheet.isDisplayed(), is(true));
            List<WebElement> webElements = seleniumSteps.find(webElements(className(CUSTOM_BUTTON_CLASS))
                    .foundFrom(spreadSheet));
            assertThat(webElements, contains(equalTo(CUSTOM_LABELED_BUTTON1)));
            assertThat(webElements.toString(), is(expectedDescriptionOfTheFoundElements(1, className(CUSTOM_BUTTON_CLASS),
                    shouldBeVisible())));
        }
        finally {
            removeProperty(FIND_ONLY_VISIBLE_ELEMENTS_WHEN_NO_CONDITION.getPropertyName());
        }
    }

    @Test(retryAnalyzer = RetryAnalyzer.class)
    public void findWebElementsFromAnotherOnlyWhenTimeIsDefinedTest() {
        WebElement spreadSheet = seleniumSteps.find(webElement(className(SPREAD_SHEET_CLASS)));
        setStartBenchMark();
        List<WebElement> webElements = seleniumSteps.find(webElements(className(CUSTOM_BUTTON_CLASS), FIVE_SECONDS)
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
        }
        finally {
            removeProperty(ELEMENT_WAITING_TIME_UNIT.getPropertyName());
            removeProperty(ELEMENT_WAITING_TIME_VALUE.getPropertyName());
        }
    }

    @DataProvider(name = "search criteria1")
    public static Object[][] searchCriteria() {
        return new Object[][] {
                {tagName(BUTTON_TAG), shouldBeEnabled(),
                        contains(equalTo(COMMON_BUTTON3),
                                equalTo(COMMON_BUTTON4),
                                equalTo(COMMON_LABELED_BUTTON2),
                                equalTo(COMMON_LABELED_BUTTON4)),
                        expectedDescriptionOfTheFoundElements(4, tagName(BUTTON_TAG), shouldBeEnabled())},

                {xpath(RADIO_BUTTON_XPATH), shouldBeEnabled().negate(),
                        contains(equalTo(COMMON_RADIOBUTTON2),
                                equalTo(COMMON_LABELED_RADIOBUTTON4),
                                equalTo(COMMON_LABELED_RADIOBUTTON5),
                                equalTo(COMMON_LABELED_RADIOBUTTON8)),
                        expectedDescriptionOfTheFoundElements(4, xpath(RADIO_BUTTON_XPATH), shouldBeEnabled().negate())},

                {tagName(LINK_TAG), shouldBeVisible(),
                        contains(
                                equalTo(COMMON_LABELED_LINK1),
                                equalTo(COMMON_LABELED_LINK4),
                                equalTo(COMMON_LINK2),
                                equalTo(COMMON_LINK3)),
                        expectedDescriptionOfTheFoundElements(4, tagName(LINK_TAG), shouldBeVisible())},

                {tagName(LINK_TAG), shouldBeVisible().negate(),
                        contains(
                                equalTo(COMMON_LABELED_LINK2),
                                equalTo(COMMON_LABELED_LINK3),
                                equalTo(COMMON_LINK1),
                                equalTo(COMMON_LINK4)),
                        expectedDescriptionOfTheFoundElements(4, tagName(LINK_TAG), shouldBeVisible().negate())},

                {className(MULTI_SELECT_CLASS),
                        shouldContainElements(webElements(className(ITEM_OPTION_CLASS), FIVE_SECONDS), 2),
                        contains(equalTo(MULTI_SELECT4)),
                        expectedDescriptionOfTheFoundElements(1, className(MULTI_SELECT_CLASS),
                                shouldContainElements(webElements(className(ITEM_OPTION_CLASS), FIVE_SECONDS), 2))},

                {className(MULTI_SELECT_CLASS),
                        shouldContainElements(webElements(className(ITEM_OPTION_CLASS), FIVE_SECONDS), 2).negate(),
                        contains(equalTo(MULTI_SELECT1),
                                equalTo(MULTI_SELECT2),
                                equalTo(MULTI_SELECT3)),
                        expectedDescriptionOfTheFoundElements(3, className(MULTI_SELECT_CLASS),
                                shouldContainElements(webElements(className(ITEM_OPTION_CLASS), FIVE_SECONDS), 2).negate())},

                {className(MULTI_SELECT_CLASS),
                        shouldContainElements(webElements(className(ITEM_OPTION_CLASS), FIVE_SECONDS)),
                        contains(equalTo(MULTI_SELECT1),
                                equalTo(MULTI_SELECT2),
                                equalTo(MULTI_SELECT3),
                                equalTo(MULTI_SELECT4)),
                        expectedDescriptionOfTheFoundElements(4, className(MULTI_SELECT_CLASS),
                                shouldContainElements(webElements(className(ITEM_OPTION_CLASS), FIVE_SECONDS)))},

                {tagName(SELECT),
                        shouldContainElements(webElements(className(ITEM_OPTION_CLASS), ofMillis(1))).negate(),
                        contains(equalTo(COMMON_LABELED_SELECT1),
                                equalTo(COMMON_LABELED_SELECT2),
                                equalTo(COMMON_LABELED_SELECT3),
                                equalTo(COMMON_LABELED_SELECT4),
                                equalTo(COMMON_SELECT1),
                                equalTo(COMMON_SELECT2),
                                equalTo(COMMON_SELECT3),
                                equalTo(COMMON_SELECT4)),
                        expectedDescriptionOfTheFoundElements(8, tagName(SELECT),
                                shouldContainElements(webElements(className(ITEM_OPTION_CLASS), ofMillis(1))).negate())},

                {tagName(TEXT_AREA_TAG),
                        shouldHaveAttribute(ATTR11, VALUE13),
                        contains(equalTo(TEXT_AREA2),
                                equalTo(TEXT_AREA2)),
                        expectedDescriptionOfTheFoundElements(2, tagName(TEXT_AREA_TAG),
                                shouldHaveAttribute(ATTR11, VALUE13))},

                {tagName(TEXT_AREA_TAG),
                        shouldHaveAttribute(ATTR11, VALUE13).negate(),
                        contains(equalTo(TEXT_AREA1),
                                equalTo(TEXT_AREA3),
                                equalTo(TEXT_AREA4),
                                equalTo(TEXT_AREA1)),
                        expectedDescriptionOfTheFoundElements(4, tagName(TEXT_AREA_TAG),
                                shouldHaveAttribute(ATTR11, VALUE13).negate())},

                {tagName(TEXT_AREA_TAG), shouldHaveAttributeContains(ATTR11, "13"),
                        contains(equalTo(TEXT_AREA2),
                                equalTo(TEXT_AREA2)),
                        expectedDescriptionOfTheFoundElements(2, tagName(TEXT_AREA_TAG),
                                shouldHaveAttributeContains(ATTR11, "13"))},

                {tagName(TEXT_AREA_TAG),
                        shouldHaveAttributeContains(ATTR11, "13").negate(),
                        contains(equalTo(TEXT_AREA1),
                                equalTo(TEXT_AREA3),
                                equalTo(TEXT_AREA4),
                                equalTo(TEXT_AREA1)),
                        expectedDescriptionOfTheFoundElements(4, tagName(TEXT_AREA_TAG),
                                shouldHaveAttributeContains(ATTR11, "13").negate())},

                {tagName(TEXT_AREA_TAG), shouldHaveAttributeContains(ATTR11, compile("13")),
                        contains(equalTo(TEXT_AREA2),
                                equalTo(TEXT_AREA2)),
                        expectedDescriptionOfTheFoundElements(2, tagName(TEXT_AREA_TAG),
                                shouldHaveAttributeContains(ATTR11, compile("13")))},

                {tagName(TEXT_AREA_TAG),
                        shouldHaveAttributeContains(ATTR11, compile("13")).negate(),
                        contains(equalTo(TEXT_AREA1),
                                equalTo(TEXT_AREA3),
                                equalTo(TEXT_AREA4),
                                equalTo(TEXT_AREA1)),
                        expectedDescriptionOfTheFoundElements(4, tagName(TEXT_AREA_TAG),
                                shouldHaveAttributeContains(ATTR11, compile("13")).negate())},

                {xpath(RADIO_BUTTON_XPATH),
                        shouldHaveCssValue(CSS18, CSS_VALUE9),
                        contains(equalTo(COMMON_RADIOBUTTON3)),
                        expectedDescriptionOfTheFoundElements(1, xpath(RADIO_BUTTON_XPATH),
                                shouldHaveCssValue(CSS18, CSS_VALUE9))},

                {xpath(RADIO_BUTTON_XPATH),
                        shouldHaveCssValue(CSS18, CSS_VALUE9).negate(),
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
                                xpath(RADIO_BUTTON_XPATH), shouldHaveCssValue(CSS18, CSS_VALUE9).negate())},

                {xpath(CHECK_BOX_XPATH),
                        shouldHaveCssValueContains(CSS13, "15")
                                .or(shouldHaveCssValueContains(CSS2, "17")),
                        contains(
                                equalTo(COMMON_CHECKBOX4),
                                equalTo(COMMON_LABELED_CHECKBOX1)),
                        expectedDescriptionOfTheFoundElements(2,
                                xpath(CHECK_BOX_XPATH), shouldHaveCssValueContains(CSS13, "15")
                                        .or(shouldHaveCssValueContains(CSS2, "17")))},

                {xpath(CHECK_BOX_XPATH),
                        shouldHaveCssValueContains(CSS13, "15")
                                .or(shouldHaveCssValueContains(CSS2, "17")).negate(),
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
                                shouldHaveCssValueContains(CSS13, "15")
                                        .or(shouldHaveCssValueContains(CSS2, "17")).negate())},

                {className(TAB_CLASS),
                        shouldHaveCssValueContains(CSS15, compile("1"))
                                .and(shouldHaveCssValueContains(CSS20, compile(CSS_VALUE2))),
                        contains(equalTo(CUSTOM_LABELED_TAB1),
                                equalTo(CUSTOM_LABELED_TAB3)),
                        expectedDescriptionOfTheFoundElements(2, className(TAB_CLASS),
                                shouldHaveCssValueContains(CSS15, compile("1"))
                                        .and(shouldHaveCssValueContains(CSS20, compile(CSS_VALUE2))))},

                {className(TAB_CLASS),
                        shouldHaveCssValueContains(CSS15, compile("1"))
                                .and(shouldHaveCssValueContains(CSS20, compile(CSS_VALUE2))).negate(),
                        contains(equalTo(CUSTOM_LABELED_TAB2),
                                equalTo(CUSTOM_LABELED_TAB4)),
                        expectedDescriptionOfTheFoundElements(2, className(TAB_CLASS),
                                shouldHaveCssValueContains(CSS15, compile("1"))
                                        .and(shouldHaveCssValueContains(CSS20, compile(CSS_VALUE2))).negate())},

                {xpath(TEXT_FIELD_XPATH),
                        shouldHaveText(INPUT_TEXT2),
                        contains(equalTo(COMMON_TEXT_INPUT2)),
                        expectedDescriptionOfTheFoundElements(1,
                                xpath(TEXT_FIELD_XPATH), shouldHaveText(INPUT_TEXT2))},

                {xpath(TEXT_FIELD_XPATH),
                        shouldHaveText(INPUT_TEXT2).negate(),
                        contains(equalTo(COMMON_LABELED_INPUT1),
                                equalTo(COMMON_LABELED_INPUT2),
                                equalTo(COMMON_LABELED_INPUT3),
                                equalTo(COMMON_LABELED_INPUT4),
                                equalTo(COMMON_TEXT_INPUT1),
                                equalTo(COMMON_TEXT_INPUT3),
                                equalTo(COMMON_TEXT_INPUT4)),
                        expectedDescriptionOfTheFoundElements(7, xpath(TEXT_FIELD_XPATH), shouldHaveText(INPUT_TEXT2).negate())},

                {xpath(TEXT_FIELD_XPATH),
                        shouldHaveText(compile("Text")),
                        contains(equalTo(COMMON_TEXT_INPUT1),
                                equalTo(COMMON_TEXT_INPUT2),
                                equalTo(COMMON_TEXT_INPUT3),
                                equalTo(COMMON_TEXT_INPUT4)),
                        expectedDescriptionOfTheFoundElements(4,
                                xpath(TEXT_FIELD_XPATH),
                                shouldHaveText(compile("Text")))},

                {xpath(TEXT_FIELD_XPATH), shouldHaveText(compile("Text")).negate(),
                        contains(equalTo(COMMON_LABELED_INPUT1),
                                equalTo(COMMON_LABELED_INPUT2),
                                equalTo(COMMON_LABELED_INPUT3),
                                equalTo(COMMON_LABELED_INPUT4)),
                        expectedDescriptionOfTheFoundElements(4,
                                xpath(TEXT_FIELD_XPATH),
                                shouldHaveText(compile("Text")).negate())}
        };
    }

    @Test(dataProvider = "search criteria1", retryAnalyzer = RetryAnalyzer.class)
    public void findElementsByCriteriaTest(By by, Predicate<? extends SearchContext> criteria,
                                           Matcher<List<WebElement>> matcher,
                                           String expectedListDescription) {
        setStartBenchMark();
        List<WebElement> webElements = seleniumSteps.find(webElements(by, (Predicate<? super WebElement>) criteria));
        setEndBenchMark();
        assertThat(new BigDecimal(getTimeDifference()),
                either(closeTo(new BigDecimal(HALF_SECOND.toMillis()), new BigDecimal(450)))
                        .or(closeTo(new BigDecimal(ONE_SECOND.toMillis()), new BigDecimal(200))));
        assertThat(webElements, matcher);
        assertThat(webElements.toString(), is(expectedListDescription));
    }

    @Test(dataProvider = "search criteria1", retryAnalyzer = RetryAnalyzer.class)
    public void findElementsByCriteriaWithDefinedTimeTest(By by, Predicate<? extends SearchContext> criteria,
                                                          Matcher<List<WebElement>> matcher,
                                                          String expectedListDescription) {
        setStartBenchMark();
        List<WebElement> webElements = seleniumSteps.find(webElements(by, FIVE_SECONDS, (Predicate<? super WebElement>) criteria));
        setEndBenchMark();
        assertThat(new BigDecimal(getTimeDifference()),
                either(closeTo(new BigDecimal(HALF_SECOND.toMillis()), new BigDecimal(450)))
                        .or(closeTo(new BigDecimal(ONE_SECOND.toMillis()), new BigDecimal(200))));
        assertThat(webElements, matcher);
        assertThat(webElements.toString(), is(expectedListDescription));
    }

    @Test(dataProvider = "search criteria1", retryAnalyzer = RetryAnalyzer.class)
    public void findElementsByCriteriaWithTimeDefinedImplicitlyTest(By by, Predicate<? extends SearchContext> criteria,
                                                                    Matcher<List<WebElement>> matcher,
                                                                    String expectedListDescription) {
        setProperty(ELEMENT_WAITING_TIME_UNIT.getPropertyName(), "SECONDS");
        setProperty(ELEMENT_WAITING_TIME_VALUE.getPropertyName(), "5");
        try {
            setStartBenchMark();
            List<WebElement> webElements = seleniumSteps.find(webElements(by, (Predicate<? super WebElement>) criteria));
            setEndBenchMark();
            assertThat(new BigDecimal(getTimeDifference()),
                    either(closeTo(new BigDecimal(HALF_SECOND.toMillis()), new BigDecimal(450)))
                            .or(closeTo(new BigDecimal(ONE_SECOND.toMillis()), new BigDecimal(200))));
            assertThat(webElements, matcher);
            assertThat(webElements.toString(), is(expectedListDescription));
        }
        finally {
            removeProperty(ELEMENT_WAITING_TIME_UNIT.getPropertyName());
            removeProperty(ELEMENT_WAITING_TIME_VALUE.getPropertyName());
        }
    }

    @DataProvider(name = "search criteria2")
    public static Object[][] searchCriteriaForText() {
        return new Object[][]{
                {tagName(BUTTON_TAG), BUTTON_TEXT4, shouldBeEnabled(),
                        contains(equalTo(COMMON_BUTTON4)),
                        expectedDescriptionOfTheFoundElements(1, tagName(BUTTON_TAG),
                                shouldHaveText(BUTTON_TEXT4).and(shouldBeEnabled()))},

                {tagName(BUTTON_TAG), BUTTON_TEXT5, shouldBeEnabled().negate(),
                        contains(equalTo(COMMON_LABELED_BUTTON1)),
                        expectedDescriptionOfTheFoundElements(1, tagName(BUTTON_TAG),
                                shouldHaveText(BUTTON_TEXT5).and(shouldBeEnabled().negate()))},

                {tagName(LINK_TAG), LINK_TEXT2, shouldBeVisible(),
                        contains(equalTo(COMMON_LINK2)),
                        expectedDescriptionOfTheFoundElements(1, tagName(LINK_TAG),
                                shouldHaveText(LINK_TEXT2).and(shouldBeVisible()))},

                {tagName(LINK_TAG), LINK_TEXT4,  shouldBeVisible().negate(),
                        contains(equalTo(COMMON_LINK4)),
                        expectedDescriptionOfTheFoundElements(1, tagName(LINK_TAG),
                                shouldHaveText(LINK_TEXT4).and(shouldBeVisible().negate()))},

                {tagName(SELECT), OPTION_TEXT23,
                        shouldContainElements(webElements(tagName(OPTION), OPTION_TEXT22, FIVE_SECONDS), 2),
                        contains(equalTo(COMMON_LABELED_SELECT4)),
                        expectedDescriptionOfTheFoundElements(1, tagName(SELECT), shouldHaveText(OPTION_TEXT23)
                                .and(shouldContainElements(webElements(tagName(OPTION), OPTION_TEXT22, FIVE_SECONDS), 2)))},

                {tagName(SELECT), OPTION_TEXT19,
                        shouldContainElements(webElements(tagName(OPTION), FIVE_SECONDS), 3).negate(),
                        contains(equalTo(COMMON_LABELED_SELECT3)),
                        expectedDescriptionOfTheFoundElements(1, tagName(SELECT),
                                shouldHaveText(OPTION_TEXT19).and(shouldContainElements(webElements(tagName(OPTION), FIVE_SECONDS), 3).negate()))},

                {tagName(BUTTON_TAG), BUTTON_TEXT5,
                        shouldContainElements(webElements(tagName(LABEL_TAG), BUTTON_LABEL_TEXT1, FIVE_SECONDS)),
                        contains(equalTo(COMMON_LABELED_BUTTON1)),
                        expectedDescriptionOfTheFoundElements(1, tagName(BUTTON_TAG),
                                shouldHaveText(BUTTON_TEXT5).and(shouldContainElements(webElements(tagName(LABEL_TAG), BUTTON_LABEL_TEXT1, FIVE_SECONDS))))},

                {tagName(BUTTON_TAG), BUTTON_TEXT3,
                        shouldContainElements(webElements(tagName(LABEL_TAG), BUTTON_LABEL_TEXT1, ofMillis(50))).negate(),
                        contains(equalTo(COMMON_BUTTON3)),
                        expectedDescriptionOfTheFoundElements(1, tagName(BUTTON_TAG),
                                shouldHaveText(BUTTON_TEXT3).and(shouldContainElements(webElements(tagName(LABEL_TAG), BUTTON_LABEL_TEXT1, ofMillis(50))).negate()))},

                {CHAINED_FIND_TAB, TAB_TEXT2, shouldHaveAttribute(ATTR19, VALUE12),
                        contains(equalTo(COMMON_TAB2)),
                        expectedDescriptionOfTheFoundElements(1, CHAINED_FIND_TAB,
                                shouldHaveText(TAB_TEXT2).and(shouldHaveAttribute(ATTR19, VALUE12)))},

                {CHAINED_FIND_TAB, TAB_TEXT3, shouldHaveAttribute(ATTR19, VALUE12).negate(),
                        contains(equalTo(COMMON_TAB3)),
                        expectedDescriptionOfTheFoundElements(1, CHAINED_FIND_TAB,
                                shouldHaveText(TAB_TEXT3).and(shouldHaveAttribute(ATTR19, VALUE12).negate()))},

                {CHAINED_FIND_TAB, TAB_TEXT3, shouldHaveAttributeContains(ATTR20, VALUE14),
                        contains(equalTo(COMMON_TAB3)),
                        expectedDescriptionOfTheFoundElements(1, CHAINED_FIND_TAB,
                                shouldHaveText(TAB_TEXT3).and(shouldHaveAttributeContains(ATTR20, VALUE14)))},

                {CHAINED_FIND_TAB, TAB_TEXT4,
                        shouldHaveAttributeContains(ATTR20, VALUE14).negate(),
                        contains(equalTo(COMMON_TAB4)),
                        expectedDescriptionOfTheFoundElements(1, CHAINED_FIND_TAB,
                                shouldHaveText(TAB_TEXT4).and(shouldHaveAttributeContains(ATTR20, VALUE14).negate()))},

                {CHAINED_FIND_TAB, TAB_TEXT1, shouldHaveAttributeContains(ATTR20, compile(VALUE12)),
                        contains(equalTo(COMMON_TAB1)),
                        expectedDescriptionOfTheFoundElements(1, CHAINED_FIND_TAB,
                                shouldHaveText(TAB_TEXT1).and(shouldHaveAttributeContains(ATTR20, compile(VALUE12))))},

                {CHAINED_FIND_TAB, TAB_TEXT3, shouldHaveAttributeContains(ATTR20, compile(VALUE12)).negate(),
                        contains(equalTo(COMMON_TAB3)),
                        expectedDescriptionOfTheFoundElements(1, CHAINED_FIND_TAB,
                                shouldHaveText(TAB_TEXT3).and(shouldHaveAttributeContains(ATTR20, compile(VALUE12)).negate()))},

                {xpath(TEXT_FIELD_XPATH), INPUT_TEXT3, shouldHaveCssValue(CSS8, CSS_VALUE6),
                        contains(equalTo(COMMON_TEXT_INPUT3)),
                        expectedDescriptionOfTheFoundElements(1, xpath(TEXT_FIELD_XPATH),
                                shouldHaveText(INPUT_TEXT3).and(shouldHaveCssValue(CSS8, CSS_VALUE6)))},

                {xpath(TEXT_FIELD_XPATH), INPUT_TEXT4, shouldHaveCssValue(CSS8, CSS_VALUE6).negate(),
                        contains(equalTo(COMMON_TEXT_INPUT4)),
                        expectedDescriptionOfTheFoundElements(1, xpath(TEXT_FIELD_XPATH),
                                shouldHaveText(INPUT_TEXT4).and(shouldHaveCssValue(CSS8, CSS_VALUE6).negate()))},

                {xpath(TEXT_FIELD_XPATH), INPUT_TEXT1, shouldHaveCssValueContains(CSS8, "4")
                        .and(shouldHaveCssValueContains(CSS9, "5")),
                        contains(equalTo(COMMON_TEXT_INPUT1)),
                        expectedDescriptionOfTheFoundElements(1, xpath(TEXT_FIELD_XPATH),
                                shouldHaveText(INPUT_TEXT1).and(shouldHaveCssValueContains(CSS8, "4")
                                        .and(shouldHaveCssValueContains(CSS9, "5"))))},

                {xpath(TEXT_FIELD_XPATH), INPUT_TEXT4, shouldHaveCssValueContains(CSS8, "4")
                        .and(shouldHaveCssValueContains(CSS9, "5")).negate(),
                        contains(equalTo(COMMON_TEXT_INPUT4)),
                        expectedDescriptionOfTheFoundElements(1, xpath(TEXT_FIELD_XPATH),
                                shouldHaveText(INPUT_TEXT4).and(shouldHaveCssValueContains(CSS8, "4")
                                        .and(shouldHaveCssValueContains(CSS9, "5")).negate()))},

                {xpath(TEXT_FIELD_XPATH), INPUT_TEXT1, shouldHaveCssValueContains(CSS8, compile("4"))
                        .and(shouldHaveCssValueContains(CSS9, compile("5"))),
                        contains(equalTo(COMMON_TEXT_INPUT1)),
                        expectedDescriptionOfTheFoundElements(1, xpath(TEXT_FIELD_XPATH),
                                shouldHaveText(INPUT_TEXT1).and(shouldHaveCssValueContains(CSS8, compile("4"))
                                        .and(shouldHaveCssValueContains(CSS9, compile("5")))))},

                {xpath(TEXT_FIELD_XPATH), INPUT_TEXT4, shouldHaveCssValueContains(CSS8, compile("4"))
                        .and(shouldHaveCssValueContains(CSS9, compile("5"))).negate(),
                        contains(equalTo(COMMON_TEXT_INPUT4)),
                        expectedDescriptionOfTheFoundElements(1, xpath(TEXT_FIELD_XPATH),
                                shouldHaveText(INPUT_TEXT4).and(shouldHaveCssValueContains(CSS8, compile("4"))
                                        .and(shouldHaveCssValueContains(CSS9, compile("5"))).negate()))},
        };
    }

    @Test(dataProvider = "search criteria2", retryAnalyzer = RetryAnalyzer.class)
    public void findElementsByCriteriaAndTextTest(By by, String text, Predicate<? extends SearchContext> criteria,
                                                  Matcher<List<WebElement>> matcher,
                                                  String expectedListDescription) {
        setStartBenchMark();
        List<WebElement> webElements = seleniumSteps.find(webElements(by, text, (Predicate<? super WebElement>) criteria));
        setEndBenchMark();
        assertThat(new BigDecimal(getTimeDifference()),
                either(closeTo(new BigDecimal(HALF_SECOND.toMillis()), new BigDecimal(450)))
                        .or(closeTo(new BigDecimal(ONE_SECOND.toMillis()), new BigDecimal(200))));
        assertThat(webElements, matcher);
        assertThat(webElements.toString(), is(expectedListDescription));
    }

    @Test(dataProvider = "search criteria2", retryAnalyzer = RetryAnalyzer.class)
    public void findElementsByCriteriaAndTextWithDefinedTimeTest(By by, String text, Predicate<? extends SearchContext> criteria,
                                                                 Matcher<List<WebElement>> matcher,
                                                                 String expectedListDescription) {
        setStartBenchMark();
        List<WebElement> webElements = seleniumSteps.find(webElements(by, text, FIVE_SECONDS, (Predicate<? super WebElement>) criteria));
        setEndBenchMark();
        assertThat(new BigDecimal(getTimeDifference()),
                either(closeTo(new BigDecimal(HALF_SECOND.toMillis()), new BigDecimal(450)))
                        .or(closeTo(new BigDecimal(ONE_SECOND.toMillis()), new BigDecimal(200))));
        assertThat(webElements, matcher);
        assertThat(webElements.toString(), is(expectedListDescription));
    }

    @Test(dataProvider = "search criteria2", retryAnalyzer = RetryAnalyzer.class)
    public void findElementsByCriteriaAndTextWithTimeDefinedImplicitlyTest(By by, String text, Predicate<? extends SearchContext> criteria,
                                                                           Matcher<List<WebElement>> matcher,
                                                                           String expectedListDescription) {
        setProperty(ELEMENT_WAITING_TIME_UNIT.getPropertyName(), "SECONDS");
        setProperty(ELEMENT_WAITING_TIME_VALUE.getPropertyName(), "5");
        try {
            setStartBenchMark();
            List<WebElement> webElements = seleniumSteps.find(webElements(by, text, (Predicate<? super WebElement>) criteria));
            setEndBenchMark();
            assertThat(new BigDecimal(getTimeDifference()),
                    either(closeTo(new BigDecimal(HALF_SECOND.toMillis()), new BigDecimal(450)))
                            .or(closeTo(new BigDecimal(ONE_SECOND.toMillis()), new BigDecimal(200))));
            assertThat(webElements, matcher);
            assertThat(webElements.toString(), is(expectedListDescription));
        }
        finally {
            removeProperty(ELEMENT_WAITING_TIME_UNIT.getPropertyName());
            removeProperty(ELEMENT_WAITING_TIME_VALUE.getPropertyName());
        }
    }

    @DataProvider(name = "search criteria3")
    public static Object[][] searchCriteriaForTextPattern() {
        return new Object[][] {
                {tagName(BUTTON_TAG), compile(BUTTON_TEXT4), shouldBeEnabled(),
                        contains(equalTo(COMMON_BUTTON4)),
                        expectedDescriptionOfTheFoundElements(1, tagName(BUTTON_TAG),
                                shouldHaveText(compile(BUTTON_TEXT4)).and(shouldBeEnabled()))},

                {tagName(BUTTON_TAG), compile(BUTTON_TEXT5), shouldBeEnabled().negate(),
                        contains(equalTo(COMMON_LABELED_BUTTON1)),
                        expectedDescriptionOfTheFoundElements(1, tagName(BUTTON_TAG),
                                shouldHaveText(compile(BUTTON_TEXT5)).and(shouldBeEnabled().negate()))},

                {tagName(LINK_TAG), compile("2"), shouldBeVisible(),
                        contains(equalTo(COMMON_LINK2)),
                        expectedDescriptionOfTheFoundElements(1, tagName(LINK_TAG),
                                shouldHaveText(compile("2")).and(shouldBeVisible()))},

                {tagName(LINK_TAG), compile("4"),
                        shouldBeVisible().negate(),
                        contains(equalTo(COMMON_LINK4)),
                        expectedDescriptionOfTheFoundElements(1, tagName(LINK_TAG),
                                shouldHaveText(compile("4")).and(shouldBeVisible().negate()))},

                {tagName(SELECT), compile(OPTION_TEXT23),
                        shouldContainElements(webElements(tagName(OPTION), OPTION_TEXT22, FIVE_SECONDS), 2),
                        contains(equalTo(COMMON_LABELED_SELECT4)),
                        expectedDescriptionOfTheFoundElements(1, tagName(SELECT),
                                shouldHaveText(compile(OPTION_TEXT23))
                                        .and(shouldContainElements(webElements(tagName(OPTION), OPTION_TEXT22, FIVE_SECONDS), 2)))},

                {tagName(SELECT), compile("19"),
                        shouldContainElements(webElements(tagName(OPTION), FIVE_SECONDS), 3).negate(),
                        contains(equalTo(COMMON_LABELED_SELECT3)),
                        expectedDescriptionOfTheFoundElements(1, tagName(SELECT),
                                shouldHaveText(compile("19"))
                                        .and(shouldContainElements(webElements(tagName(OPTION), FIVE_SECONDS), 3).negate()))},

                {tagName(BUTTON_TAG), compile("Text5"),
                        shouldContainElements(webElements(tagName(LABEL_TAG), BUTTON_LABEL_TEXT1, FIVE_SECONDS)),
                        contains(equalTo(COMMON_LABELED_BUTTON1)),
                        expectedDescriptionOfTheFoundElements(1, tagName(BUTTON_TAG),
                                shouldHaveText(compile("Text5"))
                                        .and(shouldContainElements(webElements(tagName(LABEL_TAG), BUTTON_LABEL_TEXT1, FIVE_SECONDS))))},

                {tagName(BUTTON_TAG), compile("Text3"),
                        shouldContainElements(webElements(tagName(LABEL_TAG), BUTTON_LABEL_TEXT1, ofMillis(50))).negate(),
                        contains(equalTo(COMMON_BUTTON3)),
                        expectedDescriptionOfTheFoundElements(1, tagName(BUTTON_TAG),
                                shouldHaveText(compile("Text3"))
                                        .and(shouldContainElements(webElements(tagName(LABEL_TAG), BUTTON_LABEL_TEXT1, ofMillis(50))).negate()))},

                {CHAINED_FIND_TAB, compile(TAB_TEXT2), shouldHaveAttribute(ATTR19, VALUE12),
                        contains(equalTo(COMMON_TAB2)),
                        expectedDescriptionOfTheFoundElements(1, CHAINED_FIND_TAB,
                                shouldHaveText(compile(TAB_TEXT2))
                                        .and(shouldHaveAttribute(ATTR19, VALUE12)))},

                {CHAINED_FIND_TAB, compile("Text3"), shouldHaveAttribute(ATTR19, VALUE12).negate(),
                        contains(equalTo(COMMON_TAB3)),
                        expectedDescriptionOfTheFoundElements(1, CHAINED_FIND_TAB,
                                shouldHaveText(compile("Text3"))
                                        .and(shouldHaveAttribute(ATTR19, VALUE12).negate()))},

                {CHAINED_FIND_TAB, compile("Text3"), shouldHaveAttributeContains(ATTR20, VALUE14),
                        contains(equalTo(COMMON_TAB3)),
                        expectedDescriptionOfTheFoundElements(1, CHAINED_FIND_TAB,
                                shouldHaveText(compile("Text3"))
                                        .and(shouldHaveAttributeContains(ATTR20, VALUE14)))},

                {CHAINED_FIND_TAB, compile("Tab text "), shouldHaveAttributeContains(ATTR20, VALUE14).negate(),
                        contains(equalTo(COMMON_TAB1),
                                equalTo(COMMON_TAB2),
                                equalTo(COMMON_TAB4)),
                        expectedDescriptionOfTheFoundElements(3, CHAINED_FIND_TAB,
                                shouldHaveText(compile("Tab text "))
                                        .and(shouldHaveAttributeContains(ATTR20, VALUE14).negate()))},

                {CHAINED_FIND_TAB, compile("1"), shouldHaveAttributeContains(ATTR20, compile(VALUE12)),
                        contains(equalTo(COMMON_TAB1)),
                        expectedDescriptionOfTheFoundElements(1, CHAINED_FIND_TAB,
                                shouldHaveText(compile("1"))
                                        .and(shouldHaveAttributeContains(ATTR20, compile(VALUE12))))},

                {CHAINED_FIND_TAB, compile("3"), shouldHaveAttributeContains(ATTR20, compile(VALUE12)).negate(),
                        contains(equalTo(COMMON_TAB3)),
                        expectedDescriptionOfTheFoundElements(1, CHAINED_FIND_TAB,
                                shouldHaveText(compile("3"))
                                        .and(shouldHaveAttributeContains(ATTR20, compile(VALUE12)).negate()))},

                {xpath(TEXT_FIELD_XPATH), compile("3"), shouldHaveCssValue(CSS8, CSS_VALUE6),
                        contains(equalTo(COMMON_TEXT_INPUT3)),
                        expectedDescriptionOfTheFoundElements(1, xpath(TEXT_FIELD_XPATH),
                                shouldHaveText(compile("3"))
                                        .and(shouldHaveCssValue(CSS8, CSS_VALUE6)))},

                {xpath(TEXT_FIELD_XPATH), compile("4"), shouldHaveCssValue(CSS8, CSS_VALUE6).negate(),
                        contains(equalTo(COMMON_TEXT_INPUT4)),
                        expectedDescriptionOfTheFoundElements(1, xpath(TEXT_FIELD_XPATH),
                                shouldHaveText(compile("4"))
                                        .and(shouldHaveCssValue(CSS8, CSS_VALUE6).negate()))},

                {xpath(TEXT_FIELD_XPATH), compile(INPUT_TEXT1), shouldHaveCssValueContains(CSS8, "4")
                        .and(shouldHaveCssValueContains(CSS9, "5")),
                        contains(equalTo(COMMON_TEXT_INPUT1)),
                        expectedDescriptionOfTheFoundElements(1, xpath(TEXT_FIELD_XPATH),
                                shouldHaveText(compile(INPUT_TEXT1))
                                        .and(shouldHaveCssValueContains(CSS8, "4")
                                                .and(shouldHaveCssValueContains(CSS9, "5"))))},

                {xpath(TEXT_FIELD_XPATH), compile(INPUT_TEXT4), shouldHaveCssValueContains(CSS8, "4")
                        .and(shouldHaveCssValueContains(CSS9, "5")).negate(),
                        contains(equalTo(COMMON_TEXT_INPUT4)),
                        expectedDescriptionOfTheFoundElements(1, xpath(TEXT_FIELD_XPATH),
                                shouldHaveText(compile(INPUT_TEXT4))
                                        .and(shouldHaveCssValueContains(CSS8, "4")
                                                .and(shouldHaveCssValueContains(CSS9, "5")).negate()))},

                {xpath(TEXT_FIELD_XPATH), compile("1"), shouldHaveCssValueContains(CSS8, compile("4"))
                        .and(shouldHaveCssValueContains(CSS9, compile("5"))),
                        contains(equalTo(COMMON_TEXT_INPUT1)),
                        expectedDescriptionOfTheFoundElements(1, xpath(TEXT_FIELD_XPATH),
                                shouldHaveText(compile("1"))
                                        .and(shouldHaveCssValueContains(CSS8, compile("4"))
                                                .and(shouldHaveCssValueContains(CSS9, compile("5")))))},

                {xpath(TEXT_FIELD_XPATH), compile(INPUT_TEXT4), shouldHaveCssValueContains(CSS8, compile("4"))
                        .and(shouldHaveCssValueContains(CSS9, compile("5"))).negate(),
                        contains(equalTo(COMMON_TEXT_INPUT4)),
                        expectedDescriptionOfTheFoundElements(1, xpath(TEXT_FIELD_XPATH),
                                shouldHaveText(compile(INPUT_TEXT4))
                                        .and(shouldHaveCssValueContains(CSS8, compile("4"))
                                                .and(shouldHaveCssValueContains(CSS9, compile("5"))).negate()))},
        };
    }

    @Test(dataProvider = "search criteria3", retryAnalyzer = RetryAnalyzer.class)
    public void findElementByCriteriaAndTextPatternTest(By by, Pattern pattern, Predicate<? extends SearchContext> criteria,
                                                        Matcher<List<WebElement>> matcher,
                                                        String expectedListDescription) {
        setStartBenchMark();
        List<WebElement> webElements = seleniumSteps.find(webElements(by, pattern, (Predicate<? super WebElement>) criteria));
        setEndBenchMark();
        assertThat(new BigDecimal(getTimeDifference()),
                either(closeTo(new BigDecimal(HALF_SECOND.toMillis()), new BigDecimal(450)))
                        .or(closeTo(new BigDecimal(ONE_SECOND.toMillis()), new BigDecimal(200))));
        assertThat(webElements, matcher);
        assertThat(webElements.toString(), is(expectedListDescription));
    }

    @Test(dataProvider = "search criteria3", retryAnalyzer = RetryAnalyzer.class)
    public void findElementByCriteriaAndTextPatternWithDefinedTimeTest(By by, Pattern pattern, Predicate<? extends SearchContext> criteria,
                                                                       Matcher<List<WebElement>> matcher,
                                                                       String expectedListDescription) {
        setStartBenchMark();
        List<WebElement> webElements = seleniumSteps.find(webElements(by, pattern, FIVE_SECONDS, (Predicate<? super WebElement>) criteria));
        setEndBenchMark();
        assertThat(new BigDecimal(getTimeDifference()),
                either(closeTo(new BigDecimal(HALF_SECOND.toMillis()), new BigDecimal(450)))
                        .or(closeTo(new BigDecimal(ONE_SECOND.toMillis()), new BigDecimal(200))));
        assertThat(webElements, matcher);
        assertThat(webElements.toString(), is(expectedListDescription));
    }

    @Test(dataProvider = "search criteria3", retryAnalyzer = RetryAnalyzer.class)
    public void findElementByCriteriaAndTexPatterntWithTimeDefinedImplicitlyTest(By by, Pattern pattern, Predicate<? extends SearchContext> criteria,
                                                                                 Matcher<List<WebElement>> matcher,
                                                                                 String expectedListDescription) {
        setProperty(ELEMENT_WAITING_TIME_UNIT.getPropertyName(), "SECONDS");
        setProperty(ELEMENT_WAITING_TIME_VALUE.getPropertyName(), "5");
        try {
            setStartBenchMark();
            List<WebElement> webElements = seleniumSteps.find(webElements(by, pattern, (Predicate<? super WebElement>) criteria));
            setEndBenchMark();
            assertThat(new BigDecimal(getTimeDifference()),
                    either(closeTo(new BigDecimal(HALF_SECOND.toMillis()), new BigDecimal(450)))
                            .or(closeTo(new BigDecimal(ONE_SECOND.toMillis()), new BigDecimal(200))));
            assertThat(webElements, matcher);
            assertThat(webElements.toString(), is(expectedListDescription));
        }
        finally {
            removeProperty(ELEMENT_WAITING_TIME_UNIT.getPropertyName());
            removeProperty(ELEMENT_WAITING_TIME_VALUE.getPropertyName());
        }
    }
}
