package ru.tinkoff.qa.neptune.data.base.api.queries;

import org.datanucleus.api.jdo.JDOPersistenceManager;
import ru.tinkoff.qa.neptune.core.api.steps.SequentialGetStepSupplier;
import ru.tinkoff.qa.neptune.data.base.api.DataBaseStepContext;

import java.util.List;
import java.util.function.Function;

public abstract class SelectList<T, THIS extends SelectList<T, THIS>> extends SequentialGetStepSupplier
        .GetIterableChainedStepSupplier<DataBaseStepContext, List<T>, JDOPersistenceManager, T, THIS> {

    protected SelectList(String description,
                         Function<DataBaseStepContext, JDOPersistenceManager> from,
                         Function<JDOPersistenceManager, List<T>> originalFunction) {
        super(description, originalFunction);
        from(from);
    }
}
