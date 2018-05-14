package com.github.toy.constructor.selenium.functions.value;

import com.github.toy.constructor.core.api.SequentialGetSupplier;
import com.github.toy.constructor.selenium.SeleniumSteps;
import com.github.toy.constructor.selenium.api.widget.HasValue;
import com.github.toy.constructor.selenium.functions.searching.SearchSupplier;
import org.openqa.selenium.SearchContext;

import java.util.function.Function;

import static com.github.toy.constructor.core.api.StoryWriter.toGet;
import static com.github.toy.constructor.selenium.CurrentContentFunction.currentContent;
import static com.google.common.base.Preconditions.checkArgument;

public final class SequentialGetValueSupplier<T> extends
        SequentialGetSupplier<SeleniumSteps, T, HasValue<T>, SequentialGetValueSupplier<T>> {

    private SequentialGetValueSupplier() {
        super();
    }

    /**
     * Builds a function which takes a value from some element and returns the value.
     * @param from is how to find the element which has value
     * @param <T> is the type of a value to be returned
     * @param <R> is the type of an element which has value
     * @return the function which takes a value from some element and returns the value.
     */
    public static <T, R extends SearchContext & HasValue<T>> SequentialGetValueSupplier<T> ofThe(
            SearchSupplier<R> from) {
        checkArgument(from != null, "The searching for the element which has value should be defined");
        return new SequentialGetValueSupplier<T>().from(from.get().compose(currentContent()));
    }

    /**
     * Builds a function which takes a value from some element and returns the value.
     * @param from is the element which has value
     * @param <T> is the type of a value to be returned
     * @param <R> is the type of an element which has value
     * @return the function which takes a value from some element and returns the value.
     */
    public static <T, R extends SearchContext & HasValue<T>> SequentialGetValueSupplier<T> ofThe(R from) {
        checkArgument(from != null, "The element which has value should be defined");
        return new SequentialGetValueSupplier<T>().from(from);
    }

    @Override
    protected Function<HasValue<T>, T> getEndFunction() {
        return toGet("Get value", HasValue::getValue);
    }
}
