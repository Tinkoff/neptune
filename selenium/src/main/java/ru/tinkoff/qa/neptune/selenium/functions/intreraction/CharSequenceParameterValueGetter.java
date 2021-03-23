package ru.tinkoff.qa.neptune.selenium.functions.intreraction;

import org.openqa.selenium.Keys;
import ru.tinkoff.qa.neptune.core.api.steps.parameters.ParameterValueGetter;

public final class CharSequenceParameterValueGetter implements ParameterValueGetter<CharSequence> {

    @Override
    public String getParameterValue(CharSequence charSequence) {
        if (charSequence.getClass().equals(Keys.class)) {
            return ((Keys) charSequence).name();
        } else {
            return String.valueOf(charSequence);
        }
    }
}
