package ru.tinkoff.qa.neptune.selenium.content.management;

import ru.tinkoff.qa.neptune.selenium.SeleniumStepContext;

/**
 * This enum describes how often default content management strategy
 * should be used.
 */
public enum BrowserContentUsage {
    /**
     * Default content management strategy should be applied once for a class on first invocation
     * of {@link SeleniumStepContext#inBrowser()}
     */
    ONCE,
    /**
     * Default content management strategy should be applied once for each test-method that invokes
     * {@link SeleniumStepContext#inBrowser()}
     */
    FOR_EVERY_TEST_METHOD,
    /**
     * Default content management strategy should be applied once for each test-method or fixture/configuration method
     * that invokes {@link SeleniumStepContext#inBrowser()}
     */
    FOR_EVERY_METHOD;
}
