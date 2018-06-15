package com.github.toy.constructor.testng.integration;

import org.testng.IObjectFactory;
import org.testng.annotations.ObjectFactory;

public abstract class BaseTestNgTest {

    @ObjectFactory
    public static IObjectFactory setObjectFactory() {
        return new DefaultObjectFactory();
    }
}
