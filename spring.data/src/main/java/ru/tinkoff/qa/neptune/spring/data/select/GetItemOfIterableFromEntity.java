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
@SequentialGetStepSupplier.DefineCriteriaParameterName("Result criteria")
public abstract class GetItemOfIterableFromEntity<T, I extends Iterable<T>, M, S extends GetItemOfIterableFromEntity<T, I, M, S>>
        extends SequentialGetStepSupplier.GetObjectFromIterableChainedStepSupplier<SpringDataContext, T, M, S>
        implements SelectQuery<T> {

    private GetItemOfIterableFromEntity(Function<M, I> originalFunction) {
        super(originalFunction);
    }

    static <T, I extends Iterable<T>, M, ID, R extends Repository<M, ID>> GetItemOfIterableFromEntity<T, I, M, ?> getIterableItemFromEntity(
            SelectOneStepSupplier<M, ID, R> from,
            Function<M, I> f) {
        return new GetItemOfIterableFromEntityImpl<>(f).from(from);
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

    public static final class GetItemOfIterableFromEntityImpl<T, I extends Iterable<T>, M>
            extends GetItemOfIterableFromEntity<T, I, M, GetItemOfIterableFromEntityImpl<T, I, M>> {

        private GetItemOfIterableFromEntityImpl(Function<M, I> originalFunction) {
            super(originalFunction);
        }

        public <ID, R extends Repository<M, ID>> GetItemOfIterableFromEntityImpl<T, I, M> setRepository(R repository) {
            ofNullable(getFrom()).ifPresent(o -> ((SelectOneStepSupplier.SelectOneStepSupplierImpl<M, ID, R>) o).from(repository));
            return this;
        }

        @Override
        public GetItemOfIterableFromEntityImpl<T, I, M> setDescription(String description) {
            ofNullable(getFrom()).ifPresent(o -> ((SelectOneStepSupplier.SelectOneStepSupplierImpl<?, ?, ?>) o).setDescription(description));
            return this;
        }
    }
}
