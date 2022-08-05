package ru.tinkoff.qa.neptune.hibernate.select;

import ru.tinkoff.qa.neptune.core.api.event.firing.annotations.CaptureOnSuccess;
import ru.tinkoff.qa.neptune.core.api.steps.SequentialGetStepSupplier;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.IncludeParamsOfInnerGetterStep;
import ru.tinkoff.qa.neptune.database.abstractions.SelectQuery;
import ru.tinkoff.qa.neptune.database.abstractions.captors.DataCaptor;
import ru.tinkoff.qa.neptune.database.abstractions.dictionary.RequiredEntities;
import ru.tinkoff.qa.neptune.hibernate.HibernateContext;

import java.util.List;
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
@SequentialGetStepSupplier.DefineCriteriaParameterName("Criteria of an item of resulted iterable")
public abstract class GetIterableFromEntities<T, M>
        extends SequentialGetStepSupplier.GetListChainedStepSupplier<HibernateContext, List<T>, Iterable<M>, T, GetIterableFromEntities<T, M>>
        implements SelectQuery<List<T>> {

    private GetIterableFromEntities(Function<M, T> originalFunction) {
        super(ms -> stream(ms.spliterator(), false)
                .map(originalFunction)
                .collect(toList()));
    }

    static <T, M> GetIterableFromEntities<T, M> getIterableFromEntities(
            SelectManyStepSupplier<M> from,
            Function<M, T> f) {
        return new GetIterableFromEntitiesImpl<>(f).from(from);
    }

    @IncludeParamsOfInnerGetterStep
    public static final class GetIterableFromEntitiesImpl<T, M>
            extends GetIterableFromEntities<T, M> {

        private GetIterableFromEntitiesImpl(Function<M, T> originalFunction) {
            super(originalFunction);
        }

        @Override
        public GetIterableFromEntitiesImpl<T, M> setDescription(String description) {
            super.setDescription(description);
            ofNullable(getFrom()).ifPresent(o -> ((SetsDescription) o).changeDescription(new RequiredEntities().toString()));
            return this;
        }
    }
}