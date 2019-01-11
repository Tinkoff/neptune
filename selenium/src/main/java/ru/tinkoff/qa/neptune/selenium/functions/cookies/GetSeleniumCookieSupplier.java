package ru.tinkoff.qa.neptune.selenium.functions.cookies;

import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import ru.tinkoff.qa.neptune.core.api.SequentialGetStepSupplier;
import ru.tinkoff.qa.neptune.selenium.SeleniumStepContext;

import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;

import static java.util.Optional.ofNullable;
import static ru.tinkoff.qa.neptune.core.api.StoryWriter.toGet;
import static ru.tinkoff.qa.neptune.core.api.conditions.ToGetSubIterable.getIterable;
import static ru.tinkoff.qa.neptune.selenium.CurrentContentFunction.currentContent;

/**
 * This class is designed to build chain of functions that get cookies of a browser.
 */
public final class GetSeleniumCookieSupplier extends SequentialGetStepSupplier<SeleniumStepContext, Set<Cookie>, WebDriver, GetSeleniumCookieSupplier> {

    private Predicate<Cookie> cookiePredicate;

    private GetSeleniumCookieSupplier() {
        super();
    }

    /**
     * Creates an instance of {@link GetSeleniumCookieSupplier} that builds a chain of functions that get cookies
     * of a browser.
     *
     * @return an instance of {@link GetSeleniumCookieSupplier}
     */
    public static GetSeleniumCookieSupplier cookies() {
        return new GetSeleniumCookieSupplier();
    }

    @Override
    public Function<SeleniumStepContext, Set<Cookie>> get() {
        return ofNullable(super.get()).orElseGet(() -> {
            super.from(currentContent());
            return super.get();
        });
    }

    /**
     * Sets a predicate that represents criteria to get cookies. These cookies should meet the criteria.
     *
     * @param condition a predicate that represents criteria to get cookies.
     * @return self-reference
     */
    public GetSeleniumCookieSupplier withCondition(Predicate<Cookie> condition) {
        this.cookiePredicate = condition;
        return this;
    }

    Predicate<Cookie> getCondition() {
        return cookiePredicate;
    }

    @Override
    protected Function<WebDriver, Set<Cookie>> getEndFunction() {
        var func = (Function<WebDriver, Set<Cookie>>) driver -> driver.manage().getCookies();
        return ofNullable(cookiePredicate)
                .map(condition -> getIterable("Cookies", func, condition, true, true))
                .orElseGet(() -> toGet("Cookies", func));
    }
}
