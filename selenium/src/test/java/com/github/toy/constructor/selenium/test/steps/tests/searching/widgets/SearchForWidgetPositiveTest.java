package com.github.toy.constructor.selenium.test.steps.tests.searching.widgets;

import com.github.toy.constructor.selenium.api.widget.Widget;
import com.github.toy.constructor.selenium.functions.searching.SearchSupplier;
import com.github.toy.constructor.selenium.test.BaseWebDriverTest;
import com.github.toy.constructor.selenium.test.RetryAnalyzer;
import com.github.toy.constructor.selenium.test.steps.tests.searching.widgets.buttons.CustomizedButton;
import com.github.toy.constructor.selenium.test.steps.tests.searching.widgets.buttons.LabeledButton;
import com.github.toy.constructor.selenium.test.steps.tests.searching.widgets.buttons.SimpleButton;
import com.github.toy.constructor.selenium.test.steps.tests.searching.widgets.flags.LabeledCheckBox;
import com.github.toy.constructor.selenium.test.steps.tests.searching.widgets.flags.LabeledRadioButton;
import com.github.toy.constructor.selenium.test.steps.tests.searching.widgets.flags.SimpleCheckbox;
import com.github.toy.constructor.selenium.test.steps.tests.searching.widgets.flags.SimpleRadioButton;
import com.github.toy.constructor.selenium.test.steps.tests.searching.widgets.link.CustomizedLink;
import com.github.toy.constructor.selenium.test.steps.tests.searching.widgets.link.LabeledLink;
import com.github.toy.constructor.selenium.test.steps.tests.searching.widgets.select.LabeledSelect;
import com.github.toy.constructor.selenium.test.steps.tests.searching.widgets.select.MultiSelect;
import com.github.toy.constructor.selenium.test.steps.tests.searching.widgets.select.SimpleSelect;
import com.github.toy.constructor.selenium.test.steps.tests.searching.widgets.tab.CustomizedTab;
import com.github.toy.constructor.selenium.test.steps.tests.searching.widgets.tab.LabeledTab;
import com.github.toy.constructor.selenium.test.steps.tests.searching.widgets.tab.SimpleTab;
import com.github.toy.constructor.selenium.test.steps.tests.searching.widgets.table.LabeledTable;
import com.github.toy.constructor.selenium.test.steps.tests.searching.widgets.table.SimpleTable;
import com.github.toy.constructor.selenium.test.steps.tests.searching.widgets.table.SpreadsheetTable;
import org.openqa.selenium.WebElement;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.math.BigDecimal;
import java.util.function.Predicate;

import static com.github.toy.constructor.core.api.StoryWriter.condition;
import static com.github.toy.constructor.selenium.functions.searching.CommonConditions.shouldBeLabeledBy;
import static com.github.toy.constructor.selenium.functions.searching.SearchSupplier.*;
import static com.github.toy.constructor.selenium.test.FakeDOMModel.*;
import static com.github.toy.constructor.selenium.test.steps.tests.searching.widgets.WidgetNames.*;
import static java.lang.String.format;
import static java.util.List.of;
import static java.util.Optional.ofNullable;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class SearchForWidgetPositiveTest extends BaseWebDriverTest {

    private static String getWidgetDescription(String name, Predicate<?> condition) {
        return format("%s found on condition '%s'", name, condition);
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
                .or(closeTo(new BigDecimal(HALF_SECOND.toMillis()), new BigDecimal(300))));
    }
}
