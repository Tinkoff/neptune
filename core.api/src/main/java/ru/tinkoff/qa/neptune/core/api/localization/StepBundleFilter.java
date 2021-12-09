package ru.tinkoff.qa.neptune.core.api.localization;

import io.github.classgraph.ClassGraph;
import ru.tinkoff.qa.neptune.core.api.steps.SequentialActionSupplier;
import ru.tinkoff.qa.neptune.core.api.steps.SequentialGetStepSupplier;

import java.util.LinkedList;
import java.util.List;

import static java.util.Arrays.asList;
import static java.util.Comparator.comparing;
import static java.util.List.of;
import static java.util.Objects.nonNull;
import static java.util.stream.Collectors.toList;

final class StepBundleFilter extends DefaultAbstractBundleFiller {

    private static LinkedList<Class<?>> steps;

    protected StepBundleFilter(LocalizationBundlePartition p) {
        super(p, getStepClasses(), "STEPS");
    }

    public static synchronized List<Class<?>> getStepClasses() {
        if (nonNull(steps)) {
            return steps;
        }

        steps = new LinkedList<>();
        of(SequentialActionSupplier.class, SequentialGetStepSupplier.class).forEach(cls -> {
            var nestMembers = asList(cls.getNestMembers());
            steps.addAll(new ClassGraph()
                    .enableClassInfo()
                    .ignoreClassVisibility()
                    .scan()
                    .getSubclasses(cls.getName())
                    .loadClasses(cls)
                    .stream()
                    .filter(c -> !nestMembers.contains(c))
                    .collect(toList()));
        });

        steps.sort(comparing(Class::getName));
        steps.addFirst(SequentialActionSupplier.class);
        steps.addFirst(SequentialGetStepSupplier.class);
        return steps;
    }
}
