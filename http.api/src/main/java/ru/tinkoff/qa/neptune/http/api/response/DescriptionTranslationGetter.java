package ru.tinkoff.qa.neptune.http.api.response;

import ru.tinkoff.qa.neptune.core.api.steps.parameters.ParameterValueGetter;

import static ru.tinkoff.qa.neptune.core.api.steps.localization.StepLocalization.translate;

public class DescriptionTranslationGetter implements ParameterValueGetter<String> {

    @Override
    public String getParameterValue(String fieldValue) {
        return translate(fieldValue);
    }
}
