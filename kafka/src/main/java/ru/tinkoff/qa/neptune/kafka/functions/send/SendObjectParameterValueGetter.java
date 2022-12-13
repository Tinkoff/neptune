package ru.tinkoff.qa.neptune.kafka.functions.send;

import com.google.gson.Gson;
import ru.tinkoff.qa.neptune.core.api.steps.parameters.ParameterValueGetter;

public class SendObjectParameterValueGetter implements ParameterValueGetter<Object> {

    @Override
    public String getParameterValue(Object fieldValue) {
        return new Gson().toJson(fieldValue);
    }
}
