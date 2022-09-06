package ru.tinkoff.qa.neptune.core.api.steps.selections;

import com.google.common.collect.Iterables;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.StepParameter;
import ru.tinkoff.qa.neptune.core.api.steps.selections.mismatch.dictionary.IndexIsOut;

import static com.google.common.base.Preconditions.checkArgument;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

@SuppressWarnings("unchecked")
public abstract class SelectionOfItem<T, R, THIS extends SelectionOfItem<T, R, THIS>>
    extends SelectionOfIterable<T, R, THIS> {

    @StepParameter(value = "Index", doNotReportNullValues = true)
    Integer index;

    private SelectionOfItem() {
        super();
    }

    public static <T, I extends Iterable<T>> SelectionOfIterableItem<T, I> selectItemOfIterable() {
        return new SelectionOfIterableItem<>();
    }

    public static <T> SelectionOfArrayItem<T> selectItemOfArray() {
        return new SelectionOfArrayItem<>();
    }

    /**
     * Defines index of the target element
     *
     * @param index index of the target element
     * @return self-reference
     */
    public THIS index(int index) {
        checkArgument(index >= 0, "Index value should be positive");
        this.index = index;
        return (THIS) this;
    }

    @Override
    protected void validateParameters() throws IllegalArgumentException {
        if (isNull(index)
            && isNull(whenCount)
            && additionalConditionsForIterable.isEmpty()) {
            throw new IllegalArgumentException("Any parameter should be defined");
        }

        if (whenCount instanceof ItemsCountCondition.WhenCountEquals && nonNull(index)) {
            var expected = ((ItemsCountCondition.WhenCountEquals) whenCount).getCount();

            if (expected - 1 < index) {
                throw new IllegalArgumentException("Parameters are not valid, because index[" + index + "] "
                    + " is out of expected item count " + expected);
            }
        }

        if (whenCount instanceof ItemsCountCondition.WhenCountBetween && nonNull(index)) {
            var expectedMinSize = ((ItemsCountCondition.WhenCountBetween) whenCount).minimalCount();
            var expectedMaxSize = ((ItemsCountCondition.WhenCountBetween) whenCount).maximalCount();

            if (index > expectedMinSize - 1) {
                throw new IllegalArgumentException("Parameters are not valid, because index[" + index + "] "
                    + " is out of expected minimal item count " + expectedMinSize);
            }

            if (nonNull(expectedMaxSize) && index > expectedMaxSize - 1) {
                throw new IllegalArgumentException("Parameters are not valid, because index[" + index + "] "
                    + " is out of expected maximal item count " + expectedMaxSize);
            }
        }
    }

    StringBuilder mismatchDetails(StringBuilder appendTo, int size) {
        appendTo.append("\r\n")
            .append(new IndexIsOut(index, size));
        return appendTo;
    }

    public static final class SelectionOfIterableItem<R, I extends Iterable<R>>
        extends SelectionOfItem<I, R, SelectionOfIterableItem<R, I>> {

        @Override
        StringBuilder additionalMismatchDetails(StringBuilder appendTo) {
            return mismatchDetails(appendTo, Iterables.size(getLastSelectionArgumentValue()));
        }

        @Override
        R get(I toSelectFrom) {
            int toGet = 0;
            if (nonNull(index)) {
                toGet = index;
            }

            if (Iterables.size(toSelectFrom) - 1 < toGet) {
                return null;
            }

            return Iterables.get(toSelectFrom, toGet);
        }
    }

    public static final class SelectionOfArrayItem<R>
        extends SelectionOfItem<R[], R, SelectionOfArrayItem<R>> {

        @Override
        StringBuilder additionalMismatchDetails(StringBuilder appendTo) {
            return mismatchDetails(appendTo, getLastSelectionArgumentValue().length);
        }

        @Override
        R get(R[] toSelectFrom) {
            int toGet = 0;
            if (nonNull(index)) {
                toGet = index;
            }

            if (toSelectFrom.length - 1 < toGet) {
                return null;
            }

            return toSelectFrom[toGet];
        }
    }
}
