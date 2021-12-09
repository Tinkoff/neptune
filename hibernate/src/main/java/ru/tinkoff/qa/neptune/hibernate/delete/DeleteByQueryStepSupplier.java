package ru.tinkoff.qa.neptune.hibernate.delete;

import com.google.common.collect.Iterables;
import ru.tinkoff.qa.neptune.core.api.event.firing.annotations.CaptureOnFailure;
import ru.tinkoff.qa.neptune.core.api.event.firing.annotations.CaptureOnSuccess;
import ru.tinkoff.qa.neptune.core.api.steps.SequentialGetStepSupplier;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.DescriptionFragment;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.IncludeParamsOfInnerGetterStep;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.StepParameter;
import ru.tinkoff.qa.neptune.core.api.steps.parameters.ParameterValueGetter;
import ru.tinkoff.qa.neptune.database.abstractions.SelectQuery;
import ru.tinkoff.qa.neptune.database.abstractions.captors.DataCaptor;
import ru.tinkoff.qa.neptune.hibernate.HibernateContext;
import ru.tinkoff.qa.neptune.hibernate.dictionary.EntityParameterValueGetter;
import ru.tinkoff.qa.neptune.hibernate.select.HasEntityInfo;
import ru.tinkoff.qa.neptune.hibernate.select.SelectManyStepSupplier;
import ru.tinkoff.qa.neptune.hibernate.select.SelectOneStepSupplier;
import ru.tinkoff.qa.neptune.hibernate.select.SetsDescription;

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
public final class DeleteByQueryStepSupplier<R, TO_DELETE>
        extends SequentialGetStepSupplier.GetObjectChainedStepSupplier<HibernateContext, Void, TO_DELETE, DeleteByQueryStepSupplier<R, TO_DELETE>>
        implements SelectQuery<Void> {

    @StepParameter(value = "Entity", makeReadableBy = EntityParameterValueGetter.class)
    Class<R> entity;

    @CaptureOnSuccess(by = DataCaptor.class)
    @CaptureOnFailure(by = DataCaptor.class)
    List<String> deleted;

    private DeleteByQueryStepSupplier(Class<R> entity, DeleteEntities<TO_DELETE> f) {
        super(f);
        this.entity = entity;
    }

    public static <R> DeleteByQueryStepSupplier<R, R> delete(
            String description,
            SelectOneStepSupplier<R> select) {
        checkArgument(isNotBlank(description), "Description should be defined");
        var translated = translate(description);
        var entity = ((HasEntityInfo<R>) select).getEntity();
        ((SetsDescription) select).changeDescription(translated);
        return new DeleteByQueryStepSupplier<R, R>(entity, new DeleteEntities.DeleteOne<>())
                .setDescription(translated)
                .from(select);
    }

    @Description("{description}")
    public static <R> DeleteByQueryStepSupplier<R, R> delete(
            @DescriptionFragment(value = "description",
                    makeReadableBy = ParameterValueGetter.TranslatedDescriptionParameterValueGetter.class)
                    String description,
            R toDelete) {
        checkArgument(isNotBlank(description), "Description should be defined");
        checkNotNull(toDelete);
        return new DeleteByQueryStepSupplier<R, R>((Class<R>) toDelete.getClass(), new DeleteEntities.DeleteOne<>())
                .from(toDelete);
    }

    public static <R> DeleteByQueryStepSupplier<R, Iterable<R>> delete(
            String description,
            SelectManyStepSupplier<R> select) {
        checkArgument(isNotBlank(description), "Description should be defined");
        var translated = translate(description);
        var entity = ((HasEntityInfo<R>) select).getEntity();
        ((SetsDescription) select).changeDescription(translated);
        return new DeleteByQueryStepSupplier<R, Iterable<R>>(entity, new DeleteEntities.DeleteMany<>())
                .setDescription(translated)
                .from(select);
    }

    @Description("{description}")
    public static <R> DeleteByQueryStepSupplier<R, Iterable<R>> delete(
            @DescriptionFragment(value = "description",
                    makeReadableBy = ParameterValueGetter.TranslatedDescriptionParameterValueGetter.class)
                    String description,
            Iterable<R> toDelete) {
        checkArgument(isNotBlank(description), "Description should be defined");
        checkNotNull(toDelete);
        checkArgument(Iterables.size(toDelete) > 0, "At leas one item to delete should be defined");
        return new DeleteByQueryStepSupplier<R, Iterable<R>>((Class<R>) toDelete.iterator().next().getClass(),
                new DeleteEntities.DeleteMany<>())
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
