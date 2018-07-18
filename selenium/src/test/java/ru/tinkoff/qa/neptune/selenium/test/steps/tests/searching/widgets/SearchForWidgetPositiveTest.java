package ru.tinkoff.qa.neptune.selenium.test.steps.tests.searching.widgets;

import ru.tinkoff.qa.neptune.selenium.api.widget.Widget;
import ru.tinkoff.qa.neptune.selenium.functions.searching.SearchSupplier;
import ru.tinkoff.qa.neptune.selenium.test.BaseWebDriverTest;
import ru.tinkoff.qa.neptune.selenium.test.RetryAnalyzer;
import ru.tinkoff.qa.neptune.selenium.test.steps.tests.searching.widgets.buttons.CustomizedButton;
import ru.tinkoff.qa.neptune.selenium.test.steps.tests.searching.widgets.buttons.LabeledButton;
import ru.tinkoff.qa.neptune.selenium.test.steps.tests.searching.widgets.buttons.SimpleButton;
import ru.tinkoff.qa.neptune.selenium.test.steps.tests.searching.widgets.flags.LabeledCheckBox;
import ru.tinkoff.qa.neptune.selenium.test.steps.tests.searching.widgets.flags.LabeledRadioButton;
import ru.tinkoff.qa.neptune.selenium.test.steps.tests.searching.widgets.flags.SimpleCheckbox;
import ru.tinkoff.qa.neptune.selenium.test.steps.tests.searching.widgets.flags.SimpleRadioButton;
import ru.tinkoff.qa.neptune.selenium.test.steps.tests.searching.widgets.link.CustomizedLink;
import ru.tinkoff.qa.neptune.selenium.test.steps.tests.searching.widgets.link.LabeledLink;
import ru.tinkoff.qa.neptune.selenium.test.steps.tests.searching.widgets.select.LabeledSelect;
import ru.tinkoff.qa.neptune.selenium.test.steps.tests.searching.widgets.select.MultiSelect;
import ru.tinkoff.qa.neptune.selenium.test.steps.tests.searching.widgets.select.SimpleSelect;
import ru.tinkoff.qa.neptune.selenium.test.steps.tests.searching.widgets.tab.CustomizedTab;
import ru.tinkoff.qa.neptune.selenium.test.steps.tests.searching.widgets.tab.LabeledTab;
import ru.tinkoff.qa.neptune.selenium.test.steps.tests.searching.widgets.tab.SimpleTab;
import ru.tinkoff.qa.neptune.selenium.test.steps.tests.searching.widgets.table.LabeledTable;
import ru.tinkoff.qa.neptune.selenium.test.steps.tests.searching.widgets.table.SimpleTable;
import ru.tinkoff.qa.neptune.selenium.test.steps.tests.searching.widgets.table.SpreadsheetTable;
import org.openqa.selenium.WebElement;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.math.BigDecimal;
import java.util.function.Predicate;

import static ru.tinkoff.qa.neptune.core.api.StoryWriter.condition;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.CommonConditions.*;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.CommonConditions.shouldHaveAttribute;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.MultipleSearchSupplier.webElements;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.SearchSupplier.*;
import static ru.tinkoff.qa.neptune.selenium.test.FakeDOMModel.*;
import static ru.tinkoff.qa.neptune.selenium.test.FakeDOMModel.COMMON_LABELED_BUTTON1;
import static ru.tinkoff.qa.neptune.selenium.test.steps.tests.searching.widgets.WidgetNames.*;
import static java.lang.String.format;
import static java.time.Duration.ofMillis;
import static java.util.List.of;
import static java.util.Optional.ofNullable;
import static java.util.regex.Pattern.compile;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.openqa.selenium.By.tagName;

public class SearchForWidgetPositiveTest extends BaseWebDriverTest {

    private static String getWidgetDescription(String name, Predicate<?> condition) {
        return format("%s found on conditions '%s'", name, condition);
    }

    @DataProvider(name = "search without criteria")
    public static Object[][] searchCriteria() {
        return new Object[][] {
                {button(), COMMON_BUTTON1,
                        SimpleButton.class, SIMPLE_BUTTON},

                {button(FIVE_SECONDS), COMMON_BUTTON1,
                        SimpleButton.class, SIMPLE_BUTTON},

                {button(of(BUTTON_LABEL_TEXT1)), COMMON_LABELED_BUTTON1,
                        LabeledButton.class,
                        getWidgetDescription(LABELED_BUTTON, shouldBeLabeledBy(BUTTON_LABEL_TEXT1))},

                {button(of(BUTTON_LABEL_TEXT1), FIVE_SECONDS), COMMON_LABELED_BUTTON1,
                        LabeledButton.class,
                        getWidgetDescription(LABELED_BUTTON, shouldBeLabeledBy(BUTTON_LABEL_TEXT1))},

                {button(of(BUTTON_LABEL_TEXT10, BUTTON_LABEL_TEXT6)), CUSTOM_LABELED_BUTTON2,
                        CustomizedButton.class,
                        getWidgetDescription(CUSTOM_BUTTON,
                                shouldBeLabeledBy(BUTTON_LABEL_TEXT10, BUTTON_LABEL_TEXT6))},

                {button(of(BUTTON_LABEL_TEXT10, BUTTON_LABEL_TEXT6), FIVE_SECONDS), CUSTOM_LABELED_BUTTON2,
                        CustomizedButton.class,
                        getWidgetDescription(CUSTOM_BUTTON,
                                shouldBeLabeledBy(BUTTON_LABEL_TEXT10, BUTTON_LABEL_TEXT6))},

                {button(BUTTON_LABEL_TEXT10), CUSTOM_LABELED_BUTTON2,
                        CustomizedButton.class,
                        getWidgetDescription(CUSTOM_BUTTON,
                                shouldBeLabeledBy(BUTTON_LABEL_TEXT10))},

                {button(BUTTON_LABEL_TEXT10, FIVE_SECONDS), CUSTOM_LABELED_BUTTON2,
                        CustomizedButton.class,
                        getWidgetDescription(CUSTOM_BUTTON,
                                shouldBeLabeledBy(BUTTON_LABEL_TEXT10))},

                {button(shouldBeEnabled()), COMMON_BUTTON3,
                        SimpleButton.class,
                        getWidgetDescription(SIMPLE_BUTTON,
                                shouldBeEnabled())},

                {button(shouldBeEnabled().negate()), COMMON_BUTTON1,
                        SimpleButton.class,
                        getWidgetDescription(SIMPLE_BUTTON,
                                shouldBeEnabled().negate())},

                {button(shouldBeVisible()), COMMON_BUTTON2,
                        SimpleButton.class,
                        getWidgetDescription(SIMPLE_BUTTON,
                                shouldBeVisible())},

                {button(shouldBeVisible().negate()), COMMON_BUTTON1,
                        SimpleButton.class,
                        getWidgetDescription(SIMPLE_BUTTON,
                                shouldBeVisible().negate())},

                {button(shouldContainElements(webElements(tagName(LABEL_TAG), ofMillis(5)), 1)),
                        COMMON_LABELED_BUTTON1,
                        SimpleButton.class,
                        getWidgetDescription(SIMPLE_BUTTON,
                                shouldContainElements(webElements(tagName(LABEL_TAG), ofMillis(5)), 1))},

                {button(shouldContainElements(webElements(tagName(LABEL_TAG), ofMillis(5)), 1)
                        .negate()),
                        COMMON_BUTTON1,
                        SimpleButton.class,
                        getWidgetDescription(SIMPLE_BUTTON,
                                shouldContainElements(webElements(tagName(LABEL_TAG), ofMillis(5)), 1)
                                        .negate())},

                {button(shouldHaveAttribute(ATTR5, VALUE11)
                        .and(shouldHaveAttribute(ATTR6, VALUE12))), CUSTOM_LABELED_BUTTON2,
                        CustomizedButton.class,
                        getWidgetDescription(CUSTOM_BUTTON,
                                shouldHaveAttribute(ATTR5, VALUE11)
                                        .and(shouldHaveAttribute(ATTR6, VALUE12)))},

                {button(shouldHaveAttribute(ATTR5, VALUE11)
                        .and(shouldHaveAttribute(ATTR6, VALUE12)).negate()), COMMON_BUTTON1,
                        SimpleButton.class,
                        getWidgetDescription(SIMPLE_BUTTON,
                                shouldHaveAttribute(ATTR5, VALUE11)
                                        .and(shouldHaveAttribute(ATTR6, VALUE12)).negate())},

                {button(shouldHaveAttributeContains(ATTR1, "1")
                        .or(shouldHaveAttributeContains(ATTR6, "12"))), COMMON_LABELED_BUTTON1,
                        SimpleButton.class,
                        getWidgetDescription(SIMPLE_BUTTON,
                                shouldHaveAttributeContains(ATTR1, "1")
                                        .or(shouldHaveAttributeContains(ATTR6, "12")))},

                {button(shouldHaveAttributeContains(ATTR1, "1")
                        .or(shouldHaveAttributeContains(ATTR6, "12")).negate()), COMMON_BUTTON1,
                        SimpleButton.class,
                        getWidgetDescription(SIMPLE_BUTTON,
                                shouldHaveAttributeContains(ATTR1, "1")
                                        .or(shouldHaveAttributeContains(ATTR6, "12")).negate())},

                {button(shouldHaveAttributeContains(ATTR1, compile("1"))
                        .or(shouldHaveAttributeContains(ATTR6, compile("12")))), COMMON_LABELED_BUTTON1,
                        SimpleButton.class,
                        getWidgetDescription(SIMPLE_BUTTON,
                                shouldHaveAttributeContains(ATTR1, compile("1"))
                                        .or(shouldHaveAttributeContains(ATTR6, compile("12"))))},

                {button(shouldHaveAttributeContains(ATTR1, compile("1"))
                        .or(shouldHaveAttributeContains(ATTR6, compile("12"))).negate()), COMMON_BUTTON1,
                        SimpleButton.class,
                        getWidgetDescription(SIMPLE_BUTTON,
                                shouldHaveAttributeContains(ATTR1, compile("1"))
                                        .or(shouldHaveAttributeContains(ATTR6, compile("12"))).negate())},

                {button(shouldHaveCssValue(CSS2, CSS_VALUE4)
                        .and(shouldHaveCssValue(CSS3, CSS_VALUE8))), COMMON_LABELED_BUTTON2,
                        SimpleButton.class,
                        getWidgetDescription(SIMPLE_BUTTON,
                                shouldHaveCssValue(CSS2, CSS_VALUE4)
                                        .and(shouldHaveCssValue(CSS3, CSS_VALUE8)))},

                {button(shouldHaveCssValue(CSS2, CSS_VALUE4)
                        .and(shouldHaveCssValue(CSS3, CSS_VALUE8)).negate()), COMMON_BUTTON1,
                        SimpleButton.class,
                        getWidgetDescription(SIMPLE_BUTTON,
                                shouldHaveCssValue(CSS2, CSS_VALUE4)
                                        .and(shouldHaveCssValue(CSS3, CSS_VALUE8)).negate())},

                {button(shouldHaveCssValueContains(CSS5, "9")
                        .or(shouldHaveCssValueContains(CSS6, "11"))), CUSTOM_LABELED_BUTTON3,
                        CustomizedButton.class,
                        getWidgetDescription(CUSTOM_BUTTON,
                                shouldHaveCssValueContains(CSS5, "9")
                                        .or(shouldHaveCssValueContains(CSS6, "11")))},

                {button(shouldHaveCssValueContains(CSS5, "9")
                        .or(shouldHaveCssValueContains(CSS6, "11")).negate()), COMMON_BUTTON1,
                        SimpleButton.class,
                        getWidgetDescription(SIMPLE_BUTTON,
                                shouldHaveCssValueContains(CSS5, "9")
                                        .or(shouldHaveCssValueContains(CSS6, "11")).negate())},

                {button(shouldHaveCssValueContains(CSS16, compile("18"))
                        .or(shouldHaveCssValueContains(CSS4, compile("5")))), COMMON_BUTTON1,
                        SimpleButton.class,
                        getWidgetDescription(SIMPLE_BUTTON,
                                shouldHaveCssValueContains(CSS16, compile("18"))
                                        .or(shouldHaveCssValueContains(CSS4, compile("5"))))},

                {button(shouldHaveCssValueContains(CSS4, compile("5"))
                        .or(shouldHaveCssValueContains(CSS16, compile("18"))).negate()), COMMON_BUTTON3,
                        SimpleButton.class,
                        getWidgetDescription(SIMPLE_BUTTON,
                                shouldHaveCssValueContains(CSS4, compile("5"))
                                        .or(shouldHaveCssValueContains(CSS16, compile("18"))).negate())},

                {button(FIVE_SECONDS, shouldBeEnabled()), COMMON_BUTTON3,
                        SimpleButton.class,
                        getWidgetDescription(SIMPLE_BUTTON,
                                shouldBeEnabled())},

                {button(FIVE_SECONDS, shouldBeEnabled().negate()), COMMON_BUTTON1,
                        SimpleButton.class,
                        getWidgetDescription(SIMPLE_BUTTON,
                                shouldBeEnabled().negate())},

                {button(FIVE_SECONDS, shouldBeVisible()), COMMON_BUTTON2,
                        SimpleButton.class,
                        getWidgetDescription(SIMPLE_BUTTON,
                                shouldBeVisible())},

                {button(FIVE_SECONDS, shouldBeVisible().negate()), COMMON_BUTTON1,
                        SimpleButton.class,
                        getWidgetDescription(SIMPLE_BUTTON,
                                shouldBeVisible().negate())},

                {button(FIVE_SECONDS, shouldContainElements(webElements(tagName(LABEL_TAG), ofMillis(5)), 1)),
                        COMMON_LABELED_BUTTON1,
                        SimpleButton.class,
                        getWidgetDescription(SIMPLE_BUTTON,
                                shouldContainElements(webElements(tagName(LABEL_TAG), ofMillis(5)), 1))},

                {button(FIVE_SECONDS, shouldContainElements(webElements(tagName(LABEL_TAG), ofMillis(5)), 1)
                        .negate()),
                        COMMON_BUTTON1,
                        SimpleButton.class,
                        getWidgetDescription(SIMPLE_BUTTON,
                                shouldContainElements(webElements(tagName(LABEL_TAG), ofMillis(5)), 1)
                                        .negate())},

                {button(FIVE_SECONDS, shouldHaveAttribute(ATTR5, VALUE11)
                        .and(shouldHaveAttribute(ATTR6, VALUE12))), CUSTOM_LABELED_BUTTON2,
                        CustomizedButton.class,
                        getWidgetDescription(CUSTOM_BUTTON,
                                shouldHaveAttribute(ATTR5, VALUE11)
                                        .and(shouldHaveAttribute(ATTR6, VALUE12)))},

                {button(FIVE_SECONDS, shouldHaveAttribute(ATTR5, VALUE11)
                        .and(shouldHaveAttribute(ATTR6, VALUE12)).negate()), COMMON_BUTTON1,
                        SimpleButton.class,
                        getWidgetDescription(SIMPLE_BUTTON,
                                shouldHaveAttribute(ATTR5, VALUE11)
                                        .and(shouldHaveAttribute(ATTR6, VALUE12)).negate())},

                {button(FIVE_SECONDS, shouldHaveAttributeContains(ATTR1, "1")
                        .or(shouldHaveAttributeContains(ATTR6, "12"))), COMMON_LABELED_BUTTON1,
                        SimpleButton.class,
                        getWidgetDescription(SIMPLE_BUTTON,
                                shouldHaveAttributeContains(ATTR1, "1")
                                        .or(shouldHaveAttributeContains(ATTR6, "12")))},

                {button(FIVE_SECONDS, shouldHaveAttributeContains(ATTR1, "1")
                        .or(shouldHaveAttributeContains(ATTR6, "12")).negate()), COMMON_BUTTON1,
                        SimpleButton.class,
                        getWidgetDescription(SIMPLE_BUTTON,
                                shouldHaveAttributeContains(ATTR1, "1")
                                        .or(shouldHaveAttributeContains(ATTR6, "12")).negate())},

                {button(FIVE_SECONDS, shouldHaveAttributeContains(ATTR1, compile("1"))
                        .or(shouldHaveAttributeContains(ATTR6, compile("12")))), COMMON_LABELED_BUTTON1,
                        SimpleButton.class,
                        getWidgetDescription(SIMPLE_BUTTON,
                                shouldHaveAttributeContains(ATTR1, compile("1"))
                                        .or(shouldHaveAttributeContains(ATTR6, compile("12"))))},

                {button(FIVE_SECONDS, shouldHaveAttributeContains(ATTR1, compile("1"))
                        .or(shouldHaveAttributeContains(ATTR6, compile("12"))).negate()), COMMON_BUTTON1,
                        SimpleButton.class,
                        getWidgetDescription(SIMPLE_BUTTON,
                                shouldHaveAttributeContains(ATTR1, compile("1"))
                                        .or(shouldHaveAttributeContains(ATTR6, compile("12"))).negate())},

                {button(FIVE_SECONDS, shouldHaveCssValue(CSS2, CSS_VALUE4)
                        .and(shouldHaveCssValue(CSS3, CSS_VALUE8))), COMMON_LABELED_BUTTON2,
                        SimpleButton.class,
                        getWidgetDescription(SIMPLE_BUTTON,
                                shouldHaveCssValue(CSS2, CSS_VALUE4)
                                        .and(shouldHaveCssValue(CSS3, CSS_VALUE8)))},

                {button(FIVE_SECONDS, shouldHaveCssValue(CSS2, CSS_VALUE4)
                        .and(shouldHaveCssValue(CSS3, CSS_VALUE8)).negate()), COMMON_BUTTON1,
                        SimpleButton.class,
                        getWidgetDescription(SIMPLE_BUTTON,
                                shouldHaveCssValue(CSS2, CSS_VALUE4)
                                        .and(shouldHaveCssValue(CSS3, CSS_VALUE8)).negate())},

                {button(FIVE_SECONDS, shouldHaveCssValueContains(CSS5, "9")
                        .or(shouldHaveCssValueContains(CSS6, "11"))), CUSTOM_LABELED_BUTTON3,
                        CustomizedButton.class,
                        getWidgetDescription(CUSTOM_BUTTON,
                                shouldHaveCssValueContains(CSS5, "9")
                                        .or(shouldHaveCssValueContains(CSS6, "11")))},

                {button(FIVE_SECONDS, shouldHaveCssValueContains(CSS5, "9")
                        .or(shouldHaveCssValueContains(CSS6, "11")).negate()), COMMON_BUTTON1,
                        SimpleButton.class,
                        getWidgetDescription(SIMPLE_BUTTON,
                                shouldHaveCssValueContains(CSS5, "9")
                                        .or(shouldHaveCssValueContains(CSS6, "11")).negate())},

                {button(FIVE_SECONDS, shouldHaveCssValueContains(CSS16, compile("18"))
                        .or(shouldHaveCssValueContains(CSS4, compile("5")))), COMMON_BUTTON1,
                        SimpleButton.class,
                        getWidgetDescription(SIMPLE_BUTTON,
                                shouldHaveCssValueContains(CSS16, compile("18"))
                                        .or(shouldHaveCssValueContains(CSS4, compile("5"))))},

                {button(FIVE_SECONDS, shouldHaveCssValueContains(CSS4, compile("5"))
                        .or(shouldHaveCssValueContains(CSS16, compile("18"))).negate()), COMMON_BUTTON3,
                        SimpleButton.class,
                        getWidgetDescription(SIMPLE_BUTTON,
                                shouldHaveCssValueContains(CSS4, compile("5"))
                                        .or(shouldHaveCssValueContains(CSS16, compile("18"))).negate())},

                {button(of(BUTTON_LABEL_TEXT5, BUTTON_LABEL_TEXT9),
                        shouldBeEnabled()),
                        CUSTOM_LABELED_BUTTON1,
                        CustomizedButton.class,
                        getWidgetDescription(CUSTOM_BUTTON,
                                shouldBeLabeledBy(BUTTON_LABEL_TEXT5, BUTTON_LABEL_TEXT9).and(shouldBeEnabled()))},

                {button(BUTTON_LABEL_TEXT7,
                        shouldBeEnabled().negate()),
                        CUSTOM_LABELED_BUTTON3,
                        CustomizedButton.class,
                        getWidgetDescription(CUSTOM_BUTTON,
                                shouldBeLabeledBy(BUTTON_LABEL_TEXT7).and(shouldBeEnabled().negate()))},

                {button(BUTTON_LABEL_TEXT11, shouldBeVisible()),
                        CUSTOM_LABELED_BUTTON3,
                        CustomizedButton.class,
                        getWidgetDescription(CUSTOM_BUTTON,
                                shouldBeLabeledBy(BUTTON_LABEL_TEXT11).and(shouldBeVisible()))},

                {button(of(BUTTON_LABEL_TEXT6, BUTTON_LABEL_TEXT10),
                        shouldBeVisible().negate()),
                        CUSTOM_LABELED_BUTTON2,
                        CustomizedButton.class,
                        getWidgetDescription(CUSTOM_BUTTON,
                                shouldBeLabeledBy(BUTTON_LABEL_TEXT6, BUTTON_LABEL_TEXT10)
                                        .and(shouldBeVisible().negate()))},

                {button(BUTTON_LABEL_TEXT3, shouldContainElements(webElements(tagName(LABEL_TAG), ofMillis(5)), 1)),
                        COMMON_LABELED_BUTTON3,
                        LabeledButton.class,
                        getWidgetDescription(LABELED_BUTTON,
                                shouldBeLabeledBy(BUTTON_LABEL_TEXT3)
                                        .and(shouldContainElements(webElements(tagName(LABEL_TAG), ofMillis(5)), 1)))},

                {button(of(BUTTON_LABEL_TEXT7, BUTTON_LABEL_TEXT11),
                        shouldContainElements(webElements(tagName(LABEL_TAG), ofMillis(5)), 2)
                                .negate()),
                        CUSTOM_LABELED_BUTTON3,
                        CustomizedButton.class,
                        getWidgetDescription(CUSTOM_BUTTON,
                                shouldBeLabeledBy(BUTTON_LABEL_TEXT7, BUTTON_LABEL_TEXT11)
                                        .and(shouldContainElements(webElements(tagName(LABEL_TAG), ofMillis(5)), 2)
                                                .negate()))},

                {button(BUTTON_LABEL_TEXT2, shouldHaveAttribute(ATTR1, VALUE2)
                        .and(shouldHaveAttribute(ATTR2, VALUE10))),
                        COMMON_LABELED_BUTTON2,
                        LabeledButton.class,
                        getWidgetDescription(LABELED_BUTTON,
                                shouldBeLabeledBy(BUTTON_LABEL_TEXT2)
                                        .and(shouldHaveAttribute(ATTR1, VALUE2)
                                                .and(shouldHaveAttribute(ATTR2, VALUE10))))},

                {button(BUTTON_LABEL_TEXT4, shouldHaveAttribute(ATTR1, VALUE3)
                        .and(shouldHaveAttribute(ATTR2, VALUE11)).negate()),
                        COMMON_LABELED_BUTTON4,
                        SimpleButton.class,
                        getWidgetDescription(LABELED_BUTTON,
                                shouldBeLabeledBy(BUTTON_LABEL_TEXT4)
                                        .and(shouldHaveAttribute(ATTR1, VALUE3)
                                                .and(shouldHaveAttribute(ATTR2, VALUE11)).negate()))},

                {button(of(BUTTON_LABEL_TEXT8, BUTTON_LABEL_TEXT12),
                        shouldHaveAttributeContains(ATTR5, "1")), CUSTOM_LABELED_BUTTON4,
                        CustomizedButton.class,
                        getWidgetDescription(CUSTOM_BUTTON,
                                shouldBeLabeledBy(BUTTON_LABEL_TEXT8, BUTTON_LABEL_TEXT12)
                                        .and(shouldHaveAttributeContains(ATTR5, "1")))},

                {button(BUTTON_LABEL_TEXT1, shouldHaveAttributeContains(ATTR5, "1").negate()),
                        COMMON_LABELED_BUTTON1,
                        LabeledButton.class,
                        getWidgetDescription(LABELED_BUTTON,
                                shouldBeLabeledBy(BUTTON_LABEL_TEXT1)
                                        .and(shouldHaveAttributeContains(ATTR5, "1").negate()))},

                {button(of(BUTTON_LABEL_TEXT8, BUTTON_LABEL_TEXT12),
                        shouldHaveAttributeContains(ATTR5, compile("1"))),
                        CUSTOM_LABELED_BUTTON4,
                        CustomizedButton.class,
                        getWidgetDescription(CUSTOM_BUTTON,
                                shouldBeLabeledBy(BUTTON_LABEL_TEXT8, BUTTON_LABEL_TEXT12)
                                        .and(shouldHaveAttributeContains(ATTR5, compile("1"))))},

                {button(BUTTON_LABEL_TEXT1, shouldHaveAttributeContains(ATTR5, compile("1")).negate()),
                        COMMON_LABELED_BUTTON1,
                        LabeledButton.class,
                        getWidgetDescription(LABELED_BUTTON,
                                shouldBeLabeledBy(BUTTON_LABEL_TEXT1)
                                        .and(shouldHaveAttributeContains(ATTR5, compile("1")).negate()))},

                {button(BUTTON_LABEL_TEXT2, shouldHaveCssValue(CSS2, CSS_VALUE4)
                        .and(shouldHaveCssValue(CSS3, CSS_VALUE8))),
                        COMMON_LABELED_BUTTON2,
                        LabeledButton.class,
                        getWidgetDescription(LABELED_BUTTON,
                                shouldBeLabeledBy(BUTTON_LABEL_TEXT2)
                                        .and(shouldHaveCssValue(CSS2, CSS_VALUE4).and(shouldHaveCssValue(CSS3, CSS_VALUE8))))},

                {button(BUTTON_LABEL_TEXT1, shouldHaveCssValue(CSS2, CSS_VALUE4)
                        .and(shouldHaveCssValue(CSS3, CSS_VALUE8)).negate()),
                        COMMON_LABELED_BUTTON1,
                        LabeledButton.class,
                        getWidgetDescription(LABELED_BUTTON,
                                shouldBeLabeledBy(BUTTON_LABEL_TEXT1).and(shouldHaveCssValue(CSS2, CSS_VALUE4)
                                        .and(shouldHaveCssValue(CSS3, CSS_VALUE8)).negate()))},

                {button(of(BUTTON_LABEL_TEXT7, BUTTON_LABEL_TEXT11),
                        shouldHaveCssValueContains(CSS5, "9")
                                .or(shouldHaveCssValueContains(CSS6, "11"))),
                        CUSTOM_LABELED_BUTTON3,
                        CustomizedButton.class,
                        getWidgetDescription(CUSTOM_BUTTON,
                                shouldBeLabeledBy(BUTTON_LABEL_TEXT7, BUTTON_LABEL_TEXT11).and(shouldHaveCssValueContains(CSS5, "9")
                                        .or(shouldHaveCssValueContains(CSS6, "11"))))},

                {button(BUTTON_LABEL_TEXT1, shouldHaveCssValueContains(CSS5, "9")
                        .or(shouldHaveCssValueContains(CSS6, "11")).negate()),
                        COMMON_LABELED_BUTTON1,
                        LabeledButton.class,
                        getWidgetDescription(LABELED_BUTTON,
                                shouldBeLabeledBy(BUTTON_LABEL_TEXT1)
                                        .and(shouldHaveCssValueContains(CSS5, "9")
                                                .or(shouldHaveCssValueContains(CSS6, "11")).negate()))},

                {button(BUTTON_LABEL_TEXT2,
                        shouldHaveCssValueContains(CSS2, compile("4"))
                                .or(shouldHaveCssValueContains(CSS3, compile("8")))),
                        COMMON_LABELED_BUTTON2,
                        LabeledButton.class,
                        getWidgetDescription(LABELED_BUTTON,
                                shouldBeLabeledBy(BUTTON_LABEL_TEXT2)
                                        .and(shouldHaveCssValueContains(CSS2, compile("4"))
                                                .or(shouldHaveCssValueContains(CSS3, compile("8")))))},

                {button(BUTTON_LABEL_TEXT6,
                        shouldHaveCssValueContains(CSS2, compile("4"))
                                .or(shouldHaveCssValueContains(CSS3, compile("8"))).negate()),
                        CUSTOM_LABELED_BUTTON2,
                        CustomizedButton.class,
                        getWidgetDescription(CUSTOM_BUTTON,
                                shouldBeLabeledBy(BUTTON_LABEL_TEXT6).and(shouldHaveCssValueContains(CSS2, compile("4"))
                                        .or(shouldHaveCssValueContains(CSS3, compile("8"))).negate()))},

                {button(of(BUTTON_LABEL_TEXT5, BUTTON_LABEL_TEXT9), FIVE_SECONDS, shouldBeEnabled()),
                        CUSTOM_LABELED_BUTTON1,
                        CustomizedButton.class,
                        getWidgetDescription(CUSTOM_BUTTON,
                                shouldBeLabeledBy(BUTTON_LABEL_TEXT5, BUTTON_LABEL_TEXT9).and(shouldBeEnabled()))},

                {button(BUTTON_LABEL_TEXT7, FIVE_SECONDS,
                        shouldBeEnabled().negate()),
                        CUSTOM_LABELED_BUTTON3,
                        CustomizedButton.class,
                        getWidgetDescription(CUSTOM_BUTTON,
                                shouldBeLabeledBy(BUTTON_LABEL_TEXT7).and(shouldBeEnabled().negate()))},

                {button(BUTTON_LABEL_TEXT11, FIVE_SECONDS,  shouldBeVisible()),
                        CUSTOM_LABELED_BUTTON3,
                        CustomizedButton.class,
                        getWidgetDescription(CUSTOM_BUTTON,
                                shouldBeLabeledBy(BUTTON_LABEL_TEXT11).and(shouldBeVisible()))},

                {button(of(BUTTON_LABEL_TEXT6, BUTTON_LABEL_TEXT10), FIVE_SECONDS,
                        shouldBeVisible().negate()),
                        CUSTOM_LABELED_BUTTON2,
                        CustomizedButton.class,
                        getWidgetDescription(CUSTOM_BUTTON,
                                shouldBeLabeledBy(BUTTON_LABEL_TEXT6, BUTTON_LABEL_TEXT10)
                                        .and(shouldBeVisible().negate()))},

                {button(BUTTON_LABEL_TEXT3, FIVE_SECONDS, shouldContainElements(webElements(tagName(LABEL_TAG), ofMillis(5)), 1)),
                        COMMON_LABELED_BUTTON3,
                        LabeledButton.class,
                        getWidgetDescription(LABELED_BUTTON,
                                shouldBeLabeledBy(BUTTON_LABEL_TEXT3)
                                        .and(shouldContainElements(webElements(tagName(LABEL_TAG), ofMillis(5)), 1)))},

                {button(of(BUTTON_LABEL_TEXT7, BUTTON_LABEL_TEXT11), FIVE_SECONDS,
                        shouldContainElements(webElements(tagName(LABEL_TAG), ofMillis(5)), 2)
                                .negate()),
                        CUSTOM_LABELED_BUTTON3,
                        CustomizedButton.class,
                        getWidgetDescription(CUSTOM_BUTTON,
                                shouldBeLabeledBy(BUTTON_LABEL_TEXT7, BUTTON_LABEL_TEXT11)
                                        .and(shouldContainElements(webElements(tagName(LABEL_TAG), ofMillis(5)), 2)
                                                .negate()))},

                {button(BUTTON_LABEL_TEXT2, FIVE_SECONDS, shouldHaveAttribute(ATTR1, VALUE2)
                        .and(shouldHaveAttribute(ATTR2, VALUE10))),
                        COMMON_LABELED_BUTTON2,
                        LabeledButton.class,
                        getWidgetDescription(LABELED_BUTTON,
                                shouldBeLabeledBy(BUTTON_LABEL_TEXT2)
                                        .and(shouldHaveAttribute(ATTR1, VALUE2)
                                                .and(shouldHaveAttribute(ATTR2, VALUE10))))},

                {button(BUTTON_LABEL_TEXT4, FIVE_SECONDS,  shouldHaveAttribute(ATTR1, VALUE3)
                        .and(shouldHaveAttribute(ATTR2, VALUE11)).negate()),
                        COMMON_LABELED_BUTTON4,
                        SimpleButton.class,
                        getWidgetDescription(LABELED_BUTTON,
                                shouldBeLabeledBy(BUTTON_LABEL_TEXT4)
                                        .and(shouldHaveAttribute(ATTR1, VALUE3)
                                                .and(shouldHaveAttribute(ATTR2, VALUE11)).negate()))},

                {button(of(BUTTON_LABEL_TEXT8, BUTTON_LABEL_TEXT12), FIVE_SECONDS,
                        shouldHaveAttributeContains(ATTR5, "1")), CUSTOM_LABELED_BUTTON4,
                        CustomizedButton.class,
                        getWidgetDescription(CUSTOM_BUTTON,
                                shouldBeLabeledBy(BUTTON_LABEL_TEXT8, BUTTON_LABEL_TEXT12)
                                        .and(shouldHaveAttributeContains(ATTR5, "1")))},

                {button(BUTTON_LABEL_TEXT1, FIVE_SECONDS, shouldHaveAttributeContains(ATTR5, "1").negate()),
                        COMMON_LABELED_BUTTON1,
                        LabeledButton.class,
                        getWidgetDescription(LABELED_BUTTON,
                                shouldBeLabeledBy(BUTTON_LABEL_TEXT1)
                                        .and(shouldHaveAttributeContains(ATTR5, "1").negate()))},

                {button(of(BUTTON_LABEL_TEXT8, BUTTON_LABEL_TEXT12), FIVE_SECONDS,
                        shouldHaveAttributeContains(ATTR5, compile("1"))),
                        CUSTOM_LABELED_BUTTON4,
                        CustomizedButton.class,
                        getWidgetDescription(CUSTOM_BUTTON,
                                shouldBeLabeledBy(BUTTON_LABEL_TEXT8, BUTTON_LABEL_TEXT12)
                                        .and(shouldHaveAttributeContains(ATTR5, compile("1"))))},

                {button(BUTTON_LABEL_TEXT1, FIVE_SECONDS, shouldHaveAttributeContains(ATTR5, compile("1")).negate()),
                        COMMON_LABELED_BUTTON1,
                        LabeledButton.class,
                        getWidgetDescription(LABELED_BUTTON,
                                shouldBeLabeledBy(BUTTON_LABEL_TEXT1)
                                        .and(shouldHaveAttributeContains(ATTR5, compile("1")).negate()))},

                {button(BUTTON_LABEL_TEXT2, FIVE_SECONDS, shouldHaveCssValue(CSS2, CSS_VALUE4)
                        .and(shouldHaveCssValue(CSS3, CSS_VALUE8))),
                        COMMON_LABELED_BUTTON2,
                        LabeledButton.class,
                        getWidgetDescription(LABELED_BUTTON,
                                shouldBeLabeledBy(BUTTON_LABEL_TEXT2)
                                        .and(shouldHaveCssValue(CSS2, CSS_VALUE4).and(shouldHaveCssValue(CSS3, CSS_VALUE8))))},

                {button(BUTTON_LABEL_TEXT1, FIVE_SECONDS, shouldHaveCssValue(CSS2, CSS_VALUE4)
                        .and(shouldHaveCssValue(CSS3, CSS_VALUE8)).negate()),
                        COMMON_LABELED_BUTTON1,
                        LabeledButton.class,
                        getWidgetDescription(LABELED_BUTTON,
                                shouldBeLabeledBy(BUTTON_LABEL_TEXT1).and(shouldHaveCssValue(CSS2, CSS_VALUE4)
                                        .and(shouldHaveCssValue(CSS3, CSS_VALUE8)).negate()))},

                {button(of(BUTTON_LABEL_TEXT7, BUTTON_LABEL_TEXT11), FIVE_SECONDS,
                        shouldHaveCssValueContains(CSS5, "9")
                                .or(shouldHaveCssValueContains(CSS6, "11"))),
                        CUSTOM_LABELED_BUTTON3,
                        CustomizedButton.class,
                        getWidgetDescription(CUSTOM_BUTTON,
                                shouldBeLabeledBy(BUTTON_LABEL_TEXT7, BUTTON_LABEL_TEXT11).and(shouldHaveCssValueContains(CSS5, "9")
                                        .or(shouldHaveCssValueContains(CSS6, "11"))))},

                {button(BUTTON_LABEL_TEXT1, FIVE_SECONDS, shouldHaveCssValueContains(CSS5, "9")
                        .or(shouldHaveCssValueContains(CSS6, "11")).negate()),
                        COMMON_LABELED_BUTTON1,
                        LabeledButton.class,
                        getWidgetDescription(LABELED_BUTTON,
                                shouldBeLabeledBy(BUTTON_LABEL_TEXT1)
                                        .and(shouldHaveCssValueContains(CSS5, "9")
                                                .or(shouldHaveCssValueContains(CSS6, "11")).negate()))},

                {button(BUTTON_LABEL_TEXT2, FIVE_SECONDS,
                        shouldHaveCssValueContains(CSS2, compile("4"))
                                .or(shouldHaveCssValueContains(CSS3, compile("8")))),
                        COMMON_LABELED_BUTTON2,
                        LabeledButton.class,
                        getWidgetDescription(LABELED_BUTTON,
                                shouldBeLabeledBy(BUTTON_LABEL_TEXT2)
                                        .and(shouldHaveCssValueContains(CSS2, compile("4"))
                                                .or(shouldHaveCssValueContains(CSS3, compile("8")))))},

                {button(BUTTON_LABEL_TEXT6, FIVE_SECONDS,
                        shouldHaveCssValueContains(CSS2, compile("4"))
                                .or(shouldHaveCssValueContains(CSS3, compile("8"))).negate()),
                        CUSTOM_LABELED_BUTTON2,
                        CustomizedButton.class,
                        getWidgetDescription(CUSTOM_BUTTON,
                                shouldBeLabeledBy(BUTTON_LABEL_TEXT6).and(shouldHaveCssValueContains(CSS2, compile("4"))
                                        .or(shouldHaveCssValueContains(CSS3, compile("8"))).negate()))},

                {flag(), COMMON_CHECKBOX1,
                        SimpleCheckbox.class, SIMPLE_CHECKBOX},

                {flag(FIVE_SECONDS), COMMON_CHECKBOX1,
                        SimpleCheckbox.class, SIMPLE_CHECKBOX},

                {flag(of(RADIOBUTTON_LABEL_TEXT9)), COMMON_LABELED_RADIOBUTTON5,
                        LabeledRadioButton.class,
                        getWidgetDescription(LABELED_RADIOBUTTON, shouldBeLabeledBy(RADIOBUTTON_LABEL_TEXT9))},

                {flag(of(RADIOBUTTON_LABEL_TEXT6), FIVE_SECONDS), COMMON_LABELED_RADIOBUTTON6,
                        LabeledRadioButton.class,
                        getWidgetDescription(LABELED_RADIOBUTTON,
                                shouldBeLabeledBy(RADIOBUTTON_LABEL_TEXT6))},

                {flag(of(CHECKBOX_LABEL_TEXT7, CHECKBOX_LABEL_TEXT11)), COMMON_LABELED_CHECKBOX7,
                        LabeledCheckBox.class,
                        getWidgetDescription(LABELED_CHECKBOX,
                                shouldBeLabeledBy(CHECKBOX_LABEL_TEXT7, CHECKBOX_LABEL_TEXT11))},

                {flag(of(CHECKBOX_LABEL_TEXT7, CHECKBOX_LABEL_TEXT11), FIVE_SECONDS), COMMON_LABELED_CHECKBOX7,
                        LabeledCheckBox.class,
                        getWidgetDescription(LABELED_CHECKBOX,
                                shouldBeLabeledBy(CHECKBOX_LABEL_TEXT7, CHECKBOX_LABEL_TEXT11))},

                {flag(CHECKBOX_LABEL_TEXT4), COMMON_LABELED_CHECKBOX4, LabeledCheckBox.class,
                        getWidgetDescription(LABELED_CHECKBOX,
                                shouldBeLabeledBy(CHECKBOX_LABEL_TEXT4))},

                {flag(CHECKBOX_LABEL_TEXT5, FIVE_SECONDS), COMMON_LABELED_CHECKBOX5,
                        LabeledCheckBox.class,
                        getWidgetDescription(LABELED_CHECKBOX,
                                shouldBeLabeledBy(CHECKBOX_LABEL_TEXT5))},

                {flag(CHECKBOX_LABEL_TEXT3,
                        shouldBeEnabled()),
                        COMMON_LABELED_CHECKBOX3,
                        LabeledCheckBox.class,
                        getWidgetDescription(LABELED_CHECKBOX,
                                shouldBeLabeledBy(CHECKBOX_LABEL_TEXT3).and(shouldBeEnabled()))},

                {flag(CHECKBOX_LABEL_TEXT2,
                        shouldBeEnabled().negate()),
                        COMMON_LABELED_CHECKBOX2,
                        LabeledCheckBox.class,
                        getWidgetDescription(LABELED_CHECKBOX,
                                shouldBeLabeledBy(CHECKBOX_LABEL_TEXT2).and(shouldBeEnabled().negate()))},

                {flag(CHECKBOX_LABEL_TEXT4, shouldBeVisible()),
                        COMMON_LABELED_CHECKBOX4,
                        LabeledCheckBox.class,
                        getWidgetDescription(LABELED_CHECKBOX,
                                shouldBeLabeledBy(CHECKBOX_LABEL_TEXT4).and(shouldBeVisible()))},

                {flag(of(CHECKBOX_LABEL_TEXT6, CHECKBOX_LABEL_TEXT10),
                        shouldBeVisible().negate()),
                        COMMON_LABELED_CHECKBOX6,
                        LabeledCheckBox.class,
                        getWidgetDescription(LABELED_CHECKBOX,
                                shouldBeLabeledBy(CHECKBOX_LABEL_TEXT6, CHECKBOX_LABEL_TEXT10)
                                        .and(shouldBeVisible().negate()))},

                {flag(CHECKBOX_LABEL_TEXT5, shouldContainElements(webElements(tagName(LABEL_TAG), ofMillis(5)), 2)),
                        COMMON_LABELED_CHECKBOX5,
                        LabeledCheckBox.class,
                        getWidgetDescription(LABELED_CHECKBOX,
                                shouldBeLabeledBy(CHECKBOX_LABEL_TEXT5)
                                        .and(shouldContainElements(webElements(tagName(LABEL_TAG), ofMillis(5)), 2)))},

                {flag(of(CHECKBOX_LABEL_TEXT1),
                        shouldContainElements(webElements(tagName(LABEL_TAG), ofMillis(5)), 2)
                                .negate()),
                        COMMON_LABELED_CHECKBOX1,
                        LabeledCheckBox.class,
                        getWidgetDescription(LABELED_CHECKBOX,
                                shouldBeLabeledBy(CHECKBOX_LABEL_TEXT1)
                                        .and(shouldContainElements(webElements(tagName(LABEL_TAG), ofMillis(5)), 2)
                                                .negate()))},

                {flag(of(RADIOBUTTON_LABEL_TEXT3),
                        shouldHaveAttribute(ATTR14, VALUE14)
                                .and(shouldHaveAttribute(ATTR15, VALUE15))),
                        COMMON_LABELED_RADIOBUTTON3,
                        LabeledRadioButton.class,
                        getWidgetDescription(LABELED_RADIOBUTTON,
                                shouldBeLabeledBy(RADIOBUTTON_LABEL_TEXT3)
                                        .and(shouldHaveAttribute(ATTR14, VALUE14)
                                                .and(shouldHaveAttribute(ATTR15, VALUE15))))},

                {flag(RADIOBUTTON_LABEL_TEXT4,
                        shouldHaveAttribute(ATTR14, VALUE14)
                                .and(shouldHaveAttribute(ATTR15, VALUE15)).negate()),
                        COMMON_LABELED_RADIOBUTTON4,
                        LabeledRadioButton.class,
                        getWidgetDescription(LABELED_RADIOBUTTON,
                                shouldBeLabeledBy(RADIOBUTTON_LABEL_TEXT4)
                                        .and(shouldHaveAttribute(ATTR14, VALUE14)
                                                .and(shouldHaveAttribute(ATTR15, VALUE15)).negate()))},

                {flag(of(RADIOBUTTON_LABEL_TEXT7, RADIOBUTTON_LABEL_TEXT11),
                        shouldHaveAttributeContains(ATTR14, "18")
                                .and(shouldHaveAttributeContains(ATTR15, "19"))),
                        COMMON_LABELED_RADIOBUTTON7,
                        LabeledRadioButton.class,
                        getWidgetDescription(LABELED_RADIOBUTTON,
                                shouldBeLabeledBy(RADIOBUTTON_LABEL_TEXT7, RADIOBUTTON_LABEL_TEXT11)
                                        .and(shouldHaveAttributeContains(ATTR14, "18")
                                                .and(shouldHaveAttributeContains(ATTR15, "19"))))},

                {flag(of(RADIOBUTTON_LABEL_TEXT6, RADIOBUTTON_LABEL_TEXT10),
                        shouldHaveAttributeContains(ATTR14, "18")
                                .and(shouldHaveAttributeContains(ATTR15, "19")).negate()),
                        COMMON_LABELED_RADIOBUTTON6,
                        LabeledRadioButton.class,
                        getWidgetDescription(LABELED_RADIOBUTTON,
                                shouldBeLabeledBy(RADIOBUTTON_LABEL_TEXT6, RADIOBUTTON_LABEL_TEXT10)
                                        .and(shouldHaveAttributeContains(ATTR14, "18")
                                                .and(shouldHaveAttributeContains(ATTR15, "19")).negate()))},

                {flag(of(RADIOBUTTON_LABEL_TEXT7, RADIOBUTTON_LABEL_TEXT11),
                        shouldHaveAttributeContains(ATTR14, compile("18"))
                                .and(shouldHaveAttributeContains(ATTR15, compile("19")))),
                        COMMON_LABELED_RADIOBUTTON7,
                        LabeledRadioButton.class,
                        getWidgetDescription(LABELED_RADIOBUTTON,
                                shouldBeLabeledBy(RADIOBUTTON_LABEL_TEXT7, RADIOBUTTON_LABEL_TEXT11)
                                        .and(shouldHaveAttributeContains(ATTR14, compile("18"))
                                                .and(shouldHaveAttributeContains(ATTR15, compile("19")))))},

                {flag(of(RADIOBUTTON_LABEL_TEXT6, RADIOBUTTON_LABEL_TEXT10),
                        shouldHaveAttributeContains(ATTR14, compile("18"))
                                .and(shouldHaveAttributeContains(ATTR15, compile("19"))).negate()),
                        COMMON_LABELED_RADIOBUTTON6,
                        LabeledRadioButton.class,
                        getWidgetDescription(LABELED_RADIOBUTTON,
                                shouldBeLabeledBy(RADIOBUTTON_LABEL_TEXT6, RADIOBUTTON_LABEL_TEXT10)
                                        .and(shouldHaveAttributeContains(ATTR14, compile("18"))
                                                .and(shouldHaveAttributeContains(ATTR15, compile("19"))).negate()))},

                {flag(RADIOBUTTON_LABEL_TEXT2, shouldHaveCssValue(CSS18, CSS_VALUE13)
                        .and(shouldHaveCssValue(CSS19, CSS_VALUE14))),
                        COMMON_LABELED_RADIOBUTTON2,
                        LabeledRadioButton.class,
                        getWidgetDescription(LABELED_RADIOBUTTON,
                                shouldBeLabeledBy(RADIOBUTTON_LABEL_TEXT2)
                                        .and(shouldHaveCssValue(CSS18, CSS_VALUE13)
                                                .and(shouldHaveCssValue(CSS19, CSS_VALUE14))))},

                {flag(CHECKBOX_LABEL_TEXT9, shouldHaveCssValue(ATTR14, VALUE13)
                        .and(shouldHaveCssValue(ATTR15, VALUE14)).negate()),
                        COMMON_LABELED_CHECKBOX5,
                        LabeledCheckBox.class,
                        getWidgetDescription(LABELED_CHECKBOX,
                                shouldBeLabeledBy(CHECKBOX_LABEL_TEXT9).and(shouldHaveCssValue(ATTR14, VALUE13)
                                        .and(shouldHaveCssValue(ATTR15, VALUE14)).negate()))},

                {flag(of(CHECKBOX_LABEL_TEXT8, CHECKBOX_LABEL_TEXT12),
                        shouldHaveCssValueContains(CSS2, CSS_VALUE5)
                                .or(shouldHaveCssValueContains(CSS3, CSS_VALUE8))),
                        COMMON_LABELED_CHECKBOX8,
                        LabeledCheckBox.class,
                        getWidgetDescription(LABELED_CHECKBOX,
                                shouldBeLabeledBy(CHECKBOX_LABEL_TEXT8, CHECKBOX_LABEL_TEXT12)
                                        .and(shouldHaveCssValueContains(CSS2, CSS_VALUE5)
                                                .or(shouldHaveCssValueContains(CSS3, CSS_VALUE8))))},

                {flag(of(CHECKBOX_LABEL_TEXT1),
                        shouldHaveCssValueContains(CSS2, CSS_VALUE5)
                                .or(shouldHaveCssValueContains(CSS3, CSS_VALUE8)).negate()),
                        COMMON_LABELED_CHECKBOX1,
                        LabeledCheckBox.class,
                        getWidgetDescription(LABELED_CHECKBOX,
                                shouldBeLabeledBy(CHECKBOX_LABEL_TEXT1)
                                        .and(shouldHaveCssValueContains(CSS2, CSS_VALUE5)
                                                .or(shouldHaveCssValueContains(CSS3, CSS_VALUE8)).negate()))},

                {flag(of(CHECKBOX_LABEL_TEXT8, CHECKBOX_LABEL_TEXT12),
                        shouldHaveCssValueContains(CSS2, compile(CSS_VALUE5))
                                .or(shouldHaveCssValueContains(CSS3, compile(CSS_VALUE8)))),
                        COMMON_LABELED_CHECKBOX8,
                        LabeledCheckBox.class,
                        getWidgetDescription(LABELED_CHECKBOX,
                                shouldBeLabeledBy(CHECKBOX_LABEL_TEXT8, CHECKBOX_LABEL_TEXT12)
                                        .and(shouldHaveCssValueContains(CSS2, compile(CSS_VALUE5))
                                                .or(shouldHaveCssValueContains(CSS3, compile(CSS_VALUE8)))))},

                {flag(of(CHECKBOX_LABEL_TEXT1),
                        shouldHaveCssValueContains(CSS2, compile(CSS_VALUE5))
                                .or(shouldHaveCssValueContains(CSS3, compile(CSS_VALUE8))).negate()),
                        COMMON_LABELED_CHECKBOX1,
                        LabeledCheckBox.class,
                        getWidgetDescription(LABELED_CHECKBOX,
                                shouldBeLabeledBy(CHECKBOX_LABEL_TEXT1)
                                        .and(shouldHaveCssValueContains(CSS2, compile(CSS_VALUE5))
                                                .or(shouldHaveCssValueContains(CSS3, compile(CSS_VALUE8))).negate()))},

                {flag(CHECKBOX_LABEL_TEXT3, FIVE_SECONDS,
                        shouldBeEnabled()),
                        COMMON_LABELED_CHECKBOX3,
                        LabeledCheckBox.class,
                        getWidgetDescription(LABELED_CHECKBOX,
                                shouldBeLabeledBy(CHECKBOX_LABEL_TEXT3).and(shouldBeEnabled()))},

                {flag(CHECKBOX_LABEL_TEXT2, FIVE_SECONDS,
                        shouldBeEnabled().negate()),
                        COMMON_LABELED_CHECKBOX2,
                        LabeledCheckBox.class,
                        getWidgetDescription(LABELED_CHECKBOX,
                                shouldBeLabeledBy(CHECKBOX_LABEL_TEXT2).and(shouldBeEnabled().negate()))},

                {flag(CHECKBOX_LABEL_TEXT4, FIVE_SECONDS, shouldBeVisible()),
                        COMMON_LABELED_CHECKBOX4,
                        LabeledCheckBox.class,
                        getWidgetDescription(LABELED_CHECKBOX,
                                shouldBeLabeledBy(CHECKBOX_LABEL_TEXT4).and(shouldBeVisible()))},

                {flag(of(CHECKBOX_LABEL_TEXT6, CHECKBOX_LABEL_TEXT10), FIVE_SECONDS,
                        shouldBeVisible().negate()),
                        COMMON_LABELED_CHECKBOX6,
                        LabeledCheckBox.class,
                        getWidgetDescription(LABELED_CHECKBOX,
                                shouldBeLabeledBy(CHECKBOX_LABEL_TEXT6, CHECKBOX_LABEL_TEXT10)
                                        .and(shouldBeVisible().negate()))},

                {flag(CHECKBOX_LABEL_TEXT5, FIVE_SECONDS, shouldContainElements(webElements(tagName(LABEL_TAG), ofMillis(5)), 2)),
                        COMMON_LABELED_CHECKBOX5,
                        LabeledCheckBox.class,
                        getWidgetDescription(LABELED_CHECKBOX,
                                shouldBeLabeledBy(CHECKBOX_LABEL_TEXT5)
                                        .and(shouldContainElements(webElements(tagName(LABEL_TAG), ofMillis(5)), 2)))},

                {flag(of(CHECKBOX_LABEL_TEXT1), FIVE_SECONDS,
                        shouldContainElements(webElements(tagName(LABEL_TAG), ofMillis(5)), 2)
                                .negate()),
                        COMMON_LABELED_CHECKBOX1,
                        LabeledCheckBox.class,
                        getWidgetDescription(LABELED_CHECKBOX,
                                shouldBeLabeledBy(CHECKBOX_LABEL_TEXT1)
                                        .and(shouldContainElements(webElements(tagName(LABEL_TAG), ofMillis(5)), 2)
                                                .negate()))},

                {flag(of(RADIOBUTTON_LABEL_TEXT3), FIVE_SECONDS,
                        shouldHaveAttribute(ATTR14, VALUE14)
                                .and(shouldHaveAttribute(ATTR15, VALUE15))),
                        COMMON_LABELED_RADIOBUTTON3,
                        LabeledRadioButton.class,
                        getWidgetDescription(LABELED_RADIOBUTTON,
                                shouldBeLabeledBy(RADIOBUTTON_LABEL_TEXT3)
                                        .and(shouldHaveAttribute(ATTR14, VALUE14)
                                                .and(shouldHaveAttribute(ATTR15, VALUE15))))},

                {flag(RADIOBUTTON_LABEL_TEXT4, FIVE_SECONDS,
                        shouldHaveAttribute(ATTR14, VALUE14)
                                .and(shouldHaveAttribute(ATTR15, VALUE15)).negate()),
                        COMMON_LABELED_RADIOBUTTON4,
                        LabeledRadioButton.class,
                        getWidgetDescription(LABELED_RADIOBUTTON,
                                shouldBeLabeledBy(RADIOBUTTON_LABEL_TEXT4)
                                        .and(shouldHaveAttribute(ATTR14, VALUE14)
                                                .and(shouldHaveAttribute(ATTR15, VALUE15)).negate()))},

                {flag(of(RADIOBUTTON_LABEL_TEXT7, RADIOBUTTON_LABEL_TEXT11), FIVE_SECONDS,
                        shouldHaveAttributeContains(ATTR14, "18")
                                .and(shouldHaveAttributeContains(ATTR15, "19"))),
                        COMMON_LABELED_RADIOBUTTON7,
                        LabeledRadioButton.class,
                        getWidgetDescription(LABELED_RADIOBUTTON,
                                shouldBeLabeledBy(RADIOBUTTON_LABEL_TEXT7, RADIOBUTTON_LABEL_TEXT11)
                                        .and(shouldHaveAttributeContains(ATTR14, "18")
                                                .and(shouldHaveAttributeContains(ATTR15, "19"))))},

                {flag(of(RADIOBUTTON_LABEL_TEXT6, RADIOBUTTON_LABEL_TEXT10), FIVE_SECONDS,
                        shouldHaveAttributeContains(ATTR14, "18")
                                .and(shouldHaveAttributeContains(ATTR15, "19")).negate()),
                        COMMON_LABELED_RADIOBUTTON6,
                        LabeledRadioButton.class,
                        getWidgetDescription(LABELED_RADIOBUTTON,
                                shouldBeLabeledBy(RADIOBUTTON_LABEL_TEXT6, RADIOBUTTON_LABEL_TEXT10)
                                        .and(shouldHaveAttributeContains(ATTR14, "18")
                                                .and(shouldHaveAttributeContains(ATTR15, "19")).negate()))},

                {flag(of(RADIOBUTTON_LABEL_TEXT7, RADIOBUTTON_LABEL_TEXT11), FIVE_SECONDS,
                        shouldHaveAttributeContains(ATTR14, compile("18"))
                                .and(shouldHaveAttributeContains(ATTR15, compile("19")))),
                        COMMON_LABELED_RADIOBUTTON7,
                        LabeledRadioButton.class,
                        getWidgetDescription(LABELED_RADIOBUTTON,
                                shouldBeLabeledBy(RADIOBUTTON_LABEL_TEXT7, RADIOBUTTON_LABEL_TEXT11)
                                        .and(shouldHaveAttributeContains(ATTR14, compile("18"))
                                                .and(shouldHaveAttributeContains(ATTR15, compile("19")))))},

                {flag(of(RADIOBUTTON_LABEL_TEXT6, RADIOBUTTON_LABEL_TEXT10), FIVE_SECONDS,
                        shouldHaveAttributeContains(ATTR14, compile("18"))
                                .and(shouldHaveAttributeContains(ATTR15, compile("19"))).negate()),
                        COMMON_LABELED_RADIOBUTTON6,
                        LabeledRadioButton.class,
                        getWidgetDescription(LABELED_RADIOBUTTON,
                                shouldBeLabeledBy(RADIOBUTTON_LABEL_TEXT6, RADIOBUTTON_LABEL_TEXT10)
                                        .and(shouldHaveAttributeContains(ATTR14, compile("18"))
                                                .and(shouldHaveAttributeContains(ATTR15, compile("19"))).negate()))},

                {flag(RADIOBUTTON_LABEL_TEXT2, FIVE_SECONDS, shouldHaveCssValue(CSS18, CSS_VALUE13)
                        .and(shouldHaveCssValue(CSS19, CSS_VALUE14))),
                        COMMON_LABELED_RADIOBUTTON2,
                        LabeledRadioButton.class,
                        getWidgetDescription(LABELED_RADIOBUTTON,
                                shouldBeLabeledBy(RADIOBUTTON_LABEL_TEXT2)
                                        .and(shouldHaveCssValue(CSS18, CSS_VALUE13)
                                                .and(shouldHaveCssValue(CSS19, CSS_VALUE14))))},

                {flag(CHECKBOX_LABEL_TEXT9, FIVE_SECONDS, shouldHaveCssValue(ATTR14, VALUE13)
                        .and(shouldHaveCssValue(ATTR15, VALUE14)).negate()),
                        COMMON_LABELED_CHECKBOX5,
                        LabeledCheckBox.class,
                        getWidgetDescription(LABELED_CHECKBOX,
                                shouldBeLabeledBy(CHECKBOX_LABEL_TEXT9).and(shouldHaveCssValue(ATTR14, VALUE13)
                                        .and(shouldHaveCssValue(ATTR15, VALUE14)).negate()))},

                {flag(of(CHECKBOX_LABEL_TEXT8, CHECKBOX_LABEL_TEXT12), FIVE_SECONDS,
                        shouldHaveCssValueContains(CSS2, CSS_VALUE5)
                                .or(shouldHaveCssValueContains(CSS3, CSS_VALUE8))),
                        COMMON_LABELED_CHECKBOX8,
                        LabeledCheckBox.class,
                        getWidgetDescription(LABELED_CHECKBOX,
                                shouldBeLabeledBy(CHECKBOX_LABEL_TEXT8, CHECKBOX_LABEL_TEXT12)
                                        .and(shouldHaveCssValueContains(CSS2, CSS_VALUE5)
                                                .or(shouldHaveCssValueContains(CSS3, CSS_VALUE8))))},

                {flag(of(CHECKBOX_LABEL_TEXT1), FIVE_SECONDS,
                        shouldHaveCssValueContains(CSS2, CSS_VALUE5)
                                .or(shouldHaveCssValueContains(CSS3, CSS_VALUE8)).negate()),
                        COMMON_LABELED_CHECKBOX1,
                        LabeledCheckBox.class,
                        getWidgetDescription(LABELED_CHECKBOX,
                                shouldBeLabeledBy(CHECKBOX_LABEL_TEXT1)
                                        .and(shouldHaveCssValueContains(CSS2, CSS_VALUE5)
                                                .or(shouldHaveCssValueContains(CSS3, CSS_VALUE8)).negate()))},

                {flag(of(CHECKBOX_LABEL_TEXT8, CHECKBOX_LABEL_TEXT12), FIVE_SECONDS,
                        shouldHaveCssValueContains(CSS2, compile(CSS_VALUE5))
                                .or(shouldHaveCssValueContains(CSS3, compile(CSS_VALUE8)))),
                        COMMON_LABELED_CHECKBOX8,
                        LabeledCheckBox.class,
                        getWidgetDescription(LABELED_CHECKBOX,
                                shouldBeLabeledBy(CHECKBOX_LABEL_TEXT8, CHECKBOX_LABEL_TEXT12)
                                        .and(shouldHaveCssValueContains(CSS2, compile(CSS_VALUE5))
                                                .or(shouldHaveCssValueContains(CSS3, compile(CSS_VALUE8)))))},

                {flag(of(CHECKBOX_LABEL_TEXT1), FIVE_SECONDS,
                        shouldHaveCssValueContains(CSS2, compile(CSS_VALUE5))
                                .or(shouldHaveCssValueContains(CSS3, compile(CSS_VALUE8))).negate()),
                        COMMON_LABELED_CHECKBOX1,
                        LabeledCheckBox.class,
                        getWidgetDescription(LABELED_CHECKBOX,
                                shouldBeLabeledBy(CHECKBOX_LABEL_TEXT1)
                                        .and(shouldHaveCssValueContains(CSS2, compile(CSS_VALUE5))
                                                .or(shouldHaveCssValueContains(CSS3, compile(CSS_VALUE8))).negate()))},

                {checkbox(), COMMON_CHECKBOX1,
                        SimpleCheckbox.class, SIMPLE_CHECKBOX},

                {checkbox(FIVE_SECONDS), COMMON_CHECKBOX1,
                        SimpleCheckbox.class, SIMPLE_CHECKBOX},

                {checkbox(of(CHECKBOX_LABEL_TEXT6)), COMMON_LABELED_CHECKBOX6,
                        LabeledCheckBox.class,
                        getWidgetDescription(LABELED_CHECKBOX,
                                shouldBeLabeledBy(CHECKBOX_LABEL_TEXT6))},

                {checkbox(of(CHECKBOX_LABEL_TEXT6), FIVE_SECONDS), COMMON_LABELED_CHECKBOX6,
                        LabeledCheckBox.class,
                        getWidgetDescription(LABELED_CHECKBOX,
                                shouldBeLabeledBy(CHECKBOX_LABEL_TEXT6))},

                {checkbox(of(CHECKBOX_LABEL_TEXT12, CHECKBOX_LABEL_TEXT8)), COMMON_LABELED_CHECKBOX8,
                        LabeledCheckBox.class,
                        getWidgetDescription(LABELED_CHECKBOX,
                                shouldBeLabeledBy(CHECKBOX_LABEL_TEXT12, CHECKBOX_LABEL_TEXT8))},

                {checkbox(of(CHECKBOX_LABEL_TEXT12, CHECKBOX_LABEL_TEXT8), FIVE_SECONDS),
                        COMMON_LABELED_CHECKBOX8, LabeledCheckBox.class,
                        getWidgetDescription(LABELED_CHECKBOX,
                                shouldBeLabeledBy(CHECKBOX_LABEL_TEXT12, CHECKBOX_LABEL_TEXT8))},

                {checkbox(CHECKBOX_LABEL_TEXT5), COMMON_LABELED_CHECKBOX5,
                        LabeledCheckBox.class,
                        getWidgetDescription(LABELED_CHECKBOX,
                                shouldBeLabeledBy(CHECKBOX_LABEL_TEXT5))},

                {checkbox(CHECKBOX_LABEL_TEXT5, FIVE_SECONDS), COMMON_LABELED_CHECKBOX5,
                        LabeledCheckBox.class,
                        getWidgetDescription(LABELED_CHECKBOX,
                                shouldBeLabeledBy(CHECKBOX_LABEL_TEXT5))},



                {radioButton(), COMMON_RADIOBUTTON1,
                        SimpleRadioButton.class, SIMPLE_RADIOBUTTON},

                {radioButton(FIVE_SECONDS), COMMON_RADIOBUTTON1,
                        SimpleRadioButton.class, SIMPLE_RADIOBUTTON},

                {radioButton(of(RADIOBUTTON_LABEL_TEXT1)), COMMON_LABELED_RADIOBUTTON1,
                        LabeledRadioButton.class,
                        getWidgetDescription(LABELED_RADIOBUTTON,
                                shouldBeLabeledBy(RADIOBUTTON_LABEL_TEXT1))},

                {radioButton(of(RADIOBUTTON_LABEL_TEXT6), FIVE_SECONDS), COMMON_LABELED_RADIOBUTTON6,
                        LabeledRadioButton.class,
                        getWidgetDescription(LABELED_RADIOBUTTON,
                                shouldBeLabeledBy(RADIOBUTTON_LABEL_TEXT6))},

                {radioButton(of(RADIOBUTTON_LABEL_TEXT12, RADIOBUTTON_LABEL_TEXT8)), COMMON_LABELED_RADIOBUTTON8,
                        LabeledRadioButton.class,
                        getWidgetDescription(LABELED_RADIOBUTTON,
                                shouldBeLabeledBy(RADIOBUTTON_LABEL_TEXT12, RADIOBUTTON_LABEL_TEXT8))},

                {radioButton(of(RADIOBUTTON_LABEL_TEXT11, RADIOBUTTON_LABEL_TEXT7), FIVE_SECONDS),
                        COMMON_LABELED_RADIOBUTTON7, LabeledRadioButton.class,
                        getWidgetDescription(LABELED_RADIOBUTTON,
                                shouldBeLabeledBy(RADIOBUTTON_LABEL_TEXT11, RADIOBUTTON_LABEL_TEXT7))},

                {radioButton(RADIOBUTTON_LABEL_TEXT5), COMMON_LABELED_RADIOBUTTON5,
                        LabeledRadioButton.class,
                        getWidgetDescription(LABELED_RADIOBUTTON,
                                shouldBeLabeledBy(RADIOBUTTON_LABEL_TEXT5))},

                {radioButton(RADIOBUTTON_LABEL_TEXT6, FIVE_SECONDS), COMMON_LABELED_RADIOBUTTON6,
                        LabeledRadioButton.class,
                        getWidgetDescription(LABELED_RADIOBUTTON,
                                shouldBeLabeledBy(RADIOBUTTON_LABEL_TEXT6))},


                {link(), COMMON_LABELED_LINK1,
                        LabeledLink.class, LABELED_LINK},

                {link(FIVE_SECONDS), COMMON_LABELED_LINK1,
                        LabeledLink.class, LABELED_LINK},

                {link(of(LINK_LABEL_TEXT4)), COMMON_LABELED_LINK4,
                        LabeledLink.class,
                        getWidgetDescription(LABELED_LINK,
                                shouldBeLabeledBy(LINK_LABEL_TEXT4))},

                {link(of(LINK_LABEL_TEXT4), FIVE_SECONDS), COMMON_LABELED_LINK4,
                        LabeledLink.class,
                        getWidgetDescription(LABELED_LINK,
                                shouldBeLabeledBy(LINK_LABEL_TEXT4))},

                {link(of(LINK_LABEL_TEXT6, LINK_LABEL_TEXT10)), CUSTOM_LABELED_LINK2,
                        CustomizedLink.class,
                        getWidgetDescription(CUSTOM_LINK,
                                shouldBeLabeledBy(LINK_LABEL_TEXT6, LINK_LABEL_TEXT10))},

                {link(of(LINK_LABEL_TEXT8, LINK_LABEL_TEXT12), FIVE_SECONDS),
                        CUSTOM_LABELED_LINK4,
                        CustomizedLink.class,
                        getWidgetDescription(CUSTOM_LINK,
                                shouldBeLabeledBy(LINK_LABEL_TEXT8, LINK_LABEL_TEXT12))},

                {link(LINK_LABEL_TEXT3), COMMON_LABELED_LINK3, LabeledLink.class,
                        getWidgetDescription(LABELED_LINK, shouldBeLabeledBy(LINK_LABEL_TEXT3))},

                {link(LINK_LABEL_TEXT4, FIVE_SECONDS), COMMON_LABELED_LINK4,
                        LabeledLink.class,
                        getWidgetDescription(LABELED_LINK,
                                shouldBeLabeledBy(LINK_LABEL_TEXT4))},



                {select(), COMMON_LABELED_SELECT1,
                        SimpleSelect.class, SIMPLE_SELECT},

                {select(FIVE_SECONDS), COMMON_LABELED_SELECT1,
                        SimpleSelect.class, SIMPLE_SELECT},

                {select(of(SELECT_LABEL_TEXT1)), MULTI_SELECT1,
                        MultiSelect.class,
                        getWidgetDescription(MULTI_SELECT,
                                shouldBeLabeledBy(SELECT_LABEL_TEXT1))},

                {select(of(SELECT_LABEL_TEXT1), FIVE_SECONDS), MULTI_SELECT1,
                        MultiSelect.class,
                        getWidgetDescription(MULTI_SELECT,
                                shouldBeLabeledBy(SELECT_LABEL_TEXT1))},

                {select(of(SELECT_LABEL_TEXT7, SELECT_LABEL_TEXT11)), MULTI_SELECT3,
                        MultiSelect.class,
                        getWidgetDescription(MULTI_SELECT,
                                shouldBeLabeledBy(SELECT_LABEL_TEXT7, SELECT_LABEL_TEXT11))},

                {select(of(SELECT_LABEL_TEXT7, SELECT_LABEL_TEXT11), FIVE_SECONDS), MULTI_SELECT3,
                        MultiSelect.class,
                        getWidgetDescription(MULTI_SELECT,
                                shouldBeLabeledBy(SELECT_LABEL_TEXT7, SELECT_LABEL_TEXT11))},

                {select(SELECT_LABEL_TEXT3), COMMON_LABELED_SELECT3, LabeledSelect.class,
                        getWidgetDescription(LABELED_SELECT,
                                shouldBeLabeledBy(SELECT_LABEL_TEXT3))},

                {select(SELECT_LABEL_TEXT3, FIVE_SECONDS), COMMON_LABELED_SELECT3, LabeledSelect.class,
                        getWidgetDescription(LABELED_SELECT,
                                shouldBeLabeledBy(SELECT_LABEL_TEXT3))},



                {tab(), COMMON_LABELED_TAB1,
                        SimpleTab.class, SIMPLE_TAB},

                {tab(FIVE_SECONDS), COMMON_LABELED_TAB1,
                        SimpleTab.class, SIMPLE_TAB},

                {tab(of(TAB_TEXT7)), COMMON_LABELED_TAB3,
                        LabeledTab.class,
                        getWidgetDescription(LABELED_TAB,
                                shouldBeLabeledBy(TAB_TEXT7))},

                {tab(of(TAB_TEXT7), FIVE_SECONDS), COMMON_LABELED_TAB3,
                        LabeledTab.class,
                        getWidgetDescription(LABELED_TAB,
                                shouldBeLabeledBy(TAB_TEXT7))},

                {tab(of(TAB_TEXT11, TAB_TEXT15)), CUSTOM_LABELED_TAB3,
                        CustomizedTab.class,
                        getWidgetDescription(CUSTOMIZED_TAB,
                                shouldBeLabeledBy(TAB_TEXT11, TAB_TEXT15))},

                {tab(of(TAB_TEXT12, TAB_TEXT16), FIVE_SECONDS), CUSTOM_LABELED_TAB4,
                        CustomizedTab.class,
                        getWidgetDescription(CUSTOMIZED_TAB,
                                shouldBeLabeledBy(TAB_TEXT12, TAB_TEXT16))},

                {tab(TAB_TEXT13), CUSTOM_LABELED_TAB1, CustomizedTab.class,
                        getWidgetDescription(CUSTOMIZED_TAB,
                                shouldBeLabeledBy(TAB_TEXT13))},

                {tab(TAB_TEXT14, FIVE_SECONDS), CUSTOM_LABELED_TAB2, CustomizedTab.class,
                        getWidgetDescription(CUSTOMIZED_TAB,
                                shouldBeLabeledBy(TAB_TEXT14))},


                {table(), COMMON_LABELED_TABLE1,
                        SimpleTable.class, SIMPLE_TABLE},

                {table(FIVE_SECONDS), COMMON_LABELED_TABLE1,
                        SimpleTable.class, SIMPLE_TABLE},

                {table(of(TABLE_LABEL_TEXT9)), SPREAD_SHEET_TABLE1,
                        SpreadsheetTable.class,
                        getWidgetDescription(SPREADSHEET_TABLE,
                                shouldBeLabeledBy(TABLE_LABEL_TEXT9))},

                {table(of(TABLE_LABEL_TEXT10), FIVE_SECONDS), SPREAD_SHEET_TABLE2,
                        SpreadsheetTable.class,
                        getWidgetDescription(SPREADSHEET_TABLE,
                                shouldBeLabeledBy(TABLE_LABEL_TEXT10))},

                {table(of(TABLE_LABEL_TEXT6, TABLE_LABEL_TEXT10)), SPREAD_SHEET_TABLE2,
                        SpreadsheetTable.class,
                        getWidgetDescription(SPREADSHEET_TABLE,
                                shouldBeLabeledBy(TABLE_LABEL_TEXT6, TABLE_LABEL_TEXT10))},

                {table(of(TABLE_LABEL_TEXT6, TABLE_LABEL_TEXT10), FIVE_SECONDS), SPREAD_SHEET_TABLE2,
                        SpreadsheetTable.class,
                        getWidgetDescription(SPREADSHEET_TABLE,
                                shouldBeLabeledBy(TABLE_LABEL_TEXT6, TABLE_LABEL_TEXT10))},

                {table(TABLE_LABEL_TEXT2), COMMON_LABELED_TABLE2, LabeledTable.class,
                        getWidgetDescription(LABELED_TABLE,
                                shouldBeLabeledBy(TABLE_LABEL_TEXT2))},

                {table(TABLE_LABEL_TEXT3, FIVE_SECONDS), COMMON_LABELED_TABLE3, LabeledTable.class,
                        getWidgetDescription(LABELED_TABLE,
                                shouldBeLabeledBy(TABLE_LABEL_TEXT3))},


                {button()
                        .foundFrom(tableCell(condition("Cell should be equal to " + CELL_TEXT73,
                                tableCell -> CELL_TEXT73.equals(tableCell.getValue()))))

                        .foundFrom(tableRow(condition("Row should have tag " + DIV,
                                tableRow -> DIV.equals(tableRow.getWrappedElement().getTagName()))))

                        .foundFrom(table(TABLE_LABEL_TEXT5)),

                        CUSTOM_LABELED_BUTTON1,
                        CustomizedButton.class, CUSTOM_BUTTON},

                {button(FIVE_SECONDS)
                        .foundFrom(tableRow())
                                .foundFrom(table(TABLE_LABEL_TEXT6)),

                        CUSTOM_LABELED_BUTTON2,
                        CustomizedButton.class, CUSTOM_BUTTON},

                {button(of(BUTTON_LABEL_TEXT5))
                        .foundFrom(tableCell(condition("Cell should be equal to " + CELL_TEXT73,
                                tableCell -> CELL_TEXT73.equals(tableCell.getValue()))))

                        .foundFrom(tableRow(condition("Row should have tag " + DIV,
                                tableRow -> DIV.equals(tableRow.getWrappedElement().getTagName()))))

                        .foundFrom(table(TABLE_LABEL_TEXT5)),

                        CUSTOM_LABELED_BUTTON1,
                        CustomizedButton.class,
                        getWidgetDescription(CUSTOM_BUTTON,
                                shouldBeLabeledBy(BUTTON_LABEL_TEXT5))},

                {button(of(BUTTON_LABEL_TEXT6), FIVE_SECONDS)
                        .foundFrom(tableRow())
                        .foundFrom(table(TABLE_LABEL_TEXT6)),

                        CUSTOM_LABELED_BUTTON2,
                        CustomizedButton.class,
                        getWidgetDescription(CUSTOM_BUTTON,
                                shouldBeLabeledBy(BUTTON_LABEL_TEXT6))},

                {button(of(BUTTON_LABEL_TEXT6, BUTTON_LABEL_TEXT10))
                        .foundFrom(tableRow())
                        .foundFrom(table(TABLE_LABEL_TEXT6)),

                        CUSTOM_LABELED_BUTTON2,
                        CustomizedButton.class,
                        getWidgetDescription(CUSTOM_BUTTON,
                                shouldBeLabeledBy(BUTTON_LABEL_TEXT6, BUTTON_LABEL_TEXT10))},

                {button(of(BUTTON_LABEL_TEXT5, BUTTON_LABEL_TEXT9), FIVE_SECONDS)
                        .foundFrom(tableCell(condition("Cell should be equal to " + CELL_TEXT73,
                                tableCell -> CELL_TEXT73.equals(tableCell.getValue()))))

                        .foundFrom(tableRow(condition("Row should have tag " + DIV,
                                tableRow -> DIV.equals(tableRow.getWrappedElement().getTagName()))))

                        .foundFrom(table(TABLE_LABEL_TEXT5)),

                        CUSTOM_LABELED_BUTTON1,
                        CustomizedButton.class,
                        getWidgetDescription(CUSTOM_BUTTON,
                                shouldBeLabeledBy(BUTTON_LABEL_TEXT5, BUTTON_LABEL_TEXT9))},

                {button(BUTTON_LABEL_TEXT6)
                        .foundFrom(tableRow())
                        .foundFrom(table(TABLE_LABEL_TEXT6)),

                        CUSTOM_LABELED_BUTTON2,
                        CustomizedButton.class,
                        getWidgetDescription(CUSTOM_BUTTON,
                                shouldBeLabeledBy(BUTTON_LABEL_TEXT6))},

                {button(BUTTON_LABEL_TEXT5, FIVE_SECONDS)
                        .foundFrom(tableCell(condition("Cell should be equal to " + CELL_TEXT73,
                                tableCell -> CELL_TEXT73.equals(tableCell.getValue()))))

                        .foundFrom(tableRow(condition("Row should have tag " + DIV,
                                tableRow -> DIV.equals(tableRow.getWrappedElement().getTagName()))))

                        .foundFrom(table(TABLE_LABEL_TEXT5)),

                        CUSTOM_LABELED_BUTTON1,
                        CustomizedButton.class,
                        getWidgetDescription(CUSTOM_BUTTON,
                                shouldBeLabeledBy(BUTTON_LABEL_TEXT5))},
        };
    }

    @Test(retryAnalyzer = RetryAnalyzer.class, dataProvider = "search without criteria")
    public <T extends Widget> void findWidgetTest(SearchSupplier<T> search,
                                                  WebElement element,
                                                  Class<? extends Widget> widgetClass,
                                                  String expectedDescription) {
        setStartBenchMark();
        T t = seleniumSteps.find(search);
        setEndBenchMark();
        System.out.println(t.getClass().getName());
        assertThat(widgetClass.isAssignableFrom(t.getClass()), is(true));
        ofNullable(element).ifPresent(element1 -> assertThat(t.getWrappedElement(), equalTo(element1)));
        assertThat(t.toString(), is(expectedDescription));
        assertThat(new BigDecimal(getTimeDifference()), either(lessThan(new BigDecimal(HALF_SECOND.toMillis())))
                .or(closeTo(new BigDecimal(HALF_SECOND.toMillis()), new BigDecimal(600))));
    }
}
