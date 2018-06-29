package com.github.toy.constructor.check.test;

import com.github.toy.constructor.core.api.Captor;

import java.util.ArrayList;
import java.util.List;

import static java.lang.String.format;

public class DefaultListLogger extends Captor<Object, String> {

    static final List<String> messages = new ArrayList<>();

    public DefaultListLogger() {
        super(List.of((toBeInjected, message) -> {
            System.out.println(message);
            messages.add(message);
        }));
    }

    @Override
    protected String getData(Object caught) {
        return caught.toString();
    }

    @Override
    protected Class<Object> getTypeToBeCaptured() {
        return Object.class;
    }
}
