package ru.tinkoff.qa.neptune.selenium.functions.cookies;

import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import ru.tinkoff.qa.neptune.core.api.steps.Criteria;
import ru.tinkoff.qa.neptune.core.api.steps.SequentialActionSupplier;
import ru.tinkoff.qa.neptune.core.api.steps.StepParameter;
import ru.tinkoff.qa.neptune.selenium.SeleniumStepContext;

import java.time.Duration;
import java.util.Collection;
import java.util.Set;

import static com.google.common.base.Preconditions.checkArgument;
import static java.util.Optional.ofNullable;
import static ru.tinkoff.qa.neptune.core.api.steps.Criteria.AND;
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

        @StepParameter("Criteria to find cookies for removal")
        private final Criteria<Cookie> toBeRemoved;
        @StepParameter(value = "Time to find cookies for removal", doNotReportNullValues = true)
        private final Duration timeToFindCookies;
        private WebDriver driver;

        @SafeVarargs
        private RemoveFoundCookies(Duration timeToFindCookies, Criteria<Cookie>... toBeRemoved) {
            super("Remove cookies");

            this.toBeRemoved = ofNullable(toBeRemoved)
                    .map(criteria -> {
                        checkArgument(
                                criteria.length > 0,
                                "It is necessary to define at least one criteria to find cookies for removal"
                        );

                        if (criteria.length == 1) {
                            return criteria[0];
                        }
                        return AND(criteria);
                    })
                    .orElseThrow(() -> new IllegalArgumentException("It is necessary to define at least " +
                            "one criteria to find cookies for removal"));

            this.timeToFindCookies = timeToFindCookies;

            performOn(seleniumStepContext -> {
                var driver = seleniumStepContext.getWrappedDriver();
                try {
                    var getCookies = cookies().criteria(this.toBeRemoved);
                    ofNullable(this.timeToFindCookies).ifPresent(getCookies::timeOut);
                    return seleniumStepContext.get(getCookies);
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
    }

    /**
     * This class is designed to build an action that cleans browser's "cookie jar" of defined cookies.
     */
    private static final class RemoveDefinedCookies extends RemoveCookiesActionSupplier<WebDriver> {

        @StepParameter("Cookies for removal")
        private final Collection<Cookie> cookies;

        private RemoveDefinedCookies(Collection<Cookie> toBeRemoved) {
            super("Remove cookies");
            cookies = ofNullable(toBeRemoved)
                    .map(c -> {
                        checkArgument(
                                c.size() > 0,
                                "It is necessary to define at least one criteria to find cookies for removal"
                        );
                        return c;
                    })
                    .orElseThrow(() -> new IllegalArgumentException("It is necessary to define cookies for removal"));
            performOn(currentContent());
        }

        @Override
        protected void performActionOn(WebDriver value) {
            var options = value.manage();
            cookies.forEach(options::deleteCookie);
        }
    }
}
