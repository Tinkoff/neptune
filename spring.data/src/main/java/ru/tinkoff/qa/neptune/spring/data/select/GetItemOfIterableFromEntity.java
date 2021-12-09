package ru.tinkoff.qa.neptune.spring.data.select;

import org.springframework.data.repository.Repository;
import ru.tinkoff.qa.neptune.core.api.event.firing.annotations.CaptureOnSuccess;
import ru.tinkoff.qa.neptune.core.api.steps.SequentialGetStepSupplier;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.IncludeParamsOfInnerGetterStep;
import ru.tinkoff.qa.neptune.database.abstractions.SelectQuery;
import ru.tinkoff.qa.neptune.database.abstractions.captors.DataCaptor;
import ru.tinkoff.qa.neptune.database.abstractions.dictionary.RequiredEntity;
import ru.tinkoff.qa.neptune.spring.data.SpringDataContext;

import java.util.function.Function;

import static java.util.Optional.ofNullable;

@CaptureOnSuccess(by = DataCaptor.class)
@SequentialGetStepSupplier.DefineCriteriaParameterName
public abstract class GetItemOfIterableFromEntity<T, I extends Iterable<T>, M>
        extends SequentialGetStepSupplier.GetObjectFromIterableChainedStepSupplier<SpringDataContext, T, M, GetItemOfIterableFromEntity<T, I, M>>
        implements SelectQuery<T> {

    private GetItemOfIterableFromEntity(Function<M, I> originalFunction) {
        super(originalFunction);
    }

    static <T, I extends Iterable<T>, M, ID, R extends Repository<M, ID>> GetItemOfIterableFromEntity<T, I, M> getIterableItemFromEntity(
            SelectOneStepSupplier<M, ID, R> from,
            Function<M, I> f) {
        return new GetItemOfIterableFromEntityImpl<>(f).from(from);
    }

    @IncludeParamsOfInnerGetterStep
    public static final class GetItemOfIterableFromEntityImpl<T, I extends Iterable<T>, M>
            extends GetItemOfIterableFromEntity<T, I, M> {

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