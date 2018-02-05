package com.github.toy.constructor.selenium.functions.searching;

import com.github.toy.constructor.core.api.GetSupplier;
import com.github.toy.constructor.core.api.SequentialGetSupplier;
import com.github.toy.constructor.selenium.SeleniumSteps;
import org.openqa.selenium.SearchContext;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

import static com.github.toy.constructor.core.api.StoryWriter.toGet;
import static com.github.toy.constructor.core.api.ToGetConditionalHelper.getSubIterable;
import static java.util.Optional.ofNullable;

public final class SequentialMultipleSearchSupplier<R extends SearchContext>
        extends SequentialGetSupplier<SeleniumSteps, List<R>, SearchContext, SequentialMultipleSearchSupplier<R>> {
    
    private final Function<SearchContext, List<R>>  searching;
    private final Predicate<R> condition;

    SequentialMultipleSearchSupplier(Function<SearchContext, List<R>> searching,
                                     Predicate<R> condition) {
        this.searching = searching;
        this.condition = condition;
    }

    @Override
    protected SequentialMultipleSearchSupplier<R> from(GetSupplier<SeleniumSteps, SearchContext, ?> supplier) {
        return super.from(supplier);
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
        return getSubIterable(searching.toString(),
                searching,
                condition, true,
                false);
    }
}
