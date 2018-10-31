package ru.tinkoff.qa.neptune.selenium.functions.navigation;

import org.apache.commons.validator.routines.UrlValidator;
import ru.tinkoff.qa.neptune.selenium.functions.target.locator.window.GetWindowSupplier;
import ru.tinkoff.qa.neptune.selenium.functions.target.locator.window.Window;

import java.net.MalformedURLException;
import java.net.URL;

import static java.util.Optional.ofNullable;
import static ru.tinkoff.qa.neptune.selenium.functions.target.locator.window.GetWindowSupplier.window;
import static com.google.common.base.Preconditions.checkArgument;
import static java.lang.String.format;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static ru.tinkoff.qa.neptune.selenium.properties.SessionFlagProperties.ENABLE_ABILITY_TO_USE_RELATIVE_URL;
import static ru.tinkoff.qa.neptune.selenium.properties.URLProperties.BASE_WEB_DRIVER_URL_PROPERTY;

public final class ToUrl extends NavigationActionSupplier<ToUrl> {
    private static final UrlValidator URL_VALIDATOR = new UrlValidator();

    private ToUrl() {
        super();
    }

    private static String checkUrl(String url) {
        checkArgument(!isBlank(url), "Url should not be blank");

        String toURL;
        if (URL_VALIDATOR.isValid(url)) {
            toURL = url;
        }
        else {
            if (ENABLE_ABILITY_TO_USE_RELATIVE_URL.get()) {
                toURL = ofNullable(BASE_WEB_DRIVER_URL_PROPERTY.get())
                        .map(url1 -> url1 + url)
                        .orElseThrow(() -> new IllegalArgumentException(format("It is impossible to navigate by URL %s. " +
                                        "This value is not a valid URL and the property %s is not defined", url,
                                BASE_WEB_DRIVER_URL_PROPERTY.getPropertyName())));
            }
            else {
                throw new IllegalArgumentException(format("It is impossible to navigate by URL %s. " +
                                "This value is not a valid URL and the property %s is not defined or its value is %s", url,
                        ENABLE_ABILITY_TO_USE_RELATIVE_URL.getPropertyName(), false));
            }
        }

        try {
            return new URL(toURL).toString();
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException(format("Given url %s is malformed", url));
        }
    }

    /**
     * Builds navigation to some URL in the first window.
     *
     * @param url the url to navigate to.
     * @return built navigation action
     */
    public static ToUrl toUrl(String url) {
        return new ToUrl().andThenToUrl(url);
    }

    /**
     * Builds navigation to some URL in some window which should be found.
     *
     * @param windowSupplier is how to get the window where navigation should be performed
     * @param url the url to navigate to.
     * @return built navigation action
     */
    public static ToUrl toUrl(GetWindowSupplier windowSupplier, String url) {
        return new ToUrl().andThenToUrl(windowSupplier, url);
    }

    /**
     * Builds navigation to some URL in the window.
     *
     * @param window is the window where navigation should be performed
     * @param url the url to navigate to.
     * @return built navigation action
     */
    public static ToUrl toUrl(Window window, String url) {
        return new ToUrl().andThenToUrl(window, url);
    }

    /**
     * Adds another navigation to some URL in the first window.
     *
     * @param url the url to navigate to.
     * @return built navigation action
     */
    public ToUrl andThenToUrl(String url) {
        return andThenToUrl(window(), url);
    }

    /**
     * Adds another navigation to some URL in some window which should be found.
     *
     * @param windowSupplier is how to get the window where navigation should be performed
     * @param url the url to navigate to.
     * @return built navigation action
     */
    public ToUrl andThenToUrl(GetWindowSupplier windowSupplier, String url) {
        return andThen("Navigate to URL", windowSupplier, url);
    }

    /**
     * Adds another navigation to some URL in the window.
     *
     * @param window is the window where navigation should be performed
     * @param url the url to navigate to.
     * @return built navigation action
     */
    public ToUrl andThenToUrl(Window window, String url) {
        return andThen("Navigate to URL", window, url);
    }

    @Override
    protected void performActionOn(Window value, Object... additionalArgument) {
        value.to(checkUrl(String.valueOf(additionalArgument[0])));
    }
}
