package ru.tinkoff.qa.neptune.spring.data.delete;

import com.google.common.collect.Iterables;
import org.springframework.data.repository.Repository;
import ru.tinkoff.qa.neptune.core.api.event.firing.annotations.CaptureOnFailure;
import ru.tinkoff.qa.neptune.core.api.event.firing.annotations.CaptureOnSuccess;
import ru.tinkoff.qa.neptune.core.api.steps.SequentialGetStepSupplier;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.DescriptionFragment;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.IncludeParamsOfInnerGetterStep;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.StepParameter;
import ru.tinkoff.qa.neptune.core.api.steps.parameters.ParameterValueGetter;
import ru.tinkoff.qa.neptune.database.abstractions.SelectQuery;
import ru.tinkoff.qa.neptune.spring.data.SpringDataContext;
import ru.tinkoff.qa.neptune.database.abstractions.captors.DataCaptor;
import ru.tinkoff.qa.neptune.spring.data.dictionary.RepositoryParameterValueGetter;
import ru.tinkoff.qa.neptune.spring.data.select.HasRepositoryInfo;
import ru.tinkoff.qa.neptune.spring.data.select.SelectManyStepSupplier;
import ru.tinkoff.qa.neptune.spring.data.select.SelectOneStepSupplier;
import ru.tinkoff.qa.neptune.spring.data.select.SetsDescription;

import java.util.List;
import java.util.stream.Collectors;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.ALWAYS;
import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static java.util.List.of;
import static java.util.Objects.isNull;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static ru.tinkoff.qa.neptune.core.api.localization.StepLocalization.translate;
import static ru.tinkoff.qa.neptune.database.abstractions.data.serializer.DataSerializer.serializeObject;
import static ru.tinkoff.qa.neptune.database.abstractions.data.serializer.DataSerializer.serializeObjects;

@SuppressWarnings("unchecked")
@IncludeParamsOfInnerGetterStep
@SequentialGetStepSupplier.DefineGetImperativeParameterName("Delete:")
public final class DeleteByQueryStepSupplier<TO_DELETE, R, ID, T extends Repository<R, ID>>
        extends SequentialGetStepSupplier.GetObjectChainedStepSupplier<SpringDataContext, Void, TO_DELETE, DeleteByQueryStepSupplier<TO_DELETE, R, ID, T>>
        implements SelectQuery<Void> {

    @StepParameter(value = "Repository", makeReadableBy = RepositoryParameterValueGetter.class)
    final T repo;

    @CaptureOnSuccess(by = DataCaptor.class)
    @CaptureOnFailure(by = DataCaptor.class)
    List<String> deleted;

    private DeleteByQueryStepSupplier(T repo, DeleteEntities<TO_DELETE, R, ID, T> f) {
        super(f);
        this.repo = repo;
    }

    public static <R, ID, T extends Repository<R, ID>> DeleteByQueryStepSupplier<R, R, ID, T> delete(
            String description,
            SelectOneStepSupplier<R, ID, T> select) {
        checkArgument(isNotBlank(description), "Description should be defined");
        var translated = translate(description);
        var repository = ((HasRepositoryInfo<R, ID, T>) select).getRepository();
        ((SetsDescription) select).changeDescription(translated);
        return new DeleteByQueryStepSupplier<>(repository, new DeleteEntities.DeleteOne<>(repository))
                .setDescription(translated)
                .from(select);
    }

    @Description("{description}")
    public static <R, ID, T extends Repository<R, ID>> DeleteByQueryStepSupplier<R, R, ID, T> delete(
            @DescriptionFragment(value = "description",
                    makeReadableBy = ParameterValueGetter.TranslatedDescriptionParameterValueGetter.class)
                    String description,
            T repository,
            R toDelete) {
        checkArgument(isNotBlank(description), "Description should be defined");
        checkNotNull(toDelete);
        return new DeleteByQueryStepSupplier<>(repository, new DeleteEntities.DeleteOne<>(repository))
                .from(toDelete);
    }

    public static <R, ID, T extends Repository<R, ID>> DeleteByQueryStepSupplier<Iterable<R>, R, ID, T> delete(
            String description,
            SelectManyStepSupplier<R, ID, T> select) {
        checkArgument(isNotBlank(description), "Description should be defined");
        var translated = translate(description);
        var repository = ((HasRepositoryInfo<R, ID, T>) select).getRepository();
        ((SetsDescription) select).changeDescription(translated);
        return new DeleteByQueryStepSupplier<>(repository, new DeleteEntities.DeleteMany<>(repository))
                .setDescription(translated)
                .from(select);
    }

    @Description("{description}")
    public static <R, ID, T extends Repository<R, ID>> DeleteByQueryStepSupplier<Iterable<R>, R, ID, T> delete(
            @DescriptionFragment(value = "description",
                    makeReadableBy = ParameterValueGetter.TranslatedDescriptionParameterValueGetter.class)
                    String description,
            T repository,
            Iterable<R> toDelete) {
        checkArgument(isNotBlank(description), "Description should be defined");
        checkNotNull(toDelete);
        checkArgument(Iterables.size(toDelete) > 0, "At leas one item to delete should be defined");
        return new DeleteByQueryStepSupplier<>(repository, new DeleteEntities.DeleteMany<>(repository))
                .from(toDelete);
    }

    @Override
    protected void onStart(TO_DELETE toDelete) {
        if (isNull(toDelete)) {
            return;
        }

        if (toDelete instanceof Iterable) {
            deleted = serializeObjects(ALWAYS, (Iterable<?>) toDelete).collect(Collectors.toList());
        } else {
            deleted = of(serializeObject(ALWAYS, toDelete));
        }
    }
}
