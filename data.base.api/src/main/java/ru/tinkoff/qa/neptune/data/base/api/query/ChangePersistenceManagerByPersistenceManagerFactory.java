package ru.tinkoff.qa.neptune.data.base.api.query;

import org.datanucleus.api.jdo.JDOPersistenceManagerFactory;
import ru.tinkoff.qa.neptune.data.base.api.DataBaseSteps;

import java.util.function.Function;

import static java.lang.String.format;
import static ru.tinkoff.qa.neptune.core.api.StoryWriter.toGet;

class ChangePersistenceManagerByPersistenceManagerFactory implements Function<DataBaseSteps, DataBaseSteps> {

    private final JDOPersistenceManagerFactory persistenceManagerFactory;

    ChangePersistenceManagerByPersistenceManagerFactory(JDOPersistenceManagerFactory persistenceManagerFactory) {
        this.persistenceManagerFactory = persistenceManagerFactory;
    }

    static Function<DataBaseSteps, DataBaseSteps> changeConnectionByersistenceManagerFactory(
            JDOPersistenceManagerFactory persistenceManagerFactory) {
        return toGet(format("Change connection by persistence manager factory %s", persistenceManagerFactory),
                new ChangePersistenceManagerByPersistenceManagerFactory(persistenceManagerFactory));
    }

    @Override
    public DataBaseSteps apply(DataBaseSteps dataBaseSteps) {
        return dataBaseSteps.switchTo(persistenceManagerFactory);
    }
}
