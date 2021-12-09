package ru.tinkoff.qa.neptune.hibernate.delete;

import ru.tinkoff.qa.neptune.core.api.steps.SequentialGetStepSupplier;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.StepParameter;
import ru.tinkoff.qa.neptune.database.abstractions.SelectQuery;
import ru.tinkoff.qa.neptune.hibernate.HibernateContext;
import ru.tinkoff.qa.neptune.hibernate.dictionary.EntityParameterValueGetter;

@SequentialGetStepSupplier.DefineGetImperativeParameterName("Delete:")
@Description("All records")
public final class DeleteAllFromStepSupplier<R>
        extends SequentialGetStepSupplier.GetObjectChainedStepSupplier<HibernateContext, Void, Class<R>, DeleteAllFromStepSupplier<R>>
        implements SelectQuery<Void> {

    @StepParameter(value = "Entity", makeReadableBy = EntityParameterValueGetter.class)
    Class<R> entity;

    private DeleteAllFromStepSupplier() {
        super(new DeleteAll<>());
    }

    public static <R> DeleteAllFromStepSupplier<R> deleteAllRecords(Class<R> entityCls) {
        return new DeleteAllFromStepSupplier<R>().from(entityCls);
    }

    @Override
    protected DeleteAllFromStepSupplier<R> from(Class<R> from) {
        entity = from;
        return super.from(from);
    }
}
