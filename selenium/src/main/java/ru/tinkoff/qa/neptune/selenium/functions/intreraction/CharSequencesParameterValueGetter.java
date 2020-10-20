package ru.tinkoff.qa.neptune.selenium.functions.intreraction;

import org.openqa.selenium.Keys;
import ru.tinkoff.qa.neptune.core.api.steps.parameters.StepParameter;

import static java.util.Arrays.stream;
import static java.util.stream.Collectors.joining;

public final class CharSequencesParameterValueGetter implements StepParameter.ParameterValueGetter<CharSequence[]> {

    @Override
    public String getParameterValue(CharSequence[] keys) {
        return stream(keys).map(charSequence -> {
            if (charSequence.getClass().equals(Keys.class)) {
                return ((Keys) charSequence).name();
            } else {
                return String.valueOf(charSequence);
            }
        }).collect(joining(","));
    }
}
