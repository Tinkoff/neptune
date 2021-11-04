package ru.tinkoff.qa.neptune.spring.data.select;

import org.springframework.data.repository.Repository;
import ru.tinkoff.qa.neptune.core.api.event.firing.annotations.CaptureOnSuccess;
import ru.tinkoff.qa.neptune.core.api.steps.SequentialGetStepSupplier;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.IncludeParamsOfInnerGetterStep;
import ru.tinkoff.qa.neptune.database.abstractions.SelectQuery;
import ru.tinkoff.qa.neptune.spring.data.SpringDataContext;
import ru.tinkoff.qa.neptune.spring.data.captors.EntitiesCaptor;
import ru.tinkoff.qa.neptune.spring.data.dictionary.RequiredEntities;

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
@CaptureOnSuccess(by = EntitiesCaptor.class)
@SequentialGetStepSupplier.DefineCriteriaParameterName("Result criteria")
public abstract class GetIterableItemFromEntities<T, M, S extends GetIterableItemFromEntities<T, M, S>>
        extends SequentialGetStepSupplier.GetObjectFromIterableChainedStepSupplier<SpringDataContext, T, Iterable<M>, S>
        implements SelectQuery<T> {

    protected GetIterableItemFromEntities(Function<M, T> originalFunction) {
        super(ms -> stream(ms.spliterator(), false)
                .map(originalFunction)
                .collect(toList()));
    }

    static <T, M, ID, R extends Repository<M, ID>> GetIterableItemFromEntities<T, M, ?> getIterableItemFromEntities(
            SelectManyStepSupplier<M, ID, R> from,
            Function<M, T> f) {
        return new GetIterableItemFromEntitiesImpl<>(f).from(from);
    }

    @IncludeParamsOfInnerGetterStep
    public static final class GetIterableItemFromEntitiesImpl<T, M>
            extends GetIterableItemFromEntities<T, M, GetIterableItemFromEntitiesImpl<T, M>> {

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