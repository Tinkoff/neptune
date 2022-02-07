package ru.tinkoff.qa.neptune.hibernate.save;

import com.google.common.collect.Iterables;
import ru.tinkoff.qa.neptune.core.api.event.firing.annotations.CaptureOnSuccess;
import ru.tinkoff.qa.neptune.core.api.steps.SequentialGetStepSupplier;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.DescriptionFragment;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.IncludeParamsOfInnerGetterStep;
import ru.tinkoff.qa.neptune.core.api.steps.parameters.ParameterValueGetter;
import ru.tinkoff.qa.neptune.database.abstractions.InsertQuery;
import ru.tinkoff.qa.neptune.database.abstractions.SelectQuery;
import ru.tinkoff.qa.neptune.database.abstractions.UpdateAction;
import ru.tinkoff.qa.neptune.database.abstractions.captors.DataCaptor;
import ru.tinkoff.qa.neptune.database.abstractions.dictionary.Update;
import ru.tinkoff.qa.neptune.hibernate.HibernateContext;
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

@SequentialGetStepSupplier.DefineGetImperativeParameterName("Save:")
@CaptureOnSuccess(by = DataCaptor.class)
public abstract class SaveStepSupplier<INPUT, RESULT, R>
        extends SequentialGetStepSupplier.GetObjectStepSupplier<HibernateContext, RESULT, SaveStepSupplier<INPUT, RESULT, R>>
        implements InsertQuery<RESULT>,
        SelectQuery<RESULT> {

    protected SaveFunction<R, RESULT> f;
    protected INPUT toSave;

    List<UpdateAction<R>> updates = of();

    protected SaveStepSupplier(SaveFunction<R, RESULT> originalFunction) {
        super(originalFunction);
        this.f = originalFunction;
    }

    public static <R> SaveStepSupplier<R, R, R> save(
            String description,
            SelectOneStepSupplier<R> select) {
        checkArgument(isNotBlank(description), "Description should be defined");
        var translated = translate(description);
        ((SetsDescription) select).changeDescription(translated);
        return new SaveOneStepSupplier<>(select)
                .setDescription(translated);
    }

    @Description("{description}")
    public static <R> SaveStepSupplier<R, R, R> save(
            @DescriptionFragment(value = "description",
                    makeReadableBy = ParameterValueGetter.TranslatedDescriptionParameterValueGetter.class)
                    String description,
            R toSave) {
        checkArgument(isNotBlank(description), "Description should be defined");
        checkNotNull(toSave);
        return new SaveOneStepSupplier<>(toSave);
    }

    public static <R> SaveStepSupplier<Iterable<R>, Iterable<R>, R> save(
            String description,
            SelectManyStepSupplier<R> select) {
        checkArgument(isNotBlank(description), "Description should be defined");
        var translated = translate(description);
        ((SetsDescription) select).changeDescription(translated);
        return new SaveManyStepSupplier<>(select)
                .setDescription(translated);
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
        return new SaveManyStepSupplier<>(toSave);
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

        protected SelectOneStepSupplier<R> select;

        protected SaveOneStepSupplier(R toSave) {
            super(new SaveFunction.SaveOne<>());
            this.toSave = toSave;
        }

        protected SaveOneStepSupplier(SelectOneStepSupplier<R> select) {
            super(new SaveFunction.SaveOne<>());
            this.select = select;
        }

        @Override
        protected void onStart(HibernateContext context) {
            if (select != null) {
                toSave = select.get().apply(context);
            }
            f.setToSave(List.of(toSave));

            updates.forEach(u -> u.performUpdate(of(toSave)));
            super.onStart(context);
        }
    }

    @IncludeParamsOfInnerGetterStep
    private static class SaveManyStepSupplier<R> extends SaveStepSupplier<Iterable<R>, Iterable<R>, R> {

        protected SelectManyStepSupplier<R> select;

        protected SaveManyStepSupplier(Iterable<R> toSave) {
            super(new SaveFunction.SaveMany<>());
            this.toSave = toSave;
        }

        protected SaveManyStepSupplier(SelectManyStepSupplier<R> select) {
            super(new SaveFunction.SaveMany<>());
            this.select = select;
        }

        @Override
        protected void onStart(HibernateContext context) {
            if (select != null) {
                toSave = select.get().apply(context);
            }
            f.setToSave(toSave);

            updates.forEach(u -> u.performUpdate(newArrayList(toSave)));
        }
    }
}
