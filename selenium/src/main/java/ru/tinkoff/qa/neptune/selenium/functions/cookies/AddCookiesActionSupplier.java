package ru.tinkoff.qa.neptune.selenium.functions.cookies;

import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import ru.tinkoff.qa.neptune.core.api.steps.SequentialActionSupplier;
import ru.tinkoff.qa.neptune.core.api.steps.StepParameter;
import ru.tinkoff.qa.neptune.selenium.SeleniumStepContext;

import java.util.Collection;

import static com.google.common.base.Preconditions.checkArgument;
import static java.util.Objects.nonNull;
import static ru.tinkoff.qa.neptune.selenium.CurrentContentFunction.currentContent;

/**
 * This class is designed to build an action that adds cookies to browser's "cookie jar".
 */
public final class AddCookiesActionSupplier extends SequentialActionSupplier<SeleniumStepContext, WebDriver, AddCookiesActionSupplier> {

    @StepParameter("Cookies to add")
    private final Collection<Cookie> cookies;

    private AddCookiesActionSupplier(Collection<Cookie> cookies) {
        super("Add cookies to browsers cookie jar");
        checkArgument(nonNull(cookies), "Cookies to be added should not be a null value");
        checkArgument(cookies.size() > 0, "At least one cookie should be defined for the adding");
        this.cookies = cookies;
        performOn(currentContent());
    }

    /**
     * Creates an instance of {@link AddCookiesActionSupplier}
     *
     * @param cookies to be added
     * @return an instance of {@link AddCookiesActionSupplier}
     */
    public static AddCookiesActionSupplier addCookies(Collection<Cookie> cookies) {
        return new AddCookiesActionSupplier(cookies);
    }

    @Override
    protected void performActionOn(WebDriver value) {
        cookies.forEach(cookie -> value.manage().addCookie(cookie));
    }
}
