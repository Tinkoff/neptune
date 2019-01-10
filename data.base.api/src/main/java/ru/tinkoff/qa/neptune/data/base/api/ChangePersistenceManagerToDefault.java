package ru.tinkoff.qa.neptune.data.base.api;

import java.util.function.Function;

import static ru.tinkoff.qa.neptune.core.api.StoryWriter.toGet;

final class ChangePersistenceManagerToDefault implements Function<DataBaseStepPerformer, DataBaseStepPerformer> {

    private ChangePersistenceManagerToDefault() {
        super();
    }

    static Function<DataBaseStepPerformer, DataBaseStepPerformer> changeConnectionToDefault() {
        return toGet("Use default connection", new ChangePersistenceManagerToDefault());
    }

    @Override
    public DataBaseStepPerformer apply(DataBaseStepPerformer dataBaseSteps) {
        return dataBaseSteps.switchToDefault();
    }
}
