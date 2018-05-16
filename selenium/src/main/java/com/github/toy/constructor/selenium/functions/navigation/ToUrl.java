package com.github.toy.constructor.selenium.functions.navigation;

import com.github.toy.constructor.selenium.functions.target.locator.window.GetWindowSupplier;
import com.github.toy.constructor.selenium.functions.target.locator.window.Window;

import java.net.MalformedURLException;
import java.net.URL;

import static com.github.toy.constructor.selenium.functions.target.locator.window.GetWindowSupplier.window;
import static com.google.common.base.Preconditions.checkArgument;
import static java.lang.String.format;
import static org.apache.commons.lang3.StringUtils.isBlank;

public final class ToUrl extends NavigationActionSupplier<ToUrl> {

    private ToUrl() {
        super();
    }

    private static String checkUrl(String url) {
        checkArgument(!isBlank(url), "Url should not be blank");
        try {
            return new URL(url).toString();
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
