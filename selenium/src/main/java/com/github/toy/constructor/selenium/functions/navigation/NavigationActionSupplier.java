package com.github.toy.constructor.selenium.functions.navigation;

import com.github.toy.constructor.core.api.SequentialActionSupplier;
import com.github.toy.constructor.selenium.SeleniumSteps;
import com.github.toy.constructor.selenium.functions.target.locator.window.GetWindowSupplier;
import com.github.toy.constructor.selenium.functions.target.locator.window.Window;
import org.openqa.selenium.WebDriver;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.function.Consumer;

import static com.github.toy.constructor.selenium.functions.target.locator.window.GetWindowSupplier.window;
import static com.google.common.base.Preconditions.checkArgument;
import static java.lang.String.format;
import static org.apache.commons.lang3.StringUtils.isBlank;

@SuppressWarnings("unchecked")
public final class NavigationActionSupplier extends SequentialActionSupplier<SeleniumSteps, Window, NavigationActionSupplier> {

    private static final Consumer<Window> FORWARD = WebDriver.Navigation::forward;
    private static final Consumer<Window> BACK = WebDriver.Navigation::back;
    private static final Consumer<Window> REFRESH = WebDriver.Navigation::refresh;

    private NavigationActionSupplier() {
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

    private static Consumer<Window> navigateTo(String url) {
        return window -> window.to(checkUrl(url));
    }

    /**
     * Builds navigation to some URL in the first window.
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
     * Builds navigation forward in the first window.
     *
     * @return built navigation action
     */
    public static NavigationActionSupplier forward() {
        return new NavigationActionSupplier().andThenForward();
    }

    /**
     * Builds navigation forward in some window which should be found.
     *
     * @param windowSupplier is how to get the window where navigation should be performed
     * @return built navigation action
     */
    public static NavigationActionSupplier forward(GetWindowSupplier windowSupplier) {
        return new NavigationActionSupplier().andThenForward(windowSupplier);
    }

    /**
     * Builds navigation forward in the window.
     *
     * @param window is the window where navigation should be performed
     * @return built navigation action
     */
    public static NavigationActionSupplier forward(Window window) {
        return new NavigationActionSupplier().andThenForward(window);
    }

    /**
     * Builds navigation back in the first window.
     *
     * @return built navigation action
     */
    public static NavigationActionSupplier back() {
        return new NavigationActionSupplier().andThenBack();
    }

    /**
     * Builds navigation back in some window which should be found.
     *
     * @param windowSupplier is how to get the window where navigation should be performed
     * @return built navigation action
     */
    public static NavigationActionSupplier back(GetWindowSupplier windowSupplier) {
        return new NavigationActionSupplier().andThenBack(windowSupplier);
    }

    /**
     * Builds navigation back in the window.
     *
     * @param window is the window where navigation should be performed
     * @return built navigation action
     */
    public static NavigationActionSupplier back(Window window) {
        return new NavigationActionSupplier().andThenBack(window);
    }

    /**
     * Builds the refreshing in the first window.
     *
     * @return built the refreshing action
     */
    public static NavigationActionSupplier refresh() {
        return new NavigationActionSupplier().andThenRefresh();
    }

    /**
     * Builds the refreshing in some window which should be found.
     *
     * @param windowSupplier is how to get the window where the refreshing should be performed
     * @return built the refreshing action
     */
    public static NavigationActionSupplier refresh(GetWindowSupplier windowSupplier) {
        return new NavigationActionSupplier().andThenRefresh(windowSupplier);
    }

    /**
     * Builds the refreshing in the window.
     *
     * @param window is the window where the refreshing should be performed
     * @return built the refreshing action
     */
    public static NavigationActionSupplier refresh(Window window) {
        return new NavigationActionSupplier().andThenRefresh(window);
    }

    private NavigationActionSupplier andThen(String description,
                                             GetWindowSupplier windowSupplier,
                                             Consumer<Window> consumer) {
        return super.andThen(description, windowSupplier, consumer);
    }

    private NavigationActionSupplier andThen(String description,
                                             Window window,
                                             Consumer<Window> consumer) {
        return super.andThen(description, window, consumer);
    }

    /**
     * Adds another navigation to some URL in the first window.
     *
     * @param url the url to navigate to.
     * @return built navigation action
     */
    public NavigationActionSupplier andThenToUrl(String url) {
        return andThenToUrl(window(), url);
    }

    /**
     * Adds another navigation to some URL in some window which should be found.
     *
     * @param windowSupplier is how to get the window where navigation should be performed
     * @param url the url to navigate to.
     * @return built navigation action
     */
    public NavigationActionSupplier andThenToUrl(GetWindowSupplier windowSupplier, String url) {
        return andThen("Navigate to URL", windowSupplier, navigateTo(url));
    }

    /**
     * Adds another navigation to some URL in the window.
     *
     * @param window is the window where navigation should be performed
     * @param url the url to navigate to.
     * @return built navigation action
     */
    public NavigationActionSupplier andThenToUrl(Window window, String url) {
        return andThen("Navigate to URL", window, navigateTo(url));
    }

    /**
     * Adds another navigation forward in the first window.
     *
     * @return built navigation action
     */
    public NavigationActionSupplier andThenForward() {
        return andThenForward(window());
    }

    /**
     * Adds another navigation forward in some window which should be found.
     *
     * @param windowSupplier is how to get the window where navigation should be performed
     * @return built navigation action
     */
    public NavigationActionSupplier andThenForward(GetWindowSupplier windowSupplier) {
        return andThen("Navigate forward", windowSupplier, FORWARD);
    }

    /**
     * Adds another navigation forward in the window.
     *
     * @param window is the window where navigation should be performed
     * @return built navigation action
     */
    public NavigationActionSupplier andThenForward(Window window) {
        return andThen("Navigate forward", window, FORWARD);
    }

    /**
     * Adds another navigation back in the first window.
     *
     * @return built navigation action
     */
    public NavigationActionSupplier andThenBack() {
        return andThenBack(window());
    }

    /**
     * Adds another navigation back in some window which should be found.
     *
     * @param windowSupplier is how to get the window where navigation should be performed
     * @return built navigation action
     */
    public NavigationActionSupplier andThenBack(GetWindowSupplier windowSupplier) {
        return andThen("Navigate forward", windowSupplier, BACK);
    }

    /**
     * Adds another navigation back in the window.
     *
     * @param window is the window where navigation should be performed
     * @return built navigation action
     */
    public NavigationActionSupplier andThenBack(Window window) {
        return andThen("Navigate forward", window, BACK);
    }

    /**
     * Adds another refreshing in the first window.
     *
     * @return built refreshing action
     */
    public NavigationActionSupplier andThenRefresh() {
        return andThenRefresh(window());
    }

    /**
     * Adds another refreshing in some window which should be found.
     *
     * @param windowSupplier is how to get the window where the refreshing should be performed
     * @return built refreshing action
     */
    public NavigationActionSupplier andThenRefresh(GetWindowSupplier windowSupplier) {
        return andThen("Refresh", windowSupplier, REFRESH);
    }

    /**
     * Adds another refreshing in the window.
     *
     * @param window is the window where the refreshing should be performed
     * @return built navigation action
     */
    public NavigationActionSupplier andThenRefresh(Window window) {
        return andThen("Refresh", window, REFRESH);
    }

    @Override
    protected void performActionOn(Window value, Object... additionalArgument) {
        ((Consumer<Window>) additionalArgument[0]).accept(value);
    }
}
