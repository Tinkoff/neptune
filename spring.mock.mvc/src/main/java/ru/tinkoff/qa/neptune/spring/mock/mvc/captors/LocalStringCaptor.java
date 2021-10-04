package ru.tinkoff.qa.neptune.spring.mock.mvc.captors;

import ru.tinkoff.qa.neptune.core.api.event.firing.captors.StringCaptor;

abstract class LocalStringCaptor extends StringCaptor<String> {

    @Override
    public String getCaptured(Object toBeCaptured) {
        if (toBeCaptured == null) {
            return null;
        }

        return toBeCaptured.toString();
    }

    @Override
    public StringBuilder getData(String caught) {
        return new StringBuilder(caught);
    }
}
