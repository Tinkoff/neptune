package ru.tinkoff.qa.neptune.selenium;

import org.mockito.Mock;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import ru.tinkoff.qa.neptune.selenium.test.MockWebDriver;

import java.time.Duration;

import static java.time.Duration.ofSeconds;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

public abstract class BaseWebDriverPreparations {

    protected static final Duration FIVE_SECONDS = ofSeconds(5);
    protected static final Duration ONE_SECOND = ofSeconds(1);

    @Mock
    protected WrappedWebDriver wrappedWebDriver;
    protected SeleniumStepContext seleniumSteps;


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
    public void setUpBeforeClass() {
        openMocks(this);
    }

    @BeforeMethod
    public void beforeTestMethod() {
        start = 0;
        end = 0;

        when(wrappedWebDriver.getWrappedDriver()).thenReturn(new MockWebDriver());
        seleniumSteps = new SeleniumStepContext() {

            @Override
            public WebDriver getWrappedDriver() {
                return wrappedWebDriver.getWrappedDriver();
            }

            @Override
            void refreshDriverIfNecessary() {
                //does nothing
            }
        };
    }
}
