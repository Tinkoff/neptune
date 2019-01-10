package ru.tinkoff.qa.neptune.selenium.functions.cookies;

import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import ru.tinkoff.qa.neptune.core.api.SequentialActionSupplier;
import ru.tinkoff.qa.neptune.selenium.SeleniumStepPerformer;

import java.util.Arrays;

import static com.google.common.base.Preconditions.checkArgument;
import static java.lang.String.format;
import static java.util.Arrays.stream;
import static java.util.Objects.nonNull;
import static java.util.Optional.ofNullable;
import static ru.tinkoff.qa.neptune.selenium.CurrentContentFunction.currentContent;

/**
 * This class is designed to build an action that removes cookies from browser's "cookie jar".
 */
public abstract class RemoveCookiesActionSupplier<T> extends SequentialActionSupplier<SeleniumStepPerformer, T, RemoveCookiesActionSupplier<T>> {

    private RemoveCookiesActionSupplier(String description) {
        super(description);
    }

    /**
     * Creates an instance of {@link RemoveCookiesActionSupplier}
     *
     * @param getCookies is the way how to get browser cookies to be removed from the jar
     * @return an instance of {@link RemoveCookiesActionSupplier}
     */
    public static RemoveCookiesActionSupplier<SeleniumStepPerformer> deleteCookies(GetSeleniumCookieSupplier getCookies) {
        checkArgument(nonNull(getCookies), "The way how to get cookies to delete is not defined");
        var description = ofNullable(getCookies.getCondition())
                .map(cookiePredicate -> format("Delete from browser's cookie jar. Cookies: %s", getCookies))
                .orElse("Delete all the cookies from browser's cookie jar");

        return new RemoveCookiesActionSupplier<SeleniumStepPerformer>(description) {
            @Override
            protected void performActionOn(SeleniumStepPerformer value) {
                var toBeDeleted = value.get(getCookies);
                toBeDeleted.forEach(cookie -> value.getWrappedDriver().manage().deleteCookie(cookie));
            }
        }.performOn(seleniumSteps -> seleniumSteps);
    }

    /**
     * Creates an instance of {@link RemoveCookiesActionSupplier}
     *
     * @param cookies browser cookies to be removed from the jar
     * @return an instance of {@link RemoveCookiesActionSupplier}
     */
    public static RemoveCookiesActionSupplier<WebDriver> deleteCookies(Cookie...cookies) {
        checkArgument(cookies.length > 0, "Should be defined at least one cookie to delete");
        return new RemoveCookiesActionSupplier<WebDriver>(format("Delete from browser's cookie jar. Cookies: %s", Arrays.toString(cookies))) {
            @Override
            protected void performActionOn(WebDriver value) {
                stream(cookies).forEach(cookie -> value.manage().deleteCookie(cookie));
            }
        }.performOn(currentContent());
    }

    /**
     * Creates an instance of {@link RemoveCookiesActionSupplier}
     *
     * @return an instance of {@link RemoveCookiesActionSupplier}
     */
    public static RemoveCookiesActionSupplier<WebDriver> deleteAllCookies() {
        return new RemoveCookiesActionSupplier<WebDriver>("Delete all the cookies from browser's cookie jar") {
            @Override
            protected void performActionOn(WebDriver value) {
                value.manage().deleteAllCookies();
            }
        }.performOn(currentContent());
    }
}
