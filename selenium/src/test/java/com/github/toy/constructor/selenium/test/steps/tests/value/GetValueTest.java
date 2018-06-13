package com.github.toy.constructor.selenium.test.steps.tests.value;

import com.github.toy.constructor.selenium.api.widget.drafts.*;
import com.github.toy.constructor.selenium.test.BaseWebDriverTest;
import org.testng.annotations.Test;

import static com.github.toy.constructor.selenium.functions.edit.EditActionSupplier.valueOfThe;
import static com.github.toy.constructor.selenium.functions.searching.CommonConditions.*;
import static com.github.toy.constructor.selenium.functions.searching.MultipleSearchSupplier.buttons;
import static com.github.toy.constructor.selenium.functions.searching.MultipleSearchSupplier.textFields;
import static com.github.toy.constructor.selenium.functions.searching.MultipleSearchSupplier.webElements;
import static com.github.toy.constructor.selenium.functions.searching.SearchSupplier.*;
import static com.github.toy.constructor.selenium.functions.searching.SearchSupplier.radioButton;
import static com.github.toy.constructor.selenium.functions.value.SequentialGetValueSupplier.ofThe;
import static com.github.toy.constructor.selenium.test.FakeDOMModel.*;
import static java.time.Duration.ofMillis;
import static org.hamcrest.MatcherAssert.assertThat;

import static java.util.List.of;
import static org.hamcrest.Matchers.*;
import static org.openqa.selenium.By.tagName;

public class GetValueTest extends BaseWebDriverTest {

    @Test
    public void valueOfTextFieldTest() {
        assertThat(seleniumSteps.getValue(ofThe(textField(INPUT_LABEL_TEXT1))),
                is(INPUT_TEXT5));

        assertThat(seleniumSteps.edit(valueOfThe(textField(INPUT_LABEL_TEXT1), of(INPUT_TEXT4)))
                        .getValue(ofThe(textField(INPUT_LABEL_TEXT1))),
                is(INPUT_TEXT4));

        assertThat(seleniumSteps.getValue(ofThe(textField(shouldHaveAttribute(ATTR8, VALUE4)))),
                emptyOrNullString());

        assertThat(seleniumSteps
                        .edit(valueOfThe(textField(shouldHaveAttribute(ATTR8, VALUE4)), of(INPUT_TEXT1))
                                .andValueOfThe(textField(of(INPUT_LABEL_TEXT8, INPUT_LABEL_TEXT12)), of(INPUT_TEXT2)))
                        .getValue(ofThe(textField(shouldHaveAttribute(ATTR8, VALUE4)))),
                is(INPUT_TEXT1));

        assertThat(seleniumSteps.getValue(ofThe(textField(of(INPUT_LABEL_TEXT8, INPUT_LABEL_TEXT12)))),
                is(INPUT_TEXT2));

        TextField nestedTextField = seleniumSteps.find(textField(INPUT_LABEL_TEXT5)
                .foundFrom(table(of(TABLE_LABEL_TEXT5, TABLE_LABEL_TEXT9))));

        assertThat(seleniumSteps.edit(valueOfThe(nestedTextField, of(INPUT_TEXT7)))
                .getValue(ofThe(nestedTextField)),
                is(INPUT_TEXT7));
    }

    @Test
    public void valueOfFlagTest() {
        assertThat(seleniumSteps.getValue(ofThe(flag(RADIOBUTTON_LABEL_TEXT4))),
                is(false));

        assertThat(seleniumSteps.edit(valueOfThe(flag(RADIOBUTTON_LABEL_TEXT4), false))
                        .getValue(ofThe(flag(RADIOBUTTON_LABEL_TEXT4))),
                is(false));

        assertThat(seleniumSteps.edit(valueOfThe(flag(RADIOBUTTON_LABEL_TEXT4), true))
                        .getValue(ofThe(flag(RADIOBUTTON_LABEL_TEXT4))),
                is(true));

        seleniumSteps.edit(valueOfThe(flag(of(CHECKBOX_LABEL_TEXT8, CHECKBOX_LABEL_TEXT12)), true)
                .andValueOfThe(flag(RADIOBUTTON_LABEL_TEXT2), true)
                .andValueOfThe(flag(shouldBeEnabled()), true));

        assertThat(seleniumSteps.getValue(ofThe(flag(of(CHECKBOX_LABEL_TEXT8, CHECKBOX_LABEL_TEXT12)))),
                is(true));

        assertThat(seleniumSteps.getValue(ofThe(flag(RADIOBUTTON_LABEL_TEXT2))),
                is(true));

        assertThat(seleniumSteps.getValue(ofThe(flag(shouldBeEnabled()))),
                is(true));

        Flag nestedFlag = seleniumSteps.find(flag(of(CHECKBOX_LABEL_TEXT5, CHECKBOX_LABEL_TEXT9))
                .foundFrom(table(of(TABLE_LABEL_TEXT5, TABLE_LABEL_TEXT9))));

        assertThat(seleniumSteps.edit(valueOfThe(nestedFlag, true))
                        .getValue(ofThe(nestedFlag)),
                is(true));
    }

    @Test
    public void valueOfCheckBoxTest() {
        assertThat(seleniumSteps.getValue(ofThe(checkbox(CHECKBOX_LABEL_TEXT4))),
                is(false));

        assertThat(seleniumSteps.edit(valueOfThe(checkbox(CHECKBOX_LABEL_TEXT4), false))
                        .getValue(ofThe(flag(CHECKBOX_LABEL_TEXT4))),
                is(false));

        assertThat(seleniumSteps.edit(valueOfThe(checkbox(CHECKBOX_LABEL_TEXT4), true))
                        .getValue(ofThe(checkbox(CHECKBOX_LABEL_TEXT4))),
                is(true));

        seleniumSteps.edit(valueOfThe(checkbox(of(CHECKBOX_LABEL_TEXT7, CHECKBOX_LABEL_TEXT11)), true)
                .andValueOfThe(checkbox(CHECKBOX_LABEL_TEXT3), true)
                .andValueOfThe(checkbox(shouldBeEnabled().negate()), true));

        assertThat(seleniumSteps.getValue(ofThe(checkbox(of(CHECKBOX_LABEL_TEXT7, CHECKBOX_LABEL_TEXT11)))),
                is(true));

        assertThat(seleniumSteps.getValue(ofThe(checkbox(CHECKBOX_LABEL_TEXT3))),
                is(true));

        assertThat(seleniumSteps.getValue(ofThe(flag(shouldBeEnabled().negate()))),
                is(true));

        Flag.CheckBox checkbox = seleniumSteps.find(checkbox(of(CHECKBOX_LABEL_TEXT6, CHECKBOX_LABEL_TEXT10)));

        assertThat(seleniumSteps.edit(valueOfThe(checkbox, true))
                        .getValue(ofThe(checkbox)),
                is(true));
    }

    @Test
    public void valueOfRadioButtonTest() {
        assertThat(seleniumSteps.getValue(ofThe(radioButton(RADIOBUTTON_LABEL_TEXT1))),
                is(false));

        assertThat(seleniumSteps.edit(valueOfThe(radioButton(RADIOBUTTON_LABEL_TEXT1), false))
                        .getValue(ofThe(flag(RADIOBUTTON_LABEL_TEXT1))),
                is(false));

        assertThat(seleniumSteps.edit(valueOfThe(radioButton(RADIOBUTTON_LABEL_TEXT1), true))
                        .getValue(ofThe(radioButton(RADIOBUTTON_LABEL_TEXT1))),
                is(true));

        seleniumSteps.edit(valueOfThe(radioButton(of(RADIOBUTTON_LABEL_TEXT7, RADIOBUTTON_LABEL_TEXT11)), true)
                .andValueOfThe(radioButton(RADIOBUTTON_LABEL_TEXT3), true)
                .andValueOfThe(radioButton(shouldBeVisible()), true));

        assertThat(seleniumSteps.getValue(ofThe(radioButton(of(RADIOBUTTON_LABEL_TEXT7, RADIOBUTTON_LABEL_TEXT11)))),
                is(true));

        assertThat(seleniumSteps.getValue(ofThe(radioButton(RADIOBUTTON_LABEL_TEXT3))),
                is(true));

        assertThat(seleniumSteps.getValue(ofThe(radioButton(shouldBeVisible()))),
                is(true));

        Flag.RadioButton radioButton = seleniumSteps.find(radioButton(of(RADIOBUTTON_LABEL_TEXT6, RADIOBUTTON_LABEL_TEXT10)));

        assertThat(seleniumSteps.edit(valueOfThe(radioButton, true))
                        .getValue(ofThe(radioButton)),
                is(true));
    }

    @Test
    public void valueOfSelectTest() {
        assertThat(seleniumSteps.getValue(ofThe(select(SELECT_LABEL_TEXT2))),
                emptyIterable());

        assertThat(seleniumSteps.edit(valueOfThe(select(SELECT_LABEL_TEXT2), OPTION_TEXT18)
                        .andValueOfThe(select(SELECT_LABEL_TEXT2), OPTION_TEXT16))
                        .getValue(ofThe(select(SELECT_LABEL_TEXT2))),
                contains(OPTION_TEXT16));

        seleniumSteps.edit(valueOfThe(select(of(SELECT_LABEL_TEXT1, SELECT_LABEL_TEXT9)), OPTION_TEXT27)
                .andValueOfThe(select(of(SELECT_LABEL_TEXT1, SELECT_LABEL_TEXT9)), OPTION_TEXT25)
                .andValueOfThe(select(of(SELECT_LABEL_TEXT1, SELECT_LABEL_TEXT9)), OPTION_TEXT26)
                .andValueOfThe(select(shouldHaveAttribute(ATTR1, VALUE4)), OPTION_TEXT5)
                .andValueOfThe(select(shouldContainElements(webElements(tagName(OPTION), OPTION_TEXT14))), OPTION_TEXT15)
                .andValueOfThe(select(shouldContainElements(webElements(tagName(OPTION), OPTION_TEXT14))), OPTION_TEXT13));

        assertThat(seleniumSteps.getValue(ofThe(select(of(SELECT_LABEL_TEXT1, SELECT_LABEL_TEXT9)))),
                contains(OPTION_TEXT25, OPTION_TEXT26, OPTION_TEXT27));

        assertThat(seleniumSteps.getValue(ofThe(select(shouldHaveAttribute(ATTR1, VALUE4)))),
                contains(OPTION_TEXT5));

        assertThat(seleniumSteps.getValue(ofThe(select(shouldContainElements(webElements(tagName(OPTION), OPTION_TEXT14))))),
                contains(OPTION_TEXT13));

        Select select = seleniumSteps.find(select(SELECT_LABEL_TEXT10));
        assertThat(seleniumSteps.edit(valueOfThe(select, OPTION_TEXT29))
                        .edit(valueOfThe(select, OPTION_TEXT28))
                        .getValue(ofThe(select)),
                contains(OPTION_TEXT28, OPTION_TEXT29));
    }

    @Test
    public void valueOfTable() {
        assertThat(seleniumSteps.getValue(ofThe(table())),
                allOf(hasEntry(is(HEADER_TEXT14), contains(CELL_TEXT38, CELL_TEXT41, CELL_TEXT44)),
                        hasEntry(is(HEADER_TEXT13), contains(CELL_TEXT37, CELL_TEXT40, CELL_TEXT43)),
                        hasEntry(is(HEADER_TEXT15), contains(CELL_TEXT39, CELL_TEXT42, CELL_TEXT45))));

        assertThat(seleniumSteps.getValue(ofThe(table(shouldHaveCssValue(CSS1, CSS_VALUE4)))),
                allOf(hasEntry(is(HEADER_TEXT4), contains(CELL_TEXT10, CELL_TEXT13, CELL_TEXT16)),
                        hasEntry(is(HEADER_TEXT5), contains(CELL_TEXT11, CELL_TEXT14, CELL_TEXT17)),
                        hasEntry(is(HEADER_TEXT6), contains(CELL_TEXT12, CELL_TEXT15, CELL_TEXT18))));

        Table table = seleniumSteps.find(table(TABLE_LABEL_TEXT3));

        assertThat(seleniumSteps.getValue(ofThe(table)),
                allOf(hasEntry(is(HEADER_TEXT19), contains(CELL_TEXT55, CELL_TEXT58, CELL_TEXT61)),
                        hasEntry(is(HEADER_TEXT20), contains(CELL_TEXT56, CELL_TEXT59, CELL_TEXT62)),
                        hasEntry(is(HEADER_TEXT21), contains(CELL_TEXT57, CELL_TEXT60, CELL_TEXT63))));
    }

    @Test
    public void valueOfRow() {
        assertThat(seleniumSteps.getValue(ofThe(tableRow())),
                contains(CELL_TEXT37, CELL_TEXT38, CELL_TEXT39));

        assertThat(seleniumSteps.getValue(ofThe(tableRow(shouldContainElements(buttons()))
                .foundFrom(table(of(TABLE_LABEL_TEXT5, TABLE_LABEL_TEXT9))))),
                contains(CELL_TEXT73, CELL_TEXT74, CELL_TEXT75));

        TableRow row = seleniumSteps.find(tableRow(shouldContainElements(textFields(ofMillis(5))))
                .foundFrom(table(of(TABLE_LABEL_TEXT5, TABLE_LABEL_TEXT9))));

        assertThat(seleniumSteps.getValue(ofThe(row)),
                contains(CELL_TEXT76, CELL_TEXT77, CELL_TEXT78));
    }
}
