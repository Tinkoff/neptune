package com.github.toy.constructor.core.api.test.proxy;

import com.github.toy.constructor.core.api.proxy.Logger;

import static java.lang.String.format;

public class SPIListLogger extends DefaultListLogger implements Logger {

    private static SPIListLogger spiListLogger;

    public SPIListLogger() {
        spiListLogger = this;
    }

    static SPIListLogger getLogger() {
        return spiListLogger;
    }

    @Override
    public void log(String message) {
        super.log(format("SPI:%s", message));
    }
}
