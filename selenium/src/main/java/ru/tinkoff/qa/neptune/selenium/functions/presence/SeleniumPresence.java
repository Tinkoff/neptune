package ru.tinkoff.qa.neptune.selenium.functions.presence;

import ru.tinkoff.qa.neptune.core.api.Presence;
import ru.tinkoff.qa.neptune.selenium.SeleniumSteps;
import ru.tinkoff.qa.neptune.selenium.functions.searching.MultipleSearchSupplier;
import ru.tinkoff.qa.neptune.selenium.functions.searching.SearchSupplier;
import org.openqa.selenium.NoSuchElementException;

import java.util.function.Function;

import static ru.tinkoff.qa.neptune.selenium.CurrentContentFunction.currentContent;
import static java.util.List.of;

public final class SeleniumPresence extends Presence<SeleniumSteps> {

    private SeleniumPresence(Function<SeleniumSteps, ?> toBePresent) {
        super(toBePresent);
    }

    /**
     * Creates an instance of {@link Presence}.
     *
     * @param supplier supplier of a search criteria to find a single element.
     * @return an instance of {@link Presence}.
     */
    public static Presence<SeleniumSteps> presenceOfAnElement(SearchSupplier<?> supplier) {
        return new SeleniumPresence(supplier.get().compose(currentContent()))
                .addIgnored(of(NoSuchElementException.class));
    }

    /**
     * Creates an instance of {@link Presence}.
     *
     * @param supplier supplier of a search criteria to find a list of elements.
     * @return an instance of {@link Presence}.
     */
    public static Presence<SeleniumSteps> presenceOfElements(MultipleSearchSupplier<?> supplier) {
        return new SeleniumPresence(supplier.get().compose(currentContent()));
    }



}
