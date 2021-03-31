package ru.tinkoff.qa.neptune.check;

import ru.tinkoff.qa.neptune.core.api.steps.parameters.ParameterValueGetter;

import static ru.tinkoff.qa.neptune.core.api.steps.localization.StepLocalization.translate;

public class DescriptionTranslator implements ParameterValueGetter<String> {

    @Override
    public String getParameterValue(String fieldValue) {
        return translate(fieldValue);
    }
}
