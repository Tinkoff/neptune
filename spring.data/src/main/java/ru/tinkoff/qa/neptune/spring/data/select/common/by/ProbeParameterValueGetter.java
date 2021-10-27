package ru.tinkoff.qa.neptune.spring.data.select.common.by;

import ru.tinkoff.qa.neptune.core.api.steps.parameters.ParameterValueGetter;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;
import static ru.tinkoff.qa.neptune.spring.data.data.serializer.DataSerializer.serializeObject;

public final class ProbeParameterValueGetter implements ParameterValueGetter<Object> {

    @Override
    public String getParameterValue(Object fieldValue) {
        return serializeObject(NON_NULL, fieldValue);
    }
}
