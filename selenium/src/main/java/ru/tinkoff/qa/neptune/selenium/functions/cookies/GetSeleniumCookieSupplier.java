package ru.tinkoff.qa.neptune.selenium.functions.cookies;

import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import ru.tinkoff.qa.neptune.core.api.event.firing.annotations.CaptureOnSuccess;
import ru.tinkoff.qa.neptune.core.api.event.firing.annotations.MaxDepthOfReporting;
import ru.tinkoff.qa.neptune.core.api.event.firing.collections.CollectionCaptor;
import ru.tinkoff.qa.neptune.core.api.steps.Criteria;
import ru.tinkoff.qa.neptune.core.api.steps.SequentialGetStepSupplier;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;
import ru.tinkoff.qa.neptune.selenium.SeleniumStepContext;

import java.time.Duration;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;

import static ru.tinkoff.qa.neptune.selenium.SeleniumStepContext.CurrentContentFunction.currentContent;

/**
 * This class is designed to build chain of functions that get cookies of a browser.
 */
@CaptureOnSuccess(by = CollectionCaptor.class)
@SequentialGetStepSupplier.DefineCriteriaParameterName("Cookie criteria")
@SequentialGetStepSupplier.DefineTimeOutParameterName("Time to find cookies")
@Description("Cookies")
@MaxDepthOfReporting(0)
public final class GetSeleniumCookieSupplier extends SequentialGetStepSupplier
        .GetIterableChainedStepSupplier<SeleniumStepContext, Set<Cookie>, WebDriver, Cookie, GetSeleniumCookieSupplier> {

    private GetSeleniumCookieSupplier() {
        super(driver -> driver.manage().getCookies());
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

    @Override
    protected GetSeleniumCookieSupplier from(Function<SeleniumStepContext, ? extends WebDriver> from) {
        return super.from(from);
    }
}
