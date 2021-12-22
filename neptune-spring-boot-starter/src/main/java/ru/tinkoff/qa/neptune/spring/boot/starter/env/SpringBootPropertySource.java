package ru.tinkoff.qa.neptune.spring.boot.starter.env;

import ru.tinkoff.qa.neptune.core.api.properties.PropertySource;

import static java.util.Optional.ofNullable;
import static ru.tinkoff.qa.neptune.spring.boot.starter.env.SpringEnvironmentWrappingConfiguration.getCurrentApplicationEnvironment;

public class SpringBootPropertySource implements PropertySource {

    @Override
    public String getProperty(String property) {
        return ofNullable(getCurrentApplicationEnvironment())
                .map(environment -> environment.getProperty(property))
                .orElse(null);
    }

    @Override
    public boolean isPropertyDefined(String property) {
        return ofNullable(getCurrentApplicationEnvironment())
                .map(environment -> environment.containsProperty(property))
                .orElse(false);
    }
}
