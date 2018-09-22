package ru.tinkoff.qa.neptune.testng.integration;

import org.testng.IObjectFactory;
import org.testng.annotations.Listeners;
import org.testng.annotations.ObjectFactory;

@Listeners(DefaultTestRunningListener.class)
public abstract class BaseTestNgTest {

    private static final IObjectFactory OBJECT_FACTORY = new DefaultObjectFactory();

    @ObjectFactory
    public static IObjectFactory setObjectFactory() {
        return OBJECT_FACTORY;
    }
}
