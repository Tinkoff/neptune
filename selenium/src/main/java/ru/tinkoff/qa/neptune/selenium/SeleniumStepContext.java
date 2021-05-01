package ru.tinkoff.qa.neptune.selenium;

import com.browserup.bup.BrowserUpProxy;
import com.browserup.harreader.model.HarEntry;
import org.openqa.selenium.*;
import ru.tinkoff.qa.neptune.core.api.cleaning.ContextRefreshable;
import ru.tinkoff.qa.neptune.core.api.cleaning.Stoppable;
import ru.tinkoff.qa.neptune.core.api.steps.Criteria;
import ru.tinkoff.qa.neptune.core.api.steps.SequentialGetStepSupplier;
import ru.tinkoff.qa.neptune.core.api.steps.context.Context;
import ru.tinkoff.qa.neptune.core.api.steps.context.CreateWith;
import ru.tinkoff.qa.neptune.selenium.api.widget.*;
import ru.tinkoff.qa.neptune.selenium.functions.browser.proxy.BrowserProxyGetStepSupplier;
import ru.tinkoff.qa.neptune.selenium.functions.cookies.AddCookiesActionSupplier;
import ru.tinkoff.qa.neptune.selenium.functions.cookies.GetSeleniumCookieSupplier;
import ru.tinkoff.qa.neptune.selenium.functions.expand.CollapseActionSupplier;
import ru.tinkoff.qa.neptune.selenium.functions.expand.ExpandActionSupplier;
import ru.tinkoff.qa.neptune.selenium.functions.intreraction.InteractiveAction;
import ru.tinkoff.qa.neptune.selenium.functions.java.script.GetJavaScriptResultSupplier;
import ru.tinkoff.qa.neptune.selenium.functions.searching.MultipleSearchSupplier;
import ru.tinkoff.qa.neptune.selenium.functions.searching.SearchSupplier;
import ru.tinkoff.qa.neptune.selenium.functions.target.locator.active.element.GetActiveElementSupplier;
import ru.tinkoff.qa.neptune.selenium.functions.target.locator.alert.GetAlertSupplier;
import ru.tinkoff.qa.neptune.selenium.functions.target.locator.content.DefaultContentSupplier;
import ru.tinkoff.qa.neptune.selenium.functions.target.locator.frame.Frame;
import ru.tinkoff.qa.neptune.selenium.functions.target.locator.frame.GetFrameSupplier;
import ru.tinkoff.qa.neptune.selenium.functions.target.locator.frame.parent.ParentFrameSupplier;
import ru.tinkoff.qa.neptune.selenium.functions.target.locator.window.GetWindowSupplier;
import ru.tinkoff.qa.neptune.selenium.functions.target.locator.window.Window;
import ru.tinkoff.qa.neptune.selenium.functions.windows.*;
import ru.tinkoff.qa.neptune.selenium.properties.SupportedWebDrivers;

import java.net.URL;
import java.time.Duration;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static java.util.Arrays.asList;
import static java.util.Optional.ofNullable;
import static ru.tinkoff.qa.neptune.selenium.BrowserProxy.getCurrentProxy;
import static ru.tinkoff.qa.neptune.selenium.content.management.ContentManagementCommand.getCurrentCommand;
import static ru.tinkoff.qa.neptune.selenium.functions.click.ClickActionSupplier.on;
import static ru.tinkoff.qa.neptune.selenium.functions.cookies.RemoveCookiesActionSupplier.deleteCookies;
import static ru.tinkoff.qa.neptune.selenium.functions.edit.EditActionSupplier.valueOfThe;
import static ru.tinkoff.qa.neptune.selenium.functions.elements.GetElementPositionSupplier.positionOfElement;
import static ru.tinkoff.qa.neptune.selenium.functions.elements.GetElementSizeSupplier.elementSize;
import static ru.tinkoff.qa.neptune.selenium.functions.navigation.Back.back;
import static ru.tinkoff.qa.neptune.selenium.functions.navigation.Forward.forward;
import static ru.tinkoff.qa.neptune.selenium.functions.navigation.GetCurrentUrlSupplier.currentUrl;
import static ru.tinkoff.qa.neptune.selenium.functions.navigation.Refresh.refreshWindow;
import static ru.tinkoff.qa.neptune.selenium.functions.navigation.ToUrl.toUrl;
import static ru.tinkoff.qa.neptune.selenium.functions.target.locator.SwitchActionSupplier.to;
import static ru.tinkoff.qa.neptune.selenium.functions.target.locator.alert.AcceptAlertActionSupplier.acceptAlert;
import static ru.tinkoff.qa.neptune.selenium.functions.target.locator.alert.DismissAlertActionSupplier.dismissAlert;
import static ru.tinkoff.qa.neptune.selenium.functions.target.locator.alert.SendKeysToAlertActionSupplier.sendKeysToAlert;
import static ru.tinkoff.qa.neptune.selenium.functions.value.SequentialGetAttributeValueSupplier.attributeValue;
import static ru.tinkoff.qa.neptune.selenium.functions.value.SequentialGetCSSValueSupplier.cssValue;
import static ru.tinkoff.qa.neptune.selenium.functions.value.SequentialGetValueSupplier.ofThe;
import static ru.tinkoff.qa.neptune.selenium.functions.windows.GetWindowTitleSupplier.titleOf;
import static ru.tinkoff.qa.neptune.selenium.functions.windows.SetWindowPositionSupplier.setPositionOf;
import static ru.tinkoff.qa.neptune.selenium.functions.windows.SetWindowPositionSupplier.setWindowPosition;
import static ru.tinkoff.qa.neptune.selenium.functions.windows.SetWindowSizeSupplier.setSizeOf;
import static ru.tinkoff.qa.neptune.selenium.functions.windows.SetWindowSizeSupplier.setWindowSize;

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

    private void changeContentIfNecessary() {
        ofNullable(getCurrentCommand()).ifPresent(
                contentManagementCommand -> {
                    getWrappedDriver().getWindowHandles();
                    perform(contentManagementCommand);
                }
        );
    }

    @Override
    public WebDriver getWrappedDriver() {
        return wrappedWebDriver.getWrappedDriver();
    }

    public <R extends SearchContext> R find(SearchSupplier<R> what) {
        changeContentIfNecessary();
        return get(what);
    }

    public <R extends SearchContext> List<R> find(MultipleSearchSupplier<R> what) {
        changeContentIfNecessary();
        return get(what);
    }

    /**
     * Performs the clicking on a clickable widget
     *
     * @param toFind is description of a widget to be clicked
     * @param <T>    is a type of a clickable {@link Widget}
     * @return self-reference
     */
    public <T extends Widget & Clickable> SeleniumStepContext click(SearchSupplier<T> toFind) {
        changeContentIfNecessary();
        perform(on(toFind));
        return this;
    }

    /**
     * Performs the clicking on a web element
     *
     * @param toFind is description of a web element to be clicked
     * @return self-reference
     */
    public SeleniumStepContext click(SequentialGetStepSupplier<Object, WebElement, ?, ?, ?> toFind) {
        changeContentIfNecessary();
        perform(on(toFind));
        return this;
    }

    /**
     * Performs the clicking on a clickable widget
     *
     * @param widget is a widget to be clicked
     * @param <T>    is a type of a clickable {@link Widget}
     * @return self-reference
     */
    public <T extends Widget & Clickable> SeleniumStepContext click(T widget) {
        changeContentIfNecessary();
        perform(on(widget));
        return this;
    }

    /**
     * Performs the clicking on a web element
     *
     * @param element is a web element to be clicked
     * @return self-reference
     */
    public SeleniumStepContext click(WebElement element) {
        changeContentIfNecessary();
        perform(on(element));
        return this;
    }


    /**
     * Performs the expanding on an expandable widget
     *
     * @param toFind is description of a widget to be expanded
     * @param <T>    is a type of a expandable {@link Widget}
     * @return self-reference
     */
    public <T extends Widget & Expandable> SeleniumStepContext expand(SearchSupplier<T> toFind) {
        changeContentIfNecessary();
        perform(ExpandActionSupplier.expand(toFind));
        return this;
    }

    /**
     * Performs the expanding on an expandable widget
     *
     * @param widget to be expanded
     * @param <T>    is a type of a expandable {@link Widget}
     * @return self-reference
     */
    public <T extends Widget & Expandable> SeleniumStepContext expand(T widget) {
        changeContentIfNecessary();
        perform(ExpandActionSupplier.expand(widget));
        return this;
    }

    /**
     * Performs the collapsing on an expandable/collapsable widget
     *
     * @param toFind is description of a widget to be collapsed
     * @param <T>    is a type of an expandable/collapsable {@link Widget}
     * @return self-reference
     */
    public <T extends Widget & Expandable> SeleniumStepContext collapse(SearchSupplier<T> toFind) {
        changeContentIfNecessary();
        perform(CollapseActionSupplier.collapse(toFind));
        return this;
    }

    /**
     * Performs the collapsing on an expandable/collapsable widget
     *
     * @param widget to be collapsed
     * @param <T>    is a type of an expandable/collapsable {@link Widget}
     * @return self-reference
     */
    public <T extends Widget & Expandable> SeleniumStepContext collapse(T widget) {
        changeContentIfNecessary();
        perform(CollapseActionSupplier.collapse(widget));
        return this;
    }


    /**
     * Performs the editing of an editable widget.
     *
     * @param toFind is description of a widget to be edited
     * @param value  is value used to perform the editing
     * @param <R>    is a type of value used to perform the editing
     * @param <T>    is a type of editable {@link Widget}
     * @return self-reference
     */
    public <R, T extends Widget & Editable<R>> SeleniumStepContext edit(SearchSupplier<T> toFind, R value) {
        changeContentIfNecessary();
        perform(valueOfThe(toFind, value));
        return this;
    }

    /**
     * Performs the editing of an element by the using of a multiple value.
     *
     * @param toFind is description of a widget to be edited
     * @param value  is a sequence of objects which are used to change value of a widget
     * @param <T>    is a type of editable {@link Widget}
     * @return self-reference
     */
    public <R, T extends Editable<List<R>>> SeleniumStepContext edit(SearchSupplier<T> toFind, R... value) {
        checkNotNull(value);
        changeContentIfNecessary();
        checkArgument(value.length > 0, "");
        perform(valueOfThe(toFind, asList(value)));
        return this;
    }

    /**
     * Performs the editing of an editable widget.
     *
     * @param editable is a widget to be edited
     * @param value    is value used to perform the editing
     * @param <R>      is a type of value used to perform the editing
     * @param <T>      is a type of editable {@link Widget}
     * @return self-reference
     */
    public <R, T extends Widget & Editable<R>> SeleniumStepContext edit(T editable, R value) {
        changeContentIfNecessary();
        perform(valueOfThe(editable, value));
        return this;
    }

    /**
     * Performs the editing of an element by the using of a multiple value.
     *
     * @param t     is a widget to be edited
     * @param value is a sequence of objects which are used to change value of a widget
     * @return self-reference
     */
    public <R, T extends Editable<List<R>>> SeleniumStepContext edit(T t, R... value) {
        checkNotNull(value);
        changeContentIfNecessary();
        checkArgument(value.length > 0, "");
        perform(valueOfThe(t, asList(value)));
        return this;
    }

    /**
     * Returns a result of js evaluation.
     *
     * @param javaScriptResultSupplier is which script and how to execute it
     * @return a result of js evaluation
     */
    public Object evaluate(GetJavaScriptResultSupplier javaScriptResultSupplier) {
        changeContentIfNecessary();
        return get(javaScriptResultSupplier);
    }

    /**
     * Returns captured browser network traffic
     *
     * @param browserProxyGetStepSupplier is description of traffic to be returned
     * @return list of proxied requests
     */
    public List<HarEntry> get(BrowserProxyGetStepSupplier browserProxyGetStepSupplier) {
        checkArgument(Objects.nonNull(browserProxyGetStepSupplier), "Browser proxy supplier is not defined");
        changeContentIfNecessary();
        return super.get(browserProxyGetStepSupplier);
    }

    /**
     * Starts over browser traffic recording
     *
     * @return self-reference
     */
    public SeleniumStepContext resetProxyRecording() {
        ofNullable(getCurrentProxy()).ifPresent(BrowserProxy::newHar);
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
     * Checks is element present or not
     *
     * @param toFind is description of an element to be present
     * @return is element present|visible or not
     */
    public boolean presenceOf(SearchSupplier<?> toFind) {
        checkNotNull(toFind, "It is necessary to define how to find an element to be present");
        changeContentIfNecessary();
        return super.presenceOf(toFind,
                NoSuchElementException.class,
                StaleElementReferenceException.class);
    }

    /**
     * Checks is element present. If it is not present then it throws an exception
     *
     * @param toFind is description of an element to be present
     * @return is element present|visible or not
     */
    public boolean presenceOfOrThrow(SearchSupplier<?> toFind) {
        checkNotNull(toFind, "It is necessary to define how to find an element to be present");
        changeContentIfNecessary();
        return super.presenceOfOrThrow(toFind,
                NoSuchElementException.class,
                StaleElementReferenceException.class);
    }

    /**
     * Checks are elements present or not
     *
     * @param toFind is description of elements to be present
     * @return are elements present|visible or not
     */
    public boolean presenceOf(MultipleSearchSupplier<?> toFind) {
        checkNotNull(toFind, "It is necessary to define how to find elements to be present");
        changeContentIfNecessary();
        return super.presenceOf(toFind,
                NoSuchElementException.class,
                StaleElementReferenceException.class);
    }

    /**
     * Checks are elements present or not. If they are not present then it throws an exception
     *
     * @param toFind is description of elements to be present
     * @return are elements present|visible or not
     */
    public boolean presenceOfOrThrow(MultipleSearchSupplier<?> toFind) {
        checkNotNull(toFind, "It is necessary to define how to find elements to be present");
        changeContentIfNecessary();
        return super.presenceOfOrThrow(toFind,
                NoSuchElementException.class,
                StaleElementReferenceException.class);
    }

    /**
     * Checks is window present or not
     *
     * @param getWindow is a description of a window to be present
     * @return is window present or not
     */
    public boolean presenceOf(GetWindowSupplier getWindow) {
        changeContentIfNecessary();
        return super.presenceOf(getWindow, NoSuchWindowException.class);
    }

    /**
     * Checks is window present. If it is not present then it throws an exception.
     *
     * @param getWindow    is a description of a window to be present
     * @return is window present or not
     */
    public boolean presenceOfOrThrow(GetWindowSupplier getWindow) {
        changeContentIfNecessary();
        return super.presenceOfOrThrow(getWindow,
                NoSuchWindowException.class);
    }


    /**
     * Checks is frame present or not.
     *
     * @param getFrame is a description of a frame to be present
     * @return is frame present or not
     */
    public boolean presenceOf(GetFrameSupplier getFrame) {
        changeContentIfNecessary();
        return super.presenceOf(getFrame, NoSuchFrameException.class);
    }

    /**
     * Checks is frame present. If it is not present then it throws an exception.
     *
     * @param getFrame     is a description of a frame to be present
     * @return is frame present or not
     */
    public boolean presenceOfOrThrow(GetFrameSupplier getFrame) {
        changeContentIfNecessary();
        return super.presenceOfOrThrow(getFrame,
                NoSuchFrameException.class);
    }


    /**
     * Checks are cookies present or not
     *
     * @param getCookies is a description of cookies to be present
     * @return are cookies present or not
     */
    public boolean presenceOf(GetSeleniumCookieSupplier getCookies) {
        changeContentIfNecessary();
        return super.presenceOf(getCookies);
    }

    /**
     * Checks are cookies present or not. If they are not present then it throws an exception.
     *
     * @param getCookies   is a description of cookies to be present
     * @return are cookies present or not
     */
    public boolean presenceOfOrThrow(GetSeleniumCookieSupplier getCookies) {
        changeContentIfNecessary();
        return super.presenceOfOrThrow(getCookies);
    }

    /**
     * Checks is alert present or not
     *
     * @param getAlert is a description of an alert to be present
     * @return is alert present or not
     */
    public boolean presenceOf(GetAlertSupplier getAlert) {
        changeContentIfNecessary();
        return super.presenceOf(getAlert, NoAlertPresentException.class);
    }

    /**
     * Checks is alert present. If it is not present then it throws an exception.
     *
     * @param getAlert     is a description of an alert to be present
     * @return is alert present or not
     */
    public boolean presenceOfOrThrow(GetAlertSupplier getAlert) {
        changeContentIfNecessary();
        return super.presenceOfOrThrow(getAlert,
                NoAlertPresentException.class);
    }


    /**
     * Checks are required requests/responses present or not
     *
     * @param getTraffic is a description of requests/responses to be caught
     * @return are requests/responses present or not
     */
    public boolean presenceOf(BrowserProxyGetStepSupplier getTraffic) {
        changeContentIfNecessary();
        return super.presenceOf(getTraffic);
    }

    /**
     * Checks are required requests/responses present. If they are not present then it throws an exception.
     *
     * @param getTraffic   is a description of requests/responses to be caught
     * @return are requests/responses present or not
     */
    public boolean presenceOfOrThrow(BrowserProxyGetStepSupplier getTraffic) {
        changeContentIfNecessary();
        return super.presenceOfOrThrow(getTraffic);
    }


    /**
     * Checks is element absent or not
     *
     * @param toBeAbsent is how to find the element
     * @param timeOut    is a time to wait for the element is absent.
     * @return is element absent or not
     */
    public boolean absenceOf(SearchSupplier<?> toBeAbsent,
                             Duration timeOut) {
        checkNotNull(toBeAbsent, "It is necessary to define how to find an element to be absent");
        changeContentIfNecessary();
        return super.absenceOf(toBeAbsent, timeOut);
    }

    /**
     * Checks is element is absent. If it is present then it throws an exception.
     *
     * @param toBeAbsent       is how to find the element
     * @param timeOut          is a time to wait for the element is absent.
     * @return is element absent or not
     */
    public boolean absenceOfOrThrow(SearchSupplier<?> toBeAbsent,
                                    Duration timeOut) {
        checkNotNull(toBeAbsent, "It is necessary to define how to find an element to be absent");
        changeContentIfNecessary();
        return super.absenceOfOrThrow(toBeAbsent, timeOut);
    }

    /**
     * Checks are elements absent or not
     *
     * @param toBeAbsent is how to find elements
     * @param timeOut    is a time to wait for elements are absent.
     * @return are elements absent or not
     */
    public boolean absenceOf(MultipleSearchSupplier<?> toBeAbsent,
                             Duration timeOut) {
        checkNotNull(toBeAbsent, "It is necessary to define how to find elements to be absent");
        changeContentIfNecessary();
        return super.absenceOf(toBeAbsent, timeOut);
    }

    /**
     * Checks are elements absent. If they are present then it throws an exception.
     *
     * @param toBeAbsent       is how to find elements
     * @param timeOut          is a time to wait for elements are absent.
     * @return are elements absent or not
     */
    public boolean absenceOfOrThrow(MultipleSearchSupplier<?> toBeAbsent,
                                    Duration timeOut) {
        checkNotNull(toBeAbsent, "It is necessary to define how to find elements to be absent");
        changeContentIfNecessary();
        return super.absenceOfOrThrow(toBeAbsent, timeOut);
    }

    /**
     * Checks is window absent or not
     *
     * @param toBeAbsent is a description of a window to be absent
     * @param timeOut    is a time to wait for a window is absent.
     * @return is window absent or not
     */
    public boolean absenceOf(GetWindowSupplier toBeAbsent, Duration timeOut) {
        changeContentIfNecessary();
        return super.absenceOf(toBeAbsent, timeOut);
    }

    /**
     * Checks is window absent. If it is present then it throws an exception.
     *
     * @param toBeAbsent   is a description of a window to be absent
     * @param timeOut      is a time to wait for a window is absent.
     * @return is window absent or not
     */
    public boolean absenceOfOrThrow(GetWindowSupplier toBeAbsent, Duration timeOut) {
        changeContentIfNecessary();
        return super.absenceOfOrThrow(toBeAbsent, timeOut);
    }


    /**
     * Checks is frame absent or not
     *
     * @param toBeAbsent is a description of a frame to be absent
     * @param timeOut    is a time to wait for a frame is absent.
     * @return is frame absent or not
     */
    public boolean absenceOf(GetFrameSupplier toBeAbsent, Duration timeOut) {
        changeContentIfNecessary();
        return super.absenceOf(toBeAbsent, timeOut);
    }

    /**
     * Checks is frame absent. If it is present then it throws an exception.
     *
     * @param toBeAbsent   is a description of a frame to be absent
     * @param timeOut      is a time to wait for a frame is absent.
     * @return is frame absent or not
     */
    public boolean absenceOfOrThrow(GetFrameSupplier toBeAbsent, Duration timeOut) {
        changeContentIfNecessary();
        return super.absenceOfOrThrow(toBeAbsent, timeOut);
    }


    /**
     * Checks whenever cookies are absent or not
     *
     * @param toBeAbsent is a description of cookies to be absent
     * @param timeOut    is a time to wait for cookies are absent.
     * @return are cookies absent or not
     */
    public boolean absenceOf(GetSeleniumCookieSupplier toBeAbsent, Duration timeOut) {
        changeContentIfNecessary();
        return super.absenceOf(toBeAbsent, timeOut);
    }

    /**
     * Checks whenever cookies are absent. If they are present then it throws an exception.
     *
     * @param toBeAbsent   is a description of cookies to be absent
     * @param timeOut      is a time to wait for cookies are absent.
     * @return are cookies absent or not
     */
    public boolean absenceOfOrThrow(GetSeleniumCookieSupplier toBeAbsent, Duration timeOut) {
        changeContentIfNecessary();
        return super.absenceOfOrThrow(toBeAbsent, timeOut);
    }

    /**
     * Checks is alert absent or not
     *
     * @param toBeAbsent is a description of an alert to be absent
     * @param timeOut    is a time to wait for an alert is absent.
     * @return is alert absent or not
     */
    public boolean absenceOf(GetAlertSupplier toBeAbsent, Duration timeOut) {
        changeContentIfNecessary();
        return super.absenceOf(toBeAbsent, timeOut);
    }

    /**
     * Checks is alert absent. If it is present then it throws an exception.
     *
     * @param toBeAbsent   is a description of an alert to be absent
     * @param timeOut      is a time to wait for an alert is absent.
     * @return is alert absent or not
     */
    public boolean absenceOfOrThrow(GetAlertSupplier toBeAbsent, Duration timeOut) {
        changeContentIfNecessary();
        return super.absenceOfOrThrow(toBeAbsent, timeOut);
    }

    /**
     * Cleans browser's cookie jar.
     *
     * @return self-reference
     */
    public SeleniumStepContext removeCookies() {
        changeContentIfNecessary();
        perform(deleteCookies());
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
        changeContentIfNecessary();
        perform(deleteCookies(timeToFindCookies, toBeRemoved));
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
        changeContentIfNecessary();
        return removeCookies(null, toBeRemoved);
    }

    /**
     * Cleans browser's cookie jar.
     *
     * @param cookies cookies that should be deleted
     * @return self-reference
     */
    public SeleniumStepContext removeCookies(Collection<Cookie> cookies) {
        changeContentIfNecessary();
        perform(deleteCookies(cookies));
        return this;
    }

    /**
     * Cleans browser's cookie jar.
     *
     * @param cookies cookies that should be deleted
     * @return self-reference
     */
    public SeleniumStepContext removeCookies(Cookie... cookies) {
        changeContentIfNecessary();
        return removeCookies(asList(cookies));
    }

    /**
     * Adds cookies to browser's cookie jar.
     *
     * @param cookies to be added
     * @return self-reference
     */
    public SeleniumStepContext addCookies(Collection<Cookie> cookies) {
        changeContentIfNecessary();
        perform(AddCookiesActionSupplier.addCookies(cookies));
        return this;
    }

    /**
     * Adds cookies to browser's cookie jar.
     *
     * @param cookies to be added
     * @return self-reference
     */
    public SeleniumStepContext addCookies(Cookie... cookies) {
        changeContentIfNecessary();
        return addCookies(asList(cookies));
    }

    /**
     * Gets URL of a page that loaded in the browser window/tab that active currently.
     *
     * @return string URL of a loaded page
     */
    public String getCurrentUrl() {
        changeContentIfNecessary();
        return get(currentUrl());
    }

    /**
     * Gets URL of a page that loaded in the browser window/tab.
     *
     * @param getWindow is how to find/get target window/tab to get an URL from
     * @return string URL of a loaded page
     */
    public String getCurrentUrl(GetWindowSupplier getWindow) {
        changeContentIfNecessary();
        return get(currentUrl(getWindow));
    }

    /**
     * Gets URL of a page that loaded in the browser window/tab.
     *
     * @param window is a window to get an URL from
     * @return {@link URL} of a loaded page
     */
    public String getCurrentUrl(Window window) {
        changeContentIfNecessary();
        return get(currentUrl(window));
    }

    /**
     * Performs navigation back in the browser window/tab that active currently.
     *
     * @return self-reference
     */
    public SeleniumStepContext navigateBack() {
        changeContentIfNecessary();
        perform(back());
        return this;
    }

    /**
     * Performs navigation back in the browser window/tab.
     *
     * @param windowSupplier is how to find/get target window/tab
     * @return self-reference
     */
    public SeleniumStepContext navigateBack(GetWindowSupplier windowSupplier) {
        changeContentIfNecessary();
        perform(back(windowSupplier));
        return this;
    }

    /**
     * Performs navigation back in the browser window/tab.
     *
     * @param window is a target window
     * @return self-reference
     */
    public SeleniumStepContext navigateBack(Window window) {
        changeContentIfNecessary();
        perform(back(window));
        return this;
    }

    /**
     * Performs navigation forward in the browser window/tab that active currently.
     *
     * @return self-reference
     */
    public SeleniumStepContext navigateForward() {
        changeContentIfNecessary();
        perform(forward());
        return this;
    }

    /**
     * Performs navigation forward in the browser window/tab.
     *
     * @param windowSupplier is how to find/get target window/tab
     * @return self-reference
     */
    public SeleniumStepContext navigateForward(GetWindowSupplier windowSupplier) {
        changeContentIfNecessary();
        perform(forward(windowSupplier));
        return this;
    }

    /**
     * Performs navigation forward in the browser window/tab.
     *
     * @param window is a target window
     * @return self-reference
     */
    public SeleniumStepContext navigateForward(Window window) {
        changeContentIfNecessary();
        perform(forward(window));
        return this;
    }


    /**
     * Performs the refreshing of the browser window/tab that active currently.
     *
     * @return self-reference
     */
    public SeleniumStepContext refresh() {
        changeContentIfNecessary();
        perform(refreshWindow());
        return this;
    }

    /**
     * Performs the refreshing of the browser window/tab.
     *
     * @param windowSupplier is how to find/get target window/tab
     * @return self-reference
     */
    public SeleniumStepContext refresh(GetWindowSupplier windowSupplier) {
        changeContentIfNecessary();
        perform(refreshWindow(windowSupplier));
        return this;
    }

    /**
     * Performs the refreshing of the browser window/tab.
     *
     * @param window is a target window
     * @return self-reference
     */
    public SeleniumStepContext refresh(Window window) {
        changeContentIfNecessary();
        perform(refreshWindow(window));
        return this;
    }


    /**
     * Performs navigation in the browser window/tab that active currently.
     *
     * @param url is a target URL
     * @return self-reference
     */
    public SeleniumStepContext navigateTo(URL url) {
        changeContentIfNecessary();
        perform(toUrl(url));
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
        changeContentIfNecessary();
        perform(toUrl(in, url));
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
        changeContentIfNecessary();
        perform(toUrl(in, url));
        return this;
    }

    /**
     * Performs navigation in the default browser window/tab that active currently.
     *
     * @param url is a string value the url to navigate to. Also it may be a path relative to base web driver url.
     * @return self-reference
     * @see ru.tinkoff.qa.neptune.selenium.properties.URLProperties#BASE_WEB_DRIVER_URL_PROPERTY
     */
    public SeleniumStepContext navigateTo(String url) {
        changeContentIfNecessary();
        perform(toUrl(url));
        return this;
    }

    /**
     * Performs navigation in the browser window/tab.
     *
     * @param url is a string value the url to navigate to. Also it may be a path relative to base web driver url.
     * @param in  is how to find/get target window/tab
     * @return self-reference
     * @see ru.tinkoff.qa.neptune.selenium.properties.URLProperties#BASE_WEB_DRIVER_URL_PROPERTY
     */
    public SeleniumStepContext navigateTo(String url, GetWindowSupplier in) {
        changeContentIfNecessary();
        perform(toUrl(in, url));
        return this;
    }

    /**
     * Performs navigation in the browser window/tab.
     *
     * @param url is a string value the url to navigate to. Also it may be a path relative to base web driver url.
     * @param in  is a target window
     * @return self-reference
     * @see ru.tinkoff.qa.neptune.selenium.properties.URLProperties#BASE_WEB_DRIVER_URL_PROPERTY
     */
    public SeleniumStepContext navigateTo(String url, Window in) {
        changeContentIfNecessary();
        perform(toUrl(in, url));
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
        changeContentIfNecessary();
        perform(to(getActiveElement));
        return this;
    }

    /**
     * Performs the switching to an alert
     *
     * @param getAlert is how to get the alert
     * @return self-reference
     */
    public SeleniumStepContext switchTo(GetAlertSupplier getAlert) {
        changeContentIfNecessary();
        perform(to(getAlert));
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
        changeContentIfNecessary();
        perform(to(getDefaultContent));
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
        changeContentIfNecessary();
        perform(to(getParentFrame));
        return this;
    }

    /**
     * Performs the switching to the frame.
     *
     * @param getFrame is how to get the frame
     * @return self-reference
     */
    public SeleniumStepContext switchTo(GetFrameSupplier getFrame) {
        changeContentIfNecessary();
        perform(to(getFrame));
        return this;
    }

    /**
     * Performs the switching to the frame.
     *
     * @param frame is the frame to switch to
     * @return self-reference
     */
    public SeleniumStepContext switchTo(Frame frame) {
        changeContentIfNecessary();
        perform(to(frame));
        return this;
    }

    /**
     * Performs the switching to the browser window/tab.
     *
     * @param getWindow is how to get the window/tab
     * @return self-reference
     */
    public SeleniumStepContext switchTo(GetWindowSupplier getWindow) {
        changeContentIfNecessary();
        perform(to(getWindow));
        return this;
    }

    /**
     * Performs the switching to the browser window/tab.
     *
     * @param window is the browser/window to switch to
     * @return self-reference
     */
    public SeleniumStepContext switchTo(Window window) {
        changeContentIfNecessary();
        perform(to(window));
        return this;
    }

    /**
     * Returns an instance of {@link Dimension} as a representation of size of the browser window/tab that active currently
     *
     * @return an instance of {@link Dimension}
     */
    public Dimension windowSize() {
        changeContentIfNecessary();
        return get(GetWindowSizeSupplier.windowSize());
    }

    /**
     * Returns an instance of {@link Dimension} as a representation of size of the element/widget
     *
     * @param getElement is how to find the target web element/widget
     * @return an instance of {@link Dimension}
     */
    public Dimension sizeOf(SearchSupplier<?> getElement) {
        changeContentIfNecessary();
        return get(elementSize(getElement));
    }

    /**
     * Returns an instance of {@link Dimension} as a representation of size of the browser window/tab
     *
     * @param getWindow is how to find the target browser window/tab
     * @return an instance of {@link Dimension}
     */
    public Dimension sizeOf(GetWindowSupplier getWindow) {
        changeContentIfNecessary();
        return get(GetWindowSizeSupplier.windowSize(getWindow));
    }

    /**
     * Returns an instance of {@link Dimension} as a representation of size of the widget
     *
     * @param widget is the target widget
     * @return an instance of {@link Dimension}
     */
    public Dimension sizeOf(Widget widget) {
        changeContentIfNecessary();
        return get(elementSize(widget));
    }

    /**
     * Returns an instance of {@link Dimension} as a representation of size of the web element
     *
     * @param element is the target web element
     * @return an instance of {@link Dimension}
     */
    public Dimension sizeOf(WebElement element) {
        changeContentIfNecessary();
        return get(elementSize(element));
    }

    /**
     * Returns an instance of {@link Dimension} as a representation of size of the browser window/tab
     *
     * @param window is the target browser window/tab
     * @return an instance of {@link Dimension}
     */
    public Dimension sizeOf(Window window) {
        changeContentIfNecessary();
        return get(GetWindowSizeSupplier.windowSize(window));
    }


    /**
     * Returns an instance of {@link Point} as a representation of position of the browser window/tab that active currently
     *
     * @return an instance of {@link Point}
     */
    public Point windowPosition() {
        changeContentIfNecessary();
        return get(GetWindowPositionSupplier.windowPosition());
    }

    /**
     * Returns an instance of {@link Point} as a representation of position of the element/widget
     *
     * @param getElement is how to find the target web element/widget
     * @return an instance of {@link Point}
     */
    public Point positionOf(SearchSupplier<?> getElement) {
        changeContentIfNecessary();
        return get(positionOfElement(getElement));
    }

    /**
     * Returns an instance of {@link Point} as a representation of position of the browser window/tab
     *
     * @param getWindow is how to find the target browser window/tab
     * @return an instance of {@link Point}
     */
    public Point positionOf(GetWindowSupplier getWindow) {
        changeContentIfNecessary();
        return get(GetWindowPositionSupplier.positionOf(getWindow));
    }

    /**
     * Returns an instance of {@link Point} as a representation of position of the widget
     *
     * @param widget is the target widget
     * @return an instance of {@link Point}
     */
    public Point positionOf(Widget widget) {
        changeContentIfNecessary();
        return get(positionOfElement(widget));
    }

    /**
     * Returns an instance of {@link Point} as a representation of position of the web element
     *
     * @param element is the target web element
     * @return an instance of {@link Point}
     */
    public Point positionOf(WebElement element) {
        changeContentIfNecessary();
        return get(positionOfElement(element));
    }

    /**
     * Returns an instance of {@link Point} as a representation of position of the browser window/tab
     *
     * @param window is the target browser window/tab
     * @return an instance of {@link Point}
     */
    public Point positionOf(Window window) {
        changeContentIfNecessary();
        return get(GetWindowPositionSupplier.positionOf(window));
    }

    /**
     * Performs the closing of the browser window/tab that active currently
     *
     * @return self-reference
     */
    public SeleniumStepContext closeWindow() {
        changeContentIfNecessary();
        perform(CloseWindowActionSupplier.closeWindow());
        return this;
    }

    /**
     * Performs the closing of the browser window/tab.
     *
     * @param supplier is how to find the browser window/tab to close it.
     * @return self-reference
     */
    public SeleniumStepContext closeWindow(GetWindowSupplier supplier) {
        changeContentIfNecessary();
        perform(CloseWindowActionSupplier.closeWindow(supplier));
        return this;
    }

    /**
     * Performs the closing of the browser window/tab.
     *
     * @param window the browser window/tab to close
     * @return self-reference
     */
    public SeleniumStepContext closeWindow(Window window) {
        changeContentIfNecessary();
        perform(CloseWindowActionSupplier.closeWindow(window));
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
        changeContentIfNecessary();
        perform(setWindowPosition(new Point(x, y)));
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
        changeContentIfNecessary();
        perform(setPositionOf(supplier, new Point(x, y)));
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
        changeContentIfNecessary();
        perform(setPositionOf(window, new Point(x, y)));
        return this;
    }


    /**
     * Performs the setting to new size of the browser window/tab that active currently
     *
     * @param width  is new width of the window/tab
     * @param height is new height of the window/tab
     * @return self-reference
     */
    public SeleniumStepContext changeWindowSize(int width, int height) {
        changeContentIfNecessary();
        perform(setWindowSize(new Dimension(width, height)));
        return this;
    }

    /**
     * Performs the setting to new size of the browser window/tab.
     *
     * @param supplier is how to find the browser window/tab
     * @param width    is new width of the window/tab
     * @param height   is new height of the window/tab
     * @return self-reference
     */
    public SeleniumStepContext changeWindowSize(GetWindowSupplier supplier, int width, int height) {
        changeContentIfNecessary();
        perform(setSizeOf(supplier, new Dimension(width, height)));
        return this;
    }

    /**
     * Performs the setting to new size of the browser window/tab.
     *
     * @param width  is new width of the window/tab
     * @param height is new height of the window/tab
     * @param window the browser window/tab
     * @return self-reference
     */
    public SeleniumStepContext changeWindowSize(Window window, int width, int height) {
        changeContentIfNecessary();
        perform(setSizeOf(window, new Dimension(width, height)));
        return this;
    }


    /**
     * Performs the setting active browser window/tab full screen
     *
     * @return self-reference
     */
    public SeleniumStepContext fullScreen() {
        changeContentIfNecessary();
        perform(FullScreenWindowSupplier.fullScreen());
        return this;
    }

    /**
     * Performs the setting the browser window/tab full screen
     *
     * @param supplier is how to find the browser window/tab
     * @return self-reference
     */
    public SeleniumStepContext fullScreen(GetWindowSupplier supplier) {
        changeContentIfNecessary();
        perform(FullScreenWindowSupplier.fullScreen(supplier));
        return this;
    }

    /**
     * Performs the setting the browser window/tab full screen
     *
     * @param window the browser window/tab
     * @return self-reference
     */
    public SeleniumStepContext fullScreen(Window window) {
        changeContentIfNecessary();
        perform(FullScreenWindowSupplier.fullScreen(window));
        return this;
    }


    /**
     * Performs an accept-action. This action is performed on a browser alert.
     *
     * @param getAlert is description of an alert to accept
     * @return self-reference
     */
    public SeleniumStepContext accept(GetAlertSupplier getAlert) {
        changeContentIfNecessary();
        perform(acceptAlert(getAlert));
        return this;
    }

    /**
     * Performs an accept-action. This action is performed on a browser alert.
     *
     * @param alert is an alert to accept
     * @return self-reference
     */
    public SeleniumStepContext accept(Alert alert) {
        changeContentIfNecessary();
        perform(acceptAlert(alert));
        return this;
    }


    /**
     * Performs an dismiss-action. This action is performed on a browser alert.
     *
     * @param getAlert is description of an alert to dismiss
     * @return self-reference
     */
    public SeleniumStepContext dismiss(GetAlertSupplier getAlert) {
        changeContentIfNecessary();
        perform(dismissAlert(getAlert));
        return this;
    }

    /**
     * Performs an dismiss-action. This action is performed on a browser alert.
     *
     * @param alert is an alert to dismiss
     * @return self-reference
     */
    public SeleniumStepContext dismiss(Alert alert) {
        changeContentIfNecessary();
        perform(dismissAlert(alert));
        return this;
    }


    /**
     * Performs an send keys-action. This action is performed on a browser alert.
     *
     * @param getAlert is description of an alert to send keys
     * @param keys     a is string/keys to send
     * @return self-reference
     */
    public SeleniumStepContext alertSendKeys(GetAlertSupplier getAlert, String keys) {
        changeContentIfNecessary();
        perform(sendKeysToAlert(getAlert, keys));
        return this;
    }

    /**
     * Performs an send keys-action. This action is performed on a browser alert.
     *
     * @param alert is an alert to send keys
     * @param keys  is string/keys to send
     * @return self-reference
     */
    public SeleniumStepContext alertSendKeys(Alert alert, String keys) {
        changeContentIfNecessary();
        perform(sendKeysToAlert(alert, keys));
        return this;
    }

    /**
     * Performs an interactive action.
     *
     * @param action description of the action to be performed
     * @return self-reference
     */
    public SeleniumStepContext interactive(InteractiveAction action) {
        changeContentIfNecessary();
        perform(action);
        return this;
    }

    /**
     * Returns title of the browser window/tab that active currently.
     *
     * @return window title
     */
    public String windowTitle() {
        changeContentIfNecessary();
        return get(GetWindowTitleSupplier.windowTitle());
    }

    /**
     * Returns title of the browser window/tab.
     *
     * @param supplier is how to find the browser window/tab
     * @return window title
     */
    public String windowTitle(GetWindowSupplier supplier) {
        changeContentIfNecessary();
        return get(titleOf(supplier));
    }

    /**
     * Returns title of the browser window/tab.
     *
     * @param window is the target browser window/tab
     * @return window title
     */
    public String windowTitle(Window window) {
        changeContentIfNecessary();
        return get(titleOf(window));
    }

    /**
     * Returns attribute value of an element of a page
     *
     * @param of   is how to find the element
     * @param attr as a target attribute name
     * @return attr value
     */
    public String attrValueOf(SearchSupplier<?> of, String attr) {
        changeContentIfNecessary();
        return get(attributeValue(attr, of));
    }

    /**
     * Returns attribute value of the element of a page
     *
     * @param of   is the widget to get attr value from
     * @param attr as a target attribute name
     * @return attr value
     */
    public String attrValueOf(Widget of, String attr) {
        changeContentIfNecessary();
        return get(attributeValue(attr, of));
    }

    /**
     * Returns attribute value of the element of a page
     *
     * @param of   is the web element to get attr value from
     * @param attr as a target attribute name
     * @return attr value
     */
    public String attrValueOf(WebElement of, String attr) {
        changeContentIfNecessary();
        return get(attributeValue(attr, of));
    }


    /**
     * Returns css value of an element of a page
     *
     * @param of          is how to find the element
     * @param cssProperty as a target css name
     * @return css value
     */
    public String cssValueOf(SearchSupplier<?> of, String cssProperty) {
        changeContentIfNecessary();
        return get(cssValue(cssProperty, of));
    }

    /**
     * Returns css value of the element of a page
     *
     * @param of          is the widget to get css value from
     * @param cssProperty as a target css name
     * @return css value
     */
    public String cssValueOf(Widget of, String cssProperty) {
        changeContentIfNecessary();
        return get(cssValue(cssProperty, of));
    }

    /**
     * Returns css value of the element of a page
     *
     * @param of          is the web element to get css value from
     * @param cssProperty as a target css name
     * @return css value
     */
    public String cssValueOf(WebElement of, String cssProperty) {
        changeContentIfNecessary();
        return get(cssValue(cssProperty, of));
    }

    /**
     * Returns value of an element of a page
     *
     * @param of  is how to find the element
     * @param <T> is a type of value to be returned
     * @param <R> is a type of an element that may have value of {@code T}
     * @return value
     */
    public <T, R extends SearchContext & HasValue<T>> T valueOf(SearchSupplier<R> of) {
        changeContentIfNecessary();
        return get(ofThe(of));
    }

    /**
     * Returns value of the element of a page
     *
     * @param of  is the element that has value of type {@code T}
     * @param <T> is a type of value to be returned
     * @param <R> is a type of an element that may have value of {@code T}
     * @return value
     */
    public <T, R extends SearchContext & HasValue<T>> T valueOf(R of) {
        changeContentIfNecessary();
        return get(ofThe(of));
    }


    /**
     * Returns a set of browser {@link Cookie}
     *
     * @param getCookies is how to get find cookies
     * @return a set of {@link Cookie}
     */
    public Set<Cookie> get(GetSeleniumCookieSupplier getCookies) {
        changeContentIfNecessary();
        return super.get(getCookies);
    }


    /**
     * Returns {@link Alert} as as a representation of a browser alert
     *
     * @param getAlert is how to get the alert
     * @return an instance of {@link Alert}
     */
    public Alert get(GetAlertSupplier getAlert) {
        changeContentIfNecessary();
        return super.get(getAlert);
    }

    /**
     * Returns {@link Frame} as as a representation of an iframe
     *
     * @param getFrame is how to get the frame
     * @return an instance of {@link Frame}
     */
    public Frame get(GetFrameSupplier getFrame) {
        changeContentIfNecessary();
        return super.get(getFrame);
    }


    /**
     * Returns {@link Window} as a representation of a browser window/tab
     *
     * @param getWindow is a description of a window to get
     * @return an instance of {@link Window}
     */
    public Window get(GetWindowSupplier getWindow) {
        changeContentIfNecessary();
        return super.get(getWindow);
    }

    public static final class CurrentContentFunction implements Function<SeleniumStepContext, WebDriver> {

        private CurrentContentFunction() {
            super();
        }

        public static Function<SeleniumStepContext, WebDriver> currentContent() {
            return new CurrentContentFunction();
        }

        @Override
        public WebDriver apply(SeleniumStepContext seleniumSteps) {
            return seleniumSteps.getWrappedDriver();
        }
    }

    public static final class GetProxy implements Function<SeleniumStepContext, BrowserUpProxy> {

        public static Function<SeleniumStepContext, BrowserUpProxy> getBrowserProxy() {
            return new GetProxy();
        }

        @Override
        public BrowserUpProxy apply(SeleniumStepContext seleniumSteps) {
            return ofNullable(getCurrentProxy())
                    .map(browserProxy -> {
                        browserProxy.createProxy();
                        return browserProxy.getProxy();
                    })
                    .orElse(null);
        }
    }
}
