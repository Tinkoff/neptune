package ru.tinkoff.qa.neptune.data.base.api;

import org.datanucleus.api.jdo.JDOPersistenceManagerFactory;

import java.util.function.Function;

import static java.lang.String.format;
import static ru.tinkoff.qa.neptune.core.api.StoryWriter.toGet;

final class ChangePersistenceManagerByPersistenceManagerFactory implements Function<DataBaseSteps, DataBaseSteps> {

    private final JDOPersistenceManagerFactory persistenceManagerFactory;

    private ChangePersistenceManagerByPersistenceManagerFactory(JDOPersistenceManagerFactory persistenceManagerFactory) {
        this.persistenceManagerFactory = persistenceManagerFactory;
    }

    static Function<DataBaseSteps, DataBaseSteps> changeConnectionByPersistenceManagerFactory(
            JDOPersistenceManagerFactory persistenceManagerFactory) {
        return toGet(format("Change connection by persistence manager factory %s", persistenceManagerFactory.getName()),
                new ChangePersistenceManagerByPersistenceManagerFactory(persistenceManagerFactory));
    }

    @Override
    public DataBaseSteps apply(DataBaseSteps dataBaseSteps) {
        return dataBaseSteps.switchTo(persistenceManagerFactory);
    }
}
