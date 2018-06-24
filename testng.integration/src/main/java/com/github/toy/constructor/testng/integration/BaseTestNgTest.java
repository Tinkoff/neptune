package com.github.toy.constructor.testng.integration;

import org.testng.IObjectFactory;
import org.testng.annotations.Listeners;
import org.testng.annotations.ObjectFactory;

@Listeners(DefaultTestRunningListener.class)
public abstract class BaseTestNgTest {

    @ObjectFactory
    public static IObjectFactory setObjectFactory() {
        return new DefaultObjectFactory();
    }
}
