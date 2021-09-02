package ru.tinkoff.qa.neptune.core.api.hamcrest.common.any;

import org.hamcrest.Matcher;
import ru.tinkoff.qa.neptune.core.api.steps.parameters.ParameterValueGetter;

import static ru.tinkoff.qa.neptune.core.api.hamcrest.NeptuneFeatureMatcher.concatMatcherDescriptions;
import static ru.tinkoff.qa.neptune.core.api.logical.lexemes.Or.OR_LEXEME;

public final class AnyMatcherParameterValueGetter implements ParameterValueGetter<Matcher<?>[]> {

    @Override
    public String getParameterValue(Matcher<?>[] fieldValue) {
        return concatMatcherDescriptions(" " + OR_LEXEME + " ", fieldValue);
    }
}
