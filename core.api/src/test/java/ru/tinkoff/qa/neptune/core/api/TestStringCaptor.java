package ru.tinkoff.qa.neptune.core.api;

import ru.tinkoff.qa.neptune.core.api.event.firing.captors.StringCaptor;

public class TestStringCaptor extends StringCaptor<Object> {

    public TestStringCaptor() {
        super();
    }

    @Override
    protected StringBuilder getData(Object caught) {
        return new StringBuilder(caught.toString());
    }

    @Override
    public Class<Object> getTypeToBeCaptured() {
        return Object.class;
    }
}
