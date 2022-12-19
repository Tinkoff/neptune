package ru.tinkoff.qa.neptune.kafka.captors;

import ru.tinkoff.qa.neptune.core.api.event.firing.captors.StringCaptor;

abstract class ObjectCaptor extends StringCaptor<Object> implements KafkaValueCaptor {

    @Override
    public Object getCaptured(Object toBeCaptured) {
        return toBeCaptured;
    }

    @Override
    public StringBuilder getData(Object caught) {
        return KafkaValueCaptor.super.getData(caught);
    }
}
