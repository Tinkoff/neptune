package com.github.toy.constructor.selenium.test;

import com.github.toy.constructor.core.api.Logger;

import static java.lang.String.format;

public class ConsoleLogger implements Logger<Object> {
    @Override
    public void log(Object objectToLog, String message) {
        System.out.println(format("SPI:%s", message));
    }
}
