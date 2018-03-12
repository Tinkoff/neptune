package com.github.toy.constructor.selenium.functions.edit;

import com.github.toy.constructor.core.api.SequentialActionSupplier;
import com.github.toy.constructor.selenium.SeleniumSteps;
import com.github.toy.constructor.selenium.api.widget.Editable;
import com.github.toy.constructor.selenium.functions.searching.SequentialSearchSupplier;
import org.openqa.selenium.SearchContext;

import java.util.function.Function;

import static java.lang.String.format;

public final class EditActionSupplier<T> extends SequentialActionSupplier<SeleniumSteps, Editable<T>, EditActionSupplier<T>> {

    private EditActionSupplier() {
        super();
    }

    public static <T, R extends SearchContext & Editable<T>> EditActionSupplier<T> valueOf(
            SequentialSearchSupplier<R> of, T newValue) {
        return new EditActionSupplier<T>().andThen(format("Edit. Set new value %s", newValue),
                (Function<SeleniumSteps, Editable<T>>) of.get(), newValue);
    }

    public static <T, R extends SearchContext & Editable<T>> EditActionSupplier<T> valueOf(
            R of, T newValue) {
        return new EditActionSupplier<T>().andThen(format("Edit. Set new value %s", newValue),
                of, newValue);
    }

    @Override
    protected void performActionOn(Editable<T> value, Object... additionalArgument) {
        value.edit((T) additionalArgument[0]);
    }
}
