package ru.tinkoff.qa.neptune.selenium.test.steps.tests.cookies;

import org.openqa.selenium.Cookie;
import org.testng.annotations.Test;
import ru.tinkoff.qa.neptune.selenium.test.BaseWebDriverTest;

import java.util.Date;
import java.util.HashSet;

import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.core.IsEqual.equalTo;
import static ru.tinkoff.qa.neptune.selenium.functions.cookies.CommonBrowserCookieCriteria.cookieDomain;
import static ru.tinkoff.qa.neptune.selenium.functions.cookies.CommonBrowserCookieCriteria.cookieIsSecure;
import static ru.tinkoff.qa.neptune.selenium.functions.cookies.GetSeleniumCookieSupplier.cookies;

public class CookieTest extends BaseWebDriverTest {

    @Test
    public void getAllCookieTest() {
        var cookies = seleniumSteps.get(cookies());
        assertThat(new HashSet<>(cookies), equalTo(seleniumSteps.getWrappedDriver().manage().getCookies()));
    }

    @Test(priority = 1)
    public void getCookieConditionalTest() {
        var cookies = seleniumSteps.get(cookies()
                .criteria(cookieDomain("paypal.com"))
                .criteria(cookieIsSecure()));
        assertThat(cookies, hasSize(1));
    }

    @Test(priority = 2)
    public void addCookieTest() {
        var cookie1 = new Cookie("key6", "value6", "wikipedia.org", "some/path6", new Date(), true);
        var cookie2 = new Cookie("key7", "value7", "oracle.com", "some/path7", new Date(), false);

        var cookies = seleniumSteps
                .addCookies(cookie1, cookie2)
                .get(cookies());
        assertThat(cookies, hasItems(cookie1, cookie2));
    }

    @Test(priority = 3)
    public void removeCookiesWithSearchingTest() {
        var cookies = seleniumSteps.get(cookies()
                .criteria(cookieDomain("paypal.com"))
                .criteria(cookieIsSecure()));

        assert !cookies.isEmpty();

        var cookies2 = seleniumSteps.removeCookies(
                cookieDomain("paypal.com"),
                cookieIsSecure())
                .get(cookies());

        assertThat(cookies2, not(hasItems(cookies.toArray(new Cookie[]{}))));
    }

    @Test(priority = 5)
    public void removeCookiesTest() {
        var cookie1 = new Cookie("key2", "value2", "www.google.com", "some/path2", new Date(), false);
        var cookie2 = new Cookie("key3", "value3", "github.com", "some/path3", new Date(), false);

        var cookies = seleniumSteps
                .removeCookies(cookie1, cookie2)
                .get(cookies());

        assertThat(cookies, not(hasItems(cookie1, cookie2)));
    }

    @Test(priority = 4)
    public void removeAllCookiesTest() {
        var cookies = seleniumSteps
                .removeCookies()
                .get(cookies());
        assertThat(cookies, emptyCollectionOf(Cookie.class));
    }
}
