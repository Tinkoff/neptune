package ru.tinkoff.qa.neptune.selenium.functions.intreraction;

import org.openqa.selenium.Keys;
import ru.tinkoff.qa.neptune.core.api.steps.parameters.StepParameter;

public final class CharSequenceParameterValueGetter implements StepParameter.ParameterValueGetter<CharSequence> {

    @Override
    public String getParameterValue(CharSequence charSequence) {
        if (charSequence.getClass().equals(Keys.class)) {
            return ((Keys) charSequence).name();
        } else {
            return String.valueOf(charSequence);
        }
    }
}
