package ru.tinkoff.qa.neptune.check.parameter.value.getters;

import org.hamcrest.Matcher;
import ru.tinkoff.qa.neptune.core.api.hamcrest.NeptuneFeatureMatcher;
import ru.tinkoff.qa.neptune.core.api.steps.parameters.ParameterValueGetter;

import static ru.tinkoff.qa.neptune.core.api.logical.lexemes.Not.NOT_LEXEME;
import static ru.tinkoff.qa.neptune.core.api.steps.localization.StepLocalization.translate;

public final class NotParameterValueGetter implements ParameterValueGetter<Matcher<?>> {

    @Override
    public String getParameterValue(Matcher<?> fieldValue) {
        if (fieldValue instanceof NeptuneFeatureMatcher) {
            return NOT_LEXEME + " " + fieldValue;
        }

        return translate("not " + fieldValue);
    }
}
