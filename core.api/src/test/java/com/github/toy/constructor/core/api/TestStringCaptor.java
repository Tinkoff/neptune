package com.github.toy.constructor.core.api;

import com.github.toy.constructor.core.api.captors.StringCaptor;

public class TestStringCaptor extends StringCaptor<Object> {

    public TestStringCaptor() {
        super();
    }

    @Override
    protected StringBuilder getData(Object caught) {
        return new StringBuilder(caught.toString());
    }

    @Override
    protected Class<Object> getTypeToBeCaptured() {
        return Object.class;
    }
}
