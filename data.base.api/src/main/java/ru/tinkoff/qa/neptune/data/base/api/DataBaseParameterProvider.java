package ru.tinkoff.qa.neptune.data.base.api;

import ru.tinkoff.qa.neptune.core.api.ConstructorParameters;
import ru.tinkoff.qa.neptune.core.api.steps.context.ParameterProvider;

import static ru.tinkoff.qa.neptune.core.api.ConstructorParameters.params;
import static ru.tinkoff.qa.neptune.data.base.api.properties.DefaultPersistenceManagerFactoryProperty
        .DEFAULT_JDO_PERSISTENCE_MANAGER_FACTORY_PROPERTY;

public class DataBaseParameterProvider implements ParameterProvider {
    @Override
    public ConstructorParameters provide() {
        return params(DEFAULT_JDO_PERSISTENCE_MANAGER_FACTORY_PROPERTY.get().get());
    }
}
