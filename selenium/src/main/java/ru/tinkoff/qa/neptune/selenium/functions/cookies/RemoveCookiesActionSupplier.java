package ru.tinkoff.qa.neptune.selenium.functions.cookies;

import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import ru.tinkoff.qa.neptune.core.api.event.firing.annotations.MaxDepthOfReporting;
import ru.tinkoff.qa.neptune.core.api.steps.Criteria;
import ru.tinkoff.qa.neptune.core.api.steps.SequentialActionSupplier;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.IncludeParamsOfInnerGetterStep;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.StepParameter;
import ru.tinkoff.qa.neptune.selenium.SeleniumStepContext;

import java.time.Duration;
import java.util.Collection;
import java.util.List;

import static com.google.common.base.Preconditions.checkArgument;
import static java.util.Arrays.stream;
import static java.util.Objects.nonNull;
import static java.util.Optional.ofNullable;
import static ru.tinkoff.qa.neptune.selenium.SeleniumStepContext.CurrentContentFunction.currentContent;
import static ru.tinkoff.qa.neptune.selenium.functions.cookies.GetSeleniumCookieSupplier.cookies;

/**
 * This class is designed to build an action that removes cookies from browser's "cookie jar".
 */
@Description("Remove cookies")
public abstract class RemoveCookiesActionSupplier<T>
        extends SequentialActionSupplier<SeleniumStepContext, T, RemoveCookiesActionSupplier<T>> {

    private RemoveCookiesActionSupplier() {
        super();
    }

    /**
     * Creates an instance to build an action that cleans browser's "cookie jar".
     *
     * @return instance of {@link RemoveCookiesActionSupplier}
     */
    @Description("Remove all cookies")
    public static RemoveCookiesActionSupplier<WebDriver> deleteCookies() {
        return new RemoveAllCookiesActionSupplier();
    }

    /**
     * Creates an instance to build an action that cleans browser's "cookie jar" of cookies.
     * These cookies are expected to be found by criteria. It is possible to define time of the waiting
     * for expected cookies are present.
     *
     * @param timeToFindCookies time of the waiting for expected cookies are present.
     * @param toBeRemoved       which cookies should be deleted
     * @return instance of {@link RemoveCookiesActionSupplier}
     */
    @SafeVarargs
    public static RemoveCookiesActionSupplier<List<Cookie>> deleteCookies(Duration timeToFindCookies,
                                                                          Criteria<Cookie>... toBeRemoved) {
        return new RemoveFoundCookies(timeToFindCookies, toBeRemoved);
    }

    /**
     * Creates an instance to build an action that cleans browser's "cookie jar" of defined cookies.
     *
     * @param toBeRemoved cookies that should be deleted
     * @return instance of {@link RemoveCookiesActionSupplier}
     */
    public static RemoveCookiesActionSupplier<WebDriver> deleteCookies(Collection<Cookie> toBeRemoved) {
        return new RemoveDefinedCookies(toBeRemoved);
    }

    /**
     * This class is designed to build an action that cleans browser's "cookie jar".
     */
    @MaxDepthOfReporting(0)
    private static final class RemoveAllCookiesActionSupplier
            extends RemoveCookiesActionSupplier<WebDriver> {

        private RemoveAllCookiesActionSupplier() {
            super();
            performOn(currentContent());
        }

        @Override
        protected void howToPerform(WebDriver value) {
            value.manage().deleteAllCookies();
        }
    }

    /**
     * This class is designed to build an action that cleans browser's "cookie jar" of cookies.
     * These cookies are expected to be found by criteria. It is possible to define time of the waiting
     * for expected cookies are present.
     */
    @MaxDepthOfReporting(0)
    @IncludeParamsOfInnerGetterStep
    private static final class RemoveFoundCookies extends RemoveCookiesActionSupplier<List<Cookie>> {

        private WebDriver driver;

        @SafeVarargs
        private RemoveFoundCookies(Duration timeToFindCookies, Criteria<Cookie>... toBeRemoved) {
            super();
            checkArgument(nonNull(toBeRemoved) && toBeRemoved.length > 0,
                    "It is necessary to define at least one criteria to find cookies for removal");
            var getCookies = cookies();
            stream(toBeRemoved).forEach(getCookies::criteria);
            ofNullable(timeToFindCookies).ifPresent(getCookies::timeOut);
            performOn(getCookies.from(currentContent()));
        }

        @Override
        protected void onStart(SeleniumStepContext seleniumStepContext) {
            driver = seleniumStepContext.getWrappedDriver();
        }

        @Override
        protected void howToPerform(List<Cookie> value) {
            var options = driver.manage();
            value.forEach(options::deleteCookie);
        }
    }

    /**
     * This class is designed to build an action that cleans browser's "cookie jar" of defined cookies.
     */
    @MaxDepthOfReporting(0)
    private static final class RemoveDefinedCookies extends RemoveCookiesActionSupplier<WebDriver> {

        @StepParameter("Cookies for removal")
        private final Collection<Cookie> cookies;

        private RemoveDefinedCookies(Collection<Cookie> toBeRemoved) {
            super();
            checkArgument(nonNull(toBeRemoved) && toBeRemoved.size() > 0,
                    "It is necessary to define at least one cookie for removal");
            cookies = toBeRemoved;
            performOn(currentContent());
        }

        @Override
        protected void howToPerform(WebDriver value) {
            var options = value.manage();
            cookies.forEach(options::deleteCookie);
        }
    }
}
