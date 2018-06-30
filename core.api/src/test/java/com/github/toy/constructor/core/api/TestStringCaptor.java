package com.github.toy.constructor.core.api;

import com.github.toy.constructor.core.api.event.firing.captors.StringCaptor;

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
