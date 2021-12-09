package ru.tinkoff.qa.neptune.hibernate.select;

import ru.tinkoff.qa.neptune.core.api.event.firing.annotations.CaptureOnSuccess;
import ru.tinkoff.qa.neptune.core.api.steps.SequentialGetStepSupplier;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.IncludeParamsOfInnerGetterStep;
import ru.tinkoff.qa.neptune.database.abstractions.SelectQuery;
import ru.tinkoff.qa.neptune.database.abstractions.captors.DataCaptor;
import ru.tinkoff.qa.neptune.database.abstractions.dictionary.RequiredEntity;
import ru.tinkoff.qa.neptune.hibernate.HibernateContext;

import java.util.function.Function;

import static java.util.Optional.ofNullable;

/**
 * Gets some {@link Iterable} from selected entity.
 *
 * @param <T> is a type of object to get
 * @param <M> is a type of entity
 */
@CaptureOnSuccess(by = DataCaptor.class)
@SequentialGetStepSupplier.DefineCriteriaParameterName("Criteria of an item of resulted array")
public abstract class GetArrayFromEntity<T, M, S extends GetArrayFromEntity<T, M, S>>
        extends SequentialGetStepSupplier.GetArrayChainedStepSupplier<HibernateContext, T, M, S>
        implements SelectQuery<T[]> {

    private GetArrayFromEntity(Function<M, T[]> originalFunction) {
        super(originalFunction);
    }

    static <T, M> GetArrayFromEntity<T, M, ?> getArrayFromEntity(
            SelectOneStepSupplier<M> from,
            Function<M, T[]> f) {
        return new GetArrayFromEntityImpl<>(f).from(from);
    }

    @IncludeParamsOfInnerGetterStep
    public static final class GetArrayFromEntityImpl<T, M>
            extends GetArrayFromEntity<T, M, GetArrayFromEntityImpl<T, M>> {

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
