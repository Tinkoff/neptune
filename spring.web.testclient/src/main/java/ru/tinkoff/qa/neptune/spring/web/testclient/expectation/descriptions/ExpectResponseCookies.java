package ru.tinkoff.qa.neptune.spring.web.testclient.expectation.descriptions;

import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;

import static ru.tinkoff.qa.neptune.core.api.localization.StepLocalization.translate;

@Description("Response cookie(s)")
public final class ExpectResponseCookies {
    @Override
    public String toString() {
        return translate(this);
    }
}
