package com.github.toy.constructor.selenium.functions.searching;

import com.github.toy.constructor.core.api.SequentialGetSupplier;
import com.github.toy.constructor.selenium.SeleniumSteps;
import org.openqa.selenium.SearchContext;

import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

import static com.github.toy.constructor.core.api.StoryWriter.condition;
import static com.github.toy.constructor.core.api.StoryWriter.toGet;
import static com.github.toy.constructor.selenium.functions.searching.SearchSupplier.item;
import static java.util.Optional.ofNullable;

public final class SequentialMultipleSearchSupplier<R extends SearchContext>
        extends SequentialGetSupplier<SeleniumSteps, List<R>, SearchContext, SequentialMultipleSearchSupplier<R>> {
    
    private final MultipleSearchSupplier<R> toFind;
    private Function<SearchContext, ? extends SearchContext> chain;

    private SequentialMultipleSearchSupplier(MultipleSearchSupplier<R> toFind) {
        this.toFind = toFind;
    }

    public static <R extends SearchContext> SequentialMultipleSearchSupplier<R> elements(MultipleSearchSupplier<R> toFind) {
        return new SequentialMultipleSearchSupplier<>(toFind);
    }

    /**
     * Constructs the chained searching from some instance of {@link SearchContext}.
     *
     * @param from is how to find some elements from a parent element.
     * @return self-reference
     */
    public <Q extends SearchContext> SequentialMultipleSearchSupplier<R> from(Supplier<Function<SearchContext, Q>> from) {
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
    @Override
    public <Q extends SearchContext> SequentialMultipleSearchSupplier<R> from(Q from) {
        return from(item(toGet(from.toString(), q -> List.of(from)),
                condition("as is", q -> true)));
    }

    @Override
    public Function<SeleniumSteps, List<R>> get() {
        return ofNullable(super.get())
                .orElseGet(() -> {
                    Function<SearchContext, List<R>> endFunction = getEndFunction();
                    return toGet(endFunction.toString(), seleniumSteps ->
                            endFunction.apply(seleniumSteps.getWrappedDriver()));
                });
    }

    @Override
    protected Function<SearchContext, List<R>> getEndFunction() {
        return toFind.get();
    }
}
