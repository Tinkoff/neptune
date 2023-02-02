package ru.tinkoff.qa.neptune.core.api.steps.selections;

import ru.tinkoff.qa.neptune.core.api.steps.SelfDescribed;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.DescriptionFragment;

import static com.google.common.base.Preconditions.checkArgument;
import static java.util.Objects.isNull;

abstract class Direction extends SelfDescribed {

    @DescriptionFragment("indexFrom")
    private final int indexFrom;

    private Integer count;

    Direction(int indexFrom) {
        this.indexFrom = indexFrom;
    }

    int getIndexFrom() {
        return indexFrom;
    }

    Integer getCount() {
        return count;
    }

    void setCount(Integer count) {
        this.count = count;
    }

    abstract Integer minIndex();

    abstract Integer maxIndex();

    @Description("Before item index={indexFrom}")
    static final class Before extends Direction {

        Before(int indexFrom) {
            super(checkIndex(indexFrom));
        }

        private static int checkIndex(int indexFrom) {
            checkArgument(indexFrom > 0, "Index value should be greater than 0");
            return indexFrom;
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

    @Description("After item index={indexFrom}")
    static final class After extends Direction {

        After(int indexFrom) {
            super(checkIndex(indexFrom));
        }

        private static int checkIndex(int indexFrom) {
            checkArgument(indexFrom >= 0, "Index value should be positive");
            return indexFrom;
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
