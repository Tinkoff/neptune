package ru.tinkoff.qa.neptune.core.api.hamcrest.common.not;

import org.hamcrest.Matcher;
import ru.tinkoff.qa.neptune.core.api.steps.parameters.ParameterValueGetter;

import static java.util.Arrays.stream;
import static java.util.stream.Collectors.joining;
import static ru.tinkoff.qa.neptune.core.api.logical.lexemes.Not.NOT_LEXEME;

public final class NotMatcherParameterValueGetter implements ParameterValueGetter<Matcher<?>[]> {

    @Override
    public String getParameterValue(Matcher<?>[] fieldValue) {
        return stream(fieldValue).map(m -> NOT_LEXEME + " " + m).collect(joining(","));
    }
}
