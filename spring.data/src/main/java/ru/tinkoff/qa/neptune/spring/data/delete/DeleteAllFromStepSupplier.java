package ru.tinkoff.qa.neptune.spring.data.delete;

import org.springframework.data.repository.Repository;
import ru.tinkoff.qa.neptune.core.api.steps.SequentialGetStepSupplier;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.StepParameter;
import ru.tinkoff.qa.neptune.database.abstractions.SelectQuery;
import ru.tinkoff.qa.neptune.spring.data.SpringDataContext;
import ru.tinkoff.qa.neptune.spring.data.dictionary.RepositoryParameterValueGetter;

@SequentialGetStepSupplier.DefineGetImperativeParameterName("Delete:")
@Description("All records")
public final class DeleteAllFromStepSupplier<R, ID, T extends Repository<R, ID>>
        extends SequentialGetStepSupplier.GetObjectChainedStepSupplier<SpringDataContext, Void, T, DeleteAllFromStepSupplier<R, ID, T>>
        implements SelectQuery<Void> {

    @StepParameter(value = "Repository", makeReadableBy = RepositoryParameterValueGetter.class)
    T repository;

    private DeleteAllFromStepSupplier() {
        super(new DeleteAll<>());
    }

    public static <R, ID, T extends Repository<R, ID>> DeleteAllFromStepSupplier<R, ID, T> deleteAllRecords(T repo) {
        return new DeleteAllFromStepSupplier<R, ID, T>().from(repo);
    }

    @Override
    protected DeleteAllFromStepSupplier<R, ID, T> from(T from) {
        repository = from;
        return super.from(from);
    }
}
