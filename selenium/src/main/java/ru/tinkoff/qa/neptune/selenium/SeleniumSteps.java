package ru.tinkoff.qa.neptune.selenium;

import ru.tinkoff.qa.neptune.core.api.GetStep;
import ru.tinkoff.qa.neptune.core.api.PerformActionStep;
import ru.tinkoff.qa.neptune.core.api.cleaning.Refreshable;
import ru.tinkoff.qa.neptune.core.api.cleaning.Stoppable;
import ru.tinkoff.qa.neptune.core.api.CreateWith;
import ru.tinkoff.qa.neptune.selenium.functions.navigation.NavigationActionSupplier;
import ru.tinkoff.qa.neptune.selenium.functions.searching.MultipleSearchSupplier;
import ru.tinkoff.qa.neptune.selenium.functions.searching.SearchSupplier;
import ru.tinkoff.qa.neptune.selenium.functions.target.locator.SwitchActionSupplier;
import ru.tinkoff.qa.neptune.selenium.functions.target.locator.alert.AlertActionSupplier;
import ru.tinkoff.qa.neptune.selenium.functions.click.ClickActionSupplier;
import ru.tinkoff.qa.neptune.selenium.functions.edit.EditActionSupplier;
import ru.tinkoff.qa.neptune.selenium.functions.java.script.GetJavaScriptResultSupplier;
import ru.tinkoff.qa.neptune.selenium.functions.value.SequentialGetValueSupplier;
import ru.tinkoff.qa.neptune.selenium.properties.SupportedWebDrivers;
import org.openqa.selenium.*;

import java.util.List;

import static ru.tinkoff.qa.neptune.selenium.CurrentContentFunction.currentContent;

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
        return get(what.get().compose(CurrentContentFunction.currentContent()));
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
