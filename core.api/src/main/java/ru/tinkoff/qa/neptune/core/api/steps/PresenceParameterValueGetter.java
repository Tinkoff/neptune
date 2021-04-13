package ru.tinkoff.qa.neptune.core.api.steps;

import ru.tinkoff.qa.neptune.core.api.steps.parameters.ParameterValueGetter;

import java.util.function.Function;

import static ru.tinkoff.qa.neptune.core.api.utils.IsLoggableUtil.isLoggable;

public class PresenceParameterValueGetter implements ParameterValueGetter<Function<?, ?>> {

    @Override
    public String getParameterValue(Function<?, ?> fieldValue) {
        if (StepFunction.class.isAssignableFrom(fieldValue.getClass())) {
            return fieldValue.toString();
        } else {
            return isLoggable(fieldValue) ?
                    fieldValue.toString() :
                    "<not described value>";
        }
    }
}
