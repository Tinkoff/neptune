package ru.tinkoff.qa.neptune.selenium.functions.value;

import ru.tinkoff.qa.neptune.core.api.SequentialGetStepSupplier;
import ru.tinkoff.qa.neptune.selenium.SeleniumSteps;
import ru.tinkoff.qa.neptune.selenium.api.widget.HasValue;
import ru.tinkoff.qa.neptune.selenium.functions.searching.SearchSupplier;
import org.openqa.selenium.SearchContext;

import java.util.function.Function;

import static ru.tinkoff.qa.neptune.core.api.StoryWriter.toGet;
import static ru.tinkoff.qa.neptune.selenium.CurrentContentFunction.currentContent;
import static com.google.common.base.Preconditions.checkArgument;

public final class SequentialGetValueSupplier<T> extends
        SequentialGetStepSupplier<SeleniumSteps, T, HasValue<T>, SequentialGetValueSupplier<T>> {

    private SequentialGetValueSupplier() {
        super();
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
        checkArgument(from != null, "The searching for the element which has value should be defined");
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
        checkArgument(from != null, "The element which has value should be defined");
        return new SequentialGetValueSupplier<T>().from(from);
    }

    @Override
    protected Function<HasValue<T>, T> getEndFunction() {
        return toGet("Value", HasValue::getValue);
    }
}
