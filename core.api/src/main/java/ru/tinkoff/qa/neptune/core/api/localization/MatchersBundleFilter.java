package ru.tinkoff.qa.neptune.core.api.localization;

import io.github.classgraph.ClassGraph;
import ru.tinkoff.qa.neptune.core.api.hamcrest.NeptuneFeatureMatcher;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;

import java.util.List;

import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.toList;

final class MatchersBundleFilter extends DefaultAbstractBundleFiller {

    MatchersBundleFilter(LocalizationBundlePartition p) {
        super(p, getMatchers(), "MATCHERS");
    }

    private static List<Class<?>> getMatchers() {
        return new ClassGraph()
                .enableClassInfo()
                .ignoreClassVisibility()
                .scan()
                .getSubclasses(NeptuneFeatureMatcher.class.getName())
                .loadClasses(NeptuneFeatureMatcher.class)
                .stream()
                .filter(c -> c.getAnnotation(Description.class) != null)
                .map(cls -> (Class<?>) cls)
                .sorted(comparing(Class::getName))
                .collect(toList());
    }
}
