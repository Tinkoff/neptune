package ru.tinkoff.qa.neptune.kafka.functions.poll;

import ru.tinkoff.qa.neptune.core.api.steps.parameters.ParameterValueGetter;

import static java.lang.String.join;

public class TopicValueGetter implements ParameterValueGetter<String[]> {
    @Override
    public String getParameterValue(String[] fieldValue) {
        return join(",", fieldValue);
    }
}
