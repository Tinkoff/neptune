package ru.tinkoff.qa.neptune.hibernate.select;

import ru.tinkoff.qa.neptune.core.api.event.firing.annotations.CaptureOnSuccess;
import ru.tinkoff.qa.neptune.core.api.steps.SequentialGetStepSupplier;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.IncludeParamsOfInnerGetterStep;
import ru.tinkoff.qa.neptune.database.abstractions.SelectQuery;
import ru.tinkoff.qa.neptune.database.abstractions.captors.DataCaptor;
import ru.tinkoff.qa.neptune.database.abstractions.dictionary.RequiredEntity;
import ru.tinkoff.qa.neptune.hibernate.HibernateContext;

import java.util.function.Function;

import static java.util.Optional.ofNullable;

/**
 * Gets some {@link Iterable} from selected entity.
 *
 * @param <T> is a type of object to get
 * @param <M> is a type of entity
 */
@CaptureOnSuccess(by = DataCaptor.class)
@SequentialGetStepSupplier.DefineCriteriaParameterName("Result criteria")
public abstract class GetItemOfIterableFromEntity<T, I extends Iterable<T>, M, S extends GetItemOfIterableFromEntity<T, I, M, S>>
        extends SequentialGetStepSupplier.GetObjectFromIterableChainedStepSupplier<HibernateContext, T, M, S>
        implements SelectQuery<T> {

    private GetItemOfIterableFromEntity(Function<M, I> originalFunction) {
        super(originalFunction);
    }

    static <T, I extends Iterable<T>, M> GetItemOfIterableFromEntity<T, I, M, ?> getIterableItemFromEntity(
            SelectOneStepSupplier<M> from,
            Function<M, I> f) {
        return new GetItemOfIterableFromEntityImpl<>(f).from(from);
    }

    @IncludeParamsOfInnerGetterStep
    public static final class GetItemOfIterableFromEntityImpl<T, I extends Iterable<T>, M>
            extends GetItemOfIterableFromEntity<T, I, M, GetItemOfIterableFromEntityImpl<T, I, M>> {

        private GetItemOfIterableFromEntityImpl(Function<M, I> originalFunction) {
            super(originalFunction);
        }

        @Override
        public GetItemOfIterableFromEntityImpl<T, I, M> setDescription(String description) {
            super.setDescription(description);
            ofNullable(getFrom()).ifPresent(o -> ((SetsDescription) o).changeDescription(new RequiredEntity().toString()));
            return this;
        }
    }
}
