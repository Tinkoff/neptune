package ru.tinkoff.qa.neptune.selenium;

import net.lightbody.bmp.BrowserMobProxy;
import net.lightbody.bmp.core.har.HarEntry;
import org.openqa.selenium.*;
import ru.tinkoff.qa.neptune.core.api.cleaning.ContextRefreshable;
import ru.tinkoff.qa.neptune.core.api.cleaning.Stoppable;
import ru.tinkoff.qa.neptune.core.api.steps.context.Context;
import ru.tinkoff.qa.neptune.core.api.steps.context.CreateWith;
import ru.tinkoff.qa.neptune.selenium.functions.browser.proxy.BrowserProxyGetStepSupplier;
import ru.tinkoff.qa.neptune.selenium.functions.click.ClickActionSupplier;
import ru.tinkoff.qa.neptune.selenium.functions.edit.EditActionSupplier;
import ru.tinkoff.qa.neptune.selenium.functions.java.script.GetJavaScriptResultSupplier;
import ru.tinkoff.qa.neptune.selenium.functions.navigation.NavigationActionSupplier;
import ru.tinkoff.qa.neptune.selenium.functions.searching.MultipleSearchSupplier;
import ru.tinkoff.qa.neptune.selenium.functions.searching.SearchSupplier;
import ru.tinkoff.qa.neptune.selenium.functions.target.locator.SwitchActionSupplier;
import ru.tinkoff.qa.neptune.selenium.functions.target.locator.alert.AlertActionSupplier;
import ru.tinkoff.qa.neptune.selenium.functions.value.SequentialGetValueSupplier;
import ru.tinkoff.qa.neptune.selenium.properties.SupportedWebDrivers;

import java.time.Duration;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static java.time.Duration.ofMillis;
import static ru.tinkoff.qa.neptune.selenium.CurrentContentFunction.currentContent;

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

    public BrowserMobProxy getProxy() {
        return wrappedWebDriver.getProxy();
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

    public Object evaluate(GetJavaScriptResultSupplier javaScriptResultSupplier) {
        return javaScriptResultSupplier.get().apply(this);
    }

    public SeleniumStepContext alert(AlertActionSupplier alertActionSupplier) {
        alertActionSupplier.get().accept(this);
        return this;
    }

    public SeleniumStepContext performSwitch(SwitchActionSupplier switchActionSupplier) {
        switchActionSupplier.get().accept(this);
        return this;
    }

    public SeleniumStepContext navigate(NavigationActionSupplier<?> navigationActionSupplier) {
        navigationActionSupplier.get().accept(this);
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

    public List<HarEntry> get(BrowserProxyGetStepSupplier browserProxy) {
        checkArgument(Objects.nonNull(browserProxy), "Browser proxy supplier is not defined");
        return browserProxy.get().apply(this);
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
    public SeleniumStepContext perform(Consumer<SeleniumStepContext> actionConsumer) {
        checkArgument(Objects.nonNull(actionConsumer), "Action is not defined");
        actionConsumer.accept(this);
        return this;
    }

    /**
     * This method was added for backward compatibility temporary
     */
    @Deprecated(since = "0.11.2-ALPHA")
    public SeleniumStepContext perform(Supplier<Consumer<SeleniumStepContext>> actionSupplier) {
        checkNotNull(actionSupplier, "Supplier of the action was not defined");
        return this.perform(actionSupplier.get());
    }
}
