package ru.tinkoff.qa.neptune.selenium.test.steps.tests.presence;

import org.openqa.selenium.NoSuchElementException;
import org.testng.annotations.Test;
import ru.tinkoff.qa.neptune.selenium.test.BaseWebDriverTest;

import static java.lang.String.format;
import static java.util.List.of;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static ru.tinkoff.qa.neptune.core.api.steps.Condition.condition;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.MultipleSearchSupplier.textFields;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.SearchSupplier.flag;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.SearchSupplier.tableRow;
import static ru.tinkoff.qa.neptune.selenium.test.FakeDOMModel.*;

public class PresenceTest extends BaseWebDriverTest {

    @Test
    public void positiveTestOfSearchSupplier() {
        var presence = seleniumSteps.presenceOf(flag().foundFrom(tableRow()
                .timeOut(FIVE_SECONDS)
                .criteria(condition(format("Contains %s, %s and %s", CELL_TEXT87, CELL_TEXT88, CELL_TEXT89), tableRow ->
                        tableRow.getValue().containsAll(of(CELL_TEXT87, CELL_TEXT88, CELL_TEXT89))))));

        assertThat(presence, is(true));
    }

    @Test
    public void negativeTestOfSearchSupplier() {
        var presence = seleniumSteps.presenceOf(flag().timeOut(FIVE_SECONDS).foundFrom(tableRow().timeOut(FIVE_SECONDS)
                .criteria(condition(format("Contains %s, %s and %s", CELL_TEXT49, CELL_TEXT50, CELL_TEXT51),
                        tableRow -> tableRow.getValue().containsAll(of(CELL_TEXT49, CELL_TEXT50, CELL_TEXT51))))));
        assertThat(presence, is(false));
    }

    @Test(expectedExceptions = NoSuchElementException.class, expectedExceptionsMessageRegExp = "Test exception")
    public void negativeTestOfSearchSupplierWithExceptionThrowing() {
        assertThat(seleniumSteps.presenceOf(flag()
                        .timeOut(FIVE_SECONDS)
                        .foundFrom(tableRow().timeOut(FIVE_SECONDS)
                                .criteria(condition(format("Contains %s, %s and %s", CELL_TEXT49, CELL_TEXT50, CELL_TEXT51),
                                        tableRow -> tableRow.getValue().containsAll(of(CELL_TEXT49, CELL_TEXT50, CELL_TEXT51))))),
                "Test exception"),
                is(false));
    }

    @Test
    public void positiveTestOfMultiSearchSupplier() {
        var presence = seleniumSteps.presenceOf(textFields().foundFrom(tableRow()
                .timeOut(FIVE_SECONDS)
                .criteria(condition(format("Contains %s, %s and %s", CELL_TEXT84, CELL_TEXT85, CELL_TEXT86), tableRow ->
                        tableRow.getValue().containsAll(of(CELL_TEXT84, CELL_TEXT85, CELL_TEXT86))))));

        assertThat(presence, is(true));
    }

    @Test
    public void negativeTestOfMultiSearchSupplier() {
        assertThat(seleniumSteps.presenceOf(textFields()
                        .timeOut(FIVE_SECONDS)
                        .foundFrom(tableRow()
                                .timeOut(FIVE_SECONDS)
                                .criteria(condition(format("Contains %s, %s and %s", CELL_TEXT22, CELL_TEXT23, CELL_TEXT24),
                                        tableRow -> tableRow.getValue().containsAll(of(CELL_TEXT22, CELL_TEXT23, CELL_TEXT24)))))),
                is(false));
    }

    @Test(expectedExceptions = IllegalStateException.class, expectedExceptionsMessageRegExp = "Test exception")
    public void negativeTestOfMultiSearchSupplierWithExceptionThrowing() {
        assertThat(seleniumSteps.presenceOf(textFields()
                        .timeOut(FIVE_SECONDS)
                        .foundFrom(tableRow()
                                .timeOut(FIVE_SECONDS)
                                .criteria(condition(format("Contains %s, %s and %s", CELL_TEXT22, CELL_TEXT23, CELL_TEXT24), tableRow ->
                                        tableRow.getValue().containsAll(of(CELL_TEXT22, CELL_TEXT23, CELL_TEXT24))))),
                "Test exception"),
                is(false));
    }
}
