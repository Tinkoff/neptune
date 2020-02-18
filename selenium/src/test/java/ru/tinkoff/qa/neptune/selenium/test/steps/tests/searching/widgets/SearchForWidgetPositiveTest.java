package ru.tinkoff.qa.neptune.selenium.test.steps.tests.searching.widgets;

import org.openqa.selenium.WebElement;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import ru.tinkoff.qa.neptune.core.api.steps.Criteria;
import ru.tinkoff.qa.neptune.selenium.api.widget.Widget;
import ru.tinkoff.qa.neptune.selenium.functions.searching.SearchSupplier;
import ru.tinkoff.qa.neptune.selenium.test.BaseWebDriverTest;
import ru.tinkoff.qa.neptune.selenium.test.RetryAnalyzer;
import ru.tinkoff.qa.neptune.selenium.test.steps.tests.searching.widgets.buttons.CustomizedButton;
import ru.tinkoff.qa.neptune.selenium.test.steps.tests.searching.widgets.buttons.LabeledButton;
import ru.tinkoff.qa.neptune.selenium.test.steps.tests.searching.widgets.buttons.SimpleButton;

import static java.lang.String.format;
import static java.time.Duration.ofMillis;
import static java.util.Optional.ofNullable;
import static java.util.regex.Pattern.compile;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.openqa.selenium.By.tagName;
import static ru.tinkoff.qa.neptune.core.api.steps.Criteria.*;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.CommonElementCriteria.*;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.MultipleSearchSupplier.webElements;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.SearchSupplier.*;
import static ru.tinkoff.qa.neptune.selenium.test.FakeDOMModel.*;
import static ru.tinkoff.qa.neptune.selenium.test.steps.tests.searching.widgets.WidgetNames.*;

public class SearchForWidgetPositiveTest extends BaseWebDriverTest {

    private static String getWidgetDescription(String name, Criteria<?> condition) {
        return format("%s ['%s']", name, condition);
    }

    private static String getWidgetDescription(String name, Criteria<?> condition, String... labels) {
        return format("%s '%s' ['%s']", name, String.join(",", labels), condition);
    }

    @DataProvider(name = "search without criteria")
    public static Object[][] searchCriteria() {
        return new Object[][]{
                {button(), COMMON_BUTTON1,
                        SimpleButton.class, SIMPLE_BUTTON},

                {button().timeOut(FIVE_SECONDS), COMMON_BUTTON1,
                        SimpleButton.class, SIMPLE_BUTTON},

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


                {button().criteria(enabled()),
                        COMMON_BUTTON3,
                        SimpleButton.class,
                        getWidgetDescription(SIMPLE_BUTTON,
                                enabled())},

                {button().criteria(NOT(enabled())), COMMON_BUTTON1,
                        SimpleButton.class,
                        getWidgetDescription(SIMPLE_BUTTON,
                                NOT(enabled()))},

                {button().criteria(visible()), COMMON_BUTTON2,
                        SimpleButton.class,
                        getWidgetDescription(SIMPLE_BUTTON,
                                visible())},

                {button().criteria(NOT(visible())), COMMON_BUTTON1,
                        SimpleButton.class,
                        getWidgetDescription(SIMPLE_BUTTON,
                                NOT(visible()))},

                {button().criteria(nestedElements(webElements(tagName(LABEL_TAG))
                        .timeOut(ofMillis(5)), 1)),
                        COMMON_LABELED_BUTTON1,
                        SimpleButton.class,
                        getWidgetDescription(SIMPLE_BUTTON,
                                nestedElements(webElements(tagName(LABEL_TAG))
                                        .timeOut(ofMillis(5)), 1))},

                {button().criteria(NOT(nestedElements(webElements(tagName(LABEL_TAG))
                        .timeOut(ofMillis(5)), 1))),
                        COMMON_BUTTON1,
                        SimpleButton.class,
                        getWidgetDescription(SIMPLE_BUTTON,
                                NOT(nestedElements(webElements(tagName(LABEL_TAG))
                                        .timeOut(ofMillis(5)), 1)))},

                {button()
                        .criteria(attribute(ATTR5, VALUE11))
                        .criteria(attribute(ATTR6, VALUE12)),
                        CUSTOM_LABELED_BUTTON2,
                        CustomizedButton.class,
                        getWidgetDescription(CUSTOM_BUTTON,
                                AND(attribute(ATTR5, VALUE11),
                                        attribute(ATTR6, VALUE12)))},

                {button().criteria(attribute(ATTR5, VALUE11))
                        .criteria(NOT(attribute(ATTR6, VALUE13))),
                        CUSTOM_LABELED_BUTTON2,
                        CustomizedButton.class,
                        getWidgetDescription(CUSTOM_BUTTON,
                                AND(attribute(ATTR5, VALUE11),
                                        NOT(attribute(ATTR6, VALUE13))))},

                {button().criteria(OR(
                        attributeContains(ATTR1, "1"),
                        attributeContains(ATTR6, "12")
                )), COMMON_LABELED_BUTTON1,
                        SimpleButton.class,
                        getWidgetDescription(SIMPLE_BUTTON,
                                OR(
                                        attributeContains(ATTR1, "1"),
                                        attributeContains(ATTR6, "12")
                                ))},

                {button().criteria(OR(attributeContains(ATTR1, "1"),
                        NOT(attributeContains(ATTR6, "12")))), COMMON_BUTTON1,
                        SimpleButton.class,
                        getWidgetDescription(SIMPLE_BUTTON,
                                OR(attributeContains(ATTR1, "1"),
                                        NOT(attributeContains(ATTR6, "12"))))},

                {button().criteria(OR(
                        attributeMatches(ATTR1, compile("1")),
                        attributeMatches(ATTR6, compile("12"))
                )), COMMON_LABELED_BUTTON1,
                        SimpleButton.class,
                        getWidgetDescription(SIMPLE_BUTTON,
                                OR(
                                        attributeMatches(ATTR1, compile("1")),
                                        attributeMatches(ATTR6, compile("12"))
                                ))},

                {button().criteria(OR(
                        attributeMatches(ATTR1, compile("1")),
                        NOT(attributeMatches(ATTR6, compile("12")))
                )), COMMON_BUTTON1,
                        SimpleButton.class,
                        getWidgetDescription(SIMPLE_BUTTON,
                                OR(
                                        attributeMatches(ATTR1, compile("1")),
                                        NOT(attributeMatches(ATTR6, compile("12")))
                                ))},

                {button().criteria(css(CSS2, CSS_VALUE4))
                        .criteria(css(CSS3, CSS_VALUE8)),
                        COMMON_LABELED_BUTTON2,
                        SimpleButton.class,
                        getWidgetDescription(SIMPLE_BUTTON,
                                AND(
                                        css(CSS2, CSS_VALUE4),
                                        css(CSS3, CSS_VALUE8)
                                ))},

                {button()
                        .criteria(css(CSS2, CSS_VALUE4))
                        .criteria(NOT(css(CSS3, CSS_VALUE10))),
                        COMMON_LABELED_BUTTON2,
                        SimpleButton.class,
                        getWidgetDescription(SIMPLE_BUTTON,
                                AND(
                                        css(CSS2, CSS_VALUE4),
                                        NOT(css(CSS3, CSS_VALUE10))
                                ))},

                {button().criteria(OR(
                        cssContains(CSS5, "9"),
                        cssContains(CSS6, "11")
                )), CUSTOM_LABELED_BUTTON3,
                        CustomizedButton.class,
                        getWidgetDescription(CUSTOM_BUTTON,
                                OR(
                                        cssContains(CSS5, "9"),
                                        cssContains(CSS6, "11")
                                ))},

                {button().criteria(NOT(
                        cssContains(CSS5, "9"),
                        cssContains(CSS6, "11"))), COMMON_BUTTON1,
                        SimpleButton.class,
                        getWidgetDescription(SIMPLE_BUTTON,
                                NOT(
                                        cssContains(CSS5, "9"),
                                        cssContains(CSS6, "11")))},

                {button().criteria(OR(cssMatches(CSS16, compile("18")),
                        cssMatches(CSS4, compile("5")))),
                        COMMON_BUTTON1,
                        SimpleButton.class,
                        getWidgetDescription(SIMPLE_BUTTON,
                                OR(cssMatches(CSS16, compile("18")),
                                        cssMatches(CSS4, compile("5"))))},


                {button()
                        .timeOut(FIVE_SECONDS)
                        .criteria(enabled()),
                        COMMON_BUTTON3,
                        SimpleButton.class,
                        getWidgetDescription(SIMPLE_BUTTON,
                                enabled())},

                {button()
                        .timeOut(FIVE_SECONDS)
                        .criteria(NOT(enabled())),
                        COMMON_BUTTON1,
                        SimpleButton.class,
                        getWidgetDescription(SIMPLE_BUTTON,
                                NOT(enabled()))},

                {button()
                        .timeOut(FIVE_SECONDS)
                        .criteria(visible()),
                        COMMON_BUTTON2,
                        SimpleButton.class,
                        getWidgetDescription(SIMPLE_BUTTON,
                                visible())},

                {button()
                        .timeOut(FIVE_SECONDS)
                        .criteria(NOT(visible())),
                        COMMON_BUTTON1,
                        SimpleButton.class,
                        getWidgetDescription(SIMPLE_BUTTON,
                                NOT(visible()))},

                {button()
                        .timeOut(FIVE_SECONDS)
                        .criteria(nestedElements(webElements(tagName(LABEL_TAG)).timeOut(ofMillis(5)), 1)),
                        COMMON_LABELED_BUTTON1,
                        SimpleButton.class,
                        getWidgetDescription(SIMPLE_BUTTON,
                                nestedElements(webElements(tagName(LABEL_TAG))
                                        .timeOut(ofMillis(5)), 1))},

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
