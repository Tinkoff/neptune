package ru.tinkoff.qa.neptune.core.api.localization;

import io.github.classgraph.ClassGraph;
import ru.tinkoff.qa.neptune.core.api.steps.SequentialActionSupplier;
import ru.tinkoff.qa.neptune.core.api.steps.SequentialGetStepSupplier;
import ru.tinkoff.qa.neptune.core.api.steps.parameters.StepParameterPojo;

import java.util.List;

import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.toList;

final class ParameterPojoBundleFilter extends DefaultAbstractBundleFiller {

    final static List<Class<?>> PARAMETER_POJOS = new ClassGraph()
            .enableAllInfo()
            .scan()
            .getClassesImplementing(StepParameterPojo.class.getName())
            .loadClasses(StepParameterPojo.class)
            .stream()
            .filter(c -> !SequentialGetStepSupplier.class.isAssignableFrom(c) &&
                    !SequentialActionSupplier.class.isAssignableFrom(c))
            .map(cls -> (Class<?>) cls)
            .sorted(comparing(Class::getName))
            .collect(toList());

    protected ParameterPojoBundleFilter(LocalizationBundlePartition p) {
        super(p, PARAMETER_POJOS, "PARAMETER WRAPPERS");
    }
}
