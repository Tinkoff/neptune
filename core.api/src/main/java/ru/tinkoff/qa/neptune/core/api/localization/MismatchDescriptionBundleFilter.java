package ru.tinkoff.qa.neptune.core.api.localization;

import io.github.classgraph.ClassGraph;
import ru.tinkoff.qa.neptune.core.api.hamcrest.MismatchDescriber;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;

import java.util.List;

import static java.util.Comparator.comparing;
import static java.util.Objects.nonNull;
import static java.util.stream.Collectors.toList;

final class MismatchDescriptionBundleFilter extends DefaultAbstractBundleFiller {

    private static List<Class<?>> mismatchDescriptions;

    protected MismatchDescriptionBundleFilter(LocalizationBundlePartition p) {
        super(p, getMismatchDescriptions(), "MISMATCH DESCRIPTIONS");
    }

    public static synchronized List<Class<?>> getMismatchDescriptions() {
        if (nonNull(mismatchDescriptions)) {
            return mismatchDescriptions;
        }

        mismatchDescriptions = new ClassGraph()
                .enableAllInfo()
                .scan(1)
                .getSubclasses(MismatchDescriber.class.getName())
                .loadClasses(MismatchDescriber.class)
                .stream()
                .filter(c -> c.getAnnotation(Description.class) != null)
                .map(cls -> (Class<?>) cls)
                .sorted(comparing(Class::getName))
                .collect(toList());

        return mismatchDescriptions;
    }
}
