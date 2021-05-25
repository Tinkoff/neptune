package ru.tinkoff.qa.neptune.check.parameter.value.getters;

import org.hamcrest.Matcher;
import ru.tinkoff.qa.neptune.core.api.steps.parameters.ParameterValueGetter;

import static ru.tinkoff.qa.neptune.core.api.hamcrest.NeptuneFeatureMatcher.concatMatcherDescriptions;
import static ru.tinkoff.qa.neptune.core.api.logical.lexemes.OnlyOne.ONLY_ONE_LEXEME;

public final class OnlyOneParameterValueGetter implements ParameterValueGetter<Matcher<?>[]> {

    @Override
    public String getParameterValue(Matcher<?>[] fieldValue) {
        return concatMatcherDescriptions(" " + ONLY_ONE_LEXEME + " ", fieldValue);
    }
}
