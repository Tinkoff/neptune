package com.github.toy.constructor.check.test;

import com.github.toy.constructor.core.api.Logger;

import java.util.ArrayList;
import java.util.List;

public class DefaultListLogger implements Logger {

    static final List<String> messages = new ArrayList<>();

    @Override
    public void log(Object objectToLog, String message) {
        System.out.println(message);
        messages.add(message);
    }
}
