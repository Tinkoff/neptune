package ru.tinkoff.qa.neptune.spring.boot.starter.env;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import static ru.tinkoff.qa.neptune.spring.boot.starter.application.contexts.CurrentApplicationContextTestExecutionListener.getCurrentApplicationContext;

/**
 * Intercepts instances of {@link Environment} of launched {@link ApplicationContext}'s
 */
@Configuration
public class SpringEnvironmentWrappingConfiguration {

    private final static List<SpringEnvironmentWrappingConfiguration> ENVIRONMENT_WRAPPERS = new CopyOnWriteArrayList<>();

    @Autowired
    Environment environment;

    @Autowired
    ApplicationContext context;

    public SpringEnvironmentWrappingConfiguration() {
        super();
        ENVIRONMENT_WRAPPERS.add(this);
    }

    /**
     * Returns environment of current {@link ApplicationContext}
     *
     * @return environment of current {@link ApplicationContext}
     */
    static Environment getCurrentApplicationEnvironment() {
        var context = getCurrentApplicationContext();
        return ENVIRONMENT_WRAPPERS
                .stream()
                .filter(env -> env.getContext() == context)
                .findFirst()
                .map(SpringEnvironmentWrappingConfiguration::getEnv)
                .orElse(null);
    }

    private Environment getEnv() {
        return environment;
    }

    private ApplicationContext getContext() {
        return context;
    }
}
