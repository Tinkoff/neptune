package ru.tinkoff.qa.neptune.data.base.api;

import org.datanucleus.api.jdo.JDOPersistenceManagerFactory;
import ru.tinkoff.qa.neptune.core.api.CreateWith;
import ru.tinkoff.qa.neptune.core.api.GetStep;
import ru.tinkoff.qa.neptune.core.api.PerformActionStep;

import javax.jdo.PersistenceManager;
import java.util.HashMap;
import java.util.Map;

import static ru.tinkoff.qa.neptune.data.base.api.persistence.data.PersistenceManagerFactoryStore.getPersistenceManagerFactory;

@CreateWith(provider = DataBaseParameterProvider.class)
public class DataBaseSteps implements GetStep<DataBaseSteps>, PerformActionStep<DataBaseSteps> {

    private final JDOPersistenceManagerFactory defaultFactory;
    private final Map<JDOPersistenceManagerFactory, PersistenceManager> jdoPersistenceManagerMap = new HashMap<>();
    private JDOPersistenceManagerFactory currentFactory;

    public DataBaseSteps(JDOPersistenceManagerFactory defaultFactory) {
        this.defaultFactory = defaultFactory;
        switchToDefault();
    }

    public DataBaseSteps switchTo(JDOPersistenceManagerFactory jdoPersistenceManagerFactory) {
        PersistenceManager factory = jdoPersistenceManagerMap.get(jdoPersistenceManagerFactory);
        if (factory == null || factory.isClosed()) {
            jdoPersistenceManagerMap.put(jdoPersistenceManagerFactory,
                    jdoPersistenceManagerFactory.getPersistenceManager());
        }
        currentFactory = jdoPersistenceManagerFactory;
        return this;
    }

    public DataBaseSteps switchTo(String persistenceUnitName) {
        return switchTo(getPersistenceManagerFactory(persistenceUnitName, true));
    }

    public DataBaseSteps switchToDefault() {
        return switchTo(defaultFactory);
    }

    protected JDOPersistenceManagerFactory getCurrentFactory() {
        return currentFactory;
    }
}
