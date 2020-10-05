package ru.tinkoff.qa.neptune.testng.integration.test;

import ru.tinkoff.qa.neptune.core.api.dependency.injection.DependencyInjector;

import java.lang.reflect.Field;

public class TestDependencyInjector implements DependencyInjector {

    @Override
    public boolean toSet(Field field) {
        var name = field.getName();
        return name.equals("a") || name.equals("b") || name.equals("c");
    }

    @Override
    public Object getValueToSet(Field field) {
        var name = field.getName();
        if (name.equals("a")) {
            return 1;
        }

        if (name.equals("b")) {
            return 2;
        }

        return 3;
    }
}
