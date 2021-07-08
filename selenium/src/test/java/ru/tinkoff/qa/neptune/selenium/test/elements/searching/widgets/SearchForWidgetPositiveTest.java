package ru.tinkoff.qa.neptune.selenium.test.elements.searching.widgets;

import org.openqa.selenium.WebElement;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import ru.tinkoff.qa.neptune.selenium.api.widget.Widget;
import ru.tinkoff.qa.neptune.selenium.functions.searching.SearchSupplier;
import ru.tinkoff.qa.neptune.selenium.test.BaseWebDriverTest;
import ru.tinkoff.qa.neptune.selenium.test.RetryAnalyzer;
import ru.tinkoff.qa.neptune.selenium.test.elements.searching.widgets.buttons.CustomizedButton;
import ru.tinkoff.qa.neptune.selenium.test.elements.searching.widgets.buttons.LabeledButton;
import ru.tinkoff.qa.neptune.selenium.test.elements.searching.widgets.buttons.SimpleButton;

import static java.time.Duration.ofMillis;
import static java.util.Optional.ofNullable;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.openqa.selenium.By.tagName;
import static ru.tinkoff.qa.neptune.core.api.steps.Criteria.NOT;
import static ru.tinkoff.qa.neptune.core.api.steps.Criteria.OR;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.CommonElementCriteria.*;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.MultipleSearchSupplier.webElements;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.SearchSupplier.*;
import static ru.tinkoff.qa.neptune.selenium.test.FakeDOMModel.*;

public class SearchForWidgetPositiveTest extends BaseWebDriverTest {

    @DataProvider(name = "search without criteria")
    public static Object[][] searchCriteria() {
        return new Object[][]{
                {button(), COMMON_BUTTON1, SimpleButton.class},

                {button().timeOut(FIVE_SECONDS), COMMON_BUTTON1, SimpleButton.class},

                {button(BUTTON_LABEL_TEXT1), COMMON_LABELED_BUTTON1, LabeledButton.class},

                {button(BUTTON_LABEL_TEXT1)
                        .timeOut(FIVE_SECONDS), COMMON_LABELED_BUTTON1,
                        LabeledButton.class},

                {button(BUTTON_LABEL_TEXT6), CUSTOM_LABELED_BUTTON2,
                        CustomizedButton.class},


                {button().criteria(enabled()),
                        COMMON_BUTTON3,
                        SimpleButton.class},

                {button().criteria(NOT(enabled())), COMMON_BUTTON1,
                        SimpleButton.class},

                {button().criteria(visible()), COMMON_BUTTON2,
                        SimpleButton.class},

                {button().criteria(NOT(visible())), COMMON_BUTTON1,
                        SimpleButton.class},

                {button().criteria(nested(webElements(tagName(LABEL_TAG))
                        .timeOut(ofMillis(5)), 1)),
                        COMMON_LABELED_BUTTON1,
                        SimpleButton.class},

                {button().criteria(NOT(nested(webElements(tagName(LABEL_TAG))
                        .timeOut(ofMillis(5)), 1))),
                        COMMON_BUTTON1,
                        SimpleButton.class},

                {button()
                        .criteria(attr(ATTR5, VALUE11))
                        .criteria(attr(ATTR6, VALUE12)),
                        CUSTOM_LABELED_BUTTON2,
                        CustomizedButton.class},

                {button().criteria(attr(ATTR5, VALUE11))
                        .criteria(NOT(attr(ATTR6, VALUE13))),
                        CUSTOM_LABELED_BUTTON2,
                        CustomizedButton.class},

                {button().criteria(OR(
                        attrMatches(ATTR1, "1"),
                        attrMatches(ATTR6, "12")
                )), COMMON_LABELED_BUTTON1,
                        SimpleButton.class},

                {button().criteria(OR(attrMatches(ATTR1, "1"),
                        NOT(attrMatches(ATTR6, "12")))), COMMON_BUTTON1,
                        SimpleButton.class},

                {button().criteria(OR(
                        attrMatches(ATTR1, "1"),
                        attrMatches(ATTR6, "12")
                )), COMMON_LABELED_BUTTON1,
                        SimpleButton.class},

                {button().criteria(OR(
                        attrMatches(ATTR1, "1"),
                        NOT(attrMatches(ATTR6, "12"))
                )), COMMON_BUTTON1,
                        SimpleButton.class},

                {button().criteria(css(CSS2, CSS_VALUE4))
                        .criteria(css(CSS3, CSS_VALUE8)),
                        COMMON_LABELED_BUTTON2,
                        SimpleButton.class},

                {button()
                        .criteria(css(CSS2, CSS_VALUE4))
                        .criteria(NOT(css(CSS3, CSS_VALUE10))),
                        COMMON_LABELED_BUTTON2,
                        SimpleButton.class},

                {button().criteria(OR(
                        cssMatches(CSS5, "9"),
                        cssMatches(CSS6, "11")
                )), CUSTOM_LABELED_BUTTON3,
                        CustomizedButton.class},

                {button().criteria(NOT(
                        cssMatches(CSS5, "9"),
                        cssMatches(CSS6, "11"))), COMMON_BUTTON1,
                        SimpleButton.class},

                {button().criteria(OR(cssMatches(CSS16, "18"),
                        cssMatches(CSS4, "5"))),
                        COMMON_BUTTON1,
                        SimpleButton.class},


                {button()
                        .timeOut(FIVE_SECONDS)
                        .criteria(enabled()),
                        COMMON_BUTTON3,
                        SimpleButton.class},

                {button()
                        .timeOut(FIVE_SECONDS)
                        .criteria(NOT(enabled())),
                        COMMON_BUTTON1,
                        SimpleButton.class},

                {button()
                        .timeOut(FIVE_SECONDS)
                        .criteria(visible()),
                        COMMON_BUTTON2,
                        SimpleButton.class},

                {button()
                        .timeOut(FIVE_SECONDS)
                        .criteria(NOT(visible())),
                        COMMON_BUTTON1,
                        SimpleButton.class},

                {button()
                        .timeOut(FIVE_SECONDS)
                        .criteria(nested(webElements(tagName(LABEL_TAG)).timeOut(ofMillis(5)), 1)),
                        COMMON_LABELED_BUTTON1,
                        SimpleButton.class},

                {button()
                        .foundFrom(tableCell().criteria("Cell should be equal to " + CELL_TEXT73, tableCell ->
                        CELL_TEXT73.equals(tableCell.getValue()))
                        .foundFrom(tableRow().criteria("Row should have tag " + DIV, tableRow ->
                                DIV.equals(tableRow.getWrappedElement().getTagName()))
                                .foundFrom(table(TABLE_LABEL_TEXT5)))),

                        CUSTOM_LABELED_BUTTON1,
                        CustomizedButton.class},

                {button().timeOut(FIVE_SECONDS)
                        .foundFrom(tableRow()
                        .foundFrom(table(TABLE_LABEL_TEXT6))),

                        CUSTOM_LABELED_BUTTON2,
                        CustomizedButton.class},

                {button(BUTTON_LABEL_TEXT5)
                        .foundFrom(tableCell().criteria("Cell should be equal to " + CELL_TEXT73,
                        tableCell -> CELL_TEXT73.equals(tableCell.getValue()))

                        .foundFrom(tableRow().criteria("Row should have tag " + DIV,
                                tableRow -> DIV.equals(tableRow.getWrappedElement().getTagName())))

                        .foundFrom(table(TABLE_LABEL_TEXT5))),

                        CUSTOM_LABELED_BUTTON1,
                        CustomizedButton.class},


                {button(BUTTON_LABEL_TEXT10)
                        .foundFrom(tableRow())
                        .foundFrom(table(TABLE_LABEL_TEXT6)),

                        CUSTOM_LABELED_BUTTON2,
                        CustomizedButton.class},

                {button(BUTTON_LABEL_TEXT9)
                        .timeOut(FIVE_SECONDS)
                        .foundFrom(tableCell().criteria("Cell should be equal to " + CELL_TEXT73,
                                tableCell -> CELL_TEXT73.equals(tableCell.getValue())))

                        .foundFrom(tableRow().criteria("Row should have tag " + DIV,
                                tableRow -> DIV.equals(tableRow.getWrappedElement().getTagName())))

                        .foundFrom(table(TABLE_LABEL_TEXT5)),

                        CUSTOM_LABELED_BUTTON1,
                        CustomizedButton.class}
        };
    }

    @Test(retryAnalyzer = RetryAnalyzer.class, dataProvider = "search without criteria")
    public <T extends Widget> void findWidgetTest(SearchSupplier<T> search,
                                                  WebElement element,
                                                  Class<?> widgetClass) {
        setStartBenchMark();
        T t = seleniumSteps.find(search);
        setEndBenchMark();
        assertThat(t.getClass().getSuperclass(), is(widgetClass));
        ofNullable(element).ifPresent(element1 -> assertThat(t.getWrappedElement(), equalTo(element1)));
        assertThat(getTimeDifference() - ONE_SECOND.toMillis(), lessThan(100L));
    }
}
