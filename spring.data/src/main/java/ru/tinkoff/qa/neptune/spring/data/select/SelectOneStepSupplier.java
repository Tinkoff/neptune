package ru.tinkoff.qa.neptune.spring.data.select;

import org.springframework.data.repository.Repository;
import ru.tinkoff.qa.neptune.core.api.event.firing.annotations.CaptureOnSuccess;
import ru.tinkoff.qa.neptune.core.api.event.firing.annotations.MaxDepthOfReporting;
import ru.tinkoff.qa.neptune.core.api.steps.SequentialGetStepSupplier;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.StepParameter;
import ru.tinkoff.qa.neptune.database.abstractions.SelectQuery;
import ru.tinkoff.qa.neptune.spring.data.RepositoryParameterValueGetter;
import ru.tinkoff.qa.neptune.spring.data.SpringDataContext;
import ru.tinkoff.qa.neptune.spring.data.captors.EntitiesCaptor;

import java.time.Duration;
import java.util.Map;
import java.util.function.Function;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static ru.tinkoff.qa.neptune.spring.data.properties.SpringDataWaitingSelectedResultDuration.SPRING_DATA_SLEEPING_TIME;
import static ru.tinkoff.qa.neptune.spring.data.properties.SpringDataWaitingSelectedResultDuration.SPRING_DATA_WAITING_FOR_SELECTION_RESULT_TIME;
import static ru.tinkoff.qa.neptune.spring.data.select.GetArrayFromEntity.getArrayFromEntity;
import static ru.tinkoff.qa.neptune.spring.data.select.GetItemOfArrayFromEntity.getArrayItemFromEntity;
import static ru.tinkoff.qa.neptune.spring.data.select.GetItemOfIterableFromEntity.getIterableItemFromEntity;
import static ru.tinkoff.qa.neptune.spring.data.select.GetIterableFromEntity.getIterableFromEntity;
import static ru.tinkoff.qa.neptune.spring.data.select.GetObjectFromEntity.getObjectFromEntity;

@SuppressWarnings("unchecked")
@MaxDepthOfReporting(0)
@SequentialGetStepSupplier.DefineGetImperativeParameterName("Select:")
@SequentialGetStepSupplier.DefineTimeOutParameterName("Time to select required entity")
@SequentialGetStepSupplier.DefineCriteriaParameterName("Entity criteria")
@CaptureOnSuccess(by = EntitiesCaptor.class)
public abstract class SelectOneStepSupplier<R, ID, T extends Repository<R, ID>>
        extends SequentialGetStepSupplier.GetObjectChainedStepSupplier<SpringDataContext, R, T, SelectOneStepSupplier<R, ID, T>>
        implements SelectQuery<R> {

    @StepParameter(value = "Repository", makeReadableBy = RepositoryParameterValueGetter.class)
    T repository;

    private final SelectionAdditionalArgumentsFactory additionalArgumentsFactory;

    SelectOneStepSupplier(T repository, Function<T, R> select) {
        super(select);
        checkNotNull(select);
        additionalArgumentsFactory = new SelectionAdditionalArgumentsFactory(select);
        addIgnored(Throwable.class);
        timeOut(SPRING_DATA_WAITING_FOR_SELECTION_RESULT_TIME.get());
        pollingInterval(SPRING_DATA_SLEEPING_TIME.get());
        from(repository);
    }

    @Override
    protected SelectOneStepSupplier<R, ID, T> from(T from) {
        repository = from;
        return super.from(from);
    }

    T getRepository() {
        return (T) getFrom();
    }

    @Override
    protected SelectOneStepSupplier<R, ID, T> setDescription(String description) {
        checkArgument(isNotBlank(description), "Description should be defined");
        return super.setDescription(description);
    }

    @Override
    public SelectOneStepSupplier<R, ID, T> timeOut(Duration timeOut) {
        return super.timeOut(timeOut);
    }

    @Override
    public SelectOneStepSupplier<R, ID, T> pollingInterval(Duration pollingTime) {
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
