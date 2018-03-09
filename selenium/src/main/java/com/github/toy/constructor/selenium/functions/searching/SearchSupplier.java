package com.github.toy.constructor.selenium.functions.searching;

import com.github.toy.constructor.core.api.GetSupplier;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.SearchContext;

import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

import static com.github.toy.constructor.core.api.StoryWriter.toGet;
import static com.github.toy.constructor.core.api.ToGetConditionalHelper.getFromIterable;
import static java.lang.String.format;
import static java.util.Optional.ofNullable;

public class SearchSupplier<R extends SearchContext> extends GetSupplier<SearchContext, R, SearchSupplier<R>> {

    private SearchSupplier() {
        super();
    }

    /**
     * Returns an instance of {@link SearchSupplier} which wraps a function.
     * The wrapped function takes an instance of {@link SearchContext} for the searching
     * and returns some instance of {@link SearchContext} found from the input value.
     *
     * @param transformation is a function which performs the searching from some {@link SearchContext}
     *                       and transform a found item to another instance of {@link SearchContext}
     * @param condition      to specify the searching criteria
     * @param <T>            is a type of a value to be returned by resulted function
     * @return an instance of {@link SearchSupplier}
     */
    public static <T extends SearchContext> SearchSupplier<T> item(
            Function<SearchContext, List<T>> transformation,
            Predicate<T> condition) {
        SearchSupplier<T> supplier = new SearchSupplier<>();

        Function<SearchContext, T> resultFunction = getFromIterable("A single item",
                transformation,
                condition, true,
                true);

        return supplier.set(toGet(resultFunction.toString(), searchContext ->
                ofNullable(resultFunction.apply(searchContext)).orElseThrow(() ->
                        new NoSuchElementException(format("Nothing was found. Attempt to get: %s. Condition: %s",
                                resultFunction,
                                condition)))));
    }
}
