package ru.tinkoff.qa.neptune.core.api.event.firing.annotations;

import io.github.classgraph.ClassGraph;
import ru.tinkoff.qa.neptune.core.api.event.firing.Captor;
import ru.tinkoff.qa.neptune.core.api.event.firing.captors.FileCaptor;
import ru.tinkoff.qa.neptune.core.api.event.firing.captors.ImageCaptor;
import ru.tinkoff.qa.neptune.core.api.event.firing.captors.StringCaptor;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static java.lang.reflect.Modifier.isAbstract;
import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toList;

@SuppressWarnings("rawtypes")
final class CaptorUtil {

    private static final Map<Class<?>, Class<? extends Captor>> ABSTRACT_CAPTORS = mapOfAbstractCaptors();

    private static Map<Class<?>, Class<? extends Captor>> mapOfAbstractCaptors() {
        var children = new ClassGraph()
                .enableAllInfo()
                .scan()
                .getSubclasses(Captor.class.getName())
                .loadClasses(Captor.class);

        var abstractChildren = children.stream()
                .filter(captorClass -> isAbstract(captorClass.getModifiers())
                        && !captorClass.equals(Captor.class)
                        && !captorClass.equals(ImageCaptor.class)
                        && !captorClass.equals(FileCaptor.class)
                        && !captorClass.equals(StringCaptor.class));

        var result = new HashMap<Class<?>, Class<? extends Captor>>();
        abstractChildren.forEach(captorClass -> children
                .stream()
                .filter(captorClass1 -> !isAbstract(captorClass1.getModifiers())
                        && captorClass.isAssignableFrom(captorClass1))
                .findFirst()
                .ifPresent(captorClass1 -> result.put(captorClass, captorClass1)));

        return result;
    }

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
                    return ABSTRACT_CAPTORS.get(cls);
                })
                .filter(Objects::nonNull)
                .map(aClass -> createCaptor((Class<Captor<Object, Object>>) aClass))
                .collect(toList());
    }
}
