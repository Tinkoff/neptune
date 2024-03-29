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
import static ru.tinkoff.qa.neptune.hibernate.HibernateContext.NO_DESC_ERROR_TEXT;

@SequentialGetStepSupplier.DefineGetImperativeParameterName("Save:")
@CaptureOnSuccess(by = DataCaptor.class)
public abstract class SaveStepSupplier<INPUT, RESULT, R>
        extends SequentialGetStepSupplier.GetObjectChainedStepSupplier<HibernateContext, RESULT, INPUT, SaveStepSupplier<INPUT, RESULT, R>>
        implements InsertQuery<RESULT>,
        SelectQuery<RESULT> {

    protected SaveFunction<R, RESULT> f;
    List<UpdateAction<R>> updates = of();

    protected SaveStepSupplier(SaveFunction<INPUT, RESULT> originalFunction) {
        super(originalFunction);
    }

    public static <R> SaveStepSupplier<R, R, R> save(
            String description,
            SelectOneStepSupplier<R> select) {
        checkArgument(isNotBlank(description), NO_DESC_ERROR_TEXT);
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
        checkArgument(isNotBlank(description), NO_DESC_ERROR_TEXT);
        checkNotNull(toSave);
        return new SaveOneStepSupplier<>(toSave);
    }

    public static <R> SaveStepSupplier<Iterable<R>, Iterable<R>, R> save(
            String description,
            SelectManyStepSupplier<R> select) {
        checkArgument(isNotBlank(description), NO_DESC_ERROR_TEXT);
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
        checkArgument(isNotBlank(description), NO_DESC_ERROR_TEXT);
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

        protected SaveOneStepSupplier(R toSave) {
            super(new SaveFunction.SaveOne<>());
            from(toSave);
        }

        protected SaveOneStepSupplier(SelectOneStepSupplier<R> select) {
            super(new SaveFunction.SaveOne<>());
            from(select);
        }

        @Override
        protected void onStart(R toSave) {
            updates.forEach(u -> u.performUpdate(of(toSave)));
        }
    }

    @IncludeParamsOfInnerGetterStep
    private static class SaveManyStepSupplier<R> extends SaveStepSupplier<Iterable<R>, Iterable<R>, R> {

        protected SaveManyStepSupplier(Iterable<R> toSave) {
            super(new SaveFunction.SaveMany<>());
            from(toSave);
        }

        protected SaveManyStepSupplier(SelectManyStepSupplier<R> select) {
            super(new SaveFunction.SaveMany<>());
            from(select);
        }

        @Override
        protected void onStart(Iterable<R> toSave) {
            updates.forEach(u -> u.performUpdate(newArrayList(toSave)));
        }
    }
}
