package ru.tinkoff.qa.neptune.core.api.event.firing.annotation;

import ru.tinkoff.qa.neptune.core.api.event.firing.Captor;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import static java.util.Optional.ofNullable;

public final class CaptorFilterByProducedType {

    private final Class<?> classOfProducedValue;

    public CaptorFilterByProducedType(Class<?> classOfProducedValue) {
        this.classOfProducedValue = classOfProducedValue;
    }

    public boolean matches(Captor<?, ?> captor) {
        Class<?> superClass = captor.getClass();
        Type genericSuperClass = null;

        while (!Captor.class.equals(superClass)) {
            genericSuperClass = superClass.getGenericSuperclass();
            superClass = superClass.getSuperclass();
        }

        if (genericSuperClass == null) {
            return false;
        }

        var args = ((ParameterizedType) genericSuperClass).getActualTypeArguments();
        if (args.length != 2) {
            return false;
        }

        var parameter = (Class<?>) args[1];
        return classOfProducedValue.isAssignableFrom(parameter);
    }


    @Override
    public boolean equals(Object toBeEqual) {
        return ofNullable(toBeEqual)
                .map(o -> CaptorFilterByProducedType.class.equals(o.getClass())
                        && ((CaptorFilterByProducedType) o).classOfProducedValue.equals(classOfProducedValue))
                .orElse(false);
    }
}
