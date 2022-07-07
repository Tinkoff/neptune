package ru.tinkoff.qa.neptune.selenium.test.browser.proxy.step;

import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import ru.tinkoff.qa.neptune.selenium.SeleniumStepContext;
import ru.tinkoff.qa.neptune.selenium.functions.browser.proxy.HttpTraffic;
import ru.tinkoff.qa.neptune.selenium.test.capability.suppliers.ChromeSettingsSupplierForProxy;

import java.net.URL;
import java.util.List;

import static io.netty.handler.codec.http.HttpMethod.GET;
import static java.time.Duration.ofSeconds;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static ru.tinkoff.qa.neptune.core.api.hamcrest.iterables.SetOfObjectsEachItemMatcher.eachOfIterable;
import static ru.tinkoff.qa.neptune.selenium.functions.browser.proxy.BrowserProxyGetStepSupplier.proxiedRequests;
import static ru.tinkoff.qa.neptune.selenium.hamcrest.matchers.browser.proxy.RequestHasMethod.requestHasMethod;
import static ru.tinkoff.qa.neptune.selenium.hamcrest.matchers.browser.proxy.RequestHasUrl.requestHasStringUrl;
import static ru.tinkoff.qa.neptune.selenium.hamcrest.matchers.browser.proxy.ResponseHasStatusCode.responseHasStatusCode;
import static ru.tinkoff.qa.neptune.selenium.properties.CapabilityTypes.CHROME;
import static ru.tinkoff.qa.neptune.selenium.properties.SupportedWebDriverProperty.SUPPORTED_WEB_DRIVER_PROPERTY_PROPERTY;
import static ru.tinkoff.qa.neptune.selenium.properties.SupportedWebDrivers.CHROME_DRIVER;
import static ru.tinkoff.qa.neptune.selenium.properties.URLProperties.BASE_WEB_DRIVER_URL_PROPERTY;

@SuppressWarnings("unchecked")
public class BrowserProxyStepTest {

    private SeleniumStepContext seleniumSteps;

    @BeforeMethod
    public void setUp() throws Exception {
        SUPPORTED_WEB_DRIVER_PROPERTY_PROPERTY.accept(CHROME_DRIVER);
        BASE_WEB_DRIVER_URL_PROPERTY.accept(new URL("https://www.google.com"));
        CHROME.accept(new Class[]{ChromeSettingsSupplierForProxy.class});
        seleniumSteps = new SeleniumStepContext();
    }

    @Test
    public void proxiedCaptureTest() {
        seleniumSteps.enableAndRefreshNetwork();
        seleniumSteps.navigateTo("https://www.google.com");
        List<HttpTraffic> requests = seleniumSteps.get(proxiedRequests());

        assertThat("Captured requests size is greater than 0", requests, hasSize(greaterThan(0)));
    }

    @Test
    public void proxyGetStepSupplierCriteriaTest() {
        seleniumSteps.enableAndRefreshNetwork();
        seleniumSteps
            .navigateTo("https://github.com")
            .enableAndRefreshNetwork();

        List<HttpTraffic> requests = seleniumSteps.navigateTo("/")
            .get(proxiedRequests()
                .recordedRequestMethod(GET)
                .recordedResponseStatusCode(200)
                .recordedRequestUrlMatches("https://www.google.com")
                .timeOut(ofSeconds(10)));

        assertThat("Proxy with filter captured only one request", requests, hasSize(greaterThanOrEqualTo(1)));
        assertThat("Captured entries have GET HTTP, status code 200 and same url", requests,
            eachOfIterable(requestHasMethod(GET),
                responseHasStatusCode(200),
                requestHasStringUrl(containsString("https://www.google.com")))
        );
    }

    @Test
    public void checkDoubleCall() {
        seleniumSteps.enableAndRefreshNetwork();

        List<HttpTraffic> requests1 = seleniumSteps.navigateTo("/")
            .get(proxiedRequests()
                .recordedRequestMethod(GET)
                .recordedResponseStatusCode(200)
                .recordedRequestUrlMatches("https://www.google.com/complete")
                .timeOut(ofSeconds(10)));

        List<HttpTraffic> requests2 = seleniumSteps
            .get(proxiedRequests()
                .recordedRequestMethod(GET)
                .recordedResponseStatusCode(200)
                .recordedRequestUrlMatches("https://www.google.com/complete")
                .timeOut(ofSeconds(10)));


        assertThat("Сhecking the number of records", requests1, containsInAnyOrder(requests2.toArray()));
    }

    @Test
    public void closedWindowBeforeReceivingRequests() {
        seleniumSteps.enableAndRefreshNetwork();
        seleniumSteps.navigateTo("https://www.google.com");
        seleniumSteps.closeWindow();
        List<HttpTraffic> requests = seleniumSteps.get(proxiedRequests());

        assertThat("Proxy captured requests", requests, hasSize(greaterThan(0)));
    }

    @Test
    public void closedWindowBeforeRefreshContext() {
        seleniumSteps.enableAndRefreshNetwork();
        seleniumSteps.navigateTo("https://www.google.com");
        seleniumSteps.closeWindow();
        seleniumSteps.refreshContext();

        assertThat("fake assert", true, is(true));
    }

    @AfterMethod(alwaysRun = true)
    public void tearDownDriver() {
        seleniumSteps.stop();
    }

    @AfterClass(alwaysRun = true)
    public void tearDownProperties() {
        SUPPORTED_WEB_DRIVER_PROPERTY_PROPERTY.accept(null);
        BASE_WEB_DRIVER_URL_PROPERTY.accept(null);
        CHROME.accept(null);
    }
}
