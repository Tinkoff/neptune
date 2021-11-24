package ru.tinkoff.qa.neptune.core.api.localization;

import io.github.classgraph.ClassGraph;
import ru.tinkoff.qa.neptune.core.api.hamcrest.NeptuneFeatureMatcher;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;

import java.util.List;

import static java.util.Comparator.comparing;
import static java.util.Objects.nonNull;
import static java.util.stream.Collectors.toList;

final class MatchersBundleFilter extends DefaultAbstractBundleFiller {

    private static List<Class<?>> matchers;

    protected MatchersBundleFilter(LocalizationBundlePartition p) {
        super(p, getMatchers(), "MATCHERS");
    }

    public static synchronized List<Class<?>> getMatchers() {
        if (nonNull(matchers)) {
            return matchers;
        }

        matchers = new ClassGraph()
                .enableAllInfo()
                .scan(1)
                .getSubclasses(NeptuneFeatureMatcher.class.getName())
                .loadClasses(NeptuneFeatureMatcher.class)
                .stream()
                .filter(c -> c.getAnnotation(Description.class) != null)
                .map(cls -> (Class<?>) cls)
                .sorted(comparing(Class::getName))
                .collect(toList());

        return matchers;
    }
}
