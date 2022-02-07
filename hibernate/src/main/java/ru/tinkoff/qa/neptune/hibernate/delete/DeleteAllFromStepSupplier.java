package ru.tinkoff.qa.neptune.hibernate.delete;

import ru.tinkoff.qa.neptune.core.api.steps.SequentialGetStepSupplier;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;
import ru.tinkoff.qa.neptune.database.abstractions.SelectQuery;
import ru.tinkoff.qa.neptune.hibernate.HibernateContext;

@SequentialGetStepSupplier.DefineGetImperativeParameterName("Delete:")
@Description("All records")
public final class DeleteAllFromStepSupplier<R>
        extends SequentialGetStepSupplier.GetObjectStepSupplier<HibernateContext, Void, DeleteAllFromStepSupplier<R>>
        implements SelectQuery<Void> {

    private DeleteAllFromStepSupplier(Class<R> entity) {
        super(new DeleteAll<>(entity));
    }

    public static <R> DeleteAllFromStepSupplier<R> deleteAllRecords(Class<R> entityCls) {
        return new DeleteAllFromStepSupplier<>(entityCls);
    }
}
