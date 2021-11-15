package ru.tinkoff.qa.neptune.spring.data.select;

import org.springframework.data.repository.Repository;
import ru.tinkoff.qa.neptune.core.api.event.firing.annotations.CaptureOnSuccess;
import ru.tinkoff.qa.neptune.core.api.steps.SequentialGetStepSupplier;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.IncludeParamsOfInnerGetterStep;
import ru.tinkoff.qa.neptune.database.abstractions.SelectQuery;
import ru.tinkoff.qa.neptune.spring.data.SpringDataContext;
import ru.tinkoff.qa.neptune.spring.data.captors.EntitiesCaptor;
import ru.tinkoff.qa.neptune.spring.data.dictionary.RequiredEntity;

import java.util.function.Function;

import static java.util.Optional.ofNullable;

@CaptureOnSuccess(by = EntitiesCaptor.class)
@SequentialGetStepSupplier.DefineCriteriaParameterName
public abstract class GetObjectFromEntity<T, M> extends SequentialGetStepSupplier
        .GetObjectChainedStepSupplier<SpringDataContext, T, M, GetObjectFromEntity<T, M>>
        implements SelectQuery<T> {

    private GetObjectFromEntity(Function<M, T> originalFunction) {
        super(originalFunction);
    }

    static <T, M, ID, R extends Repository<M, ID>> GetObjectFromEntity<T, M> getObjectFromEntity(
            SelectOneStepSupplier<M, ID, R> from,
            Function<M, T> f) {
        return new GetObjectFromEntityImpl<>(f).from(from);
    }

    @IncludeParamsOfInnerGetterStep
    public static final class GetObjectFromEntityImpl<T, M>
            extends GetObjectFromEntity<T, M> {

        private GetObjectFromEntityImpl(Function<M, T> originalFunction) {
            super(originalFunction);
        }

        @Override
        public GetObjectFromEntityImpl<T, M> setDescription(String description) {
            super.setDescription(description);
            ofNullable(getFrom()).ifPresent(o -> ((SetsDescription) o).changeDescription(new RequiredEntity().toString()));
            return this;
        }
    }
}