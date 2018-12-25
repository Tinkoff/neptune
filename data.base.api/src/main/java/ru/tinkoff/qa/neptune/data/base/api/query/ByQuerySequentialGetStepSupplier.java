package ru.tinkoff.qa.neptune.data.base.api.query;

import ru.tinkoff.qa.neptune.core.api.StoryWriter;
import ru.tinkoff.qa.neptune.data.base.api.DataBaseSteps;
import ru.tinkoff.qa.neptune.data.base.api.PersistableObject;

import javax.jdo.JDOQLTypedQuery;
import java.util.function.Function;
import java.util.function.Predicate;

import static com.google.common.base.Preconditions.checkArgument;
import static java.util.Objects.nonNull;

@SuppressWarnings("unchecked")
public abstract class ByQuerySequentialGetStepSupplier<T, S, R, Q extends ByQuerySequentialGetStepSupplier<T, S, R, Q>>
        extends SelectSequentialGetStepSupplier<S, R, Q> {

    private final Function<DataBaseSteps, R> query;
    Predicate<T> condition;

    ByQuerySequentialGetStepSupplier(Function<DataBaseSteps, R> query) {
        this.query = query;
    }

    /**
     * Sometimes the performing of a query can take o lot of time. The better solution is to create lighter query
     * and filter result by some condition. It is necessary to describe given condition by {@link StoryWriter#condition(String, Predicate)}.
     *
     * @param condition is a predicate to filter the selection result.
     * @return self-reference
     */
    public Q withCondition(Predicate<T> condition) {
        checkArgument(nonNull(condition), "Condition should be defined");
        this.condition = condition;
        return (Q) this;
    }

    @Override
    public Function<DataBaseSteps, S> get() {
        super.from(query);
        return super.get();
    }
}
