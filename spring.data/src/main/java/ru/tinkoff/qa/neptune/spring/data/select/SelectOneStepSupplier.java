package ru.tinkoff.qa.neptune.spring.data.select;

import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.repository.Repository;
import ru.tinkoff.qa.neptune.core.api.event.firing.annotations.CaptureOnSuccess;
import ru.tinkoff.qa.neptune.core.api.event.firing.annotations.MaxDepthOfReporting;
import ru.tinkoff.qa.neptune.core.api.steps.SequentialGetStepSupplier;
import ru.tinkoff.qa.neptune.database.abstractions.SelectQuery;
import ru.tinkoff.qa.neptune.spring.data.SpringDataContext;
import ru.tinkoff.qa.neptune.spring.data.captors.EntitiesCaptor;
import ru.tinkoff.qa.neptune.spring.data.select.by.SelectionByMethod;

import java.time.Duration;
import java.util.Map;
import java.util.function.Function;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.springframework.data.domain.ExampleMatcher.matching;
import static ru.tinkoff.qa.neptune.spring.data.select.GetArrayFromEntity.getArrayFromEntity;
import static ru.tinkoff.qa.neptune.spring.data.select.GetItemOfArrayFromEntity.getArrayItemFromEntity;
import static ru.tinkoff.qa.neptune.spring.data.select.GetItemOfIterableFromEntity.getIterableItemFromEntity;
import static ru.tinkoff.qa.neptune.spring.data.select.GetIterableFromEntity.getIterableFromEntity;
import static ru.tinkoff.qa.neptune.spring.data.select.GetObjectFromEntity.getObjectFromEntity;
import static ru.tinkoff.qa.neptune.spring.data.select.by.SelectionByExample.getSingleByExample;
import static ru.tinkoff.qa.neptune.spring.data.select.by.SelectionByIds.getSingleById;

@MaxDepthOfReporting(0)
@SequentialGetStepSupplier.DefineFromParameterName("Repository")
@SequentialGetStepSupplier.DefineGetImperativeParameterName("Select:")
@SequentialGetStepSupplier.DefineTimeOutParameterName("Time to select required entity")
@SequentialGetStepSupplier.DefineCriteriaParameterName("Entity criteria")
@CaptureOnSuccess(by = EntitiesCaptor.class)
public abstract class SelectOneStepSupplier<R, ID, T extends Repository<R, ID>>
        extends SequentialGetStepSupplier.GetObjectChainedStepSupplier<SpringDataContext, R, T, SelectOneStepSupplier<R, ID, T>>
        implements SelectQuery<R> {

    private final SelectionAdditionalArgumentsFactory additionalArgumentsFactory;

    private SelectOneStepSupplier(Function<T, R> select) {
        super(select);
        checkNotNull(select);
        additionalArgumentsFactory = new SelectionAdditionalArgumentsFactory(select);
        addIgnored(Throwable.class);
    }

    public static <R, ID, T extends Repository<R, ID>> SelectOneStepSupplier<R, ID, T> byId(ID id) {
        return new SelectOneStepSupplierImpl<>(getSingleById(id));
    }

    public static <R, ID, T extends Repository<R, ID>> SelectOneStepSupplier<R, ID, T> byExample(R probe,
                                                                                                 ExampleMatcher matcher) {
        return new SelectOneStepSupplierImpl<>(getSingleByExample(probe, matcher));
    }

    public static <R, ID, T extends Repository<R, ID>> SelectOneStepSupplier<R, ID, T> byExample(R probe) {
        return byExample(probe, matching());
    }

    public static <R, ID, T extends Repository<R, ID>> SelectOneStepSupplier<R, ID, T> by(Function<T, R> f) {
        return new SelectOneStepSupplierImpl<>(new SelectionByMethod<>(f));
    }

    @Override
    public SelectOneStepSupplier<R, ID, T> timeOut(Duration timeOut) {
        return super.timeOut(timeOut);
    }

    @Override
    protected Map<String, String> additionalParameters() {
        return additionalArgumentsFactory.getAdditionalParameters();
    }

    <S> GetObjectFromEntity<S, R, ?> thenGetObject(Function<R, S> f) {
        return getObjectFromEntity(this, f);
    }

    <ITEM, S extends Iterable<ITEM>> GetIterableFromEntity<ITEM, S, R, ?> thenGetIterable(Function<R, S> f) {
        return getIterableFromEntity(this, f);
    }

    <S> GetArrayFromEntity<S, R, ?> thenGetArray(Function<R, S[]> f) {
        return getArrayFromEntity(this, f);
    }

    <ITEM, S extends Iterable<ITEM>> GetItemOfIterableFromEntity<ITEM, S, R, ?> thenGetIterableItem(Function<R, S> f) {
        return getIterableItemFromEntity(this, f);
    }

    <S> GetItemOfArrayFromEntity<S, R, ?> thenGetArrayItem(Function<R, S[]> f) {
        return getArrayItemFromEntity(this, f);
    }

    public static final class SelectOneStepSupplierImpl<R, ID, T extends Repository<R, ID>> extends SelectOneStepSupplier<R, ID, T> {

        private SelectOneStepSupplierImpl(Function<T, R> select) {
            super(select);
        }

        @Override
        public SelectOneStepSupplier<R, ID, T> from(T from) {
            return super.from(from);
        }

        @Override
        public SelectOneStepSupplier<R, ID, T> setDescription(String description) {
            checkArgument(isNotBlank(description), "Description should be defined");
            return super.setDescription(description);
        }
    }
}
