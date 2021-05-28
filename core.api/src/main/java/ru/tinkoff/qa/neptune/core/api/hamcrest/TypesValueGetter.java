package ru.tinkoff.qa.neptune.core.api.hamcrest;

import ru.tinkoff.qa.neptune.core.api.steps.parameters.ParameterValueGetter;

import static java.util.Arrays.stream;
import static java.util.stream.Collectors.joining;

public final class TypesValueGetter implements ParameterValueGetter<Class<?>[]> {
    @Override
    public String getParameterValue(Class<?>[] fieldValue) {
        return stream(fieldValue)
                .map(Class::toString)
                .collect(joining("\r\n"));
    }
}
