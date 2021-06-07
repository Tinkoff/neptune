package ru.tinkoff.qa.neptune.http.api.hamcrest.response;

import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;

import static ru.tinkoff.qa.neptune.core.api.steps.localization.StepLocalization.translate;

@Description("Header")
final class Header {
    @Override
    public String toString() {
        return translate(this);
    }
}
