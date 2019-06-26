package ru.tinkoff.qa.neptune.selenium.functions.searching;

import ru.tinkoff.qa.neptune.core.api.steps.Absence;
import ru.tinkoff.qa.neptune.selenium.SeleniumStepContext;

import java.util.function.Function;

import static java.time.Duration.ofMillis;
import static ru.tinkoff.qa.neptune.selenium.CurrentContentFunction.currentContent;

public final class ElementAbsence extends Absence<SeleniumStepContext, ElementAbsence> {

    private ElementAbsence(Function<SeleniumStepContext, ?> toBeAbsent) {
        super(toBeAbsent);
    }

    /**
     * Creates an instance of {@link ElementAbsence}.
     *
     * @param supplier supplier of a search criteria to find a single element. This element is expected to be absent.
     * @return an instance of {@link ElementAbsence}.
     */
    @SuppressWarnings("unchecked")
    public static ElementAbsence absenceOfAnElement(SearchSupplier<?> supplier) {
        return new ElementAbsence(supplier.clone().timeOut(ofMillis(0)).get().compose(currentContent()));
    }

    /**
     * Creates an instance of {@link ElementAbsence}.
     *
     * @param supplier supplier of a search criteria to find a list of elements. These elements are expected to be absent.
     * @return an instance of {@link ElementAbsence}.
     */
    public static ElementAbsence absenceOfElements(MultipleSearchSupplier<?> supplier) {
        return new ElementAbsence(supplier.clone().timeOut(ofMillis(0)).get().compose(currentContent()));
    }
}
