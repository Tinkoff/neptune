package ru.tinkoff.qa.neptune.spring.data.select.dictionary;

import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;

import static ru.tinkoff.qa.neptune.core.api.localization.StepLocalization.translate;

@Description("Invocation arguments")
public final class InvocationArguments {

    @Override
    public String toString() {
        return translate(this);
    }
}
