package ru.tinkoff.qa.neptune.selenium.test.steps.tests.presence;

import ru.tinkoff.qa.neptune.selenium.test.BaseWebDriverTest;
import org.testng.annotations.Test;

import static ru.tinkoff.qa.neptune.core.api.steps.StoryWriter.condition;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.presence.ElementPresence.presenceOfAnElement;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.presence.ElementPresence.presenceOfElements;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.MultipleSearchSupplier.textFields;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.SearchSupplier.flag;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.SearchSupplier.tableRow;
import static ru.tinkoff.qa.neptune.selenium.test.FakeDOMModel.*;
import static java.lang.String.format;
import static java.util.List.of;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class PresenceTest extends BaseWebDriverTest {

    @Test
    public void positiveTestOfSearchSupplier() {
        assertThat(seleniumSteps.get(presenceOfAnElement(flag()
                        .foundFrom(tableRow()
                                .timeOut(FIVE_SECONDS)
                                .criteria(condition(format("Contains %s, %s and %s", CELL_TEXT87, CELL_TEXT88, CELL_TEXT89), tableRow ->
                                        tableRow.getValue().containsAll(of(CELL_TEXT87, CELL_TEXT88, CELL_TEXT89))))))),
                is(true));
    }

    @Test
    public void negativeTestOfSearchSupplier() {
        assertThat(seleniumSteps.get(presenceOfAnElement(flag()
                        .timeOut(FIVE_SECONDS)
                        .foundFrom(tableRow().timeOut(FIVE_SECONDS)
                                .criteria(condition(format("Contains %s, %s and %s", CELL_TEXT49, CELL_TEXT50, CELL_TEXT51),
                                        tableRow -> tableRow.getValue().containsAll(of(CELL_TEXT49, CELL_TEXT50, CELL_TEXT51))))))),
                is(false));
    }

    @Test
    public void positiveTestOfMultiSearchSupplier() {
        assertThat(seleniumSteps
                        .get(presenceOfElements(textFields()
                                .foundFrom(tableRow()
                                        .timeOut(FIVE_SECONDS)
                                        .criteria(condition(format("Contains %s, %s and %s", CELL_TEXT84, CELL_TEXT85, CELL_TEXT86), tableRow ->
                                                tableRow.getValue().containsAll(of(CELL_TEXT84, CELL_TEXT85, CELL_TEXT86))))))),
                is(true));
    }

    @Test
    public void negativeTestOfMultiSearchSupplier() {
        assertThat(seleniumSteps
                        .get(presenceOfElements(textFields()
                                .timeOut(FIVE_SECONDS)
                                .foundFrom(tableRow()
                                        .timeOut(FIVE_SECONDS)
                                        .criteria(condition(format("Contains %s, %s and %s", CELL_TEXT22, CELL_TEXT23, CELL_TEXT24), tableRow ->
                                                tableRow.getValue().containsAll(of(CELL_TEXT22, CELL_TEXT23, CELL_TEXT24))))))),
                is(false));
    }
}
