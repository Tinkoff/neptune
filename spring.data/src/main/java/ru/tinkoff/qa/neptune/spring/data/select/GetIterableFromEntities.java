package ru.tinkoff.qa.neptune.spring.data.select;

import org.springframework.data.repository.Repository;
import ru.tinkoff.qa.neptune.core.api.event.firing.annotations.CaptureOnSuccess;
import ru.tinkoff.qa.neptune.core.api.steps.SequentialGetStepSupplier;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.IncludeParamsOfInnerGetterStep;
import ru.tinkoff.qa.neptune.database.abstractions.SelectQuery;
import ru.tinkoff.qa.neptune.spring.data.SpringDataContext;
import ru.tinkoff.qa.neptune.spring.data.captors.EntitiesCaptor;

import java.util.List;
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
@SequentialGetStepSupplier.DefineCriteriaParameterName("Criteria of an item of resulted iterable")
public abstract class GetIterableFromEntities<T, M, S extends GetIterableFromEntities<T, M, S>>
        extends SequentialGetStepSupplier.GetIterableChainedStepSupplier<SpringDataContext, List<T>, Iterable<M>, T, S>
        implements SelectQuery<List<T>> {

    protected GetIterableFromEntities(Function<M, T> originalFunction) {
        super(ms -> stream(ms.spliterator(), false)
                .map(originalFunction)
                .collect(toList()));
    }

    static <T, M, ID, R extends Repository<M, ID>> GetIterableFromEntities<T, M, ?> getIterableFromEntities(
            SelectManyStepSupplier<M, ID, R> from,
            Function<M, T> f) {
        return new GetIterableFromEntitiesImpl<>(f).from(from);
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

    public static final class GetIterableFromEntitiesImpl<T, M>
            extends GetIterableFromEntities<T, M, GetIterableFromEntitiesImpl<T, M>> {

        private GetIterableFromEntitiesImpl(Function<M, T> originalFunction) {
            super(originalFunction);
        }

        public <ID, R extends Repository<M, ID>> GetIterableFromEntitiesImpl<T, M> setRepository(R repository) {
            ofNullable(getFrom()).ifPresent(o -> ((SelectOneStepSupplier.SelectOneStepSupplierImpl<M, ID, R>) o).from(repository));
            return this;
        }

        @Override
        public GetIterableFromEntitiesImpl<T, M> setDescription(String description) {
            ofNullable(getFrom()).ifPresent(o -> ((SelectOneStepSupplier.SelectOneStepSupplierImpl<?, ?, ?>) o).setDescription(description));
            return this;
        }
    }
}
