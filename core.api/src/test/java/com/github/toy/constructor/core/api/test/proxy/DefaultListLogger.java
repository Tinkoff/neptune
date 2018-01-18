package com.github.toy.constructor.core.api.test.proxy;

import com.github.toy.constructor.core.api.proxy.Logger;

import java.util.ArrayList;
import java.util.List;

public class DefaultListLogger implements Logger {

    final List<String> messages = new ArrayList<>();

    @Override
    public void log(String message) {
        System.out.println(message);
        messages.add(message);
    }
}
