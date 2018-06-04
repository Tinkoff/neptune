package com.github.toy.constructor.selenium.test.steps.tests.searching;

import com.github.toy.constructor.selenium.test.BaseWebDriverTest;
import org.hamcrest.Matcher;
import org.openqa.selenium.By;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.List;
import java.util.function.Predicate;

import static com.github.toy.constructor.selenium.functions.searching.CommonConditions.*;
import static com.github.toy.constructor.selenium.functions.searching.MultipleSearchSupplier.webElements;
import static com.github.toy.constructor.selenium.functions.searching.SearchSupplier.webElement;
import static com.github.toy.constructor.selenium.properties.FlagProperties.FIND_ONLY_VISIBLE_ELEMENTS_WHEN_NO_CONDITION;
import static com.github.toy.constructor.selenium.properties.WaitingProperties.TimeUnitProperties.ELEMENT_WAITING_TIME_UNIT;
import static com.github.toy.constructor.selenium.properties.WaitingProperties.TimeValueProperties.ELEMENT_WAITING_TIME_VALUE;
import static com.github.toy.constructor.selenium.test.FakeDOMModel.*;
import static java.lang.String.format;
import static java.time.Duration.ofMillis;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.openqa.selenium.By.className;
import static org.openqa.selenium.By.tagName;
import static org.openqa.selenium.By.xpath;

@SuppressWarnings("unchecked")
public class SearchForWebElementsPositiveTest extends BaseWebDriverTest {

    private static final String FOUND_BY_PATTERN = "%s web elements found %s";
    private static final String FOUND_ON_CONDITION = FOUND_BY_PATTERN + " on condition '%s'";

    private static String expectedDescriptionOfTheFoundElements(int count, By by, Predicate<? extends SearchContext> condition) {
        return format(FOUND_ON_CONDITION, count, by, condition);
    }

    private static String expectedDescriptionOfTheFoundElements(int count, By by) {
        return format(FOUND_BY_PATTERN, count, by);
    }

    @Test
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

    @Test
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

    @Test
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

    @Test
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

    @Test
    public void findWebElementsChainedWithoutConditionTest() {
        List<WebElement> webElements = seleniumSteps.find(webElements(className(CUSTOM_BUTTON_CLASS))
                .foundFrom(webElement(className(SPREAD_SHEET_CLASS))));
        assertThat(webElements, contains(equalTo(CUSTOM_LABELED_BUTTON1)));
        assertThat(webElements.toString(), is(expectedDescriptionOfTheFoundElements(1, className(CUSTOM_BUTTON_CLASS))));
    }

    @Test
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

    @Test
    public void findWebElementsChainedOnlyWhenTimeIsDefinedTest() {
        setStartBenchMark();
        List<WebElement> webElements = seleniumSteps.find(webElements(className(CUSTOM_BUTTON_CLASS), FIVE_SECONDS)
                .foundFrom(webElement(className(SPREAD_SHEET_CLASS), FIVE_SECONDS)));
        setEndBenchMark();
        assertThat(getTimeDifference(), lessThan(HALF_SECOND.toMillis()));
        assertThat(webElements, contains(equalTo(CUSTOM_LABELED_BUTTON1)));
        assertThat(webElements.toString(), is(expectedDescriptionOfTheFoundElements(1, className(CUSTOM_BUTTON_CLASS))));
    }

    @Test
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

    @Test
    public void findWebElementsFromAnotherWithoutConditionTest() {
        WebElement spreadSheet = seleniumSteps.find(webElement(className(SPREAD_SHEET_CLASS)));
        List<WebElement> webElements = seleniumSteps.find(webElements(className(CUSTOM_BUTTON_CLASS))
                .foundFrom(spreadSheet));
        assertThat(webElements, contains(equalTo(CUSTOM_LABELED_BUTTON1)));
        assertThat(webElements.toString(), is(expectedDescriptionOfTheFoundElements(1, className(CUSTOM_BUTTON_CLASS))));
    }

    @Test
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

    @Test
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

    @Test
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
        };
    }

    @Test(dataProvider = "search criteria1")
    public void findElementsByCriteriaTest(By by, Predicate<? extends SearchContext> criteria,
                                           Matcher<List<WebElement>> matcher,
                                           String expectedListDescription) {
        setStartBenchMark();
        List<WebElement> webElements = seleniumSteps.find(webElements(by, (Predicate<? super WebElement>) criteria));
        setEndBenchMark();
        assertThat(getTimeDifference(), lessThan(ONE_SECOND.toMillis()));
        assertThat(webElements, matcher);
        assertThat(webElements.toString(), is(expectedListDescription));
    }

    @Test(dataProvider = "search criteria1")
    public void findElementsByCriteriaWithDefinedTimeTest(By by, Predicate<? extends SearchContext> criteria,
                                                          Matcher<List<WebElement>> matcher,
                                                          String expectedListDescription) {
        setStartBenchMark();
        List<WebElement> webElements = seleniumSteps.find(webElements(by, FIVE_SECONDS, (Predicate<? super WebElement>) criteria));
        setEndBenchMark();
        assertThat(getTimeDifference(), lessThan(ONE_SECOND.toMillis()));
        assertThat(webElements, matcher);
        assertThat(webElements.toString(), is(expectedListDescription));
    }

    @Test(dataProvider = "search criteria1")
    public void findElementsByCriteriaWithTimeDefinedImplicitlyTest(By by, Predicate<? extends SearchContext> criteria,
                                                                    Matcher<List<WebElement>> matcher,
                                                                    String expectedListDescription) {
        setProperty(ELEMENT_WAITING_TIME_UNIT.getPropertyName(), "SECONDS");
        setProperty(ELEMENT_WAITING_TIME_VALUE.getPropertyName(), "5");
        try {
            setStartBenchMark();
            List<WebElement> webElements = seleniumSteps.find(webElements(by, (Predicate<? super WebElement>) criteria));
            setEndBenchMark();
            assertThat(getTimeDifference(), lessThan(ONE_SECOND.toMillis()));
            assertThat(webElements, matcher);
            assertThat(webElements.toString(), is(expectedListDescription));
        }
        finally {
            removeProperty(ELEMENT_WAITING_TIME_UNIT.getPropertyName());
            removeProperty(ELEMENT_WAITING_TIME_VALUE.getPropertyName());
        }
    }
}
