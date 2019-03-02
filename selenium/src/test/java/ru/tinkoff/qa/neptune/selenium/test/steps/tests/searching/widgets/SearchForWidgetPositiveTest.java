package ru.tinkoff.qa.neptune.selenium.test.steps.tests.searching.widgets;

import ru.tinkoff.qa.neptune.selenium.api.widget.Widget;
import ru.tinkoff.qa.neptune.selenium.functions.searching.SearchSupplier;
import ru.tinkoff.qa.neptune.selenium.test.BaseWebDriverTest;
import ru.tinkoff.qa.neptune.selenium.test.RetryAnalyzer;
import ru.tinkoff.qa.neptune.selenium.test.steps.tests.searching.widgets.buttons.CustomizedButton;
import ru.tinkoff.qa.neptune.selenium.test.steps.tests.searching.widgets.buttons.LabeledButton;
import ru.tinkoff.qa.neptune.selenium.test.steps.tests.searching.widgets.buttons.SimpleButton;
import org.openqa.selenium.WebElement;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.function.Predicate;

import static ru.tinkoff.qa.neptune.selenium.functions.searching.CommonConditions.*;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.CommonConditions.shouldHaveAttribute;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.MultipleSearchSupplier.webElements;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.SearchSupplier.*;
import static ru.tinkoff.qa.neptune.selenium.test.FakeDOMModel.*;
import static ru.tinkoff.qa.neptune.selenium.test.FakeDOMModel.COMMON_LABELED_BUTTON1;
import static ru.tinkoff.qa.neptune.selenium.test.steps.tests.searching.widgets.WidgetNames.*;
import static java.lang.String.format;
import static java.time.Duration.ofMillis;
import static java.util.Optional.ofNullable;
import static java.util.function.Predicate.*;
import static java.util.regex.Pattern.compile;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.openqa.selenium.By.tagName;

public class SearchForWidgetPositiveTest extends BaseWebDriverTest {

    private static String getWidgetDescription(String name, Predicate<?> condition) {
        return format("%s ['%s']", name, condition);
    }

    private static String getWidgetDescription(String name, Predicate<?> condition, String... labels) {
        return format("%s '%s' ['%s']", name, String.join(",", labels), condition);
    }

    @DataProvider(name = "search without criteria")
    public static Object[][] searchCriteria() {
        return new Object[][]{
                /*{button(), COMMON_BUTTON1,
                        SimpleButton.class, SIMPLE_BUTTON},

                {button().timeOut(FIVE_SECONDS), COMMON_BUTTON1,
                        SimpleButton.class, SIMPLE_BUTTON},*/

                {button(BUTTON_LABEL_TEXT1), COMMON_LABELED_BUTTON1,
                        LabeledButton.class,
                        format("%s '%s'", LABELED_BUTTON, BUTTON_LABEL_TEXT1)},

                {button(BUTTON_LABEL_TEXT1)
                        .timeOut(FIVE_SECONDS), COMMON_LABELED_BUTTON1,
                        LabeledButton.class,
                        format("%s '%s'", LABELED_BUTTON, BUTTON_LABEL_TEXT1)},

                {button(BUTTON_LABEL_TEXT10, BUTTON_LABEL_TEXT6), CUSTOM_LABELED_BUTTON2,
                        CustomizedButton.class,
                        format("%s '%s,%s'", CUSTOM_BUTTON, BUTTON_LABEL_TEXT10, BUTTON_LABEL_TEXT6)},


                {button().criteria(shouldBeEnabled()),
                        COMMON_BUTTON3,
                        SimpleButton.class,
                        getWidgetDescription(SIMPLE_BUTTON,
                                shouldBeEnabled())},

                {button().criteria(not(shouldBeEnabled())), COMMON_BUTTON1,
                        SimpleButton.class,
                        getWidgetDescription(SIMPLE_BUTTON,
                                not(shouldBeEnabled()))},

                {button().criteria(shouldBeVisible()), COMMON_BUTTON2,
                        SimpleButton.class,
                        getWidgetDescription(SIMPLE_BUTTON,
                                shouldBeVisible())},

                {button().criteria(not(shouldBeVisible())), COMMON_BUTTON1,
                        SimpleButton.class,
                        getWidgetDescription(SIMPLE_BUTTON,
                                not(shouldBeVisible()))},

                {button().criteria(shouldContainElements(webElements(tagName(LABEL_TAG))
                        .timeOut(ofMillis(5)), 1)),
                        COMMON_LABELED_BUTTON1,
                        SimpleButton.class,
                        getWidgetDescription(SIMPLE_BUTTON,
                                shouldContainElements(webElements(tagName(LABEL_TAG))
                                        .timeOut(ofMillis(5)), 1))},

                {button().criteria(not(shouldContainElements(webElements(tagName(LABEL_TAG))
                        .timeOut(ofMillis(5)), 1))),
                        COMMON_BUTTON1,
                        SimpleButton.class,
                        getWidgetDescription(SIMPLE_BUTTON,
                                not(shouldContainElements(webElements(tagName(LABEL_TAG))
                                        .timeOut(ofMillis(5)), 1)))},

                {button()
                        .criteria(shouldHaveAttribute(ATTR5, VALUE11))
                        .criteria(shouldHaveAttribute(ATTR6, VALUE12)),
                        CUSTOM_LABELED_BUTTON2,
                        CustomizedButton.class,
                        getWidgetDescription(CUSTOM_BUTTON,
                                shouldHaveAttribute(ATTR5, VALUE11)
                                        .and(shouldHaveAttribute(ATTR6, VALUE12)))},

                {button().criteria(shouldHaveAttribute(ATTR5, VALUE11))
                        .criteria(not(shouldHaveAttribute(ATTR6, VALUE13))),
                        CUSTOM_LABELED_BUTTON2,
                        CustomizedButton.class,
                        getWidgetDescription(CUSTOM_BUTTON,
                                shouldHaveAttribute(ATTR5, VALUE11)
                                        .and(not(shouldHaveAttribute(ATTR6, VALUE13))))},

                {button().criteria(shouldHaveAttributeContains(ATTR1, "1")
                        .or(shouldHaveAttributeContains(ATTR6, "12"))), COMMON_LABELED_BUTTON1,
                        SimpleButton.class,
                        getWidgetDescription(SIMPLE_BUTTON,
                                shouldHaveAttributeContains(ATTR1, "1")
                                        .or(shouldHaveAttributeContains(ATTR6, "12")))},

                {button().criteria(shouldHaveAttributeContains(ATTR1, "1")
                        .or(not(shouldHaveAttributeContains(ATTR6, "12")))), COMMON_BUTTON1,
                        SimpleButton.class,
                        getWidgetDescription(SIMPLE_BUTTON,
                                shouldHaveAttributeContains(ATTR1, "1")
                                        .or(not(shouldHaveAttributeContains(ATTR6, "12"))))},

                {button().criteria(shouldHaveAttributeContains(ATTR1, compile("1"))
                        .or(shouldHaveAttributeContains(ATTR6, compile("12")))), COMMON_LABELED_BUTTON1,
                        SimpleButton.class,
                        getWidgetDescription(SIMPLE_BUTTON,
                                shouldHaveAttributeContains(ATTR1, compile("1"))
                                        .or(shouldHaveAttributeContains(ATTR6, compile("12"))))},

                {button().criteria(shouldHaveAttributeContains(ATTR1, compile("1"))
                        .or(not(shouldHaveAttributeContains(ATTR6, compile("12"))))), COMMON_BUTTON1,
                        SimpleButton.class,
                        getWidgetDescription(SIMPLE_BUTTON,
                                shouldHaveAttributeContains(ATTR1, compile("1"))
                                        .or(not(shouldHaveAttributeContains(ATTR6, compile("12")))))},

                {button()
                        .criteria(shouldHaveCssValue(CSS2, CSS_VALUE4))
                        .criteria(shouldHaveCssValue(CSS3, CSS_VALUE8)),
                        COMMON_LABELED_BUTTON2,
                        SimpleButton.class,
                        getWidgetDescription(SIMPLE_BUTTON,
                                shouldHaveCssValue(CSS2, CSS_VALUE4)
                                        .and(shouldHaveCssValue(CSS3, CSS_VALUE8)))},

                {button()
                        .criteria(shouldHaveCssValue(CSS2, CSS_VALUE4))
                        .criteria(not(shouldHaveCssValue(CSS3, CSS_VALUE10))),
                        COMMON_LABELED_BUTTON2,
                        SimpleButton.class,
                        getWidgetDescription(SIMPLE_BUTTON,
                                shouldHaveCssValue(CSS2, CSS_VALUE4)
                                        .and(not(shouldHaveCssValue(CSS3, CSS_VALUE10))))},

                {button()
                        .criteria(shouldHaveCssValueContains(CSS5, "9")
                        .or(shouldHaveCssValueContains(CSS6, "11"))), CUSTOM_LABELED_BUTTON3,
                        CustomizedButton.class,
                        getWidgetDescription(CUSTOM_BUTTON,
                                shouldHaveCssValueContains(CSS5, "9")
                                        .or(shouldHaveCssValueContains(CSS6, "11")))},

                {button()
                        .criteria(not(shouldHaveCssValueContains(CSS5, "9")
                        .or(shouldHaveCssValueContains(CSS6, "11")))), COMMON_BUTTON1,
                        SimpleButton.class,
                        getWidgetDescription(SIMPLE_BUTTON,
                                not(shouldHaveCssValueContains(CSS5, "9")
                                        .or(shouldHaveCssValueContains(CSS6, "11"))))},

                {button().criteria(shouldHaveCssValueContains(CSS16, compile("18"))
                        .or(shouldHaveCssValueContains(CSS4, compile("5")))),
                        COMMON_BUTTON1,
                        SimpleButton.class,
                        getWidgetDescription(SIMPLE_BUTTON,
                                shouldHaveCssValueContains(CSS16, compile("18"))
                                        .or(shouldHaveCssValueContains(CSS4, compile("5"))))},

                {button().criteria(shouldHaveCssValueContains(CSS4, compile("5"))
                        .or(shouldHaveCssValueContains(CSS16, compile("18"))).negate()), COMMON_BUTTON3,
                        SimpleButton.class,
                        getWidgetDescription(SIMPLE_BUTTON,
                                shouldHaveCssValueContains(CSS4, compile("5"))
                                        .or(shouldHaveCssValueContains(CSS16, compile("18"))).negate())},


                {button()
                        .timeOut(FIVE_SECONDS)
                        .criteria(shouldBeEnabled()),
                        COMMON_BUTTON3,
                        SimpleButton.class,
                        getWidgetDescription(SIMPLE_BUTTON,
                                shouldBeEnabled())},

                {button()
                        .timeOut(FIVE_SECONDS)
                        .criteria(not(shouldBeEnabled())),
                        COMMON_BUTTON1,
                        SimpleButton.class,
                        getWidgetDescription(SIMPLE_BUTTON,
                                not(shouldBeEnabled()))},

                {button()
                        .timeOut(FIVE_SECONDS)
                        .criteria(shouldBeVisible()),
                        COMMON_BUTTON2,
                        SimpleButton.class,
                        getWidgetDescription(SIMPLE_BUTTON,
                                shouldBeVisible())},

                {button()
                        .timeOut(FIVE_SECONDS)
                        .criteria(not(shouldBeVisible())),
                        COMMON_BUTTON1,
                        SimpleButton.class,
                        getWidgetDescription(SIMPLE_BUTTON,
                                not(shouldBeVisible()))},

                {button()
                        .timeOut(FIVE_SECONDS)
                        .criteria(shouldContainElements(webElements(tagName(LABEL_TAG)).timeOut(ofMillis(5)), 1)),
                        COMMON_LABELED_BUTTON1,
                        SimpleButton.class,
                        getWidgetDescription(SIMPLE_BUTTON,
                                shouldContainElements(webElements(tagName(LABEL_TAG))
                                        .timeOut(ofMillis(5)), 1))},

                {button()
                        .timeOut(FIVE_SECONDS)
                        .criteria(not(shouldContainElements(webElements(tagName(LABEL_TAG)).timeOut(ofMillis(5)), 1))),
                        COMMON_BUTTON1,
                        SimpleButton.class,
                        getWidgetDescription(SIMPLE_BUTTON,
                                not(shouldContainElements(webElements(tagName(LABEL_TAG))
                                        .timeOut(ofMillis(5)), 1)))},

                {button()
                        .timeOut(FIVE_SECONDS)
                        .criteria(shouldHaveAttribute(ATTR5, VALUE11))
                        .criteria(shouldHaveAttribute(ATTR6, VALUE12)),
                        CUSTOM_LABELED_BUTTON2,
                        CustomizedButton.class,
                        getWidgetDescription(CUSTOM_BUTTON,
                                shouldHaveAttribute(ATTR5, VALUE11)
                                        .and(shouldHaveAttribute(ATTR6, VALUE12)))},

                {button()
                        .timeOut(FIVE_SECONDS)
                        .criteria(shouldHaveAttribute(ATTR5, VALUE11))
                        .criteria(not(shouldHaveAttribute(ATTR6, VALUE13))),
                        CUSTOM_LABELED_BUTTON2,
                        CustomizedButton.class,
                        getWidgetDescription(CUSTOM_BUTTON,
                                shouldHaveAttribute(ATTR5, VALUE11)
                                        .and(not(shouldHaveAttribute(ATTR6, VALUE13))))},

                {button()
                        .timeOut(FIVE_SECONDS)
                        .criteria(shouldHaveAttributeContains(ATTR1, "1").or(shouldHaveAttributeContains(ATTR6, "12"))),
                        COMMON_LABELED_BUTTON1,
                        SimpleButton.class,
                        getWidgetDescription(SIMPLE_BUTTON,
                                shouldHaveAttributeContains(ATTR1, "1")
                                        .or(shouldHaveAttributeContains(ATTR6, "12")))},

                {button()
                        .timeOut(FIVE_SECONDS)
                        .criteria(shouldHaveAttributeContains(ATTR1, "1").or(not(shouldHaveAttributeContains(ATTR6, "12")))),
                        COMMON_BUTTON1,
                        SimpleButton.class,
                        getWidgetDescription(SIMPLE_BUTTON,
                                shouldHaveAttributeContains(ATTR1, "1")
                                        .or(not(shouldHaveAttributeContains(ATTR6, "12"))))},

                {button()
                        .timeOut(FIVE_SECONDS)
                        .criteria(shouldHaveAttributeContains(ATTR1, compile("1")).or(shouldHaveAttributeContains(ATTR6, compile("12")))),
                        COMMON_LABELED_BUTTON1,
                        SimpleButton.class,
                        getWidgetDescription(SIMPLE_BUTTON,
                                shouldHaveAttributeContains(ATTR1, compile("1"))
                                        .or(shouldHaveAttributeContains(ATTR6, compile("12"))))},

                {button()
                        .timeOut(FIVE_SECONDS)
                        .criteria(shouldHaveAttributeContains(ATTR1, compile("1")).or(not(shouldHaveAttributeContains(ATTR6, compile("12"))))),
                        COMMON_BUTTON1,
                        SimpleButton.class,
                        getWidgetDescription(SIMPLE_BUTTON,
                                shouldHaveAttributeContains(ATTR1, compile("1"))
                                        .or(not(shouldHaveAttributeContains(ATTR6, compile("12")))))},

                {button()
                        .timeOut(FIVE_SECONDS)
                        .criteria(shouldHaveCssValue(CSS2, CSS_VALUE4))
                        .criteria(shouldHaveCssValue(CSS3, CSS_VALUE8)),
                        COMMON_LABELED_BUTTON2,
                        SimpleButton.class,
                        getWidgetDescription(SIMPLE_BUTTON,
                                shouldHaveCssValue(CSS2, CSS_VALUE4)
                                        .and(shouldHaveCssValue(CSS3, CSS_VALUE8)))},

                {button()
                        .timeOut(FIVE_SECONDS)
                        .criteria(shouldHaveCssValue(CSS2, CSS_VALUE4))
                        .criteria(not(shouldHaveCssValue(CSS3, CSS_VALUE10))),
                        COMMON_LABELED_BUTTON2,
                        SimpleButton.class,
                        getWidgetDescription(SIMPLE_BUTTON,
                                shouldHaveCssValue(CSS2, CSS_VALUE4)
                                        .and(not(shouldHaveCssValue(CSS3, CSS_VALUE10))))},

                {button()
                        .timeOut(FIVE_SECONDS)
                        .criteria(shouldHaveCssValueContains(CSS5, "9").or(shouldHaveCssValueContains(CSS6, "11"))),
                        CUSTOM_LABELED_BUTTON3,
                        CustomizedButton.class,
                        getWidgetDescription(CUSTOM_BUTTON,
                                shouldHaveCssValueContains(CSS5, "9")
                                        .or(shouldHaveCssValueContains(CSS6, "11")))},

                {button()
                        .timeOut(FIVE_SECONDS)
                        .criteria(not(shouldHaveCssValueContains(CSS5, "9").or(shouldHaveCssValueContains(CSS6, "11")))),
                        COMMON_BUTTON1,
                        SimpleButton.class,
                        getWidgetDescription(SIMPLE_BUTTON,
                                not(shouldHaveCssValueContains(CSS5, "9")
                                        .or(shouldHaveCssValueContains(CSS6, "11"))))},

                {button()
                        .timeOut(FIVE_SECONDS)
                        .criteria(shouldHaveCssValueContains(CSS16, compile("18")).or(shouldHaveCssValueContains(CSS4, compile("5")))),
                        COMMON_BUTTON1,
                        SimpleButton.class,
                        getWidgetDescription(SIMPLE_BUTTON,
                                shouldHaveCssValueContains(CSS16, compile("18"))
                                        .or(shouldHaveCssValueContains(CSS4, compile("5"))))},

                {button()
                        .timeOut(FIVE_SECONDS)
                        .criteria(shouldHaveCssValueContains(CSS4, compile("5")).or(shouldHaveCssValueContains(CSS16, compile("18"))).negate()),
                        COMMON_BUTTON3,
                        SimpleButton.class,
                        getWidgetDescription(SIMPLE_BUTTON,
                                shouldHaveCssValueContains(CSS4, compile("5"))
                                        .or(shouldHaveCssValueContains(CSS16, compile("18"))).negate())},
//----
                {button(BUTTON_LABEL_TEXT5, BUTTON_LABEL_TEXT9)
                        .criteria(shouldBeEnabled()),
                        CUSTOM_LABELED_BUTTON1,
                        CustomizedButton.class,
                        getWidgetDescription(CUSTOM_BUTTON, shouldBeEnabled(), BUTTON_LABEL_TEXT5, BUTTON_LABEL_TEXT9)},

                {button(BUTTON_LABEL_TEXT7)
                        .criteria(not(shouldBeEnabled())),
                        CUSTOM_LABELED_BUTTON3,
                        CustomizedButton.class,
                        getWidgetDescription(CUSTOM_BUTTON, not(shouldBeEnabled()), BUTTON_LABEL_TEXT7)},

                {button(BUTTON_LABEL_TEXT11)
                        .criteria(shouldBeVisible()),
                        CUSTOM_LABELED_BUTTON3,
                        CustomizedButton.class,
                        getWidgetDescription(CUSTOM_BUTTON, shouldBeVisible(), BUTTON_LABEL_TEXT11)},

                {button(BUTTON_LABEL_TEXT6, BUTTON_LABEL_TEXT10)
                        .criteria(not(shouldBeVisible())),
                        CUSTOM_LABELED_BUTTON2,
                        CustomizedButton.class,
                        getWidgetDescription(CUSTOM_BUTTON, not(shouldBeVisible()), BUTTON_LABEL_TEXT6, BUTTON_LABEL_TEXT10)},

                {button(BUTTON_LABEL_TEXT3)
                        .criteria(shouldContainElements(webElements(tagName(LABEL_TAG)).timeOut(ofMillis(5)), 1)),
                        COMMON_LABELED_BUTTON3,
                        LabeledButton.class,
                        getWidgetDescription(LABELED_BUTTON,
                                shouldContainElements(webElements(tagName(LABEL_TAG)).timeOut(ofMillis(5)), 1),
                                BUTTON_LABEL_TEXT3)},

                {button(BUTTON_LABEL_TEXT7, BUTTON_LABEL_TEXT11)
                        .criteria(not(shouldContainElements(webElements(tagName(LABEL_TAG)).timeOut(ofMillis(5)), 2))),
                        CUSTOM_LABELED_BUTTON3,
                        CustomizedButton.class,
                        getWidgetDescription(CUSTOM_BUTTON,
                                not(shouldContainElements(webElements(tagName(LABEL_TAG)).timeOut(ofMillis(5)), 2)),
                                BUTTON_LABEL_TEXT7, BUTTON_LABEL_TEXT11)},

                {button(BUTTON_LABEL_TEXT2).criteria(shouldHaveAttribute(ATTR1, VALUE2))
                        .criteria(shouldHaveAttribute(ATTR2, VALUE10)),
                        COMMON_LABELED_BUTTON2,
                        LabeledButton.class,
                        getWidgetDescription(LABELED_BUTTON, shouldHaveAttribute(ATTR1, VALUE2).and(shouldHaveAttribute(ATTR2, VALUE10)),
                                BUTTON_LABEL_TEXT2)},

                {button(BUTTON_LABEL_TEXT4)
                        .criteria(not(shouldHaveAttribute(ATTR1, VALUE3)))
                        .criteria(not(shouldHaveAttribute(ATTR2, VALUE11))),
                        COMMON_LABELED_BUTTON4,
                        LabeledButton.class,
                        getWidgetDescription(LABELED_BUTTON, not(shouldHaveAttribute(ATTR1, VALUE3))
                                        .and(not(shouldHaveAttribute(ATTR2, VALUE11))),
                                BUTTON_LABEL_TEXT4)},

                {button(BUTTON_LABEL_TEXT8, BUTTON_LABEL_TEXT12).criteria(shouldHaveAttributeContains(ATTR5, "1")),
                        CUSTOM_LABELED_BUTTON4,
                        CustomizedButton.class,
                        getWidgetDescription(CUSTOM_BUTTON, shouldHaveAttributeContains(ATTR5, "1"),
                                BUTTON_LABEL_TEXT8, BUTTON_LABEL_TEXT12)},

                {button(BUTTON_LABEL_TEXT1).criteria(not(shouldHaveAttributeContains(ATTR5, "1"))),
                        COMMON_LABELED_BUTTON1,
                        LabeledButton.class,
                        getWidgetDescription(LABELED_BUTTON, (not(shouldHaveAttributeContains(ATTR5, "1"))),
                                BUTTON_LABEL_TEXT1)},

                {button(BUTTON_LABEL_TEXT8, BUTTON_LABEL_TEXT12)
                        .criteria(shouldHaveAttributeContains(ATTR5, compile("1"))),
                        CUSTOM_LABELED_BUTTON4,
                        CustomizedButton.class,
                        getWidgetDescription(CUSTOM_BUTTON,
                                shouldHaveAttributeContains(ATTR5, compile("1")),
                                BUTTON_LABEL_TEXT8, BUTTON_LABEL_TEXT12)},

                {button(BUTTON_LABEL_TEXT1).criteria(not(shouldHaveAttributeContains(ATTR5, compile("1")))),
                        COMMON_LABELED_BUTTON1,
                        LabeledButton.class,
                        getWidgetDescription(LABELED_BUTTON,
                                not(shouldHaveAttributeContains(ATTR5, compile("1"))),
                                BUTTON_LABEL_TEXT1)},

                {button(BUTTON_LABEL_TEXT2).criteria(shouldHaveCssValue(CSS2, CSS_VALUE4))
                        .criteria(shouldHaveCssValue(CSS3, CSS_VALUE8)),
                        COMMON_LABELED_BUTTON2,
                        LabeledButton.class,
                        getWidgetDescription(LABELED_BUTTON,
                                shouldHaveCssValue(CSS2, CSS_VALUE4).and(shouldHaveCssValue(CSS3, CSS_VALUE8)),
                                BUTTON_LABEL_TEXT2)},

                {button(BUTTON_LABEL_TEXT1).criteria(not(shouldHaveCssValue(CSS2, CSS_VALUE4)))
                        .criteria(not(shouldHaveCssValue(CSS3, CSS_VALUE8))),
                        COMMON_LABELED_BUTTON1,
                        LabeledButton.class,
                        getWidgetDescription(LABELED_BUTTON,
                                not(shouldHaveCssValue(CSS2, CSS_VALUE4))
                                        .and(not(shouldHaveCssValue(CSS3, CSS_VALUE8))), BUTTON_LABEL_TEXT1)},

                {button(BUTTON_LABEL_TEXT7, BUTTON_LABEL_TEXT11)
                        .criteria(shouldHaveCssValueContains(CSS5, "9")
                        .or(shouldHaveCssValueContains(CSS6, "11"))),
                        CUSTOM_LABELED_BUTTON3,
                        CustomizedButton.class,
                        getWidgetDescription(CUSTOM_BUTTON, shouldHaveCssValueContains(CSS5, "9")
                                        .or(shouldHaveCssValueContains(CSS6, "11")),
                                BUTTON_LABEL_TEXT7, BUTTON_LABEL_TEXT11)},

                {button(BUTTON_LABEL_TEXT1)
                        .criteria(not(shouldHaveCssValueContains(CSS5, "9")
                        .or(shouldHaveCssValueContains(CSS6, "11")))),
                        COMMON_LABELED_BUTTON1,
                        LabeledButton.class,
                        getWidgetDescription(LABELED_BUTTON,
                                not(shouldHaveCssValueContains(CSS5, "9")
                                        .or(shouldHaveCssValueContains(CSS6, "11"))), BUTTON_LABEL_TEXT1)},

                {button(BUTTON_LABEL_TEXT2)
                        .criteria(shouldHaveCssValueContains(CSS2, compile("4"))
                        .or(shouldHaveCssValueContains(CSS3, compile("8")))),
                        COMMON_LABELED_BUTTON2,
                        LabeledButton.class,
                        getWidgetDescription(LABELED_BUTTON, shouldHaveCssValueContains(CSS2, compile("4"))
                                        .or(shouldHaveCssValueContains(CSS3, compile("8"))),
                                BUTTON_LABEL_TEXT2)},

                {button(BUTTON_LABEL_TEXT6).criteria(not(shouldHaveCssValueContains(CSS2, compile("4"))
                        .or(shouldHaveCssValueContains(CSS3, compile("8"))))),
                        CUSTOM_LABELED_BUTTON2,
                        CustomizedButton.class,
                        getWidgetDescription(CUSTOM_BUTTON,
                                not(shouldHaveCssValueContains(CSS2, compile("4"))
                                        .or(shouldHaveCssValueContains(CSS3, compile("8")))),
                                BUTTON_LABEL_TEXT6)},
                //----
                {button(BUTTON_LABEL_TEXT5, BUTTON_LABEL_TEXT9)
                        .timeOut(FIVE_SECONDS)
                        .criteria(shouldBeEnabled()),
                        CUSTOM_LABELED_BUTTON1,
                        CustomizedButton.class,
                        getWidgetDescription(CUSTOM_BUTTON, shouldBeEnabled(), BUTTON_LABEL_TEXT5, BUTTON_LABEL_TEXT9)},

                {button(BUTTON_LABEL_TEXT7)
                        .timeOut(FIVE_SECONDS)
                        .criteria(not(shouldBeEnabled())),
                        CUSTOM_LABELED_BUTTON3,
                        CustomizedButton.class,
                        getWidgetDescription(CUSTOM_BUTTON, not(shouldBeEnabled()), BUTTON_LABEL_TEXT7)},

                {button(BUTTON_LABEL_TEXT11)
                        .timeOut(FIVE_SECONDS)
                        .criteria(shouldBeVisible()),
                        CUSTOM_LABELED_BUTTON3,
                        CustomizedButton.class,
                        getWidgetDescription(CUSTOM_BUTTON, shouldBeVisible(), BUTTON_LABEL_TEXT11)},

                {button(BUTTON_LABEL_TEXT6, BUTTON_LABEL_TEXT10)
                        .timeOut(FIVE_SECONDS)
                        .criteria(not(shouldBeVisible())),
                        CUSTOM_LABELED_BUTTON2,
                        CustomizedButton.class,
                        getWidgetDescription(CUSTOM_BUTTON, not(shouldBeVisible()), BUTTON_LABEL_TEXT6, BUTTON_LABEL_TEXT10)},

                {button(BUTTON_LABEL_TEXT3)
                        .timeOut(FIVE_SECONDS)
                        .criteria(shouldContainElements(webElements(tagName(LABEL_TAG)).timeOut(ofMillis(5)), 1)),
                        COMMON_LABELED_BUTTON3,
                        LabeledButton.class,
                        getWidgetDescription(LABELED_BUTTON,
                                shouldContainElements(webElements(tagName(LABEL_TAG)).timeOut(ofMillis(5)), 1),
                                BUTTON_LABEL_TEXT3)},

                {button(BUTTON_LABEL_TEXT7, BUTTON_LABEL_TEXT11)
                        .timeOut(FIVE_SECONDS)
                        .criteria(not(shouldContainElements(webElements(tagName(LABEL_TAG)).timeOut(ofMillis(5)), 2))),
                        CUSTOM_LABELED_BUTTON3,
                        CustomizedButton.class,
                        getWidgetDescription(CUSTOM_BUTTON,
                                not(shouldContainElements(webElements(tagName(LABEL_TAG)).timeOut(ofMillis(5)), 2)),
                                BUTTON_LABEL_TEXT7, BUTTON_LABEL_TEXT11)},

                {button(BUTTON_LABEL_TEXT2).criteria(shouldHaveAttribute(ATTR1, VALUE2))
                        .timeOut(FIVE_SECONDS)
                        .criteria(shouldHaveAttribute(ATTR2, VALUE10)),
                        COMMON_LABELED_BUTTON2,
                        LabeledButton.class,
                        getWidgetDescription(LABELED_BUTTON, shouldHaveAttribute(ATTR1, VALUE2).and(shouldHaveAttribute(ATTR2, VALUE10)),
                                BUTTON_LABEL_TEXT2)},

                {button(BUTTON_LABEL_TEXT4)
                        .timeOut(FIVE_SECONDS)
                        .criteria(not(shouldHaveAttribute(ATTR1, VALUE3)))
                        .criteria(not(shouldHaveAttribute(ATTR2, VALUE11))),
                        COMMON_LABELED_BUTTON4,
                        LabeledButton.class,
                        getWidgetDescription(LABELED_BUTTON, not(shouldHaveAttribute(ATTR1, VALUE3))
                                        .and(not(shouldHaveAttribute(ATTR2, VALUE11))),
                                BUTTON_LABEL_TEXT4)},

                {button(BUTTON_LABEL_TEXT8, BUTTON_LABEL_TEXT12)
                        .timeOut(FIVE_SECONDS)
                        .criteria(shouldHaveAttributeContains(ATTR5, "1")),
                        CUSTOM_LABELED_BUTTON4,
                        CustomizedButton.class,
                        getWidgetDescription(CUSTOM_BUTTON, shouldHaveAttributeContains(ATTR5, "1"),
                                BUTTON_LABEL_TEXT8, BUTTON_LABEL_TEXT12)},

                {button(BUTTON_LABEL_TEXT1)
                        .timeOut(FIVE_SECONDS)
                        .criteria(not(shouldHaveAttributeContains(ATTR5, "1"))),
                        COMMON_LABELED_BUTTON1,
                        LabeledButton.class,
                        getWidgetDescription(LABELED_BUTTON, (not(shouldHaveAttributeContains(ATTR5, "1"))),
                                BUTTON_LABEL_TEXT1)},

                {button(BUTTON_LABEL_TEXT8, BUTTON_LABEL_TEXT12)
                        .timeOut(FIVE_SECONDS)
                        .criteria(shouldHaveAttributeContains(ATTR5, compile("1"))),
                        CUSTOM_LABELED_BUTTON4,
                        CustomizedButton.class,
                        getWidgetDescription(CUSTOM_BUTTON,
                                shouldHaveAttributeContains(ATTR5, compile("1")),
                                BUTTON_LABEL_TEXT8, BUTTON_LABEL_TEXT12)},

                {button(BUTTON_LABEL_TEXT1)
                        .timeOut(FIVE_SECONDS)
                        .criteria(not(shouldHaveAttributeContains(ATTR5, compile("1")))),
                        COMMON_LABELED_BUTTON1,
                        LabeledButton.class,
                        getWidgetDescription(LABELED_BUTTON,
                                not(shouldHaveAttributeContains(ATTR5, compile("1"))),
                                BUTTON_LABEL_TEXT1)},

                {button(BUTTON_LABEL_TEXT2)
                        .timeOut(FIVE_SECONDS)
                        .criteria(shouldHaveCssValue(CSS2, CSS_VALUE4))
                        .criteria(shouldHaveCssValue(CSS3, CSS_VALUE8)),
                        COMMON_LABELED_BUTTON2,
                        LabeledButton.class,
                        getWidgetDescription(LABELED_BUTTON,
                                shouldHaveCssValue(CSS2, CSS_VALUE4).and(shouldHaveCssValue(CSS3, CSS_VALUE8)),
                                BUTTON_LABEL_TEXT2)},

                {button(BUTTON_LABEL_TEXT1)
                        .timeOut(FIVE_SECONDS)
                        .criteria(not(shouldHaveCssValue(CSS2, CSS_VALUE4)))
                        .criteria(not(shouldHaveCssValue(CSS3, CSS_VALUE8))),
                        COMMON_LABELED_BUTTON1,
                        LabeledButton.class,
                        getWidgetDescription(LABELED_BUTTON,
                                not(shouldHaveCssValue(CSS2, CSS_VALUE4))
                                        .and(not(shouldHaveCssValue(CSS3, CSS_VALUE8))), BUTTON_LABEL_TEXT1)},

                {button(BUTTON_LABEL_TEXT7, BUTTON_LABEL_TEXT11)
                        .timeOut(FIVE_SECONDS)
                        .criteria(shouldHaveCssValueContains(CSS5, "9")
                        .or(shouldHaveCssValueContains(CSS6, "11"))),
                        CUSTOM_LABELED_BUTTON3,
                        CustomizedButton.class,
                        getWidgetDescription(CUSTOM_BUTTON, shouldHaveCssValueContains(CSS5, "9")
                                        .or(shouldHaveCssValueContains(CSS6, "11")),
                                BUTTON_LABEL_TEXT7, BUTTON_LABEL_TEXT11)},

                {button(BUTTON_LABEL_TEXT1)
                        .timeOut(FIVE_SECONDS)
                        .criteria(not(shouldHaveCssValueContains(CSS5, "9")
                        .or(shouldHaveCssValueContains(CSS6, "11")))),
                        COMMON_LABELED_BUTTON1,
                        LabeledButton.class,
                        getWidgetDescription(LABELED_BUTTON,
                                not(shouldHaveCssValueContains(CSS5, "9")
                                        .or(shouldHaveCssValueContains(CSS6, "11"))), BUTTON_LABEL_TEXT1)},

                {button(BUTTON_LABEL_TEXT2)
                        .timeOut(FIVE_SECONDS)
                        .criteria(shouldHaveCssValueContains(CSS2, compile("4"))
                        .or(shouldHaveCssValueContains(CSS3, compile("8")))),
                        COMMON_LABELED_BUTTON2,
                        LabeledButton.class,
                        getWidgetDescription(LABELED_BUTTON, shouldHaveCssValueContains(CSS2, compile("4"))
                                        .or(shouldHaveCssValueContains(CSS3, compile("8"))),
                                BUTTON_LABEL_TEXT2)},

                {button(BUTTON_LABEL_TEXT6)
                        .timeOut(FIVE_SECONDS)
                        .criteria(not(shouldHaveCssValueContains(CSS2, compile("4"))
                        .or(shouldHaveCssValueContains(CSS3, compile("8"))))),
                        CUSTOM_LABELED_BUTTON2,
                        CustomizedButton.class,
                        getWidgetDescription(CUSTOM_BUTTON,
                                not(shouldHaveCssValueContains(CSS2, compile("4"))
                                        .or(shouldHaveCssValueContains(CSS3, compile("8")))),
                                BUTTON_LABEL_TEXT6)},

                {button()
                        .foundFrom(tableCell().criteria("Cell should be equal to " + CELL_TEXT73, tableCell ->
                        CELL_TEXT73.equals(tableCell.getValue()))
                        .foundFrom(tableRow().criteria("Row should have tag " + DIV, tableRow ->
                                DIV.equals(tableRow.getWrappedElement().getTagName()))
                                .foundFrom(table(TABLE_LABEL_TEXT5)))),

                        CUSTOM_LABELED_BUTTON1,
                        CustomizedButton.class, CUSTOM_BUTTON},

                {button().timeOut(FIVE_SECONDS)
                        .foundFrom(tableRow()
                                .foundFrom(table(TABLE_LABEL_TEXT6))),

                        CUSTOM_LABELED_BUTTON2,
                        CustomizedButton.class, CUSTOM_BUTTON},

                {button(BUTTON_LABEL_TEXT5)
                        .foundFrom(tableCell().criteria("Cell should be equal to " + CELL_TEXT73,
                                tableCell -> CELL_TEXT73.equals(tableCell.getValue()))

                        .foundFrom(tableRow().criteria("Row should have tag " + DIV,
                                tableRow -> DIV.equals(tableRow.getWrappedElement().getTagName())))

                        .foundFrom(table(TABLE_LABEL_TEXT5))),

                        CUSTOM_LABELED_BUTTON1,
                        CustomizedButton.class,
                        format("%s '%s'", CUSTOM_BUTTON, BUTTON_LABEL_TEXT5)},



                {button(BUTTON_LABEL_TEXT6, BUTTON_LABEL_TEXT10)
                        .foundFrom(tableRow())
                        .foundFrom(table(TABLE_LABEL_TEXT6)),

                        CUSTOM_LABELED_BUTTON2,
                        CustomizedButton.class,
                        format("%s '%s,%s'", CUSTOM_BUTTON, BUTTON_LABEL_TEXT6, BUTTON_LABEL_TEXT10)},

                {button(BUTTON_LABEL_TEXT5, BUTTON_LABEL_TEXT9)
                        .timeOut(FIVE_SECONDS)
                        .foundFrom(tableCell().criteria("Cell should be equal to " + CELL_TEXT73,
                                tableCell -> CELL_TEXT73.equals(tableCell.getValue())))

                        .foundFrom(tableRow().criteria("Row should have tag " + DIV,
                                tableRow -> DIV.equals(tableRow.getWrappedElement().getTagName())))

                        .foundFrom(table(TABLE_LABEL_TEXT5)),

                        CUSTOM_LABELED_BUTTON1,
                        CustomizedButton.class,
                        format("%s '%s,%s'", CUSTOM_BUTTON, BUTTON_LABEL_TEXT5, BUTTON_LABEL_TEXT9)}
        };
    }

    @Test(retryAnalyzer = RetryAnalyzer.class, dataProvider = "search without criteria")
    public <T extends Widget> void findWidgetTest(SearchSupplier<T> search,
                                                  WebElement element,
                                                  Class<?> widgetClass,
                                                  String expectedDescription) {
        setStartBenchMark();
        T t = seleniumSteps.find(search);
        setEndBenchMark();
        assertThat(t.getClass().getSuperclass(), is(widgetClass));
        ofNullable(element).ifPresent(element1 -> assertThat(t.getWrappedElement(), equalTo(element1)));
        assertThat(t.toString(), is(expectedDescription));
        assertThat(getTimeDifference() - ONE_SECOND.toMillis(), lessThan(HALF_SECOND.toMillis()));
    }
}
