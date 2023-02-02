package ru.tinkoff.qa.neptune.core.api.hamcrest;

import ru.tinkoff.qa.neptune.core.api.steps.SelfDescribed;

/**
 * Implementors of this class are used to append name of an object to {@link ObjectIsNotPresentMismatch} and
 * {@link MismatchDescriber}. t is necessary to annotate an implementor by {@link ru.tinkoff.qa.neptune.core.api.steps.annotations.Description}
 * and fulfill the description by values of fields annotated by {@link ru.tinkoff.qa.neptune.core.api.steps.annotations.DescriptionFragment}
 */
public abstract class MatchObjectName extends SelfDescribed {
}
