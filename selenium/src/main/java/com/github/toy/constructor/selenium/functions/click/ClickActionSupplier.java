package com.github.toy.constructor.selenium.functions.click;

import com.github.toy.constructor.core.api.SequentialActionSupplier;
import com.github.toy.constructor.selenium.SeleniumSteps;
import com.github.toy.constructor.selenium.api.widget.Clickable;
import com.github.toy.constructor.selenium.functions.searching.SequentialSearchSupplier;
import org.openqa.selenium.SearchContext;

import java.util.function.Function;

import static com.google.common.base.Preconditions.checkArgument;

public final class ClickActionSupplier extends SequentialActionSupplier<SeleniumSteps, Clickable, ClickActionSupplier> {

    private ClickActionSupplier() {
        super();
    }

    /**
     * Builds the click action on some clickable element
     * @param on is how to find the clickable element
     * @param <R> is the type of the clickable element
     * @return built click action
     */
    public static <R extends SearchContext & Clickable> ClickActionSupplier on(SequentialSearchSupplier<R> on) {
        return new ClickActionSupplier().andOn(on);
    }

    /**
     * Builds the click action on some clickable element
     * @param on is the target clickable element
     * @param <R> is the type of the clickable element
     * @return built click action
     */
    public static <R extends SearchContext & Clickable> ClickActionSupplier on(R on) {
        return new ClickActionSupplier().andOn(on);
    }

    /**
     * Adds the click action on some another clickable element
     * @param on is how to find the clickable element
     * @param <R> is the type of the clickable element
     * @return built click action
     */
    public <R extends SearchContext & Clickable> ClickActionSupplier andOn(SequentialSearchSupplier<R> on) {
        checkArgument(on != null, "The searching for the clickable element should be defined");
        Function<SeleniumSteps, R> function = on.get();
        return andThen("Click", function);
    }

    /**
     * Adds the click action on some another clickable element
     * @param on s the target clickable element
     * @param <R> is the type of the clickable element
     * @return built click action
     */
    public <R extends SearchContext & Clickable> ClickActionSupplier andOn(R on) {
        checkArgument(on != null, "The clickable element should be defined");
        return andThen("Click", on);
    }

    @Override
    protected void performActionOn(Clickable value, Object... ignored) {
        value.click();
    }
}
