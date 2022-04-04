package ru.tinkoff.qa.neptune.spring.web.testclient.localization;

import org.springframework.test.web.reactive.server.*;
import ru.tinkoff.qa.neptune.core.api.localization.BindToPartition;
import ru.tinkoff.qa.neptune.core.api.localization.BundleFillerExtension;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.util.List;

import static java.lang.reflect.Modifier.isPublic;
import static java.util.Arrays.stream;
import static java.util.Comparator.comparing;
import static java.util.List.of;
import static java.util.stream.Collectors.toList;

@BindToPartition("spring.assertions")
@BindToPartition("spring.web.testclient")
public final class SpringWebTestClientMatcherBundleExtension extends BundleFillerExtension {

    public SpringWebTestClientMatcherBundleExtension() {
        super(of(
                StatusAssertions.class,
                HeaderAssertions.class,
                CookieAssertions.class,
                JsonPathAssertions.class,
                XpathAssertions.class
        ), "SPRING WEB TEST CLIENT CRITERIA");
    }

    @Override
    protected List<AnnotatedElement> addFields(Class<?> clazz) {
        return of();
    }

    @Override
    protected List<Method> addMethods(Class<?> clazz) {
        return stream(clazz.getDeclaredMethods())
                .filter(m -> isPublic(m.getModifiers()) && !"equals".equals(m.getName()) && !"hashCode".equals(m.getName()))
                .sorted(comparing(Method::toString))
                .collect(toList());
    }
}
