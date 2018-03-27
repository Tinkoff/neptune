package com.github.toy.constructor.selenium.functions.searching;

import com.github.toy.constructor.core.api.SequentialGetSupplier;
import com.github.toy.constructor.selenium.SeleniumSteps;
import org.openqa.selenium.SearchContext;

import java.util.function.Function;

import static com.github.toy.constructor.core.api.StoryWriter.toGet;
import static com.google.common.base.Preconditions.checkArgument;
import static java.util.Optional.ofNullable;

public final class SequentialSearchSupplier<R extends SearchContext>
        extends SequentialGetSupplier<SeleniumSteps, R, SearchContext, SequentialSearchSupplier<R>> {

    private final SearchSupplier<R> toFind;
    private Function<SearchContext, ? extends SearchContext> chain;

    private SequentialSearchSupplier(SearchSupplier<R> toFind) {
        this.toFind = toFind;
    }

    public static <R extends SearchContext> SequentialSearchSupplier<R> element(SearchSupplier<R> toFind) {
        checkArgument(toFind != null, "It is necessary to define what to find");
        return new SequentialSearchSupplier<>(toFind);
    }

    /**
     * Constructs the chained searching from some instance of {@link SearchContext}.
     *
     * @param from is how to find some element from a parent element.
     * @return self-reference
     */
    public <Q extends SearchContext> SequentialSearchSupplier<R> foundFrom(SearchSupplier<Q> from) {
        checkArgument(from != null, "The searching for the parent element should be defined");
        if (chain != null) {
            chain = from.get().andThen(chain);
        }
        else {
            chain = from.get();
        }
        return super.from(toGet(chain.toString(),
                seleniumSteps -> chain.apply(seleniumSteps.getWrappedDriver())));
    }

    /**
     * Constructs the chained searching from some instance of {@link SearchContext}.
     *
     * @param from is a parent element.
     * @param <Q> is a type of the parent element.
     * @return self-reference
     */
    public <Q extends SearchContext> SequentialSearchSupplier<R> foundFrom(Q from) {
        checkArgument(from != null, "The parent element should be defined");
        return super.from(from);
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
        return toFind.get();
    }
}
