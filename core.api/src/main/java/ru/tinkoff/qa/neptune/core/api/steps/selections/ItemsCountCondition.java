package ru.tinkoff.qa.neptune.core.api.steps.selections;

import ru.tinkoff.qa.neptune.core.api.steps.SelfDescribed;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.DescriptionFragment;

import static com.google.common.base.Preconditions.checkArgument;
import static java.lang.String.valueOf;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static java.util.Optional.ofNullable;
import static org.apache.commons.lang3.StringUtils.EMPTY;

public abstract class ItemsCountCondition extends SelfDescribed {

    private ItemsCountCondition() {
        super();
    }

    /**
     * A condition when count of items equals to defined value
     *
     * @param count is expected count
     * @return instance of {@link ItemsCountCondition}
     */
    public static ItemsCountCondition isEqual(int count) {
        return new WhenCountEquals(count);
    }

    /**
     * A condition when count of items greater than defined value
     *
     * @param count is count lower border
     * @return instance of {@link WhenCountGreater}
     */
    public static WhenCountGreater isGreater(int count) {
        return new WhenCountGreater(count, false);
    }

    /**
     * A condition when count of items greater than or equals to defined value
     *
     * @param count is count lower border
     * @return instance of {@link WhenCountGreater}
     */
    public static WhenCountGreater isGreaterOrEqual(int count) {
        return new WhenCountGreater(count, true);
    }

    /**
     * A condition when count of items lesser than defined value
     *
     * @param count is count upper border
     * @return instance of {@link WhenCountGreater}
     */
    public static WhenCountLesser isLesser(int count) {
        return new WhenCountLesser(count, false);
    }

    /**
     * A condition when count of items lesser than or equals to defined value
     *
     * @param count is count upper border
     * @return instance of {@link WhenCountGreater}
     */
    public static WhenCountLesser isLesserOrEqual(int count) {
        return new WhenCountLesser(count, true);
    }

    abstract boolean isCountAsExpected(int count);

    @Description("equal {count}")
    static final class WhenCountEquals extends ItemsCountCondition {

        @DescriptionFragment("count")
        private final int count;

        private WhenCountEquals(int count) {
            checkArgument(count > 0,
                "Count value value should be greater than 0");
            this.count = count;
        }

        @Override
        protected boolean isCountAsExpected(int count) {
            return count == getCount();
        }

        int getCount() {
            return count;
        }
    }

    static abstract class WhenCountBetween extends ItemsCountCondition {

        private CountBorder min;
        private CountBorder max;

        @Override
        protected boolean isCountAsExpected(int count) {
            return count >= minimalCount() && ofNullable(maximalCount())
                .map(maximal -> count <= maximal)
                .orElse(true);
        }

        Integer minimalCount() {
            if (isNull(min)) {
                return 1;
            }

            if (min.isInclusive()) {
                return min.getBorderValue();
            } else {
                return min.getBorderValue() + 1;
            }
        }

        Integer maximalCount() {
            if (isNull(max)) {
                return null;
            }

            if (max.isInclusive()) {
                return max.getBorderValue();
            } else {
                return max.getBorderValue() - 1;
            }
        }

        @Override
        public String toString() {
            if (isNull(min) && isNull(max)) {
                return EMPTY;
            }

            var toStringBuilder = new StringBuilder();
            if (nonNull(min)) {
                if (min.inclusive) {
                    toStringBuilder.append(new GreaterThanOrEqual()).append(" ").append(min);
                } else {
                    toStringBuilder.append(new GreaterThan()).append(" ").append(min);
                }

                if (nonNull(max)) {
                    toStringBuilder.append("; ");
                }
            }

            if (nonNull(max)) {
                if (max.inclusive) {
                    toStringBuilder.append(new LesserThanOrEqual()).append(" ").append(max);
                } else {
                    toStringBuilder.append(new LesserThan()).append(" ").append(max);
                }
            }

            return toStringBuilder.toString();
        }

        void setMin(int min, boolean inclusive) {
            this.min = new CountBorder(min, inclusive);
        }

        void setMax(int max, boolean inclusive) {
            if (!inclusive) {
                checkArgument(max > 1, "Max count should be greater than 1");
            }
            this.max = new CountBorder(max, inclusive);
        }
    }

    private static class CountBorder {

        private final int borderValue;
        private final boolean inclusive;

        private CountBorder(int borderValue, boolean inclusive) {
            checkArgument(borderValue > 0,
                "Border value should be greater than 0");
            this.borderValue = borderValue;
            this.inclusive = inclusive;
        }

        @Override
        public String toString() {
            return valueOf(borderValue);
        }

        int getBorderValue() {
            return borderValue;
        }

        boolean isInclusive() {
            return inclusive;
        }
    }

    @Description("Greater than")
    private static final class GreaterThan extends SelfDescribed {
    }

    @Description("Greater than or equal")
    private static final class GreaterThanOrEqual extends SelfDescribed {
    }

    @Description("Lesser than")
    private static final class LesserThan extends SelfDescribed {
    }

    @Description("Lesser than or equal")
    private static final class LesserThanOrEqual extends SelfDescribed {
    }

    public static final class WhenCountGreater extends WhenCountBetween {

        WhenCountGreater(int borderValue, boolean inclusive) {
            setMin(borderValue, inclusive);
        }

        private ItemsCountCondition defineUppersBorder(int borderValue, boolean inclusively) {
            var minCount = minimalCount();
            setMax(borderValue, inclusively);
            checkArgument(maximalCount() >= minCount,
                "Not valid upper value ("
                    + (inclusively ?
                    "lower than or equal to " + borderValue :
                    "lower than " + borderValue)
                    + ") " +
                    "because the lower value is " + minCount);
            return this;
        }

        /**
         * Defines upper value of items count exclusively
         *
         * @param borderValue is upper limit
         * @return self-reference
         */
        public ItemsCountCondition andLesser(int borderValue) {
            return defineUppersBorder(borderValue, false);
        }

        /**
         * Defines upper value of items count inclusively
         *
         * @param borderValue is upper limit
         * @return self-reference
         */
        public ItemsCountCondition andLesserOrEqual(int borderValue) {
            return defineUppersBorder(borderValue, true);
        }
    }

    public static final class WhenCountLesser extends WhenCountBetween {

        WhenCountLesser(int borderValue, boolean inclusive) {
            setMax(borderValue, inclusive);
        }

        private ItemsCountCondition defineLowerBorder(int borderValue, boolean inclusively) {
            var maxCount = maximalCount();
            setMin(borderValue, inclusively);
            checkArgument(maxCount >= minimalCount(),
                "Not valid lower value ("
                    + (inclusively ? "greater than or equal to " + borderValue :
                    "greater than " + borderValue)
                    + ") " +
                    "because the upper value is " + maxCount);
            return this;
        }

        /**
         * Defines lower value of items count exclusively
         *
         * @param borderValue is lower limit
         * @return self-reference
         */
        public ItemsCountCondition andGreater(int borderValue) {
            return defineLowerBorder(borderValue, false);
        }

        /**
         * Defines lower value of items count inclusively
         *
         * @param borderValue is lower limit
         * @return self-reference
         */
        public ItemsCountCondition andGreaterOrEqual(int borderValue) {
            return defineLowerBorder(borderValue, true);
        }
    }
}
