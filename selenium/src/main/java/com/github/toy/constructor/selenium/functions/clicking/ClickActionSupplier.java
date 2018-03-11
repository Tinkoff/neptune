package com.github.toy.constructor.selenium.functions.clicking;

import com.github.toy.constructor.core.api.SequentialActionSupplier;
import com.github.toy.constructor.selenium.SeleniumSteps;
import com.github.toy.constructor.selenium.api.widget.Clickable;
import com.github.toy.constructor.selenium.functions.searching.SequentialSearchSupplier;
import org.openqa.selenium.SearchContext;

import java.util.function.Function;

public final class ClickActionSupplier extends SequentialActionSupplier<SeleniumSteps, Clickable, ClickActionSupplier> {

    private ClickActionSupplier() {
        super();
    }

    public static <R extends SearchContext & Clickable> ClickActionSupplier on(SequentialSearchSupplier<R> searchForElement) {
        Function<SeleniumSteps, R> function = searchForElement.get();
        return new ClickActionSupplier().andThen("Click", (Function<SeleniumSteps, Clickable>) function);
    }

    public <R extends SearchContext & Clickable> ClickActionSupplier andOn(SequentialSearchSupplier<R> searchForElement) {
        Function<SeleniumSteps, R> function = searchForElement.get();
        return andThen("Click", (Function<SeleniumSteps, Clickable>) function);
    }

    @Override
    protected void performActionOn(Clickable value, Object... ignored) {
        value.click();
    }
}
