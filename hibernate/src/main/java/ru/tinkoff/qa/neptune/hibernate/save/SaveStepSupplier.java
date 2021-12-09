package ru.tinkoff.qa.neptune.hibernate.save;

import com.google.common.collect.Iterables;
import ru.tinkoff.qa.neptune.core.api.event.firing.annotations.CaptureOnSuccess;
import ru.tinkoff.qa.neptune.core.api.steps.SequentialGetStepSupplier;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.DescriptionFragment;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.IncludeParamsOfInnerGetterStep;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.StepParameter;
import ru.tinkoff.qa.neptune.core.api.steps.parameters.ParameterValueGetter;
import ru.tinkoff.qa.neptune.database.abstractions.InsertQuery;
import ru.tinkoff.qa.neptune.database.abstractions.SelectQuery;
import ru.tinkoff.qa.neptune.database.abstractions.UpdateAction;
import ru.tinkoff.qa.neptune.database.abstractions.captors.DataCaptor;
import ru.tinkoff.qa.neptune.database.abstractions.dictionary.Update;
import ru.tinkoff.qa.neptune.hibernate.HibernateContext;
import ru.tinkoff.qa.neptune.hibernate.dictionary.EntityParameterValueGetter;
import ru.tinkoff.qa.neptune.hibernate.select.HasEntityInfo;
import ru.tinkoff.qa.neptune.hibernate.select.SelectManyStepSupplier;
import ru.tinkoff.qa.neptune.hibernate.select.SelectOneStepSupplier;
import ru.tinkoff.qa.neptune.hibernate.select.SetsDescription;

import java.util.List;
import java.util.Map;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.collect.Lists.newArrayList;
import static java.util.Arrays.asList;
import static java.util.List.of;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static ru.tinkoff.qa.neptune.core.api.localization.StepLocalization.translate;

@SuppressWarnings("unchecked")
@SequentialGetStepSupplier.DefineGetImperativeParameterName("Save:")
@CaptureOnSuccess(by = DataCaptor.class)
public abstract class SaveStepSupplier<INPUT, RESULT, R>
        extends SequentialGetStepSupplier.GetObjectChainedStepSupplier<HibernateContext, RESULT, INPUT, SaveStepSupplier<INPUT, RESULT, R>>
        implements InsertQuery<RESULT>,
        SelectQuery<RESULT> {

    @StepParameter(value = "Entity", makeReadableBy = EntityParameterValueGetter.class)
    Class<R> entity;

    List<UpdateAction<R>> updates = of();

    protected SaveStepSupplier(Class<R> entity, SaveFunction<INPUT, RESULT> originalFunction) {
        super(originalFunction);
        this.entity = entity;
    }

    public static <R> SaveStepSupplier<R, R, R> save(
            String description,
            SelectOneStepSupplier<R> select) {
        checkArgument(isNotBlank(description), "Description should be defined");
        var translated = translate(description);
        var entity = ((HasEntityInfo<R>) select).getEntity();
        ((SetsDescription) select).changeDescription(translated);
        return new SaveOneStepSupplier<>(entity)
                .setDescription(translated)
                .from(select);
    }

    @Description("{description}")
    public static <R> SaveStepSupplier<R, R, R> save(
            @DescriptionFragment(value = "description",
                    makeReadableBy = ParameterValueGetter.TranslatedDescriptionParameterValueGetter.class)
                    String description,
            R toSave) {
        checkArgument(isNotBlank(description), "Description should be defined");
        checkNotNull(toSave);
        return new SaveOneStepSupplier<>((Class<R>) toSave.getClass()).from(toSave);
    }

    public static <R> SaveStepSupplier<Iterable<R>, Iterable<R>, R> save(
            String description,
            SelectManyStepSupplier<R> select) {
        checkArgument(isNotBlank(description), "Description should be defined");
        var translated = translate(description);
        var entity = ((HasEntityInfo<R>) select).getEntity();
        ((SetsDescription) select).changeDescription(translated);
        return new SaveManyStepSupplier<>(entity)
                .setDescription(translated)
                .from(select);
    }

    @Description("{description}")
    public static <R> SaveStepSupplier<Iterable<R>, Iterable<R>, R> save(
            @DescriptionFragment(value = "description",
                    makeReadableBy = ParameterValueGetter.TranslatedDescriptionParameterValueGetter.class)
                    String description,
            Iterable<R> toSave) {
        checkArgument(isNotBlank(description), "Description should be defined");
        checkNotNull(toSave);
        checkArgument(Iterables.size(toSave) > 0, "At leas one item to save should be defined");
        return new SaveManyStepSupplier<>((Class<R>) toSave.iterator().next().getClass()).from(toSave);
    }

    @Override
    public Map<String, String> getParameters() {
        var i = 1;
        var result = super.getParameters();
        for (var u : updates) {
            result.put(new Update() + " " + i, u.toString());
            i++;
        }
        return result;
    }

    @SafeVarargs
    public final SaveStepSupplier<INPUT, RESULT, R> setUpdates(UpdateAction<R>... updates) {
        checkNotNull(updates);
        checkArgument(updates.length > 0, "At least one update should be defined");
        this.updates = asList(updates);
        return this;
    }

    @IncludeParamsOfInnerGetterStep
    private static class SaveOneStepSupplier<R> extends SaveStepSupplier<R, R, R> {

        protected SaveOneStepSupplier(Class<R> entity) {
            super(entity, new SaveFunction.SaveOne<>());
        }

        @Override
        protected void onStart(R r) {
            updates.forEach(u -> u.performUpdate(of(r)));
            super.onStart(r);
        }
    }

    @IncludeParamsOfInnerGetterStep
    private static class SaveManyStepSupplier<R> extends SaveStepSupplier<Iterable<R>, Iterable<R>, R> {

        protected SaveManyStepSupplier(Class<R> entity) {
            super(entity, new SaveFunction.SaveMany<>());
        }

        @Override
        protected void onStart(Iterable<R> rs) {
            updates.forEach(u -> u.performUpdate(newArrayList(rs)));
            super.onStart(rs);
        }
    }
}
