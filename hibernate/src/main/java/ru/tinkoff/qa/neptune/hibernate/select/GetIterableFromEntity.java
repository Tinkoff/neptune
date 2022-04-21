package ru.tinkoff.qa.neptune.hibernate.select;

import ru.tinkoff.qa.neptune.core.api.event.firing.annotations.CaptureOnSuccess;
import ru.tinkoff.qa.neptune.core.api.steps.SequentialGetStepSupplier;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.IncludeParamsOfInnerGetterStep;
import ru.tinkoff.qa.neptune.database.abstractions.SelectQuery;
import ru.tinkoff.qa.neptune.database.abstractions.captors.DataCaptor;
import ru.tinkoff.qa.neptune.database.abstractions.dictionary.RequiredEntity;
import ru.tinkoff.qa.neptune.hibernate.HibernateContext;

import java.util.List;
import java.util.function.Function;

import static java.util.Optional.ofNullable;

/**
 * Gets some {@link Iterable} from selected entity.
 *
 * @param <T> is a type of object to get
 * @param <M> is a type of entity
 */
@CaptureOnSuccess(by = DataCaptor.class)
@SequentialGetStepSupplier.DefineCriteriaParameterName("Criteria of an item of resulted iterable")
public abstract class GetIterableFromEntity<T, I extends Iterable<T>, M>
        extends SequentialGetStepSupplier.GetListChainedStepSupplier<HibernateContext, I, M, T, GetIterableFromEntity<T, I, M>>
        implements SelectQuery<List<T>> {

    private GetIterableFromEntity(Function<M, I> originalFunction) {
        super(originalFunction);
    }

    static <T, I extends Iterable<T>, M> GetIterableFromEntity<T, I, M> getIterableFromEntity(
            SelectOneStepSupplier<M> from,
            Function<M, I> f) {
        return new GetIterableFromEntityImpl<>(f).from(from);
    }

    @IncludeParamsOfInnerGetterStep
    public static final class GetIterableFromEntityImpl<T, I extends Iterable<T>, M>
            extends GetIterableFromEntity<T, I, M> {

        private GetIterableFromEntityImpl(Function<M, I> originalFunction) {
            super(originalFunction);
        }

        @Override
        public GetIterableFromEntityImpl<T, I, M> setDescription(String description) {
            super.setDescription(description);
            ofNullable(getFrom()).ifPresent(o -> ((SetsDescription) o).changeDescription(new RequiredEntity().toString()));
            return this;
        }
    }
}
