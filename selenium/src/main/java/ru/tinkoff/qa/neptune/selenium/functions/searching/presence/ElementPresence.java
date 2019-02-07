package ru.tinkoff.qa.neptune.selenium.functions.searching.presence;

import org.openqa.selenium.SearchContext;
import ru.tinkoff.qa.neptune.core.api.steps.Presence;
import ru.tinkoff.qa.neptune.core.api.steps.StepFunction;
import ru.tinkoff.qa.neptune.selenium.SeleniumStepContext;
import ru.tinkoff.qa.neptune.selenium.functions.searching.MultipleSearchSupplier;
import ru.tinkoff.qa.neptune.selenium.functions.searching.SearchSupplier;
import org.openqa.selenium.NoSuchElementException;

import java.util.function.Function;

import static ru.tinkoff.qa.neptune.selenium.CurrentContentFunction.currentContent;
import static java.util.List.of;

public final class ElementPresence extends Presence<SeleniumStepContext> {

    private ElementPresence(Function<SeleniumStepContext, ?> toBePresent) {
        super(toBePresent);
    }

    /**
     * Creates an instance of {@link Presence}.
     *
     * @param supplier supplier of a search criteria to find a single element.
     * @return an instance of {@link Presence}.
     */
    public static Presence<SeleniumStepContext> presenceOfAnElement(SearchSupplier<?> supplier) {
        StepFunction<SearchContext, ?> f = (StepFunction<SearchContext, ?>) supplier.get();
        f.addIgnored(of(NoSuchElementException.class));
        return new ElementPresence(f.compose(currentContent()));
    }

    /**
     * Creates an instance of {@link Presence}.
     *
     * @param supplier supplier of a search criteria to find a list of elements.
     * @return an instance of {@link Presence}.
     */
    public static Presence<SeleniumStepContext> presenceOfElements(MultipleSearchSupplier<?> supplier) {
        return new ElementPresence(supplier.get().compose(currentContent()));
    }



}
