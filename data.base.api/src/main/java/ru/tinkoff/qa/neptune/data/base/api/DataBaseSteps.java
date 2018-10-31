package ru.tinkoff.qa.neptune.data.base.api;

import org.datanucleus.api.jdo.JDOPersistenceManager;
import org.datanucleus.api.jdo.JDOPersistenceManagerFactory;
import ru.tinkoff.qa.neptune.core.api.CreateWith;
import ru.tinkoff.qa.neptune.core.api.GetStep;
import ru.tinkoff.qa.neptune.core.api.PerformActionStep;
import ru.tinkoff.qa.neptune.core.api.cleaning.Refreshable;
import ru.tinkoff.qa.neptune.core.api.cleaning.StoppableOnJVMShutdown;
import ru.tinkoff.qa.neptune.data.base.api.persistence.data.PersistenceManagerFactorySupplier;

import java.util.HashMap;
import java.util.Map;

import static com.google.common.base.Preconditions.checkArgument;
import static ru.tinkoff.qa.neptune.data.base.api.persistence.data.PersistenceManagerFactoryStore.getPersistenceManagerFactory;
import static ru.tinkoff.qa.neptune.data.base.api.properties.DefaultPersistenceManagerFactoryProperty.DEFAULT_JDO_PERSISTENCE_MANAGER_FACTORY_PROPERTY;

@CreateWith(provider = DataBaseParameterProvider.class)
public class DataBaseSteps implements GetStep<DataBaseSteps>, PerformActionStep<DataBaseSteps>, StoppableOnJVMShutdown,
        Refreshable {

    private JDOPersistenceManagerFactory defaultFactory;
    private final Map<JDOPersistenceManagerFactory, JDOPersistenceManager> jdoPersistenceManagerMap = new HashMap<>();
    private JDOPersistenceManagerFactory currentFactory;

    public DataBaseSteps(JDOPersistenceManagerFactory defaultFactory) {
        checkArgument(defaultFactory != null, "Value of default JDO persistence manager factory " +
                "should differ from null");
        checkArgument(!defaultFactory.isClosed(), "Default JDO persistence manager factory " +
                "should not be closed");
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
        var manager = jdoPersistenceManagerMap.get(jdoPersistenceManagerFactory);
        if (manager == null || manager.isClosed()) {
            jdoPersistenceManagerMap.put(jdoPersistenceManagerFactory,
                    (JDOPersistenceManager) jdoPersistenceManagerFactory.getPersistenceManager());
        }
        currentFactory = jdoPersistenceManagerFactory;
        return this;
    }

    /**
     * This method performs the switching to desired database by class of persistence manager factory supplier.
     *
     * @param persistenceFactorySupplier is a class of persistence manager factory supplier.
     * @return self-reference
     */
    public DataBaseSteps switchTo(Class<? extends PersistenceManagerFactorySupplier> persistenceFactorySupplier) {
        return switchTo(jdoPersistenceManagerMap.keySet().stream().filter(jdoPersistenceManagerFactory ->
                persistenceFactorySupplier.getName()
                        .equals(jdoPersistenceManagerFactory.getName()) &&
                        !jdoPersistenceManagerFactory.isClosed())
                .findFirst()
                .orElseGet(() -> getPersistenceManagerFactory(persistenceFactorySupplier, true)));
    }

    public DataBaseSteps switchToDefault() {
        if (defaultFactory.isClosed()) {
            defaultFactory = DEFAULT_JDO_PERSISTENCE_MANAGER_FACTORY_PROPERTY.get().get();
        }
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
    public Thread getHookOnJvmStop() {
        return new Thread(() -> jdoPersistenceManagerMap.forEach((key, value) -> {
            jdoPersistenceManagerMap.keySet().forEach(JDOPersistenceManagerFactory::close);
            value.close();
            key.close();
        }));
    }

    @Override
    public void refresh() {
        switchToDefault();
    }
}
