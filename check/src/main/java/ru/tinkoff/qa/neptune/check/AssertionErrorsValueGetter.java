package ru.tinkoff.qa.neptune.check;

import ru.tinkoff.qa.neptune.core.api.steps.parameters.ParameterValueGetter;

import java.util.Collection;

import static java.util.stream.Collectors.joining;

public class AssertionErrorsValueGetter implements ParameterValueGetter<Collection<AssertionError>> {

    private static final String LINE_SEPARATOR = "\r\n";

    @Override
    public String getParameterValue(Collection<AssertionError> fieldValue) {
        return fieldValue.stream()
                .map(Throwable::getMessage)
                .collect(joining(LINE_SEPARATOR + LINE_SEPARATOR));
    }
}
