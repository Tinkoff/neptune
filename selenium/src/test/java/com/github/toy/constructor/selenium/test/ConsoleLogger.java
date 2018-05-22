package com.github.toy.constructor.selenium.test;

import com.github.toy.constructor.core.api.proxy.Logger;

import static java.lang.String.format;

public class ConsoleLogger implements Logger {

    @Override
    public void log(String message) {
        System.out.println(format("SPI:%s", message));
    }
}
