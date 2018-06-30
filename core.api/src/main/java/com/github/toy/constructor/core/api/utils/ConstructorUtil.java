package com.github.toy.constructor.core.api.utils;

import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static java.lang.String.format;
import static java.util.Arrays.asList;
import static java.util.Map.entry;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;

public final class ConstructorUtil {

    private static final Map<Class<?>, Class<?>> FOR_USED_SIMPLE_TYPES =
            Map.ofEntries(entry(Integer.class, int.class),
                    entry(Long.class, long.class),
                    entry(Boolean.class, boolean.class),
                    entry(Byte.class, byte.class),
                    entry(Short.class, short.class),
                    entry(Float.class, float.class),
                    entry(Double.class, double.class),
                    entry(Character.class, char.class));

    private ConstructorUtil() {
        super();
    }

    public static <T> Constructor<T> findSuitableConstructor(Class<T> clazz, Object...params) throws Exception {
        List<Constructor<?>> constructorList = asList(clazz.getDeclaredConstructors());
        final List<Class<?>> paramTypes = Arrays.stream(params).map(o -> ofNullable(o)
                .map(Object::getClass)
                .orElse(null))
                .collect(toList());

        Constructor<?> foundConstructor = constructorList.stream().filter(constructor -> {
            List<Class<?>> constructorTypes = asList(constructor.getParameterTypes());
            return constructorTypes.size() == paramTypes.size() && matches(constructorTypes, paramTypes);
        })
                .findFirst().orElseThrow(() -> new NoSuchMethodException(
                        format("There is no constructor that convenient to parameter list %s", paramTypes)));
        foundConstructor.setAccessible(true);
        return (Constructor<T>) foundConstructor;
    }

    private static boolean matches(List<Class<?>> constructorTypes,
                                   List<Class<?>> paramTypes) {
        int i = -1;
        for (Class<?> parameter : constructorTypes) {
            i++;
            Class<?> currentType = paramTypes.get(i);
            if (currentType == null && FOR_USED_SIMPLE_TYPES.get(parameter) != null) {
                return false;
            }
            else if (currentType == null){
                continue;
            }

            if (parameter.isAssignableFrom(currentType)) {
                continue;
            }

            Class<?> simple;
            if ((simple = FOR_USED_SIMPLE_TYPES.get(currentType)) != null &&
                    parameter.isAssignableFrom(simple)) {
                continue;
            }

            Class<?> declaredArrayType = parameter.getComponentType();
            Class<?> currentArrayType = currentType.getComponentType();
            if (declaredArrayType != null && currentArrayType != null &&
                    declaredArrayType.isAssignableFrom(currentArrayType)) {
                continue;
            }
            return false;
        }
        return true;
    }
}
