package ru.tinkoff.qa.neptune.core.api.localization;

import io.github.classgraph.ClassGraph;
import ru.tinkoff.qa.neptune.core.api.steps.SequentialActionSupplier;
import ru.tinkoff.qa.neptune.core.api.steps.SequentialGetStepSupplier;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;

import java.util.ArrayList;
import java.util.List;

import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.toCollection;

final class CriteriaBundleFilter extends DefaultAbstractBundleFiller {

    final static List<Class<?>> CRITERIA = new ClassGraph()
            .enableAllInfo()
            .scan()
            .getClassesWithMethodAnnotation(Description.class.getName())
            .loadClasses(true)
            .stream()
            .filter(classClass ->
                    !SequentialActionSupplier.class.isAssignableFrom(classClass)
                            &&
                            !SequentialGetStepSupplier.class.isAssignableFrom(classClass))
            .sorted(comparing(Class::getName))
            .collect(toCollection(ArrayList::new));

    protected CriteriaBundleFilter(LocalizationBundlePartition p) {
        super(p, CRITERIA, "CRITERIA");
    }
}
