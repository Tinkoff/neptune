package ru.tinkoff.qa.neptune.selenium;

import org.openqa.selenium.*;
import ru.tinkoff.qa.neptune.core.api.cleaning.ContextRefreshable;
import ru.tinkoff.qa.neptune.core.api.cleaning.Stoppable;
import ru.tinkoff.qa.neptune.core.api.steps.context.ActionStepContext;
import ru.tinkoff.qa.neptune.core.api.steps.context.CreateWith;
import ru.tinkoff.qa.neptune.core.api.steps.context.GetStepContext;
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

import java.util.List;

import static ru.tinkoff.qa.neptune.selenium.CurrentContentFunction.currentContent;

@CreateWith(provider = SeleniumParameterProvider.class)
public class SeleniumStepContext implements ActionStepContext<SeleniumStepContext>, GetStepContext<SeleniumStepContext>, WrapsDriver, ContextRefreshable,
        TakesScreenshot, Stoppable {

    private final WrappedWebDriver wrappedWebDriver;

    public SeleniumStepContext(SupportedWebDrivers supportedWebDriver) {
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

    public SeleniumStepContext click(ClickActionSupplier clickActionSupplier) {
        return perform(clickActionSupplier);
    }

    public <T> T getValue(SequentialGetValueSupplier<T> getValueSupplier) {
        return get(getValueSupplier);
    }

    public SeleniumStepContext edit(EditActionSupplier editActionSupplier) {
        return perform(editActionSupplier);
    }

    public Object evaluate(GetJavaScriptResultSupplier javaScriptResultSupplier) {
        return get(javaScriptResultSupplier);
    }

    public SeleniumStepContext alert(AlertActionSupplier alertActionSupplier) {
        return perform(alertActionSupplier);
    }

    public SeleniumStepContext performSwitch(SwitchActionSupplier switchActionSupplier) {
        return perform(switchActionSupplier);
    }

    public SeleniumStepContext navigate(NavigationActionSupplier<?> navigationActionSupplier) {
        return perform(navigationActionSupplier);
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
}
