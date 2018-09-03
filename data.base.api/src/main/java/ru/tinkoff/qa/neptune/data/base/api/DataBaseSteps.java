package ru.tinkoff.qa.neptune.data.base.api;

import org.datanucleus.api.jdo.JDOPersistenceManager;
import org.datanucleus.api.jdo.JDOPersistenceManagerFactory;
import ru.tinkoff.qa.neptune.core.api.CreateWith;
import ru.tinkoff.qa.neptune.core.api.GetStep;
import ru.tinkoff.qa.neptune.core.api.PerformActionStep;
import ru.tinkoff.qa.neptune.core.api.cleaning.Stoppable;
import ru.tinkoff.qa.neptune.data.base.api.persistence.data.PersistenceManagerFactorySupplier;

import java.util.HashMap;
import java.util.Map;

import static com.google.common.base.Preconditions.checkArgument;
import static ru.tinkoff.qa.neptune.data.base.api.persistence.data.PersistenceManagerFactoryStore.getPersistenceManagerFactory;

@CreateWith(provider = DataBaseParameterProvider.class)
public class DataBaseSteps implements GetStep<DataBaseSteps>, PerformActionStep<DataBaseSteps>, Stoppable {

    private final JDOPersistenceManagerFactory defaultFactory;
    private final Map<JDOPersistenceManagerFactory, JDOPersistenceManager> jdoPersistenceManagerMap = new HashMap<>();
    private JDOPersistenceManagerFactory currentFactory;

    public DataBaseSteps(JDOPersistenceManagerFactory defaultFactory) {
        this.defaultFactory = defaultFactory;
        switchToDefault();
    }

    /**
     * This method performs the switching to desired database by created persistence manager factory.
     *
     * @param jdoPersistenceManagerFactory is persistence manager factory which is opened and ready to use.
     * @return self-reference
     */
    public DataBaseSteps switchTo(JDOPersistenceManagerFactory jdoPersistenceManagerFactory) {
        checkArgument(!jdoPersistenceManagerFactory.isClosed(), "Persistence manager " +
                "factory should be not closed");
        JDOPersistenceManager manager = jdoPersistenceManagerMap.get(jdoPersistenceManagerFactory);
        if (manager == null || manager.isClosed()) {
            jdoPersistenceManagerMap.put(jdoPersistenceManagerFactory,
                    (JDOPersistenceManager) jdoPersistenceManagerFactory.getPersistenceManager());
        }
        currentFactory = jdoPersistenceManagerFactory;
        return this;
    }

    /**
     * This method performs the switching to desired database by name of persistence. Such persistence unit should
     * be properly described by {@link PersistenceManagerFactorySupplier}
     *
     * @param persistenceUnitName is a name of persistence.
     * @return self-reference
     */
    public DataBaseSteps switchTo(CharSequence persistenceUnitName) {
        return switchTo(getPersistenceManagerFactory(persistenceUnitName, true));
    }

    public DataBaseSteps switchToDefault() {
        return switchTo(defaultFactory);
    }

    public JDOPersistenceManagerFactory getCurrentFactory() {
        return currentFactory;
    }

    public JDOPersistenceManager getCurrentPersistenceManager() {
        switchTo(getCurrentFactory());
        return jdoPersistenceManagerMap.get(currentFactory);
    }

    @Override
    public void shutDown() {
        jdoPersistenceManagerMap.forEach((key, value) -> {
            value.close();
            key.close();
        });
    }
}
