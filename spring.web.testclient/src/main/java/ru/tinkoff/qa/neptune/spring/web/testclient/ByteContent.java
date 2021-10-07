package ru.tinkoff.qa.neptune.spring.web.testclient;

import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;

import static ru.tinkoff.qa.neptune.core.api.localization.StepLocalization.translate;

@Description("Body byte content")
final class ByteContent {
    @Override
    public String toString() {
        return translate(this);
    }
}
