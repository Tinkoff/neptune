package ru.tinkoff.qa.neptune.core.api.localization;

import io.github.classgraph.ClassGraph;
import ru.tinkoff.qa.neptune.core.api.steps.Criteria;
import ru.tinkoff.qa.neptune.core.api.steps.SequentialActionSupplier;
import ru.tinkoff.qa.neptune.core.api.steps.SequentialGetStepSupplier;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;

import java.util.ArrayList;
import java.util.List;

import static java.util.Arrays.stream;
import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.toCollection;

final class CriteriaBundleFilter extends DefaultAbstractBundleFiller {

    CriteriaBundleFilter(LocalizationBundlePartition p) {
        super(p, getCriteria(), "CRITERIA");
    }

    private static List<Class<?>> getCriteria() {
        return new ClassGraph()
                .enableClassInfo()
                .ignoreClassVisibility()
                .enableMethodInfo()
                .ignoreMethodVisibility()
                .enableAnnotationInfo()
                .scan()
                .getClassesWithMethodAnnotation(Description.class.getName())
                .loadClasses(true)
                .stream()
                .filter(classClass ->
                        !SequentialActionSupplier.class.isAssignableFrom(classClass)
                                && !SequentialGetStepSupplier.class.isAssignableFrom(classClass)
                                && stream(classClass.getDeclaredMethods()).anyMatch(m -> Criteria.class.isAssignableFrom(m.getReturnType())))
                .sorted(comparing(Class::getName))
                .collect(toCollection(ArrayList::new));
    }
}
