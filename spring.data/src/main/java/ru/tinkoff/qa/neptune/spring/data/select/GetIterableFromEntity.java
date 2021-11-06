package ru.tinkoff.qa.neptune.spring.data.select;

import org.springframework.data.repository.Repository;
import ru.tinkoff.qa.neptune.core.api.event.firing.annotations.CaptureOnSuccess;
import ru.tinkoff.qa.neptune.core.api.steps.SequentialGetStepSupplier;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.IncludeParamsOfInnerGetterStep;
import ru.tinkoff.qa.neptune.database.abstractions.SelectQuery;
import ru.tinkoff.qa.neptune.spring.data.SpringDataContext;
import ru.tinkoff.qa.neptune.spring.data.captors.EntitiesCaptor;
import ru.tinkoff.qa.neptune.spring.data.dictionary.RequiredEntity;

import java.util.List;
import java.util.function.Function;

import static java.util.Optional.ofNullable;

/**
 * Gets some {@link Iterable} from selected entity.
 *
 * @param <T> is a type of object to get
 * @param <M> is a type of entity
 */
@CaptureOnSuccess(by = EntitiesCaptor.class)
@SequentialGetStepSupplier.DefineCriteriaParameterName("Criteria of an item of resulted iterable")
public abstract class GetIterableFromEntity<T, I extends Iterable<T>, M, S extends GetIterableFromEntity<T, I, M, S>>
        extends SequentialGetStepSupplier.GetListChainedStepSupplier<SpringDataContext, I, M, T, S>
        implements SelectQuery<List<T>> {

    private GetIterableFromEntity(Function<M, I> originalFunction) {
        super(originalFunction);
    }

    static <T, I extends Iterable<T>, M, ID, R extends Repository<M, ID>> GetIterableFromEntity<T, I, M, ?> getIterableFromEntity(
            SelectOneStepSupplier<M, ID, R> from,
            Function<M, I> f) {
        return new GetIterableFromEntityImpl<>(f).from(from);
    }

    @IncludeParamsOfInnerGetterStep
    public static final class GetIterableFromEntityImpl<T, I extends Iterable<T>, M>
            extends GetIterableFromEntity<T, I, M, GetIterableFromEntityImpl<T, I, M>> {

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
