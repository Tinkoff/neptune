package ru.tinkoff.qa.neptune.core.api.localization.bundle.extensions;

import ru.tinkoff.qa.neptune.core.api.localization.BindToPartition;
import ru.tinkoff.qa.neptune.core.api.localization.BundleFillerExtension;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.util.List;

import static java.util.Arrays.stream;
import static java.util.List.of;
import static java.util.stream.Collectors.toList;

@BindToPartition("all.packages")
@BindToPartition("defined.packages")
public class TestExtension extends BundleFillerExtension {


    public TestExtension() {
        super(of(
                ru.tinkoff.qa.neptune.core.api.localization.additional.classes.ClassA.class,
                ru.tinkoff.qa.neptune.core.api.localization.additional.classes.ClassB.class,
                ru.tinkoff.qa.neptune.core.api.localization.additional.classes.ClassD.class,

                ru.tinkoff.qa.neptune.core.api.localization.some.classes.ClassA.class,
                ru.tinkoff.qa.neptune.core.api.localization.some.classes.ClassB.class,
                ru.tinkoff.qa.neptune.core.api.localization.some.classes.other.classes.ClassD.class,

                ru.tinkoff.qa.neptune.core.api.localization.ClassA.class,
                ru.tinkoff.qa.neptune.core.api.localization.ClassB.class,
                ru.tinkoff.qa.neptune.core.api.localization.ClassD.class
        ), "TEST OBJECTS", new TrestingToIncludeClassDescription());
    }

    @Override
    protected List<AnnotatedElement> addFields(Class<?> clazz) {
        return of();
    }

    @Override
    protected List<Method> addMethods(Class<?> clazz) {
        return stream(clazz.getDeclaredMethods())
                .filter(m -> stream(m.getParameterTypes()).anyMatch(Class::isArray))
                .collect(toList());
    }
}
