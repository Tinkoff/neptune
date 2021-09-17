package ru.tinkoff.qa.neptune.database.abstractions;

import ru.tinkoff.qa.neptune.core.api.cleaning.Stoppable;
import ru.tinkoff.qa.neptune.core.api.steps.SequentialGetStepSupplier;
import ru.tinkoff.qa.neptune.core.api.steps.context.Context;

public abstract class AbstractDatabaseStepContext<T extends AbstractDatabaseStepContext<T>> extends Context<T>
        implements Stoppable {

    @Override
    public abstract void stop();

    protected abstract <R, Q extends SequentialGetStepSupplier<T, R, ?, ?, ?> & SelectQuery<R>> R select(Q query);

    protected abstract <R, Q extends SequentialGetStepSupplier<T, R, ?, ?, ?> & SelectQuery<R>> R update(Q query, UpdateAction<R>... actions);

    protected abstract <R, Q extends SequentialGetStepSupplier<T, R, ?, ?, ?> & SelectQuery<R>> R delete(Q query);

    protected abstract <R, Q extends SequentialGetStepSupplier<T, R, ?, ?, ?> & InsertQuery<R>> R insert(Q query);
}
