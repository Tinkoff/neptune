package com.github.toy.constructor.selenium.test;

import com.github.toy.constructor.selenium.SeleniumSteps;
import com.github.toy.constructor.selenium.WrappedWebDriver;
import com.github.toy.constructor.selenium.test.MockWebDriver;
import org.mockito.Mock;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;

import java.time.Duration;

import static com.github.toy.constructor.core.api.proxy.ConstructorParameters.params;
import static com.github.toy.constructor.core.api.proxy.Substitution.getSubstituted;
import static java.time.Duration.ofMillis;
import static java.time.Duration.ofSeconds;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public abstract class BaseWebDriverTest {

    protected static final Duration FIVE_SECONDS = ofSeconds(5);
    protected static final Duration ONE_SECOND = ofSeconds(1);
    protected static final Duration HALF_SECOND = ofMillis(500);

    private WebDriver driver;
    protected SeleniumSteps seleniumSteps;

    @Mock
    protected WrappedWebDriver wrappedWebDriver;

    private long start;
    private long end;

    protected static void setProperty(String property, String value) {
        System.setProperty(property, value);
    }

    protected static void removeProperty(String property) {
        System.getProperties().remove(property);
    }

    protected void setStartBenchMark() {
        start = System.currentTimeMillis();
    }

    protected void setEndBenchMark() {
        end = System.currentTimeMillis();
    }

    protected long getTimeDifference() {
        return end - start;
    }

    @BeforeClass
    public void setUpBeforeSuite() {
        initMocks(this);
    }

    @BeforeMethod
    public void beforeTestMethod() throws Exception {
        start = 0;
        end = 0;
        driver = new MockWebDriver();
        when(wrappedWebDriver.getWrappedDriver()).thenReturn(driver);
        seleniumSteps = getSubstituted(SeleniumSteps.class, params(wrappedWebDriver));
    }
}
