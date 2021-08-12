package ru.tinkoff.qa.neptune.kafka.functions.send;

import ru.tinkoff.qa.neptune.core.api.steps.parameters.ParameterValueGetter;

import static ru.tinkoff.qa.neptune.kafka.properties.KafkaDefaultDataTransformer.KAFKA_DEFAULT_DATA_TRANSFORMER;

public class ObjectParameterValueGetter implements ParameterValueGetter<Object> {
    @Override
    public String getParameterValue(Object fieldValue) {
        return KAFKA_DEFAULT_DATA_TRANSFORMER.get().serialize(fieldValue);
    }
}
