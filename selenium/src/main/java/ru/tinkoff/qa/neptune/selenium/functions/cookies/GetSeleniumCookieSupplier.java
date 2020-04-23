package ru.tinkoff.qa.neptune.selenium.functions.cookies;

import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import ru.tinkoff.qa.neptune.core.api.event.firing.annotation.MakeFileCapturesOnFinishing;
import ru.tinkoff.qa.neptune.core.api.event.firing.annotation.MakeStringCapturesOnFinishing;
import ru.tinkoff.qa.neptune.core.api.steps.Criteria;
import ru.tinkoff.qa.neptune.core.api.steps.SequentialGetStepSupplier;
import ru.tinkoff.qa.neptune.selenium.SeleniumStepContext;

import java.time.Duration;
import java.util.Set;
import java.util.function.Predicate;

import static ru.tinkoff.qa.neptune.selenium.CurrentContentFunction.currentContent;

/**
 * This class is designed to build chain of functions that get cookies of a browser.
 */
@MakeStringCapturesOnFinishing
@MakeFileCapturesOnFinishing
public final class GetSeleniumCookieSupplier extends SequentialGetStepSupplier
        .GetIterableChainedStepSupplier<SeleniumStepContext, Set<Cookie>, WebDriver, Cookie, GetSeleniumCookieSupplier> {

    private GetSeleniumCookieSupplier() {
        super("Cookies", driver -> driver.manage().getCookies());
        from(currentContent());
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
    public GetSeleniumCookieSupplier criteria(Criteria<? super Cookie> condition) {
        return super.criteria(condition);
    }

    @Override
    public GetSeleniumCookieSupplier criteria(String conditionDescription, Predicate<? super Cookie> condition) {
        return super.criteria(conditionDescription, condition);
    }

    @Override
    public GetSeleniumCookieSupplier timeOut(Duration duration) {
        return super.timeOut(duration);
    }
}
