package ru.tinkoff.qa.neptune.selenium.test.steps.tests.target.locator;

import org.mockito.Mock;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import ru.tinkoff.qa.neptune.selenium.SeleniumStepContext;
import ru.tinkoff.qa.neptune.selenium.WrappedWebDriver;

import java.util.HashSet;

import static java.util.Arrays.asList;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;
import static org.openqa.selenium.WindowType.WINDOW;
import static ru.tinkoff.qa.neptune.selenium.functions.target.locator.window.GetNewWindowSupplier.newWindow;
import static ru.tinkoff.qa.neptune.selenium.properties.SupportedWebDrivers.CHROME_DRIVER;


public class NewWindowTest {
    @Mock
    protected WrappedWebDriver wrappedWebDriver;
    @Mock
    protected WebDriver driver;
    @Mock
    protected WebDriver driver2;
    @Mock
    protected WebDriver.TargetLocator targetLocator;
    @Mock
    protected WebDriver.TargetLocator targetLocator2;

    protected SeleniumStepContext seleniumSteps;

    @BeforeClass
    public void setUpBeforeClass() {
        openMocks(this);
    }

    @BeforeMethod
    public void beforeTestMethod() {
        when(wrappedWebDriver.getWrappedDriver()).thenReturn(driver);
        when(driver.getWindowHandle()).thenReturn("origin_handle");
        when(driver.switchTo()).thenReturn(targetLocator);

        when(targetLocator.newWindow(WINDOW)).thenReturn(driver2);
        when(driver2.getWindowHandle()).thenReturn("new_handle", "origin_handle");
        when(driver2.getWindowHandles()).thenReturn(new HashSet<>(asList("new_handle", "origin_handle")));

        when(driver2.switchTo()).thenReturn(targetLocator2);
        when(targetLocator2.window("origin_handle")).thenReturn(driver);

        seleniumSteps = new SeleniumStepContext(CHROME_DRIVER) {
            @Override
            public WebDriver getWrappedDriver() {
                return wrappedWebDriver.getWrappedDriver();
            }
        };
    }

    @Test
    public void checkTheOpeningOfANewWindow() {
        seleniumSteps.get(newWindow());

        verify(targetLocator).newWindow(WINDOW);
    }
}
