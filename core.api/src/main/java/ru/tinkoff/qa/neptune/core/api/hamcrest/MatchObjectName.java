package ru.tinkoff.qa.neptune.core.api.hamcrest;

import static ru.tinkoff.qa.neptune.core.api.localization.StepLocalization.translate;

/**
 * Implementors of this class are used to append name of an object to {@link ObjectIsNotPresentMismatch} and
 * {@link MismatchDescriber}. t is necessary to annotate an implementor by {@link ru.tinkoff.qa.neptune.core.api.steps.annotations.Description}
 * and fulfill the description by values of fields annotated by {@link ru.tinkoff.qa.neptune.core.api.steps.annotations.DescriptionFragment}
 */
public abstract class MatchObjectName {

    @Override
    public final String toString() {
        return translate(this);
    }
}
