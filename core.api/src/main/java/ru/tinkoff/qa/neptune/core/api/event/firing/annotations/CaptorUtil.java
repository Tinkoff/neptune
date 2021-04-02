package ru.tinkoff.qa.neptune.core.api.event.firing.annotations;

import io.github.classgraph.ClassGraph;
import ru.tinkoff.qa.neptune.core.api.event.firing.Captor;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Objects;

import static java.lang.reflect.Modifier.isAbstract;
import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toList;

final class CaptorUtil {

    private static Captor<Object, Object> createCaptor(Class<Captor<Object, Object>> captorClass) {
        try {
            var c = captorClass.getConstructor();
            c.setAccessible(true);
            return c.newInstance();
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings("unchecked")
    static List<Captor<Object, Object>> createCaptors(Class<? extends Captor<?, ?>>[] classes) {
        return stream(classes)
                .map(cls -> {
                    var m = cls.getModifiers();
                    if (!isAbstract(m)) {
                        return cls;
                    }

                    var children = new ClassGraph()
                            .enableAllInfo()
                            .scan()
                            .getSubclasses(cls.getName())
                            .loadClasses(cls);

                    if (children.size() > 0) {
                        return children.get(0);
                    }

                    return null;
                })
                .filter(Objects::nonNull)
                .map(aClass -> createCaptor((Class<Captor<Object, Object>>) aClass))
                .collect(toList());
    }
}
