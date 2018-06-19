package com.github.toy.constructor.core.api;

import java.util.ArrayList;
import java.util.List;

import static java.lang.String.format;

public class SPIListLogger implements ReportLogger<Object> {

    static SPIListLogger spiListLogger;

    static final List<String> messages = new ArrayList<>();

    public SPIListLogger() {
        spiListLogger = this;
    }

    @Override
    public void log(Object objectToLog, String message) {
        messages.add(format("%s. Result: %s", message, objectToLog));
    }
}
