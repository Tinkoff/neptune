package ru.tinkoff.qa.neptune.core.api.hamcrest;

import static ru.tinkoff.qa.neptune.core.api.steps.localization.StepLocalization.translate;

/**
 * Helper class that builds description of mismatch using parameters. Also
 * it makes mismatch descriptions able to be re-used.
 * Warning!!!! Every class that extends this abstraction should be annotated
 * by {@link ru.tinkoff.qa.neptune.core.api.steps.annotations.Description} to
 * make description of a mismatch to be translatable.
 */
public abstract class MismatchDescriber {

    @Override
    public String toString() {
        return translate(this);
    }
}