package ru.tinkoff.qa.neptune.spring.mock.mvc.localization;

import org.springframework.test.web.servlet.ResultMatcher;
import ru.tinkoff.qa.neptune.core.api.localization.BundleFillerExtension;
import ru.tinkoff.qa.neptune.core.api.localization.ToIncludeClassDescription;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Objects;

import static java.lang.reflect.Modifier.isPublic;
import static java.util.Arrays.stream;
import static java.util.Comparator.comparing;
import static java.util.List.of;
import static java.util.stream.Collectors.toList;

public abstract class AbstractResultMatcherBundleExtension extends BundleFillerExtension {

    protected AbstractResultMatcherBundleExtension(List<Class<?>> toAdd,
                                                   String name,
                                                   ToIncludeClassDescription toIncludeClassDescription) {
        super(toAdd, name, toIncludeClassDescription);
    }

    protected AbstractResultMatcherBundleExtension(List<Class<?>> toAdd,
                                                   String name) {
        super(toAdd, name);
    }


    @Override
    protected List<AnnotatedElement> addFields(Class<?> clazz) {
        return of();
    }

    @Override
    protected List<Method> addMethods(Class<?> clazz) {
        return stream(clazz.getDeclaredMethods())
                .filter(m -> isPublic(m.getModifiers()) &&
                        (Objects.equals(m.getReturnType(), ResultMatcher.class)))
                .sorted(comparing(Method::toString))
                .collect(toList());
    }
}
