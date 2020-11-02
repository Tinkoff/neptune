package ru.tinkoff.qa.neptune.selenium.test.webdriver.starting;

import java.util.function.Supplier;

public class TestWebDriverTunerSupplier implements Supplier<TestWebDriverTuner> {

    static final TestWebDriverTuner TEST_WEB_DRIVER_TUNER = new TestWebDriverTuner();

    @Override
    public TestWebDriverTuner get() {
        return TEST_WEB_DRIVER_TUNER;
    }
}
