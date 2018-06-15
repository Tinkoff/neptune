package com.github.toy.constructor.testng.integration;

import org.testng.annotations.ObjectFactory;
import org.testng.internal.ObjectFactoryImpl;

import java.lang.reflect.Constructor;

public class DefaultObjectFactory extends ObjectFactoryImpl {

    @ObjectFactory
    @Override
    public Object newInstance(Constructor constructor, Object... params) {
        return super.newInstance(constructor, params);
    }
}
