package ru.tinkoff.qa.neptune.core.api.event.firing.annotations;

import io.github.classgraph.ClassGraph;
import ru.tinkoff.qa.neptune.core.api.event.firing.Captor;
import ru.tinkoff.qa.neptune.core.api.event.firing.captors.FileCaptor;
import ru.tinkoff.qa.neptune.core.api.event.firing.captors.ImageCaptor;
import ru.tinkoff.qa.neptune.core.api.event.firing.captors.StringCaptor;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.lang.reflect.Modifier.isAbstract;
import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toList;

@SuppressWarnings("rawtypes")
public final class CaptorUtil {

    private static final Map<Class<?>, List<Class<Captor>>> ABSTRACT_CAPTORS = mapOfAbstractCaptors();

    private static Map<Class<?>, List<Class<Captor>>> mapOfAbstractCaptors() {
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

        var result = new HashMap<Class<?>, List<Class<Captor>>>();
        abstractChildren.forEach(captorClass -> {
            var found = children.stream()
                    .filter(captorClass1 -> !isAbstract(captorClass1.getModifiers())
                            && captorClass.isAssignableFrom(captorClass1))
                    .collect(toList());
            if (found.size() > 0) {
                result.put(captorClass, found);
            }
        });

        return result;
    }

    private static Captor createCaptor(Class<? extends Captor> captorClass) {
        try {
            var c = captorClass.getConstructor();
            c.setAccessible(true);
            return c.newInstance();
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings("unchecked")
    public static List<Captor<Object, Object>> createCaptors(Class<? extends Captor>[] classes) {
        var result = new ArrayList<Captor<Object, Object>>();
        stream(classes)
                .forEach(cls -> {
                    var m = cls.getModifiers();
                    if (!isAbstract(m)) {
                        result.add(createCaptor(cls));
                        return;
                    }

                    var children = ABSTRACT_CAPTORS.get(cls);
                    if (children != null) {
                        children.forEach(captorClass -> result.add(createCaptor(captorClass)));
                    }
                });
        return result;
    }
}
