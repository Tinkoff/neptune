package ru.tinkoff.qa.neptune.data.base.api;

import java.util.function.Function;

import static ru.tinkoff.qa.neptune.core.api.StoryWriter.toGet;

final class ChangePersistenceManagerToDefault implements Function<DataBaseStepContext, DataBaseStepContext> {

    private ChangePersistenceManagerToDefault() {
        super();
    }

    static Function<DataBaseStepContext, DataBaseStepContext> changeConnectionToDefault() {
        return toGet("Use default connection", new ChangePersistenceManagerToDefault());
    }

    @Override
    public DataBaseStepContext apply(DataBaseStepContext dataBaseSteps) {
        return dataBaseSteps.switchToDefault();
    }
}
