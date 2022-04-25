package ru.tinkoff.qa.neptune.http.api.response.dictionary;

import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;

import static ru.tinkoff.qa.neptune.core.api.localization.StepLocalization.translate;

@Description("at least one item")
public final class HasAtLeastOneItem {

    @Override
    public String toString() {
        return translate(this);
    }
}
