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
import static java.util.Objects.nonNull;
import static java.util.stream.Collectors.toList;
import static ru.tinkoff.qa.neptune.core.api.localization.AttachmentsBundleFilter.getAttachments;
import static ru.tinkoff.qa.neptune.core.api.localization.CriteriaBundleFilter.getCriteria;
import static ru.tinkoff.qa.neptune.core.api.localization.MatchedObjectsBundleFilter.getMatchedObjects;
import static ru.tinkoff.qa.neptune.core.api.localization.MatchersBundleFilter.getMatchers;
import static ru.tinkoff.qa.neptune.core.api.localization.MismatchDescriptionBundleFilter.getMismatchDescriptions;
import static ru.tinkoff.qa.neptune.core.api.localization.ParameterPojoBundleFilter.getParameterPojos;
import static ru.tinkoff.qa.neptune.core.api.localization.StepBundleFilter.getStepClasses;

final class OtherObjectsBundleFilter extends DefaultAbstractBundleFiller {

    private static List<Class<?>> other;

    protected OtherObjectsBundleFilter(LocalizationBundlePartition p) {
        super(p, getOther(), "OTHER");
    }

    public static synchronized List<Class<?>> getOther() {
        if (nonNull(other)) {
            return other;
        }

        other = new ClassGraph()
                .enableAllInfo()
                .scan(1)
                .getClassesWithAnnotation(Description.class.getName())
                .loadClasses(true)
                .stream()
                .filter(c -> !getStepClasses().contains(c) &&
                        !getAttachments().contains(c) &&
                        !getCriteria().contains(c) &&
                        !getMatchers().contains(c) &&
                        !getMismatchDescriptions().contains(c) &&
                        !getMatchedObjects().contains(c) &&
                        !getParameterPojos().contains(c) &&
                        !Captor.class.isAssignableFrom(c) &&
                        !NeptuneFeatureMatcher.class.isAssignableFrom(c) &&
                        !MismatchDescriber.class.isAssignableFrom(c) &&
                        !MatchObjectName.class.isAssignableFrom(c) &&
                        !SequentialGetStepSupplier.class.isAssignableFrom(c) &&
                        !SequentialActionSupplier.class.isAssignableFrom(c))
                .map(cls -> (Class<?>) cls)
                .sorted(comparing(Class::getName))
                .collect(toList());

        return other;
    }
}
