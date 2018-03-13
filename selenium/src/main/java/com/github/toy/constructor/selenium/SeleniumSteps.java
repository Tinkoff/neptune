package com.github.toy.constructor.selenium;

import com.github.toy.constructor.core.api.GetStep;
import com.github.toy.constructor.core.api.PerformStep;
import com.github.toy.constructor.selenium.api.widget.Editable;
import com.github.toy.constructor.selenium.functions.click.ClickActionSupplier;
import com.github.toy.constructor.selenium.functions.edit.EditActionSupplier;
import com.github.toy.constructor.selenium.functions.searching.SequentialMultipleSearchSupplier;
import com.github.toy.constructor.selenium.functions.searching.SequentialSearchSupplier;
import com.github.toy.constructor.selenium.functions.value.SequentialGetValueSupplier;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.internal.WrapsDriver;

import java.util.List;

public class SeleniumSteps implements PerformStep<SeleniumSteps>, GetStep<SeleniumSteps>, WrapsDriver{

    private final WebDriver driver;

    public SeleniumSteps(WebDriver driver) {
        this.driver = driver;
    }

    @Override
    public WebDriver getWrappedDriver() {
        return driver;
    }

    public <R extends SearchContext> R find(SequentialSearchSupplier<R> what) {
        return get(what);
    }

    public <R extends SearchContext> List<R> find(SequentialMultipleSearchSupplier<R> what) {
        return get(what);
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
}
