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
 * Gets some object from selected entity.
 *
 * @param <T> is a type of object to get
 * @param <M> is a type of entity
 */
@CaptureOnSuccess(by = DataCaptor.class)
@SequentialGetStepSupplier.DefineCriteriaParameterName("Result criteria")
public abstract class GetObjectFromEntity<T, M, S extends GetObjectFromEntity<T, M, S>> extends SequentialGetStepSupplier
        .GetObjectChainedStepSupplier<HibernateContext, T, M, S>
        implements SelectQuery<T> {

    private GetObjectFromEntity(Function<M, T> originalFunction) {
        super(originalFunction);
    }

    static <T, M> GetObjectFromEntity<T, M, ?> getObjectFromEntity(
            SelectOneStepSupplier<M> from,
            Function<M, T> f) {
        return new GetObjectFromEntityImpl<>(f).from(from);
    }

    @Override
    protected S from(SequentialGetStepSupplier<HibernateContext, ? extends M, ?, ?, ?> from) {
        setDescription(from.toString());
        return super.from(from);
    }

    @IncludeParamsOfInnerGetterStep
    public static final class GetObjectFromEntityImpl<T, M>
            extends GetObjectFromEntity<T, M, GetObjectFromEntityImpl<T, M>> {

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
