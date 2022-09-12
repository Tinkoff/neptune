package ru.tinkoff.qa.neptune.core.api.steps.selections;

import ru.tinkoff.qa.neptune.core.api.steps.annotations.StepParameter;
import ru.tinkoff.qa.neptune.core.api.steps.parameters.ParameterValueGetter;
import ru.tinkoff.qa.neptune.core.api.steps.selections.mismatch.dictionary.MaxIndexIsOut;
import ru.tinkoff.qa.neptune.core.api.steps.selections.mismatch.dictionary.MinIndexIsOut;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static java.util.Arrays.stream;
import static java.util.Comparator.naturalOrder;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static java.util.stream.Collectors.joining;
import static org.apache.commons.lang3.ArrayUtils.add;
import static org.apache.commons.lang3.ArrayUtils.removeElements;

@SuppressWarnings("unchecked")
public abstract class SelectionOfItems<T, R, THIS extends SelectionOfItems<T, R, THIS>>
        extends SelectionOfIterable<T, R, THIS> {

    private Integer minIndex = 0;
    private Integer maxIndex;

    @StepParameter(value = "How many got items to return",
            doNotReportNullValues = true)
    private Integer size;

    @StepParameter(value = "To return got items before/after",
            doNotReportNullValues = true)
    private Direction direction;

    @StepParameter(value = "Indexes of got items to be returned",
            doNotReportNullValues = true,
            makeReadableBy = IndexesOfFoundItemsParameterValueGetter.class)
    private Integer[] indexes;

    private SelectionOfItems() {
        super();
    }

    public static <T> SelectionOfList<T> selectList() {
        return new SelectionOfList<>();
    }

    public static <T> SelectionOfArray<T> selectArray() {
        return new SelectionOfArray<>();
    }

    @Override
    protected void validateParameters() throws IllegalArgumentException {
        if (isNull(size)
                && isNull(direction)
                && isNull(whenCount)
                && additionalConditionsForIterable.isEmpty()
                && isNull(indexes)) {
            throw new IllegalArgumentException("Any parameter should be defined");
        }

        if (nonNull(direction)) {
            direction.setCount(size);
            minIndex = direction.minIndex();
            maxIndex = direction.maxIndex();
        } else if (nonNull(indexes)) {
            minIndex = indexes[0];
            maxIndex = indexes[indexes.length - 1];
        } else if (nonNull(size)) {
            maxIndex = size - 1;
        }

        if (isNull(whenCount)) {
            return;
        }

        if (whenCount instanceof ItemsCountCondition.WhenCountEquals) {
            var expected = ((ItemsCountCondition.WhenCountEquals) whenCount).getCount();

            if (expected - 1 < minIndex) {
                throw new IllegalArgumentException("Parameters are not valid, because min index[" + minIndex + "] "
                        + " is out of expected item count " + expected);
            }

            if (nonNull(maxIndex) &&
                    (expected - 1 < maxIndex)) {
                throw new IllegalArgumentException("Parameters are not valid, because max index[" + maxIndex + "] "
                        + " is out of expected item count " + expected);
            }
        }

        if (whenCount instanceof ItemsCountCondition.WhenCountBetween) {
            var expectedMinSize = ((ItemsCountCondition.WhenCountBetween) whenCount).minimalCount();
            var expectedMaxSize = ((ItemsCountCondition.WhenCountBetween) whenCount).maximalCount();

            if (minIndex > expectedMinSize - 1) {
                throw new IllegalArgumentException("Parameters are not valid, because min index[" + minIndex + "] "
                        + " is out of expected minimal item count " + expectedMinSize);
            }

            if (nonNull(expectedMaxSize) && nonNull(maxIndex) && (maxIndex > expectedMaxSize - 1)) {
                throw new IllegalArgumentException("Parameters are not valid, because max index[" + maxIndex + "] "
                        + " is out of expected maximal item count " + expectedMaxSize);
            }
        }
    }

    /**
     * Sets size/length of resulted object.
     * Invocation of this method erases value set by {@link #indexes(Integer...)}
     *
     * @param size size/length of resulted object
     * @return self-reference
     */
    public THIS ofCount(int size) {
        checkArgument(size > 0, "Size value should be greater than 0");
        this.size = size;
        this.indexes = null;
        return (THIS) this;
    }

    private THIS setDirection(Direction direction) {
        checkNotNull(direction);
        this.direction = direction;
        this.indexes = null;
        return (THIS) this;
    }

    /**
     * Sets upper index (exclusively) to get items.
     * Invocation of this method replaces value set by {@link #afterIndex(int)} and
     * erases value set by {@link #indexes(Integer...)}.
     *
     * @param index is exclusive value of the upper index
     * @return self-reference
     */
    public THIS beforeIndex(int index) {
        return setDirection(new Direction.Before(index));
    }

    /**
     * Sets lower index (exclusively) to get items.
     * Invocation of this method replaces value set by {@link #beforeIndex(int)} and
     * erases value set by {@link #indexes(Integer...)}.
     *
     * @param index is exclusive value of the lower index
     * @return self-reference
     */
    public THIS afterIndex(int index) {
        return setDirection(new Direction.After(index));
    }

    /**
     * Defines indexes of items to be returned.
     * Invocation of this method erases value set by {@link #ofCount(int)}}
     * and {@link #beforeIndex(int)} / {@link #afterIndex(int)}
     *
     * @param indexes indexes of items to be returned
     * @return self-reference
     */
    public THIS indexes(Integer... indexes) {
        checkNotNull(indexes);
        checkArgument(indexes.length > 0, "At least one index should be defined");
        checkArgument(stream(indexes).noneMatch(i -> i < 0), "Every index should be 0 or a positive value");
        this.indexes = stream(indexes).distinct().sorted(naturalOrder()).toArray(Integer[]::new);
        this.size = null;
        this.direction = null;
        return (THIS) this;
    }

    <I> List<I> sublistOfResultedItems(List<I> toSelectFrom) {
        var listSize = toSelectFrom.size();
        if (isNull(indexes)) {
            if (minIndex <= listSize - 1
                    && (maxIndex == null || maxIndex <= listSize - 1)) {
                return new ArrayList<>(toSelectFrom.subList(
                        minIndex,
                        nonNull(maxIndex) ? maxIndex + 1 : listSize
                ));
            }
        } else {
            var result = new ArrayList<I>();
            for (int index : indexes) {
                if (index <= listSize - 1) {
                    result.add(toSelectFrom.get(index));
                } else {
                    return null;
                }
            }
            return result;
        }
        return null;
    }

    StringBuilder mismatchDetails(StringBuilder appendTo, int size) {
        if (minIndex > size - 1) {
            appendTo.append("\r\n")
                    .append(new MinIndexIsOut(minIndex, size));
        }

        if (nonNull(maxIndex) && maxIndex > size - 1) {
            appendTo.append("\r\n")
                    .append(new MaxIndexIsOut(maxIndex, size));
        }

        return appendTo;
    }

    public static final class IndexesOfFoundItemsParameterValueGetter implements ParameterValueGetter<Integer[]> {

        @Override
        public String getParameterValue(Integer[] fieldValue) {
            return stream(fieldValue).map(Object::toString).collect(joining(","));
        }
    }

    public static final class SelectionOfList<T> extends SelectionOfItems<List<T>, List<T>, SelectionOfList<T>> {

        @Override
        StringBuilder additionalMismatchDetails(StringBuilder appendTo) {
            return mismatchDetails(appendTo, getLastSelectionArgumentValue().size());
        }

        @Override
        List<T> get(List<T> toSelectFrom) {
            return sublistOfResultedItems(toSelectFrom);
        }
    }

    public static final class SelectionOfArray<T> extends SelectionOfItems<T[], T[], SelectionOfArray<T>> {

        @Override
        StringBuilder additionalMismatchDetails(StringBuilder appendTo) {
            return mismatchDetails(appendTo, getLastSelectionArgumentValue().length);
        }

        @Override
        T[] get(T[] toSelectFrom) {
            var selected = sublistOfResultedItems(Arrays.asList(toSelectFrom));

            if (isNull(selected)) {
                return null;
            }

            var toReturn = removeElements(toSelectFrom, toSelectFrom);
            for (var t : selected) {
                toReturn = add(toReturn, t);
            }

            return toReturn;
        }
    }
}
