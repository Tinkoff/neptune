package com.github.toy.constructor.check.test;

import com.github.toy.constructor.core.api.Captor;

import java.util.ArrayList;
import java.util.List;

public class DefaultListLogger implements Captor {

    static final List<String> messages = new ArrayList<>();

    @Override
    public void doCapture(Object caught, String message) {
        System.out.println(message);
        messages.add(message);
    }
}
