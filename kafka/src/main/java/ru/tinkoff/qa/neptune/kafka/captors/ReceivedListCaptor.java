package ru.tinkoff.qa.neptune.kafka.captors;

import ru.tinkoff.qa.neptune.core.api.event.firing.captors.StringCaptor;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;

import java.util.Collection;

import static java.util.Objects.isNull;

@Description("Received data")
public class ReceivedListCaptor extends StringCaptor<Object> implements KafkaValueCaptor {

    @Override
    public Object getCaptured(Object toBeCaptured) {
        if (isNull(toBeCaptured) || !(toBeCaptured instanceof Collection)) {
            return null;
        }

        if (((Collection<?>) toBeCaptured).isEmpty()) {
            return null;
        }

        return toBeCaptured;
    }

    @Override
    public StringBuilder getData(Object caught) {
        return KafkaValueCaptor.super.getData(caught);
    }
}
