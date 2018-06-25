package com.github.toy.constructor.testng.integration;

import com.github.toy.constructor.core.api.GetStep;
import com.github.toy.constructor.core.api.PerformStep;
import org.testng.TestNGException;
import org.testng.annotations.ObjectFactory;
import org.testng.internal.ObjectFactoryImpl;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.github.toy.constructor.core.api.Substitution.getSubstituted;
import static java.lang.reflect.Modifier.isFinal;
import static java.lang.reflect.Modifier.isStatic;
import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toList;

public class DefaultObjectFactory extends ObjectFactoryImpl {

    private final Map<Class<?>, Object> stepMap = new HashMap<>();

    /**
     * This factory method does the same as {@link ObjectFactoryImpl#newInstance(Constructor, Object...)} does
     * and fills fields of type that extend {@link GetStep} and/or {@link PerformStep}.
     * <p>
     *     WARNING!!!!
     *     It is supposed that every class which instance should be set as a field value should be annotated
     *     by {@link com.github.toy.constructor.core.api.CreateWith} by default.
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

        Object result = super.newInstance(constructor, params);
        Class<?> clazz = result.getClass();
        while (!clazz.equals(Object.class)) {
            List<Field> fields = stream(clazz.getDeclaredFields())
                    .filter(field -> {
                        Class<?> type = field.getType();
                        int modifiers = field.getModifiers();
                        return !isStatic(modifiers) && !isFinal(modifiers)
                                && (GetStep.class.isAssignableFrom(type)
                                || PerformStep.class.isAssignableFrom(type));
                    }).collect(toList());

            fields.forEach(field -> {
                field.setAccessible(true);
                try {
                    Class<?> fieldType = field.getType();
                    Object objectToSet = stepMap.entrySet().stream()
                            .filter(entry -> entry.getKey().isAssignableFrom(fieldType)
                                    || fieldType.isAssignableFrom(entry.getKey()))
                            .findFirst().map(Map.Entry::getValue)
                            .orElseGet(() -> {
                                try {
                                    Object toBeReturned = getSubstituted(fieldType);
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
