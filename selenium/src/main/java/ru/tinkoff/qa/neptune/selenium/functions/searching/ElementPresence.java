package ru.tinkoff.qa.neptune.selenium.functions.searching;

import org.openqa.selenium.SearchContext;
import ru.tinkoff.qa.neptune.core.api.steps.Presence;
import ru.tinkoff.qa.neptune.core.api.steps.StepFunction;
import ru.tinkoff.qa.neptune.selenium.SeleniumStepContext;
import org.openqa.selenium.NoSuchElementException;

import java.util.function.Function;

import static ru.tinkoff.qa.neptune.selenium.CurrentContentFunction.currentContent;

public final class ElementPresence extends Presence<SeleniumStepContext, ElementPresence> {

    private ElementPresence(Function<SeleniumStepContext, ?> toBePresent) {
        super(toBePresent);
    }

    /**
     * Creates an instance of {@link ElementPresence}.
     *
     * @param supplier supplier of a search criteria to find a single element.
     * @return an instance of {@link ElementPresence}.
     */
    @SuppressWarnings("unchecked")
    public static ElementPresence presenceOfAnElement(SearchSupplier<?> supplier) {
        StepFunction<SearchContext, ?> f = (StepFunction<SearchContext, ?>) supplier.get();
        f.addIgnored(NoSuchElementException.class);
        return new ElementPresence(f.compose(currentContent()));
    }

    /**
     * Creates an instance of {@link ElementPresence}.
     *
     * @param supplier supplier of a search criteria to find a list of elements.
     * @return an instance of {@link ElementPresence}.
     */
    public static ElementPresence presenceOfElements(MultipleSearchSupplier<?> supplier) {
        return new ElementPresence(supplier.get().compose(currentContent()));
    }
}
