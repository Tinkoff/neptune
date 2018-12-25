package ru.tinkoff.qa.neptune.selenium.functions.cookies;

import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import ru.tinkoff.qa.neptune.core.api.SequentialActionSupplier;
import ru.tinkoff.qa.neptune.selenium.SeleniumSteps;

import java.util.Arrays;

import static com.google.common.base.Preconditions.checkArgument;
import static java.lang.String.format;
import static java.util.Arrays.stream;
import static java.util.Objects.nonNull;
import static ru.tinkoff.qa.neptune.selenium.CurrentContentFunction.currentContent;

/**
 * This class is designed to build an action that adds cookies to browser's "cookie jar".
 */
public final class AddCookiesActionSupplier extends SequentialActionSupplier<SeleniumSteps, WebDriver, AddCookiesActionSupplier> {

    private final Cookie[] cookies;

    private AddCookiesActionSupplier(Cookie... cookies) {
        super(format("Adding the cookies %s", Arrays.toString(cookies)));
        checkArgument(nonNull(cookies), "Cookies to be add should not be a null value");
        checkArgument(cookies.length > 0, "At least one cookie should be defined for the adding");
        this.cookies = cookies;
    }

    /**
     * Creates an instance of {@link AddCookiesActionSupplier}
     *
     * @param cookies to be added
     * @return an instance of {@link AddCookiesActionSupplier}
     */
    public static AddCookiesActionSupplier addCookies(Cookie...cookies) {
        return new AddCookiesActionSupplier(cookies).performOn(currentContent());
    }

    @Override
    protected void performActionOn(WebDriver value) {
        checkArgument(nonNull(cookies), "Cookies to be add should not be a null value");
        checkArgument(cookies.length > 0, "At least one cookie should be defined for the adding");
        stream(cookies).forEach(cookie -> value.manage().addCookie(cookie));
    }
}
