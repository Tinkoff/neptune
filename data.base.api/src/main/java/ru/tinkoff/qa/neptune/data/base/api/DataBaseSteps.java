package ru.tinkoff.qa.neptune.data.base.api;

import org.datanucleus.api.jpa.JPAEntityManagerFactory;
import ru.tinkoff.qa.neptune.core.api.CreateWith;
import ru.tinkoff.qa.neptune.core.api.GetStep;
import ru.tinkoff.qa.neptune.core.api.PerformActionStep;

import javax.persistence.EntityManager;
import java.util.HashMap;
import java.util.Map;

import static java.util.Optional.ofNullable;
import static ru.tinkoff.qa.neptune.data.base.api.persistence.data.PersistenceEntityManagerFactoryStore.getEntityManagerFactory;

@CreateWith(provider = DataBaseParameterProvider.class)
public class DataBaseSteps implements GetStep<DataBaseSteps>, PerformActionStep<DataBaseSteps> {

    private final JPAEntityManagerFactory defaultFactory;
    private final Map<JPAEntityManagerFactory, EntityManager> jpaEntityManagerMap = new HashMap<>();
    private JPAEntityManagerFactory currentFactory;

    public DataBaseSteps(JPAEntityManagerFactory defaultFactory) {
        this.defaultFactory = defaultFactory;
        switchToDefault();
    }

    public DataBaseSteps switchTo(JPAEntityManagerFactory jpaEntityManagerFactory) {
        EntityManager factory = jpaEntityManagerMap.get(jpaEntityManagerFactory);
        if (factory == null || factory.isOpen()) {
            jpaEntityManagerMap.put(defaultFactory, defaultFactory.createEntityManager());
        }
        currentFactory = jpaEntityManagerFactory;
        return this;
    }

    public DataBaseSteps switchTo(String persistenceUnitName) {
        return switchTo(getEntityManagerFactory(persistenceUnitName, true));
    }

    public DataBaseSteps switchToDefault() {
        return switchTo(defaultFactory);
    }

    protected JPAEntityManagerFactory getCurrentFactory() {
        return currentFactory;
    }
}
