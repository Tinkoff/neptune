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
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static ru.tinkoff.qa.neptune.hibernate.properties.HibernateWaitingSelectedResultDuration.HIBERNATE_SLEEPING_TIME;
import static ru.tinkoff.qa.neptune.hibernate.properties.HibernateWaitingSelectedResultDuration.HIBERNATE_WAITING_FOR_SELECTION_RESULT_TIME;
import static ru.tinkoff.qa.neptune.hibernate.select.GetIterableFromEntities.getIterableFromEntities;
import static ru.tinkoff.qa.neptune.hibernate.select.GetIterableItemFromEntities.getIterableItemFromEntities;

@SuppressWarnings("unchecked")
@MaxDepthOfReporting(0)
@SequentialGetStepSupplier.DefineGetImperativeParameterName("Select:")
@SequentialGetStepSupplier.DefineTimeOutParameterName("Time to select required entities")
@SequentialGetStepSupplier.DefineCriteriaParameterName("Entity criteria")
@CaptureOnSuccess(by = DataCaptor.class)
public abstract class SelectManyStepSupplier<R>
        extends SequentialGetStepSupplier.GetListChainedStepSupplier<HibernateContext, Iterable<R>, Class<?>, R, SelectManyStepSupplier<R>>
        implements SelectQuery<List<R>> {

    @StepParameter(value = "Entity", makeReadableBy = EntityParameterValueGetter.class)
    Class<?> entity;

    @StepParameter(value = "selected by")
    final Function<Class<?>, Iterable<R>> select;

    private final SelectionAdditionalArgumentsFactory additionalArgumentsFactory;

    public SelectManyStepSupplier(Class<?> entity, Function<Class<?>, Iterable<R>> select) {
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
    protected SelectManyStepSupplier<R> from(Class<?> from) {
        entity = from;
        return super.from(from);
    }

    Class<?> getEntity() {
        return (Class<?>) getFrom();
    }

    @Override
    protected SelectManyStepSupplier<R> setDescription(String description) {
        checkArgument(isNotBlank(description), "Description should be defined");
        return super.setDescription(description);
    }

    @Override
    public SelectManyStepSupplier<R> timeOut(Duration timeOut) {
        return super.timeOut(timeOut);
    }

    @Override
    public SelectManyStepSupplier<R> pollingInterval(Duration pollingTime) {
        return super.pollingInterval(pollingTime);
    }

    @Override
    protected Map<String, String> additionalParameters() {
        return additionalArgumentsFactory.getAdditionalParameters();
    }

    public <ITEM> GetIterableFromEntities<ITEM, R, ?> thenGetIterable(Function<R, ITEM> f) {
        return getIterableFromEntities(this, f);
    }

    public <ITEM> GetIterableItemFromEntities<ITEM, R, ?> thenGetIterableItem(Function<R, ITEM> f) {
        return getIterableItemFromEntities(this, f);
    }
}
