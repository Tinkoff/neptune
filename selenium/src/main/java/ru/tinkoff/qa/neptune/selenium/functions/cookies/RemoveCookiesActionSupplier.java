package ru.tinkoff.qa.neptune.selenium.functions.cookies;

import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import ru.tinkoff.qa.neptune.core.api.SequentialActionSupplier;
import ru.tinkoff.qa.neptune.selenium.SeleniumSteps;

import static java.util.Arrays.stream;
import static ru.tinkoff.qa.neptune.selenium.CurrentContentFunction.currentContent;

/**
 * This class is designed to build an action that removes cookies from browser's "cookie jar".
 */
public abstract class RemoveCookiesActionSupplier<T> extends SequentialActionSupplier<SeleniumSteps, T, RemoveCookiesActionSupplier<T>> {

    private RemoveCookiesActionSupplier() {
        super();
    }

    /**
     * Creates an instance of {@link RemoveCookiesActionSupplier}
     *
     * @param getCookies is the way how to get browser cookies to be removed from the jar
     * @return an instance of {@link RemoveCookiesActionSupplier}
     */
    public static RemoveCookiesActionSupplier<SeleniumSteps> deleteCookies(GetSeleniumCookieSupplier getCookies) {
        return new RemoveCookiesActionSupplier<SeleniumSteps>() {
            @Override
            protected void performActionOn(SeleniumSteps value, Object... additionalArgument) {
                stream(additionalArgument)
                        .map(o -> (GetSeleniumCookieSupplier) o)
                        .forEach(getSeleniumCookieSupplier -> {
                            var toBeDeleted = value.get(getSeleniumCookieSupplier);
                            toBeDeleted.forEach(cookie -> value.getWrappedDriver().manage().deleteCookie(cookie));
                        });
            }
        }.andThen("The deleting from browser's cookie jar", seleniumSteps -> seleniumSteps, getCookies);
    }

    /**
     * Creates an instance of {@link RemoveCookiesActionSupplier}
     *
     * @param cookies browser cookies to be removed from the jar
     * @return an instance of {@link RemoveCookiesActionSupplier}
     */
    public static RemoveCookiesActionSupplier<WebDriver> deleteCookies(Cookie...cookies) {
        return new RemoveCookiesActionSupplier<WebDriver>() {
            @Override
            protected void performActionOn(WebDriver value, Object... additionalArgument) {
                stream(additionalArgument).map(o -> (Cookie) o)
                        .forEach(cookie -> value.manage().deleteCookie(cookie));
            }
        }.andThen("The deleting from browser's cookie jar", currentContent(), (Object[]) cookies);
    }

    /**
     * Creates an instance of {@link RemoveCookiesActionSupplier}
     *
     * @return an instance of {@link RemoveCookiesActionSupplier}
     */
    public static RemoveCookiesActionSupplier<WebDriver> deleteAllCookies() {
        return new RemoveCookiesActionSupplier<WebDriver>() {
            @Override
            protected void performActionOn(WebDriver value, Object... additionalArgument) {
                value.manage().deleteAllCookies();
            }
        }.andThen("The deleting all the cookies from browser's cookie jar", currentContent());
    }
}
