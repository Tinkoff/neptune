package ru.tinkoff.qa.neptune.core.api.steps.selections;

import com.google.common.collect.Iterables;
import org.apache.commons.lang3.ArrayUtils;
import ru.tinkoff.qa.neptune.core.api.steps.Criteria;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.StepParameter;
import ru.tinkoff.qa.neptune.core.api.steps.conditions.ResultSelection;
import ru.tinkoff.qa.neptune.core.api.steps.selections.mismatch.dictionary.*;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.apache.commons.lang3.StringUtils.SPACE;
import static ru.tinkoff.qa.neptune.core.api.localization.StepLocalization.translate;

@SuppressWarnings("unchecked")
abstract class SelectionOfIterable<T, R, THIS extends SelectionOfIterable<T, R, THIS>>
    extends ResultSelection<T, R> {

    private final List<Criteria<T>> notMatched = new ArrayList<>();
    @StepParameter("Return the result when total count of found items is")
    ItemsCountCondition whenCount;
    LinkedList<Criteria<T>> additionalConditionsForIterable = new LinkedList<>();
    private boolean wasNull;
    private boolean wasEmpty;
    private boolean isSizeConditionReached;
    private int size;

    /**
     * Defines a size/length condition for entire object of items.
     *
     * @param sizeCondition a size/length condition for entire object
     * @return self-reference
     */
    public THIS whenCount(ItemsCountCondition sizeCondition) {
        checkNotNull(sizeCondition);
        whenCount = sizeCondition;
        return (THIS) this;
    }

    /**
     * Defines a condition for entire object of items.
     *
     * @param condition a condition for entire object
     */
    public THIS onCondition(Criteria<T> condition) {
        checkNotNull(condition);
        additionalConditionsForIterable.addLast(condition);
        return (THIS) this;
    }

    @Override
    protected R evaluateResultSelection(T toSelectFrom) {
        wasNull = false;
        wasEmpty = false;
        isSizeConditionReached = true;
        notMatched.clear();

        if (isNull(toSelectFrom)) {
            wasNull = true;
            return null;
        }

        if (toSelectFrom instanceof Iterable<?>) {
            size = Iterables.size((Iterable<?>) toSelectFrom);
        } else if (toSelectFrom.getClass().isArray()) {
            size = ArrayUtils.getLength(toSelectFrom);
        } else {
            throw new UnsupportedOperationException("Values of "
                + toSelectFrom.getClass()
                + " are not supported to get size/length");
        }

        if (size == 0) {
            wasEmpty = true;
            return null;
        }

        if (nonNull(whenCount)) {
            isSizeConditionReached = whenCount.isCountAsExpected(size);
        }

        if (!additionalConditionsForIterable.isEmpty()) {
            additionalConditionsForIterable.forEach(ac -> {
                try {
                    if (!ac.get().test(toSelectFrom)) {
                        notMatched.add(ac);
                    }
                } catch (Throwable t) {
                    t.printStackTrace();
                    notMatched.add(ac);
                }
            });
        }

        if (!isSizeConditionReached || !notMatched.isEmpty()) {
            return null;
        }

        return get(toSelectFrom);
    }

    @Override
    protected String mismatchMessage() {
        var messageBuilder = new StringBuilder();
        messageBuilder.append(new ImpossibleToSelectResultedItemsFromIterable());
        if (wasNull) {
            messageBuilder.append("\r\n").append(new WasNull());
            return messageBuilder.toString();
        }

        if (wasEmpty) {
            messageBuilder.append("\r\n").append(new WasEmpty());
            return messageBuilder.toString();
        }

        if (!isSizeConditionReached) {
            messageBuilder.append("\r\n")
                .append(new CountOfItemsDoesntMatchCondition(size, whenCount));
            return messageBuilder.toString();
        }

        if (!notMatched.isEmpty()) {
            notMatched
                .forEach(ac -> messageBuilder.append("\r\n")
                    .append(new DoesntMatch(ac)));
            return messageBuilder.toString();
        }

        return additionalMismatchDetails(messageBuilder).toString();
    }


    abstract StringBuilder additionalMismatchDetails(StringBuilder appendTo);

    abstract R get(T toSelectFrom);

    @Override
    public Map<String, String> getParameters() {
        var params = super.getParameters();

        if (!additionalConditionsForIterable.isEmpty()) {
            for (int i = 0; i < additionalConditionsForIterable.size(); i++) {
                var condition = additionalConditionsForIterable.get(i);
                var index = i == 0 ? EMPTY : SPACE + (i + 1);
                params.put(new OnCondition() + index, condition.toString());
            }
        }

        return params;
    }

    @Override
    protected R evaluate(T selectFrom) {
        return super.evaluate(selectFrom);
    }

    @Description("Return the result on condition")
    private static final class OnCondition {
        @Override
        public String toString() {
            return translate(this);
        }
    }
}
