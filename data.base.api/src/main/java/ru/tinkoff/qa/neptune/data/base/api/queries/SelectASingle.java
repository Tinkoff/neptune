package ru.tinkoff.qa.neptune.data.base.api.queries;

import org.datanucleus.api.jdo.JDOPersistenceManager;
import ru.tinkoff.qa.neptune.core.api.steps.SequentialGetStepSupplier;
import ru.tinkoff.qa.neptune.data.base.api.DataBaseStepContext;

import java.util.function.Function;

public abstract class SelectASingle<T, THIS extends SelectASingle<T, THIS>>
        extends SequentialGetStepSupplier.GetObjectChainedStepSupplier<DataBaseStepContext, T, JDOPersistenceManager, THIS> {

    protected SelectASingle(String description,
                            Function<DataBaseStepContext, JDOPersistenceManager> from,
                            Function<JDOPersistenceManager, T> originalFunction) {
        super(description, originalFunction);
        from(from);
    }
}
