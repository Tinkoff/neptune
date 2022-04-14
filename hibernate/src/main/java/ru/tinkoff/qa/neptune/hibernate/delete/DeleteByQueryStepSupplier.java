package ru.tinkoff.qa.neptune.hibernate.delete;

import com.google.common.collect.Iterables;
import ru.tinkoff.qa.neptune.core.api.event.firing.annotations.CaptureOnFailure;
import ru.tinkoff.qa.neptune.core.api.event.firing.annotations.CaptureOnSuccess;
import ru.tinkoff.qa.neptune.core.api.steps.SequentialGetStepSupplier;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.DescriptionFragment;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.IncludeParamsOfInnerGetterStep;
import ru.tinkoff.qa.neptune.core.api.steps.parameters.ParameterValueGetter;
import ru.tinkoff.qa.neptune.database.abstractions.SelectQuery;
import ru.tinkoff.qa.neptune.database.abstractions.captors.DataCaptor;
import ru.tinkoff.qa.neptune.hibernate.HibernateContext;
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

@IncludeParamsOfInnerGetterStep
@SequentialGetStepSupplier.DefineGetImperativeParameterName("Delete:")
public abstract class DeleteByQueryStepSupplier<R>
        extends SequentialGetStepSupplier.GetObjectStepSupplier<HibernateContext, Void, DeleteByQueryStepSupplier<R>>
        implements SelectQuery<Void> {

    protected DeleteEntities<R> f;
    protected R toDelete;

    @CaptureOnSuccess(by = DataCaptor.class)
    @CaptureOnFailure(by = DataCaptor.class)
    List<String> deleted;

    private DeleteByQueryStepSupplier(DeleteEntities<R> f) {
        super(f);
        this.f = f;
    }

    public static <R> DeleteByQueryStepSupplier<R> delete(
            String description,
            SelectOneStepSupplier<R> select) {
        checkArgument(isNotBlank(description), "Description should be defined");
        var translated = translate(description);
        ((SetsDescription) select).changeDescription(translated);
        return new DeleteOneStepSupplier<>(select)
                .setDescription(translated);
    }

    @Description("{description}")
    public static <R> DeleteByQueryStepSupplier<R> delete(
            @DescriptionFragment(value = "description",
                    makeReadableBy = ParameterValueGetter.TranslatedDescriptionParameterValueGetter.class)
                    String description,
            R toDelete) {
        checkArgument(isNotBlank(description), "Description should be defined");
        checkNotNull(toDelete);
        return new DeleteOneStepSupplier<>(toDelete);
    }

    public static <R> DeleteByQueryStepSupplier<Iterable<R>> delete(
            String description,
            SelectManyStepSupplier<R> select) {
        checkArgument(isNotBlank(description), "Description should be defined");
        var translated = translate(description);
        ((SetsDescription) select).changeDescription(translated);
        return new DeleteManyStepSupplier<>(select)
                .setDescription(translated);
    }

    @Description("{description}")
    public static <R> DeleteByQueryStepSupplier<Iterable<R>> delete(
            @DescriptionFragment(value = "description",
                    makeReadableBy = ParameterValueGetter.TranslatedDescriptionParameterValueGetter.class)
                    String description,
            Iterable<R> toDelete) {
        checkArgument(isNotBlank(description), "Description should be defined");
        checkNotNull(toDelete);
        checkArgument(Iterables.size(toDelete) > 0, "At leas one item to delete should be defined");
        return new DeleteManyStepSupplier<>(toDelete);
    }

    @IncludeParamsOfInnerGetterStep
    private static class DeleteOneStepSupplier<R> extends DeleteByQueryStepSupplier<R> {

        protected SelectOneStepSupplier<R> select;

        protected DeleteOneStepSupplier(R toDelete) {
            super(new DeleteEntities.DeleteOne<>());
            this.toDelete = toDelete;
        }

        protected DeleteOneStepSupplier(SelectOneStepSupplier<R> select) {
            super(new DeleteEntities.DeleteOne<>());
            this.select = select;
        }

        @Override
        protected void onStart(HibernateContext context) {
            if (select != null) {
                toDelete = select.get().apply(context);
            }
            f.setToDelete(toDelete);

            super.onStart(context);
        }
    }

    @IncludeParamsOfInnerGetterStep
    private static class DeleteManyStepSupplier<R> extends DeleteByQueryStepSupplier<Iterable<R>> {

        protected SelectManyStepSupplier<R> select;

        protected DeleteManyStepSupplier(Iterable<R> toDelete) {
            super(new DeleteEntities.DeleteMany<>());
            this.toDelete = toDelete;
        }

        protected DeleteManyStepSupplier(SelectManyStepSupplier<R> select) {
            super(new DeleteEntities.DeleteMany<>());
            this.select = select;
        }

        @Override
        protected void onStart(HibernateContext context) {
            if (select != null) {
                toDelete = select.get().apply(context);
            }
            f.setToDelete(toDelete);

            super.onStart(context);
        }
    }

    @Override
    protected void onStart(HibernateContext context) {
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
