package ru.tinkoff.qa.neptune.spring.data.select;

import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.QueryByExampleExecutor;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import ru.tinkoff.qa.neptune.core.api.event.firing.annotations.CaptureOnSuccess;
import ru.tinkoff.qa.neptune.core.api.event.firing.annotations.MaxDepthOfReporting;
import ru.tinkoff.qa.neptune.core.api.steps.SequentialGetStepSupplier;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.StepParameter;
import ru.tinkoff.qa.neptune.database.abstractions.SelectQuery;
import ru.tinkoff.qa.neptune.spring.data.SpringDataContext;
import ru.tinkoff.qa.neptune.spring.data.captors.EntitiesCaptor;
import ru.tinkoff.qa.neptune.spring.data.select.by.SelectionAsPage;
import ru.tinkoff.qa.neptune.spring.data.select.by.SelectionByMethod;
import ru.tinkoff.qa.neptune.spring.data.select.by.SelectionBySorting;

import java.time.Duration;
import java.util.Map;
import java.util.function.Function;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.springframework.data.domain.ExampleMatcher.matching;
import static ru.tinkoff.qa.neptune.core.api.localization.StepLocalization.translate;
import static ru.tinkoff.qa.neptune.spring.data.select.GetIterableFromEntities.getIterableFromEntities;
import static ru.tinkoff.qa.neptune.spring.data.select.GetIterableItemFromEntities.getIterableItemFromEntities;
import static ru.tinkoff.qa.neptune.spring.data.select.by.SelectionByExample.getIterableByExample;
import static ru.tinkoff.qa.neptune.spring.data.select.by.SelectionByIds.getIterableByIds;

@SuppressWarnings("unchecked")
@MaxDepthOfReporting(0)
@SequentialGetStepSupplier.DefineGetImperativeParameterName("Select:")
@SequentialGetStepSupplier.DefineTimeOutParameterName("Time to select required entities")
@SequentialGetStepSupplier.DefineCriteriaParameterName("Criteria for every resulted entity")
@CaptureOnSuccess(by = EntitiesCaptor.class)
public abstract class SelectManyStepSupplier<R, ID, T extends Repository<R, ID>>
        extends SequentialGetStepSupplier.GetIterableChainedStepSupplier<SpringDataContext, Iterable<R>, T, R, SelectManyStepSupplier<R, ID, T>>
        implements SelectQuery<Iterable<R>> {

    private final SelectionAdditionalArgumentsFactory additionalArgumentsFactory;

    private SelectManyStepSupplier(Function<T, Iterable<R>> select) {
        super(select);
        checkNotNull(select);
        additionalArgumentsFactory = new SelectionAdditionalArgumentsFactory(select);
    }

    public static <R, ID, T extends Repository<R, ID>> SelectManyStepSupplier<R, ID, T> byIds(ID... ids) {
        return new SelectManyStepSupplierImpl<>(getIterableByIds(ids));
    }

    public static <R, ID, T extends Repository<R, ID>> SelectManyStepSupplier<R, ID, T> allBySorting(Sort sort) {
        return new SelectManyStepSupplierImpl<>(new SelectionBySorting<>(sort));
    }

    public static <R, ID, T extends PagingAndSortingRepository<R, ID>> SelectManyStepSupplier<R, ID, T> asAPage(Pageable pageable) {
        return new SelectManyStepSupplierImpl<>(new SelectionAsPage<>(pageable));
    }

    public static <R, ID, T extends Repository<R, ID>> SelectManyStepSupplier<R, ID, T> allByExample(R probe,
                                                                                                     ExampleMatcher matcher,
                                                                                                     Sort sort) {
        return new SelectManyStepSupplierImpl<>(getIterableByExample(probe, matcher, sort));
    }

    public static <R, ID, T extends QueryByExampleExecutor<R> & Repository<R, ID>> SelectManyStepSupplier<R, ID, T> allByExample(R probe,
                                                                                                                                 Sort sort) {
        return allByExample(probe, matching(), sort);
    }

    public static <R, ID, T extends ReactiveQueryByExampleExecutor<R> & Repository<R, ID>> SelectManyStepSupplier<R, ID, T> allByExample(
            R probe) {
        return allByExample(probe, matching(), null);
    }

    public static <R, ID, T extends ReactiveQueryByExampleExecutor<R> & Repository<R, ID>> SelectManyStepSupplier<R, ID, T> allByExample(
            R probe,
            ExampleMatcher matcher) {
        return allByExample(probe, matcher, null);
    }

    public static <R, S extends Iterable<R>, ID, T extends Repository<R, ID>> SelectManyStepSupplier<R, ID, T> allBy(Function<T, S> f) {
        return new SelectManyStepSupplierImpl<>(new SelectionByMethod<>((Function<T, Iterable<R>>) f));
    }

    @Override
    protected SelectManyStepSupplier<R, ID, T> timeOut(Duration timeOut) {
        return super.timeOut(timeOut);
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

    public static final class SelectManyStepSupplierImpl<R, ID, T extends Repository<R, ID>>
            extends SelectManyStepSupplier<R, ID, T> {

        @StepParameter("Repository")
        T repository;

        private SelectManyStepSupplierImpl(Function<T, Iterable<R>> select) {
            super(select);
        }

        @Override
        public SelectManyStepSupplier<R, ID, T> from(T from) {
            repository = from;
            return super.from(from);
        }

        @Override
        public SelectManyStepSupplier<R, ID, T> setDescription(String description) {
            checkArgument(isNotBlank(description), "Description should be defined");
            return super.setDescription(translate(description));
        }
    }
}
