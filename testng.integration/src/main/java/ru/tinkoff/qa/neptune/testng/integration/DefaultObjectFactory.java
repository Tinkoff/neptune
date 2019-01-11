package ru.tinkoff.qa.neptune.testng.integration;

import ru.tinkoff.qa.neptune.core.api.steps.context.ActionStepContext;
import ru.tinkoff.qa.neptune.core.api.steps.context.CreateWith;
import ru.tinkoff.qa.neptune.core.api.steps.context.GetStepContext;
import org.testng.TestNGException;
import org.testng.annotations.ObjectFactory;
import org.testng.internal.ObjectFactoryImpl;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;

import static ru.tinkoff.qa.neptune.core.api.steps.proxy.ProxyFactory.getProxied;
import static ru.tinkoff.qa.neptune.core.api.properties.GeneralPropertyInitializer.refreshProperties;
import static java.lang.reflect.Modifier.isFinal;
import static java.lang.reflect.Modifier.isStatic;
import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toList;

public class DefaultObjectFactory extends ObjectFactoryImpl {

    private final Map<Class<?>, Object> stepMap = new HashMap<>();
    private boolean arePropertiesInitialized;

    DefaultObjectFactory() {
        super();
        if (!arePropertiesInitialized) {
            refreshProperties();
            arePropertiesInitialized = true;
        }
    }

    /**
     * This factory method does the same as {@link ObjectFactoryImpl#newInstance(Constructor, Object...)} does
     * and fills fields of type that extend {@link GetStepContext} and/or {@link ActionStepContext}.
     * <p>
     *     WARNING!!!!
     *     It is supposed that every class (or super-classes) whose instance is supposed be set as a field value should be annotated
     *     by {@link CreateWith} by default.
     *     Also test classes should extend {@link BaseTestNgTest}
     * </p>
     *
     * @param constructor of a test class.
     * @param params to instantiate the test class.
     * @return created object of the test class.
     */
    @ObjectFactory
    @Override
    public Object newInstance(Constructor constructor, Object... params) {

        var result = super.newInstance(constructor, params);
        var clazz = result.getClass();
        while (!clazz.equals(Object.class)) {
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
                    var objectToSet = stepMap.entrySet().stream()
                            .filter(entry -> entry.getKey().isAssignableFrom(fieldType)
                                    || fieldType.isAssignableFrom(entry.getKey()))
                            .findFirst().map(Map.Entry::getValue)
                            .orElseGet(() -> {
                                try {
                                    var toBeReturned = getProxied(fieldType);
                                    stepMap.put(fieldType, toBeReturned);
                                    return toBeReturned;
                                } catch (Throwable t) {
                                    throw new TestNGException(t.getMessage(), t);
                                }
                            });

                    field.set(result, objectToSet);
                } catch (Exception e) {
                    throw new TestNGException(e.getMessage(), e);
                }
            });
            clazz = clazz.getSuperclass();
        }
        return result;
    }
}
