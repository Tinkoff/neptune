package ru.tinkoff.qa.neptune.database.abstractions;

import ru.tinkoff.qa.neptune.core.api.cleaning.Stoppable;
import ru.tinkoff.qa.neptune.core.api.steps.SequentialGetStepSupplier;
import ru.tinkoff.qa.neptune.core.api.steps.context.Context;

import java.time.Duration;

public abstract class AbstractDatabaseStepContext<T extends AbstractDatabaseStepContext<T>> extends Context<T>
        implements Stoppable {

    @Override
    public abstract void stop();

    protected abstract <R, Q extends SequentialGetStepSupplier<T, Q, ?, ?, ?> & SelectQuery<R>> R select(Q query);

    protected abstract <R, Q extends SequentialGetStepSupplier<T, Q, ?, ?, ?> & SelectQuery<R>> R update(Q query, UpdateAction<R>... actions);

    protected abstract <R, Q extends SequentialGetStepSupplier<T, Q, ?, ?, ?> & SelectQuery<R>> R delete(Q query);

    protected abstract <R, Q extends SequentialGetStepSupplier<T, Q, ?, ?, ?> & InsertQuery<R>> R insert(Q query);

    public final <R, Q extends SequentialGetStepSupplier<T, Q, ?, ?, ?> & SelectQuery<R>> boolean presence(Q query) {
        return super.presenceOf(query);
    }

    public final <R, Q extends SequentialGetStepSupplier<T, Q, ?, ?, ?> & SelectQuery<R>> boolean presenceOrThrow(Q query) {
        return super.presenceOfOrThrow(query);
    }

    public final <R, Q extends SequentialGetStepSupplier<T, Q, ?, ?, ?> & SelectQuery<R>> boolean absence(Q query, Duration timeOut) {
        return super.absenceOf(query, timeOut);
    }

    public final <R, Q extends SequentialGetStepSupplier<T, Q, ?, ?, ?> & SelectQuery<R>> boolean absenceOrThrow(Q query, Duration timeOut) {
        return super.absenceOfOrThrow(query, timeOut);
    }
}
