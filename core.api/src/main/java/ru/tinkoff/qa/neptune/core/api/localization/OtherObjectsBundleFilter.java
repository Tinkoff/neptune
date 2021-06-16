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
import static ru.tinkoff.qa.neptune.core.api.localization.AttachmentsBundleFilter.ATTACHMENTS;
import static ru.tinkoff.qa.neptune.core.api.localization.CriteriaBundleFilter.CRITERIA;
import static ru.tinkoff.qa.neptune.core.api.localization.MatchedObjectsBundleFilter.MATCHED_OBJECTS;
import static ru.tinkoff.qa.neptune.core.api.localization.MatchersBundleFilter.MATCHERS;
import static ru.tinkoff.qa.neptune.core.api.localization.MismatchDescriptionBundleFilter.MISMATCH_DESCRIPTIONS;
import static ru.tinkoff.qa.neptune.core.api.localization.StepBundleFilter.STEPS;

final class OtherObjectsBundleFilter extends DefaultAbstractBundleFiller {

    final static List<Class<?>> OTHER = new ClassGraph()
            .enableAllInfo()
            .scan()
            .getClassesWithAnnotation(Description.class.getName())
            .loadClasses(true)
            .stream()
            .filter(c -> !STEPS.contains(c) &&
                    !ATTACHMENTS.contains(c) &&
                    !CRITERIA.contains(c) &&
                    !MATCHERS.contains(c) &&
                    !MISMATCH_DESCRIPTIONS.contains(c) &&
                    !MATCHED_OBJECTS.contains(c) &&
                    !Captor.class.isAssignableFrom(c) &&
                    !NeptuneFeatureMatcher.class.isAssignableFrom(c) &&
                    !MismatchDescriber.class.isAssignableFrom(c) &&
                    !MatchObjectName.class.isAssignableFrom(c)&&
                    !SequentialGetStepSupplier.class.isAssignableFrom(c) &&
                    !SequentialActionSupplier.class.isAssignableFrom(c))
            .map(cls -> (Class<?>) cls)
            .sorted(comparing(Class::getName))
            .collect(toList());

    protected OtherObjectsBundleFilter(LocalizationBundlePartition p) {
        super(p, OTHER, "OTHER");
    }
}
