package ru.tinkoff.qa.neptune.core.api.event.firing.annotations;

import io.github.classgraph.ClassGraph;
import ru.tinkoff.qa.neptune.core.api.event.firing.Captor;
import ru.tinkoff.qa.neptune.core.api.event.firing.CapturedDataInjector;
import ru.tinkoff.qa.neptune.core.api.event.firing.captors.CapturedFileInjector;
import ru.tinkoff.qa.neptune.core.api.event.firing.captors.CapturedImageInjector;
import ru.tinkoff.qa.neptune.core.api.event.firing.captors.CapturedStringInjector;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import static java.lang.reflect.Modifier.isAbstract;
import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toList;

/**
 * Annotates subclasses of {@link Captor} to define injectors of attachments to log/test report.
 * Also it may be overridden in subclasses by declaration of another UseInjectors.
 */
@Retention(RUNTIME)
@Target(TYPE)
@SuppressWarnings({"rawtypes", "unchecked"})
public @interface UseInjectors {

    /**
     * @return implementors / extenders of {@link CapturedDataInjector} that inject attaches to log/test report.
     * ATTENTION!!! Defined classes should have no declared constructors or they should have declared constructors
     * without parameters. Also, it is possible to define abstract classes/extended interfaces there, but if they
     * have no non-abstract subclasses/implementors in classpath then they are ignored.
     */
    Class<? extends CapturedDataInjector>[] value();

    final class UseInjectorReader {

        private static final Map<Class<?>, List<Class<CapturedDataInjector>>> ABSTRACT_INJECTORS = mapOfAbstractInjectors();

        private static Map<Class<?>, List<Class<CapturedDataInjector>>> mapOfAbstractInjectors() {
            var children = new ClassGraph()
                    .enableAllInfo()
                    .scan()
                    .getClassesImplementing(CapturedDataInjector.class.getName())
                    .loadClasses(CapturedDataInjector.class);

            var abstractChildren = new HashSet<Class<?>>();
            abstractChildren.add(CapturedFileInjector.class);
            abstractChildren.add(CapturedImageInjector.class);
            abstractChildren.add(CapturedStringInjector.class);
            abstractChildren.addAll(children
                    .stream()
                    .filter(captorClass -> isAbstract(captorClass.getModifiers())).collect(toList()));

            var result = new HashMap<Class<?>, List<Class<CapturedDataInjector>>>();
            abstractChildren.forEach(abstractInjector -> {
                var found = children.stream()
                        .filter(injector -> !injector.isInterface()
                                && !isAbstract(injector.getModifiers())
                                && abstractInjector.isAssignableFrom(injector))
                        .collect(toList());

                if (found.size() > 0) {
                    result.put(abstractInjector, found);
                }
            });

            return result;
        }

        private static CapturedDataInjector createInjector(Class<? extends CapturedDataInjector> captorClass) {
            try {
                var c = captorClass.getConstructor();
                c.setAccessible(true);
                return c.newInstance();
            } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException(e);
            }
        }

        private static List<CapturedDataInjector<Object>> createInjectors(Class<? extends CapturedDataInjector>[] classes) {
            var result = new ArrayList<CapturedDataInjector<Object>>();
            stream(classes)
                    .forEach(cls -> {
                        if (!isAbstract(cls.getModifiers()) && !cls.isInterface()) {
                            result.add(createInjector(cls));
                            return;
                        }

                        var children = ABSTRACT_INJECTORS.get(cls);
                        if (children != null) {
                            children.forEach(captorClass -> result.add(createInjector(captorClass)));
                        }
                    });
            return result;
        }

        public static List<CapturedDataInjector<Object>> createInjectors(Class<?> clazz) {
            var result = new ArrayList<CapturedDataInjector<Object>>();

            var cls = clazz;
            while (!cls.equals(Object.class)) {
                var useInjectors = cls.getAnnotation(UseInjectors.class);
                if (useInjectors != null) {
                    result.addAll(createInjectors(useInjectors.value()));
                    return result;
                }
                cls = cls.getSuperclass();
            }
            return result;
        }
    }
}
