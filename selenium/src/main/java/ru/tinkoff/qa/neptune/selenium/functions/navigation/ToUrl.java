package ru.tinkoff.qa.neptune.selenium.functions.navigation;

import ru.tinkoff.qa.neptune.core.api.event.firing.annotations.CaptureOnFailure;
import ru.tinkoff.qa.neptune.core.api.event.firing.annotations.CaptureOnSuccess;
import ru.tinkoff.qa.neptune.core.api.event.firing.annotations.MaxDepthOfReporting;
import ru.tinkoff.qa.neptune.core.api.steps.SequentialActionSupplier;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.DescriptionFragment;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.IncludeParamsOfInnerGetterStep;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.StepParameter;
import ru.tinkoff.qa.neptune.selenium.SeleniumStepContext;
import ru.tinkoff.qa.neptune.selenium.captors.WebDriverImageCaptor;
import ru.tinkoff.qa.neptune.selenium.functions.target.locator.window.GetWindowSupplier;
import ru.tinkoff.qa.neptune.selenium.functions.target.locator.window.Window;

import java.net.MalformedURLException;
import java.net.URL;

import static com.google.common.base.Preconditions.checkArgument;
import static java.lang.String.format;
import static java.util.Objects.nonNull;
import static java.util.Optional.ofNullable;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static ru.tinkoff.qa.neptune.selenium.functions.target.locator.window.GetWindowSupplier.currentWindow;
import static ru.tinkoff.qa.neptune.selenium.properties.URLProperties.BASE_WEB_DRIVER_URL_PROPERTY;

@CaptureOnFailure(by = WebDriverImageCaptor.class)
@CaptureOnSuccess(by = WebDriverImageCaptor.class)
@SequentialActionSupplier.DefinePerformOnParameterName("Window/tab to perform navigation")
@MaxDepthOfReporting(1)
@IncludeParamsOfInnerGetterStep
public final class ToUrl extends SequentialActionSupplier<SeleniumStepContext, Window, ToUrl> {

    @StepParameter("Navigate to")
    private final URL url;

    private ToUrl(URL url) {
        super();
        checkArgument(nonNull(url), "URL value should differ from null");
        this.url = url;
    }

    private static URL checkUrl(String url) {
        checkArgument(!isBlank(url), "Url should not be blank");

        try {
            return new URL(url);
        } catch (MalformedURLException e) {
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
        }
    }

    /**
     * Builds navigation to some URL in some window that should be found.
     *
     * @param windowSupplier is how to get/window to perform navigation
     * @param url            is a page url
     * @return built navigation action
     */
    @Description("Navigate to URL {URL}")
    public static ToUrl toUrl(GetWindowSupplier windowSupplier, @DescriptionFragment("URL") URL url) {
        return new ToUrl(url).performOn(windowSupplier);
    }

    /**
     * Builds navigation to some URL in the window.
     *
     * @param window is the window where navigation should be performed
     * @param url    is a page url
     * @return built navigation action
     */
    @Description("Navigate to URL {URL}")
    public static ToUrl toUrl(Window window, @DescriptionFragment("URL") URL url) {
        return new ToUrl(url).performOn(window);
    }

    /**
     * Builds navigation to some URL in the browser window/tab that active currently.
     *
     * @param url the url to navigate to.
     * @return built navigation action
     */
    public static ToUrl toUrl(URL url) {
        return toUrl(currentWindow(), url);
    }

    /**
     * Builds navigation to some URL in the browser window/tab that active currently.
     *
     * @param url is a string value the url to navigate to. Also it may be a path relative to base web driver url.
     * @return built navigation action
     * @see ru.tinkoff.qa.neptune.selenium.properties.URLProperties#BASE_WEB_DRIVER_URL_PROPERTY
     */
    public static ToUrl toUrl(String url) {
        return toUrl(checkUrl(url));
    }

    /**
     * Builds navigation to some URL in some window that should be found.
     *
     * @param windowSupplier is how to get/window to perform navigation
     * @param url            is a string value the url to navigate to. Also it may be a path relative to base web driver url.
     * @return built navigation action
     * @see ru.tinkoff.qa.neptune.selenium.properties.URLProperties#BASE_WEB_DRIVER_URL_PROPERTY
     */
    public static ToUrl toUrl(GetWindowSupplier windowSupplier, String url) {
        return toUrl(windowSupplier, checkUrl(url));
    }

    /**
     * Builds navigation to some URL in the window.
     *
     * @param window is the window where navigation should be performed
     * @param url    is a string value the url to navigate to. Also it may be a path relative to base web driver url.d
     * @return built navigation action
     * @see ru.tinkoff.qa.neptune.selenium.properties.URLProperties#BASE_WEB_DRIVER_URL_PROPERTY
     */
    public static ToUrl toUrl(Window window, String url) {
        return toUrl(window, checkUrl(url));
    }


    @Override
    protected void howToPerform(Window value) {
        value.to(url);
    }
}
