package ru.tinkoff.qa.neptune.data.base.api;

import java.util.function.Function;

import static ru.tinkoff.qa.neptune.core.api.StoryWriter.toGet;

final class ChangePersistenceManagerToDefault implements Function<DataBaseSteps, DataBaseSteps> {

    private ChangePersistenceManagerToDefault() {
        super();
    }

    public static Function<DataBaseSteps, DataBaseSteps> changeConnectionToDefault() {
        return toGet("Use default connection", new ChangePersistenceManagerToDefault());
    }

    @Override
    public DataBaseSteps apply(DataBaseSteps dataBaseSteps) {
        return dataBaseSteps.switchToDefault();
    }
}
