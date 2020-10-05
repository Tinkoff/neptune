package ru.tinkoff.qa.neptune.testng.integration;

import org.testng.annotations.Listeners;

import static ru.tinkoff.qa.neptune.core.api.dependency.injection.DependencyInjector.injectValues;

@Listeners(DefaultTestRunningListener.class)
public abstract class BaseTestNgTest {

    public BaseTestNgTest() {
        injectValues(this);
    }
}
