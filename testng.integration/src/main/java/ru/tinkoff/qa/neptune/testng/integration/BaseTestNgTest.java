package ru.tinkoff.qa.neptune.testng.integration;

import org.testng.TestNGException;
import org.testng.annotations.Listeners;
import ru.tinkoff.qa.neptune.core.api.steps.context.ActionStepContext;
import ru.tinkoff.qa.neptune.core.api.steps.context.GetStepContext;

import static java.lang.reflect.Modifier.isFinal;
import static java.lang.reflect.Modifier.isStatic;
import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toList;
import static ru.tinkoff.qa.neptune.core.api.properties.GeneralPropertyInitializer.refreshProperties;
import static ru.tinkoff.qa.neptune.testng.integration.InstantiatedContexts.getInstantiatedContext;

@Listeners(DefaultTestRunningListener.class)
public abstract class BaseTestNgTest<T extends BaseTestNgTest<T>> implements GetStepContext<T>, ActionStepContext<T> {
    static {
        initProperties();
    }

    private static boolean arePropertiesInitialized = false;

    private static void initProperties() {
        if (!arePropertiesInitialized) {
            refreshProperties();
            arePropertiesInitialized = true;
        }
    }

    private static <T> void initContexts(T obj) {
        Class<?> clazz = obj.getClass();
        while (!clazz.equals(Object.class) && !clazz.equals(Class.class)) {
            var fields = stream(clazz.getDeclaredFields())
                    .filter(field -> {
                        var type = field.getType();
                        var modifiers = field.getModifiers();
                        return !isStatic(modifiers) && !isFinal(modifiers)
                                && (GetStepContext.class.isAssignableFrom(type)
                                || ActionStepContext.class.isAssignableFrom(type));
                    }).collect(toList());

            fields.forEach(field -> {
                field.setAccessible(true);
                try {
                    var fieldType = field.getType();
                    var objectToSet = getInstantiatedContext(fieldType);

                    field.set(obj, objectToSet);
                } catch (Exception e) {
                    throw new TestNGException(e.getMessage(), e);
                }
            });
            clazz = clazz.getSuperclass();
        }
    }

    public BaseTestNgTest() {
        super();
        initContexts(this);
    }
}
