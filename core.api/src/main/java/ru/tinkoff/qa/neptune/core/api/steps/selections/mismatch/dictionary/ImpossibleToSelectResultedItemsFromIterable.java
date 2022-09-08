package ru.tinkoff.qa.neptune.core.api.steps.selections.mismatch.dictionary;

import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;

import static ru.tinkoff.qa.neptune.core.api.localization.StepLocalization.translate;

@Description("It is not possible to select resulted items because:")
public final class ImpossibleToSelectResultedItemsFromIterable {

    @Override
    public String toString() {
        return translate(this);
    }
}
