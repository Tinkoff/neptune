package ru.tinkoff.qa.neptune.spring.data.select.by;

import ru.tinkoff.qa.neptune.core.api.steps.parameters.ParameterValueGetter;

import java.util.stream.Collectors;

import static java.util.Arrays.stream;

public final class IDParameterValueGetter implements ParameterValueGetter<Object[]> {

    @Override
    public String getParameterValue(Object[] fieldValue) {
        return stream(fieldValue)
                .map(String::valueOf)
                .collect(Collectors.joining(", "));
    }
}
