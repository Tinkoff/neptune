package ru.tinkoff.qa.neptune.core.api.hamcrest;

import ru.tinkoff.qa.neptune.core.api.steps.parameters.ParameterValueGetter;

import static java.util.Arrays.stream;

public final class ThrowableValueGetter implements ParameterValueGetter<Throwable> {

    @Override
    public String getParameterValue(Throwable fieldValue) {
        var builder = new StringBuilder(fieldValue.getMessage()).append(":");
        stream(fieldValue.getStackTrace())
                .forEach(st -> builder.append("\r\n").append(st.toString()));
        return builder.toString();
    }
}
