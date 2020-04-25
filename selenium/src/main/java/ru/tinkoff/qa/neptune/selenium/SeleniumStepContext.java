package ru.tinkoff.qa.neptune.selenium;

import org.openqa.selenium.*;
import ru.tinkoff.qa.neptune.core.api.cleaning.ContextRefreshable;
import ru.tinkoff.qa.neptune.core.api.cleaning.Stoppable;
import ru.tinkoff.qa.neptune.core.api.steps.Criteria;
import ru.tinkoff.qa.neptune.core.api.steps.SequentialActionSupplier;
import ru.tinkoff.qa.neptune.core.api.steps.StepAction;
import ru.tinkoff.qa.neptune.core.api.steps.context.Context;
import ru.tinkoff.qa.neptune.core.api.steps.context.CreateWith;
import ru.tinkoff.qa.neptune.selenium.api.widget.Widget;
import ru.tinkoff.qa.neptune.selenium.functions.click.ClickActionSupplier;
import ru.tinkoff.qa.neptune.selenium.functions.cookies.AddCookiesActionSupplier;
import ru.tinkoff.qa.neptune.selenium.functions.cookies.GetSeleniumCookieSupplier;
import ru.tinkoff.qa.neptune.selenium.functions.edit.EditActionSupplier;
import ru.tinkoff.qa.neptune.selenium.functions.java.script.GetJavaScriptResultSupplier;
import ru.tinkoff.qa.neptune.selenium.functions.searching.MultipleSearchSupplier;
import ru.tinkoff.qa.neptune.selenium.functions.searching.SearchSupplier;
import ru.tinkoff.qa.neptune.selenium.functions.target.locator.active.element.GetActiveElementSupplier;
import ru.tinkoff.qa.neptune.selenium.functions.target.locator.alert.AlertActionSupplier;
import ru.tinkoff.qa.neptune.selenium.functions.target.locator.alert.GetAlertSupplier;
import ru.tinkoff.qa.neptune.selenium.functions.target.locator.content.DefaultContentSupplier;
import ru.tinkoff.qa.neptune.selenium.functions.target.locator.frame.Frame;
import ru.tinkoff.qa.neptune.selenium.functions.target.locator.frame.GetFrameSupplier;
import ru.tinkoff.qa.neptune.selenium.functions.target.locator.frame.parent.ParentFrameSupplier;
import ru.tinkoff.qa.neptune.selenium.functions.target.locator.window.GetWindowSupplier;
import ru.tinkoff.qa.neptune.selenium.functions.target.locator.window.Window;
import ru.tinkoff.qa.neptune.selenium.functions.value.SequentialGetValueSupplier;
import ru.tinkoff.qa.neptune.selenium.functions.windows.CloseWindowActionSupplier;
import ru.tinkoff.qa.neptune.selenium.functions.windows.GetWindowPositionSupplier;
import ru.tinkoff.qa.neptune.selenium.functions.windows.GetWindowSizeSupplier;
import ru.tinkoff.qa.neptune.selenium.functions.windows.GetWindowTitleSupplier;
import ru.tinkoff.qa.neptune.selenium.properties.SupportedWebDrivers;

import java.net.URL;
import java.time.Duration;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Supplier;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static java.time.Duration.ofMillis;
import static java.util.Arrays.asList;
import static ru.tinkoff.qa.neptune.selenium.CurrentContentFunction.currentContent;
import static ru.tinkoff.qa.neptune.selenium.functions.cookies.RemoveCookiesActionSupplier.deleteCookies;
import static ru.tinkoff.qa.neptune.selenium.functions.elements.GetElementPositionSupplier.positionOfElement;
import static ru.tinkoff.qa.neptune.selenium.functions.elements.GetElementSizeSupplier.elementSize;
import static ru.tinkoff.qa.neptune.selenium.functions.navigation.Back.back;
import static ru.tinkoff.qa.neptune.selenium.functions.navigation.Forward.forward;
import static ru.tinkoff.qa.neptune.selenium.functions.navigation.GetCurrentUrlSupplier.currentUrl;
import static ru.tinkoff.qa.neptune.selenium.functions.navigation.Refresh.refreshWindow;
import static ru.tinkoff.qa.neptune.selenium.functions.navigation.ToUrl.toUrl;
import static ru.tinkoff.qa.neptune.selenium.functions.target.locator.SwitchActionSupplier.to;
import static ru.tinkoff.qa.neptune.selenium.functions.windows.GetWindowTitleSupplier.titleOf;
import static ru.tinkoff.qa.neptune.selenium.functions.windows.SetWindowPositionSupplier.setPositionOf;
import static ru.tinkoff.qa.neptune.selenium.functions.windows.SetWindowPositionSupplier.setWindowPosition;

@CreateWith(provider = SeleniumParameterProvider.class)
public class SeleniumStepContext extends Context<SeleniumStepContext> implements WrapsDriver, ContextRefreshable,
        TakesScreenshot, Stoppable {

    private static final SeleniumStepContext context = getInstance(SeleniumStepContext.class);
    private final WrappedWebDriver wrappedWebDriver;

    public SeleniumStepContext(SupportedWebDrivers supportedWebDriver) {
        this.wrappedWebDriver = new WrappedWebDriver(supportedWebDriver);
    }

    public static SeleniumStepContext inBrowser() {
        return context;
    }

    @Override
    public WebDriver getWrappedDriver() {
        return wrappedWebDriver.getWrappedDriver();
    }

    public <R extends SearchContext> R find(SearchSupplier<R> what) {
        return what.get().compose(currentContent()).apply(this);
    }

    public <R extends SearchContext> List<R> find(MultipleSearchSupplier<R> what) {
        return what.get().compose(currentContent()).apply(this);
    }

    public SeleniumStepContext click(ClickActionSupplier clickActionSupplier) {
        clickActionSupplier.get().accept(this);
        return this;
    }

    public <T> T getValue(SequentialGetValueSupplier<T> getValueSupplier) {
        return getValueSupplier.get().apply(this);
    }

    public SeleniumStepContext edit(EditActionSupplier editActionSupplier) {
        editActionSupplier.get().accept(this);
        return this;
    }

    /**
     * Returns a result of js evaluation.
     *
     * @param javaScriptResultSupplier is which script and how to execute it
     * @return a result of js evaluation
     */
    public Object getEvaluationOf(GetJavaScriptResultSupplier javaScriptResultSupplier) {
        return javaScriptResultSupplier.get().apply(this);
    }

    public SeleniumStepContext alert(AlertActionSupplier alertActionSupplier) {
        alertActionSupplier.get().accept(this);
        return this;
    }


    @Override
    public void refreshContext() {
        wrappedWebDriver.refreshContext();
    }


    @Override
    public <X> X getScreenshotAs(OutputType<X> target) throws WebDriverException {
        return ((TakesScreenshot) getWrappedDriver()).getScreenshotAs(target);
    }

    @Override
    public void stop() {
        wrappedWebDriver.shutDown();
    }

    /**
     * Checks whenever an element is present or not
     *
     * @param toFind is how to find the element
     * @return is element present|visible or not
     */
    public boolean presenceOf(SearchSupplier<?> toFind) {
        checkNotNull(toFind, "It is necessary to define how to find an element to be present");
        return super.presenceOf(toFind.get().compose(currentContent()),
                NoSuchElementException.class,
                StaleElementReferenceException.class);
    }

    /**
     * Checks whenever an element is present or not
     *
     * @param toFind       is how to find the element
     * @param errorMessage is a message of {@link NoSuchElementException} to be thrown when element is not present
     * @return is element present|visible or not
     */
    public boolean presenceOf(SearchSupplier<?> toFind, String errorMessage) {
        checkNotNull(toFind, "It is necessary to define how to find an element to be present");
        return super.presenceOf(toFind.get().compose(currentContent()),
                () -> new NoSuchElementException(errorMessage),
                NoSuchElementException.class,
                StaleElementReferenceException.class);
    }

    /**
     * Checks whenever elements is present or not
     *
     * @param toFind is how to find elements
     * @return are elements present|visible or not
     */
    public boolean presenceOf(MultipleSearchSupplier<?> toFind) {
        checkNotNull(toFind, "It is necessary to define how to find elements to be present");
        return super.presenceOf(toFind.get().compose(currentContent()),
                NoSuchElementException.class,
                StaleElementReferenceException.class);
    }

    /**
     * Checks whenever elements is present or not
     *
     * @param toFind       is how to find elements
     * @param errorMessage is a message of {@link NoSuchElementException} to be thrown when elements are not present
     * @return are elements present|visible or not
     */
    public boolean presenceOf(MultipleSearchSupplier<?> toFind, String errorMessage) {
        checkNotNull(toFind, "It is necessary to define how to find elements to be present");
        return super.presenceOf(toFind.get().compose(currentContent()),
                () -> new NoSuchElementException(errorMessage),
                NoSuchElementException.class,
                StaleElementReferenceException.class);
    }

    /**
     * Checks whenever an element is absent or not
     *
     * @param toBeAbsent is how to find the element
     * @param timeOut    is a time to wait for the element is absent.
     * @return is element absent or not
     */
    public boolean absenceOf(SearchSupplier<?> toBeAbsent,
                             Duration timeOut) {
        checkNotNull(toBeAbsent, "It is necessary to define how to find an element to be absent");
        return super.absenceOf(toBeAbsent
                        .clone()
                        .timeOut(ofMillis(0))
                        .get()
                        .compose(currentContent()),
                timeOut);
    }

    /**
     * Checks whenever an element is absent or not
     *
     * @param toBeAbsent       is how to find the element
     * @param timeOut          is a time to wait for the element is absent.
     * @param exceptionMessage is a message of {@link IllegalStateException} to be thrown when the element is present
     * @return is element absent or not
     */
    public boolean absenceOf(SearchSupplier<?> toBeAbsent,
                             Duration timeOut,
                             String exceptionMessage) {
        checkNotNull(toBeAbsent, "It is necessary to define how to find an element to be absent");
        return super.absenceOf(toBeAbsent
                        .clone()
                        .timeOut(ofMillis(0))
                        .get()
                        .compose(currentContent()),
                timeOut,
                exceptionMessage);
    }

    /**
     * Checks whenever elements are absent or not
     *
     * @param toBeAbsent is how to find elements
     * @param timeOut    is a time to wait for elements are absent.
     * @return are elements absent or not
     */
    public boolean absenceOf(MultipleSearchSupplier<?> toBeAbsent,
                             Duration timeOut) {
        checkNotNull(toBeAbsent, "It is necessary to define how to find elements to be absent");
        return super.absenceOf(toBeAbsent
                        .clone()
                        .timeOut(ofMillis(0))
                        .get()
                        .compose(currentContent()),
                timeOut);
    }

    /**
     * Checks whenever an element is absent or not
     *
     * @param toBeAbsent       is how to find elements
     * @param timeOut          is a time to wait for elements are absent.
     * @param exceptionMessage is a message of {@link IllegalStateException} to be thrown when elements are present
     * @return are elements absent or not
     */
    public boolean absenceOf(MultipleSearchSupplier<?> toBeAbsent,
                             Duration timeOut,
                             String exceptionMessage) {
        checkNotNull(toBeAbsent, "It is necessary to define how to find elements to be absent");
        return super.absenceOf(toBeAbsent
                        .clone()
                        .timeOut(ofMillis(0))
                        .get()
                        .compose(currentContent()),
                timeOut,
                exceptionMessage);
    }

    /**
     * Cleans browser's cookie jar.
     *
     * @return self-reference
     */
    public SeleniumStepContext removeCookies() {
        deleteCookies().get().accept(this);
        return this;
    }

    /**
     * Cleans browser's cookie jar.
     *
     * @param timeToFindCookies time of the waiting for expected cookies are present.
     * @param toBeRemoved       which cookies should be deleted
     * @return self-reference
     */
    @SafeVarargs
    public final SeleniumStepContext removeCookies(Duration timeToFindCookies,
                                                   Criteria<Cookie>... toBeRemoved) {
        deleteCookies(timeToFindCookies, toBeRemoved).get().accept(this);
        return this;
    }

    /**
     * Cleans browser's cookie jar.
     *
     * @param toBeRemoved which cookies should be deleted
     * @return self-reference
     */
    @SafeVarargs
    public final SeleniumStepContext removeCookies(Criteria<Cookie>... toBeRemoved) {
        return removeCookies(null, toBeRemoved);
    }

    /**
     * Cleans browser's cookie jar.
     *
     * @param cookies cookies that should be deleted
     * @return self-reference
     */
    public SeleniumStepContext removeCookies(Collection<Cookie> cookies) {
        deleteCookies(cookies).get().accept(this);
        return this;
    }

    /**
     * Cleans browser's cookie jar.
     *
     * @param cookies cookies that should be deleted
     * @return self-reference
     */
    public SeleniumStepContext removeCookies(Cookie... cookies) {
        return removeCookies(asList(cookies));
    }

    /**
     * Adds cookies to browser's cookie jar.
     *
     * @param cookies to be added
     * @return self-reference
     */
    public SeleniumStepContext addCookies(Collection<Cookie> cookies) {
        AddCookiesActionSupplier.addCookies(cookies).get().accept(this);
        return this;
    }

    /**
     * Adds cookies to browser's cookie jar.
     *
     * @param cookies to be added
     * @return self-reference
     */
    public SeleniumStepContext addCookies(Cookie... cookies) {
        return addCookies(asList(cookies));
    }

    /**
     * Gets URL of a page that loaded in the browser window/tab that active currently.
     *
     * @return string URL of a loaded page
     */
    public String getCurrentUrl() {
        return currentUrl().get().apply(this);
    }

    /**
     * Gets URL of a page that loaded in the browser window/tab.
     *
     * @param getWindow is how to find/get target window/tab to get an URL from
     * @return string URL of a loaded page
     */
    public String getCurrentUrl(GetWindowSupplier getWindow) {
        return currentUrl(getWindow).get().apply(this);
    }

    /**
     * Gets URL of a page that loaded in the browser window/tab.
     *
     * @param window is a window to get an URL from
     * @return {@link URL} of a loaded page
     */
    public String getCurrentUrl(Window window) {
        return currentUrl(window).get().apply(this);
    }

    /**
     * Performs navigation back in the browser window/tab that active currently.
     *
     * @return self-reference
     */
    public SeleniumStepContext navigateBack() {
        back().get().accept(this);
        return this;
    }

    /**
     * Performs navigation back in the browser window/tab.
     *
     * @param windowSupplier is how to find/get target window/tab
     * @return self-reference
     */
    public SeleniumStepContext navigateBack(GetWindowSupplier windowSupplier) {
        back(windowSupplier).get().accept(this);
        return this;
    }

    /**
     * Performs navigation back in the browser window/tab.
     *
     * @param window is a target window
     * @return self-reference
     */
    public SeleniumStepContext navigateBack(Window window) {
        back(window).get().accept(this);
        return this;
    }

    /**
     * Performs navigation forward in the browser window/tab that active currently.
     *
     * @return self-reference
     */
    public SeleniumStepContext navigateForward() {
        forward().get().accept(this);
        return this;
    }

    /**
     * Performs navigation forward in the browser window/tab.
     *
     * @param windowSupplier is how to find/get target window/tab
     * @return self-reference
     */
    public SeleniumStepContext navigateForward(GetWindowSupplier windowSupplier) {
        forward(windowSupplier).get().accept(this);
        return this;
    }

    /**
     * Performs navigation forward in the browser window/tab.
     *
     * @param window is a target window
     * @return self-reference
     */
    public SeleniumStepContext navigateForward(Window window) {
        forward(window).get().accept(this);
        return this;
    }


    /**
     * Performs the refreshing of the browser window/tab that active currently.
     *
     * @return self-reference
     */
    public SeleniumStepContext refresh() {
        refreshWindow().get().accept(this);
        return this;
    }

    /**
     * Performs the refreshing of the browser window/tab.
     *
     * @param windowSupplier is how to find/get target window/tab
     * @return self-reference
     */
    public SeleniumStepContext refresh(GetWindowSupplier windowSupplier) {
        refreshWindow(windowSupplier).get().accept(this);
        return this;
    }

    /**
     * Performs the refreshing of the browser window/tab.
     *
     * @param window is a target window
     * @return self-reference
     */
    public SeleniumStepContext refresh(Window window) {
        refreshWindow(window).get().accept(this);
        return this;
    }


    /**
     * Performs navigation in the browser window/tab that active currently.
     *
     * @param url is a target URL
     * @return self-reference
     */
    public SeleniumStepContext navigateTo(URL url) {
        toUrl(url).get().accept(this);
        return this;
    }

    /**
     * Performs navigation in the browser window/tab.
     *
     * @param url is a target URL
     * @param in  is how to find/get target window/tab
     * @return self-reference
     */
    public SeleniumStepContext navigateTo(URL url, GetWindowSupplier in) {
        toUrl(in, url).get().accept(this);
        return this;
    }

    /**
     * Performs navigation in the browser window/tab.
     *
     * @param url is a target URL
     * @param in  is a target window
     * @return self-reference
     */
    public SeleniumStepContext navigateTo(URL url, Window in) {
        toUrl(in, url).get().accept(this);
        return this;
    }

    /**
     * Performs navigation in the default browser window/tab that active currently.
     *
     * @param url is a string value the url to navigate to. Also it may be a path relative to base web driver url.
     *            It is possible when ability to navigate by relative path is enabled
     * @return self-reference
     * @see ru.tinkoff.qa.neptune.selenium.properties.URLProperties#BASE_WEB_DRIVER_URL_PROPERTY
     * @see ru.tinkoff.qa.neptune.selenium.properties.SessionFlagProperties#ENABLE_ABILITY_TO_NAVIGATE_BY_RELATIVE_URL
     */
    public SeleniumStepContext navigateTo(String url) {
        toUrl(url).get().accept(this);
        return this;
    }

    /**
     * Performs navigation in the browser window/tab.
     *
     * @param url is a string value the url to navigate to. Also it may be a path relative to base web driver url.
     *            It is possible when ability to navigate by relative path is enabled
     * @param in  is how to find/get target window/tab
     * @return self-reference
     * @see ru.tinkoff.qa.neptune.selenium.properties.URLProperties#BASE_WEB_DRIVER_URL_PROPERTY
     * @see ru.tinkoff.qa.neptune.selenium.properties.SessionFlagProperties#ENABLE_ABILITY_TO_NAVIGATE_BY_RELATIVE_URL
     */
    public SeleniumStepContext navigateTo(String url, GetWindowSupplier in) {
        toUrl(in, url).get().accept(this);
        return this;
    }

    /**
     * Performs navigation in the browser window/tab.
     *
     * @param url is a string value the url to navigate to. Also it may be a path relative to base web driver url.
     *            It is possible when ability to navigate by relative path is enabled
     * @param in  is a target window
     * @return self-reference
     * @see ru.tinkoff.qa.neptune.selenium.properties.URLProperties#BASE_WEB_DRIVER_URL_PROPERTY
     * @see ru.tinkoff.qa.neptune.selenium.properties.SessionFlagProperties#ENABLE_ABILITY_TO_NAVIGATE_BY_RELATIVE_URL
     */
    public SeleniumStepContext navigateTo(String url, Window in) {
        toUrl(in, url).get().accept(this);
        return this;
    }

    /**
     * Performs the switching to active element
     * Taken from Selenium documentation:
     * <p>
     * Switches to the element that currently has focus within the document currently "switched to",
     * or the body element if this cannot be detected. This matches the semantics of calling
     * "document.activeElement" in Javascript.
     * <p>
     * Returns the WebElement with focus, or the body element if no element with focus can be
     * detected.
     * </p>
     *
     * @param getActiveElement is how to get the active element
     * @return self-reference
     */
    public SeleniumStepContext switchTo(GetActiveElementSupplier getActiveElement) {
        to(getActiveElement).get().accept(this);
        return this;
    }

    /**
     * Performs the switching to an alert
     *
     * @param getAlert is how to get the alert
     * @return self-reference
     */
    public SeleniumStepContext switchTo(GetAlertSupplier getAlert) {
        to(getAlert).get().accept(this);
        return this;
    }

    /**
     * Performs the switching to default content.
     * Taken from Selenium documentation:
     * <p>
     * Selects either the first frame on the page, or the main document when a page contains
     * iframes.
     * <p>
     * Returns the instant of {@link WebDriver} focused on the top window/first frame.
     * </p>
     *
     * @param getDefaultContent is how to get default content
     * @return self-reference
     */
    public SeleniumStepContext switchTo(DefaultContentSupplier getDefaultContent) {
        to(getDefaultContent).get().accept(this);
        return this;
    }

    /**
     * Performs the switching to parent frame.
     * Taken from Selenium documentation:
     * <p>
     * Change focus to the parent context. If the current context is the top level browsing context,
     * the context remains unchanged.
     * </p>
     *
     * @param getParentFrame is how to get the parent frame
     * @return self-reference
     */
    public SeleniumStepContext switchTo(ParentFrameSupplier getParentFrame) {
        to(getParentFrame).get().accept(this);
        return this;
    }

    /**
     * Performs the switching to the frame.
     *
     * @param getFrame is how to get the frame
     * @return self-reference
     */
    public SeleniumStepContext switchTo(GetFrameSupplier getFrame) {
        to(getFrame).get().accept(this);
        return this;
    }

    /**
     * Performs the switching to the frame.
     *
     * @param frame is the frame to switch to
     * @return self-reference
     */
    public SeleniumStepContext switchTo(Frame frame) {
        to(frame).get().accept(this);
        return this;
    }

    /**
     * Performs the switching to the browser window/tab.
     *
     * @param getWindow is how to get the window/tab
     * @return self-reference
     */
    public SeleniumStepContext switchTo(GetWindowSupplier getWindow) {
        to(getWindow).get().accept(this);
        return this;
    }

    /**
     * Performs the switching to the browser window/tab.
     *
     * @param window is the browser/window to switch to
     * @return self-reference
     */
    public SeleniumStepContext switchTo(Window window) {
        to(window).get().accept(this);
        return this;
    }

    /**
     * Returns an instance of {@link Dimension} as a representation of size of the browser window/tab that active currently
     *
     * @return an instance of {@link Dimension}
     */
    public Dimension windowSize() {
        return GetWindowSizeSupplier.windowSize().get().apply(this);
    }

    /**
     * Returns an instance of {@link Dimension} as a representation of size of the element/widget
     *
     * @param getElement is how to find the target web element/widget
     * @return an instance of {@link Dimension}
     */
    public Dimension sizeOf(SearchSupplier<?> getElement) {
        return elementSize(getElement).get().apply(this);
    }

    /**
     * Returns an instance of {@link Dimension} as a representation of size of the browser window/tab
     *
     * @param getWindow is how to find the target browser window/tab
     * @return an instance of {@link Dimension}
     */
    public Dimension sizeOf(GetWindowSupplier getWindow) {
        return GetWindowSizeSupplier.windowSize(getWindow).get().apply(this);
    }

    /**
     * Returns an instance of {@link Dimension} as a representation of size of the widget
     *
     * @param widget is the target widget
     * @return an instance of {@link Dimension}
     */
    public Dimension sizeOf(Widget widget) {
        return elementSize(widget).get().apply(this);
    }

    /**
     * Returns an instance of {@link Dimension} as a representation of size of the web element
     *
     * @param element is the target web element
     * @return an instance of {@link Dimension}
     */
    public Dimension sizeOf(WebElement element) {
        return elementSize(element).get().apply(this);
    }

    /**
     * Returns an instance of {@link Dimension} as a representation of size of the browser window/tab
     *
     * @param window is the target browser window/tab
     * @return an instance of {@link Dimension}
     */
    public Dimension sizeOf(Window window) {
        return GetWindowSizeSupplier.windowSize(window).get().apply(this);
    }


    /**
     * Returns an instance of {@link Point} as a representation of position of the browser window/tab that active currently
     *
     * @return an instance of {@link Point}
     */
    public Point windowPosition() {
        return GetWindowPositionSupplier.windowPosition().get().apply(this);
    }

    /**
     * Returns an instance of {@link Point} as a representation of position of the element/widget
     *
     * @param getElement is how to find the target web element/widget
     * @return an instance of {@link Point}
     */
    public Point positionOf(SearchSupplier<?> getElement) {
        return positionOfElement(getElement).get().apply(this);
    }

    /**
     * Returns an instance of {@link Point} as a representation of position of the browser window/tab
     *
     * @param getWindow is how to find the target browser window/tab
     * @return an instance of {@link Point}
     */
    public Point positionOf(GetWindowSupplier getWindow) {
        return GetWindowPositionSupplier.positionOf(getWindow).get().apply(this);
    }

    /**
     * Returns an instance of {@link Point} as a representation of position of the widget
     *
     * @param widget is the target widget
     * @return an instance of {@link Point}
     */
    public Point positionOf(Widget widget) {
        return positionOfElement(widget).get().apply(this);
    }

    /**
     * Returns an instance of {@link Point} as a representation of position of the web element
     *
     * @param element is the target web element
     * @return an instance of {@link Point}
     */
    public Point positionOf(WebElement element) {
        return positionOfElement(element).get().apply(this);
    }

    /**
     * Returns an instance of {@link Point} as a representation of position of the browser window/tab
     *
     * @param window is the target browser window/tab
     * @return an instance of {@link Point}
     */
    public Point positionOf(Window window) {
        return GetWindowPositionSupplier.positionOf(window).get().apply(this);
    }

    /**
     * Performs the closing of the browser window/tab that active currently
     *
     * @return self-reference
     */
    public SeleniumStepContext closeWindow() {
        CloseWindowActionSupplier.closeWindow().get().accept(this);
        return this;
    }

    /**
     * Performs the closing of the browser window/tab.
     *
     * @param supplier is how to find the browser window/tab to close it.
     * @return self-reference
     */
    public SeleniumStepContext closeWindow(GetWindowSupplier supplier) {
        CloseWindowActionSupplier.closeWindow(supplier).get().accept(this);
        return this;
    }

    /**
     * Performs the closing of the browser window/tab.
     *
     * @param window the browser window/tab to close
     * @return self-reference
     */
    public SeleniumStepContext closeWindow(Window window) {
        CloseWindowActionSupplier.closeWindow(window).get().accept(this);
        return this;
    }


    /**
     * Performs the setting to new position of the browser window/tab that active currently
     *
     * @param x is the X-coordinate of upper left corner of the window/tab
     * @param y is the Y-coordinate of upper left corner of the window/tab
     * @return self-reference
     */
    public SeleniumStepContext changeWindowPosition(int x, int y) {
        setWindowPosition(new Point(x, y)).get().accept(this);
        return this;
    }

    /**
     * Performs the setting to new position of the browser window/tab.
     *
     * @param x        is the X-coordinate of upper left corner of the window/tab
     * @param y        is the Y-coordinate of upper left corner of the window/tab
     * @param supplier is how to find the browser window/tab
     * @return self-reference
     */
    public SeleniumStepContext changeWindowPosition(GetWindowSupplier supplier, int x, int y) {
        setPositionOf(supplier, new Point(x, y)).get().accept(this);
        return this;
    }

    /**
     * Performs the setting to new position of the browser window/tab.
     *
     * @param x      is the X-coordinate of upper left corner of the window/tab
     * @param y      is the Y-coordinate of upper left corner of the window/tab
     * @param window the browser window/tab
     * @return self-reference
     */
    public SeleniumStepContext changeWindowPosition(Window window, int x, int y) {
        setPositionOf(window, new Point(x, y)).get().accept(this);
        return this;
    }


    /**
     * Returns title of the browser window/tab that active currently.
     *
     * @return window title
     */
    public String windowTitle() {
        return GetWindowTitleSupplier.windowTitle().get().apply(this);
    }

    /**
     * Returns title of the browser window/tab.
     *
     * @param supplier is how to find the browser window/tab
     * @return window title
     */
    public String windowTitle(GetWindowSupplier supplier) {
        return titleOf(supplier).get().apply(this);
    }

    /**
     * Returns title of the browser window/tab.
     *
     * @param window is the target browser window/tab
     * @return window title
     */
    public String windowTitle(Window window) {
        return titleOf(window).get().apply(this);
    }


    /**
     * Returns a set of browser {@link Cookie}
     *
     * @param getCookies is how to get find cookies
     * @return a set of {@link Cookie}
     */
    public Set<Cookie> get(GetSeleniumCookieSupplier getCookies) {
        return getCookies.get().apply(this);
    }


    /**
     * Returns {@link Alert} as as a representation of a browser alert
     *
     * @param getAlert is how to get the alert
     * @return an instance of {@link Alert}
     */
    public Alert get(GetAlertSupplier getAlert) {
        return getAlert.get().apply(this);
    }

    /**
     * Returns {@link Frame} as as a representation of an iframe
     *
     * @param getFrame is how to get the frame
     * @return an instance of {@link Frame}
     */
    public Frame get(GetFrameSupplier getFrame) {
        return getFrame.get().apply(this);
    }


    /**
     * Returns {@link Window} as a representation of a browser window/tab
     *
     * @param getWindow is a description of a window to get
     * @return an instance of {@link Window}
     */
    public Window get(GetWindowSupplier getWindow) {
        return getWindow.get().apply(this);
    }


    /**
     * This method was added for backward compatibility temporary
     */
    @Deprecated(since = "0.11.2-ALPHA")
    public <T> T get(Function<SeleniumStepContext, T> function) {
        checkArgument(Objects.nonNull(function), "The function is not defined");
        return function.apply(this);
    }

    /**
     * This method was added for backward compatibility temporary
     */
    @Deprecated(since = "0.11.2-ALPHA")
    public <T> T get(Supplier<Function<SeleniumStepContext, T>> functionSupplier) {
        checkNotNull(functionSupplier, "Supplier of the value to get was not defined");
        return this.get(functionSupplier.get());
    }

    /**
     * This method was added for backward compatibility temporary
     */
    @Deprecated(since = "0.11.2-ALPHA")
    public SeleniumStepContext perform(StepAction<SeleniumStepContext> actionConsumer) {
        checkArgument(Objects.nonNull(actionConsumer), "Action is not defined");
        actionConsumer.accept(this);
        return this;
    }

    /**
     * This method was added for backward compatibility temporary
     */
    @Deprecated(since = "0.11.2-ALPHA")
    public SeleniumStepContext perform(SequentialActionSupplier<SeleniumStepContext, ?, ?> actionSupplier) {
        checkNotNull(actionSupplier, "Supplier of the action was not defined");
        return this.perform(actionSupplier.get());
    }
}
