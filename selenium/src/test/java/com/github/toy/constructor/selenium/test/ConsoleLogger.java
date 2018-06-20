package com.github.toy.constructor.selenium.test;

import com.github.toy.constructor.core.api.Captor;

import static java.lang.String.format;

public class ConsoleLogger implements Captor<Object> {
    @Override
    public void doCapture(Object caught, String message) {
        System.out.println(format("SPI:%s", message));
    }
}
