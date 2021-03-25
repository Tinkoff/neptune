package ru.tinkoff.qa.neptune.selenium.functions.edit;

import ru.tinkoff.qa.neptune.core.api.steps.parameters.ParameterValueGetter;

import java.util.Arrays;
import java.util.stream.Stream;

import static com.google.common.base.Preconditions.checkArgument;
import static java.util.List.of;
import static java.util.Objects.nonNull;
import static java.util.stream.Collectors.joining;
import static java.util.stream.StreamSupport.stream;

public final class EditParameterValueGetter implements ParameterValueGetter<Object> {

    @Override
    public String getParameterValue(Object fieldValue) {
        checkArgument(nonNull(fieldValue), "The value to be used for the editing should not be null");
        var clazz = fieldValue.getClass();

        Stream<?> stream;
        if (Iterable.class.isAssignableFrom(clazz)) {
            stream = stream(((Iterable<?>) fieldValue).spliterator(), false);
        } else if (clazz.isArray()) {
            stream = Arrays.stream((Object[]) fieldValue);
        } else {
            stream = of(fieldValue).stream();
        }

        return stream.map(t -> {
            checkArgument(nonNull(t), "A null-value is defined to change value of an element");
            if (t.getClass().isEnum()) {
                return ((Enum<?>) t).name();
            }
            return String.valueOf(t);
        }).collect(joining(","));
    }
}
