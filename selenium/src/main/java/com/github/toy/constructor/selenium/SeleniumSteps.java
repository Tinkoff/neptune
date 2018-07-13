package com.github.toy.constructor.selenium;

import com.github.toy.constructor.core.api.GetStep;
import com.github.toy.constructor.core.api.PerformActionStep;
import com.github.toy.constructor.core.api.cleaning.Refreshable;
import com.github.toy.constructor.core.api.cleaning.Stoppable;
import com.github.toy.constructor.core.api.CreateWith;
import com.github.toy.constructor.selenium.functions.navigation.NavigationActionSupplier;
import com.github.toy.constructor.selenium.functions.searching.MultipleSearchSupplier;
import com.github.toy.constructor.selenium.functions.searching.SearchSupplier;
import com.github.toy.constructor.selenium.functions.target.locator.SwitchActionSupplier;
import com.github.toy.constructor.selenium.functions.target.locator.alert.AlertActionSupplier;
import com.github.toy.constructor.selenium.functions.click.ClickActionSupplier;
import com.github.toy.constructor.selenium.functions.edit.EditActionSupplier;
import com.github.toy.constructor.selenium.functions.java.script.GetJavaScriptResultSupplier;
import com.github.toy.constructor.selenium.functions.value.SequentialGetValueSupplier;
import com.github.toy.constructor.selenium.properties.SupportedWebDrivers;
import org.openqa.selenium.*;
import org.openqa.selenium.internal.WrapsDriver;

import java.util.List;

import static com.github.toy.constructor.selenium.CurrentContentFunction.currentContent;

@CreateWith(provider = SeleniumParameterProvider.class)
public class SeleniumSteps implements PerformActionStep<SeleniumSteps>, GetStep<SeleniumSteps>, WrapsDriver, Refreshable,
        Stoppable, TakesScreenshot {

    private final WrappedWebDriver wrappedWebDriver;

    public SeleniumSteps(SupportedWebDrivers supportedWebDriver) {
        this.wrappedWebDriver = new WrappedWebDriver(supportedWebDriver);
    }

    @Override
    public WebDriver getWrappedDriver() {
        return wrappedWebDriver.getWrappedDriver();
    }

    public <R extends SearchContext> R find(SearchSupplier<R> what) {
        return get(what.get().compose(currentContent()));
    }

    public <R extends SearchContext> List<R> find(MultipleSearchSupplier<R> what) {
        return get(what.get().compose(currentContent()));
    }

    public SeleniumSteps click(ClickActionSupplier clickActionSupplier) {
        return perform(clickActionSupplier);
    }

    public <T> T getValue(SequentialGetValueSupplier<T> getValueSupplier) {
        return get(getValueSupplier);
    }

    public SeleniumSteps edit(EditActionSupplier editActionSupplier) {
        return perform(editActionSupplier);
    }

    public Object evaluate(GetJavaScriptResultSupplier javaScriptResultSupplier) {
        return get(javaScriptResultSupplier);
    }

    public SeleniumSteps alert(AlertActionSupplier alertActionSupplier) {
        return perform(alertActionSupplier);
    }

    public SeleniumSteps performSwitch(SwitchActionSupplier switchActionSupplier) {
        return perform(switchActionSupplier);
    }

    public SeleniumSteps navigate(NavigationActionSupplier navigationActionSupplier) {
        return perform(navigationActionSupplier);
    }

    @Override
    public void refresh() {
        wrappedWebDriver.refresh();
    }

    @Override
    public void shutDown() {
        wrappedWebDriver.shutDown();
    }

    @Override
    public <X> X getScreenshotAs(OutputType<X> target) throws WebDriverException {
        return ((TakesScreenshot) getWrappedDriver()).getScreenshotAs(target);
    }
}
