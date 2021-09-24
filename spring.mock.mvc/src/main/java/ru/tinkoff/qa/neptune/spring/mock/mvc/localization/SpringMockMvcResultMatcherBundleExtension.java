package ru.tinkoff.qa.neptune.spring.mock.mvc.localization;

import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import ru.tinkoff.qa.neptune.core.api.localization.BindToPartition;
import ru.tinkoff.qa.neptune.core.api.localization.BundleFillerExtension;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import static java.lang.reflect.Modifier.isPublic;
import static java.util.Arrays.stream;
import static java.util.Comparator.comparing;
import static java.util.List.of;
import static java.util.stream.Collectors.toList;

@BindToPartition("spring.result.matchers")
@BindToPartition("spring.mock.mvc")
public final class SpringMockMvcResultMatcherBundleExtension extends BundleFillerExtension {

    private final static List<Class<?>> CLASSES = prepareClasses();

    public SpringMockMvcResultMatcherBundleExtension() {
        super(CLASSES, "SPRING RESULT MATCHERS");
    }

    private static List<Class<?>> prepareClasses() {
        var result = new LinkedList<Class<?>>();
        result.addFirst(MockMvcResultMatchers.class);

        stream(MockMvcResultMatchers.class.getDeclaredMethods())
                .filter(SpringMockMvcResultMatcherBundleExtension::methodHasReturnedTypeThatCreatesResultMatcher)
                .map(Method::getReturnType)
                .filter(c -> !c.equals(ResultMatcher.class))
                .distinct()
                .sorted(comparing(Class::getName))
                .forEach(result::add);

        return result;
    }

    private static boolean methodHasReturnedTypeThatCreatesResultMatcher(Method m) {
        if (!isPublic(m.getModifiers())) {
            return false;
        }

        var returningType = m.getReturnType();
        return stream(returningType.getDeclaredMethods())
                .anyMatch(m1 -> isPublic(m1.getModifiers()) && Objects.equals(m1.getReturnType(), ResultMatcher.class));
    }

    public static List<Class<?>> getFactoryClasses() {
        return CLASSES;
    }

    @Override
    protected List<AnnotatedElement> addFields(Class<?> clazz) {
        return of();
    }

    @Override
    protected List<Method> addMethods(Class<?> clazz) {
        return stream(clazz.getDeclaredMethods())
                .filter(m -> isPublic(m.getModifiers()) &&
                        (Objects.equals(m.getReturnType(), ResultMatcher.class) || CLASSES.contains(m.getReturnType())))
                .sorted(comparing(Method::getName))
                .collect(toList());
    }
}
