package ru.tinkoff.qa.neptune.selenium.test.webdriver.starting;

import org.hamcrest.Matcher;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.remote.Browser;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import ru.tinkoff.qa.neptune.selenium.properties.CapabilityTypes;
import ru.tinkoff.qa.neptune.selenium.properties.SupportedWebDrivers;
import ru.tinkoff.qa.neptune.selenium.test.capability.suppliers.ChromeSettingsSupplierHeadless;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import static java.util.Map.entry;
import static java.util.Map.ofEntries;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.instanceOf;
import static ru.tinkoff.qa.neptune.core.api.hamcrest.iterables.SetOfObjectsConsistsOfMatcher.arrayInOrder;
import static ru.tinkoff.qa.neptune.selenium.properties.CapabilityTypes.CHROME;
import static ru.tinkoff.qa.neptune.selenium.properties.CapabilityTypes.CommonCapabilityProperties.BROWSER_NAME;
import static ru.tinkoff.qa.neptune.selenium.properties.CapabilityTypes.REMOTE;
import static ru.tinkoff.qa.neptune.selenium.properties.SupportedWebDriverProperty.SUPPORTED_WEB_DRIVER_PROPERTY_PROPERTY;
import static ru.tinkoff.qa.neptune.selenium.properties.SupportedWebDrivers.CHROME_DRIVER;
import static ru.tinkoff.qa.neptune.selenium.properties.SupportedWebDrivers.REMOTE_DRIVER;
import static ru.tinkoff.qa.neptune.selenium.properties.URLProperties.REMOTE_WEB_DRIVER_URL_PROPERTY;

public class GetSupportedWebDriversTest {
    private Map<String, String> properties;

    @DataProvider
    public Object[][] testData() throws MalformedURLException {
        return new Object[][]{
                {CHROME_DRIVER, CHROME, arrayInOrder(instanceOf(Capabilities.class))},
                {REMOTE_DRIVER, REMOTE, arrayInOrder(equalTo(new URL("https://github.com/")), instanceOf(Capabilities.class))}
        };
    }

    @Test(expectedExceptions = IllegalStateException.class)
    public void urlNotDefined() {
        properties = new HashMap<>(ofEntries(
                entry(SUPPORTED_WEB_DRIVER_PROPERTY_PROPERTY.getName(), REMOTE_DRIVER.name()),
                entry(BROWSER_NAME.getName(), Browser.CHROME.browserName()),
                entry(REMOTE.getName(), ChromeSettingsSupplierHeadless.class.getName())));
        properties.forEach(System::setProperty);

        REMOTE_WEB_DRIVER_URL_PROPERTY.accept(null);

        try {
            REMOTE_DRIVER.get();
        } catch (Exception e) {
            throw e;
        }
    }

    @Test(dataProvider = "testData")
    public void urlDefined(SupportedWebDrivers supportedWebDrivers, CapabilityTypes capabilityTypes, Matcher matcher) {
        properties = new HashMap<>(ofEntries(
                entry(SUPPORTED_WEB_DRIVER_PROPERTY_PROPERTY.getName(), supportedWebDrivers.name()),
                entry(BROWSER_NAME.getName(), Browser.CHROME.browserName()),
                entry(capabilityTypes.getName(), ChromeSettingsSupplierHeadless.class.getName())));
        properties.forEach(System::setProperty);

        try {
            REMOTE_WEB_DRIVER_URL_PROPERTY.accept(new URL("https://github.com/"));

            assertThat(supportedWebDrivers.get(), matcher);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    @AfterMethod
    public void remote() {
        properties.keySet().forEach(s -> System.getProperties().remove(s));
    }
}
