package com.github.toy.constructor.selenium.functions.searching;

import com.github.toy.constructor.core.api.GetSupplier;
import org.openqa.selenium.SearchContext;

import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

import static com.github.toy.constructor.core.api.ToGetConditionalHelper.getSubIterable;

public class MultipleSearchSupplier<R extends SearchContext> extends
        GetSupplier<SearchContext, List<R>, MultipleSearchSupplier<R>> {

    private MultipleSearchSupplier() {
        super();
    }

    /**
     * Returns an instance of {@link MultipleSearchSupplier} which wraps a function.
     * The wrapped function takes an instance of {@link SearchContext} for the searching
     * and returns some list of instances of {@link SearchContext} found from the input value.
     *
     * @param transformation is a function which performs the searching from some {@link SearchContext}
     *                       and transform the list of found items to another list of instances
     *                       of {@link SearchContext}
     * @param condition to specify the searching criteria
     * @param <T> is a type of a value to be returned by resulted function
     * @return an instance of {@link MultipleSearchSupplier}
     */
    public static <T extends SearchContext> MultipleSearchSupplier<T> items(
            Function<SearchContext,List<T>> transformation,
            Predicate<T> condition) {
        MultipleSearchSupplier<T> supplier = new MultipleSearchSupplier<>();

        return supplier.set(getSubIterable("List of",
                transformation,
                condition, true,
                true));
    }
}
