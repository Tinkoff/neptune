package ru.tinkoff.qa.neptune.selenium.functions.cookies;

import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import ru.tinkoff.qa.neptune.core.api.SequentialActionSupplier;
import ru.tinkoff.qa.neptune.selenium.SeleniumSteps;

import static com.google.common.base.Preconditions.checkArgument;
import static java.util.Arrays.stream;
import static ru.tinkoff.qa.neptune.selenium.CurrentContentFunction.currentContent;

/**
 * This class is designed to build an action that adds cookies to browser's "cookie jar".
 */
public final class AddCookiesActionSupplier extends SequentialActionSupplier<SeleniumSteps, WebDriver, AddCookiesActionSupplier> {

    private AddCookiesActionSupplier() {
        super();
    }

    /**
     * Creates an instance of {@link AddCookiesActionSupplier}
     *
     * @param cookies to be added
     * @return an instance of {@link AddCookiesActionSupplier}
     */
    public static AddCookiesActionSupplier addCookies(Cookie...cookies) {
        checkArgument(cookies != null, "Cookies to be add should not be a null value");
        checkArgument(cookies.length > 0, "At least one cookie should be defined for the adding");
        return new AddCookiesActionSupplier().andThen("Adding the cookies",
                currentContent(), (Object[]) cookies);
    }

    @Override
    protected void performActionOn(WebDriver value, Object... additionalArgument) {
        stream(additionalArgument).map(o -> (Cookie) o)
                .forEach(cookie -> value.manage().addCookie(cookie));
    }
}
