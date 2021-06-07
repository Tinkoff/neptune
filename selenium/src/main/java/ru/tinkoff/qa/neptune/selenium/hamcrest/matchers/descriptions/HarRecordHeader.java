package ru.tinkoff.qa.neptune.selenium.hamcrest.matchers.descriptions;

import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;

import static ru.tinkoff.qa.neptune.core.api.steps.localization.StepLocalization.translate;

@Description("Header")
public final class HarRecordHeader {
    @Override
    public String toString() {
        return translate(this);
    }
}
