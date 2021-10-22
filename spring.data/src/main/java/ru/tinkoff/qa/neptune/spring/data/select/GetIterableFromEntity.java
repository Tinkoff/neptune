package ru.tinkoff.qa.neptune.spring.data.select;

import org.springframework.data.repository.Repository;
import ru.tinkoff.qa.neptune.core.api.steps.SequentialGetStepSupplier;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.IncludeParamsOfInnerGetterStep;
import ru.tinkoff.qa.neptune.database.abstractions.SelectQuery;
import ru.tinkoff.qa.neptune.spring.data.SpringDataContext;

import java.util.function.Function;

import static java.util.Optional.ofNullable;
import static org.apache.commons.lang3.StringUtils.EMPTY;

/**
 * Gets some {@link Iterable} from selected entity.
 *
 * @param <T> is a type of object to get
 * @param <M> is a type of entity
 */
@SuppressWarnings("unchecked")
@IncludeParamsOfInnerGetterStep
@SequentialGetStepSupplier.DefineCriteriaParameterName("Criteria of an item of resulted iterable")
public abstract class GetIterableFromEntity<T, I extends Iterable<T>, M, S extends GetIterableFromEntity<T, I, M, S>>
        extends SequentialGetStepSupplier.GetIterableChainedStepSupplier<SpringDataContext, I, M, T, S>
        implements SelectQuery<I> {

    private GetIterableFromEntity(Function<M, I> originalFunction) {
        super(originalFunction);
    }

    static <T, I extends Iterable<T>, M, ID, R extends Repository<M, ID>> GetIterableFromEntity<T, I, M, ?> getIterableFromEntity(
            SelectOneStepSupplier<M, ID, R> from,
            Function<M, I> f) {
        return new GetIterableFromEntityImpl<>(f).from(from);
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

    public static final class GetIterableFromEntityImpl<T, I extends Iterable<T>, M>
            extends GetIterableFromEntity<T, I, M, GetIterableFromEntityImpl<T, I, M>> {

        private GetIterableFromEntityImpl(Function<M, I> originalFunction) {
            super(originalFunction);
        }

        public <ID, R extends Repository<M, ID>> GetIterableFromEntityImpl<T, I, M> setRepository(R repository) {
            ofNullable(getFrom()).ifPresent(o -> ((SelectOneStepSupplier.SelectOneStepSupplierImpl<M, ID, R>) o).from(repository));
            return this;
        }

        @Override
        public GetIterableFromEntityImpl<T, I, M> setDescription(String description) {
            ofNullable(getFrom()).ifPresent(o -> ((SelectOneStepSupplier.SelectOneStepSupplierImpl<?, ?, ?>) o).setDescription(description));
            return this;
        }
    }
}
