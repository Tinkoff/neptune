package ru.tinkoff.qa.neptune.core.api.steps.selections;

import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.testng.Assert.fail;
import static ru.tinkoff.qa.neptune.core.api.hamcrest.throwable.ThrowableMessageMatcher.throwableHasMessage;
import static ru.tinkoff.qa.neptune.core.api.steps.Criteria.condition;
import static ru.tinkoff.qa.neptune.core.api.steps.selections.ItemsCountCondition.*;
import static ru.tinkoff.qa.neptune.core.api.steps.selections.SelectionOfItems.selectList;

public class ListResultSelectionTest extends SelectionsPreparations {

    @Test
    public void listPositiveTestSizeEquals() {
        var result = selectList().ofCount(5).evaluate(OBJECT_LIST);
        assertThat(result, contains(1,
            2,
            true,
            false,
            "ABCD"));
    }

    @Test
    public void listPositiveTestIndexes() {
        var result = selectList().indexes(1, 3, 5).evaluate(OBJECT_LIST);
        assertThat(result, contains(2,
            false,
            "EFG"));
    }

    @Test(dataProvider = "positiveSizeConditions")
    public void listPositiveTestSizeCondition(ItemsCountCondition sizeCondition) {
        var result = selectList()
            .whenCount(sizeCondition)
            .evaluate(OBJECT_LIST);
        assertThat(result, equalTo(OBJECT_LIST));
    }

    @Test
    public void listPositiveTestBeforeIndex() {
        var result = selectList()
            .beforeIndex(6)
            .evaluate(OBJECT_LIST);
        assertThat(result, contains(1,
            2,
            true,
            false,
            "ABCD",
            "EFG"));
    }

    @Test
    public void listPositiveTestAfterIndex() {
        var result = selectList()
            .afterIndex(6)
            .evaluate(OBJECT_LIST);
        assertThat(result, contains(5.2));
    }

    @Test
    public void listPositiveTestOfCondition() {
        var result = selectList()
            .onCondition(condition(
                "contains a list",
                objects -> objects.contains(List.of(2,
                    true,
                    false,
                    "ABCD",
                    "EFG"))))
            .evaluate(OBJECT_LIST);
        assertThat(result, equalTo(OBJECT_LIST));
    }

    @Test
    public void listPositiveTestCombined() {
        var result = selectList()
            .onCondition(condition(
                "contains a list",
                objects -> objects.contains(List.of(2,
                    true,
                    false,
                    "ABCD",
                    "EFG"))))
            .whenCount(isLesserOrEqual(9).andGreaterOrEqual(8))
            .afterIndex(6)
            .beforeIndex(6)
            .ofCount(3)
            .evaluate(OBJECT_LIST);
        assertThat(result, contains(false,
            "ABCD",
            "EFG"));
    }

    @Test
    public void listPositiveTestCombined2() {
        var result = selectList()
            .onCondition(condition(
                "contains a list",
                objects -> objects.contains(List.of(2,
                    true,
                    false,
                    "ABCD",
                    "EFG"))))
            .whenCount(isGreaterOrEqual(8))
            .beforeIndex(3)
            .afterIndex(3)
            .ofCount(3)
            .evaluate(OBJECT_LIST);
        assertThat(result, contains("ABCD",
            "EFG",
            List.of(2,
                true,
                false,
                "ABCD",
                "EFG")));
    }

    @Test
    public void listPositiveTestCombined3() {
        var result = selectList()
            .onCondition(condition(
                "contains a list",
                objects -> objects.contains(List.of(2,
                    true,
                    false,
                    "ABCD",
                    "EFG"))))
            .whenCount(isLesserOrEqual(9).andGreaterOrEqual(8))
            .afterIndex(6)
            .beforeIndex(6)
            .ofCount(3)
            .indexes(1, 3, 5)
            .evaluate(OBJECT_LIST);
        assertThat(result, contains(2,
            false,
            "EFG"));
    }

    @Test
    public void listPositiveTestCombined4() {
        var result = selectList()
            .onCondition(condition(
                "contains a list",
                objects -> objects.contains(List.of(2,
                    true,
                    false,
                    "ABCD",
                    "EFG"))))
            .whenCount(isGreaterOrEqual(8))
            .indexes(1, 3, 5)
            .beforeIndex(3)
            .afterIndex(3)
            .ofCount(3)
            .evaluate(OBJECT_LIST);
        assertThat(result, contains("ABCD",
            "EFG",
            List.of(2,
                true,
                false,
                "ABCD",
                "EFG")));
    }

    @Test
    public void negativeTestNull() {
        var selection = selectList().ofCount(10);
        var result = selection.evaluate(null);
        assertThat(result, nullValue());
        assertThat(selection.mismatchMessage(), equalTo(
            "It is not possible to select resulted items because:\r\n" +
                "Set of got items was null"));
    }

    @Test
    public void negativeTestEmpty() {
        var selection = selectList().ofCount(10);
        var result = selection.evaluate(new ArrayList<>());
        assertThat(result, nullValue());
        assertThat(selection.mismatchMessage(), equalTo(
            "It is not possible to select resulted items because:\r\n" +
                "Set of got items was empty"));
    }

    @Test
    public void negativeTestSizeEquals() {
        var selection = selectList().ofCount(10);
        var result = selection.evaluate(OBJECT_LIST);
        assertThat(result, nullValue());
        assertThat(selection.mismatchMessage(), equalTo(
            "It is not possible to select resulted items because:\r\n" +
                "Max index [9] is out of got items count [8]"));
    }

    @Test
    public void negativeTestIndexes() {
        var selection = selectList().indexes(1, 2, 8, 9);
        var result = selection.evaluate(OBJECT_LIST);
        assertThat(result, nullValue());
        assertThat(selection.mismatchMessage(), equalTo(
            "It is not possible to select resulted items because:\r\n" +
                "Max index [9] is out of got items count [8]"));
    }

    @Test(dataProvider = "negativeSizeConditions")
    public void negativeTestSizeConditional(ItemsCountCondition sizeCondition,
                                            String mismatchDescription) {
        var selection = selectList()
            .whenCount(sizeCondition);
        var result = selection.evaluate(OBJECT_LIST);
        assertThat(result, nullValue());
        assertThat(selection.mismatchMessage(), equalTo(mismatchDescription));
    }

    @Test
    public void listNegativeTestBeforeIndex() {
        var selection = selectList()
            .beforeIndex(10);

        var result = selection.evaluate(OBJECT_LIST);
        assertThat(result, nullValue());
        assertThat(selection.mismatchMessage(), equalTo(
            "It is not possible to select resulted items because:\r\n" +
                "Max index [9] is out of got items count [8]"));
    }

    @Test
    public void listNegativeTestAfterIndex() {
        var selection = selectList()
            .afterIndex(10);

        var result = selection.evaluate(OBJECT_LIST);
        assertThat(result, nullValue());
        assertThat(selection.mismatchMessage(), equalTo(
            "It is not possible to select resulted items because:\r\n" +
                "Min index [11] is out of got items count [8]"));
    }

    @Test
    public void listNegativeTestOfCondition() {
        var selection = selectList()
            .onCondition(condition(
                "contains a list",
                objects -> objects.contains(List.of(2,
                    true,
                    false,
                    "ABCD",
                    "EFG"))))
            .onCondition(condition(
                "does not contain a list",
                objects -> !objects.contains(List.of(2,
                    true,
                    false,
                    "ABCD",
                    "EFG"))));

        var result = selection.evaluate(OBJECT_LIST);
        assertThat(result, nullValue());
        assertThat(selection.mismatchMessage(), equalTo(
            "It is not possible to select resulted items because:\r\n" +
                "Set of got items doesnt match 'does not contain a list'"));
    }

    @Test
    public void listNegativeTestCombined() {
        var selection = selectList()
            .onCondition(condition(
                "contains a list",
                objects -> objects.contains(List.of(2,
                    true,
                    false,
                    "ABCD",
                    "EFG"))))
            .onCondition(condition(
                "does not contain a list",
                objects -> !objects.contains(List.of(2,
                    true,
                    false,
                    "ABCD",
                    "EFG"))))
            .whenCount(isEqual(10))
            .afterIndex(6)
            .beforeIndex(10)
            .ofCount(4);

        var result = selection.evaluate(OBJECT_LIST);
        assertThat(result, nullValue());
        assertThat(selection.mismatchMessage(), equalTo(
            "It is not possible to select resulted items because:\r\n" +
                "Count [8] of got items doesnt match 'equal 10'"));
    }

    @Test
    public void listNegativeTestCombined2() {
        var selection = selectList()
            .onCondition(condition(
                "contains a list",
                objects -> objects.contains(List.of(2,
                    true,
                    false,
                    "ABCD",
                    "EFG"))))
            .onCondition(condition(
                "does not contain a list",
                objects -> !objects.contains(List.of(2,
                    true,
                    false,
                    "ABCD",
                    "EFG"))))
            .afterIndex(6)
            .beforeIndex(10)
            .ofCount(4);

        var result = selection.evaluate(OBJECT_LIST);
        assertThat(result, nullValue());
        assertThat(selection.mismatchMessage(), equalTo(
            "It is not possible to select resulted items because:\r\n" +
                "Set of got items doesnt match 'does not contain a list'"));
    }

    @Test
    public void listNegativeTestCombined3() {
        var selection = selectList()
            .afterIndex(6)
            .beforeIndex(10)
            .ofCount(4);

        var result = selection.evaluate(OBJECT_LIST);
        assertThat(result, nullValue());
        assertThat(selection.mismatchMessage(), equalTo(
            "It is not possible to select resulted items because:\r\n" +
                "Max index [9] is out of got items count [8]"));
    }

    @Test
    public void listNegativeTestCombined4() {
        var selection = selectList()
            .afterIndex(6)
            .ofCount(4);

        var result = selection.evaluate(OBJECT_LIST);
        assertThat(result, nullValue());
        assertThat(selection.mismatchMessage(), equalTo(
            "It is not possible to select resulted items because:\r\n" +
                "Max index [10] is out of got items count [8]"));
    }

    @Test
    public void listNegativeTestOfInvalidParameters() {
        var selection = selectList();

        try {
            selection.validateParameters();
        } catch (IllegalArgumentException e) {
            assertThat(e, throwableHasMessage("Any parameter should be defined"));
            return;
        }

        fail("Exception was expected");
    }

    @Test
    public void listNegativeTestOfInvalidParameters2() {
        var selection = selectList()
            .ofCount(10)
            .whenCount(isEqual(6));

        try {
            selection.validateParameters();
        } catch (IllegalArgumentException e) {
            assertThat(e,
                throwableHasMessage("Parameters are not valid, because max index[9]  " +
                    "is out of expected item count 6"));
            return;
        }

        fail("Exception was expected");
    }

    @Test
    public void listNegativeTestOfInvalidParameters3() {
        var selection = selectList()
            .ofCount(2)
            .whenCount(isEqual(6))
            .afterIndex(6);

        try {
            selection.validateParameters();
        } catch (IllegalArgumentException e) {
            assertThat(e,
                throwableHasMessage("Parameters are not valid, because min index[7]  " +
                    "is out of expected item count 6"));
            return;
        }

        fail("Exception was expected");
    }

    @Test
    public void listNegativeTestOfInvalidParameters4() {
        var selection = selectList()
            .ofCount(5)
            .whenCount(isGreater(2))
            .afterIndex(6);

        try {
            selection.validateParameters();
        } catch (IllegalArgumentException e) {
            assertThat(e,
                throwableHasMessage("Parameters are not valid, because min index[7]  " +
                    "is out of expected minimal item count 3"));
            return;
        }

        fail("Exception was expected");
    }

    @Test
    public void listNegativeTestOfInvalidParameters5() {
        var selection = selectList()
            .ofCount(5)
            .whenCount(isGreater(13).andLesser(15))
            .afterIndex(12);

        try {
            selection.validateParameters();
        } catch (IllegalArgumentException e) {
            assertThat(e,
                throwableHasMessage("Parameters are not valid, because max index[17]  " +
                    "is out of expected maximal item count 14"));
            return;
        }

        fail("Exception was expected");
    }
}
