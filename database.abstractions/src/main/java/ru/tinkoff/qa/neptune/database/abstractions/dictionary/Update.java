package ru.tinkoff.qa.neptune.database.abstractions.dictionary;

import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;

import static ru.tinkoff.qa.neptune.core.api.localization.StepLocalization.translate;

@Description("Update")
public final class Update {
    public String toString() {
        return translate(this);
    }
}
