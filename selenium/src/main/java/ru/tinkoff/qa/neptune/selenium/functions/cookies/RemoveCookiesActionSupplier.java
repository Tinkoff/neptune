package ru.tinkoff.qa.neptune.selenium.functions.cookies;

import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import ru.tinkoff.qa.neptune.core.api.steps.Criteria;
import ru.tinkoff.qa.neptune.core.api.steps.DefaultReportStepParameterFactory;
import ru.tinkoff.qa.neptune.core.api.steps.SequentialActionSupplier;
import ru.tinkoff.qa.neptune.core.api.steps.StepParameter;
import ru.tinkoff.qa.neptune.selenium.SeleniumStepContext;

import java.time.Duration;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

import static com.google.common.base.Preconditions.checkArgument;
import static java.util.Arrays.stream;
import static java.util.Objects.nonNull;
import static java.util.Optional.ofNullable;
import static ru.tinkoff.qa.neptune.selenium.CurrentContentFunction.currentContent;
import static ru.tinkoff.qa.neptune.selenium.functions.cookies.GetSeleniumCookieSupplier.cookies;

/**
 * This class is designed to build an action that removes cookies from browser's "cookie jar".
 */
public abstract class RemoveCookiesActionSupplier<T>
        extends SequentialActionSupplier<SeleniumStepContext, T, RemoveCookiesActionSupplier<T>> {

    private RemoveCookiesActionSupplier(String description) {
        super(description);
    }

    /**
     * Creates an instance to build an action that cleans browser's "cookie jar".
     *
     * @return instance of {@link RemoveCookiesActionSupplier}
     */
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
    public static RemoveCookiesActionSupplier<Set<Cookie>> deleteCookies(Duration timeToFindCookies,
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
    private static final class RemoveAllCookiesActionSupplier
            extends RemoveCookiesActionSupplier<WebDriver> {

        private RemoveAllCookiesActionSupplier() {
            super("Delete all the cookies from browser's cookie jar");
            performOn(currentContent());
        }

        @Override
        protected void performActionOn(WebDriver value) {
            value.manage().deleteAllCookies();
        }
    }

    /**
     * This class is designed to build an action that cleans browser's "cookie jar" of cookies.
     * These cookies are expected to be found by criteria. It is possible to define time of the waiting
     * for expected cookies are present.
     */
    private static final class RemoveFoundCookies extends RemoveCookiesActionSupplier<Set<Cookie>> {

        private final GetSeleniumCookieSupplier getCookies;
        private WebDriver driver;

        @SafeVarargs
        private RemoveFoundCookies(Duration timeToFindCookies, Criteria<Cookie>... toBeRemoved) {
            super("Remove cookies");
            checkArgument(nonNull(toBeRemoved) && toBeRemoved.length > 0,
                    "It is necessary to define at least one criteria to find cookies for removal");
            var getCookies = cookies();
            stream(toBeRemoved).forEach(getCookies::criteria);
            ofNullable(timeToFindCookies).ifPresent(getCookies::timeOut);
            this.getCookies = getCookies;

            performOn(seleniumStepContext -> {
                var driver = seleniumStepContext.getWrappedDriver();
                try {
                    return seleniumStepContext.get(this.getCookies);
                } finally {
                    this.driver = driver;
                }
            });
        }

        @Override
        protected void performActionOn(Set<Cookie> value) {
            var options = driver.manage();
            value.forEach(options::deleteCookie);
        }

        @Override
        protected Map<String, String> getParameters() {
            return DefaultReportStepParameterFactory.getParameters(getCookies);
        }
    }

    /**
     * This class is designed to build an action that cleans browser's "cookie jar" of defined cookies.
     */
    private static final class RemoveDefinedCookies extends RemoveCookiesActionSupplier<WebDriver> {

        @StepParameter("Cookies for removal")
        private final Collection<Cookie> cookies;

        private RemoveDefinedCookies(Collection<Cookie> toBeRemoved) {
            super("Remove cookies");
            checkArgument(nonNull(toBeRemoved) && toBeRemoved.size() > 0,
                    "It is necessary to define at least one cookie for removal");
            cookies = toBeRemoved;
            performOn(currentContent());
        }

        @Override
        protected void performActionOn(WebDriver value) {
            var options = value.manage();
            cookies.forEach(options::deleteCookie);
        }
    }
}
