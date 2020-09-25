package ru.tinkoff.qa.neptune.selenium.functions.navigation;

import ru.tinkoff.qa.neptune.core.api.event.firing.annotation.MakeFileCapturesOnFinishing;
import ru.tinkoff.qa.neptune.core.api.event.firing.annotation.MakeImageCapturesOnFinishing;
import ru.tinkoff.qa.neptune.core.api.steps.SequentialActionSupplier;
import ru.tinkoff.qa.neptune.core.api.steps.StepParameter;
import ru.tinkoff.qa.neptune.selenium.SeleniumStepContext;
import ru.tinkoff.qa.neptune.selenium.functions.target.locator.window.GetWindowSupplier;
import ru.tinkoff.qa.neptune.selenium.functions.target.locator.window.Window;

import java.net.MalformedURLException;
import java.net.URL;

import static com.google.common.base.Preconditions.checkArgument;
import static java.lang.String.format;
import static java.util.Objects.nonNull;
import static java.util.Optional.ofNullable;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static ru.tinkoff.qa.neptune.selenium.functions.target.locator.window.GetWindowSupplier.window;
import static ru.tinkoff.qa.neptune.selenium.properties.SessionFlagProperties.ENABLE_ABILITY_TO_NAVIGATE_BY_RELATIVE_URL;
import static ru.tinkoff.qa.neptune.selenium.properties.URLProperties.BASE_WEB_DRIVER_URL_PROPERTY;

@MakeImageCapturesOnFinishing
@MakeFileCapturesOnFinishing
@SequentialActionSupplier.DefaultParameterNames(
        performOn = "Window/tab to perform navigation"
)
public final class ToUrl extends SequentialActionSupplier<SeleniumStepContext, Window, ToUrl> {

    @StepParameter("Navigate to")
    private final URL url;

    private ToUrl(URL url) {
        super("Navigate to URL");
        checkArgument(nonNull(url), "URL value should differ from null");
        this.url = url;
    }

    private static URL checkUrl(String url) {
        checkArgument(!isBlank(url), "Url should not be blank");

        try {
            return new URL(url);
        } catch (MalformedURLException e) {
            if (ENABLE_ABILITY_TO_NAVIGATE_BY_RELATIVE_URL.get()) {
                return ofNullable(BASE_WEB_DRIVER_URL_PROPERTY.get())
                        .map(url1 -> {
                            try {
                                return new URL(url1 + url);
                            } catch (MalformedURLException ex) {
                                throw new IllegalArgumentException(format("Given url %s is malformed", url), ex);
                            }
                        })
                        .orElseThrow(() -> new IllegalArgumentException(format("It is impossible to navigate by URL %s. " +
                                        "This value is not a valid URL and the property %s is not defined", url,
                                BASE_WEB_DRIVER_URL_PROPERTY.getName())));
            } else {
                throw new IllegalArgumentException(format("It is impossible to navigate by URL %s. " +
                                "This value is not a valid URL and the property %s is not defined/its value is %s", url,
                        ENABLE_ABILITY_TO_NAVIGATE_BY_RELATIVE_URL.getName(), false));
            }
        }
    }

    /**
     * Builds navigation to some URL in some window that should be found.
     *
     * @param windowSupplier is how to get/window to perform navigation
     * @param url            is a page url
     * @return built navigation action
     */
    public static ToUrl toUrl(GetWindowSupplier windowSupplier, URL url) {
        return new ToUrl(url).performOn(windowSupplier);
    }

    /**
     * Builds navigation to some URL in the window.
     *
     * @param window is the window where navigation should be performed
     * @param url    is a page url
     * @return built navigation action
     */
    public static ToUrl toUrl(Window window, URL url) {
        return new ToUrl(url).performOn(window);
    }

    /**
     * Builds navigation to some URL in the browser window/tab that active currently.
     *
     * @param url the url to navigate to.
     * @return built navigation action
     */
    public static ToUrl toUrl(URL url) {
        return toUrl(window(), url);
    }

    /**
     * Builds navigation to some URL in the browser window/tab that active currently.
     *
     * @param url is a string value the url to navigate to. Also it may be a path relative to base web driver url.
     *            It is possible when ability to navigate by relative path is enabled
     * @return built navigation action
     * @see ru.tinkoff.qa.neptune.selenium.properties.URLProperties#BASE_WEB_DRIVER_URL_PROPERTY
     * @see ru.tinkoff.qa.neptune.selenium.properties.SessionFlagProperties#ENABLE_ABILITY_TO_NAVIGATE_BY_RELATIVE_URL
     */
    public static ToUrl toUrl(String url) {
        return toUrl(checkUrl(url));
    }

    /**
     * Builds navigation to some URL in some window that should be found.
     *
     * @param windowSupplier is how to get/window to perform navigation
     * @param url            is a string value the url to navigate to. Also it may be a path relative to base web driver url.
     *                       It is possible when ability to navigate by relative path is enabled
     * @return built navigation action
     * @see ru.tinkoff.qa.neptune.selenium.properties.URLProperties#BASE_WEB_DRIVER_URL_PROPERTY
     * @see ru.tinkoff.qa.neptune.selenium.properties.SessionFlagProperties#ENABLE_ABILITY_TO_NAVIGATE_BY_RELATIVE_URL
     */
    public static ToUrl toUrl(GetWindowSupplier windowSupplier, String url) {
        return toUrl(windowSupplier, checkUrl(url));
    }

    /**
     * Builds navigation to some URL in the window.
     *
     * @param window is the window where navigation should be performed
     * @param url    is a string value the url to navigate to. Also it may be a path relative to base web driver url.
     *               It is possible when ability to navigate by relative path is enabled
     * @return built navigation action
     * @see ru.tinkoff.qa.neptune.selenium.properties.URLProperties#BASE_WEB_DRIVER_URL_PROPERTY
     * @see ru.tinkoff.qa.neptune.selenium.properties.SessionFlagProperties#ENABLE_ABILITY_TO_NAVIGATE_BY_RELATIVE_URL
     */
    public static ToUrl toUrl(Window window, String url) {
        return toUrl(window, checkUrl(url));
    }


    @Override
    protected void performActionOn(Window value) {
        value.to(url);
    }
}
