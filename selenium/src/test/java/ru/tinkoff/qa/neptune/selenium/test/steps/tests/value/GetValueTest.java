package ru.tinkoff.qa.neptune.selenium.test.steps.tests.value;

import org.testng.annotations.Test;
import ru.tinkoff.qa.neptune.selenium.api.widget.drafts.*;
import ru.tinkoff.qa.neptune.selenium.test.BaseWebDriverTest;

import static java.lang.String.format;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.openqa.selenium.By.tagName;
import static ru.tinkoff.qa.neptune.core.api.steps.Criteria.NOT;
import static ru.tinkoff.qa.neptune.core.api.steps.Criteria.condition;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.CommonElementCriteria.*;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.MultipleSearchSupplier.*;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.SearchSupplier.*;
import static ru.tinkoff.qa.neptune.selenium.test.FakeDOMModel.*;

public class GetValueTest extends BaseWebDriverTest {

    @Test
    public void valueOfTextFieldTest() {
        assertThat(seleniumSteps.valueOf(textField(INPUT_LABEL_TEXT1)),
                is(INPUT_TEXT5));

        assertThat(seleniumSteps.edit(textField(INPUT_LABEL_TEXT1), INPUT_TEXT4)
                        .valueOf(textField(INPUT_LABEL_TEXT1)),
                is(INPUT_TEXT4));

        assertThat(seleniumSteps.valueOf(textField().criteria(attr(ATTR8, VALUE4))),
                emptyOrNullString());

        assertThat(seleniumSteps
                        .edit(textField().criteria(attr(ATTR8, VALUE4)), INPUT_TEXT1)
                        .edit(textField(INPUT_LABEL_TEXT8, INPUT_LABEL_TEXT12), INPUT_TEXT2)
                        .valueOf(textField().criteria(attr(ATTR8, VALUE4))),
                is(INPUT_TEXT1));

        assertThat(seleniumSteps.valueOf(textField(INPUT_LABEL_TEXT8, INPUT_LABEL_TEXT12)),
                is(INPUT_TEXT2));

        TextField nestedTextField = seleniumSteps.find(textField(INPUT_LABEL_TEXT5)
                .foundFrom(table(TABLE_LABEL_TEXT5, TABLE_LABEL_TEXT9)));

        assertThat(seleniumSteps.edit(nestedTextField, INPUT_TEXT7)
                        .valueOf(nestedTextField),
                is(INPUT_TEXT7));
    }

    @Test
    public void valueOfFlagTest() {
        assertThat(seleniumSteps.valueOf(flag(RADIOBUTTON_LABEL_TEXT4)),
                is(false));

        assertThat(seleniumSteps.edit(flag(RADIOBUTTON_LABEL_TEXT4), false)
                        .valueOf(flag(RADIOBUTTON_LABEL_TEXT4)),
                is(false));

        assertThat(seleniumSteps.edit(flag(RADIOBUTTON_LABEL_TEXT4), true)
                        .valueOf(flag(RADIOBUTTON_LABEL_TEXT4)),
                is(true));

        seleniumSteps.edit(flag(CHECKBOX_LABEL_TEXT8, CHECKBOX_LABEL_TEXT12), true)
                .edit(flag(RADIOBUTTON_LABEL_TEXT2), true)
                .edit(flag().criteria(enabled()), true);

        assertThat(seleniumSteps.valueOf(flag(CHECKBOX_LABEL_TEXT8, CHECKBOX_LABEL_TEXT12)),
                is(true));

        assertThat(seleniumSteps.valueOf(flag(RADIOBUTTON_LABEL_TEXT2)),
                is(true));

        assertThat(seleniumSteps.valueOf(flag()
                        .criteria(enabled())),
                is(true));

        Flag nestedFlag = seleniumSteps.find(flag(CHECKBOX_LABEL_TEXT5, CHECKBOX_LABEL_TEXT9)
                .foundFrom(table(TABLE_LABEL_TEXT5, TABLE_LABEL_TEXT9)));

        assertThat(seleniumSteps.edit(nestedFlag, true)
                        .valueOf(nestedFlag),
                is(true));
    }

    @Test
    public void valueOfCheckBoxTest() {
        assertThat(seleniumSteps.valueOf(checkbox(CHECKBOX_LABEL_TEXT4)),
                is(false));

        assertThat(seleniumSteps.edit(checkbox(CHECKBOX_LABEL_TEXT4), false)
                        .valueOf(flag(CHECKBOX_LABEL_TEXT4)),
                is(false));

        assertThat(seleniumSteps.edit(checkbox(CHECKBOX_LABEL_TEXT4), true)
                        .valueOf(checkbox(CHECKBOX_LABEL_TEXT4)),
                is(true));

        seleniumSteps.edit(checkbox(CHECKBOX_LABEL_TEXT7, CHECKBOX_LABEL_TEXT11), true)
                .edit(checkbox(CHECKBOX_LABEL_TEXT3), true)
                .edit(checkbox().criteria(NOT(enabled())), true);

        assertThat(seleniumSteps.valueOf(checkbox(CHECKBOX_LABEL_TEXT7, CHECKBOX_LABEL_TEXT11)),
                is(true));

        assertThat(seleniumSteps.valueOf(checkbox(CHECKBOX_LABEL_TEXT3)),
                is(true));

        assertThat(seleniumSteps.valueOf(flag().criteria(NOT(enabled()))),
                is(true));

        Flag.CheckBox checkbox = seleniumSteps.find(checkbox(CHECKBOX_LABEL_TEXT6, CHECKBOX_LABEL_TEXT10));

        assertThat(seleniumSteps.edit(checkbox, true)
                        .valueOf(checkbox),
                is(true));
    }

    @Test
    public void valueOfRadioButtonTest() {
        assertThat(seleniumSteps.valueOf(radioButton(RADIOBUTTON_LABEL_TEXT1)),
                is(false));

        assertThat(seleniumSteps.edit(radioButton(RADIOBUTTON_LABEL_TEXT1), false)
                        .valueOf(flag(RADIOBUTTON_LABEL_TEXT1)),
                is(false));

        assertThat(seleniumSteps.edit(radioButton(RADIOBUTTON_LABEL_TEXT1), true)
                        .valueOf(radioButton(RADIOBUTTON_LABEL_TEXT1)),
                is(true));

        seleniumSteps.edit(radioButton(RADIOBUTTON_LABEL_TEXT7, RADIOBUTTON_LABEL_TEXT11), true)
                .edit(radioButton(RADIOBUTTON_LABEL_TEXT3), true)
                .edit(radioButton().criteria(visible()), true);

        assertThat(seleniumSteps.valueOf(radioButton(RADIOBUTTON_LABEL_TEXT7, RADIOBUTTON_LABEL_TEXT11)),
                is(true));

        assertThat(seleniumSteps.valueOf(radioButton(RADIOBUTTON_LABEL_TEXT3)),
                is(true));

        assertThat(seleniumSteps.valueOf(radioButton().criteria(visible())),
                is(true));

        Flag.RadioButton radioButton = seleniumSteps.find(radioButton(RADIOBUTTON_LABEL_TEXT6, RADIOBUTTON_LABEL_TEXT10));

        assertThat(seleniumSteps.edit(radioButton, true).valueOf(radioButton),
                is(true));
    }

    @Test
    public void valueOfSelectTest() {
        assertThat(seleniumSteps.valueOf(select(SELECT_LABEL_TEXT2)),
                emptyIterable());

        assertThat(seleniumSteps.edit(select(SELECT_LABEL_TEXT2), OPTION_TEXT18)
                        .edit(select(SELECT_LABEL_TEXT2), OPTION_TEXT16)
                        .valueOf(select(SELECT_LABEL_TEXT2)),
                contains(OPTION_TEXT16));

        seleniumSteps.edit(select(SELECT_LABEL_TEXT1, SELECT_LABEL_TEXT9), OPTION_TEXT27)
                .edit(select(SELECT_LABEL_TEXT1, SELECT_LABEL_TEXT9), OPTION_TEXT25)
                .edit(select(SELECT_LABEL_TEXT1, SELECT_LABEL_TEXT9), OPTION_TEXT26)
                .edit(select().criteria(attr(ATTR1, VALUE4)), OPTION_TEXT5)
                .edit(select().criteria(nested(webElements(tagName(OPTION)).criteria(text(OPTION_TEXT14)))), OPTION_TEXT15)
                .edit(select().criteria(nested(webElements(tagName(OPTION)).criteria(text(OPTION_TEXT14)))), OPTION_TEXT13);

        assertThat(seleniumSteps.valueOf(select(SELECT_LABEL_TEXT1, SELECT_LABEL_TEXT9)),
                contains(OPTION_TEXT25, OPTION_TEXT26, OPTION_TEXT27));

        assertThat(seleniumSteps.valueOf(select().criteria(attr(ATTR1, VALUE4))),
                contains(OPTION_TEXT5));

        assertThat(seleniumSteps.valueOf(select().criteria(nested(webElements(tagName(OPTION))
                        .criteria(text(OPTION_TEXT14))))),
                contains(OPTION_TEXT13));

        Select select = seleniumSteps.find(select(SELECT_LABEL_TEXT10));
        assertThat(seleniumSteps.edit(select, OPTION_TEXT29)
                        .edit(select, OPTION_TEXT28)
                        .valueOf(select),
                contains(OPTION_TEXT28, OPTION_TEXT29));
    }

    @Test
    public void valueOfTableTest() {
        assertThat(seleniumSteps.valueOf(table()),
                allOf(hasEntry(is(HEADER_TEXT14), contains(CELL_TEXT38, CELL_TEXT41, CELL_TEXT44)),
                        hasEntry(is(HEADER_TEXT13), contains(CELL_TEXT37, CELL_TEXT40, CELL_TEXT43)),
                        hasEntry(is(HEADER_TEXT15), contains(CELL_TEXT39, CELL_TEXT42, CELL_TEXT45))));

        assertThat(seleniumSteps.valueOf(table()
                        .criteria(css(CSS1, CSS_VALUE4))),
                allOf(hasEntry(is(HEADER_TEXT4), contains(CELL_TEXT10, CELL_TEXT13, CELL_TEXT16)),
                        hasEntry(is(HEADER_TEXT5), contains(CELL_TEXT11, CELL_TEXT14, CELL_TEXT17)),
                        hasEntry(is(HEADER_TEXT6), contains(CELL_TEXT12, CELL_TEXT15, CELL_TEXT18))));

        Table table = seleniumSteps.find(table(TABLE_LABEL_TEXT3));

        assertThat(seleniumSteps.valueOf(table),
                allOf(hasEntry(is(HEADER_TEXT19), contains(CELL_TEXT55, CELL_TEXT58, CELL_TEXT61)),
                        hasEntry(is(HEADER_TEXT20), contains(CELL_TEXT56, CELL_TEXT59, CELL_TEXT62)),
                        hasEntry(is(HEADER_TEXT21), contains(CELL_TEXT57, CELL_TEXT60, CELL_TEXT63))));
    }

    @Test
    public void valueOfRowTest() {
        assertThat(seleniumSteps.valueOf(tableRow()),
                contains(CELL_TEXT37, CELL_TEXT38, CELL_TEXT39));

        assertThat(seleniumSteps.valueOf(tableRow()
                        .criteria(nested(buttons()))
                        .foundFrom(table(TABLE_LABEL_TEXT5, TABLE_LABEL_TEXT9))),
                contains(CELL_TEXT73, CELL_TEXT74, CELL_TEXT75));

        TableRow row = seleniumSteps.find(tableRow()
                .criteria(nested(textFields()))
                .foundFrom(table(TABLE_LABEL_TEXT5, TABLE_LABEL_TEXT9)));

        assertThat(seleniumSteps.valueOf(row),
                contains(CELL_TEXT76, CELL_TEXT77, CELL_TEXT78));
    }

    @Test
    public void valueOfHeaderTest() {
        assertThat(seleniumSteps.valueOf(tableHeader()),
                contains(HEADER_TEXT13, HEADER_TEXT14, HEADER_TEXT15));

        assertThat(seleniumSteps.valueOf(tableHeader()
                        .criteria(condition(format("Contains value %s", HEADER_TEXT16), tableHeader -> tableHeader
                                .getValue().contains(HEADER_TEXT16)))),
                contains(HEADER_TEXT16, HEADER_TEXT17, HEADER_TEXT18));

        assertThat(seleniumSteps.valueOf(tableHeader()
                        .timeOut(FIVE_SECONDS)
                        .foundFrom(table().criteria(nested(textFields(INPUT_LABEL_TEXT10))))),
                contains(HEADER_TEXT28, HEADER_TEXT29, HEADER_TEXT30));

        TableHeader header = seleniumSteps.find(tableHeader()
                .timeOut(FIVE_SECONDS)
                .criteria(condition(format("Contains value %s", HEADER_TEXT22), tableHeader -> tableHeader.getValue().contains(HEADER_TEXT22)))
                .foundFrom(table(TABLE_LABEL_TEXT4)));

        assertThat(seleniumSteps.valueOf(header),
                contains(HEADER_TEXT22, HEADER_TEXT23, HEADER_TEXT24));
    }

    @Test
    public void valueOfFooterTest() {
        assertThat(seleniumSteps.valueOf(tableFooter()),
                contains(FOOTER_TEXT13, FOOTER_TEXT14, FOOTER_TEXT15));

        assertThat(seleniumSteps.valueOf(tableFooter()
                        .criteria(condition(format("Contains value %s", FOOTER_TEXT1), tableFooter ->
                                tableFooter.getValue().contains(FOOTER_TEXT1)))),
                contains(FOOTER_TEXT1, FOOTER_TEXT2, FOOTER_TEXT3));

        assertThat(seleniumSteps.valueOf(tableFooter()
                        .timeOut(FIVE_SECONDS)
                        .foundFrom(table().criteria(nested(textFields(INPUT_LABEL_TEXT6, INPUT_LABEL_TEXT10))))),
                contains(FOOTER_TEXT28, FOOTER_TEXT29, FOOTER_TEXT30));

        TableFooter footer = seleniumSteps.find(tableFooter()
                .timeOut(FIVE_SECONDS)
                .criteria(condition(format("Contains value %s", FOOTER_TEXT22), tableFooter ->
                        tableFooter.getValue().contains(FOOTER_TEXT22)))
                .foundFrom(table(TABLE_LABEL_TEXT4)));

        assertThat(seleniumSteps.valueOf(footer),
                contains(FOOTER_TEXT22, FOOTER_TEXT23, FOOTER_TEXT24));
    }

    @Test
    public void valueOfCellTest() {
        assertThat(seleniumSteps.valueOf(tableCell()),
                is(CELL_TEXT37));

        assertThat(seleniumSteps.valueOf(tableCell()
                        .criteria(nested(textFields(INPUT_LABEL_TEXT6, INPUT_LABEL_TEXT10)))
                        .foundFrom(table(TABLE_LABEL_TEXT6))),
                is(CELL_TEXT84));

        TableCell cell = seleniumSteps
                .find(tableCell()
                        .criteria(nested(checkBoxes(CHECKBOX_LABEL_TEXT6, CHECKBOX_LABEL_TEXT10)))
                        .foundFrom(table(TABLE_LABEL_TEXT6)));
        assertThat(seleniumSteps.valueOf(cell),
                is(CELL_TEXT87));
    }
}
