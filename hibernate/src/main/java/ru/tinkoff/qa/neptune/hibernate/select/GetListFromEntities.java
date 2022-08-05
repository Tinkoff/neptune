package ru.tinkoff.qa.neptune.hibernate.select;

import ru.tinkoff.qa.neptune.core.api.event.firing.annotations.CaptureOnSuccess;
import ru.tinkoff.qa.neptune.core.api.steps.SequentialGetStepSupplier;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.IncludeParamsOfInnerGetterStep;
import ru.tinkoff.qa.neptune.database.abstractions.SelectQuery;
import ru.tinkoff.qa.neptune.database.abstractions.captors.DataCaptor;
import ru.tinkoff.qa.neptune.database.abstractions.dictionary.RequiredEntities;
import ru.tinkoff.qa.neptune.hibernate.HibernateContext;

import java.util.List;
import java.util.function.Function;

import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;
import static java.util.stream.StreamSupport.stream;

@CaptureOnSuccess(by = DataCaptor.class)
@SequentialGetStepSupplier.DefineCriteriaParameterName("Criteria of every item")
public abstract class GetListFromEntities<T, M>
        extends SequentialGetStepSupplier.GetListChainedStepSupplier<HibernateContext, List<T>, Iterable<M>, T, GetListFromEntities<T, M>>
        implements SelectQuery<List<T>> {

    private GetListFromEntities(Function<M, T> originalFunction) {
        super(ms -> stream(ms.spliterator(), false)
                .map(originalFunction)
                .collect(toList()));
    }

    static <T, M> GetListFromEntities<T, M> getListFromEntities(
            SelectManyStepSupplier<M> from,
            Function<M, T> f) {
        return new GetListFromEntitiesImpl<>(f).from(from);
    }

    @IncludeParamsOfInnerGetterStep
    public static final class GetListFromEntitiesImpl<T, M>
            extends GetListFromEntities<T, M> {

        private GetListFromEntitiesImpl(Function<M, T> originalFunction) {
            super(originalFunction);
        }

        @Override
        public GetListFromEntitiesImpl<T, M> setDescription(String description) {
            super.setDescription(description);
            ofNullable(getFrom()).ifPresent(o -> ((SetsDescription) o).changeDescription(new RequiredEntities().toString()));
            return this;
        }
    }
}