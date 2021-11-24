package ru.tinkoff.qa.neptune.spring.data.select;

import org.springframework.data.repository.Repository;
import ru.tinkoff.qa.neptune.core.api.event.firing.annotations.CaptureOnSuccess;
import ru.tinkoff.qa.neptune.core.api.event.firing.annotations.MaxDepthOfReporting;
import ru.tinkoff.qa.neptune.core.api.steps.SequentialGetStepSupplier;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.StepParameter;
import ru.tinkoff.qa.neptune.database.abstractions.SelectQuery;
import ru.tinkoff.qa.neptune.spring.data.SpringDataContext;
import ru.tinkoff.qa.neptune.database.abstractions.captors.DataCaptor;
import ru.tinkoff.qa.neptune.spring.data.dictionary.RepositoryParameterValueGetter;

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
import static ru.tinkoff.qa.neptune.spring.data.select.GetListFromEntity.getListFromEntity;
import static ru.tinkoff.qa.neptune.spring.data.select.GetObjectFromEntity.getObjectFromEntity;

@SuppressWarnings("unchecked")
@MaxDepthOfReporting(0)
@SequentialGetStepSupplier.DefineGetImperativeParameterName("Select:")
@SequentialGetStepSupplier.DefineTimeOutParameterName("Time to select required entity")
@SequentialGetStepSupplier.DefineCriteriaParameterName("Entity criteria")
@CaptureOnSuccess(by = DataCaptor.class)
public abstract class SelectOneStepSupplier<R, ID, T extends Repository<R, ID>>
        extends SequentialGetStepSupplier.GetObjectChainedStepSupplier<SpringDataContext, R, T, SelectOneStepSupplier<R, ID, T>>
        implements SelectQuery<R> {

    @StepParameter(value = "Repository", makeReadableBy = RepositoryParameterValueGetter.class)
    T repository;

    @StepParameter(value = "selected by")
    final Function<T, R> select;

    private final SelectionAdditionalArgumentsFactory additionalArgumentsFactory;

    protected SelectOneStepSupplier(T repository, Function<T, R> select) {
        super(select);
        checkNotNull(select);
        this.select = select;
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

    /**
     * Creates a step that returns an object calculated or taken from selected entity-object
     *
     * @param f   describes how to get desired data from the entity-object
     * @param <S> is a type of desired result
     * @return step that returns an object after execution.
     */
    public <S> GetObjectFromEntity<S, R> thenGetObject(Function<R, S> f) {
        return getObjectFromEntity(this, f);
    }

    /**
     * Creates a step that returns a list calculated or taken from selected entity-object
     *
     * @param f      describes how to get desired data from the entity-object
     * @param <ITEM> is a type of resulted list item
     * @param <S>    is a type of iterable to get. This iterable is converted to list which
     *               consists of required objects
     * @return step that returns a list after execution.
     */
    public <ITEM, S extends Iterable<ITEM>> GetListFromEntity<ITEM, S, R> thenGetList(Function<R, S> f) {
        return getListFromEntity(this, f);
    }

    /**
     * Creates a step that returns an array calculated or taken from selected entity-object
     *
     * @param f   describes how to get desired data from the entity-object
     * @param <S> is a type of array item
     * @return step that returns an array after execution.
     */
    public <S> GetArrayFromEntity<S, R> thenGetArray(Function<R, S[]> f) {
        return getArrayFromEntity(this, f);
    }

    /**
     * Creates a step that returns an object taken from an iterable calculated or taken from selected entity-object
     *
     * @param f      is how to get an iterable to take the resulted object from
     * @param <ITEM> is a type of iterable item
     * @param <S>    is a type of iterable to take the resulted object from
     * @return step that returns an object after execution.
     */
    public <ITEM, S extends Iterable<ITEM>> GetItemOfIterableFromEntity<ITEM, S, R> thenGetIterableItem(Function<R, S> f) {
        return getIterableItemFromEntity(this, f);
    }

    /**
     * Creates a step that returns an object taken from an array calculated or taken from selected entity-object
     *
     * @param f   is how to get an array to take the resulted object from
     * @param <S> is a type of array item
     * @return step that returns an object after execution.
     */
    public <S> GetItemOfArrayFromEntity<S, R> thenGetArrayItem(Function<R, S[]> f) {
        return getArrayItemFromEntity(this, f);
    }
}
