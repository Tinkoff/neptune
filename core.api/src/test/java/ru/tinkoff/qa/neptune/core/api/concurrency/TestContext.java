package ru.tinkoff.qa.neptune.core.api.concurrency;

import ru.tinkoff.qa.neptune.core.api.cleaning.Stoppable;
import ru.tinkoff.qa.neptune.core.api.steps.context.Context;

import static ru.tinkoff.qa.neptune.core.api.steps.context.ContextFactory.getCreatedContextOrCreate;

public class TestContext extends Context<TestContext> implements Stoppable {

    public static TestContext getContext() {
        return getCreatedContextOrCreate(TestContext.class);
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
