package ru.tinkoff.qa.neptune.hibernate.select;

import ru.tinkoff.qa.neptune.core.api.event.firing.annotations.CaptureOnSuccess;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.MaxDepthOfReporting;
import ru.tinkoff.qa.neptune.core.api.steps.SequentialGetStepSupplier;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.StepParameter;
import ru.tinkoff.qa.neptune.database.abstractions.SelectQuery;
import ru.tinkoff.qa.neptune.database.abstractions.captors.DataCaptor;
import ru.tinkoff.qa.neptune.hibernate.HibernateContext;

import java.time.Duration;
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
import static ru.tinkoff.qa.neptune.hibernate.select.GetListFromEntity.getListFromEntity;
import static ru.tinkoff.qa.neptune.hibernate.select.GetObjectFromEntity.getObjectFromEntity;

@MaxDepthOfReporting(0)
@SequentialGetStepSupplier.DefineGetImperativeParameterName("Select:")
@SequentialGetStepSupplier.DefineTimeOutParameterName("Time to select required entity")
@SequentialGetStepSupplier.DefineCriteriaParameterName("Entity criteria")
@CaptureOnSuccess(by = DataCaptor.class)
public abstract class SelectOneStepSupplier<R>
        extends SequentialGetStepSupplier.GetObjectStepSupplier<HibernateContext, R, SelectOneStepSupplier<R>>
        implements SelectQuery<R> {

    final Function<HibernateContext, R> f;

    @StepParameter(value = "selected by")
    final String select;

    protected SelectOneStepSupplier(Function<HibernateContext, R> select) {
        super(select);
        checkNotNull(select);
        this.select = select.toString();
        this.f = select;
        addIgnored(Throwable.class);
        timeOut(HIBERNATE_WAITING_FOR_SELECTION_RESULT_TIME.get());
        pollingInterval(HIBERNATE_SLEEPING_TIME.get());
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

    public <S> GetObjectFromEntity<S, R> thenGetObject(Function<R, S> f) {
        return getObjectFromEntity(this, f);
    }

    public <ITEM, S extends Iterable<ITEM>> GetIterableFromEntity<ITEM, S, R> thenGetIterable(Function<R, S> f) {
        return getIterableFromEntity(this, f);
    }

    public <S> GetArrayFromEntity<S, R> thenGetArray(Function<R, S[]> f) {
        return getArrayFromEntity(this, f);
    }

    public <ITEM, S extends Iterable<ITEM>> GetItemOfIterableFromEntity<ITEM, S, R> thenGetIterableItem(Function<R, S> f) {
        return getIterableItemFromEntity(this, f);
    }

    public <S> GetItemOfArrayFromEntity<S, R> thenGetArrayItem(Function<R, S[]> f) {
        return getArrayItemFromEntity(this, f);
    }

    public <ITEM, S extends Iterable<ITEM>> GetListFromEntity<ITEM, S, R> thenGetList(Function<R, S> f) {
        return getListFromEntity(this, f);
    }
}
