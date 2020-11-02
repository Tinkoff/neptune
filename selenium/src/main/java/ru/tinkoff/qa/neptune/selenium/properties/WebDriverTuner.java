package ru.tinkoff.qa.neptune.selenium.properties;

import org.openqa.selenium.WebDriver;
import ru.tinkoff.qa.neptune.selenium.SeleniumStepContext;

/**
 * Designed to provide an ability to apply some customer settings on a new session of {@link WebDriver}
 * and every time after invocation of {@link SeleniumStepContext#refresh()}
 */
public interface WebDriverTuner {

    /**
     * Applies customer settings on a new session of {@link WebDriver} and/or every time after invocation
     * of {@link SeleniumStepContext#refresh()}
     *
     * @param webDriver    an instance of {@link WebDriver} to apply settings
     * @param isNewSession is current session newly created or not
     */
    void tuneDriver(WebDriver webDriver, boolean isNewSession);
}
