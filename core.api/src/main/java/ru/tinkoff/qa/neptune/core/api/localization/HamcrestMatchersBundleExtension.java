package ru.tinkoff.qa.neptune.core.api.localization;

import io.github.classgraph.ClassGraph;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import ru.tinkoff.qa.neptune.core.api.hamcrest.NeptuneFeatureMatcher;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.util.List;

import static java.lang.reflect.Modifier.isPublic;
import static java.util.Arrays.stream;
import static java.util.Comparator.comparing;
import static java.util.List.of;
import static java.util.stream.Collectors.toList;

@BindToPartition("core")
public class HamcrestMatchersBundleExtension extends BundleFillerExtension {

    public HamcrestMatchersBundleExtension() {
        super(prepareClasses(), "MATCHERS FROM ORG.HAMCREST");
    }

    private static boolean staticMethodThatReturnsMatcher(Method m) {
        return isPublic(m.getModifiers())
                && (Matcher.class.isAssignableFrom(m.getReturnType()))
                && !NeptuneFeatureMatcher.class.isAssignableFrom(m.getReturnType())
                && !"describedAs".equals(m.getName());
    }

    private static List<Class<?>> prepareClasses() {
        return new ClassGraph()
                .enableClassInfo()
                .ignoreClassVisibility()
                .scan()
                .getAllClasses()
                .loadClasses(true)
                .stream()
                .filter(c -> c.getPackageName().startsWith("org.hamcrest")
                        && !c.equals(Matchers.class)
                        && stream(c.getDeclaredMethods()).anyMatch(HamcrestMatchersBundleExtension::staticMethodThatReturnsMatcher))
                .distinct()
                .sorted(comparing(Class::getName))
                .collect(toList());
    }

    @Override
    protected List<AnnotatedElement> addFields(Class<?> clazz) {
        return of();
    }

    @Override
    protected List<Method> addMethods(Class<?> clazz) {
        return stream(clazz.getDeclaredMethods())
                .filter(HamcrestMatchersBundleExtension::staticMethodThatReturnsMatcher)
                .sorted(comparing(Method::toString))
                .collect(toList());
    }
}
