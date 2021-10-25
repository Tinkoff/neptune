package ru.tinkoff.qa.neptune.spring.data.select;

import org.springframework.data.repository.Repository;
import ru.tinkoff.qa.neptune.core.api.event.firing.annotations.CaptureOnSuccess;
import ru.tinkoff.qa.neptune.core.api.steps.SequentialGetStepSupplier;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.IncludeParamsOfInnerGetterStep;
import ru.tinkoff.qa.neptune.database.abstractions.SelectQuery;
import ru.tinkoff.qa.neptune.spring.data.SpringDataContext;
import ru.tinkoff.qa.neptune.spring.data.captors.EntitiesCaptor;

import java.util.function.Function;

import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;
import static java.util.stream.StreamSupport.stream;
import static org.apache.commons.lang3.StringUtils.EMPTY;

/**
 * Gets some {@link Iterable} from selected entity.
 *
 * @param <T> is a type of object to get
 * @param <M> is a type of entity
 */
@SuppressWarnings("unchecked")
@CaptureOnSuccess(by = EntitiesCaptor.class)
@IncludeParamsOfInnerGetterStep
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

    @Override
    protected S from(SequentialGetStepSupplier<SpringDataContext, ? extends Iterable<M>, ?, ?, ?> from) {
        setDescription(from.toString());
        return super.from(from);
    }

    @Override
    protected String getDescription() {
        return ofNullable(getFrom())
                .map(Object::toString)
                .orElse(EMPTY);
    }

    public static final class GetIterableItemFromEntitiesImpl<T, M>
            extends GetIterableItemFromEntities<T, M, GetIterableItemFromEntitiesImpl<T, M>> {

        private GetIterableItemFromEntitiesImpl(Function<M, T> originalFunction) {
            super(originalFunction);
        }

        public <ID, R extends Repository<M, ID>> GetIterableItemFromEntitiesImpl<T, M> setRepository(R repository) {
            ofNullable(getFrom()).ifPresent(o -> ((SelectOneStepSupplier.SelectOneStepSupplierImpl<M, ID, R>) o).from(repository));
            return this;
        }

        @Override
        public GetIterableItemFromEntitiesImpl<T, M> setDescription(String description) {
            ofNullable(getFrom()).ifPresent(o -> ((SelectOneStepSupplier.SelectOneStepSupplierImpl<?, ?, ?>) o).setDescription(description));
            return this;
        }
    }
}
