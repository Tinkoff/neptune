package ru.tinkoff.qa.neptune.core.api.steps.selections;

import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.DescriptionFragment;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Preconditions.checkArgument;
import static java.util.Objects.isNull;
import static ru.tinkoff.qa.neptune.core.api.localization.StepLocalization.translate;

abstract class Direction<T> {

    @DescriptionFragment("indexFrom")
    private final int indexFrom;

    private Integer count;

    Direction(int indexFrom) {
        this.indexFrom = indexFrom;
    }

    abstract List<T> select(List<T> selectFrom);

    @Override
    public String toString() {
        return translate(this);
    }

    int getIndexFrom() {
        return indexFrom;
    }

    Integer getCount() {
        return count;
    }

    Direction<T> setCount(Integer count) {
        this.count = count;
        return this;
    }

    abstract Integer minIndex();

    abstract Integer maxIndex();

    @Description("Before item index={indexFrom} exclusively")
    static final class Before<T> extends Direction<T> {

        Before(int indexFrom) {
            super(checkIndex(indexFrom));
        }

        private static int checkIndex(int indexFrom) {
            checkArgument(indexFrom > 0, "Index value should be greater than 0");
            return indexFrom;
        }

        @Override
        List<T> select(List<T> selectFrom) {
            if (selectFrom.size() < getIndexFrom() + 1) {
                return null;
            }

            return new ArrayList<>(selectFrom.subList(minIndex(), getIndexFrom()));
        }

        @Override
        Integer minIndex() {
            var count = getCount();
            return isNull(count) ? 0 : getIndexFrom() - count;
        }

        @Override
        Integer maxIndex() {
            return getIndexFrom() - 1;
        }
    }

    @Description("After item index={indexFrom} exclusively")
    static final class After<T> extends Direction<T> {

        After(int indexFrom) {
            super(checkIndex(indexFrom));
        }

        private static int checkIndex(int indexFrom) {
            checkArgument(indexFrom >= 0, "Index value should be positive");
            return indexFrom;
        }

        @Override
        List<T> select(List<T> selectFrom) {
            var count = getCount();
            if (selectFrom.size() < getIndexFrom() + (isNull(count) ? 0 : count) + 1) {
                return null;
            }

            int end = isNull(count) ? selectFrom.size() : getIndexFrom() + count + 1;
            return new ArrayList<>(selectFrom.subList(maxIndex(), end));
        }

        @Override
        Integer minIndex() {
            return getIndexFrom() + 1;
        }

        @Override
        Integer maxIndex() {
            var count = getCount();
            return isNull(count) ? null : getIndexFrom() + count;
        }
    }
}
