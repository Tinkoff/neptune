package com.github.toy.constructor.selenium.functions.navigation;

import com.github.toy.constructor.core.api.SequentialActionSupplier;
import com.github.toy.constructor.selenium.SeleniumSteps;
import com.github.toy.constructor.selenium.functions.target.locator.window.GetWindowSupplier;
import com.github.toy.constructor.selenium.functions.target.locator.window.Window;
import org.openqa.selenium.WebDriver;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.function.Predicate;

import static com.github.toy.constructor.core.api.StoryWriter.toGet;
import static com.google.common.base.Preconditions.checkArgument;
import static java.lang.String.format;
import static org.apache.commons.lang3.StringUtils.isBlank;

public final class NavigationActionSupplier extends SequentialActionSupplier<SeleniumSteps, WebDriver, NavigationActionSupplier> {

    private static final Predicate<String> CHECK_URL = (Predicate<String>) s -> {
        try {
            new URL(s);
            return true;
        } catch (MalformedURLException e) {
            return false;
        }
    };

    private NavigationActionSupplier() {
        super();
    }

    private static void checkUrl(String url) {
        checkArgument(!isBlank(url), "Url should not be blank");
        checkArgument(CHECK_URL.test(url), format("Given url %s is malformed", url));
    }

    /**
     * Builds navigation to some URL in the current window.
     *
     * @param url the url to navigate to.
     * @return built navigation action
     */
    public static NavigationActionSupplier toUrl(String url) {
        return new NavigationActionSupplier().andThenToUrl(url);
    }

    /**
     * Builds navigation to some URL in some window which should be found.
     *
     * @param windowSupplier is how to get the window where navigation should be performed
     * @param url the url to navigate to.
     * @return built navigation action
     */
    public static NavigationActionSupplier toUrl(GetWindowSupplier windowSupplier, String url) {
        return new NavigationActionSupplier().andThenToUrl(windowSupplier, url);
    }

    /**
     * Builds navigation to some URL in the window.
     *
     * @param window is the window where navigation should be performed
     * @param url the url to navigate to.
     * @return built navigation action
     */
    public static NavigationActionSupplier toUrl(Window window, String url) {
        return new NavigationActionSupplier().andThenToUrl(window, url);
    }

    /**
     * Adds another navigation to some URL in the current window.
     *
     * @param url the url to navigate to.
     * @return built navigation action
     */
    public NavigationActionSupplier andThenToUrl(String url) {
        checkUrl(url);
        return andThen(format("Navigation to %s in current window/tab", url),
                toGet("Current window/tab", SeleniumSteps::getWrappedDriver), url);
    }

    /**
     * Adds another navigation to some URL in some window which should be found.
     *
     * @param windowSupplier is how to get the window where navigation should be performed
     * @param url the url to navigate to.
     * @return built navigation action
     */
    public NavigationActionSupplier andThenToUrl(GetWindowSupplier windowSupplier, String url) {
        checkUrl(url);
        return andThen(format("Navigation to %s in current window/tab", url),
                toGet("Current window/tab", seleniumSteps -> seleniumSteps
                        .get(windowSupplier).getWrappedDriver()), url);
    }

    /**
     * Adds another navigation to some URL in the window.
     *
     * @param window is the window where navigation should be performed
     * @param url the url to navigate to.
     * @return built navigation action
     */
    public NavigationActionSupplier andThenToUrl(Window window, String url) {
        checkUrl(url);
        return andThen(format("Navigation to %s in given window/tab %s", url, window),
                toGet(format("Window %s", window), seleniumSteps -> {
                    window.switchToMe();
                    return window.getWrappedDriver();
                }), url);
    }

    @Override
    protected void performActionOn(WebDriver value, Object... additionalArgument) {
        value.get(String.valueOf(additionalArgument[0]));
    }
}
