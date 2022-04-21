package ru.tinkoff.qa.neptune.spring.data.select;

import org.springframework.data.repository.Repository;
import ru.tinkoff.qa.neptune.core.api.event.firing.annotations.CaptureOnSuccess;
import ru.tinkoff.qa.neptune.core.api.steps.SequentialGetStepSupplier;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.IncludeParamsOfInnerGetterStep;
import ru.tinkoff.qa.neptune.database.abstractions.SelectQuery;
import ru.tinkoff.qa.neptune.database.abstractions.captors.DataCaptor;
import ru.tinkoff.qa.neptune.database.abstractions.dictionary.RequiredEntity;
import ru.tinkoff.qa.neptune.spring.data.SpringDataContext;

import java.util.List;
import java.util.function.Function;

import static java.util.Optional.ofNullable;

@CaptureOnSuccess(by = DataCaptor.class)
@SequentialGetStepSupplier.DefineCriteriaParameterName("Criteria of every item")
public abstract class GetListFromEntity<T, I extends Iterable<T>, M>
        extends SequentialGetStepSupplier.GetListChainedStepSupplier<SpringDataContext, I, M, T, GetListFromEntity<T, I, M>>
        implements SelectQuery<List<T>> {

    private GetListFromEntity(Function<M, I> originalFunction) {
        super(originalFunction);
    }

    static <T, I extends Iterable<T>, M, ID, R extends Repository<M, ID>> GetListFromEntity<T, I, M> getListFromEntity(
            SelectOneStepSupplier<M, ID, R> from,
            Function<M, I> f) {
        return new GetIterableFromEntityImpl<>(f).from(from);
    }

    @IncludeParamsOfInnerGetterStep
    public static final class GetIterableFromEntityImpl<T, I extends Iterable<T>, M>
            extends GetListFromEntity<T, I, M> {

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