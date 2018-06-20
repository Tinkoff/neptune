package com.github.toy.constructor.core.api;

import java.util.ArrayList;
import java.util.List;

import static java.lang.String.format;

public class TestCaptor implements Captor<Object> {

    static TestCaptor spiListLogger;

    static final List<String> messages = new ArrayList<>();

    public TestCaptor() {
        spiListLogger = this;
    }

    @Override
    public void doCapture(Object caught, String message) {
        messages.add(format("%s. Result: %s", message, caught));
    }
}
