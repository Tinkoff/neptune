package ru.tinkoff.qa.neptune.spring.data.select;

import org.springframework.data.repository.Repository;
import ru.tinkoff.qa.neptune.core.api.event.firing.annotations.CaptureOnSuccess;
import ru.tinkoff.qa.neptune.core.api.steps.SequentialGetStepSupplier;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.IncludeParamsOfInnerGetterStep;
import ru.tinkoff.qa.neptune.database.abstractions.SelectQuery;
import ru.tinkoff.qa.neptune.database.abstractions.dictionary.RequiredEntity;
import ru.tinkoff.qa.neptune.spring.data.SpringDataContext;
import ru.tinkoff.qa.neptune.database.abstractions.captors.DataCaptor;

import java.util.function.Function;

import static java.util.Optional.ofNullable;

@CaptureOnSuccess(by = DataCaptor.class)
@SequentialGetStepSupplier.DefineCriteriaParameterName("Criteria of every item")
public abstract class GetArrayFromEntity<T, M>
        extends SequentialGetStepSupplier.GetArrayChainedStepSupplier<SpringDataContext, T, M, GetArrayFromEntity<T, M>>
        implements SelectQuery<T[]> {

    private GetArrayFromEntity(Function<M, T[]> originalFunction) {
        super(originalFunction);
    }

    static <T, M, ID, R extends Repository<M, ID>> GetArrayFromEntity<T, M> getArrayFromEntity(
            SelectOneStepSupplier<M, ID, R> from,
            Function<M, T[]> f) {
        return new GetArrayFromEntityImpl<>(f).from(from);
    }

    @IncludeParamsOfInnerGetterStep
    public static final class GetArrayFromEntityImpl<T, M>
            extends GetArrayFromEntity<T, M> {

        private GetArrayFromEntityImpl(Function<M, T[]> originalFunction) {
            super(originalFunction);
        }

        @Override
        public GetArrayFromEntityImpl<T, M> setDescription(String description) {
            super.setDescription(description);
            ofNullable(getFrom()).ifPresent(o -> ((SetsDescription) o).changeDescription(new RequiredEntity().toString()));
            return this;
        }
    }
}