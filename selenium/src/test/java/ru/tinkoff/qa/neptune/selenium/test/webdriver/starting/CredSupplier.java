package ru.tinkoff.qa.neptune.selenium.test.webdriver.starting;

import java.util.function.Supplier;

import static java.util.Optional.ofNullable;

public class CredSupplier implements Supplier<TestBrowserCredentials> {

    private static TestBrowserCredentials TEST_BROWSER_CREDENTIALS;

    @Override
    public TestBrowserCredentials get() {
        return ofNullable(TEST_BROWSER_CREDENTIALS).orElseGet(() -> {
            TEST_BROWSER_CREDENTIALS = new TestBrowserCredentials();
            return TEST_BROWSER_CREDENTIALS;
        });
    }
}
