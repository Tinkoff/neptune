package ru.tinkoff.qa.neptune.testng.integration;

import org.testng.IObjectFactory;
import org.testng.annotations.Listeners;
import org.testng.annotations.ObjectFactory;
import ru.tinkoff.qa.neptune.core.api.steps.context.ActionStepContext;
import ru.tinkoff.qa.neptune.core.api.steps.context.GetStepContext;

@Listeners(DefaultTestRunningListener.class)
public abstract class BaseTestNgTest<T extends BaseTestNgTest<T>> implements GetStepContext<T>, ActionStepContext<T> {

    private static final IObjectFactory OBJECT_FACTORY = new DefaultObjectFactory();

    @ObjectFactory
    public static IObjectFactory setObjectFactory() {
        return OBJECT_FACTORY;
    }
}
