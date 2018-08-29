package ru.tinkoff.qa.neptune.data.base.api.query;

import ru.tinkoff.qa.neptune.data.base.api.DataBaseSteps;

import java.util.function.Function;

import static ru.tinkoff.qa.neptune.core.api.StoryWriter.toGet;

class ChangePersistenceManagerToDefault implements Function<DataBaseSteps, DataBaseSteps> {

    static Function<DataBaseSteps, DataBaseSteps> changeConnectionToDefault() {
        return toGet("Use default connection", new ChangePersistenceManagerToDefault());
    }

    @Override
    public DataBaseSteps apply(DataBaseSteps dataBaseSteps) {
        return dataBaseSteps.switchToDefault();
    }
}
