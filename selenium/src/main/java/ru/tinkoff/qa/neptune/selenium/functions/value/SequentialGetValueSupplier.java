package ru.tinkoff.qa.neptune.selenium.functions.value;

import org.openqa.selenium.SearchContext;
import ru.tinkoff.qa.neptune.core.api.event.firing.annotation.MakeCaptureOnFinishing;
import ru.tinkoff.qa.neptune.core.api.steps.SequentialGetStepSupplier;
import ru.tinkoff.qa.neptune.selenium.SeleniumStepContext;
import ru.tinkoff.qa.neptune.selenium.api.widget.HasValue;
import ru.tinkoff.qa.neptune.selenium.functions.searching.SearchSupplier;

import static com.google.common.base.Preconditions.checkArgument;
import static java.util.Objects.nonNull;
import static ru.tinkoff.qa.neptune.selenium.CurrentContentFunction.currentContent;

@SequentialGetStepSupplier.DefaultParameterNames(
        from = "Element to get value"
)
@MakeCaptureOnFinishing(typeOfCapture = Object.class)
public final class SequentialGetValueSupplier<T> extends
        SequentialGetStepSupplier.GetObjectChainedStepSupplier<SeleniumStepContext, T, HasValue<T>, SequentialGetValueSupplier<T>> {

    private SequentialGetValueSupplier() {
        super("Value", HasValue::getValue);
    }

    /**
     * Builds a function which takes a value from some element and returns the value.
     * @param from is how to find the element which has value
     * @param <T> is the type of a value to be returned
     * @param <R> is the type of an element which has value
     * @return the function which takes a value from some element and returns the value.
     */
    public static <T, R extends SearchContext & HasValue<T>> SequentialGetValueSupplier<T> ofThe(
            SearchSupplier<R> from) {
        checkArgument(nonNull(from), "The searching for the element which has value should be defined");
        return new SequentialGetValueSupplier<T>().from(from.get().compose(currentContent()));
    }

    /**
     * Builds a function which takes a value from some element and returns the value.
     * @param from is the element which has value
     * @param <T> is the type of a value to be returned
     * @param <R> is the type of an element which has value
     * @return the function which takes a value from some element and returns the value.
     */
    public static <T, R extends SearchContext & HasValue<T>> SequentialGetValueSupplier<T> ofThe(R from) {
        checkArgument(nonNull(from), "The element which has value should be defined");
        return new SequentialGetValueSupplier<T>().from(from);
    }
}
