package ru.tinkoff.qa.neptune.core.api;

import ru.tinkoff.qa.neptune.core.api.event.firing.captors.StringCaptor;

public class TestStringCaptor extends StringCaptor<Object> {

    public TestStringCaptor() {
        super("Saved to string");
    }

    @Override
    public StringBuilder getData(Object caught) {
        return new StringBuilder(caught.toString());
    }

    @Override
    public Object getCaptured(Object toBeCaptured) {
        return toBeCaptured;
    }

}
