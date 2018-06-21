package com.github.toy.constructor.core.api;

import com.github.toy.constructor.core.api.captors.CapturedStringInjector;

import java.util.ArrayList;
import java.util.List;

import static java.lang.String.format;

public class TestCapturedStringInjector implements CapturedStringInjector {

    static final List<String> messages = new ArrayList<>();

    @Override
    public void inject(StringBuilder toBeInjected, String message) {
        String msg = format("%s. Result: %s", message, toBeInjected);
        messages.add(msg);
    }
}
