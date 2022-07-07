package ru.tinkoff.qa.neptune.selenium.test.webdriver.starting;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;
import ru.tinkoff.qa.neptune.selenium.test.capability.suppliers.ChromeSettingsSupplierHeadless;

import java.net.URL;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anyOf;
import static org.hamcrest.Matchers.is;
import static ru.tinkoff.qa.neptune.selenium.SeleniumStepContext.inBrowser;
import static ru.tinkoff.qa.neptune.selenium.properties.CapabilityTypes.CHROME;
import static ru.tinkoff.qa.neptune.selenium.properties.SupportedWebDriverProperty.SUPPORTED_WEB_DRIVER_PROPERTY_PROPERTY;
import static ru.tinkoff.qa.neptune.selenium.properties.SupportedWebDrivers.CHROME_DRIVER;
import static ru.tinkoff.qa.neptune.selenium.properties.URLProperties.BASE_WEB_DRIVER_URL_PROPERTY;

@SuppressWarnings("unchecked")
public class SanityTestOfTheStartingAndStoppingOfWebDriverTest {

    private static boolean isDriverAlive(WebDriver driver) {
        try {
            driver.getCurrentUrl();
            return true;
        } catch (WebDriverException e) {
            return false;
        }
    }

    @Test()
    public void testOfTheStarting() {
        SUPPORTED_WEB_DRIVER_PROPERTY_PROPERTY.accept(CHROME_DRIVER);
        CHROME.accept(new Class[]{ChromeSettingsSupplierHeadless.class});

        var driver = inBrowser().getWrappedDriver();
        assertThat("Check class of web driver", ChromeDriver.class.isAssignableFrom(driver.getClass()),
            is(true));
        assertThat("Web driver is alive", isDriverAlive(driver), is(true));
        assertThat("Current url", driver.getCurrentUrl(), anyOf(is("about:blank"), is("data:,")));
    }

    @Test
    public void startSessionWithBaseURL() throws Exception {
        SUPPORTED_WEB_DRIVER_PROPERTY_PROPERTY.accept(CHROME_DRIVER);
        CHROME.accept(new Class[]{ChromeSettingsSupplierHeadless.class});
        BASE_WEB_DRIVER_URL_PROPERTY.accept(new URL("https://github.com/"));

        assertThat("Current url",
            inBrowser().getWrappedDriver().getCurrentUrl(),
            is("https://github.com/"));
    }

    @AfterMethod
    public void afterMethod() {
        inBrowser().stop();
        SUPPORTED_WEB_DRIVER_PROPERTY_PROPERTY.accept(null);
        CHROME.accept(null);
        BASE_WEB_DRIVER_URL_PROPERTY.accept(null);
    }
}
