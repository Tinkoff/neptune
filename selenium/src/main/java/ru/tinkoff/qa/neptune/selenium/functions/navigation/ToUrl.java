package ru.tinkoff.qa.neptune.selenium.functions.navigation;

import ru.tinkoff.qa.neptune.selenium.functions.target.locator.window.GetWindowSupplier;
import ru.tinkoff.qa.neptune.selenium.functions.target.locator.window.Window;

import java.net.MalformedURLException;
import java.net.URL;

import static java.util.Optional.ofNullable;
import static ru.tinkoff.qa.neptune.selenium.functions.target.locator.window.GetWindowSupplier.window;
import static com.google.common.base.Preconditions.checkArgument;
import static java.lang.String.format;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static ru.tinkoff.qa.neptune.selenium.properties.SessionFlagProperties.ENABLE_ABILITY_TO_NAVIGATE_BY_RELATIVE_URL;
import static ru.tinkoff.qa.neptune.selenium.properties.URLProperties.BASE_WEB_DRIVER_URL_PROPERTY;

public final class ToUrl extends NavigationActionSupplier<ToUrl> {

    private static final String DESCRIPTION = "Navigate to URL %s in %s";
    private final String url;

    private ToUrl(String description, String url) {
        super(description);
        this.url = checkUrl(url);
    }

    private static String checkUrl(String url) {
        checkArgument(!isBlank(url), "Url should not be blank");

        try {
            return new URL(url).toString();
        }
        catch (MalformedURLException e) {
            if (ENABLE_ABILITY_TO_NAVIGATE_BY_RELATIVE_URL.get()) {
                return ofNullable(BASE_WEB_DRIVER_URL_PROPERTY.get())
                        .map(url1 -> {
                            try {
                                return new URL(url1 + url).toString();
                            } catch (MalformedURLException ex) {
                                throw new IllegalArgumentException(format("Given url %s is malformed", url), ex);
                            }
                        })
                        .orElseThrow(() -> new IllegalArgumentException(format("It is impossible to navigate by URL %s. " +
                                        "This value is not a valid URL and the property %s is not defined", url,
                                BASE_WEB_DRIVER_URL_PROPERTY.getPropertyName())));
            }
            else {
                throw new IllegalArgumentException(format("It is impossible to navigate by URL %s. " +
                                "This value is not a valid URL and the property %s is not defined/its value is %s", url,
                        ENABLE_ABILITY_TO_NAVIGATE_BY_RELATIVE_URL.getPropertyName(), false));
            }
        }
    }

    /**
     * Builds navigation to some URL in the first window.
     *
     * @param url the url to navigate to.
     * @return built navigation action
     */
    public static ToUrl toUrl(String url) {
        return toUrl(window(), url);
    }

    /**
     * Builds navigation to some URL in some window which should be found.
     *
     * @param windowSupplier is how to get the window where navigation should be performed
     * @param url the url to navigate to.
     * @return built navigation action
     */
    public static ToUrl toUrl(GetWindowSupplier windowSupplier, String url) {
        return new ToUrl(format(DESCRIPTION, url, windowSupplier), url).performOn(windowSupplier);
    }

    /**
     * Builds navigation to some URL in the window.
     *
     * @param window is the window where navigation should be performed
     * @param url the url to navigate to.
     * @return built navigation action
     */
    public static ToUrl toUrl(Window window, String url) {
        return new ToUrl(format(DESCRIPTION, url, window), url).performOn(window);
    }


    @Override
    protected void performActionOn(Window value) {
        value.to(url);
    }
}
