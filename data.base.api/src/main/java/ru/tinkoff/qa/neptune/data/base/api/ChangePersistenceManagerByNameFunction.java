package ru.tinkoff.qa.neptune.data.base.api;

import java.util.function.Function;

import static java.lang.String.format;
import static ru.tinkoff.qa.neptune.core.api.StoryWriter.toGet;

final class ChangePersistenceManagerByNameFunction implements Function<DataBaseSteps, DataBaseSteps> {

    private final String name;

    private ChangePersistenceManagerByNameFunction(String name) {
        this.name = name;
    }

    public static Function<DataBaseSteps, DataBaseSteps> changeConnectionByName(String name) {
        return toGet(format("Change connection by name %s", name), new ChangePersistenceManagerByNameFunction(name));
    }

    @Override
    public DataBaseSteps apply(DataBaseSteps dataBaseSteps) {
        return dataBaseSteps.switchTo(name);
    }
}
