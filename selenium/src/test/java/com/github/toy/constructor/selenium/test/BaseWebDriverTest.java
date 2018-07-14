package com.github.toy.constructor.selenium.test;

import com.github.toy.constructor.selenium.SeleniumSteps;
import com.github.toy.constructor.selenium.WebDriverMethodInterceptor;
import com.github.toy.constructor.selenium.WrappedWebDriver;
import net.sf.cglib.proxy.Enhancer;
import org.mockito.Mock;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;

import java.time.Duration;

import static com.github.toy.constructor.selenium.properties.SupportedWebDrivers.CHROME_DRIVER;
import static java.time.Duration.ofMillis;
import static java.time.Duration.ofSeconds;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public abstract class BaseWebDriverTest {

    protected static final Duration FIVE_SECONDS = ofSeconds(5);
    protected static final Duration ONE_SECOND = ofSeconds(1);
    protected static final Duration HALF_SECOND = ofMillis(500);

    @Mock
    private WrappedWebDriver wrappedWebDriver;

    protected SeleniumSteps seleniumSteps;


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
    public void beforeTestMethod() {
        start = 0;
        end = 0;

        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(MockWebDriver.class);
        enhancer.setCallback(new WebDriverMethodInterceptor());
        WebDriver driver = (WebDriver) enhancer.create();
        when(wrappedWebDriver.getWrappedDriver()).thenReturn(driver);
        seleniumSteps = new SeleniumSteps(CHROME_DRIVER) {
            @Override
            public WebDriver getWrappedDriver() {
                return wrappedWebDriver.getWrappedDriver();
            }
        };
    }
}
