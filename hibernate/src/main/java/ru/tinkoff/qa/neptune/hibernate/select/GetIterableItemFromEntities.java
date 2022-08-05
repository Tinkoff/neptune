package ru.tinkoff.qa.neptune.hibernate.select;

import ru.tinkoff.qa.neptune.core.api.event.firing.annotations.CaptureOnSuccess;
import ru.tinkoff.qa.neptune.core.api.steps.SequentialGetStepSupplier;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.IncludeParamsOfInnerGetterStep;
import ru.tinkoff.qa.neptune.database.abstractions.SelectQuery;
import ru.tinkoff.qa.neptune.database.abstractions.captors.DataCaptor;
import ru.tinkoff.qa.neptune.database.abstractions.dictionary.RequiredEntities;
import ru.tinkoff.qa.neptune.hibernate.HibernateContext;

import java.util.function.Function;

import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;
import static java.util.stream.StreamSupport.stream;

/**
 * Gets some {@link Iterable} from selected entity.
 *
 * @param <T> is a type of object to get
 * @param <M> is a type of entity
 */
@CaptureOnSuccess(by = DataCaptor.class)
@SequentialGetStepSupplier.DefineCriteriaParameterName("Result criteria")
public abstract class GetIterableItemFromEntities<T, M>
        extends SequentialGetStepSupplier.GetObjectFromIterableChainedStepSupplier<HibernateContext, T, Iterable<M>, GetIterableItemFromEntities<T, M>>
        implements SelectQuery<T> {

    protected GetIterableItemFromEntities(Function<M, T> originalFunction) {
        super(ms -> stream(ms.spliterator(), false)
                .map(originalFunction)
                .collect(toList()));
    }

    static <T, M> GetIterableItemFromEntities<T, M> getIterableItemFromEntities(
            SelectManyStepSupplier<M> from,
            Function<M, T> f) {
        return new GetIterableItemFromEntitiesImpl<>(f).from(from);
    }

    @IncludeParamsOfInnerGetterStep
    public static final class GetIterableItemFromEntitiesImpl<T, M>
            extends GetIterableItemFromEntities<T, M> {

        private GetIterableItemFromEntitiesImpl(Function<M, T> originalFunction) {
            super(originalFunction);
        }

        @Override
        public GetIterableItemFromEntitiesImpl<T, M> setDescription(String description) {
            super.setDescription(description);
            ofNullable(getFrom()).ifPresent(o -> ((SetsDescription) o).changeDescription(new RequiredEntities().toString()));
            return this;
        }
    }
}