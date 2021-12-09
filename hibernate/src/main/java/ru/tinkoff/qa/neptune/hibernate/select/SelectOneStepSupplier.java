package ru.tinkoff.qa.neptune.hibernate.select;

import ru.tinkoff.qa.neptune.core.api.event.firing.annotations.CaptureOnSuccess;
import ru.tinkoff.qa.neptune.core.api.event.firing.annotations.MaxDepthOfReporting;
import ru.tinkoff.qa.neptune.core.api.steps.SequentialGetStepSupplier;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.StepParameter;
import ru.tinkoff.qa.neptune.database.abstractions.SelectQuery;
import ru.tinkoff.qa.neptune.database.abstractions.captors.DataCaptor;
import ru.tinkoff.qa.neptune.hibernate.HibernateContext;
import ru.tinkoff.qa.neptune.hibernate.dictionary.EntityParameterValueGetter;

import java.time.Duration;
import java.util.Map;
import java.util.function.Function;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static ru.tinkoff.qa.neptune.hibernate.properties.HibernateWaitingSelectedResultDuration.HIBERNATE_SLEEPING_TIME;
import static ru.tinkoff.qa.neptune.hibernate.properties.HibernateWaitingSelectedResultDuration.HIBERNATE_WAITING_FOR_SELECTION_RESULT_TIME;
import static ru.tinkoff.qa.neptune.hibernate.select.GetArrayFromEntity.getArrayFromEntity;
import static ru.tinkoff.qa.neptune.hibernate.select.GetItemOfArrayFromEntity.getArrayItemFromEntity;
import static ru.tinkoff.qa.neptune.hibernate.select.GetItemOfIterableFromEntity.getIterableItemFromEntity;
import static ru.tinkoff.qa.neptune.hibernate.select.GetIterableFromEntity.getIterableFromEntity;
import static ru.tinkoff.qa.neptune.hibernate.select.GetObjectFromEntity.getObjectFromEntity;

@MaxDepthOfReporting(0)
@SequentialGetStepSupplier.DefineGetImperativeParameterName("Select:")
@SequentialGetStepSupplier.DefineTimeOutParameterName("Time to select required entity")
@SequentialGetStepSupplier.DefineCriteriaParameterName("Entity criteria")
@CaptureOnSuccess(by = DataCaptor.class)
public abstract class SelectOneStepSupplier<R>
        extends SequentialGetStepSupplier.GetObjectChainedStepSupplier<HibernateContext, R, Class<?>, SelectOneStepSupplier<R>>
        implements SelectQuery<R> {

    @StepParameter(value = "Entity", makeReadableBy = EntityParameterValueGetter.class)
    Class<?> entity;

    @StepParameter(value = "selected by")
    final Function<Class<?>, R> select;

    private final SelectionAdditionalArgumentsFactory additionalArgumentsFactory;

    protected SelectOneStepSupplier(Class<?> entity, Function<Class<?>, R> select) {
        super(select);
        checkNotNull(select);
        this.select = select;
        additionalArgumentsFactory = new SelectionAdditionalArgumentsFactory(select);
        addIgnored(Throwable.class);
        timeOut(HIBERNATE_WAITING_FOR_SELECTION_RESULT_TIME.get());
        pollingInterval(HIBERNATE_SLEEPING_TIME.get());
        from(entity);
    }

    @Override
    protected SelectOneStepSupplier<R> from(Class<?> from) {
        entity = from;
        return super.from(from);
    }

    Class<?> getEntity() {
        return (Class<?>) getFrom();
    }

    @Override
    protected SelectOneStepSupplier<R> setDescription(String description) {
        checkArgument(isNotBlank(description), "Description should be defined");
        return super.setDescription(description);
    }

    @Override
    public SelectOneStepSupplier<R> timeOut(Duration timeOut) {
        return super.timeOut(timeOut);
    }

    @Override
    public SelectOneStepSupplier<R> pollingInterval(Duration pollingTime) {
        return super.pollingInterval(pollingTime);
    }

    @Override
    protected Map<String, String> additionalParameters() {
        return additionalArgumentsFactory.getAdditionalParameters();
    }

    public <S> GetObjectFromEntity<S, R, ?> thenGetObject(Function<R, S> f) {
        return getObjectFromEntity(this, f);
    }

    public <ITEM, S extends Iterable<ITEM>> GetIterableFromEntity<ITEM, S, R, ?> thenGetIterable(Function<R, S> f) {
        return getIterableFromEntity(this, f);
    }

    public <S> GetArrayFromEntity<S, R, ?> thenGetArray(Function<R, S[]> f) {
        return getArrayFromEntity(this, f);
    }

    public <ITEM, S extends Iterable<ITEM>> GetItemOfIterableFromEntity<ITEM, S, R, ?> thenGetIterableItem(Function<R, S> f) {
        return getIterableItemFromEntity(this, f);
    }

    public <S> GetItemOfArrayFromEntity<S, R, ?> thenGetArrayItem(Function<R, S[]> f) {
        return getArrayItemFromEntity(this, f);
    }
}
