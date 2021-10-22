package ru.tinkoff.qa.neptune.spring.data.select.by;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import ru.tinkoff.qa.neptune.core.api.steps.parameters.ParameterValueGetter;

import java.text.SimpleDateFormat;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;
import static java.lang.String.valueOf;
import static ru.tinkoff.qa.neptune.core.api.utils.IsLoggableUtil.isLoggable;

public final class ProbeParameterValueGetter implements ParameterValueGetter<Object> {

    @Override
    public String getParameterValue(Object fieldValue) {
        if (isLoggable(fieldValue)) {
            return valueOf(fieldValue);
        }

        try {
            return new ObjectMapper()
                    .setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:S"))
                    .setSerializationInclusion(NON_NULL)
                    .writeValueAsString(fieldValue);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return "could not serialize value";
        }
    }
}
