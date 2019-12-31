package ru.tinkoff.qa.neptune.core.api.concurency;

import ru.tinkoff.qa.neptune.core.api.cleaning.Stoppable;
import ru.tinkoff.qa.neptune.core.api.steps.context.Context;

public class TestContext extends Context<TestContext> implements Stoppable {

    private static final TestContext context = getInstance(TestContext.class);

    public static TestContext getContext() {
        return context;
    }

    private boolean isActive = true;

    public void doSomething() {
        //there is nothing to do
        isActive = true;
    }

    public Object getSomething() {
        isActive = true;
        return new Object();
    }

    @Override
    public void stop() {
        isActive = false;
    }

    boolean isActive() {
        return isActive;
    }
}
