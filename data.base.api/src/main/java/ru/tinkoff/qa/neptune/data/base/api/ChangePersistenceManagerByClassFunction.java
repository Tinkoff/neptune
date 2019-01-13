package ru.tinkoff.qa.neptune.data.base.api;

import ru.tinkoff.qa.neptune.data.base.api.persistence.data.PersistenceManagerFactorySupplier;

import java.util.function.Function;

import static java.lang.String.format;
import static ru.tinkoff.qa.neptune.core.api.StoryWriter.toGet;

final class ChangePersistenceManagerByClassFunction implements Function<DataBaseStepContext, DataBaseStepContext> {

    private final Class<? extends PersistenceManagerFactorySupplier> supplierClass;

    private ChangePersistenceManagerByClassFunction(Class<? extends PersistenceManagerFactorySupplier> supplierClass) {
        this.supplierClass = supplierClass;
    }

    static Function<DataBaseStepContext, DataBaseStepContext> changeConnectionByClass(Class<? extends PersistenceManagerFactorySupplier> supplierClass) {
        return toGet(format("Change connection by class of persistence manager factory supplier %s",
                supplierClass.getSimpleName()),
                new ChangePersistenceManagerByClassFunction(supplierClass));
    }

    @Override
    public DataBaseStepContext apply(DataBaseStepContext dataBaseSteps) {
        return dataBaseSteps.switchTo(supplierClass);
    }
}
