package ru.tinkoff.qa.neptune.selenium.functions.value;

import org.openqa.selenium.SearchContext;
import ru.tinkoff.qa.neptune.core.api.steps.SequentialGetStepSupplier;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.DescriptionFragment;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.IncludeParamsOfInnerGetterStep;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.MaxDepthOfReporting;
import ru.tinkoff.qa.neptune.selenium.api.widget.HasValue;
import ru.tinkoff.qa.neptune.selenium.functions.searching.SearchSupplier;

import static com.google.common.base.Preconditions.checkArgument;
import static java.util.Objects.nonNull;

@Description("Value of {element}")
@MaxDepthOfReporting(0)
@IncludeParamsOfInnerGetterStep
public final class SequentialGetValueSupplier<T> extends
        SequentialGetStepSupplier.GetObjectChainedStepSupplier<Object, T, HasValue<T>, SequentialGetValueSupplier<T>> {

    @DescriptionFragment("element")
    final Object element;

    private SequentialGetValueSupplier(Object element) {
        super(HasValue::getValue);
        this.element = element;
    }

    private <R extends SearchContext & HasValue<T>> SequentialGetValueSupplier(SearchSupplier<R> from) {
        this((Object) from);
        from(from);
    }

    private <R extends SearchContext & HasValue<T>> SequentialGetValueSupplier(R from) {
        this((Object) from);
        from(from);
    }

    /**
     * Builds a function which takes a value from some element and returns the value.
     *
     * @param from is how to find the element which has value
     * @param <T>  is the type of a value to be returned
     * @param <R>  is the type of an element which has value
     * @return the function which takes a value from some element and returns the value.
     */
    public static <T, R extends SearchContext & HasValue<T>> SequentialGetValueSupplier<T> ofThe(SearchSupplier<R> from) {
        checkArgument(nonNull(from), "The searching for the element which has value should be defined");
        return new SequentialGetValueSupplier<T>(from);
    }

    /**
     * Builds a function which takes a value from some element and returns the value.
     *
     * @param from is the element which has value
     * @param <T>  is the type of a value to be returned
     * @param <R>  is the type of an element which has value
     * @return the function which takes a value from some element and returns the value.
     */
    public static <T, R extends SearchContext & HasValue<T>> SequentialGetValueSupplier<T> ofThe(R from) {
        return new SequentialGetValueSupplier<T>(from);
    }
}
