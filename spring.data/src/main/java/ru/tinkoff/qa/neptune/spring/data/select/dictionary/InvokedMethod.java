package ru.tinkoff.qa.neptune.spring.data.select.dictionary;

import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;

import static ru.tinkoff.qa.neptune.core.api.localization.StepLocalization.translate;

@Description("Select by method invocation")
public final class InvokedMethod {

    @Override
    public String toString() {
        return translate(this);
    }
}
