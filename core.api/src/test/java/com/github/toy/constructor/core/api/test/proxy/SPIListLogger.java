package com.github.toy.constructor.core.api.test.proxy;

import static java.lang.String.format;

public class SPIListLogger extends DefaultListLogger {

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
