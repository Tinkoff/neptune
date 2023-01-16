package ru.tinkoff.qa.neptune.kafka.captors;

import org.apache.commons.lang3.ArrayUtils;
import ru.tinkoff.qa.neptune.core.api.event.firing.captors.StringCaptor;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;

import static java.util.Objects.isNull;

@Description("Received data")
public class ReceivedArrayCaptor extends StringCaptor<Object> implements KafkaValueCaptor {

    @Override
    public Object getCaptured(Object toBeCaptured) {
        if (isNull(toBeCaptured) || !toBeCaptured.getClass().isArray()) {
            return null;
        }

        if (ArrayUtils.getLength(toBeCaptured) == 0) {
            return null;
        }

        return toBeCaptured;
    }

    @Override
    public StringBuilder getData(Object caught) {
        return KafkaValueCaptor.super.getData(caught);
    }
}
