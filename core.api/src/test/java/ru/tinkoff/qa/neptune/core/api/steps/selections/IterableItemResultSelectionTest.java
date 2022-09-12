package ru.tinkoff.qa.neptune.core.api.steps.selections;

import com.google.common.collect.Iterables;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.nullValue;
import static org.testng.Assert.fail;
import static ru.tinkoff.qa.neptune.core.api.hamcrest.throwable.ThrowableMessageMatcher.throwableHasMessage;
import static ru.tinkoff.qa.neptune.core.api.steps.Criteria.condition;
import static ru.tinkoff.qa.neptune.core.api.steps.selections.ItemsCountCondition.isEqual;
import static ru.tinkoff.qa.neptune.core.api.steps.selections.ItemsCountCondition.isGreater;
import static ru.tinkoff.qa.neptune.core.api.steps.selections.SelectionOfItem.selectItemOfIterable;

public class IterableItemResultSelectionTest extends SelectionsPreparations {

    @Test
    public void listPositiveIndex() {
        var result = selectItemOfIterable().index(5).evaluate(OBJECT_LIST);
        assertThat(result, equalTo("EFG"));
    }

    @Test(dataProvider = "positiveSizeConditions")
    public void listPositiveWithoutIndex(ItemsCountCondition sizeCondition) {
        var result = selectItemOfIterable()
                .whenCount(sizeCondition)
                .evaluate(OBJECT_LIST);
        assertThat(result, equalTo(1));
    }

    @Test
    public void listPositiveWithoutIndexAndCondition() {
        var result = selectItemOfIterable()
                .onCondition(condition(
                        "contains a list",
                        objects -> Iterables.contains(objects, List.of(2,
                                true,
                                false,
                                "ABCD",
                                "EFG"))))
                .evaluate(OBJECT_LIST);
        assertThat(result, equalTo(1));
    }

    @Test
    public void listPositiveWithIndexAndCondition() {
        var result = selectItemOfIterable()
                .index(5)
                .onCondition(condition(
                        "contains a list",
                        objects -> Iterables.contains(objects, List.of(2,
                                true,
                                false,
                                "ABCD",
                                "EFG"))))
                .evaluate(OBJECT_LIST);
        assertThat(result, equalTo("EFG"));
    }

    @Test
    public void listPositiveCombinedWithoutIndex() {
        var result = selectItemOfIterable()
                .onCondition(condition(
                        "contains a list",
                        objects -> Iterables.contains(objects, List.of(2,
                                true,
                                false,
                                "ABCD",
                                "EFG"))))
                .whenCount(isGreater(5))
                .evaluate(OBJECT_LIST);
        assertThat(result, equalTo(1));
    }

    @Test
    public void listPositiveCombinedWithIndex() {
        var result = selectItemOfIterable()
                .index(5)
                .onCondition(condition(
                        "contains a list",
                        objects -> Iterables.contains(objects, List.of(2,
                                true,
                                false,
                                "ABCD",
                                "EFG"))))
                .whenCount(isGreater(5))
                .evaluate(OBJECT_LIST);
        assertThat(result, equalTo("EFG"));
    }

    @Test
    public void negativeTestNull() {
        var selection = selectItemOfIterable().index(10);
        var result = selection.evaluate(null);
        assertThat(result, nullValue());
        assertThat(selection.mismatchMessage(), equalTo(
                "It is not possible to select resulted items because:\r\n" +
                        "Set of got items was null"));
    }

    @Test
    public void negativeTestEmpty() {
        var selection = selectItemOfIterable().index(10);
        var result = selection.evaluate(new ArrayList<>());
        assertThat(result, nullValue());
        assertThat(selection.mismatchMessage(), equalTo(
                "It is not possible to select resulted items because:\r\n" +
                        "Set of got items was empty"));
    }

    @Test
    public void negativeTestSizeIndex() {
        var selection = selectItemOfIterable().index(10);
        var result = selection.evaluate(OBJECT_LIST);
        assertThat(result, nullValue());
        assertThat(selection.mismatchMessage(), equalTo(
                "It is not possible to select resulted items because:\r\n" +
                        "Index [10] is out of got items count [8]"));
    }

    @Test(dataProvider = "negativeSizeConditions")
    public void negativeTestSizeConditional(ItemsCountCondition sizeCondition,
                                            String mismatchDescription) {
        var selection = selectItemOfIterable()
                .whenCount(sizeCondition);
        var result = selection.evaluate(OBJECT_LIST);
        assertThat(result, nullValue());
        assertThat(selection.mismatchMessage(), equalTo(mismatchDescription));
    }

    @Test
    public void listNegativeTestOfCondition() {
        var selection = selectItemOfIterable()
                .onCondition(condition(
                        "contains a list",
                        objects -> Iterables.contains(objects, List.of(2,
                                true,
                                false,
                                "ABCD",
                                "EFG"))))
                .onCondition(condition(
                        "does not contain a list",
                        objects -> !Iterables.contains(objects, List.of(2,
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
        var selection = selectItemOfIterable()
                .onCondition(condition(
                        "contains a list",
                        objects -> Iterables.contains(objects, List.of(2,
                                true,
                                false,
                                "ABCD",
                                "EFG"))))
                .onCondition(condition(
                        "does not contain a list",
                        objects -> !Iterables.contains(objects, List.of(2,
                                true,
                                false,
                                "ABCD",
                                "EFG"))))
                .whenCount(isEqual(10))
                .index(5);

        var result = selection.evaluate(OBJECT_LIST);
        assertThat(result, nullValue());
        assertThat(selection.mismatchMessage(), equalTo(
                "It is not possible to select resulted items because:\r\n" +
                        "Count [8] of got items doesnt match 'equal 10'"));
    }

    @Test
    public void listNegativeTestCombined2() {
        var selection = selectItemOfIterable()
                .onCondition(condition(
                        "contains a list",
                        objects -> Iterables.contains(objects, List.of(2,
                                true,
                                false,
                                "ABCD",
                                "EFG"))))
                .onCondition(condition(
                        "does not contain a list",
                        objects -> !Iterables.contains(objects, List.of(2,
                                true,
                                false,
                                "ABCD",
                                "EFG"))))
                .index(5);

        var result = selection.evaluate(OBJECT_LIST);
        assertThat(result, nullValue());
        assertThat(selection.mismatchMessage(), equalTo(
                "It is not possible to select resulted items because:\r\n" +
                        "Set of got items doesnt match 'does not contain a list'"));
    }

    @Test
    public void listNegativeTestOfInvalidParameters() {
        var selection = selectItemOfIterable();

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
        var selection = selectItemOfIterable()
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
    public void listNegativeTestOfInvalidParameters3() {
        var selection = selectItemOfIterable()
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
