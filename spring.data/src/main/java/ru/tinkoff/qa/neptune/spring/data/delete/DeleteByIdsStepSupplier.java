package ru.tinkoff.qa.neptune.spring.data.delete;

import org.springframework.data.repository.Repository;
import ru.tinkoff.qa.neptune.core.api.steps.SequentialGetStepSupplier;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.DescriptionFragment;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.StepParameter;
import ru.tinkoff.qa.neptune.core.api.steps.parameters.ParameterValueGetter;
import ru.tinkoff.qa.neptune.database.abstractions.SelectQuery;
import ru.tinkoff.qa.neptune.spring.data.IDParameterValueGetter;
import ru.tinkoff.qa.neptune.spring.data.RepositoryParameterValueGetter;
import ru.tinkoff.qa.neptune.spring.data.SpringDataContext;

import java.util.function.Function;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static java.util.Arrays.asList;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

@SequentialGetStepSupplier.DefineGetImperativeParameterName("Delete:")
public final class DeleteByIdsStepSupplier<R, ID, T extends Repository<R, ID>>
        extends SequentialGetStepSupplier.GetObjectChainedStepSupplier<SpringDataContext, Void, T, DeleteByIdsStepSupplier<R, ID, T>>
        implements SelectQuery<Void> {

    @StepParameter(value = "Repository", makeReadableBy = RepositoryParameterValueGetter.class)
    T repository;

    @StepParameter(value = "Id(s)", makeReadableBy = IDParameterValueGetter.class)
    final ID[] ids;

    @SafeVarargs
    private DeleteByIdsStepSupplier(T repository, Function<T, Void> originalFunction, ID... ids) {
        super(originalFunction);
        this.ids = ids;
        from(repository);
    }

    @Override
    protected DeleteByIdsStepSupplier<R, ID, T> from(T from) {
        repository = from;
        return super.from(from);
    }

    @SafeVarargs
    @Description("{description}")
    public static <R, ID, T extends Repository<R, ID>> DeleteByIdsStepSupplier<R, ID, T> delete(
            @DescriptionFragment(value = "description",
                    makeReadableBy = ParameterValueGetter.TranslatedDescriptionParameterValueGetter.class)
                    String description,
            T repository,
            ID... ids) {
        checkArgument(isNotBlank(description), "Description should be defined");
        checkNotNull(ids);
        checkArgument(ids.length > 0, "Should be defined at least one id");
        if (ids.length == 1) {
            return new DeleteByIdsStepSupplier<>(repository, new DeleteByIds.DeleteOneById<>(ids[0]), ids);
        }
        return new DeleteByIdsStepSupplier<>(repository, new DeleteByIds.DeleteIterableById<>(asList(ids)), ids);
    }
}
