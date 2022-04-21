package ru.tinkoff.qa.neptune.database.abstractions.dictionary;

import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;

import static ru.tinkoff.qa.neptune.core.api.localization.StepLocalization.translate;

@Description("Order specifier")
public final class OrderSpec {

    @Override
    public String toString() {
        return translate(this);
    }
}
