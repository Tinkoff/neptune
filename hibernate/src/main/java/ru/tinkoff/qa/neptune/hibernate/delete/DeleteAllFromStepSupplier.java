package ru.tinkoff.qa.neptune.hibernate.delete;

import ru.tinkoff.qa.neptune.core.api.steps.SequentialGetStepSupplier;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.StepParameter;
import ru.tinkoff.qa.neptune.database.abstractions.SelectQuery;
import ru.tinkoff.qa.neptune.hibernate.HibernateContext;
import ru.tinkoff.qa.neptune.hibernate.dictionary.EntityParameterValueGetter;

@SequentialGetStepSupplier.DefineGetImperativeParameterName("Delete:")
@Description("All records")
public final class DeleteAllFromStepSupplier
        extends SequentialGetStepSupplier.GetObjectChainedStepSupplier<HibernateContext, Void, Class<?>, DeleteAllFromStepSupplier>
        implements SelectQuery<Void> {

    @StepParameter(value = "Entity", makeReadableBy = EntityParameterValueGetter.class)
    Class<?> entity;

    private DeleteAllFromStepSupplier() {
        super(new DeleteAll());
    }

    public static DeleteAllFromStepSupplier deleteAllRecords(Class<?> entityCls) {
        return new DeleteAllFromStepSupplier().from(entityCls);
    }

    @Override
    protected DeleteAllFromStepSupplier from(Class<?> from) {
        entity = from;
        return super.from(from);
    }
}
