package ru.tinkoff.qa.neptune.spring.boot.starter.env;

import ru.tinkoff.qa.neptune.core.api.properties.PropertySource;

import static java.util.Optional.ofNullable;
import static ru.tinkoff.qa.neptune.spring.boot.starter.application.contexts.CurrentApplicationContextTestExecutionListener.getCurrentApplicationContext;

public class SpringBootPropertySource implements PropertySource {

    @Override
    public String getProperty(String property) {
        var context = getCurrentApplicationContext();
        return ofNullable(context)
                .map(c -> c.getEnvironment().getProperty(property))
                .orElse(null);
    }

    @Override
    public boolean isPropertyDefined(String property) {
        var context = getCurrentApplicationContext();
        return ofNullable(context)
                .map(c -> c.getEnvironment().containsProperty(property))
                .orElse(false);
    }
}
