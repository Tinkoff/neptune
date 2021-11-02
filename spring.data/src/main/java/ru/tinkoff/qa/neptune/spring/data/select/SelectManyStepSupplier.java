package ru.tinkoff.qa.neptune.spring.data.select;

import org.springframework.data.repository.Repository;
import ru.tinkoff.qa.neptune.core.api.event.firing.annotations.CaptureOnSuccess;
import ru.tinkoff.qa.neptune.core.api.event.firing.annotations.MaxDepthOfReporting;
import ru.tinkoff.qa.neptune.core.api.steps.SequentialGetStepSupplier;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.StepParameter;
import ru.tinkoff.qa.neptune.database.abstractions.SelectQuery;
import ru.tinkoff.qa.neptune.spring.data.SpringDataContext;
import ru.tinkoff.qa.neptune.spring.data.captors.EntitiesCaptor;
import ru.tinkoff.qa.neptune.spring.data.dictionary.RepositoryParameterValueGetter;

import java.time.Duration;
import java.util.Map;
import java.util.function.Function;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static ru.tinkoff.qa.neptune.spring.data.properties.SpringDataWaitingSelectedResultDuration.SPRING_DATA_SLEEPING_TIME;
import static ru.tinkoff.qa.neptune.spring.data.properties.SpringDataWaitingSelectedResultDuration.SPRING_DATA_WAITING_FOR_SELECTION_RESULT_TIME;
import static ru.tinkoff.qa.neptune.spring.data.select.GetIterableFromEntities.getIterableFromEntities;
import static ru.tinkoff.qa.neptune.spring.data.select.GetIterableItemFromEntities.getIterableItemFromEntities;

@SuppressWarnings("unchecked")
@MaxDepthOfReporting(0)
@SequentialGetStepSupplier.DefineGetImperativeParameterName("Select:")
@SequentialGetStepSupplier.DefineTimeOutParameterName("Time to select required entities")
@SequentialGetStepSupplier.DefineCriteriaParameterName("Criteria for every resulted entity")
@CaptureOnSuccess(by = EntitiesCaptor.class)
public abstract class SelectManyStepSupplier<R, ID, T extends Repository<R, ID>>
        extends SequentialGetStepSupplier.GetIterableChainedStepSupplier<SpringDataContext, Iterable<R>, T, R, SelectManyStepSupplier<R, ID, T>>
        implements SelectQuery<Iterable<R>> {

    @StepParameter(value = "Repository", makeReadableBy = RepositoryParameterValueGetter.class)
    T repository;

    @StepParameter(value = "selected by")
    final Function<T, Iterable<R>> select;

    private final SelectionAdditionalArgumentsFactory additionalArgumentsFactory;

    protected SelectManyStepSupplier(T repository, Function<T, Iterable<R>> select) {
        super(select);
        checkNotNull(select);
        this.select = select;
        additionalArgumentsFactory = new SelectionAdditionalArgumentsFactory(select);
        addIgnored(Throwable.class);
        timeOut(SPRING_DATA_WAITING_FOR_SELECTION_RESULT_TIME.get());
        pollingInterval(SPRING_DATA_SLEEPING_TIME.get());
        from(repository);
    }

    T getRepository() {
        return (T) getFrom();
    }

    @Override
    protected SelectManyStepSupplier<R, ID, T> setDescription(String description) {
        checkArgument(isNotBlank(description), "Description should be defined");
        return super.setDescription(description);
    }

    @Override
    protected SelectManyStepSupplier<R, ID, T> from(T from) {
        repository = from;
        return super.from(from);
    }

    @Override
    public SelectManyStepSupplier<R, ID, T> timeOut(Duration timeOut) {
        return super.timeOut(timeOut);
    }

    @Override
    public SelectManyStepSupplier<R, ID, T> pollingInterval(Duration pollingTime) {
        return super.pollingInterval(pollingTime);
    }

    @Override
    protected Map<String, String> additionalParameters() {
        return additionalArgumentsFactory.getAdditionalParameters();
    }

    <ITEM> GetIterableFromEntities<ITEM, R, ?> thenGetIterable(Function<R, ITEM> f) {
        return getIterableFromEntities(this, f);
    }

    <ITEM> GetIterableItemFromEntities<ITEM, R, ?> thenGetIterableItem(Function<R, ITEM> f) {
        return getIterableItemFromEntities(this, f);
    }
}
