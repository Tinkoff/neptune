package ru.tinkoff.qa.neptune.hibernate.select;

import ru.tinkoff.qa.neptune.core.api.event.firing.annotations.CaptureOnSuccess;
import ru.tinkoff.qa.neptune.core.api.steps.SequentialGetStepSupplier;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.IncludeParamsOfInnerGetterStep;
import ru.tinkoff.qa.neptune.database.abstractions.SelectQuery;
import ru.tinkoff.qa.neptune.database.abstractions.captors.DataCaptor;
import ru.tinkoff.qa.neptune.database.abstractions.dictionary.RequiredEntity;
import ru.tinkoff.qa.neptune.hibernate.HibernateContext;

import java.util.List;
import java.util.function.Function;

import static java.util.Optional.ofNullable;

@CaptureOnSuccess(by = DataCaptor.class)
@SequentialGetStepSupplier.DefineCriteriaParameterName("Criteria of every item")
public abstract class GetListFromEntity<T, I extends Iterable<T>, M>
        extends SequentialGetStepSupplier.GetListChainedStepSupplier<HibernateContext, I, M, T, GetListFromEntity<T, I, M>>
        implements SelectQuery<List<T>> {

    private GetListFromEntity(Function<M, I> originalFunction) {
        super(originalFunction);
    }

    static <T, I extends Iterable<T>, M> GetListFromEntity<T, I, M> getListFromEntity(
            SelectOneStepSupplier<M> from,
            Function<M, I> f) {
        return new GetListFromEntityImpl<>(f).from(from);
    }

    @IncludeParamsOfInnerGetterStep
    public static final class GetListFromEntityImpl<T, I extends Iterable<T>, M>
            extends GetListFromEntity<T, I, M> {

        private GetListFromEntityImpl(Function<M, I> originalFunction) {
            super(originalFunction);
        }

        @Override
        public GetListFromEntityImpl<T, I, M> setDescription(String description) {
            super.setDescription(description);
            ofNullable(getFrom()).ifPresent(o -> ((SetsDescription) o).changeDescription(new RequiredEntity().toString()));
            return this;
        }
    }
}