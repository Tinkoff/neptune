package ru.tinkoff.qa.neptune.core.api.steps.parameters;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Default abstract implementation of {@link StepParameterPojo}
 */
public abstract class StepParameterWrapper implements StepParameterPojo {

    @Override
    public String toString() {
        var map = getParameters();
        try {
            return new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(map);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
