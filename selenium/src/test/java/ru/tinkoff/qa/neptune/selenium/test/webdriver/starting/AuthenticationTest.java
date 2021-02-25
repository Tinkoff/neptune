package ru.tinkoff.qa.neptune.selenium.test.webdriver.starting;

import org.hamcrest.Matchers;
import org.testng.annotations.*;
import ru.tinkoff.qa.neptune.selenium.SeleniumParameterProvider;
import ru.tinkoff.qa.neptune.selenium.WrappedWebDriver;
import ru.tinkoff.qa.neptune.selenium.properties.SupportedWebDrivers;
import ru.tinkoff.qa.neptune.selenium.test.capability.suppliers.ChromeSettingsSupplierHeadless;

import java.util.Map;

import static java.util.Map.entry;
import static java.util.Map.ofEntries;
import static java.util.Optional.ofNullable;
import static org.hamcrest.MatcherAssert.assertThat;
import static ru.tinkoff.qa.neptune.selenium.authentication.BrowserCredentials.changeBrowserLogin;
import static ru.tinkoff.qa.neptune.selenium.properties.CapabilityTypes.CHROME;
import static ru.tinkoff.qa.neptune.selenium.properties.SupportedWebDriverProperty.SUPPORTED_WEB_DRIVER_PROPERTY_PROPERTY;
import static ru.tinkoff.qa.neptune.selenium.properties.SupportedWebDrivers.CHROME_DRIVER;
import static ru.tinkoff.qa.neptune.selenium.properties.WebDriverCredentialsProperty.WEB_DRIVER_CREDENTIALS_PROPERTY;

public class AuthenticationTest {

    private final Map<String, String> PROPERTIES_TO_SET_BEFORE =
            ofEntries(entry(SUPPORTED_WEB_DRIVER_PROPERTY_PROPERTY.getName(), CHROME_DRIVER.name()),
                    entry(CHROME.getName(), ChromeSettingsSupplierHeadless.class.getName()));
    private WrappedWebDriver wrappedWebDriver;

    @BeforeClass
    public void setUp() {
        PROPERTIES_TO_SET_BEFORE.forEach(System::setProperty);
    }

    public void getDriver() {
        wrappedWebDriver = new WrappedWebDriver((SupportedWebDrivers)
                new SeleniumParameterProvider().provide().getParameterValues()[0]);
    }

    @BeforeMethod(alwaysRun = true)
    public void beforeMethods() {
        TestBrowserCredentials.hasLoginPerformed = false;
        TestBrowserCredentials.isANewSession = false;
    }

    @AfterMethod(alwaysRun = true)
    public void afterMethods() {
        ofNullable(wrappedWebDriver).ifPresent(WrappedWebDriver::shutDown);
        wrappedWebDriver = null;
        System.getProperties().remove(WEB_DRIVER_CREDENTIALS_PROPERTY.getName());
    }

    @Test(groups = "NO CREDENTIALS")
    public void test1() {
        getDriver();
        wrappedWebDriver.getWrappedDriver();
        assertThat(TestBrowserCredentials.hasLoginPerformed, Matchers.is(false));
    }

    @Test(groups = "NO CREDENTIALS",
            expectedExceptions = UnsupportedOperationException.class,
            expectedExceptionsMessageRegExp = "There is no object that can perform login action in a browser. Please define the 'WEB_DRIVER_CREDENTIALS' property")
    public void test2() {
        changeBrowserLogin("ABC");
    }

    @Test(groups = "NO CREDENTIALS",
            expectedExceptions = IllegalArgumentException.class,
            expectedExceptionsMessageRegExp = "Object of type class ru.tinkoff.qa.neptune.selenium.test.webdriver.starting.TestBrowserCredentials doesn't support credentials of type java.lang.Boolean")
    public void test3() {
        WEB_DRIVER_CREDENTIALS_PROPERTY.accept(TestBrowserCredentials.class);
        changeBrowserLogin(true);
    }

    @Test(dependsOnGroups = "NO CREDENTIALS")
    public void test4() {
        WEB_DRIVER_CREDENTIALS_PROPERTY.accept(TestBrowserCredentials.class);
        changeBrowserLogin("ABC");
        getDriver();
        wrappedWebDriver.getWrappedDriver();
        assertThat(TestBrowserCredentials.hasLoginPerformed, Matchers.is(true));
        assertThat(TestBrowserCredentials.isANewSession, Matchers.is(true));
    }

    @Test(dependsOnGroups = "NO CREDENTIALS", dependsOnMethods = "test4")
    public void test5() {
        WEB_DRIVER_CREDENTIALS_PROPERTY.accept(TestBrowserCredentials.class);
        changeBrowserLogin("ABC");
        getDriver();
        wrappedWebDriver.getWrappedDriver();
        assertThat(TestBrowserCredentials.hasLoginPerformed, Matchers.is(true));
        assertThat(TestBrowserCredentials.isANewSession, Matchers.is(true));

        changeBrowserLogin("ABC");
        wrappedWebDriver.getWrappedDriver();
        assertThat(TestBrowserCredentials.hasLoginPerformed, Matchers.is(false));
    }

    @Test(dependsOnGroups = "NO CREDENTIALS", dependsOnMethods = "test5")
    public void test6() {
        WEB_DRIVER_CREDENTIALS_PROPERTY.accept(TestBrowserCredentials.class);
        changeBrowserLogin("ABC");
        getDriver();
        wrappedWebDriver.getWrappedDriver();
        assertThat(TestBrowserCredentials.hasLoginPerformed, Matchers.is(true));
        assertThat(TestBrowserCredentials.isANewSession, Matchers.is(true));

        changeBrowserLogin("ABCD");
        wrappedWebDriver.getWrappedDriver();
        assertThat(TestBrowserCredentials.hasLoginPerformed, Matchers.is(true));
        assertThat(TestBrowserCredentials.isANewSession, Matchers.is(false));
    }

    @Test(dependsOnGroups = "NO CREDENTIALS", dependsOnMethods = "test5")
    public void test7() {
        WEB_DRIVER_CREDENTIALS_PROPERTY.accept(TestBrowserCredentials.class);
        changeBrowserLogin("ABC");
        getDriver();
        wrappedWebDriver.getWrappedDriver();
        assertThat(TestBrowserCredentials.hasLoginPerformed, Matchers.is(true));
        assertThat(TestBrowserCredentials.isANewSession, Matchers.is(true));

        changeBrowserLogin(null);
        wrappedWebDriver.getWrappedDriver();
        assertThat(TestBrowserCredentials.hasLoginPerformed, Matchers.is(true));
        assertThat(TestBrowserCredentials.isANewSession, Matchers.is(false));
    }

    @AfterClass
    public void tearDown() {
        PROPERTIES_TO_SET_BEFORE.keySet().forEach(s -> System.getProperties().remove(s));
    }
}
