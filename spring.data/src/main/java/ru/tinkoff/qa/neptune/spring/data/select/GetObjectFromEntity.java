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
import static org.apache.commons.lang3.StringUtils.EMPTY;

/**
 * Gets some object from selected entity.
 *
 * @param <T> is a type of object to get
 * @param <M> is a type of entity
 */
@SuppressWarnings("unchecked")
@CaptureOnSuccess(by = EntitiesCaptor.class)
@IncludeParamsOfInnerGetterStep
@SequentialGetStepSupplier.DefineCriteriaParameterName("Result criteria")
public abstract class GetObjectFromEntity<T, M, S extends GetObjectFromEntity<T, M, S>> extends SequentialGetStepSupplier
        .GetObjectChainedStepSupplier<SpringDataContext, T, M, S>
        implements SelectQuery<T> {

    private GetObjectFromEntity(Function<M, T> originalFunction) {
        super(originalFunction);
    }

    static <T, M, ID, R extends Repository<M, ID>> GetObjectFromEntity<T, M, ?> getObjectFromEntity(
            SelectOneStepSupplier<M, ID, R> from,
            Function<M, T> f) {
        return new GetObjectFromEntityImpl<>(f).from(from);
    }

    @Override
    protected S from(SequentialGetStepSupplier<SpringDataContext, ? extends M, ?, ?, ?> from) {
        setDescription(from.toString());
        return super.from(from);
    }

    @Override
    protected String getDescription() {
        return ofNullable(getFrom())
                .map(Object::toString)
                .orElse(EMPTY);
    }

    public static final class GetObjectFromEntityImpl<T, M>
            extends GetObjectFromEntity<T, M, GetObjectFromEntityImpl<T, M>> {

        private GetObjectFromEntityImpl(Function<M, T> originalFunction) {
            super(originalFunction);
        }

        public <ID, R extends Repository<M, ID>> GetObjectFromEntityImpl<T, M> setRepository(R repository) {
            ofNullable(getFrom()).ifPresent(o -> ((SelectOneStepSupplier.SelectOneStepSupplierImpl<M, ID, R>) o).from(repository));
            return this;
        }

        @Override
        public GetObjectFromEntityImpl<T, M> setDescription(String description) {
            ofNullable(getFrom()).ifPresent(o -> ((SelectOneStepSupplier.SelectOneStepSupplierImpl<?, ?, ?>) o).setDescription(description));
            return this;
        }
    }
}
