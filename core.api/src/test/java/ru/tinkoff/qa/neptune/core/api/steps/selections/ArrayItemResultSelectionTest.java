package ru.tinkoff.qa.neptune.core.api.steps.selections;

import org.apache.commons.lang3.ArrayUtils;
import org.testng.annotations.Test;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.nullValue;
import static org.testng.Assert.fail;
import static ru.tinkoff.qa.neptune.core.api.hamcrest.throwable.ThrowableMessageMatcher.throwableHasMessage;
import static ru.tinkoff.qa.neptune.core.api.steps.Criteria.condition;
import static ru.tinkoff.qa.neptune.core.api.steps.selections.ItemsCountCondition.isEqual;
import static ru.tinkoff.qa.neptune.core.api.steps.selections.ItemsCountCondition.isGreater;
import static ru.tinkoff.qa.neptune.core.api.steps.selections.SelectionOfItem.selectItemOfArray;

public class ArrayItemResultSelectionTest extends SelectionsPreparations {

    @Test
    public void arrayPositiveIndex() {
        var result = selectItemOfArray().index(5).evaluate(OBJECT_ARRAY);
        assertThat(result, equalTo("EFG"));
    }

    @Test(dataProvider = "positiveSizeConditions")
    public void arrayPositiveWithoutIndex(ItemsCountCondition sizeCondition) {
        var result = selectItemOfArray()
                .whenCount(sizeCondition)
                .evaluate(OBJECT_ARRAY);
        assertThat(result, equalTo(1));
    }

    @Test
    public void arrayPositiveWithoutIndexAndCondition() {
        var result = selectItemOfArray()
                .onCondition(condition(
                        "contains a list",
                        objects -> ArrayUtils.contains(objects, List.of(2,
                                true,
                                false,
                                "ABCD",
                                "EFG"))))
                .evaluate(OBJECT_ARRAY);
        assertThat(result, equalTo(1));
    }

    @Test
    public void arrayPositiveWithIndexAndCondition() {
        var result = selectItemOfArray()
                .index(5)
                .onCondition(condition(
                        "contains a list",
                        objects -> ArrayUtils.contains(objects, List.of(2,
                                true,
                                false,
                                "ABCD",
                                "EFG"))))
                .evaluate(OBJECT_ARRAY);
        assertThat(result, equalTo("EFG"));
    }

    @Test
    public void arrayPositiveCombinedWithoutIndex() {
        var result = selectItemOfArray()
                .onCondition(condition(
                        "contains a list",
                        objects -> ArrayUtils.contains(objects, List.of(2,
                                true,
                                false,
                                "ABCD",
                                "EFG"))))
                .whenCount(isGreater(5))
                .evaluate(OBJECT_ARRAY);
        assertThat(result, equalTo(1));
    }

    @Test
    public void arrayPositiveCombinedWithIndex() {
        var result = selectItemOfArray()
                .index(5)
                .onCondition(condition(
                        "contains a list",
                        objects -> ArrayUtils.contains(objects, List.of(2,
                                true,
                                false,
                                "ABCD",
                                "EFG"))))
                .whenCount(isGreater(5))
                .evaluate(OBJECT_ARRAY);
        assertThat(result, equalTo("EFG"));
    }

    @Test
    public void negativeTestNull() {
        var selection = selectItemOfArray().index(10);
        var result = selection.evaluate(null);
        assertThat(result, nullValue());
        assertThat(selection.mismatchMessage(), equalTo(
                "It is not possible to select resulted items because:\r\n" +
                        "Set of got items was null"));
    }

    @Test
    public void negativeTestEmpty() {
        var selection = selectItemOfArray().index(10);
        var result = selection.evaluate(new Object[0]);
        assertThat(result, nullValue());
        assertThat(selection.mismatchMessage(), equalTo(
                "It is not possible to select resulted items because:\r\n" +
                        "Set of got items was empty"));
    }

    @Test
    public void negativeTestSizeIndex() {
        var selection = selectItemOfArray().index(10);
        var result = selection.evaluate(OBJECT_ARRAY);
        assertThat(result, nullValue());
        assertThat(selection.mismatchMessage(), equalTo(
                "It is not possible to select resulted items because:\r\n" +
                        "Index [10] is out of got items count [8]"));
    }

    @Test(dataProvider = "negativeSizeConditions")
    public void negativeTestSizeConditional(ItemsCountCondition sizeCondition,
                                            String mismatchDescription) {
        var selection = selectItemOfArray()
                .whenCount(sizeCondition);
        var result = selection.evaluate(OBJECT_ARRAY);
        assertThat(result, nullValue());
        assertThat(selection.mismatchMessage(), equalTo(mismatchDescription));
    }

    @Test
    public void arrayNegativeTestOfCondition() {
        var selection = selectItemOfArray()
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
        var selection = selectItemOfArray()
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
                .index(5);

        var result = selection.evaluate(OBJECT_ARRAY);
        assertThat(result, nullValue());
        assertThat(selection.mismatchMessage(), equalTo(
                "It is not possible to select resulted items because:\r\n" +
                        "Count [8] of got items doesnt match 'equal 10'"));
    }

    @Test
    public void arrayNegativeTestCombined2() {
        var selection = selectItemOfArray()
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
                .index(5);

        var result = selection.evaluate(OBJECT_ARRAY);
        assertThat(result, nullValue());
        assertThat(selection.mismatchMessage(), equalTo(
                "It is not possible to select resulted items because:\r\n" +
                        "Set of got items doesnt match 'Does not contains a list'"));
    }

    @Test
    public void arrayNegativeTestOfInvalidParameters() {
        var selection = selectItemOfArray();

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
        var selection = selectItemOfArray()
                .index(10)
                .whenCount(isEqual(6));

        try {
            selection.validateParameters();
        } catch (IllegalArgumentException e) {
            assertThat(e,
                    throwableHasMessage("Parameters are not valid, because index[10]  " +
                            "is out of expected item count 6"));
            return;
        }

        fail("Exception was expected");
    }

    @Test
    public void arrayNegativeTestOfInvalidParameters3() {
        var selection = selectItemOfArray()
                .index(5)
                .whenCount(isGreater(2));

        try {
            selection.validateParameters();
        } catch (IllegalArgumentException e) {
            assertThat(e,
                    throwableHasMessage("Parameters are not valid, because index[5]  " +
                            "is out of expected minimal item count 3"));
            return;
        }

        fail("Exception was expected");
    }
}
