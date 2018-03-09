package com.github.toy.constructor.selenium.functions.searching;

import com.github.toy.constructor.core.api.SequentialGetSupplier;
import com.github.toy.constructor.selenium.SeleniumSteps;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.SearchContext;

import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

import static com.github.toy.constructor.core.api.StoryWriter.toGet;
import static com.github.toy.constructor.core.api.ToGetConditionalHelper.getFromIterable;
import static java.lang.String.format;
import static java.util.Optional.ofNullable;

public final class SequentialSearchSupplier<R extends SearchContext>
        extends SequentialGetSupplier<SeleniumSteps, R, SearchContext, SequentialSearchSupplier<R>> {

    private final Function<SearchContext, List<R>>  searching;
    private final Predicate<R> condition;

    private SequentialSearchSupplier(Function<SearchContext, List<R>> searching, Predicate<R> condition) {
        this.searching = searching;
        this.condition = condition;
    }

    /**
     * Constructs the chained searching from some instance of {@link SearchContext}.
     *
     * @param from is how to find some element from a parent element.
     * @return self-reference
     */
    public SequentialSearchSupplier<R> from(SequentialSearchSupplier<? extends SearchContext> from) {
        return super.from(from);
    }

    /**
     * Constructs the chained searching from some instance of {@link SearchContext}.
     *
     * @param from is a parent element.
     * @param <Q> is a type of the parent element.
     * @return self-reference
     */
    @Override
    public <Q extends SearchContext> SequentialSearchSupplier<R> from(Q from) {
        return super.from(from);
    }

    /**
     * Constructs the chained searching from some instance of {@link SearchContext}.
     *
     * @param child is how to find a child element.
     * @param <Q> is a type of the child element.
     * @return the instance of {@link SequentialSearchSupplier} which is given as the parameter.
     * But it wraps the new constructed chained searching.
     */
    public <Q extends SearchContext> SequentialSearchSupplier<Q> child(SequentialSearchSupplier<Q> child) {
        return child.from(this);
    }

    /**
     * Constructs the chained searching from some instance of {@link SearchContext}.
     *
     * @param children is how to find a list of child elements.
     * @param <Q> is a type of the child element.
     * @return the instance of {@link SequentialMultipleSearchSupplier} which is given as the parameter.
     * But it wraps the new constructed chained searching.
     */
    public <Q extends SearchContext> SequentialMultipleSearchSupplier<Q> child(SequentialMultipleSearchSupplier<Q> children) {
        return children.from(this);
    }

    @Override
    public Function<SeleniumSteps, R> get() {
        return ofNullable(super.get())
                .orElseGet(() -> {
            Function<SearchContext, R> endFunction = getEndFunction();
            return toGet(endFunction.toString(), seleniumSteps ->
                    endFunction.apply(seleniumSteps.getWrappedDriver()));
        });
    }

    @Override
    protected Function<SearchContext, R> getEndFunction() {
        Function<SearchContext, R> resultFunction = getFromIterable("A single item",
                searching,
                condition, true,
                true);
        return toGet(resultFunction.toString(), searchContext ->
                ofNullable(resultFunction.apply(searchContext)).orElseThrow(() ->
                        new NoSuchElementException(format("Nothing was found. Attempt to get: %s. Condition: %s",
                                searching,
                                condition))));
    }
}
