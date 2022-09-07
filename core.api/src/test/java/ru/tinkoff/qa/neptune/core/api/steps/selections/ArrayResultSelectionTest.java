package ru.tinkoff.qa.neptune.core.api.steps.selections;

import org.apache.commons.lang3.ArrayUtils;
import org.testng.annotations.Test;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.testng.Assert.fail;
import static ru.tinkoff.qa.neptune.core.api.hamcrest.throwable.ThrowableMessageMatcher.throwableHasMessage;
import static ru.tinkoff.qa.neptune.core.api.steps.Criteria.condition;
import static ru.tinkoff.qa.neptune.core.api.steps.selections.ItemsCountCondition.*;
import static ru.tinkoff.qa.neptune.core.api.steps.selections.SelectionOfItems.selectArray;

public class ArrayResultSelectionTest extends SelectionsPreparations {

    @Test
    public void arrayPositiveTestSizeEquals() {
        var result = selectArray().ofCount(5).evaluate(OBJECT_ARRAY);
        assertThat(result, arrayContaining(1,
            2,
            true,
            false,
            "ABCD"));
    }

    @Test
    public void arrayPositiveTestIndexes() {
        var result = selectArray().indexes(1, 3, 5).evaluate(OBJECT_ARRAY);
        assertThat(result, arrayContaining(2,
            false,
            "EFG"));
    }

    @Test(dataProvider = "positiveSizeConditions")
    public void arrayPositiveTestSizeCondition(ItemsCountCondition sizeCondition) {
        var result = selectArray()
            .whenCount(sizeCondition)
            .evaluate(OBJECT_ARRAY);
        assertThat(result, equalTo(OBJECT_ARRAY));
    }

    @Test
    public void arrayPositiveTestBeforeIndex() {
        var result = selectArray()
            .beforeIndex(6)
            .evaluate(OBJECT_ARRAY);
        assertThat(result, arrayContaining(1,
            2,
            true,
            false,
            "ABCD",
            "EFG"));
    }

    @Test
    public void arrayPositiveTestAfterIndex() {
        var result = selectArray()
            .afterIndex(6)
            .evaluate(OBJECT_ARRAY);
        assertThat(result, arrayContaining(5.2));
    }

    @Test
    public void arrayPositiveTestOfCondition() {
        var result = selectArray()
            .onCondition(condition(
                "contains a list",
                objects -> ArrayUtils.contains(objects, List.of(2,
                    true,
                    false,
                    "ABCD",
                    "EFG"))))
            .evaluate(OBJECT_ARRAY);
        assertThat(result, equalTo(OBJECT_ARRAY));
    }

    @Test
    public void arrayPositiveTestCombined() {
        var result = selectArray()
            .onCondition(condition(
                "contains a list",
                objects -> ArrayUtils.contains(objects, List.of(2,
                    true,
                    false,
                    "ABCD",
                    "EFG"))))
            .whenCount(isLesserOrEqual(9).andGreaterOrEqual(8))
            .afterIndex(6)
            .beforeIndex(6)
            .ofCount(3)
            .evaluate(OBJECT_ARRAY);
        assertThat(result, arrayContaining(false,
            "ABCD",
            "EFG"));
    }

    @Test
    public void arrayPositiveTestCombined2() {
        var result = selectArray()
            .onCondition(condition(
                "contains a list",
                objects -> ArrayUtils.contains(objects, List.of(2,
                    true,
                    false,
                    "ABCD",
                    "EFG"))))
            .whenCount(isGreaterOrEqual(8))
            .beforeIndex(3)
            .afterIndex(3)
            .ofCount(3)
            .evaluate(OBJECT_ARRAY);
        assertThat(result, arrayContaining("ABCD",
            "EFG",
            List.of(2,
                true,
                false,
                "ABCD",
                "EFG")));
    }

    @Test
    public void arrayPositiveTestCombined3() {
        var result = selectArray()
            .onCondition(condition(
                "contains a list",
                objects -> ArrayUtils.contains(objects, List.of(2,
                    true,
                    false,
                    "ABCD",
                    "EFG"))))
            .whenCount(isLesserOrEqual(9).andGreaterOrEqual(8))
            .afterIndex(6)
            .beforeIndex(6)
            .ofCount(3)
            .indexes(1, 3, 5)
            .evaluate(OBJECT_ARRAY);
        assertThat(result, arrayContaining(2,
            false,
            "EFG"));
    }

    @Test
    public void arrayPositiveTestCombined4() {
        var result = selectArray()
            .onCondition(condition(
                "contains a list",
                objects -> ArrayUtils.contains(objects, List.of(2,
                    true,
                    false,
                    "ABCD",
                    "EFG"))))
            .whenCount(isGreaterOrEqual(8))
            .indexes(1, 3, 5)
            .beforeIndex(3)
            .afterIndex(3)
            .ofCount(3)
            .evaluate(OBJECT_ARRAY);
        assertThat(result, arrayContaining("ABCD",
            "EFG",
            List.of(2,
                true,
                false,
                "ABCD",
                "EFG")));
    }

    @Test
    public void negativeTestNull() {
        var selection = selectArray().ofCount(10);
        var result = selection.evaluate(null);
        assertThat(result, nullValue());
        assertThat(selection.mismatchMessage(), equalTo(
            "It is not possible to select resulted items because:\r\n" +
                "Set of got items was null"));
    }

    @Test
    public void negativeTestEmpty() {
        var selection = selectArray().ofCount(10);
        var result = selection.evaluate(new Object[0]);
        assertThat(result, nullValue());
        assertThat(selection.mismatchMessage(), equalTo(
            "It is not possible to select resulted items because:\r\n" +
                "Set of got items was empty"));
    }

    @Test
    public void negativeTestSizeEquals() {
        var selection = selectArray().ofCount(10);
        var result = selection.evaluate(OBJECT_ARRAY);
        assertThat(result, nullValue());
        assertThat(selection.mismatchMessage(), equalTo(
            "It is not possible to select resulted items because:\r\n" +
                "Max index [9] is out of got items count [8]"));
    }

    @Test
    public void negativeTestIndexes() {
        var selection = selectArray().indexes(1, 2, 8, 9);
        var result = selection.evaluate(OBJECT_ARRAY);
        assertThat(result, nullValue());
        assertThat(selection.mismatchMessage(), equalTo(
            "It is not possible to select resulted items because:\r\n" +
                "Max index [9] is out of got items count [8]"));
    }

    @Test(dataProvider = "negativeSizeConditions")
    public void negativeTestSizeConditional(ItemsCountCondition sizeCondition,
                                            String mismatchDescription) {
        var selection = selectArray()
            .whenCount(sizeCondition);
        var result = selection.evaluate(OBJECT_ARRAY);
        assertThat(result, nullValue());
        assertThat(selection.mismatchMessage(), equalTo(mismatchDescription));
    }

    @Test
    public void arrayNegativeTestBeforeIndex() {
        var selection = selectArray()
            .beforeIndex(10);

        var result = selection.evaluate(OBJECT_ARRAY);
        assertThat(result, nullValue());
        assertThat(selection.mismatchMessage(), equalTo(
            "It is not possible to select resulted items because:\r\n" +
                "Max index [9] is out of got items count [8]"));
    }

    @Test
    public void arrayNegativeTestAfterIndex() {
        var selection = selectArray()
            .afterIndex(10);

        var result = selection.evaluate(OBJECT_ARRAY);
        assertThat(result, nullValue());
        assertThat(selection.mismatchMessage(), equalTo(
            "It is not possible to select resulted items because:\r\n" +
                "Min index [11] is out of got items count [8]"));
    }

    @Test
    public void arrayNegativeTestOfCondition() {
        var selection = selectArray()
            .onCondition(condition(
                "contains a list",
                objects -> ArrayUtils.contains(objects, List.of(2,
                    true,
                    false,
                    "ABCD",
                    "EFG"))))
            .onCondition(condition(
                "Does not contains a list",
                objects -> !ArrayUtils.contains(objects, List.of(2,
                    true,
                    false,
                    "ABCD",
                    "EFG"))));

        var result = selection.evaluate(OBJECT_ARRAY);
        assertThat(result, nullValue());
        assertThat(selection.mismatchMessage(), equalTo(
            "It is not possible to select resulted items because:\r\n" +
                "Set of got items doesnt match 'Does not contains a list'"));
    }

    @Test
    public void arrayNegativeTestCombined() {
        var selection = selectArray()
            .onCondition(condition(
                "contains a list",
                objects -> ArrayUtils.contains(objects, List.of(2,
                    true,
                    false,
                    "ABCD",
                    "EFG"))))
            .onCondition(condition(
                "Does not contains a list",
                objects -> !ArrayUtils.contains(objects, List.of(2,
                    true,
                    false,
                    "ABCD",
                    "EFG"))))
            .whenCount(isEqual(10))
            .afterIndex(6)
            .beforeIndex(10)
            .ofCount(4);

        var result = selection.evaluate(OBJECT_ARRAY);
        assertThat(result, nullValue());
        assertThat(selection.mismatchMessage(), equalTo(
            "It is not possible to select resulted items because:\r\n" +
                "Count [8] of got items doesnt match 'equal 10'"));
    }

    @Test
    public void arrayNegativeTestCombined2() {
        var selection = selectArray()
            .onCondition(condition(
                "contains a list",
                objects -> ArrayUtils.contains(objects, List.of(2,
                    true,
                    false,
                    "ABCD",
                    "EFG"))))
            .onCondition(condition(
                "Does not contains a list",
                objects -> !ArrayUtils.contains(objects, List.of(2,
                    true,
                    false,
                    "ABCD",
                    "EFG"))))
            .afterIndex(6)
            .beforeIndex(10)
            .ofCount(4);

        var result = selection.evaluate(OBJECT_ARRAY);
        assertThat(result, nullValue());
        assertThat(selection.mismatchMessage(), equalTo(
            "It is not possible to select resulted items because:\r\n" +
                "Set of got items doesnt match 'Does not contains a list'"));
    }

    @Test
    public void arrayNegativeTestCombined3() {
        var selection = selectArray()
            .afterIndex(6)
            .beforeIndex(10)
            .ofCount(4);

        var result = selection.evaluate(OBJECT_ARRAY);
        assertThat(result, nullValue());
        assertThat(selection.mismatchMessage(), equalTo(
            "It is not possible to select resulted items because:\r\n" +
                "Max index [9] is out of got items count [8]"));
    }

    @Test
    public void arrayNegativeTestCombined4() {
        var selection = selectArray()
            .afterIndex(6)
            .ofCount(4);

        var result = selection.evaluate(OBJECT_ARRAY);
        assertThat(result, nullValue());
        assertThat(selection.mismatchMessage(), equalTo(
            "It is not possible to select resulted items because:\r\n" +
                "Max index [10] is out of got items count [8]"));
    }

    @Test
    public void arrayNegativeTestOfInvalidParameters() {
        var selection = selectArray();

        try {
            selection.validateParameters();
        } catch (IllegalArgumentException e) {
            assertThat(e, throwableHasMessage("Any parameter should be defined"));
            return;
        }

        fail("Exception was expected");
    }

    @Test
    public void arrayNegativeTestOfInvalidParameters2() {
        var selection = selectArray()
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
    public void arrayNegativeTestOfInvalidParameters3() {
        var selection = selectArray()
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
    public void arrayNegativeTestOfInvalidParameters4() {
        var selection = selectArray()
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
    public void arrayNegativeTestOfInvalidParameters5() {
        var selection = selectArray()
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
