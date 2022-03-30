package ru.tinkoff.qa.neptune.core.api.localization;

import io.github.classgraph.ClassGraph;
import ru.tinkoff.qa.neptune.core.api.event.firing.Captor;
import ru.tinkoff.qa.neptune.core.api.hamcrest.MatchObjectName;
import ru.tinkoff.qa.neptune.core.api.hamcrest.MismatchDescriber;
import ru.tinkoff.qa.neptune.core.api.hamcrest.NeptuneFeatureMatcher;
import ru.tinkoff.qa.neptune.core.api.steps.SequentialActionSupplier;
import ru.tinkoff.qa.neptune.core.api.steps.SequentialGetStepSupplier;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;

import java.util.List;

import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.toList;

final class OtherObjectsBundleFilter extends DefaultAbstractBundleFiller {

    OtherObjectsBundleFilter(List<Class<?>> toExclude, LocalizationBundlePartition p) {
        super(p, getOther(toExclude), "OTHER");
    }

    public static synchronized List<Class<?>> getOther(List<Class<?>> toExclude) {
        return new ClassGraph()
                .enableClassInfo()
                .enableAnnotationInfo()
                .ignoreClassVisibility()
                .scan()
                .getClassesWithAnnotation(Description.class.getName())
                .loadClasses(true)
                .stream()
                .filter(c ->
                        !toExclude.contains(c) &&
                                !Captor.class.isAssignableFrom(c) &&
                                !NeptuneFeatureMatcher.class.isAssignableFrom(c) &&
                                !MismatchDescriber.class.isAssignableFrom(c) &&
                                !MatchObjectName.class.isAssignableFrom(c) &&
                                !SequentialGetStepSupplier.class.isAssignableFrom(c) &&
                                !SequentialActionSupplier.class.isAssignableFrom(c))
                .map(cls -> (Class<?>) cls)
                .sorted(comparing(Class::getName))
                .collect(toList());
    }
}
