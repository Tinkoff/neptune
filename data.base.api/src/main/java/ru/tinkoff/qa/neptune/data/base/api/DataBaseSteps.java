package ru.tinkoff.qa.neptune.data.base.api;

import org.datanucleus.api.jdo.JDOPersistenceManager;
import org.datanucleus.api.jdo.JDOPersistenceManagerFactory;
import ru.tinkoff.qa.neptune.core.api.CreateWith;
import ru.tinkoff.qa.neptune.core.api.GetStep;
import ru.tinkoff.qa.neptune.core.api.PerformActionStep;
import ru.tinkoff.qa.neptune.core.api.cleaning.Stoppable;
import ru.tinkoff.qa.neptune.data.base.api.query.SelectSequentialGetStepSupplier;

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

    public DataBaseSteps switchTo(String persistenceUnitName) {
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

    public <T, Q> T select(SelectSequentialGetStepSupplier<? extends T, ? extends Q, ?> selectSupplier) {
        return get(selectSupplier.get());
    }

    @Override
    public void shutDown() {
        jdoPersistenceManagerMap.forEach((key, value) -> {
            value.close();
            key.close();
        });
    }
}
